
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import java.net.UnknownHostException;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

public class MongoTestClient {
    public static void main(String []argv) {
	System.out.println("hello world");		
	try {
	    MongoClient mongo = new MongoClient("localhost", 27017);
	    DB db = mongo.getDB("test");
	    DBCollection table = db.getCollection("entries");
	    BasicDBObject document = new BasicDBObject();
	    document.put("name", "mike");
	    WriteResult result = table.insert(document);
	
	    //	} catch (UnknownHostException e) {
	    // e.printStackTrace();
        } catch (MongoException e) {
  	   e.printStackTrace();
        }        
    }
}
