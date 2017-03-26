import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

public class ServerRequest 
{
	//code for socket(?)
	
	public ServerRequest()
	{
		//constructor shit(?)
		
	}
	
	public void response(String responseCode)
	{
		String title;
			switch (responseCode)
			{
				case "200":
					title = "OK";
					break;
				case "400":
					title = "Bad Request";
					break;
				case "404":
					title = "File Not Found";
					break;
				case "405":
					title = "Method Not Allowed";
					break;
				case "500":
					title = "Internal Server Error";
					break;
				default:
					title = "Unknown Error appeared";
			}
			
			//LOGIKA HTML XTISIMO ??!
			
			//build response step by step, don't forget \n
			/*
			HTTP/1.1 200 OK
			
			Date: Mon, 23 Mar 2015 16:55:25 GMT
			//Date date = new Date();
			
			Server: CE325 (Java based server)
			//String serverName="Server: CE325 (Java based server)";
			
			Last-Modified: Mon, 23 Mar 2015 15:04:54 GMT 
			//LastModified(file.lastModified)
			
			Content-Length: 37
			// ??? ? ? ? ? ? 
			
			Connection: close
			//String Connection  = "Connection: close";
			
			Content-Type: text/plain
			//String contentType = "Content-Type: " + getMIMEtype();
			
			//display file 
			Just another test file!
			Hello World!
			*/
	}
	
	public String LastModified(long time)
	{
		//gets time that file was last modified and converts it to our format
		//System.out.println("Before Format : " + file.lastModified());
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));//set it GMT
		
		/*String str = sdf.format(time);
		return str;*/
		
		return sdf.format(time);
	}
	
	public String FileSize(File file)	//we should check FIRST if it exists
	{
		double bytes = file.length();//length of file in bytes
		
		double kilobytes = (bytes / 1024);
		double megabytes = (kilobytes / 1024);
		double gigabytes = (megabytes / 1024);

		//print in GB/MB/KB according to the size
		//%1.f = floating point number with 1 decimal
		if ( gigabytes >= 1 ) return String.format( "%.1f GBs", gigabytes );
        else if ( megabytes >= 1 ) return String.format( "%.1f MBs", megabytes );
        else if ( kilobytes >= 1 ) return String.format( "%.1f KBs", kilobytes );
        else return String.format( "%.1f Bytes", bytes );
            
	
	}
}