/*
    Jerrydog, a lightweight web application server in Java
    Copyright (C) 2015-2017 Sylvain Hallé

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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

/**
 * A concrete implementation of the {@code HttpExchange} class, allowing a user
 * to manually create HTTP requests to send to the server. This class is an
 * "empty shell" whose methods either do nothing or return {@code null}. It is up
 * to the user to override some of these methods with something meaningful.
 * @author Sylvain Hallé
 */
public class EmptyHttpExchange extends HttpExchange
{
protected final ByteArrayOutputStream m_responseBody;
	
	protected int m_responseCode = 0;
	
	protected URI m_requestUri;
	
	public EmptyHttpExchange()
	{
		super();
		m_responseBody = new ByteArrayOutputStream();;
	}
	
	public EmptyHttpExchange(String request)
	{
		super();
		try 
		{
			m_requestUri = new URI(request);
		}
		catch (URISyntaxException e) 
		{
			// Do nothing
			e.printStackTrace();
		}
		m_responseBody = new ByteArrayOutputStream();;
	}
	
	@Override
	public URI getRequestURI()
	{
		return m_requestUri;
	}
	
	@Override
	public void sendResponseHeaders(int arg0, long arg1)
	{
		m_responseCode = arg0;
	}
	
	@Override
	public OutputStream getResponseBody()
	{
		return m_responseBody;
	}
	
	@Override
	public Headers getRequestHeaders()
	{
		return new Headers();
	}
	
	@Override
	public Headers getResponseHeaders()
	{
		return new Headers();
	}
	
	@Override
	public int getResponseCode()
	{
		return m_responseCode;
	}
	
	public String getResponseString()
	{
		return new String(m_responseBody.toByteArray());
	}

	@Override
	public void close() 
	{
		// Do nothing
	}

	@Override
	public Object getAttribute(String arg0)
	{
		return null;
	}

	@Override
	public HttpContext getHttpContext() 
	{
		return null;
	}

	@Override
	public InetSocketAddress getLocalAddress() 
	{
		return null;
	}

	@Override
	public HttpPrincipal getPrincipal() 
	{
		return null;
	}

	@Override
	public String getProtocol() 
	{
		return null;
	}

	@Override
	public InetSocketAddress getRemoteAddress() 
	{
		return null;
	}

	@Override
	public InputStream getRequestBody() 
	{
		return null;
	}

	@Override
	public String getRequestMethod() 
	{
		return null;
	}

	@Override
	public void setAttribute(String arg0, Object arg1) 
	{
		// Do nothing
	}
	
	@Override
	public void setStreams(InputStream arg0, OutputStream arg1) 
	{
		// Do nothing	
	}
}
