package mongodb;

enum CRUDop { CREATE, READ, UPDATE, DELETE };
/**
   class MyRESTendpoint creates an enpoint from the HTTP method and URI path received from the REST client
   It consists of a CRUD operation plus an optional ID to identify the DB entry that the CRUD operation applies to
 */
public class MyRESTendpoint {
    public MyRESTendpoint(String http_method, String uri_path) {
	  String[] pathcomp = uri_path.split("/");	  
	  if (pathcomp.length == 4) {
	      m_id = pathcomp[3];
	      m_has_id = true;
	      System.out.println("Received " + http_method + " with ID: " + m_id);
	  }
	  else {
	      m_has_id = false;
	      System.out.println("Received " + http_method + " without ID");
	  }
	  m_is_crud = true;
	  if ( http_method.equals("POST") ) {
	      if ( m_has_id ) {
		  m_operation = CRUDop.UPDATE;
	      }
	      else {
		  m_operation = CRUDop.CREATE;
	      }
	  }
	  else if ( http_method.equals("GET") ) {
	      m_operation = CRUDop.READ;
	  }
	  else if ( http_method.equals("PUT") ) {
	      if ( m_has_id ) {
		  m_operation = CRUDop.UPDATE;
	      }
	      else {
		  m_operation = CRUDop.CREATE;
	      }
	  }
	  else if ( http_method.equals("DELETE") ) {
	      m_operation = CRUDop.DELETE;
	  }
	  else {
	      m_is_crud = false;
	  }

    }
    public CRUDop getOperation() {
	return m_operation;
    }
    public String getID() {
	return m_id;
    }
    public boolean hasID() {
	return m_has_id;
    }
    public boolean isCRUD() {
	return m_is_crud;
    }
    public String asString() {
	if (m_is_crud) {
	    if (m_has_id) {
		return m_operation.name() + " ID: " + m_id;
	    }
	    else {
		return m_operation.name();
	    }
	}
	else {
	    return "not a CRUD operation";
	}
    }
    
    private CRUDop m_operation;
    private String m_id;
    private boolean m_has_id;
    private boolean m_is_crud;
}
