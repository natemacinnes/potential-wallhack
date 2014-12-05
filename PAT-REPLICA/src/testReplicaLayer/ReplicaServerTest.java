package testReplicaLayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

	/*
	 * Try to create a replica with an invalid name
	 */
	@Test(expected = NullPointerException.class)
	public void ReplicaServerTest_001() {
		String replicaName = "test";
		ReplicaServer r1 = new ReplicaServer(replicaName);
	}
	
	/*
	 * Try creating a replica successfully
	 */
	@Test
	public void ReplicaServerTest_002() {
		String replicaName = "replica3";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		ReplicaInformation ri = new ReplicaInformation();
		Assert.assertTrue(ri.isReplicaNameValid(r1.getReplicaName()));
	}
	
	/*
	 * Send the operation startServers to a replica.
	 * Replica should start its servers successfully.
	 */
	@Test
	public void ReplicaServerTest_003() {
		String replicaName = "replica2";
		String expectedResult = "Replica " + replicaName + " started its servers";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		ReplicaInformation networkInfo = new ReplicaInformation();
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(networkInfo.getReplicaManagerPort()); 
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
	
	/*
	 * Send the operation restartServers to a replica.
	 * Replica should restart its servers successfully.
	 */
	@Test
	public void ReplicaServerTest_004() {
		String replicaName = "replica2";
		String expectedResult = "Replica " + replicaName + " restarted its servers";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		ReplicaInformation networkInfo = new ReplicaInformation();
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(networkInfo.getReplicaManagerPort()); 
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
	
	/*
	 * Send an invalid operation to a replica
	 */
	@Test
	public void ReplicaServerTest_005() {
		String replicaName = "replica2";
		String expectedResult = "This operation was not recognized by the system";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		ReplicaInformation networkInfo = new ReplicaInformation();
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(networkInfo.getFrontEndPort()); 
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
	
	/*
	 * Test reliable multicast among replica by send a message
	 * with a sequence number greater than the one expected.
	 */
	@Test
	public void ReplicaServerTest_006() {
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
	
	
	/*
	 * Send a createAccount message to a replica successfully.
	 */
	@Test
	public void ReplicaServerTest_007() {
		String replicaName = "replica3";
		String institution = "concordia";
		String expectedResult = "Operation createAccount succeed in " + institution + " library";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		ReplicaInformation networkInfo = new ReplicaInformation();
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			//Start servers
			aSocket = new DatagramSocket(networkInfo.getFrontEndPort());  
			String msg = "startServers";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			//Send createAccount operation
			msg = "0.createAccount.joedoe.joedoe.joedoe@mail.222-2222.joedoe.joedoe." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			//Receive reply
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
			r1.stopReplica();
			}	
	}
	
	/*
	 * Send a createAccount and reserveBook messages to a replica successfully.
	 */
	@Test
	public void ReplicaServerTest_008() {
		String replicaName = "replica1";
		String institution = "mcgill";
		String expectedResult1 = "Operation createAccount succeed in " + institution + " library";
		String expectedResult2 = "Operation reserveBook succeed in " + institution + " library";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		ReplicaInformation networkInfo = new ReplicaInformation();
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			//Start servers
			aSocket = new DatagramSocket(networkInfo.getFrontEndPort());  
			String msg = "startServers";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Send createAccount operation
			msg = "0.createAccount.joedoe.joedoe.joedoe@mail.222-2222.joedoe.joedoe." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Receive reply
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byte [] byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult1 = new String(byteReceive,"UTF-8");
			
			//Send reserveBook operation
			msg = "1.reserveBook.joedoe.joedoe.Distributed System.Kumar." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Receive reply
			buffer = new byte[1000];
			reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult2 = new String(byteReceive,"UTF-8");
			aSocket.close();
			r1.stopReplica();
			
			Assert.assertEquals(expectedResult1, actualResult1);
			Assert.assertEquals(expectedResult2, actualResult2);
			
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}
		catch(InterruptedException e)
		{
			System.out.println("Threading: " + e.getMessage());
		}
		catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			r1.stopReplica();
			}	
	}
	
	
	/*
	 * Send a createAccount, 2 reserveBook and 1 reserveInterLibrary messages to 
	 * a replica successfully.
	 */
	@Test
	public void ReplicaServerTest_009() {
		String replicaName = "replica1";
		String institution = "uqam";
		String expectedResult1 = "Operation createAccount succeed in " + institution + " library";
		String expectedResult2 = "Operation reserveBook succeed in " + institution + " library";
		String expectedResult3 = "Operation reserveBook failed: No more copies available";
		String expectedResult4 = "Operation reserveInterLibrary succeed in concordia library";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		ReplicaInformation networkInfo = new ReplicaInformation();
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			//Start servers
			aSocket = new DatagramSocket(networkInfo.getFrontEndPort());  
			String msg = "startServers";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Send createAccount operation
			msg = "0.createAccount.joedoe.joedoe.joedoe@mail.222-2222.joedoe.joedoe." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Receive reply
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byte [] byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult1 = new String(byteReceive,"UTF-8");
			
			//Send reserveBook operation
			msg = "1.reserveBook.joedoe.joedoe.Distributed System.Kumar." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Receive reply
			buffer = new byte[1000];
			reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult2 = new String(byteReceive,"UTF-8");
			
			
			//Send reserveBook operation again
			msg = "2.reserveBook.joedoe.joedoe.Distributed System.Kumar." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Receive reply
			buffer = new byte[1000];
			reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult3 = new String(byteReceive,"UTF-8");
			
			//Send reserveInterLibrary operation
			msg = "3.reserveInterLibrary.joedoe.joedoe.Distributed System.Kumar." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Receive reply
			buffer = new byte[1000];
			reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult4 = new String(byteReceive,"UTF-8");
			aSocket.close();
			r1.stopReplica();
			
			Assert.assertEquals(expectedResult1, actualResult1);
			Assert.assertEquals(expectedResult2, actualResult2);
			Assert.assertEquals(expectedResult3, actualResult3);
			Assert.assertEquals(expectedResult4, actualResult4);
			
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}
		catch(InterruptedException e)
		{
			System.out.println("Threading: " + e.getMessage());
		}
		catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			r1.stopReplica();
			}	
	}
	
	
	/*
	 * Send a createAccount,reserveBook and setDuration messages to a replica successfully.
	 */
	@Test
	public void ReplicaServerTest_010() {
		String replicaName = "replica1";
		String institution = "mcgill";
		String expectedResult1 = "Operation createAccount succeed in " + institution + " library";
		String expectedResult2 = "Operation reserveBook succeed in " + institution + " library";
		String expectedResult3 = "Operation setDuration succeed in " + institution + " library";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		ReplicaInformation networkInfo = new ReplicaInformation();
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			//Start servers
			aSocket = new DatagramSocket(networkInfo.getFrontEndPort());  
			String msg = "startServers";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Send createAccount operation
			msg = "0.createAccount.joedoe.joedoe.joedoe@mail.222-2222.joedoe.joedoe." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Receive reply
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byte [] byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult1 = new String(byteReceive,"UTF-8");
			
			//Send reserveBook operation
			msg = "1.reserveBook.joedoe.joedoe.Distributed System.Kumar." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Receive reply
			buffer = new byte[1000];
			reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult2 = new String(byteReceive,"UTF-8");
			
			
			//Send setDuration operation
			msg = "2.setDuration.joedoe.Distributed System.4." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Receive reply
			buffer = new byte[1000];
			reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult3 = new String(byteReceive,"UTF-8");
			aSocket.close();
			r1.stopReplica();
			
			Assert.assertEquals(expectedResult1, actualResult1);
			Assert.assertEquals(expectedResult2, actualResult2);
			Assert.assertEquals(expectedResult3, actualResult3);
			
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}
		catch(InterruptedException e)
		{
			System.out.println("Threading: " + e.getMessage());
		}
		catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			r1.stopReplica();
			}	
	}
	
	
	/*
	 * Send a createAccount,reserveBook, setDuration and getNonReturners messages to a replica successfully.
	 */
	@Test
	public void ReplicaServerTest_011() {
		String replicaName = "replica1";
		String institution = "mcgill";
		String expectedResult1 = "Operation createAccount succeed in " + institution + " library";
		String expectedResult2 = "Operation reserveBook succeed in " + institution + " library";
		String expectedResult3 = "Operation setDuration succeed in " + institution + " library";
		String expectedResult4 = "concordia university:\n\nmcgill university:\njoedoe joedoe 222-2222\n\n";
		expectedResult4 += "uqam university:\n\n";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		ReplicaInformation networkInfo = new ReplicaInformation();
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			//Start servers
			aSocket = new DatagramSocket(networkInfo.getFrontEndPort());  
			String msg = "startServers";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Send createAccount operation
			msg = "0.createAccount.joedoe.joedoe.joedoe@mail.222-2222.joedoe.joedoe." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Receive reply
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byte [] byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult1 = new String(byteReceive,"UTF-8");
			
			//Send reserveBook operation
			msg = "1.reserveBook.joedoe.joedoe.Distributed System.Kumar." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Receive reply
			buffer = new byte[1000];
			reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult2 = new String(byteReceive,"UTF-8");
			
			
			//Send setDuration operation
			msg = "2.setDuration.joedoe.Distributed System.4." + institution;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Receive reply
			buffer = new byte[1000];
			reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult3 = new String(byteReceive,"UTF-8");
			
			
			//Send getNonReturners operation
			msg = "3.getNonReturners.Admin.Admin.uqam.4";
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Receive reply
			buffer = new byte[1000];
			reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult4 = new String(byteReceive,"UTF-8");
			aSocket.close();
			r1.stopReplica();
			
			Assert.assertEquals(expectedResult1, actualResult1);
			Assert.assertEquals(expectedResult2, actualResult2);
			Assert.assertEquals(expectedResult3, actualResult3);
			Assert.assertEquals(expectedResult4, actualResult4);
			
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}
		catch(InterruptedException e)
		{
			System.out.println("Threading: " + e.getMessage());
		}
		catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			r1.stopReplica();
			}	
	}
	
	
	/*
	 * Send a createAccount and reserveBook messages to a replica successfully.
	 * Verify that the operation were written correctly in the log file
	 */
	@Test
	public void ReplicaServerTest_012() {
		String replicaName = "replica1";
		String institution = "concordia";
		String expectedResult1 = "0.createAccount.joedoe.joedoe.joedoe@mail.222-2222.joedoe.joedoe." + institution;
		String expectedResult2 = "1.reserveBook.joedoe.joedoe.Distributed System.Kumar." + institution;
		ReplicaServer r1 = new ReplicaServer(replicaName);
		String fileName = r1.getLogFileName();
		ReplicaInformation networkInfo = new ReplicaInformation();
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			//Delete existing log file
			File file  = new File(fileName);
			file.delete();
			
			//Start servers
			aSocket = new DatagramSocket(networkInfo.getFrontEndPort());  
			String msg = "startServers";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Send createAccount operation
			msg = expectedResult1;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			//Send reserveBook operation
			msg = expectedResult2;
			m = msg.getBytes();		                                                 
			request = new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			aSocket.close();
			r1.stopReplica();
			
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String actualResult1 = reader.readLine();
			String actualResult2 = reader.readLine();
			reader.close();
			
			Assert.assertEquals(expectedResult1, actualResult1);
			Assert.assertEquals(expectedResult2, actualResult2);
			
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}
		catch(InterruptedException e)
		{
			System.out.println("Threading: " + e.getMessage());
		}
		catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			r1.stopReplica();
			}	
	}
	
	
	/*
	 * Test if library servers get updated successfully after a restart
	 */
	@Test
	public void ReplicaServerTest_013() {
		String replicaName = "replica1";
		String institution = "concordia";
		int expectedDeliveryQueueSize = 3;
		int expectedMessageSequenceNumber = 3;
		ReplicaServer r1 = new ReplicaServer(replicaName);
		String fileName = r1.getLogFileName();
		ReplicaInformation networkInfo = new ReplicaInformation();
		PrintWriter printWriter = null;
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			//Delete existing log file
			File file  = new File(fileName);
			file.delete();
			
			//Create log file
			FileWriter fileWriter = new FileWriter(file,true);
			printWriter = new PrintWriter(new BufferedWriter(fileWriter));
			printWriter.println("0.createAccount.joedoe.joedoe.joedoe@mail.222-2222.joedoe.joedoe." + institution);
			printWriter.println("1.reserveBook.joedoe.joedoe.Distributed System.Kumar." + institution);
			printWriter.println("2.createAccount.doejoe.doejoe.joedoe@mail.222-2222.joedoe.joedoe." + institution);
			printWriter.close();
			
			
			//restart servers
			aSocket = new DatagramSocket(networkInfo.getFrontEndPort());  
			String msg = "restartServers";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, r1.getReplicaPort());
			aSocket.send(request);
			
			Thread.sleep(3000);
			
			aSocket.close();
			r1.stopReplica();
			
			Assert.assertEquals(expectedDeliveryQueueSize, r1.getDeliveryQueueSize());
			Assert.assertEquals(expectedMessageSequenceNumber, r1.getMessageSequenceNumber());
			
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}
		catch(InterruptedException e)
		{
			System.out.println("Threading: " + e.getMessage());
		}
		catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			r1.stopReplica();
			if(printWriter != null)
			{
				printWriter.close();
			}
			}	
	}
	
	
	/*
	 * Start a replica and verify the heartbeatListener
	 * works correctly
	 */
	@Test
	public void ReplicaServerTest_014() {
		String replicaName = "replica1";
		String expectedResult1 = "suspect replica replica2 crashed";
		String expectedResult2 = "suspect replica replica3 crashed";
		ReplicaServer r1 = new ReplicaServer(replicaName);
		ReplicaInformation networkInfo = new ReplicaInformation();
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(networkInfo.getReplicaManagerPort()); 
			
			Thread.sleep(5000);
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byte [] byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult1 = new String(byteReceive,"UTF-8");
			
			reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String actualResult2 = new String(byteReceive,"UTF-8");
			aSocket.close();
			r1.stopReplica();
			
			Assert.assertEquals(expectedResult1, actualResult1);
			Assert.assertEquals(expectedResult2, actualResult2);
			
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}
		catch(InterruptedException e)
		{
			System.out.println("Threading: " + e.getMessage());
		}
		catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			r1.stopReplica();
			}	
	}
	
	
	/*
	 * Start a replica and verify the heartbeatClient
	 * works correctly
	 */
	@Test
	public void ReplicaServerTest_015() {
		String senderReplicaName = "replica1";
		String receiverReplicaName = "replica2";
		String expectedResult = senderReplicaName +  " heartbeat";
		ReplicaServer r1 = new ReplicaServer(senderReplicaName);
		ReplicaInformation networkInfo = new ReplicaInformation();
		r1.start();
		
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(networkInfo.getReplicaPort(receiverReplicaName)); 
			
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
		}
		catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			r1.stopReplica();
			}	
	}

}
