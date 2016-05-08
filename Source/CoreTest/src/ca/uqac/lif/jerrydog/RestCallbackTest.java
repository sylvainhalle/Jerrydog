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
