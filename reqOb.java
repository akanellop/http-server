//package ce325.hw2;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;



public class reqOb{
	public String request; 
	public String userStr;
	public String remoteAd;
	public PrintWriter outStream;
	public OutputStream dataStream;
	
	public reqOb(String req,String usr,String ad, PrintWriter out,OutputStream data){
		request=req;
		userStr=usr;
		remoteAd=ad;
		outStream=out;
		dataStream=data;
	}
}

