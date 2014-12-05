package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/*
 * Stub exists to receive UDP messages from the frontEnd
 * 
 * Currently only prints the arguments that were sent.
 * 
 * */

public class testSequencer {

	public static int TestUDPPortSequencer = 2020;

	public static void main(String[] args) {
		final String confirmMsg = "confirm";

		Thread t = new Thread(new Runnable() {
			// Server code
			@Override
			public void run() {
				DatagramSocket aSocket = null;

				try {
					aSocket = new DatagramSocket(TestUDPPortSequencer);
					byte[] buffer = new byte[1000]; // create socket at agreed
													// port
					byte[] ack = null;

//LISTEN FOR REQUESTS FROM FRONTENDs
					while (true) {
						DatagramPacket request = new DatagramPacket(buffer, buffer.length);
						// get request
						aSocket.receive(request);

						String requestString = new String(request.getData());
						// Print the request out

						System.out.println("\nSEQUENCER TEST REQUEST ARGS: " + requestString.replaceAll("\u0000.*", ""));

						ack = confirmMsg.getBytes();

						DatagramPacket reply = new DatagramPacket(ack, ack.length, request.getAddress(), request.getPort());
						// Send the reply (if needed)
						// Also convert the string into a byte array before
						// sending it as a reply
						aSocket.send(reply);
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
		});

		t.start();

		System.out.println("SEQUENCER TEST UDP Server" + "with port " + TestUDPPortSequencer + " is up");
	}

}
