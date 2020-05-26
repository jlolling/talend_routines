import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import routines.FileUtil;
import routines.SOAPClient;

public class PlaySOAPClient {
	
	public static void main(String[] args) throws Exception {
		playWithHttpClient();
	}
	
	public static void playWithSOAPConnection() throws Exception {
		SOAPClient c = new SOAPClient();
		c.setAuthenticator("DTAXFER1","SylK9QO8F5");
		System.out.println("Start reading payload...");
		String message = FileUtil.readContentfromFile("/amos_migration/xfile_output_directory/XMSRV_4776_7333.txt.xml", "UTF-8");
		System.out.println(message);
		System.out.println("Start sending request...");
		c.invokeSOAP(SOAPClient.SOAP11, "http://172.31.233.55:5080/service/xfiles", "xfiles", message);
		System.out.println("Body: " + c.getReBodyMessage());
		System.out.println("Fault: " + c.getReFaultMessage());
		System.out.println("Header: " + c.getReHeaderMessage());
	}
	
	public static void playWithHttpClient() throws Exception {
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
	    buffer.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
	    buffer.append(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n");
	    buffer.append("<soap:Body>\n");
		String message = FileUtil.readContentfromFile("/amos_migration/xfile_output_directory/mig1/XDEFFLINK_8539_0_map_XDEFFLINK_m_items_19011.txt.xml", "UTF-8");
		Document doc = DocumentHelper.parseText(message);
		String messageWithoutPragma = doc.getRootElement().asXML();
	    buffer.append(messageWithoutPragma);
	    buffer.append("\n</soap:Body>\n");
	    buffer.append("</soap:Envelope>");
	    HttpPost post = new HttpPost("http://172.31.233.55:5080/service/xfiles");
	    post.setHeader("Content-type", "text/xml; charset=UTF-8");
	    post.setHeader("SOAPAction", "xfiles");
	    InputStream in = IOUtils.toInputStream(buffer.toString(), "UTF-8");
	    post.setEntity(new InputStreamEntity(in, buffer.length()));
	    System.out.println("Connect...");
	    routines.HttpClient client = new routines.HttpClient("http://172.31.233.55:5080/service/xfiles", "DTAXFER1", "SylK9QO8F5", 10000, 10000);
	    System.out.println("Send request...");
	    String responseContent = client.execute(post, true);
	    System.out.println("Response: " + responseContent);
	    System.out.println("http-status: " + client.getStatusCode());

	}

}
