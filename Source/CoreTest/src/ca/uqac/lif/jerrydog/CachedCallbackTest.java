package ca.uqac.lif.jerrydog;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.sun.net.httpserver.Headers;

public class CachedCallbackTest
{
	@Test
	public void testCache1() throws IOException
	{
		CachedServer ifs = new CachedServer();
		FakeRequest he = new FakeRequest("/foo.txt");
		ifs.handle(he);
		assertEquals(CallbackResponse.HTTP_OK, he.getResponseCode());
		// Request again; should receive a 303
		FakeRequest he2 = new FakeRequest("/foo.txt");
		he2.getRequestHeaders().add("If-Modified-Since", "foo");
		ifs.handle(he2);
		assertEquals(CallbackResponse.HTTP_NOT_MODIFIED, he2.getResponseCode());
		// Request different file
		FakeRequest he3 = new FakeRequest("/baz.txt");
		ifs.handle(he3);
		assertEquals(CallbackResponse.HTTP_OK, he3.getResponseCode());
	}
	
	@Test
	public void testCache2() throws IOException
	{
		CachedServer ifs = new CachedServer();
		FakeRequest he = new FakeRequest("/foo.txt");
		ifs.handle(he);
		assertEquals(CallbackResponse.HTTP_OK, he.getResponseCode());
		// Request again, but client did not cache; should receive a 200
		FakeRequest he2 = new FakeRequest("/foo.txt");
		ifs.handle(he2);
		assertEquals(CallbackResponse.HTTP_OK, he2.getResponseCode());
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
			super(CachedServer.class, true, 0);
		}
	}
	
	protected static class FakeRequest extends EmptyHttpExchange
	{
		protected final Headers m_requestHeaders = new Headers();
		
		protected final Headers m_responseHeaders = new Headers();
		
		public FakeRequest()
		{
			super();
			m_requestHeaders.add("Accept", "text/plain");
		}
		
		public FakeRequest(String request)
		{
			super(request);
			m_requestHeaders.add("Accept", "text/plain");
		}
		
		@Override
		public Headers getRequestHeaders()
		{
			return m_requestHeaders;
		}
		
		@Override
		public Headers getResponseHeaders()
		{
			return m_responseHeaders;
		}
	}
}
