import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.nio.file.Files;
import java.util.Date;

public class Test1 {
	public static void main(String[] args) throws IOException {
    
		int portNumber = 8000;

		try{ 
			ServerSocket serverSocket = new ServerSocket(portNumber);
			Socket clientSocket = serverSocket.accept();   

			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);           
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
				

			String inputLine, readFile;
			String root = "C:\\root\\"; 

			Date today = new Date();
	
		
			/*while ((inputLine = in.readLine()) != null) {
				
				out.println(inputLine);
				
				System.out.println("echoing: "+inputLine);
			}*/
		
			inputLine = in.readLine();
			String [] parts = inputLine.split(" "); 
			
			if (parts[1].matches("(.*)%20(.*)")){
				parts[1]=parts[1].replaceAll("%20", " ");
			}
		
			File filepath= new File (root + parts[1]);
			
			
			if(!parts[0].equals("GET")){
				out.println("405 Method Not Allowed!");
			}
			else if(!filepath.exists()){
				out.println("404 Not Found!");
				out.println(root + parts[1]);
			}
			else if((parts[2]== null ) ||															//&& parts[3]== null)
					(!(parts[2].equals("HTTP/1.1")) && !(parts[2].equals("HTTP/1.0")))){
				out.println("400 Bad Request!");
			}
			else{
				out.println("\r\n");
				out.println("HTTP/1.1 200 OK");
				out.println("Date: " + today);
				out.println("Server: CE325 (Java based server)");
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
			System.out.println("500 Internal Server Error!");
			System.out.println(e.getMessage());
		}
	}
}