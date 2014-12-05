package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import corba.FrontEnd;

public class ReplicaResultListener extends Thread {

	private FrontEnd fe;
	public String resultReplica1;
	public static int TestUDPPortReplicaListener = 2021;

	public ReplicaResultListener(FrontEnd fe) {
		this.fe = fe;
	}

	public void run() {
		DatagramSocket aSocket = null;

		try {
			aSocket = new DatagramSocket(TestUDPPortReplicaListener);
			byte[] buffer = new byte[1000]; // create socket at agreed port

			// FIXME change the flag for the loop
			boolean messagesReceived = false;
			System.out.println("REPLICA LISTENER RUNNING");

			while (!messagesReceived) {
				
				//TEST
				
				
				
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				// get request
				aSocket.receive(request);

				System.out.println("REPLICA REQUEST ADDRESS " + request.getAddress());
				String requestString = new String(request.getData()).replaceAll("\u0000.*", "");

				// Print the request out
				System.out.println("REPLICA TEST REQUEST ARGS: " + requestString);

				// FIXME Decide on the result, based on the mode of the failure.

				if (requestString != null) {
					System.out.println("TEST paterson result= " + requestString);
					decide(requestString);
					messagesReceived = true;

				}

			}

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}

	public void decide(String requestString) {
		// FIXME some logic to decide

		fe.sendResult(requestString);
	}

}
