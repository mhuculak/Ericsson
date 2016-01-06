package mongodb;
/**
    The MyMongoResponse class combines the HTTP status and body of the result to be returned
    to the REST client 
 */
public class MyMongoResponse {
    public int getHttpStatus() {
	return m_http_status;
    }
    public void setHttpStatus(int http_status) {
	m_http_status = http_status;
    }
    public String getBody() {
	return m_body;
    }
    public void setBody(String body) {
	m_body = body;
    }
    private String m_body;
    private int m_http_status;
}
