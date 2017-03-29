import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

public class Main{
	
	public static void main(String[] args){
		String Line1="";
		String Line2="";
		
		
		if (args.length != 1) {
		  System.err.println("Usage: java EchoServer <port number>");
		  System.exit(1);
		}
		
		//takes as arguments the port and the path for root server
		int portNumber = Integer.parseInt(args[0]);
		try{
			//creates server and client for specific socket
			ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
			Socket client = server.accept();  
			
			
			//code for  the client
			//for sending the output to client (sending response, maybe file?)
			PrintWriter toClient = new PrintWriter(client.getOutputStream());
			//for getting the input from client (getting request)
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			//reading from client
			
			//runs forever until you escape with CTRL+C
			while(true){
				String Req="";
				try{
					while ((Req = fromClient.readLine()) != null) {
						//toClient.println(Req);
						System.out.println(Req);
						//use this to send response
						String[] tokens = Req.split(" ");
						if(tokens[0].equals("GET")){
							Line1=Req;
							
						}
						else if(tokens[0].equals("Host:")){
							Line2=Req;
							
							//etsi douleuei mono gia http 1.1 ...
							makeResp(Line1,Line2,toClient);//move it to enother line
						}
					}
				}
				catch(Exception ex){}
			}
			
		}
		catch(Exception e){}
			
	}
	
	public static void makeResp(String Line1,String Line2,PrintWriter pw) throws Exception{
		String[] tokens1=Line1.split(" ");
		String[] tokens2=Line2.split(" ");
		
		System.out.println("tokens[1] = " + tokens1[1] );
		
		
		//String[] requestArray = req.split(" ");
		tokens1[1]=tokens1[1].substring(1); //eliminate first "/" character
		
		
		String path = ("C:\\Users\\kaslou\\Desktop\\"+tokens1[1]);
		//String path="C:\\Users\\Κατερίνα\\Desktop\\"+tokens1[1];
		
		//Calling method for statusCode
		int status = statusCode(Line1,0);
		
		//Calling method to send html
		makeHTMLresp(status, path,pw);
		
		//Calling method to send HTTP
		makeHTTPresp(status,path,tokens2[1],pw);
		
		pw.close();
		
	}
	
	public static int statusCode(String req, int flag){ // isos xreiazontai kai alla orismata
		
		//splits first line
		String[] requestArray = req.split(" ");
		int codeStatus;
		
		//xreiazetai diafotetiko path
		requestArray[1]=requestArray[1].substring(1); //eliminate first "/" character
		
		//String path=("file:///C:\\Users\\Κατερίνα\\Desktop\\"+(requestArray[1]));
		String path = ("C:\\Users\\kaslou\\Desktop\\"+requestArray[1]);
		//String path=("C:\\Users\\Κατερίνα\\Desktop\\"+(requestArray[1]));
		File file = new File(path);
		
		System.out.println("\n"+path+"\n");
		if( file.exists()){
			System.out.println("\nIT DOES EXIST\n\n");
		}
		else {
			System.out.println("\n\nFILE NOT FOUND\n\n");
		}
		
		
		//fix flag 
		if(flag==1){ //flag is 1 only if the status code is called by an exception (? maybe wrong)
			codeStatus = 500 ;//500 Internal Error
			System.out.println("500 internal error");
			return codeStatus;
		}
		else if((requestArray.length != 3) || (! (requestArray[2].equals("HTTP/1.1")))){
			codeStatus = 400; //400 Bad Request
			System.out.println("400 Bad Request");
			return  codeStatus;
		}
		else if (!requestArray[0].equals("GET") ) {
			codeStatus = 405 ; //405 Method Not Allowed
			System.out.println("405 Method not allowed");
			return codeStatus;
		}
		else if(! file.exists()){
			codeStatus = 404; // 404 Not Found
			System.out.println("404, file not found");
			return codeStatus;
		}
		else{
			System.out.println("200, everything is ok");
			return 200;// 200 OK
		}
	}
	
