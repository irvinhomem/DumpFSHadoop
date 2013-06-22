package net.zwerks.sshshell;

import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

public class RemoteDD2Netcat implements Runnable{ // implements Runnable why?
	/*Implements runnable, so that as soon as the RemoteDDNetcat command has 
	 * been sent for execution, the DumpReceiver can be started
	 */
	private Session currSession;
	private String command;
	
	public RemoteDD2Netcat(String theCommand, Session openSession) {
		this.currSession = openSession;
		this.command = theCommand;
		
		System.out.println("---------------------------------------");
		System.out.println("Starting Netcat on remote ...");
		System.out.println("---------------------------------------");
	}

	public void run() { //send
		try{
			//Session currSession = this.getOpenSession();
			Channel channel = this.currSession.openChannel("exec");
			((ChannelExec)channel).setCommand(this.command);

			channel.setInputStream(null);

			((ChannelExec)channel).setErrStream(System.err);

			InputStream in=channel.getInputStream();
			
			channel.connect();

			byte[] tmp=new byte[1024];

			while(true){
				while(in.available()>0){
					int i=in.read(tmp, 0, 1024);
					if(i<0)break;
					
					System.out.print(new String(tmp, 0, i));
				}
				if(channel.isClosed()){
					System.out.println("exit-status: "+channel.getExitStatus());
					break;
				}
				
				try{Thread.sleep(1000);}catch(Exception ee){}
			}
			channel.disconnect();
			this.currSession.disconnect();
		}catch(Exception ex){
			
		}
		
	}

}
