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
