import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class xmlParser {

	private int listenPort;
	private int statisticsPort;
	private String port1;
	private String port2;
	private String accessDirectory;
	private String errorDirectory;
	private String rootDirectory;

	public void buildDoc(){
		
		File file = new File("C:\\root\\config.xml");

		try {

			//create dBuilder object
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			//parse the content of our XML Configuration file to an appropriate object 
			Document doc = dBuilder.parse(file);
			
			
			//optional, but may be recommended --IS IT NEEDED????
			doc.getDocumentElement().normalize();
			//check
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());


			//doc.getElementsByTagName("method") returns a NodeList.
			//doc.getElementsByTagName("method").item(0) - returns a Node.
			//doc.getElementsByTagName("method").item(0).getTextContent() returns the value 
			port1 = doc.getElementsByTagName("listen port").item(0).getTextContent();
			port2 = doc.getElementsByTagName("statistics port").item(0).getTextContent();
			
			accessDirectory = doc.getElementsByTagName("access filepath").item(0).getTextContent();
			errorDirectory  = doc.getElementsByTagName("error filepath").item(0).getTextContent();
			
			
			
			//why is this used???What do we split?
			StringTokenizer tokenizer = new StringTokenizer(port1, "\"");
			while(tokenizer.hasMoreTokens()){
				port1 = tokenizer.nextToken();
			}
			try{
				//port is returned as a string, so we type cast it to an integer
				listenPort = Integer.parseInt(port1);
				statisticsPort = Integer.parseInt(port2);
			}catch(NumberFormatException e){
				e.printStackTrace();
			}

			rootDirectory = doc.getElementsByTagName("documentroot filepath").item(0).getTextContent();



		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}

	public int getListenPort(){
		return listenPort;
	}

	public String getRootDirectory(){
		return rootDirectory;
	}
	
	public int getStatisticsPort(){
		return statisticsPort;
	}
	
	public String accessDirectory(){
		return accessDirectory;
	}
	
	public String errorDirectory(){
		return errorDirectory;
	}
}
