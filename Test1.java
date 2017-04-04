import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.nio.file.Files;

public class Test1 {
	public static String SERVERNAME = "CE325 (Java based server)";
	public static void main(String[] args) throws IOException {
    
		//Port Number to Connect
		int portNumber = 8000;

		String codeStatus;
		//PrintWriter out; //needs initialisation if we will create it here
		
		try{ 
		
			//Creation of serverSocket and clientSocket
			ServerSocket serverSocket = new ServerSocket(portNumber); 
			Socket clientSocket = serverSocket.accept();   

			//Input and Output Streams: in, out
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);           
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
				

			String inputLine, readFile;
			
			//Our files for the WebServer exist inside this folder
			String ROOT = "C:\\root\\"; 

			Date today = new Date();
	
			//kalliCode
			/*while ((inputLine = in.readLine()) != null) {
				
				out.println(inputLine);
				
				System.out.println("echoing: "+inputLine);
				
			}*/
			String [] parts ;
			//String codeStatus;
			
			
			//Read GET REQUEST 
			inputLine = in.readLine();
			parts = inputLine.split(" "); 
			
			
			//decode url if it has spaces
			if (parts[1].matches("(.*)%20(.*)")){
				parts[1]=parts[1].replaceAll("%20", " ");
			}
			
			// remove "/" character of C:\root\/
			parts[1] = parts[1].substring(1); 
			
		
			//Create Filepath
			File filepath= new File (ROOT + parts[1]);
			
			
			/*
			ERROR 405 Method Not allowed.
			Only HTTP GET is supported in this code.
			*/
			if(!parts[0].equals("GET")){ 
				codeStatus = "405";
				responseForError(codeStatus,out);
				/*out.println("405 Method Not Allowed!");
				//kaneis analoga pramata gia to 405
				*/
			}
			/*
			ERROR 404 Not Found.
			The file/directory you want does not exist.
			*/
			else if(!filepath.exists()){
				codeStatus = "404";
				responseForError(codeStatus,out);
				/*
				out.println("404 Not Found!");
				out.println(ROOT + parts[1]);
				System.out.println("before responseForError 404");
				System.out.println("after responseForError 404");
				*/
			}
			/*
			ERROR 400 Bad Request 
			*/
			else if((parts[2]== null ) ||															//&& parts[3]== null)
					( !(parts[2].equals("HTTP/1.1")) && !(parts[2].equals("HTTP/1.0")) ) ){
				//out.println("400 Bad Request!");
				codeStatus = "400";
				responseForError(codeStatus,out);
				//return;
			}
			else{
				//200 ok
				//send file, do it in method (?)
				codeStatus = "200";
				if (filepath.isFile() ) {
					//sendFile
				}
				else if (filepath.isDirectory() ) {
					//sendDirectory
					//first check index.htm, if not , build some shit
				}
				
				//code below should go inside sendFile
				out.println("\r\n");
				out.println("HTTP/1.1 200 OK"); // must check if HTTP is version 1.1 or 1.0
				out.println("Date: " + today);
				out.println("Server: "+SERVERNAME);//
				out.println("Last-Modified: " + filepath.lastModified());
				out.println("Content-Length: " + filepath.length());
				out.println("Connection: ");
				out.println("Content-Type: " + Files.probeContentType(filepath.toPath()) + "\r\n");
				out.println("\r\n");
				
				BufferedReader inputStream = new BufferedReader(new FileReader(filepath));
				/*while ((readFile = inputStream.readLine()) != null) {
					out.println(readFile);
					out.flush();
				}*/
			
				OutputStream data = new BufferedOutputStream( clientSocket.getOutputStream());
				int count;
				byte[] buffer = new byte[ 8192 ];
				FileInputStream myInput = new FileInputStream( filepath.getPath() );

				while ( ( ( count = myInput.read( buffer ) ) != -1 ) )
				{
					
					//out.write(buffer);
					//out.flush();
					data.write( buffer );
					data.flush();
				}
				out.flush();
			}
		}
		catch (IOException e) {
			/*
			 ERROR 500 Internal Server Error
			*/
			codeStatus = "500";
			ServerSocket serverSocket = new ServerSocket(portNumber); 
			Socket clientSocket = serverSocket.accept();   
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);           // needs to be created again because it isn't inside the try block
			responseForError(codeStatus,out);
			System.out.println(e.getMessage());
			//System.out.println("500 Internal Server Error!");
			
		}
	}
	
	/*
	responseForError takes the corresponding status(404/405/500/etc)
	and the PrintWriter object as arguments.
	It builds the HTML page we want to show in case of an error.
	It sends the HTTP response first and then shows the HTML page which was built.
	*/
	private static void responseForError(String codeStatus, PrintWriter out) {
		String title, body;

		
		//The body and the title of the page  are chosen in a switchcase structure.
        switch ( codeStatus )  {
            case "400":		//Bad Request
                title = "Bad Request";
                body = "HTTP Error 400: Bad Request";
                break;
            case "404":		//File not Found
                title = "File Not Found";
                body = "HTTP Error 404: File Not Found";
                break;
            case "405":		//Method Not Allowed
                title = "Method Not Allowed";
                body = "HTTP Error 405: Method Not Allowed";
                break;
            case "500":		//Internal Server Error
                title = "Internal Server Error";
                body = "HTTP Error 500: Internal Server Error";
                break;
            default:		//Unknown
                title = "Unknown Error";
                body = "HTTP Error: Unknown Error";
		
		}
		
		
		//--------------------------------------------------------
		//Create a StringBuilder object, called "html", in which we build the html(you don't say!)
		StringBuilder html = new StringBuilder();

		//Start of HTML
        html.append( "<!DOCTYPE html>\r\n" );
        html.append( "<html>\r\n" );
        html.append( "<head>\r\n" );
		
		//Uncomment the following if you want the ERROR text to be red
       // html.append( "<style> .size, .date {padding: 0 32px} h1.header {color: red; vertical-align: middle;}</style>\r\n" );
		
		//Title and Body we chose earlier
        html.append( "<title>" + title + "</title>\r\n" );
        html.append( "<h1 class=\"header\">" + body + "</h1>\r\n" );
		
		//End of HTML
        html.append( "</head>\r\n" );
        html.append( "<body>\r\n" );
        html.append( "</body>\r\n" );
        html.append( "</html>\r\n" );
		//--------------------------------------------------------
		
        
		
		//--------------------------------------------------------
		//HTTP RESPONSE
		//Write to PrintWriter "out" the HTTP Response
		Date date = new Date();
		out.print( "HTTP/1.1 " + codeStatus + "\r\n" );
        out.print( "Date: " + date + "\r\n" );
        out.print( SERVERNAME + "\r\n" );
		//toString method returns a string representing the data in this sequence
		out.print( "Content-length: " + html.toString().length() + "\r\n" ); 
        out.print( "Connection: close\r\n" );
        out.print( "Content-type: text/html\r\n\r\n" );
		
		//Then, show the HTML on screen
		out.print( html.toString() );
		
		//Flush the toilet before you leave.
        out.flush();
		
		//PrintWriter 'out' needs close in order to be saved after flushed.
		out.close();
		//--------------------------------------------------------
		
	}
	
	/*
	getLastModifiedDate gets "file.lastModified" method as input.
	It returns the Date the file was last modified 
	as a String in a format we want.
	*/
	public String getLastModifiedDate( long timeDate )
    {
        String dateFormat = "EEE, d MMM YYYY HH:mm:ss z";
        SimpleDateFormat sdf = new SimpleDateFormat( dateFormat );
        sdf.setTimeZone( TimeZone.getTimeZone( "GMT" ) );

        return sdf.format( timeDate );
    }

	/*
	getFileSize(File file) gets a file as input and returns 
	its size in a String in corresponding bytes size.
	*/
    public String getFileSize( File file )
    {
        double bytes = file.length();
        double kilobytes = ( bytes / 1024);
        double megabytes = (kilobytes / 1024);
        double gigabytes = (megabytes / 1024);

        if ( gigabytes >= 1 )
            return String.format( "%.1f GBs", gigabytes );
        else if ( megabytes >= 1 )
            return String.format( "%.1f MBs", megabytes );
        else if ( kilobytes >= 1 )
            return String.format( "%.1f KBs", kilobytes );
        else
            return String.format( "%.1f Bytes", bytes );
    }
}