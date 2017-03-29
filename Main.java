import java.io.*;
import java.net.*;
import java.util.*;
public class Main{
	
	public static int statusCode(String req, int flag){ // isos xreiazontai kai alla orismata
		String[] requestArray = req.split(" ");
		int codeStatus;
		//xreiazetai diafotetiko path
		requestArray[1]=requestArray[1].substring(1);
		//String path=("file:///C:\\Users\\Κατερίνα\\Desktop\\"+(requestArray[1]));
		String path=("C:\\Users\\Κατερίνα\\Desktop\\"+(requestArray[1]));
		File file = new File(path);
		
		System.out.println("\n\n"+path+"\n\n");
		if( file.exists()){
			System.out.println("\n\ncheck\n\n");
		}
		if(flag==1){ //flag is 1 only if the status code is called by an exception (? maybe wrong)
			codeStatus = 4 ;//505 Internal Error
			return codeStatus;
		}
		else if((requestArray.length != 3) || (! (requestArray[2].equals("HTTP/1.1")))){
			codeStatus = 1; //400 Bad Request
			return  codeStatus;
		}
		else if (!requestArray[0].equals("GET") ) {
			codeStatus = 3 ; //403 Method Not Allowed
			return codeStatus;
		}
		else if(! file.exists()){
			codeStatus = 2; // 404 File not found
			return codeStatus;
		}
		else{
			return 0;// 200 OK
		}
	}
	
	public static void makeHTTPresp(int status,String path,String host, PrintWriter pw){
		File file=new File(path);
		String resp=("");
		String title;
			switch (status)
			{
				case 0:
					title = "200 OK";
					break;
				case 1:
					title = "400 Bad Request";
					break;
				case 2:
					title = "404 File Not Found";
					break;
				case 3:
					title = "405 Method Not Allowed";
					break;
				case 4:
					title = "505 Internal Server Error";
					break;
				default:
					title = "Unknown Error appeared";
			}
			
		resp += ("HTTP/1.1 " + title +"\n");
		Date date = new Date();
		resp += (date+"\n");
		resp += ("Server: CE325 (Java based server)  \r\n");
		resp +=("Last-Modified: " + file.lastModified() +"\n");
		resp +=("Content-Length: "+ file.length()+"\n");
		resp += ("Connection: close  \n");
		resp += ("Content- Type: text/plain \r\n");
		
		System.out.println(resp);
		pw.println(resp);
		
		pw.flush();
		
		
	}
	
	public static void	makeHTMLresp(int status,String path, PrintWriter pw){
		File file=new File(path);
		String resp="";
		StringBuilder htmlBuilder =new StringBuilder();
		htmlBuilder.append("<!DOCTYPE html>");
		htmlBuilder.append("<html>");
		htmlBuilder.append("<head>");
		htmlBuilder.append("<title>HTTP_SERVER</title>");
		htmlBuilder.append("<body>");
		
		switch (status)
			{
				case 0:
					//needs correct code		
					break;
				case 1:
					htmlBuilder.append("<p>400 Bad Request</p>");
					break;
				case 2:
					htmlBuilder.append("<p>404 File Not Found</p>");
					break;
				case 3:
					htmlBuilder.append("<p>405 Method Not Allowed</p>");
					break;
				case 4:
					htmlBuilder.append("<p>505 Internal Server Error</p>");
					break;
				default:
					htmlBuilder.append("<p>Unknown Error appeared</p>");
			}
		
		htmlBuilder.append("</body>");
		htmlBuilder.append("</html>");
		resp=htmlBuilder.toString();
		pw.println(resp);
		pw.flush();
		
		
		
	}
	
	public static void makeResp(String Line1,String Line2,PrintWriter pw) throws Exception{
		String[] tokens1=Line1.split(" ");
		String[] tokens2=Line2.split(" ");
		String path="file:///C:\\Users\\Κατερίνα\\Desktop\\"+tokens1[1];
		//Calling method for status code
		int status = statusCode(Line1,0);
		//Calling method to send html
		makeHTMLresp(status, path,pw);
		
		//Calling method to send HTTP
		makeHTTPresp(status,path,tokens2[1],pw);
		
		pw.close();
		
		
	}

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
							
							makeResp(Line1,Line2,toClient);
						}
					}
				}
				catch(Exception ex){}
			}
			
			
		}
		catch(Exception e){}
			
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