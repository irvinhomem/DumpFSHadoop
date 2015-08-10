package net.zwerks.dumpfs;

import java.io.InputStream;

import javax.swing.JOptionPane;

import net.zwerks.dumpfs.SSHExec.MyUserInfo;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class DumpFSHadoop {

	private String UserOnHost2Dump;
	private String Host2Dump;
	private Session openSession;
	//private DumpFSStatistics myStatistics;
	private int myLocalPort;
	private int myRemotePort;
	
	public DumpFSHadoop() {
		// TODO Auto-generated constructor stub
		
		//Print out OS that CBB is running from
		System.out.println("------------------------------------");
		System.out.println("CBB FileSystem Dumper running on: " + System.getProperty("os.name").toUpperCase() + "...");
		this.myLocalPort = 48000; //54137 <<-- Was working with old setup
		this.myRemotePort = 57314;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//For Benchmarking the time
		long progStartTime = System.currentTimeMillis();
		DumpFSStatistics myStatistics = new DumpFSStatistics();
		myStatistics.setAppStartTime(progStartTime);
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("~~~~~~~~~~~~~~~ Application Timer Started ... ("+ progStartTime +")~~~~~~~~~~~~~~");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		//Initialize the FSDumper
		DumpFSHadoop myFSDumper= new DumpFSHadoop();
		//myFSDumper.InitiateConnection("root", "", "192.168.1.150", 22);				// <<<---- Chumby
		//myFSDumper.InitiateConnection("root", "admin", "192.168.0.105", 22);		// <<<---- MT4GS
		//myFSDumper.InitiateConnection("root", "admin", "192.168.1.182", 22);		// <<<---- MT4GS
		//myFSDumper.InitiateConnection("root", "admin", "192.168.1.110", 22);		// <<<---- HTC Incredible S
		//myFSDumper.InitiateConnection("root", "admin", "192.168.0.103", 22);		// <<<---- HTC Incredible S
		//myFSDumper.InitiateConnection("root", "admin", "192.168.1.116", 22);		// <<<---- GalaxyTab 2
		//myFSDumper.InitiateConnection("root", "admin", "192.168.0.103", 22);		// <<<---- GalaxyTab 2
		//myFSDumper.InitiateConnection("root", "admin", "192.168.20.6", 22);		// <<<---- Nexus 5
		myFSDumper.InitiateConnection("root", "admin", "192.168.20.10", 2222);		// <<<---- Galaxy S4-I9506 (SSHDroid App)
		//myFSDumper.InitiateConnection("root", "qwe123", "192.168.20.10", 55925);		// <<<---- Galaxy S4-I9506 (SSH Server App)
		
		//myFSDumper.doPortForwardingL(54137, "127.0.0.1", 57314); //was working with old setup//
		myFSDumper.doPortForwardingL(myFSDumper.getMyLocalPort(), "127.0.0.1", myFSDumper.getMyRemotePort());
		
		//myFSDumper.StartLocalNetCat(54137);
		//String CommandToExecute = "set|grep SSH";
		//---->//
		//String CommandToExecute = "dd if=/dev/mtdblock5 | gzip | nc -l -p 57314 -w 10";				//Chumby
		//String CommandToExecute = "dd if=/dev/mtdblock1 | gzip | nc -l -p 57314 -w 10";				//Chumby
		/**Nexus 5**/
		//String CommandToExecute = "dd if=/dev/block/platform/msm_sdcc.1/by-name/modem | gzip | nc -l -p 57314 -w 10";		//Nexus 5 <<<---- /firmware (64MB)
		//String CommandToExecute = "dd if=/firmware | gzip | nc -l -p 57314 -w 10";		//Nexus 5 <<<---- /firmware (64MB) (*Failed /firmware is a directory)
		
		/**Galaxy S4-I9506**/
		String CommandToExecute = "su root -c 'dd if=/dev/block/platform/msm_sdcc.1/by-name/modem | gzip | nc -l -p 57314 -w 10'";		//Nexus 5 <<<---- /firmware-modem (64MB)
		/**MT4GS**/
		//String CommandToExecute = "dd if=/dev/block/mmcblk0p19 | gzip | nc -l -p 57314 -w 10";	//MT4GS <<<---- /vendor/firmware/adsp  (16MB)
		//String CommandToExecute = "dd if=/dev/block/mmcblk0p24 | gzip | nc -l -p 57314 -w 10";	//MT4GS <<<---- /cache	(118.1MB)		
		//String CommandToExecute = "dd if=/dev/block/mmcblk0p17 | gzip | nc -l -p 57314 -w 10";	//MT4GS <<<---- /vendor/firmware/misc	(199.8MB)
		//String CommandToExecute = "dd if=/storage | gzip | nc -l -p 57314 -w 10";	//MT4GS <<<---- /storage <---Doesn't work
		//String CommandToExecute = "dd if=/dev/block/mmcblk0p22 | gzip | nc -l -p 57314 -w 10";	//MT4GS <<<---- /system
		//String CommandToExecute = "dd if=/dev/block/mmcblk0p23 | gzip | nc -l -p 57314 -w 10";	//MT4GS <<<---- /data	(1.1GB)
		//String CommandToExecute = "dd if=/dev/block/vold/179:65 | gzip | nc -l -p 57314 -w 10";		//MT4GS <<<---- /storage/sdcard0 (7.3GB)
		/**Galaxy Tab**/
		//String CommandToExecute = "dd if=/dev/block/platform/omap/omap_hsmmc.1/by-name/EFS | gzip | nc -l -p 57314 -w 10";	//GalaxyTab 2 <<<---- /efs (19MB)
		//String CommandToExecute = "dd if=/dev/block/platform/omap/omap_hsmmc.1/by-name/CACHE | gzip | nc -l -p 57314 -w 10";	//GalaxyTab 2 <<<---- /cache (688MB)
		//String CommandToExecute = "dd if=/dev/block/platform/omap/omap_hsmmc.1/by-name/DATAFS | gzip | nc -l -p 57314 -w 10";	//GalaxyTab 2 <<<---- /data (4GB)
		//String CommandToExecute = "dd if=/dev/fuse | gzip | nc -l -p 57314 -w 10";	//GalaxyTab 2 <<<---- /storage/sdcard0 (4GB)
		//String CommandToExecute = "dd if=/dev/block/vold/179:25 | gzip | nc -l -p 57314 -w 10";  //Galaxy Tab 2 <<<---- /storage/extSdCard
		/**HTC Incredible S**/
		//String CommandToExecute = "dd if=/dev/block/mmcblk0p26 | gzip | nc -l -p 57314 -w 10";	//HTC Incredible S <<<---- /data	(1GB)
		//String CommandToExecute = "dd if=/dev/block/mmcblk0p27 | gzip | nc -l -p 57314 -w 10";	//HTC Incredible S <<<---- /cache	(1GB)
		//String CommandToExecute = "dd if=/dev/block/mmcblk0p29 | gzip | nc -l -p 57314 -w 10";	//HTC Incredible S <<<---- /system/lib	(1GB)
		//String CommandToExecute = "dd if=/dev/block/mmcblk0p25 | gzip | nc -l -p 57314 -w 10";	//HTC Incredible S <<<---- /system	(1GB)
		//String CommandToExecute = "dd if=/dev/block/vold/179:65 | gzip | nc -l -p 57314 -w 10";	//HTC Incredible S <<<---- /mnt/sdcard	(1GB)
		//String CommandToExecute = "dd if=/dev/block/vold/179:64 | gzip | nc -l -p 57314 -w 10";	//HTC Incredible S <<<---- /mnt/sdcard	(1GB)
		//
		//String CommandToExecute = "echo '1234567zxckhgjgh' | gzip | nc -l -p 57314 -w 10";		//Testing
				
		//myFSDumper.StartRemoteDD2Netcat(CommandToExecute);
		RemoteDD2Netcat myDD2Netcat = new RemoteDD2Netcat(CommandToExecute, myFSDumper.getOpenSession());
		Thread dd2netcatThread = new Thread(myDD2Netcat);
		dd2netcatThread.start();
		//myDD2Netcat.send();
				
		myFSDumper.StartLocalNetCat(myFSDumper.getMyLocalPort(), myStatistics);
		
		//String stopCommand = "killall nc";
		//myFSDumper.StopRemoteNetcat(stopCommand);
	}
	
	public void InitiateConnection(String username, String secret, String hostname, int mySSHPort){
		try{
			//Create new Java Secure Channel
			JSch jsch=new JSch();
		    
			//Load known_hosts file 
			String KnownHostsFilePath = System.getProperty("user.dir") + this.getCurrOSPathFormat() + "known_hosts.txt";
			System.out.println(KnownHostsFilePath);
			System.out.println("------------------------------------");
			jsch.setKnownHosts(KnownHostsFilePath);
			
			//Get the username and hostname of the machine/host to dump
			this.Host2Dump = hostname;
			this.UserOnHost2Dump = username;
			
			//Open an SSH Session using the above parameters
			Session session = jsch.getSession(this.UserOnHost2Dump, this.Host2Dump, mySSHPort);
			
			session.setPassword(secret);
			
			/*
			// username and password will be given via UserInfo interface.
			UserInfo ui=new MyUserInfo();
			session.setUserInfo(ui);
			*/
			
			session.connect();
			this.openSession = session;
			
		      
		    }
		    catch(Exception e){
		      System.out.println(e);
		    }
	}
	
	public void doPortForwardingL(int lport, String rhost, int rport){
		Session currSession = this.getOpenSession();
		try{
			int assigned_port = currSession.setPortForwardingL(lport, rhost, rport);
			System.out.println("localhost:"+assigned_port+" -> "+rhost+":"+rport);
		}
		catch(JSchException ex){
			System.out.println("Caught Exception while doing Local Port Forwarding: \n" + ex);
		}
	}
	
	public void StartLocalNetCat(int listenPort, DumpFSStatistics statsCollector){
				
		DumpReceiver dumpRcvr = new DumpReceiver(System.getProperty("user.dir") + this.getCurrOSPathFormat(), "myDumpFile.ida.gz", listenPort, statsCollector);
		Thread t = new Thread(dumpRcvr);
		try{
			t.sleep(1000);
		} catch(InterruptedException iex){
			System.out.println("Thread exception with Dumper: "+iex);
		}
		
		t.start();
		//dumpRcvr.listen(listenPort);
		
	}
	
	public void StopRemoteNetcat(String command){
		try{
			Session currSession = this.getOpenSession();
			Channel channel = currSession.openChannel("exec");
			((ChannelExec)channel).setCommand(command);

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
			currSession.disconnect();
		}catch(Exception ex){
			
		}
	}
	
	public Session getOpenSession(){
		return this.openSession;
	}
	
	public String getCurrOSPathFormat(){
		String myCurrOS =  System.getProperty("os.name").toLowerCase();
		String pathFormat = "";
		
		if (myCurrOS.equals("linux")){
			pathFormat = "/";
		}else if(myCurrOS.startsWith("win")){
			pathFormat = "\\";
		}
		
		System.out.println(pathFormat);
		
		return pathFormat;
	}
	
	public int getMyLocalPort(){
		return this.myLocalPort;
	}
	
	public int getMyRemotePort(){
		return this.myRemotePort;
	}
}

