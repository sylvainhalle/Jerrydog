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

import com.sun.net.httpserver.HttpExchange;

public class HelloCallback extends RequestCallback 
{

	@Override
	public boolean fire(HttpExchange t) 
	{
		String path = t.getRequestURI().getPath();
		if (path.compareTo("/hello") == 0)
		{
			return true;
		}
		return false;
		
	}

	@Override
	public CallbackResponse process(HttpExchange t) 
	{
		CallbackResponse cbr = new CallbackResponse(t);
		cbr.setCode(CallbackResponse.HTTP_OK).setContents("Hi");
		return cbr;
	}

}
