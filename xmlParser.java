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

	private static int listenPort;
	private static int statisticsPort;
	private static String port1;
	private static String port2;
	private static String accessDirectory;
	private static String errorDirectory;
	private static String rootDirectory;

	public static void buildDoc(){
		
		//File file = new File("C:\\root\\config.xml");
		File file = new File("config.xml");
		//might be needed to get as an argument ?
		if (file.exists() ) {
			//System.out.println("XML PARSED CONFIG FILE  " + file.getName()+ " is " + file.getAbsolutePath() ) ;
			System.out.println("XML PARSED CONFIG FILE DIR = " + file.getAbsolutePath() ) ;
		}

		try {
			
			//create dBuilder object
			//System.out.println("im inside try");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			//parse the content of our XML Configuration file to an appropriate object 
			Document doc = dBuilder.parse(file);
			
			//System.out.println(file.getName());
			
			//optional, but may be recommended --IS IT NEEDED????
			doc.getDocumentElement().normalize();
			//check
			
			/*
			//doc.getElementsByTagName("method") returns a NodeList.
			//doc.getElementsByTagName("method").item(0) - returns a Node.
			//doc.getElementsByTagName("method").item(0).getTextContent() returns the value 
			*/
			
			//System.out.println("before getting elements");
			//port1 = doc.getElementsByTagName("listen port").item(0).getTextContent();
			port1 = doc.getElementsByTagName("listenport").item(0).getTextContent();
			//System.out.println("got port1 " + port1);
			//System.out.println("after listenport");
			port2 = doc.getElementsByTagName("statisticsport").item(0).getTextContent();
			//System.out.println("got port2 " + port2);
			
			accessDirectory = doc.getElementsByTagName("accessfilepath").item(0).getTextContent();
			//System.out.println("got accessDirectory " + accessDirectory);
			errorDirectory  = doc.getElementsByTagName("errorfilepath").item(0).getTextContent();
			//System.out.println("got errorDirectory " + errorDirectory);
			rootDirectory = doc.getElementsByTagName("documentrootfilepath").item(0).getTextContent();
			//System.out.println("got rootDirectory " + rootDirectory);
			
			
			/*
			System.out.println("\nbefore tokenizer\n");
			System.out.println(port1);
			*/
			//why is this used???What do we split?
			/*
			StringTokenizer tokenizer = new StringTokenizer(port1, "\"");
			while(tokenizer.hasMoreTokens()){
				port1 = tokenizer.nextToken();
			
			*/
			try{
				//ports are returned as a string, so we type cast them to integers
				listenPort = Integer.parseInt(port1);
				statisticsPort = Integer.parseInt(port2);
				
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception happened :/ " + e.getMessage());
			//System.out.println("exception happened");
			System.out.println(port1+"\n");
		}
	}

	public static int getListenPort(){
		return listenPort;
	}

	public static String getRootDirectory(){
		return rootDirectory;
	}
	
	public static int getStatisticsPort(){
		return statisticsPort;
	}
	
	public static String getAccessDirectory(){
		return accessDirectory;
	}
	
	public static String getErrorDirectory(){
		return errorDirectory;
	}
	
	public static void main(String[] args){
	
	//xmlParser.buildDoc();
	buildDoc();
	
	System.out.println("listen port = " + 		getListenPort() );
	System.out.println("statistics Port = " + 	getStatisticsPort() );
	System.out.println("rootDirectory = " +		getRootDirectory() );
	System.out.println("accessDirectory = " +	getAccessDirectory() );
	System.out.println("errorDirectory = " + 	getErrorDirectory() );
	
	}
}
