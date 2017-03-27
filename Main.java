import java.io.*;
import java.net.*;
import java.util.*;
public class Main{
	
	public static void makeResp(String str,PrintWriter pw){
		System.out.println("in make resp arrrivew:"+str);
		String[] tokens = str.split(" ");
		String s="";
		String resp="";
		String path = "C:\\Users\\Κατερίνα\\Desktop";
		path+= tokens[1];
		File f = new File(path);
		
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
		htmlBuilder.append("<p>Error 404 not found</p>");
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
		String Req="";
		while ((Req = fromClient.readLine()) != null) {
			//toClient.println(Req);
			System.out.println(Req);
			//use this to send response
			String[] tokens = Req.split(" ");
			if(tokens[0].equals("GET")){
				System.out.println("enters");
				makeResp(Req,toClient);
			}
		}
		
		
	}
	catch(Exception e){}
		
	}
	
	
}