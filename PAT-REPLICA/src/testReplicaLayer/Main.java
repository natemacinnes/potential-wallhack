package testReplicaLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
		ReplicaServer replica = new ReplicaServer(replicaName, false);
		ReplicaInformation networkInfo = new ReplicaInformation();
		replica.start();
		
		DatagramSocket aSocket = null;
		try {
			//Start servers
			aSocket = new DatagramSocket(networkInfo.getReplicaManagerPort());  
			String msg = "startServers";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("172.30.90.189");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, replica.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			//Send createAccount operation
			msg = "0.createAccount.joedoe.joedoe.joedoe@mail.222-2222.joedoe.joedoe." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, replica.getReplicaPort());
			aSocket.send(request);
			System.out.println("request to create account was send");

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
			}	

	}

}
