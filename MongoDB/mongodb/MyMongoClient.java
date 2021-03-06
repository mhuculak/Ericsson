package mongodb;

import java.io.*;

import java.net.UnknownHostException;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;

import mongodb.MyRESTendpoint;

/**
    class MyMongoClient handles the interface with the Mongo DB. 
    It Implememnted as a singleton to avoid multple connections.

    TODO: use a pool of connections and/or serialize access to the DB connection via a synchorize.
    TODO: add ato handle a DB connection failure.
    TODO: the data used to create the DB connection should be configurable
 */
class MyMongoClient {
    private static MyMongoClient m_instance = null;
    private static MongoClient m_client = null;
    private static DB m_db = null;
    private static DBCollection m_entries = null;
    private static DBCollection m_counter = null;

    private MyMongoClient() {
	try {
	    m_client = new MongoClient("localhost", 27017);
	    m_db = m_client.getDB("test");                    
	    m_entries = m_db.getCollection("entries");
	    m_counter = m_db.getCollection("counters");
	}
	catch (MongoException e) {
	     e.printStackTrace();
        }   
    }
    private static Object getNextID() {
	if (m_counter.count() == 0) {
	    BasicDBObject document = new BasicDBObject();
	    document.append("_id", "entries");
            document.append("seq", 0);
            m_counter.insert(document);
	}
        BasicDBObject searchQuery = new BasicDBObject("_id", "entries");
        BasicDBObject increase = new BasicDBObject("seq", 1);
        BasicDBObject updateQuery = new BasicDBObject("$inc", increase);
        DBObject result = m_counter.findAndModify(searchQuery, null, null,
            false, updateQuery, true, false);

        return result.get("seq");
    }
    
    public static MyMongoClient getInstance() {
	if (m_instance == null) {
	    m_instance = new MyMongoClient(); 
	}
	return m_instance;
    }
    /**
       The invoke method will invoke the CRUD operation contained in the REST endpoint (passed as an argument)
       @param endpoint The REST endpoint containing the CRUD operation to be performed
       @param in An input stream which contains the body of the HTTP request, used to pass the document data when
                 creating or updating a document
       @return MyMongoResponse the response contains the status of the operation and, if successfull, the data returned 
               from the CRUD operation i.e. the MongoDB document in JSON format that the REST endpoint specified  
     */
    public static MyMongoResponse invoke(MyRESTendpoint endpoint, InputStream in) {
        MyMongoResponse response = new MyMongoResponse();
	WriteResult result;
	System.out.println("invoke " + endpoint.asString());
	
	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	StringBuilder builder = new StringBuilder();
	String line;
	try {
	    System.out.println("post data size=" + in.available());
	    while((line = reader.readLine()) != null) {
		System.out.println("GOT " + line);
		builder.append(line);
	    }
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
	String poststring = builder.toString();
	String[] postlist = poststring.split("&");
	int i;
	
        if (endpoint.getOperation() == CRUDop.CREATE) {
	   BasicDBObject document = new BasicDBObject();
	   document.append("_id", getNextID());

	   for ( i=0 ; i<postlist.length ; i++) {
	       String[] keyval = postlist[i].split("=");
	       if ( keyval.length == 2) {
		   document.append( keyval[0], keyval[1]);
	       }
	   }
	   result = m_entries.insert(document);
	   response.setBody(document.toString());
	   response.setHttpStatus(200); // success
	}
	else if (endpoint.getOperation() == CRUDop.UPDATE) {
	    if (endpoint.hasID()) {
  	       BasicDBObject searchQuery = new BasicDBObject("_id", Integer.parseInt(endpoint.getID()));
	       DBCursor cursor = m_entries.find(searchQuery);
               if (cursor.count()==1) {
		   BasicDBObject updateQuery =  new BasicDBObject();
		   BasicDBObject newFields = new BasicDBObject();
		   for ( i=0 ; i<postlist.length ; i++) {
		       String[] keyval = postlist[i].split("=");
		       if ( keyval.length == 2) {
			   newFields.append( keyval[0], keyval[1]);
		       }
		   }
		   updateQuery.append( "$set", newFields);
		   result = m_entries.update(searchQuery, updateQuery);
		   cursor = m_entries.find(searchQuery);
		   response.setBody(cursor.next().toString());
		   response.setHttpStatus(200); // success		   
	       }
	       else {
		   System.out.println("ERROR: found multiple documents with ID = " + endpoint.getID());
		   response.setHttpStatus(500); // success		   
	       }
	    }
	    else {
		System.out.println("ERROR: enpoint does not specify an ID" + endpoint.asString());
		response.setHttpStatus(400); // failure		
	    }
	}
	else if (endpoint.getOperation() == CRUDop.READ) {
	    if (endpoint.hasID()) {
  	       BasicDBObject searchQuery = new BasicDBObject("_id", Integer.parseInt(endpoint.getID()));
	       DBCursor cursor = m_entries.find(searchQuery);
               if (cursor.count()==1) {
		   response.setBody(cursor.next().toString());
		   response.setHttpStatus(200);
	       }
	       else {
		   System.out.println("ERROR: found multiple documents with ID = " + endpoint.getID());
		   response.setHttpStatus(500); // success
	       }
	    }
	    else {
		System.out.println("ERROR: enpoint does not specify an ID" + endpoint.asString());
		response.setHttpStatus(400); // failure
	    }
	}
	else if (endpoint.getOperation() == CRUDop.DELETE) {
	    if (endpoint.hasID()) {
  	       BasicDBObject searchQuery = new BasicDBObject("_id", Integer.parseInt(endpoint.getID()));
	       DBCursor cursor = m_entries.find(searchQuery);
               if (cursor.count()==1) {
		   response.setBody(cursor.next().toString());
		   result = m_entries.remove(searchQuery);
		   response.setHttpStatus(200); // FIXME: should verify that it was deleted
	       }
	       else {
		  System.out.println("ERROR: found multiple documents with ID = " + endpoint.getID());
		  response.setHttpStatus(500); // internal server error
	       }
	    }
	    else {
		System.out.println("ERROR: enpoint does not specify an ID" + endpoint.asString());
		response.setHttpStatus(400); // failure
	    }
	}

	//	response.setBody(endpoint.asString());
	return response;
    }    
}
