/*
    Jerrydog, a lightweight web application server in Java
    Copyright (C) 2015-2020 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.jerrydog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class CallbackResponse
{
	/**
	 * Common HTTP response codes
	 */
	public static final int HTTP_OK = 200;
	public static final int HTTP_REDIRECT = 303;
	public static final int HTTP_NOT_MODIFIED = 304;
	public static final int HTTP_BAD_REQUEST = 400;
	public static final int HTTP_NOT_FOUND = 404;
	public static final int HTTP_INTERNAL_SERVER_ERROR = 500;

	/**
	 * The content type of the response
	 */
	public static enum ContentType {JSON, TEXT, XML, PNG, JS, HTML, CSS, JPEG, PDF, ZIP, SVG, GIF, LATEX, DOT, OCTET_STREAM};

	/**
	 * The HTTP exchange containing the response headers
	 */
	protected HttpExchange m_exchange;

	/**
	 * The response code
	 */
	protected int m_responseCode;

	/**
	 * The response headers
	 */
	protected Map<String,String> m_headers;

	/**
	 * The response contents
	 */
	protected byte[] m_contents = null;

	public CallbackResponse(HttpExchange t)
	{
		this(t, HTTP_OK, "", "");
	}

	public CallbackResponse(HttpExchange t, int response_code, String contents, String content_type)
	{
		this(t, response_code, contents.getBytes(), content_type);
	}

	public CallbackResponse(HttpExchange t, int response_code, String contents, ContentType type)
	{
		this(t, response_code, contents.getBytes(), getContentTypeString(type));
	}

	public CallbackResponse(HttpExchange t, int response_code, byte[] contents, String content_type)
	{
		super();
		m_exchange = t;
		m_responseCode = response_code;
		m_contents = contents;
		m_headers = new HashMap<String,String>();
		setContentType(content_type);
	}

	/**
	 * Get the HTTP exchange
	 * @return The exchange
	 */
	public HttpExchange getExchange()
	{
		return m_exchange;
	}

	/**
	 * Disables the client-side caching in the HTTP response to be sent
	 * @return This response 
	 */
	public CallbackResponse disableCaching()
	{
		Headers h = m_exchange.getResponseHeaders();
		h.add("Pragma", "no-cache");
		h.add("Cache-Control", "no-cache, no-store, must-revalidate");
		h.add("Expires", "0");
		return this;
	}
	
	/**
	 * Enables client-side caching in the HTTP response to be sent
	 * @param duration The time, in seconds, that the browser can keep the
	 * contents of the response in its cache
	 * @return This response
	 */
	public CallbackResponse enableCaching(int duration)
	{
		Headers h = m_exchange.getResponseHeaders();
		h.add("Cache-Control", "private, max-age=" + duration);
		return this;
	}

	/**
	 * Sets the HTTP response code
	 * @param code The code
	 * @return This response
	 */
	public CallbackResponse setCode(int code)
	{
		m_responseCode = code;
		return this;
	}

	/**
	 * Gets the HTTP response code
	 * @return The code
	 */
	public int getCode()
	{
		return m_responseCode;
	}

	/**
	 * Sets the response contents
	 * @param contents A string with the response contents
	 * @return This response
	 */
	public CallbackResponse setContents(String contents)
	{
		m_contents = contents.getBytes();
		return this;
	}

	/**
	 * Sets the response contents
	 * @param contents An array of bytes with the response contents
	 * @return This response
	 */
	public CallbackResponse setContents(byte[] contents)
	{
		m_contents = contents;
		return this;
	}

	/**
	 * Gets the response contents
	 * @return An array of bytes with the response contents
	 */
	public byte[] getContents()
	{
		return m_contents;
	}

	/**
	 * Sets the response's content type
	 * @param t The content type
	 * @return This response
	 */
	public CallbackResponse setContentType(ContentType t)
	{
		setContentType(getContentTypeString(t));
		return this;
	}

	/**
	 * Sets the response's content type
	 * @param mime The MIME type
	 * @return This response
	 */
	public CallbackResponse setContentType(String mime)
	{
		m_headers.put("Content-Type", mime);
		return this;
	}

	/**
	 * Sets the response as an attachment to be downloaded
	 * @param filename The filename
	 * @return This response
	 */
	public CallbackResponse setAttachment(String filename)
	{
		setHeader("Content-Disposition", "attachment; filename=" + filename);
		return this;
	}

	/**
	 * Sets a response header
	 * @param name The parameter name
	 * @param value The parameter value
	 * @return This response
	 */
	public CallbackResponse setHeader(String name, String value)
	{
		m_headers.put(name, value);
		return this;
	}

	/**
	 * Gets the headers of this response
	 * @return The headers
	 */
	public Map<String,String> getHeaders()
	{
		return m_headers;
	}

	/**
	 * Retrieves the response's content type
	 * @return The content type
	 */
	public String getContentType()
	{
		return m_headers.get("Content-Type");
	}

	/**
	 * Converts a type into a MIME string
	 * @param t The content type
	 * @return The corresponding MIME string
	 */
	public static String getContentTypeString(ContentType t)
	{
		String out = "";
		switch (t)
		{
		case HTML:
			out = "text/html";
			break;
		case CSS:
			out = "text/css";
			break;
		case JSON:
			out = "application/json";
			break;
		case XML:
			out = "application/xml";
			break;
		case TEXT:
			out = "text/plain";
			break;
		case PNG:
			out = "image/png";
			break;
		case GIF:
			out = "image/gif";
			break;
		case JPEG:
			out = "image/jpeg";
			break;
		case JS:
			out = "application/javascript";
			break;
		case PDF:
			out = "application/pdf";
			break;
		case ZIP:
			out = "application/zip";
			break;
		case SVG:
			out = "image/svg+xml";
			break;
		case LATEX:
			out = "application/x-latex";
			break;
		case DOT:
			out = "application/x-dot";
			break;
		case OCTET_STREAM:
			out = "application/octet-stream";
			break;
		}
		return out;
	}

	/**
	 * Add a cookie to the response
	 * @param c The cookie to add
	 * @return This response
	 */
	public CallbackResponse addResponseCookie(Cookie c)
	{
		Headers h = m_exchange.getResponseHeaders();
		h.add("Set-Cookie", c.getName() + "=" + c.getValue());
		return this;
	}

	/**
	 * Add multiple cookies to the response
	 * @param t The HTTP exchange
	 * @param cookies The cookies to add
	 * @return This response
	 */
	public CallbackResponse addResponseCookies(HttpExchange t, Collection<Cookie> cookies)
	{
		for (Cookie c : cookies)
		{
			addResponseCookie(c);
		}
		return this;
	}

	/**
	 * Converts an input stream to a string. This is useful to convert
	 * the HTTP request body into a string.
	 * @param is The input stream
	 * @return The string
	 */
	public static String convertStreamToString(InputStream is) 
	{
		Scanner s = new Scanner(is);
		s.useDelimiter("\\A");
		String out = s.hasNext() ? s.next() : "";
		s.close();
		try 
		{
			is.close();
		} 
		catch (IOException e) 
		{
			// Do nothing
		}
		return out;
	}
}
