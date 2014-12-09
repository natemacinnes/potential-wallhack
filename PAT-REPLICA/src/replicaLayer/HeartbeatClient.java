package replicaLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class HeartbeatClient extends Thread {

	private boolean runClient;
	private String owner;
	private ReplicaInformation replicaInfo;
	private HashMap<String,Integer> networkInfoList;
	
	
	
	public HeartbeatClient(String replicaName)
	{
		runClient = false;
		owner = replicaName;
		replicaInfo = new ReplicaInformation();
		networkInfoList = new HashMap<String,Integer>();
		
		if(owner.equals("replica1"))
		{
			networkInfoList.put("replica2", 7783);
			networkInfoList.put("replica3", 7783);
		}
		if(owner.equals("replica2"))
		{
			networkInfoList.put("replica1", 7783);
			networkInfoList.put("replica3", 7784);
		}
		if(owner.equals("replica3"))
		{
			networkInfoList.put("replica1", 7784);
			networkInfoList.put("replica2", 7784);
		}
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
	    	int deb = 0;
		    //keep server alive
 			while(runClient){ 
 				sleep(5000);
 				String msg = owner +  " heartbeat";
 				byte [] m = msg.getBytes();
 				
 				for(String replicaName : networkInfoList.keySet())
 				{
 					//don't send to owner
 					if(!replicaName.equals(owner))
 					{
 	 					InetAddress aHost = InetAddress.getByName(replicaInfo.getReplicaIp(replicaName));
 	 	 				DatagramPacket request =
 	 	 	 				 	new DatagramPacket(m,  msg.length(), aHost, networkInfoList.get(replicaName));
 	 	 				aSocket.send(request);
 	 	 				System.out.println("send heartbeat to " + replicaName);
 					}
 				}
    		}
		}
		catch(NumberFormatException e){
			System.out.println("Formatting number: " + e.getMessage());
		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("IO: " + e.getMessage());}
		 catch (InterruptedException e) {System.out.println("IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
	}

}
