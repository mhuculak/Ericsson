
import java.io.*;
import java.net.*;

public class SimpleClient {
    public static void main(String[] args) throws Exception {

      URL url = new URL("http://localhost:9876");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      PrintWriter out = new PrintWriter(connection.getOutputStream());
      String name = "name="+URLEncoder.encode("myname", "UTF-8");
      String email = "email="+URLEncoder.encode("email@email.com", "UTF-8");
      out.println(name+"&"+email);
      out.close();
      BufferedReader in = new BufferedReader(
        new InputStreamReader(connection.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
         System.out.println(line);
      }
      in.close();
   }
}
