import java.io.*;
import java.net.*;

public class MyRESTclient {
    public static void main(String[] argv) throws Exception {
	int port;
	String host;
	String method;
	if (argv.length > 2) {
	    host = argv[0];
	    port = Integer.parseInt(argv[1]);
	    method = argv[2];
	    int postindex = 3;
	    String myurl = "http://" + host + ":" + port + "/restAPI/items/";
	    if (argv.length > 3) {
		String[] check = argv[3].split("=");
		if (check.length < 2) {
  		  String id = argv[3];
		  myurl += id;
		  postindex = 4;
		}
	    }
	    System.out.println("Method: " + method + " URL: " + myurl);
	    URL url = new URL(myurl); 
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod(method);
	    if (argv.length > postindex) {
		connection.setDoOutput(true);
		PrintWriter out = new PrintWriter(connection.getOutputStream());
		int i;
		for ( i=postindex ; i<argv.length ; i++ ) {
		    out.println(argv[i]+"&");
		    System.out.println("POST: " + argv[i]+"&");
		}
		out.close();
	    }
	    else {
		connection.setDoOutput(true);
	    }
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
              System.out.println(line);
            }
            in.close();
	}
	else {
	    System.out.println("ERROR: wrong number of arguments");
	    System.out.println("Usage: host port method <id> <key1=val1> <key2=val2>");
	}	
    }
}
