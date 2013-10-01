package net.zwerks.dumpfs;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


public class DumpReceiver implements Runnable {
	
	private ServerSocket serverSock;
	private Socket connectedSock;
    private int serverPort;
    private String OutputPath;
    private String outputFilename;
    private String HDFSOutputPath;
    private DumpFSStatistics currStats;
    private HashCreator myHashGen;
    
    private FileSystem hdfs;

	public DumpReceiver(String FileDumpDir, String DumpFileName, int listenPort, DumpFSStatistics myStatsCollector){
		// TODO Auto-generated constructor stub
		//this.OutputPath = FileDumpDir;				//Directory where incoming file is to be dumped
		this.outputFilename = DumpFileName;			//File name of incoming dump file
		this.HDFSOutputPath = "/user/hadoop_user/IDA_ARCHIVE/";					//Hadoop HDFS Chosen Dump Directory
		this.serverPort = listenPort;
		this.connectedSock = null;
		//this.inStream = null;
		this.currStats = myStatsCollector;
		this.myHashGen = new HashCreator(this.HDFSOutputPath+this.outputFilename);
		
		System.out.println("**********************************************************");
		System.out.println("Dump Receiver Activated.");
		System.out.println("File output path: "+ this.HDFSOutputPath+this.outputFilename);
		System.out.println("**********************************************************");
	}

