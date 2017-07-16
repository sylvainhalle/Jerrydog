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

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import ca.uqac.lif.jerrydog.CachedCallbackTest.FakeRequest;

public class RestCleanCallbackTest 
{	
	@Test
	public void testCleanRequest1() throws IOException
	{
		Server server = new Server();
		HelloCleanCallback cb = new HelloCleanCallback("/foo");
		server.registerCallback(cb);
		FakeRequest he = new FakeRequest("/foo");
		server.handle(he);
		Map<String,String> map = cb.getLastParameters();
		assertNotNull(map);
		assertEquals(1, map.size());
		assertEquals("", map.get(""));
	}
	
	@Test
	public void testCleanRequest2() throws IOException
	{
		Server server = new Server();
		HelloCleanCallback cb = new HelloCleanCallback("/foo");
		server.registerCallback(cb);
		FakeRequest he = new FakeRequest("/foo/bar/baz");
		server.handle(he);
		Map<String,String> map = cb.getLastParameters();
		assertNotNull(map);
		assertEquals(1, map.size());
		assertEquals("bar/baz", map.get(""));
	}
	
	@Test
	public void testCleanRequest3() throws IOException
	{
		Server server = new Server();
		HelloCleanCallback cb = new HelloCleanCallback("/foo");
		server.registerCallback(cb);
		FakeRequest he = new FakeRequest("/foo/bar/baz?a=2&b=1");
		server.handle(he);
		Map<String,String> map = cb.getLastParameters();
		assertNotNull(map);
		assertEquals(3, map.size());
		assertEquals("bar/baz", map.get(""));
		assertEquals("2", map.get("a"));
		assertEquals("1", map.get("b"));
	}
}
