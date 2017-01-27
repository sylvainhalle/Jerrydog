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

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import ca.uqac.lif.jerrydog.RequestCallback.Method;

public class RestCallbackTest 
{
	@Test
	public void testQueryToMap1()
	{
		String query = "p=v&q";
		Map<String,String> map = Server.queryToMap(query, Method.GET);
		assertNotNull(map);
		assertEquals(2, map.keySet().size());
	}
	
	@Test
	public void testQueryToMap2()
	{
		String query = "p";
		Map<String,String> map = Server.queryToMap(query, Method.GET);
		assertNotNull(map);
		assertEquals(1, map.keySet().size());
	}
	
	@Test
	public void testQueryToMap3()
	{
		String query = "";
		Map<String,String> map = Server.queryToMap(query, Method.GET);
		assertNotNull(map);
		assertEquals(1, map.keySet().size());
		assertTrue(map.containsKey(""));
	}
	
	@Test
	public void testQueryToMap4()
	{
		String query = "p&q&r";
		Map<String,String> map = Server.queryToMap(query, Method.GET);
		assertNotNull(map);
		assertEquals(3, map.keySet().size());
	}
	
	@Test
	public void testQueryToMap5()
	{
		String query = "p&q=2&r";
		Map<String,String> map = Server.queryToMap(query, Method.GET);
		assertNotNull(map);
		assertEquals(3, map.keySet().size());
	}
}
