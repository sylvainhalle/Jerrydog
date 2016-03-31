Jerrydog: a lightweight web application server in Java
======================================================

Jerrydog is a web application server, comparable in essence to
[Apache Tomcat](), but much simpler and lightweight. It allows you
to easily create a server that listens to HTTP requests, dispatches
its content to one of the *callbacks* you can create, and returns
its response. As such, Jerrydog can be seen as a thin wrapper over
Java's `com.sun.net.httpserver` classes, taking care of a lot of
boilerplate code you'd otherwise have to write.


How it works
------------

Suppose you want to create a server on `localhost` that accepts two
requests:

- When calling `http://localhost/hello`, the server should reply
  with the string "Hi"
- When calling `http://localhost/time/xyz`, the server should
  reply with the current local time in city "xyz", or with an error
  response if the city is not found.

### Create a callback

The first step is to create a class for each request, that will
take care of producing the appropriate response. You do this by
inheriting from the `RequestCallback` class. Here is a possible callback
for `hello`:

    class HelloCallback {
    
      public boolean fire(HttpExchange t) {
        String path = t.getRequestURI().getPath();
        return path.compareTo("/hello") == 0;
      }
      
      public CallbackResponse process(HttpExchange t) {
        CallbackResponse cbr = new CallbackResponse(t);
        cbr.setCode(CallbackResponse.HTTP_OK).setContents("Hi");
        return cbr;
      }
    }

The first method, `fire()`, decides based on the contents of the HTTP
request whether this callback should take care of it. In this case,
we look at the path contained in the request, and return `true` if this
path is the string "/hello", indicating "this request is for me"; we
return false otherwise.

The second method, `process()`, is responsible for producing a response
to the request. We create an empty object `CallbackResponse`, and populate
two of its fields. The first is the response *code*, which can be any of
the [HTTP status codes](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes);
the most common are represented in Jerrydog by constants, such as HTTP_OK
(200). The second is the response contents, which can be any array of
bytes (binary data, a string containing HTML, JSON, etc.). In our case,
the contents is simply the character string "Hi".

A callback can be created in the same way for the second URL:

    class TimeCallback {
    
      public boolean fire(HttpExchange t) {
        String path = t.getRequestURI().getPath();
        return path.startsWith("/time") == 0;
      }
      
      public CallbackResponse process(HttpExchange t) {
        CallbackResponse cbr = new CallbackResponse(t);
        String parts[] = t.getRequestURI().getPath().split("/");
        String time = getTimeForCity(parts[1]);
        if (time == null) {
          cbr.setCode(CallbackResponse.HTTP_NOT_FOUND);
        } else {
          cbr.setCode(CallbackResponse.HTTP_OK).setContents(time);
        }
        return cbr;
      }
      
      String getTimeForCity(String city) {
        ...
      }
    }

The callback is set to fire when the URL starts with the string "/time".
The `process()` method extracts the city name from the URL string, attempts
to retrieve the time for that city; if the time is null, the response
code is set to 404 (HTTP_NOT_FOUND), otherwise, it is set to 200 and the
time string is put into the response body.

### Creating the server

The last step is to create a server with these callbacks. The simplest way
is to instantiate an empty `Server` class, and to add the two callbacks to
it:

    Server s = new Server();
    s.registerCallback(new HelloCallback());
    s.registerCallback(new TimeCallback());
    s.startServer();

That's it. After calling `startServer()`, the server will listen to HTTP
requests on port 80 of `localhost`, and serve appropriate responses when
called (all these defaults can be changed, see the Javadoc). Total size:
17 lines of code.

A different way of doing it is by creating a new class descending from
server:

    class MyServer extends Server {
      public MyServer() {
        super();
        registerCallback(new HelloCallback());
        registerCallback(new TimeCallback());
      }
    }

In your code, you can then create instances of `MyServer`, which will
already contain the callbacks.

Going further
-------------

This simple example shows the basic functionality of Jerrydog. It is
possible to create more complex servers and callbacks.

- The `RestCallback` class provides functionalities to encode and
  decode URL parameters (such as "foo=bar&baz=123&abc")
- The `InnerFileCallback` can easily server the contents of local files
- Cookies can be carried in requests and responses using the `Cookie`
  class

Dependencies
------------

None. (And it should stay that way.)

Why is it called Jerrydog?
--------------------------

    String name = "Tomcat";
    return name.replace("Tom", "Jerry").replace("cat", "dog");

About the author                                                   {#about}
----------------

Jerrydog was written by Sylvain Hallé, associate professor at Université
du Québec à Chicoutimi, Canada.
