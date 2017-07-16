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

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

/**
 * REST callback for "clean" URLs, i.e. URLs whose parameters are
 * inside the path, instead of the parameters section of the URL.
 * For example, an URL like <code>page?id=3&amp;section=index</code> would
 * be remplaced in a clean URL by something like
 * <code>page/3/index</code>. Here, <code>page</code> is the path,
 * and the rest of the URL are actually parameters.
 * 
 * @author Sylvain Hallé
 */
public abstract class RestCleanCallback extends RestCallback
{
	public RestCleanCallback(Method m, String path)
	{
		super(m, path);
	}

	@Override
	public boolean fire(HttpExchange t)
	{
		URI u = t.getRequestURI();
		String path = u.getPath();
		String method = t.getRequestMethod();
		return ((m_ignoreMethod || method.compareToIgnoreCase(methodToString(m_method)) == 0)) 
				&& (path.compareTo(m_path) == 0 || path.startsWith(m_path + "/"));
	}
	
	@Override
	public Map<String,String> getParameters(HttpExchange t)
	{
		String data = null;
		Map<String,String> params = null;
		URI u = t.getRequestURI();
		String path = u.getPath();
		path = path.substring(m_path.length());
		if (path.startsWith("/"))
		{
			// Remove first slash
			path = path.substring(1);
		}
		if (m_method == Method.GET)
		{
			// Read GET data			
			data = u.getQuery();
			params = Server.queryToMap(data, m_method);
			params.put("", path);
		}
		else
		{
			// Read POST data
			InputStream is_post = t.getRequestBody();
			data = Server.streamToString(is_post);
			params = Server.queryToMap(data, m_method);
			params.put("", path);
		}
		return params;
	}
}
