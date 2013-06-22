package net.zwerks.sshshell;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DumpReceiver implements Runnable {
	
	private ServerSocket serverSock;
	private Socket connectedSock;
    private int serverPort;
    private String OutputPath;
    private String outputFilename;
    //private InputStream inStream;

	public DumpReceiver(String FileDumpDir, String DumpFileName, int listenPort){
		// TODO Auto-generated constructor stub
		this.OutputPath = FileDumpDir;
		this.outputFilename = DumpFileName;
		this.serverPort = listenPort;
		this.connectedSock = null;
		//this.inStream = null;
		
		System.out.println("**********************************************************");
		System.out.println("Dumper Activated.");
		System.out.println("File output path: "+ this.OutputPath+this.outputFilename);
		System.out.println("**********************************************************");
		
		//InputStream is = null;
		//this.serverPort = listenPort;
		
	}

	//@Override
	public void run() {

		InputStream inStream = null;
		//while(true){
		System.out.println("Attempting to receive file ...");
			if(this.connectedSock == null){
				try{
					//ServerSocket servSock = new ServerSocket(serverPort);
					//Socket socket = servSock.accept();
					//this.serverSock = new ServerSocket(this.serverPort);
					//this.connectedSock = serverSock.accept();
					
					Socket myConnectedSock = new Socket("127.0.0.1", this.serverPort);
					
					inStream = myConnectedSock.getInputStream();
					
					this.connectedSock = myConnectedSock;
					
				} catch(IOException ioex){
					System.out.println("Caught I/O Exception while connecting: "+ioex);
					ioex.printStackTrace();
					//wait(0);
					//System.exit(0);
				}
			}
			
			
			byte[] aByte = new byte[1];
	        int bytesRead;
			
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        
	        
	        if (inStream != null) {
	        	FileOutputStream fos = null;
	            BufferedOutputStream bos = null;
	        	
	        	System.out.println("Connected on: "+ this.connectedSock.getLocalAddress()+":"+this.connectedSock.getLocalPort());
	        	System.out.println("Receiving file from: "+ this.connectedSock.getInetAddress()+":"+this.connectedSock.getPort());
	            
	            try {
	                fos = new FileOutputStream( this.OutputPath + this.outputFilename );
	                System.out.println("Ready to write file to: " + this.OutputPath + this.outputFilename);
	                
	                //fos.
	                //bos.
	                
	                bos = new BufferedOutputStream(fos);
	                System.out.println("Bytes available: " + inStream.available());
	                
	                bytesRead = inStream.read(aByte, 0, aByte.length);

	                do {
	                        baos.write(aByte);
	                        bytesRead = inStream.read(aByte);
	                } while (bytesRead != -1);

	                bos.write(baos.toByteArray());
	                bos.flush();
	                bos.close();
	                connectedSock.close();
	            } catch (IOException ex) {
	            	System.out.println("Caught I/O Exception transferring file: "+ex);
	            }
	        }
		//}
		
	}
	
	public void listen(){

		
	}



}
