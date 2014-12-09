import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class ReplicaManagerImpl extends Thread {
	
	private int numSFReplica1;
	private int numSFReplica2;
	private int numSFReplica3;
	private boolean supportSoftwareFailure;
	private boolean isRecovering;
	private int numProcessStarted;
	private HashMap<String,Set<String>> crashNotification;
	
	public ReplicaManagerImpl(boolean supportSoftwareFailure)
	{
	    numSFReplica1 = 0;
		numSFReplica2 = 0;
		numSFReplica3 = 0;
		numProcessStarted = 0;
	    this.supportSoftwareFailure = supportSoftwareFailure;
	    isRecovering = false;
	    crashNotification = new HashMap<String,Set<String>>();
	    crashNotification.put("replica1", new HashSet<String>());
	    crashNotification.put("replica2", new HashSet<String>());
	    crashNotification.put("replica3", new HashSet<String>());
	}
	
	@Override
	public void run()
	{
		ReplicaInformation networkInfo = new ReplicaInformation();
		
		DatagramSocket aSocket = null;
		try {
			//Start servers
			aSocket = new DatagramSocket(networkInfo.getReplicaManagerPort());  
			
			String msg;
			//specify type of servers
			if(supportSoftwareFailure)
				msg = "startSoftareFailureTolerantServers";
			else
				msg = "startHighlyAvailableServers";
			
			byte [] m = msg.getBytes();
			
			//start replica1
			InetAddress aHost = InetAddress.getByName(networkInfo.getReplicaIp("replica1"));		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, networkInfo.getMulticastPort());
			aSocket.send(request);
			
			//start replica2
			aHost = InetAddress.getByName(networkInfo.getReplicaIp("replica2"));		                                                 
			request =
			 	new DatagramPacket(m,  msg.length(), aHost, networkInfo.getMulticastPort());
			aSocket.send(request);
			
			//start replica3
			aHost = InetAddress.getByName(networkInfo.getReplicaIp("replica3"));		                                                 
			request =
			 	new DatagramPacket(m,  msg.length(), aHost, networkInfo.getMulticastPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			byte[] buffer = new byte[1000];
			
			while(true)
			{
 				request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request);
  				
  				String message = extractMessage(request);
  				System.out.println(message);
  				
  				if(message.equals("Replica replica1 started its servers"))
  				{
  					numProcessStarted++;
  				}
 				if(message.equals("Replica replica2 started its servers"))
  				{
  					numProcessStarted++;
  				}
 				if(message.equals("Replica replica3 started its servers"))
  				{
  					numProcessStarted++;
  				}
 				
  				
  				if(supportSoftwareFailure)
  				{
  					System.out.println(message);
  					if(message.equals("replica1 send wrong result") && !isRecovering)
  					{
  						numSFReplica1++;
  						
  						if(numSFReplica1 == 3)
  						{
  							numSFReplica1 = 0;
  							msg = "restartSoftareFailureTolerantServers";
  							m = msg.getBytes();
  							aHost = InetAddress.getByName(networkInfo.getReplicaIp("replica1"));		                                                 
  							request =
  							 	new DatagramPacket(m,  msg.length(), aHost, networkInfo.getMulticastPort());
  							aSocket.send(request);
  							isRecovering = true;
  							System.out.println("RM sent request to replica1 to restart");
  						}
  					}
  					else if(message.equals("replica2 send wrong result") && !isRecovering)
  					{
  						numSFReplica2++;
  						
  						if(numSFReplica2 == 3)
  						{
  							numSFReplica2 = 0;
  							msg = "restartSoftareFailureTolerantServers";
  							m = msg.getBytes();
  							aHost = InetAddress.getByName(networkInfo.getReplicaIp("replica2"));		                                                 
  							request =
  							 	new DatagramPacket(m,  msg.length(), aHost, networkInfo.getMulticastPort());
  							aSocket.send(request);
  							isRecovering = true;
  							System.out.println("RM sent request to replica2 to restart");
  						}
  					}
  					else if(message.equals("replica3 send wrong result") && !isRecovering)
  					{
  						numSFReplica3++;
  						
  						if(numSFReplica3 == 3)
  						{
  							numSFReplica3 = 0;
  							msg = "restartSoftareFailureTolerantServers";
  							m = msg.getBytes();
  							aHost = InetAddress.getByName(networkInfo.getReplicaIp("replica3"));		                                                 
  							request =
  							 	new DatagramPacket(m,  msg.length(), aHost, networkInfo.getMulticastPort());
  							aSocket.send(request);
  							isRecovering = true;
  							System.out.println("RM sent request to replica3 to restart");
  						}
  					}
  				}
  				//support high availability
  				else if(!supportSoftwareFailure)
  				{
  					if(message.contains("suspect replica1 crashed") && !isRecovering && numProcessStarted==3)
  					{
  						System.out.println(message);
  						crashNotification.get("replica1").add(request.getAddress().toString());
  						
  						if(crashNotification.get("replica1").size() == 2)
  						{
  							crashNotification.get("replica1").clear();
  							crashNotification.get("replica2").clear();
  							crashNotification.get("replica3").clear();
  							msg = "restartHighlyAvailableServers";
  							m = msg.getBytes();
  							aHost = InetAddress.getByName(networkInfo.getReplicaIp("replica1"));		                                                 
  							request =
  							 	new DatagramPacket(m,  msg.length(), aHost, networkInfo.getMulticastPort());
  							aSocket.send(request);
  							isRecovering = true;
  							System.out.println("RM sent request to replica1 to restart");
  						}
  					}
  					/*else if(message.contains("suspect replica2 crashed") && !isRecovering && numProcessStarted==3)
  					{
  						crashNotification.get("replica2").add(request.getAddress().toString());
  						
  						if(crashNotification.get("replica2").size() == 2)
  						{
  							crashNotification.get("replica1").clear();
  							crashNotification.get("replica2").clear();
  							crashNotification.get("replica3").clear();
  							msg = "restartHighlyAvailableServers";
  							m = msg.getBytes();
  							aHost = InetAddress.getByName(networkInfo.getReplicaIp("replica2"));		                                                 
  							request =
  							 	new DatagramPacket(m,  msg.length(), aHost, networkInfo.getMulticastPort());
  							aSocket.send(request);
  							isRecovering = true;
  							System.out.println("RM sent request to replica2 to restart");
  						}
  					}
  					else if(message.contains("suspect replica3 crashed") && !isRecovering && numProcessStarted==3)
  					{
  						crashNotification.get("replica3").add(request.getAddress().toString());
  						
  						if(crashNotification.get("replica3").size() == 2)
  						{
  							crashNotification.get("replica1").clear();
  							crashNotification.get("replica2").clear();
  							crashNotification.get("replica3").clear();
  							msg = "restartHighlyAvailableServers";
  							m = msg.getBytes();
  							aHost = InetAddress.getByName(networkInfo.getReplicaIp("replica3"));		                                                 
  							request =
  							 	new DatagramPacket(m,  msg.length(), aHost, networkInfo.getMulticastPort());
  							aSocket.send(request);
  							isRecovering = true;
  							System.out.println("RM sent request to replica3 to restart");
  						}
  					}*/
  				}
  				
  				
  				if(message.contains("restarted its servers"))
  				{
  					isRecovering = false;
  				}
			}
			
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}
		catch(InterruptedException e)
		{
			System.out.println("IO: " + e.getMessage());
		}
		catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			}
	}
	
	private String extractMessage(DatagramPacket request)
	{
			//get only the non-empty bit out of the byte array
			byte [] byteReceive = new byte[request.getLength()];
			for(int i = 0; i < request.getLength(); i++){
				byteReceive[i] = request.getData()[i];
			}
			
			//convert the byte into a string
			return new String(byteReceive);
	}

}
