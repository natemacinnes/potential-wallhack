package replicaLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class HeartbeatClient extends Thread {

	private boolean runClient;
	private String owner;
	private ReplicaInformation replicaInfo;
	
	
	
	public HeartbeatClient(String replicaName)
	{
		runClient = false;
		owner = replicaName;
		replicaInfo = new ReplicaInformation();
	}
	
	public void stopClient()
	{
		runClient = false;
	}
	
	
	@Override
	public void run()
	{
    	DatagramSocket aSocket = null;
    	runClient = true;
		try{
			// create socket
	    	aSocket = new DatagramSocket();
	    	
		    //keep server alive
 			while(runClient){
 				aSocket = new DatagramSocket(); 
 				String msg = owner +  " heartbeat";
 				byte [] m = msg.getBytes();
 				
 				for(String replicaName : replicaInfo.getReplicaName())
 				{
 					//don't send to owner
 					if(!replicaName.equals(owner))
 					{
 	 					InetAddress aHost = InetAddress.getByName(replicaInfo.getReplicaIp(replicaName));
 	 	 				DatagramPacket request =
 	 	 	 				 	new DatagramPacket(m,  msg.length(), aHost, replicaInfo.getReplicaPort(replicaName));
 	 	 				aSocket.send(request);
 					}
 				}
    		}
		}
		catch(NumberFormatException e){
			System.out.println("Formatting number: " + e.getMessage());
		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
	}

}
