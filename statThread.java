import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.nio.file.Files;


public class statThread extends Thread{
	
	public void run(){
		try{
			openSocket();
		}catch(Exception e){}
	}
	
	/*starts the connection for the statistics port*/
	public static void openSocket() throws IOException, BindException{
		String inputLine="";
		ServerSocket statSocket = new ServerSocket(mainServer.statPortNumber); 
		while(true){
			try{
				Socket statClient = statSocket.accept();
				BufferedReader statIn = new BufferedReader(new InputStreamReader(statClient.getInputStream())); 
				PrintWriter statOut = new PrintWriter(statClient.getOutputStream(), true);
					
				//Get http request from statistics client and call appropriate method if needed
				inputLine=statIn.readLine();				
				if (inputLine!=""){
					inputLine="";
					statHTML(statOut);
				}	
				statClient.close();
				statIn.close();
				statOut.close();			
					
			}
			catch(Exception E){ //in case something bad happens
				StringWriter sw = new StringWriter();
				E.printStackTrace(new PrintWriter(sw));
				String exceptionAsString = sw.toString();
				clientThread.writeErrorLog("request for statistics port",exceptionAsString,("remote ip from "+mainServer.statPortNumber));
			}
		}
	}
		/*builds and sends the html page and the http response for the statisstics requests*/
	public static void statHTML(PrintWriter out)throws Exception{
		//Create a StringBuilder object, called "html", in which we build the html(you don't say!)
		StringBuilder html = new StringBuilder();
		//Start of HTML
		html.append( "<html>\r\n" );
		html.append( "<head>\r\n" );
		//Title 
		html.append( "<title>Statistics For Server</title>\r\n" );
		html.append( "</head>\r\n" );
		html.append( "<body>\r\n" );
			
		html.append( "<h1>Statistics for "+mainServer.SERVERNAME+"</h1>\r\n\r\n" );
		html.append( "<h4>Running since : "+mainServer.initiateTD+"</h4>\r\n" );
		if(mainServer.countCons!=0){
			html.append( "<h4>Average time per connection (secs): "+String.valueOf(mainServer.countTime/mainServer.countCons)+"</h4>\r\n" );
		}
		html.append( "<h4>Total number of connections : "+mainServer.countCons+"</h4>\r\n" );
		html.append( "<h4>Total number of errors : "+mainServer.countErrors+"</h4>\r\n" );
		//End of HTML
		html.append( "</body>\r\n" );
		html.append( "</html>\r\n" );
		//Send HTTP RESPONSE
		Date date = new Date();
		out.print("HTTP/1.1 200 OK" + "\r\n");
		out.print( "Date: " + date + "\r\n" );
		out.print( mainServer.SERVERNAME + "\r\n" );
		out.print( "Content-length: " + html.toString().length() + "\r\n" );
		out.print( "Connection: close\r\n" );
		out.print( "Content-type: text/html\r\n\r\n" );
		//SEND HTML RESPONSE
		out.print( html.toString() );
		//flush
		 out.flush();
	}

}