	//@Override
	public void run() {

		InputStream inStream = null;
		//while(true){
		System.out.println("Attempting to receive compressed file ...");
			if(this.connectedSock == null){
				try{
					
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
			
			long startCopyTime = System.currentTimeMillis();
			this.currStats.setFileTransStartTime(startCopyTime);
			
			byte[] aByte = new byte[1];
	        int bytesRead;
			
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        
	        /*For Hadoop HDFS*/
	        Configuration myHDFSConfig = new Configuration();
	        
	        myHDFSConfig.addResource("/HADOOP_HOME/conf/core-site.xml");
	        myHDFSConfig.addResource("/HADOOP_HOME/conf/hdfs-site.xml");
	        // Using the full path: "/usr/local/hadoop"
	        //myHDFSConfig.addResource("/usr/local/hadoop/conf/core-site.xml");
	        //myHDFSConfig.addResource("/usr/local/hadoop/conf/hdfs-site.xml");
	        
	        /*---*/
	        
	        
	        if (inStream != null) {            
	            /*For Hadoop HDFS*/
	            //FSDataOutputStream hdfsout = null; 
	            /*---*/
	        	
	        	System.out.println("Connected on: "+ this.connectedSock.getLocalAddress()+":"+this.connectedSock.getLocalPort());
	        	System.out.println("Receiving file from: "+ this.connectedSock.getInetAddress()+":"+this.connectedSock.getPort());
	            
	            try {
	            	System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	            	System.out.println("+++++++++++++++++++++ Beginning HDFS Transactions ++++++++++++++++++++");
	            	System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	            	
	                /*For Hadoop HDFS Capability*/
	            	this.hdfs = FileSystem.get(myHDFSConfig);
	            	
	            	//For some strange reason "localhost" was not working, so i had to use the machine name
	            	//hdfs = FileSystem.get(new URI("hdfs://localhost:54310"), myHDFSConfig);
	            	this.hdfs = FileSystem.get(new URI("hdfs://cbb-node0:54310"), myHDFSConfig);
	            	
	            	//FSDataOutputStream hdfsout = hdfs.create(new Path(this.HDFSOutputPath + this.outputFilename));
	            	Path theHDFSOuputPath = new Path(this.HDFSOutputPath + this.outputFilename);
	            	System.out.println("HDFS Path to output to ... " + theHDFSOuputPath.toString());
	            	if(this.hdfs.exists(theHDFSOuputPath)){
	            		System.out.println("HDFS Output Path: " + theHDFSOuputPath.toString() + " EXISTS");
	            	}
	            	System.out.println("Current Working Directory is: " + this.hdfs.getWorkingDirectory());
	            	System.out.println("Current Home Directory is: " + this.hdfs.getHomeDirectory());
	            	//hdfs.
	            	
	            	FSDataOutputStream hdfsout = this.hdfs.create(theHDFSOuputPath);
	                ////////hdfsout = new FSDataOutputStream(fos, null);
	                /*---*/
	            	
	                System.out.println("------------------------------------------------");
	                System.out.println("Beginning File-write to HDFS ...");
	                System.out.println("------------------------------------------------");
	                System.out.println("Path to write file to: " + this.HDFSOutputPath + this.outputFilename);
	                
	                //System.out.println("Preparing to write");
	                
	                bytesRead = inStream.read(aByte, 0, aByte.length);
	                System.out.println("Incoming Bytes available: " + inStream.available());
	                System.out.println("Preparing to write " + inStream.available() + " bytes ...");

	                double byteCounter = 0;
	                int stepCounter = 0;
	                int stepCounterRound = 0;
	                
	                System.out.print("#");
	                do {
	                        baos.write(aByte);
	                        bytesRead = inStream.read(aByte);
	                        
	                        /*For Hadoop HDFS*/
	                        hdfsout.write(aByte, 0, aByte.length);
	                        /*---*/
	                        
	                        //Some sort of feedback that stuff is happening
	                        byteCounter++;

	                        if(byteCounter % 1000000 == 0){		
	                        	System.out.print(".");
	                        	if ((byteCounter/1000000) % 2 == 0){
	                        		int my50Counter = (int)byteCounter/1000000;
	                        		System.out.print(my50Counter);
	                        		//System.out.print(bytesRead);
	                        		if(my50Counter % 50 == 0){
	                        			System.out.println("|");
	                        		}
	                        	}
	                        }
	                        
	                } while (bytesRead != -1);
	                
	                /*For Hadoop HDFS*/
	                hdfsout.close();
	                
	                //Now that File has been written to HDFS, pass the FileSystem to the Hashcreator to calculate the hash
	                this.myHashGen.AssignFileSystem(this.hdfs);
	                /*---*/
	 
	                inStream.close();
	                
	                System.out.println("Transfer complete");
	                
	                connectedSock.close();
	                
	            } catch (IOException ex) {
	            	System.out.println("Caught I/O Exception transferring file: "+ex);
					System.out.println("IO Error while getting HDFS URI");
					ex.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					System.out.println("Error in HDFS URI Syntax, while getting HDFS URI");
					e.printStackTrace();
				}
		}
		//}
	        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        	System.out.println("+++++++++++++++++++++++++++ FILE HASHES ++++++++++++++++++++++++++++++");
        	System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	        
        	System.out.println("MD5 hash: " + this.myHashGen.generateHash("MD5"));
        	System.out.println("SHA1 hash: " + this.myHashGen.generateHash("SHA1"));
        	
        	/*For Hadoop HDFS*/
        	try {
				this.hdfs.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Failed while trying to close HDFS ...");
				e.printStackTrace();
			}
        	/*---*/
        	     	
	        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        	System.out.println("+++++++++++++++++++++++++++ RUN STATISTICS +++++++++++++++++++++++++++");
        	System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	        
	        //Stuff to output the time taken for the File Transfer transaction to complete
	        long finishCopyTime = System.currentTimeMillis();
	        this.currStats.setFileTransEndTime(finishCopyTime);
           
	        System.out.println("File transfer time: " + this.currStats.convertLongToStringTime(this.currStats.getFileTransTime()));
		
	        long progEndTime = System.currentTimeMillis();
	        this.currStats.setAppEndTime(progEndTime);
	        
	        System.out.println("Total Application run time: " + this.currStats.convertLongToStringTime(this.currStats.getAppRunTime()));
	}
	
	public void listen(){

		
	}



}
