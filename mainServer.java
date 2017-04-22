import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.nio.file.Files;


public class mainServer{

	/*declare here variables that will be  taken from xml for server "initialization"*/
	public static String SERVERNAME = "CE325 (Java based server)";
	//public static String ROOT ="C:\\root\\";   //Our files for the WebServer exist inside this folder
	public static String ROOT;// =  xmlParser.getRootDirectory();
	public static File ROOTPATH ;//= new File (ROOT);
	//public static int  portNumber = 8000;
	public static int portNumber ;//= xmlParser.getListenPort();
	public static int statPortNumber;
	//Text files for logs
	public static String ACCESS;
	public static String ERROR;
	public static File ACCESSPATH;
	public static File ERRORPATH;
	//streams to write in logs
	public static PrintWriter writerAccess,writerError;
	
	//public variables for other classes and concurrency
	public static String request="",inputLine="",userStr="",remoteAd="";
	public static BufferedReader in = null;
	public static PrintWriter out = null;
	public static OutputStream data =null;
	public static BlockingQueue<String> msgQ = new ArrayBlockingQueue<String>(20);
	
	//global variables for statistics port
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static Date begDate = new Date();
	public static String initiateTD=(dateFormat.format(begDate)); //2016/11/16 12:08:43
	public static volatile int countTime=0; //+1: in clientThread, line 242
	public static volatile int countCons=0; //+1: in clientThread, line 246
	public static volatile int countErrors=0; //+1: in clientThread, line 256
	
	public static void main(String[] args) throws IOException, BindException{
			
		//build config
		xmlParser.buildDoc();
		
		//get config 
		ROOT = xmlParser.getRootDirectory();
		ROOTPATH = new File ( ROOT ) ;
		portNumber =xmlParser.getListenPort();
		statPortNumber=xmlParser.getStatisticsPort();
		ACCESS = xmlParser.getAccessDirectory();
		ACCESSPATH = new File(ACCESS);
		ERROR = xmlParser.getErrorDirectory();
		ERRORPATH = new File(ERROR);
		
		//initialize empty logs
		writerAccess = new PrintWriter(ACCESS);
		writerAccess.print("");
		writerAccess.close();
		writerError = new PrintWriter(ERROR);
		writerError.print("");
		writerError.close();
		
		//Check
		System.out.println("XML PARSED ROOT = " + ROOT);
		System.out.println("XML PARSED ACCESS FILE = " + ACCESS);
		System.out.println("XML PARSED ERROR FILE = " + ERROR);
		System.out.println("XML PARSED portNumber = " + portNumber+"\n");
		//create a serverSocket
		ServerSocket serverSocket = new ServerSocket(portNumber); 
		
		//initiate thread for statistics port
		statThread statistics= new statThread();
		statistics.start();

		//myQueue = new ArrayBlockingQueue<String>(20);
					
		while(true ){ //run forever
			/*
				create a socket(clientSocket) for the client, connect them to the Server and then create:
				1 InputStream 	: BufferedReader in
				2 OutputStreams : PrintWriter out ( for the HTTP Response) , OutputStream data( for file sending)
				*/
				Socket clientSocket = serverSocket.accept();   
				remoteAd =clientSocket.getRemoteSocketAddress().toString();
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				data = new BufferedOutputStream( clientSocket.getOutputStream());
				
			try{
				
				//GET HTTP REQUEST from client
				 while ((inputLine = in.readLine()) != null) {
					if (inputLine.startsWith("GET")){
						request=request + inputLine+"\n";
					}
					else if(inputLine.startsWith("User")){
						userStr=userStr + inputLine+"\n";
					}
					else if(inputLine.startsWith("Accept")){
						break;
					}
				}
				/*	
				Send (1st line of) GET REQUEST to send 
				the appropriate response to our client
				*/
				if (  (request!="" )&&(request!= null)  ) {
					//System.out.println("Before responsetoClient, Line 49:Request is " + request);	
					//clientThread.responseToClient(request,userStr,out,data,remoteAd);
					try{
						msgQ.put(request);
						clientThread t1= new clientThread(request,userStr,out,data,remoteAd);
						t1.start();
						t1.join();
						
					//	clientThread t2= new clientThread(request,userStr,out,data,remoteAd);
					//	t2.start();
					//	t2.join();
						
					}
					catch(InterruptedException e){
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						String exceptionAsString = sw.toString();
						clientThread.writeErrorLog(request,exceptionAsString,remoteAd);
					}
					//Response sent to client, time to close streams/sockets and wait for another request.
					request="";
					userStr="";
						
					// Close the sockets for safe reasons, they will be created again either way!
					clientSocket.close();
					out.close();
					data.close();
					
					
				}
			}
			catch(Exception E){ //in case something bad happens
				StringWriter sw = new StringWriter();
				E.printStackTrace(new PrintWriter(sw));
				String exceptionAsString = sw.toString();
				clientThread.writeErrorLog(request,exceptionAsString,remoteAd);
			}
					
					
		}
	}
	
}