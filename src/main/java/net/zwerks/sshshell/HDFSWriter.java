package net.zwerks.sshshell;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;


//NOT USED CURRENTLY
public class HDFSWriter {

	private String FileOutputPath;
	private Configuration hdfsConf;
	
	
	public HDFSWriter(String theOutputPath){
		this.hdfsConf = new Configuration();
		this.hdfsConf.addResource(new Path("/usr/local/hadoop/conf/core-site.xml"));
		this.hdfsConf.addResource(new Path("/usr/local/hadoop/conf/hdfs-site.xml"));
	}
	
	public void write(String myOutputPath, Configuration myHDFSConf){
		

		FileSystem myHDFS = null; // = new FileSystem;
		try {
			myHDFS = FileSystem.get(myHDFSConf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Caught IO Exception while getting HDFS Configuration");
			e.printStackTrace();
		}

		
		Path myPath = new Path(myOutputPath);
		
		//Check if file already exists on Output Path
		try {
			myHDFS.exists(myPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("File at " + myPath + " already exists.");
			e.printStackTrace();
		}
	}
}
