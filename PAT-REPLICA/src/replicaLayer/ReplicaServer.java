package replicaLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;

import serverInterfaceLayer.LibraryServerInterface;
import serverLayer.LibraryServerImpl;

public class ReplicaServer extends Thread{
	
	private String replicaName;
	private int replicaPort;
	private ReplicaInformation replicaInfo;
	private HashMap<String,LibraryServerInterface> serversMap;
	private HashMap<Integer,String> deliveryQueue;
	private HashMap<Integer,String> holdbackQueue;
	private boolean runReplica = false;
	private int messageSequenceNumber;
	
	public ReplicaServer(String replicaName)
	{
		this.replicaName = replicaName;
		replicaInfo = new ReplicaInformation();
		replicaPort = replicaInfo.getReplicaPort(replicaName);
		serversMap = new HashMap<String,LibraryServerInterface>();
		deliveryQueue = new HashMap<Integer, String>();
		holdbackQueue = new HashMap<Integer, String>();
		messageSequenceNumber = 0;
	}
	
	public String getReplicaName()
	{
		return replicaName;
	}
	
	public int getReplicaPort()
	{
		return replicaPort;
	}
	
	public void stopReplica()
	{
		runReplica = false;
	}
	
	@Override
	public void run()
	{
		Collection<String> replicaNames = replicaInfo.getReplicaName();
		runReplica = true;
    	DatagramSocket aSocket = null;
    	
		try{
			// create socket at agreed port
	    	aSocket = new DatagramSocket(replicaPort);
	    	
			byte[] buffer = new byte[1000];
		    //keep server alive
 			while(runReplica){
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request);
  				
  				String operationReceived = extractMessage(request);
  				
  				//If the message is an operation to be performed on the library
  				if(isLibraryOperation(operationReceived))
  				{
  					String[] msgParts = operationReceived.split("\\.");
  					int msgNumber = Integer.parseInt(msgParts[0]);
  					
  					//If the replica received that message for the first time
  					if(!holdbackQueue.containsKey(msgNumber) && !deliveryQueue.containsKey(msgNumber))
  					{
  	  					/*
  	  					 * Send message to all replica if actual sequence number is greater or equal
  	  					 * than expected sequence number. If it is smaller, it means it is a previous
  	  					 * message and we don't care
  	  					 */
  	  					if(msgNumber >= messageSequenceNumber)
  	  					{
  	  						//Send the message received to all replica
  	  						for(String name: replicaNames)
  	  						{
  	  							
  								InetAddress replicaIp = InetAddress.getByName(replicaInfo.getReplicaIp(replicaName));
  	  				    		DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), 
  	  				    				replicaIp, replicaInfo.getReplicaPort(name));
  	  				    		aSocket.send(reply);
  	  						}
  	  						
  	  						/*
  	  						 * If actual sequence number is greater than expected sequence number,
  	  						 * It is a future message. Put it in holdbackQueue and ask sequencer to resend
  	  						 * expected sequence number.
  	  						 */
  	  						if(msgNumber > messageSequenceNumber)
  	  						{
  	  							holdbackQueue.put(msgNumber, operationReceived);
  	  							
  	  							String msg = "resend." + messageSequenceNumber;
  								InetAddress sequencerIp = InetAddress.getByName(replicaInfo.getSequencerIp());
  	  				    		DatagramPacket reply = new DatagramPacket(msg.getBytes(), msg.length(), 
  	  				    				sequencerIp, replicaInfo.getSequencerPort());
  	  				    		aSocket.send(reply);
  	  						}
  	  					}
  						
  					}
  				}
  				String result = performOperation(operationReceived);
  				//convert the text obtained into an integer
  				//int numDays = Integer.parseInt(textReceive);
  				//get the non returners
  				//String result = server.ISOGetNonReturners(numDays);
  				//System.out.println(result);
  				//send result back to calling server
    			DatagramPacket reply = new DatagramPacket(result.getBytes(), result.length(), 
        				request.getAddress(), request.getPort());
    			aSocket.send(reply);
    		}
		}
		catch(NumberFormatException e){
			System.out.println("Formatting number: " + e.getMessage());
		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
	}
	
	private String performOperation(String operation)
	{
		boolean isStartOperation = operation.equals("startServers");
		boolean isRestartOperation = operation.equals("restartServers");
		boolean isLibraryOperation = operation.contains("createAccount") || operation.contains("reserveInterLibrary")
		|| operation.contains("setDuration") || operation.contains("getNonReturners") || 
		operation.contains("reserveBook");
		
		if(isStartOperation)
		{
			return startServers();
		}
		else if(isRestartOperation)
		{
			return restartServers();
		}
		else
		{
			return "This operation was not recognized by the system";
		}
	}
	
	private String startServers()
	{
		serversMap.put("concordia", new LibraryServerImpl("concordia"));
		serversMap.put("mcgill", new LibraryServerImpl("mcgill"));
		serversMap.put("uqam", new LibraryServerImpl("uqam"));
		
		return "Replica " + replicaName + " started its servers";
	}
	
	private String restartServers()
	{
		startServers();
		//TODO: write implementation of updateServers method for bringing servers up-to-date
		updateServers();
		return "Replica " + replicaName + " restarted its servers";
	}
	
	private boolean isLibraryOperation(String operation)
	{
		boolean isLibraryOperation = operation.contains("createAccount") || operation.contains("reserveInterLibrary")
				|| operation.contains("setDuration") || operation.contains("getNonReturners") || 
				operation.contains("reserveBook");
		
		return isLibraryOperation;
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
	
	public int getHoldbackQueueSize()
	{
		return holdbackQueue.size();
	}
	
	public int getDeliveryQueueSize()
	{
		return deliveryQueue.size();
	}
	
	
	
	private void updateServers()
	{
		
	} 

}