	public static void makeHTTPresp(int status,String path,String host, PrintWriter pw){
		File file=new File(path);
		//giati na xreiazomaste path?
		
		String resp=("");
		String title;
		
		if (file.exists() ) {
			long len=file.length();
			System.out.println(" file length: \n\n\n"+len);
		}
		else {
			System.out.println("path = " + path );
			System.out.println(" DOESNTWORK\n");
		}
		
		//e gia kapoio logo, to file doesnt exist edw
		
			switch (status)
			{
				case 200:
					title = "200 OK";
					break;
				case 400:
					title = "400 Bad Request";
					break;
				case 404:
					title = "404 File Not Found";
					break;
				case 405:
					title = "405 Method Not Allowed";
					break;
				case 500:
					title = "505 Internal Server Error";
					break;
				default:
					title = "Unknown Error appeared";
			}
			
			
		resp += ("HTTP/1.1 " + title +"\n<br><br>");
		Date date = new Date();
		resp += (date+"\n<br>");
		resp += ("Server: CE325 (Java based server)  \r\n<br>");
		resp +=("Last-Modified: " + getLastModifiedDate(file.lastModified()) +"\r\n<br>");
		resp +=("Content-Length: "+ file.length()+"\r\n<br>");
		resp += ("Connection: close  \r\n<br>");
		
		resp += ("Content- Type: text/plain \r\n<br>"); // get for each content- type
		
		//System.out.println(resp);
		pw.println(resp);
		
		pw.flush();
		
		
	}
	
	public static void	makeHTMLresp(int status,String path, PrintWriter pw){
		//File file=new File(path); //de nomizw pws to path xreiazetai
		String resp="";
		StringBuilder htmlBuilder =new StringBuilder();
		htmlBuilder.append("<!DOCTYPE html>\r\n<br>");
		htmlBuilder.append("<html>\r\n");
		htmlBuilder.append("<head>\r\n");
		htmlBuilder.append("<title>HTTP_SERVER</title>\r\n");
		htmlBuilder.append("<body>\r\n");
		
		switch (status)
			{
				case 200:
					//needs correct code	
					//send file
					/*
					
					html.append("<td class=\"link\"><a href=\"" + ext + "\">" + arxeio.getName() + "</a></td>");
					*/
					break;
				case 400:
					htmlBuilder.append("<p>400 Bad Request</p>");
					break;
				case 404:
					htmlBuilder.append("<p>404 File Not Found</p>");
					break;
				case 405:
					htmlBuilder.append("<p>405 Method Not Allowed</p>");
					break;
				case 500:
					htmlBuilder.append("<p>505 Internal Server Error</p>");
					break;
				default:
					htmlBuilder.append("<p>Unknown Error appeared</p>");
			}
		
		htmlBuilder.append("</body>\r\n");
		htmlBuilder.append("</html>\r\n");
		
		resp=htmlBuilder.toString();
		
		pw.println(resp);
		pw.flush(); //flush-save currently to PrintWriter
		
		
		
	}
	
	public static String getLastModifiedDate( long miliseconds ){
    
			String dateFormat = "EEE, d MMM YYYY HH:mm:ss z";
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat( dateFormat );
			dateFormatGmt.setTimeZone( TimeZone.getTimeZone( "GMT" ) );

        return dateFormatGmt.format( miliseconds );
    }
	

	
		
	
}


//URL CODE , MAYBE NEED TO WRITE ON URL ?
		/*URL myURL = new URL("http://localhost:8000");
		try {   
			BufferedReader in = new BufferedReader(new InputStreamReader(myURL.openStream()) );
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
			}
			in.close();
		} 
		catch(MalformedURLException ex) {
			//System.out.println("Malformed URL: +"+ args[0] );
			ex.printStackTrace();
		}
		catch(IOException ex) {
			//System.out.println("Error while reading or writing from URL: "+url.toString() );
		}*/
		
		//HTML code building the html file,, needind better reference
		/*
		//htmlBuilder.append("<link rel=\"alternate\" type=\"text\" title=\"Recent Changes\" href=\"/try.txt\">");
		//htmlBuilder.append("<a href=\"/try.txt\">Link 1</a>");
		//htmlBuilder.append("<a href=\""+path+">Link 1</a>");
		//htmlBuilder.append("<a href=\"file:///C/Users/Κατερίνα/Desktop/try.txt\">Link 1</a>");
		//StringBuilder str_try."<a href=\"file:///C:\\Users\\Κατερίνα\\Desktop\\try.txt\">Link 1</a>";
		htmlBuilder.append("<a href=\"file:///" + path+"\">" + f.getName()+"</a>");
	*/