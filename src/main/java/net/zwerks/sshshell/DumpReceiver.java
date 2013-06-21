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
    private InputStream inStream;

	public DumpReceiver(String FileDumpDir, String DumpFileName, int listenPort){
		// TODO Auto-generated constructor stub
		this.OutputPath = FileDumpDir;
		this.outputFilename = DumpFileName;
		this.serverPort = listenPort;
		
		System.out.println("File output path: "+ this.OutputPath+this.outputFilename);
		
		InputStream is = null;
		//this.serverPort = listenPort;
		System.out.println("Connecting ...");
		
	}

	//@Override
	public void run() {

		while(true){
			
			if(serverSock == null){
				try{
					serverSock = new ServerSocket(this.serverPort);
					connectedSock = serverSock.accept();
					
					this.inStream = connectedSock.getInputStream();
					
				} catch(IOException ioex){
					System.out.println("Caught I/O Exception while connecting: "+ioex);
					ioex.printStackTrace();
					//wait(0);
					System.exit(0);
				}
			}
			
			
			byte[] aByte = new byte[1];
	        int bytesRead;
			
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        FileOutputStream fos = null;
            BufferedOutputStream bos = null;
	        
	        if (this.inStream != null) {
	        	System.out.println("Receiving file from: "+ this.connectedSock.getInetAddress());
	            
	            try {
	                fos = new FileOutputStream( this.OutputPath + this.outputFilename );
	                System.out.println("Writing file to:" + this.OutputPath + this.outputFilename);
	                bos = new BufferedOutputStream(fos);
	                bytesRead = this.inStream.read(aByte, 0, aByte.length);

	                do {
	                        baos.write(aByte);
	                        bytesRead = this.inStream.read(aByte);
	                } while (bytesRead != -1);

	                bos.write(baos.toByteArray());
	                bos.flush();
	                bos.close();
	                connectedSock.close();
	            } catch (IOException ex) {
	            	System.out.println("Caught I/O Exception transferring file: "+ex);
	            }
	        }
		}
		
	}
	
	public void listen(){

		
	}



}
