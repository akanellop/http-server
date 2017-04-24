//package ce325.hw2;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.nio.file.Files;



public class mainServer{

//public variables/fields for other classes and concurrency 

	//declare variables that will be taken from xml for server "initialization"
	public static String SERVERNAME = "CE325 (Java based server)";
	public static String ROOT;
	public static File ROOTPATH ;
	public static int portNumber ;
	public static int statPortNumber;
	
	//Text files for logs
	public static String ACCESS;
	public static String ERROR;
	public static File ACCESSPATH;
	public static File ERRORPATH;
	
	//streams to write in logs
	public static PrintWriter writerAccess,writerError;
	
	//blocking queue for request objects
	public static BlockingQueue<reqOb> msgQ = new ArrayBlockingQueue<reqOb>(10);
	
	//global variables for statistics port
	public static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static Date begDate = new Date();
	public static String initiateTD=(dateFormat.format(begDate)); //2016/11/16 12:08:43
	public static volatile int countTime=0; //+1: in clientThread, line 242
	public static volatile int countCons=0; //+1: in clientThread, line 246
	public static volatile int countErrors=0; //+1: in clientThread, line 256
	
	
	//etc
	public static Socket clientSocket;
	public static BufferedReader in ;
	
	public static void main(String[] args) throws IOException, BindException, InterruptedException{
		
		//init 
		String request="",inputLine="",userStr="",remoteAd="";
		//BufferedReader in = null;
		PrintWriter out = null;
		OutputStream data =null;
			
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
		
		//create a serverSocket, using the given port
		ServerSocket serverSocket = new ServerSocket(portNumber); 
		
		//initiate thread for statistics port, code inside statThread class
		statThread statistics= new statThread();
		statistics.start();
		
		/*
		initiate 2 concurrent workers-threads
		for sending response to server port, code in clientThread class
		*/
		clientThread t1= new clientThread();
		t1.start();
		
		//Uncomment the following if you want to have 2 workers and test the concurrency
		/*
		clientThread t2= new clientThread();
		t2.start();
		*/
		
		
		while(true ){ //run forever
			 
			 /*
			create a socket(clientSocket) for the client, connect them to the Server and then create:
			1 InputStream 	: BufferedReader in
			2 OutputStreams : PrintWriter out ( for the HTTP Response) , OutputStream data( for file sending)
			*/
			//Socket 
			clientSocket = serverSocket.accept();   
			remoteAd =clientSocket.getRemoteSocketAddress().toString();
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			data = new BufferedOutputStream( clientSocket.getOutputStream());
			
			try{
				
				//GET HTTP REQUEST from client
				 while ((inputLine = in.readLine()) != null) {
					if (inputLine.startsWith("GET")){
						request=request + inputLine+"\n"; //1st line of GET request
					}
					else if(inputLine.startsWith("User")){
						userStr=userStr + inputLine+"\n"; //User-Agent Line of GET request 
					}
					else if(inputLine.startsWith("Accept")){ 
						break;
					}
				}
				/*	
				Send (1st line of) GET REQUEST to send 
				the appropriate response to our client
				*/
				if (  (request!="" ) && (request!= null)  ) {
					try{
						
						//put the info for the specific request in queue
						
						/*
						Now, we insert all the required data for the response inside a "reqOb" object.
						See "reqOb" class and its constructor for more information.
						*/
						reqOb curReq = new reqOb(request,userStr,remoteAd,out,data);
						
						//put the object(and its data) inside the BlockingQueue
						msgQ.put(curReq);
						
						//t2.start();
						
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
					
					//out.close();
					//data.close();
					//in.close();
					//clientSocket.close();
					//System.out.println("here");
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