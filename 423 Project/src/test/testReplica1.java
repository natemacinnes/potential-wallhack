package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import corba.FrontEnd;

public class testReplica1 {
	public static int TestUDPPortReplicaListener = 2021;

	public static void main(String[] args) {
		DatagramSocket aSocket = null;
		byte[] buffer = new byte[1000];

		try {
			// Create a datagram socket
			aSocket = new DatagramSocket();

			// Format arguments according to coding convention

			byte[] reqMsg = "REPLICARESULT".getBytes();// will receive the data
			InetAddress aHost = InetAddress.getByName("localhost");

			DatagramPacket request = new DatagramPacket(reqMsg, reqMsg.length, aHost, TestUDPPortReplicaListener);
			aSocket.send(request);

			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

			// Get the reply
			aSocket.receive(reply);

			// add formatting to string
			String rep = new String(reply.getData()).replaceAll("\u0000.*", "");
			System.out.println(rep);

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}

	}

}
