import java.net.*;
import java.io.*;
 
public class url {
  public static void main(String[] args) {    
    URL url=null;
    try {   
      url = new URL(args.length>0 ? args[0] : "http://feeds.bbci.co.uk/news/rss.xml");
     
	 BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()) );
 
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        System.out.println("new\n\n\n "+inputLine);
      }
      in.close();
    } 
    catch(MalformedURLException ex) {
      System.out.println("Malformed URL: +"+ args[0] );
      ex.printStackTrace();
    }
    catch(IOException ex) {
      System.out.println("Error while reading or writing from URL: "+url.toString() );
    }
  }
}