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
   * Whether caching is enabled
   */
  protected boolean m_cachingEnabled = true;
  
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
   * Enables or disables the use of caching
   * @param b Set to {@code true} to enable caching, {@code false} otherwise
   */
  public void setCachingEnabled(boolean b)
  {
    m_cachingEnabled = b;
  }

  @Override
  public CallbackResponse process(HttpExchange t)
  {
    URI u = t.getRequestURI();
    String path = u.getPath();
    if (!m_cachingEnabled || !m_served.contains(path))
    {
      m_served.add(path);
      return m_callback.process(t);
    }
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
