package testReplicaLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import junit.framework.Assert;
import replicaLayer.ReplicaServer;
import replicaLayer.ReplicaInformation;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReplicaServerTest {

	@Test(expected = NullPointerException.class)
	public void createReplicaServerWithWrongName() {
		String replicaName = "test";
		ReplicaServer r1 = new ReplicaServer(replicaName);
	}
	
	@Test
	public void createReplicaServerSuccessfully() {
		String replicaName = "replica3";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		ReplicaInformation ri = new ReplicaInformation();
		Assert.assertTrue(ri.isReplicaNameValid(r1.getReplicaName()));
	}
	
	@Test
	public void sendStartServersMsg() {
		String replicaName = "replica2";
		String expectedResult = "Replica " + replicaName + " started its servers";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(); 
			String msg = "startServers";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byte [] byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult = new String(byteReceive,"UTF-8");
			aSocket.close();
			r1.stopReplica();
			
			Assert.assertEquals(expectedResult, actualResult);
			
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			r1.stopReplica();
			}	
	}
	
	@Test
	public void sendRestartServersMsg() {
		String replicaName = "replica2";
		String expectedResult = "Replica " + replicaName + " restarted its servers";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(); 
			String msg = "restartServers";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byte [] byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult = new String(byteReceive,"UTF-8");
			aSocket.close();
			r1.stopReplica();
			
			Assert.assertEquals(expectedResult, actualResult);
			
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			r1.stopReplica();
			}	
	}
	
	@Test
	public void sendInvalidMsg() {
		String replicaName = "replica2";
		String expectedResult = "This operation was not recognized by the system";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(); 
			String msg = "shutdownServers";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byte [] byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult = new String(byteReceive,"UTF-8");
			aSocket.close();
			r1.stopReplica();
			
			Assert.assertEquals(expectedResult, actualResult);
			
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			r1.stopReplica();
			}	
	}
	
	@Test
	public void reliableMulticastWithGreaterSeqNumber() {
		int expectedResult = 1;
		ReplicaServer r1 = new ReplicaServer("replica1");
		ReplicaServer r2 = new ReplicaServer("replica2");
		ReplicaServer r3 = new ReplicaServer("replica3");
		r1.start();
		r2.start();
		r3.start();
		
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(); 
			String msg = "1.setDuration.joedoe.somebook.4";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			int replicaTwoActualResult = r2.getHoldbackQueueSize();
			int replicaThreeActualResult = r3.getHoldbackQueueSize();

			r1.stopReplica();
			r2.stopReplica();
			r3.stopReplica();
			
			Assert.assertEquals(expectedResult, replicaTwoActualResult);
			Assert.assertEquals(expectedResult, replicaThreeActualResult);
			
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}
		catch(InterruptedException e){
			System.out.println("Threading: " + e.getMessage());
		}
		catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			r1.stopReplica();
			r2.stopReplica();
			r3.stopReplica();
			}	
	}

}
