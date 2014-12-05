package testReplicaLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import replicaLayer.ReplicaInformation;
import replicaLayer.ReplicaServer;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String replicaName = "replica1";
		String institution = "concordia";
		ReplicaServer replica = new ReplicaServer(replicaName);
		ReplicaInformation networkInfo = new ReplicaInformation();
		replica.start();
		
		/*MulticastSocket aSocket = null;
		try {
			//Start servers
			aSocket = new MulticastSocket(networkInfo.getReplicaPort(replicaName));  
			String msg = "startServers";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName(networkInfo.getMulticastAddr());		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, networkInfo.getMulticastPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			byte [] byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult = new String(byteReceive,"UTF-8");
			System.out.println("received " + actualResult);
			
			msg = "1.reserveBook.joedoe.joedoe.Somebook.allo.concordia";
			m = msg.getBytes();		                                                 
			request =
			 	new DatagramPacket(m,  msg.length(), aHost, networkInfo.getMulticastPort());
			aSocket.send(request);

			aSocket.close();
			
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
			}	*/

	}

}
