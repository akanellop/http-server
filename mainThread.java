import java.io.*;
import java.net.*;
//import java.util.Date;
import java.util.*;


public class mainThread {
public static void main(String[] args) throws IOException{
	String req;
	String resp;
	Scanner inputStream=null;
	PrintWriter outputStream=null;
	//pairno port apo terminal
	int portNumber = Integer.parseInt(args[0]);
	resp="HTTP/1.1 200 OK";
	//dimioyrgo server kclient pano sto port- ginetai i sindesi
	ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
    Socket clientSocket = serverSocket.accept();   
	
	try{
	//dimiourgo ta stremas epikoinonias
	inputStream =new Scanner(new InputStreamReader(clientSocket.getInputStream()));
	//to inputStream perimenei na diavazei oti tha steilei o cliennt
	outputStream= new PrintWriter(new DataOutputStream(clientSocket.getOutputStream()));
	//to output stream grafei ston client
	
	//o server trexei sinexeia kai perimenei requests
	while(true){
		//otan vrei request pernaei to minima tou client sto output stream
		 while ((req = inputStream.nextLine()) != null) {
			 //outputStream.println(req);
			 System.out.println("echoing: "+req);
		 }
		 outputStream.println(resp);
	}
	}
	catch(Exception e){}
  }

}