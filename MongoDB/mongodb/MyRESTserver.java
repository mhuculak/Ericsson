package mongodb;


import java.io.*;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.*;

import mongodb.MyMongoClient;
import mongodb.MyMongoResponse;
import mongodb.MyRESTendpoint;

/**
    MyRESTserver is a REST server application implemented as an HTTP server. The server opens a listening port 
    which is passed as a command line argument. When an HTTP request is received on the port, the request is 
    formated as a REST endpoint which is then invoked to perform the requested CRUD operation

    TODO: add multi-threading
 */

public class MyRESTserver {

    class MyHandler implements HttpHandler {
       public void handle(HttpExchange he) throws IOException {
	  System.out.println("handler invoked");
	  MyRESTendpoint endpoint = new MyRESTendpoint( he.getRequestMethod(), he.getRequestURI().getPath());
	  MyMongoResponse response;
	  if (endpoint.isCRUD()) {
	      MyMongoClient mongo = MyMongoClient.getInstance();
	      response = mongo.invoke( endpoint, he.getRequestBody() );
	  }
	  else {
	      response = new MyMongoResponse();
	      response.setHttpStatus(400); // bad request
	      response.setBody("endpoint " + he.getRequestURI().getPath() + " using method " + he.getRequestMethod() + " is not a CRUD operation");
	  }
          he.sendResponseHeaders(response.getHttpStatus(), response.getBody().length());
          OutputStream os = he.getResponseBody();
          os.write(response.getBody().getBytes());
          os.close();
       }
    }
    
    public MyRESTserver(int port) {
	m_port = port;
    }
    
    public void start() {
	try {
	    HttpServer server = HttpServer.create(new InetSocketAddress(m_port), 0);
	    MyHandler handler = new MyHandler();
	    server.createContext("/restAPI/items", new MyHandler());
	    System.out.println("starting server");
	    server.start();
	}
        catch (IOException e) {
	    System.out.println("Exception thrown: " + e);
	}
    }

    public static void main(String argv[]) {
	int default_port = 9876;
	int port = default_port;
	if (argv.length == 1) {
	    port = Integer.parseInt(argv[0]);
	}
	else if (argv.length > 1) {
	    System.out.println("ERROR: too many arguments");
	    return;
	}
	MyRESTserver myserver = new MyRESTserver(port);
	myserver.start();
    }

    private int m_port;
}
