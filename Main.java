import java.io.*;
import java.net.*;
import java.util.*;
public class Main{
	
	public static void makeResp(String str,PrintWriter pw) throws Exception{
		
		String[] tokens = str.split(" ");
		String s="";
		String resp="";
		String path = "C:\\Users\\Κατερίνα\\Desktop\\root.txt";
		//path+= tokens[1];
		File f = new File(path);
		System.out.println(resp);
		Date today = new Date();
		resp="HTTP/1.1 404 NOT FOUND\r\n" ;
		resp += today +"\r\n";
		
		pw.println(resp);
		StringBuilder htmlBuilder =new StringBuilder();
		htmlBuilder.append("<!DOCTYPE html>");
		htmlBuilder.append("<html>");
		htmlBuilder.append("<head>");
		htmlBuilder.append("<title>ERROR 404</title>");
		htmlBuilder.append("<body>");
		
		
		htmlBuilder.append("<a href=\"file:////C:\\Users\\Κατερίνα\\Desktop\\try.txt\">Link 1</a>");
		//htmlBuilder.append("<a href=\"file:///C/Users/Κατερίνα/Desktop/try.txt\">Link 1</a>");
		//StringBuilder str_try."<a href=\"file:///C:\\Users\\Κατερίνα\\Desktop\\try.txt\">Link 1</a>";
		//System.out.println(str_try);
		//System.out.println(str_try);
		//htmlBuilder.append(str_try);
		//htmlBuilder.append("<a href=\"google.com\">this</a>");
		//htmlBuilder.append("<a href=\"file:///" + path+"\">" + f.getName()+"</a>");
		//htmlBuilder.append("<a href="+ f.getCanonicalPath()+ ">"+f.getName()+"</a>");
		
		
		
		htmlBuilder.append("</body>");
		htmlBuilder.append("</html>");
		resp=htmlBuilder.toString();
		pw.println(resp);
		pw.flush();
		pw.close();
	}

	public static void main(String[] args){
		
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
		PrintWriter toClient = new PrintWriter(client.getOutputStream());;
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
						makeResp(Req,toClient);
						
					}
				}
			}
			catch(Exception ex){}
		}
		
		
	}
	catch(Exception e){}
		
	}
	
	
}