package replicaLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.HashSet;
import java.util.Set;


public class HeartbeatListener extends Thread {
	
	private boolean runListener;
	private String senderReplica;
	private int port;
	private Set<String> expectedSenders;
	private String owner;
	private ReplicaInformation replicaInfo;
	private boolean isFirstTime;
	
	
	
	public HeartbeatListener(String replicaName, int port)
	{
		runListener = false;
		this.port = port;
		expectedSenders = new HashSet<String>();
		owner = replicaName;
		replicaInfo = new ReplicaInformation();
		isFirstTime = true;
	}
	
	public void setSenderReplica(String senderReplica)
	{
		this.senderReplica = senderReplica;
	}
	
	public void stopListener()
	{
		runListener = false;
	}
	
	
	@Override
	public void run()
	{
    	DatagramSocket aSocket = null;
    	runListener = true;
    	//ava.util.Timer eventHandler = new java.util.Timer();
    	//eventHandler.schedule(new checkReceivedHeartbeat() , 18000, 18000);
		try{
			// create socket at agreed port
	    	aSocket = new DatagramSocket(port);
	    	aSocket.setSoTimeout(8000);
	    	
			byte[] buffer = new byte[1000];
		    //keep server alive
 			while(runListener){
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
 				
 				try{
 					aSocket.receive(request);
 					
 					System.out.println("Heartbeat received from " + senderReplica);
 				}
 				catch(SocketTimeoutException e)
 				{
	  				String result = owner +  " suspect " + senderReplica + " crashed";
	  				
	  				//send result to replica manager
					InetAddress replicaManagerIp = InetAddress.getByName(replicaInfo.getReplicaManagerIp());
				    	DatagramPacket reply = new DatagramPacket(result.getBytes(), result.length(), 
				    		replicaManagerIp, replicaInfo.getReplicaManagerPort());
	    			aSocket.send(reply);
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
	
	private class checkReceivedHeartbeat extends TimerTask
	{
		public void run()
		{
			if(isFirstTime)
			{
				isFirstTime = false;
			}
			else
			{
				for(String replicaName : replicaInfo.getReplicaName())
				{
					//don't validate the replica to which this listener belongs to
					if(!replicaName.equals(owner))
					{
						String ip = replicaInfo.getReplicaIp(replicaName);
						/*
						 * if we haven't receive anything from this replica,
						 * inform the replica manager
						 */
						if(!expectedSenders.contains(ip))
						{
					    	DatagramSocket aSocket = null;
							try{
								// create socket at agreed port
						    	aSocket = new DatagramSocket();
				  				String result = owner +  " suspect " + replicaName + " crashed";
				  				
				  				//send result to replica manager
								InetAddress replicaManagerIp = InetAddress.getByName(replicaInfo.getReplicaManagerIp());
	  	  				    	DatagramPacket reply = new DatagramPacket(result.getBytes(), result.length(), 
	  	  				    		replicaManagerIp, replicaInfo.getReplicaManagerPort());
				    			aSocket.send(reply);
							}
							catch(NumberFormatException e){
								System.out.println("Formatting number: " + e.getMessage());
							}
							catch (SocketException e){System.out.println("Socket: " + e.getMessage());
							}catch (IOException e) {System.out.println("IO: " + e.getMessage());
							}finally {if(aSocket != null) aSocket.close();}	
						}
					}
				}
				synchronized(expectedSenders)
				{
					expectedSenders.clear();
				}
			}
		}
	}

}
