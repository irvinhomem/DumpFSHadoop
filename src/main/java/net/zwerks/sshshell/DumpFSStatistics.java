package net.zwerks.sshshell;

import java.util.concurrent.TimeUnit;

public class DumpFSStatistics {

	private long AppStartTime;
	private long AppEndTime;
	private long FileTransStartTime;
	private long FileTransEndTime;
	
	public DumpFSStatistics(){
		
	}
	
	//Application Statistics
	public void setAppStartTime(long startTime){
		this.AppStartTime = startTime;
	}
	
	public void setAppEndTime(long endTime){
		this.AppEndTime = endTime;
	}
	
	public String printAppStartTime(){
		return convertLongToStringTime(this.AppStartTime);
	}
	
	public String printAppEndTime(){
		return convertLongToStringTime(this.AppEndTime);
	}
	
	public long getAppRunTime(){
		return (this.AppEndTime - this.AppStartTime);
	}
	
	//File TRANSFER statistics
	public void setFileTransStartTime(long startTime){
		this.FileTransStartTime = startTime;
	}
	
	public void setFileTransEndTime(long endTime){
		this.FileTransEndTime = endTime;
	}
	
	public String printFileTransStartTime(){
		return convertLongToStringTime(this.FileTransStartTime);
	}
	
	public String printFileTransEndTime(){
		return convertLongToStringTime(this.FileTransEndTime);
	}
	
	public long getFileTransTime(){
		return (this.FileTransEndTime - this.FileTransStartTime);
	}
	
	public String convertLongToStringTime(long timeValue){
		String strFormattedTime = String.format("%02d:%02d:%02d:%02d", 
	            TimeUnit.MILLISECONDS.toHours(timeValue),
	            TimeUnit.MILLISECONDS.toMinutes(timeValue) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeValue)),
	            TimeUnit.MILLISECONDS.toSeconds(timeValue) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeValue)),
	            TimeUnit.MILLISECONDS.toMillis(timeValue) - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(timeValue)));
	            
		return strFormattedTime;
	}
}
