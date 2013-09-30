package net.zwerks.dumpfs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/*
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.*;
import org.bouncycastle.crypto.digests.*;
*/

//import com.twmacinta.util.MD5;

/*
 * USING ONLY NATIVE JAVA CRYPTO API'S
 * NO BOUNCY CASTLE, FAST MD5, APACHE COMMONS DIGESTUTILS
 * */

public class HashCreator {

	private FileSystem activeFileSystem;	// <<<----org.apache.hadoop.fs.FileSystem
	private File myFile;
	private FSDataInputStream hdfsDIS;
	//private FileInputStream fis;
	//ObjectInputStream ois;
	
	public HashCreator(String FilePath) {
		// TODO Auto-generated constructor stub
		this.myFile = new File(FilePath);
		
	}
	
	public void AssignFileSystem(FileSystem myFileSystem){
		this.activeFileSystem = myFileSystem;
	}

	public String generateHash(String myHashType){
		//HashType is ---> "MD5", "SHA1", "SHA256"
		try {
			Path hdfsFilePath = new Path(this.myFile.getPath());
			//Testing to see if the path exists.
			if(this.activeFileSystem.exists(hdfsFilePath)){
				//System.out.println("HDFS Output Path: " + hdfsFilePath.toString() + " EXISTS: ---> File Written");
			}
			this.hdfsDIS = this.activeFileSystem.open(hdfsFilePath);
			//this.fis = new FileInputStream(this.activeFileSystem.open(hdfsFilePath).);
			//this.fis = new FileInputStream(this.hdfsDIS.getFileDescriptor());
			//this.fis = new FileInputStream(this.myFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error (File not found): " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//FileInputStream inputStream = null;

		//FileInputStream FinputStream = this.fis;
		
		
		if(this.hdfsDIS ==null){

	        return null;
	    }
	    MessageDigest md;
	    try {
	        md = MessageDigest.getInstance(myHashType);  // <<<---- myHashType = "MD5" "SHA1" or "SHA256"
	        byte[] buffer = new byte[1024];
	        
	        /*
	        FileChannel channel = FinputStream.getChannel();
	        ByteBuffer buff = ByteBuffer.allocate(2048);
	        
	        while(channel.read(buff) != -1){
	            buff.flip();
	            md.update(buff);
	            buff.clear();
	        }
	        */
	        int numRead;

	        do {
	            numRead = this.hdfsDIS.read(buffer);
	            if (numRead > 0) {
	                md.update(buffer, 0, numRead);
	            }
	        } while (numRead != -1);

	        //this.hdfsDIS.close();
	        
	        byte[] hashValue = md.digest();
	        
	        //return new String(hashValue);
	        return this.toHex(hashValue);
	    }
	    catch (NoSuchAlgorithmException e){
	        return null;
	    } 
	    catch (IOException e){
	        return null;
	    }
	    finally{
	        try {
	            if(this.hdfsDIS!=null)this.hdfsDIS.close();
	        } catch (IOException e) {

	        }
	    } 
	}
	
	public static String toHex(byte[] bytes) {
	    BigInteger bi = new BigInteger(1, bytes);
	    return String.format("%0" + (bytes.length << 1) + "X", bi);
	}
	
}
