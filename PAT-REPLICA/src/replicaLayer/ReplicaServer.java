package replicaLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

import serverInterfaceLayer.LibraryServerInterface;
import serverLayer.LibraryServerImpl;

public class ReplicaServer extends Thread{
	
	private String replicaName;
	private int replicaPort;
	private ReplicaInformation replicaInfo;
	private HashMap<String,LibraryServerInterface> serversMap;
	private boolean runReplica = false;
	
	public ReplicaServer(String replicaName)
	{
		this.replicaName = replicaName;
		replicaInfo = new ReplicaInformation();
		replicaPort = replicaInfo.getReplicaPort(replicaName);
		serversMap = new HashMap<String,LibraryServerInterface>();
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
  				
  				//get only the non-empty bit out of the byte array
  				byte [] byteReceive = new byte[request.getLength()];
  				for(int i = 0; i < request.getLength(); i++){
  					byteReceive[i] = request.getData()[i];
  				}
  				
  				//convert the byte into a string
  				String operationReceived = new String(byteReceive);
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
		if(operation.equals("startServers"))
		{
			return startServers();
		}
		else if(operation.equals("restartServers"))
		{
			return restartServers();
		}
		else
		{
			return "This operation was not recognized by the system";
		}
	}
	
	public String startServers()
	{
		serversMap.put("concordia", new LibraryServerImpl("concordia"));
		serversMap.put("mcgill", new LibraryServerImpl("mcgill"));
		serversMap.put("uqam", new LibraryServerImpl("uqam"));
		
		return "Replica " + replicaName + " started its servers";
	}
	
	public String restartServers()
	{
		startServers();
		//TODO: write implementation of updateServers method for bringing servers up-to-date
		updateServers();
		return "Replica " + replicaName + " restarted its servers";
	}
	
	private void updateServers()
	{
		
	} 

}
