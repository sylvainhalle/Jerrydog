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

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

/**
 * Like a normal RequestCallback, except that it will return an HTTP 304
 * ("Not Modified") response every time except the first time it is
 * called
 * @author Sylvain Hallé
 *
 */
public class CachedRequestCallback extends RequestCallback
{
  /**
   * Whether the page has been already served
   */
  protected Set<String> m_served;
  
  /**
   * Whether caching is enabled on the server side; this means that the
   * server can answer a {@code 304} ("Not Modified") code to a client that
   * requests a ressource it has already requested in the past.
   */
  protected boolean m_serverCachingEnabled = true;
  
  /**
   * The time during which a client is allowed to keep the response of a
   * request in its local cache before requesting it again.
   */
  protected int m_clientCachingInterval = 0;
  
  /**
   * The callback this object wraps around
   */
  protected final RequestCallback m_callback;
  
  /**
   * Creates a new cached request callback from an existing callback
   * @param callback The callback that will be cached
   */
  public CachedRequestCallback(RequestCallback callback)
  {
    super();
    m_served = new HashSet<String>();
    m_callback = callback;
  }
  
  @Override
  public boolean fire(HttpExchange t)
  {
	  return m_callback.fire(t);
  }
  
  /**
   * Enables or disables the use of caching  on the server side; this means that the
   * server can answer a {@code 304} ("Not Modified") code to a client that
   * requests a ressource it has already requested in the past.
   * @param b Set to {@code true} to enable caching, {@code false} otherwise
   */
  public void setCachingEnabled(boolean b)
  {
    m_serverCachingEnabled = b;
  }

  /**
   * Sets the time during which a client is allowed to keep the response of a
   * request in its local cache before requesting it again.
   * @param interval The interval, in seconds
   */
  public void setCachingInterval(int interval)
  {
	  if (interval >= 0)
	  {
		  m_clientCachingInterval = interval;
	  }
	  else
	  {
		  m_clientCachingInterval = 0;
	  }
  }

  @Override
  public CallbackResponse process(HttpExchange t)
  {
    URI u = t.getRequestURI();
    String path = u.getPath();
    Headers h = t.getRequestHeaders();
    if (!m_serverCachingEnabled || !m_served.contains(path) || !h.containsKey("If-Modified-Since"))
    {
      m_served.add(path);
      CallbackResponse response = m_callback.process(t);
      if (m_clientCachingInterval > 0)
      {
    	  response.enableCaching(m_clientCachingInterval);
      }
      return response;
    }
    // We get here only if 1. caching is enabled; 2. path has already been served;
    // 3. browser says it has it in cache
    CallbackResponse out = new CallbackResponse(t, CallbackResponse.HTTP_NOT_MODIFIED, "", "");
    return out;
  }
  
  /**
   * Resets the memory of pages that have been already cached
   */
  public void reset()
  {
    m_served.clear();
  }  
}
