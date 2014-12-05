import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;


public class ReplicaManagerImpl extends Thread {
	
	private int numSFReplica1;
	private int numSFReplica2;
	private int numSFReplica3;
	private int numPCReplica1;
	private int numPCReplica2;
	private int numPCReplica3;
	private boolean supportSoftwareFailure;
	
	public ReplicaManagerImpl(boolean supportSoftwareFailure)
	{
	    numSFReplica1 = 0;
		numSFReplica2 = 0;
		numSFReplica3 = 0;
		numPCReplica1 = 0;
		numPCReplica2 = 0;
	    numPCReplica3 = 0;
	    this.supportSoftwareFailure = supportSoftwareFailure;
	}
	
	@Override
	public void run()
	{
		ReplicaInformation networkInfo = new ReplicaInformation();
		
		MulticastSocket aSocket = null;
		try {
			//Start servers
			aSocket = new MulticastSocket(networkInfo.getReplicaManagerPort());  
			
			String msg;
			//specify type of servers
			if(supportSoftwareFailure)
				msg = "startSoftareFailureTolerantServers";
			else
				msg = "startHighlyAvailableServers";
			
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName(networkInfo.getMulticastAddr());		                                                 
			DatagramPacket request =
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
