import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.nio.file.Files;

public class Test1 {
	public static String SERVERNAME = "CE325 (Java based server)";
	public static void main(String[] args) throws IOException, BindException{
    
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
			String versionOfHttp= "", extensionForMime="";
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
			
			
			//Get versionOfHttp
			if  (parts[2].equals("HTTP/1.1") ) {
				versionOfHttp = parts[2];
			}
			else if (parts[2].equals("HTTP/1.0") ){
				versionOfHttp = parts[2];
			}
			
			//Create Filepath
			File filepath= new File (ROOT + parts[1]);
			
			
			//if you load winehouse.mp3
			//String fileName = parts[1].getName();
			//then, extension will be .mp3 
			try{
				int index = parts[1].lastIndexOf('.');
				extensionForMime= parts[1].substring(index);
			}catch (Exception Ex){
				System.out.println("Error when getting suffix from file");
			}
			System.out.println("extensionForMime is " +extensionForMime);
			//use this for Mapping
			
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
				
				//Create an OutputStream so we can send data/bytes there for the client
				OutputStream data = new BufferedOutputStream( clientSocket.getOutputStream());
				
				
				/*
				extensionForMime will now get updated to the value we need
				For example, .txt=text/plain
				
				*/
				try {
					extensionForMime = getMimeExtension(extensionForMime);
				}catch(Exception Exxxx){
					System.out.println("error before going to sendFile or sendDirectory");
				}
				System.out.println("extensionForMime is : " + extensionForMime); 
				
				if (filepath.isFile() ) { // if it is a FILE, send it
					//sendFile( String versionOfHttp,PrintWriter out, File filepath, String extensionForMime, OutputStream data);
					sendFile( versionOfHttp, out,  filepath,  extensionForMime,  data);
				}
				else if (filepath.isDirectory() ) {// if it is a DIRECTORY, send index.htm or show the current dir
					//sendDirectory
					//first check index.htm, if not , build some shit
				}
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
	
	public static void sendFile( String versionOfHttp, PrintWriter out, File filepath, String extensionForMime, OutputStream data ) throws IOException{
		
		
		Date date = new Date();
		
		//Build HTTP RESPONSE 
		//---------------------------------------------------------------------
		//might need "\r" in the end of the lines
		out.println("\r\n");
		out.println(versionOfHttp + " 200 OK"); 
		out.println("Date: " + date);
		out.println("Server: "+ SERVERNAME);//
		out.println("Last-Modified: " + getLastModifiedDate(filepath.lastModified() )  );
		out.println("Content-Length: " + filepath.length());
		out.println("Connection: close ");
		
		
		//mime probably , String extensionForMime (?(
		//Replace this line with getMime shit 
		//out.println("Content-Type: " + Files.probeContentType(filepath.toPath()) + "\r\n");
		out.println("Content-Type: " + extensionForMime + "\r\n");
		
		out.println("\r\n");
			
		//Save HTTP Response 
		out.flush();
		
		//--------------------
			
		//BufferedReader inputStream = new BufferedReader(new FileReader(filepath));
		/* //this works for only printing texts
		while ((readFile = inputStream.readLine()) != null) {
			out.println(readFile);
			out.flush();
		}
		*/
			
		//OutputStream data = new BufferedOutputStream( clientSocket.getOutputStream());
		
		
		//Send Data to client through a 8 KiloBytes Buffer
		int count;
		byte[] buffer = new byte[ 8192 ];
		
		
		//Create a FileInputStream from the file  
		FileInputStream bytesFromFile = new FileInputStream( filepath.getPath() );

		//Reads up to 8 KBs of data from this input stream into an array of bytes called "count"
		//it returns -1 if EOF
		while ( (( count = bytesFromFile.read(buffer) )  != -1) ) {
					
			//Writes the data to our OutputStream
			data.write( buffer );
			data.flush();
		}
		out.flush();
		//maybe out needs close(?)
		
		bytesFromFile.close(); //close file resource 
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
		out.print( "HTTP/1.1 " + codeStatus + "\r\n" ); //
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
	public static String getLastModifiedDate( long timeDate )
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
    public static String getFileSize( File file )
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
	
	public static String getMimeExtension(String extensionForMime) throws Exception { //File filepath
		Properties mimeMap = new Properties();
		String extensionForUse;
		FileInputStream mime_types;
		
		//Create a FileInputStream from the mime-types .txt file
		mime_types = new FileInputStream( "mime-types.txt" );
		//load it to the "mimeMap" Properties Object 
		mimeMap.load(mime_types);
		//get its appropriate Content-Type and return it
		extensionForUse = mimeMap.getProperty(extensionForMime);
		
		return extensionForUse;
	}
}