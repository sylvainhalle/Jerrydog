/*
    Jerrydog, a lightweight web application server in Java
    Copyright (C) 2015-2016 Sylvain Hallé

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
import java.util.Iterator;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

/**
 * Representation of an HTTP request/response cookie.
 * See <a href="http://en.wikipedia.org/wiki/HTTP_cookie">http://en.wikipedia.org/wiki/HTTP_cookie</a>.
 */
public class Cookie
{
  /**
   * The cookie's name
   */
  protected final String m_name;
  
  /**
   * The cookie's value
   */
  protected final String m_value;
  
  /**
   * Instantiates a cookie
   * @param name The cookie's name
   * @param value The cookie's value
   */
  public Cookie(String name, String value)
  {
    super();
    m_name = name;
    m_value = value;
  }
  
  /**
   * Instantiates a cookie from the Cookie field of an HttpExchange instance
   * If there is no cookie with this name, the value will be an empty string.
   * @param t The HttpExchange instance
   * @param name The name of the cookie to extract
   */
  public Cookie(HttpExchange t, String name)
  {
	super();
	m_name = name;
	
	//Extract cookie from header
	List<String> cookies = t.getRequestHeaders().get("Cookie");
	String cookie = "";
    int cookieIndex = -1;
    int cookieIndexEnd = -1;
    for(Iterator<String> i = cookies.iterator(); i.hasNext();)
    {
      cookie = i.next();
      cookieIndex = cookie.indexOf(name);
      if(cookieIndex != -1)
      {
        //Skip "<name>=" to get to the character after "="
        cookieIndex = cookieIndex + name.length() + 1;
        break;
      }
    }
    if(cookieIndex != -1)
    {
      cookie = cookie.substring(cookieIndex);
      cookieIndexEnd = cookie.indexOf(';');
      if(cookieIndexEnd != -1)
      {
        cookie = cookie.substring(0, cookieIndexEnd);
      }
      
      m_value = cookie;
    }
    else 
    {
      m_value = "";
    }
  }
  
  /**
   * Retrieves the cookie's name
   * @return The name
   */
  public String getName()
  {
    return m_name;
  }
  
  /**
   * Retrieves the cookie's value
   * @return The value
   */
  public String getValue()
  {
    return m_value;
  }
}
