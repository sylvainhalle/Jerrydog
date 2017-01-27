package ca.uqac.lif.jerrydog;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.sun.net.httpserver.Headers;

public class CachedCallbackTest
{
	@Test
	public void testCache() throws IOException
	{
		CachedServer ifs = new CachedServer();
		FakeRequest he = new FakeRequest("/foo.txt");
		ifs.handle(he);
		assertEquals(CallbackResponse.HTTP_OK, he.getResponseCode());
		// Request again; should receive a 303
		FakeRequest he2 = new FakeRequest("/foo.txt");
		ifs.handle(he2);
		assertEquals(CallbackResponse.HTTP_NOT_MODIFIED, he2.getResponseCode());
		// Request different file
		FakeRequest he3 = new FakeRequest("/baz.txt");
		ifs.handle(he3);
		assertEquals(CallbackResponse.HTTP_OK, he3.getResponseCode());
	}
	
	@Test
	public void test404() throws IOException
	{
		CachedServer ifs = new CachedServer();
		// bar.txt does not exist
		FakeRequest he = new FakeRequest("/bar.txt");
		ifs.handle(he);
		assertEquals(CallbackResponse.HTTP_NOT_FOUND, he.getResponseCode());
	}

	
	protected static class CachedServer extends InnerFileServer
	{
		public CachedServer()
		{
			super(CachedServer.class, true);
		}
	}
	
	protected static class FakeRequest extends EmptyHttpExchange
	{
		public FakeRequest()
		{
			super();
		}
		
		public FakeRequest(String request)
		{
			super(request);

		}
		
		@Override
		public Headers getRequestHeaders()
		{
			Headers h = new Headers();
			h.add("Accept", "text/plain");
			return h;
		}
	}
}
