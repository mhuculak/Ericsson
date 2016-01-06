import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
public class MyHS {
    public static void main(String argv[]) {
	try {
    	  HttpServer server = HttpServer.create(new InetSocketAddress(9876), 0);/*
	  HttpHandler handler = new HttpHandler() {
		  public void handle(HttpExchange he) throws IOException {
		      System.out.println("handler2 invoked");
		  }
	      };
										*/
	  server.createContext("/", new MyHandler());
	  //	  server.createContext("/", handler);      
	  // server.setExecutor(null);
	  System.out.println("starting server");
	  server.start();
	}
	catch (IOException e) {
	    System.out.println("Exception thrown: " + e);
	}
    }

   static class MyHandler implements HttpHandler {
       public void handle(HttpExchange he) throws IOException {
	  System.out.println("handler invoked");
	  String method = he.getRequestMethod();
	  System.out.println("Received " + method + " request");
       }
   }
}
