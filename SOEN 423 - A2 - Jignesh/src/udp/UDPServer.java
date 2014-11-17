package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import server.ConcordiaServer;

public class UDPServer implements Runnable{
	private String udpHost = null;
	private int udpPort = 0;
	private String institution = null;

	public UDPServer(String uDPHost, int uDPPort, String institution) {
		this.udpHost = uDPHost;
		this.udpPort = uDPPort;
		this.institution = institution;
	}

	// method to start the UDP server
	public void start() throws IOException, ClassNotFoundException {

		DatagramSocket aSocket = null;

		System.out.println("Test UDP Server" + "with port " + udpPort + ""+ institution + " is up");
		
		try {
			aSocket = new DatagramSocket(udpPort);
			// create socket at agreed port
			byte[] buffer = new byte[1000];
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				// get request
				aSocket.receive(request);
				
				//FIXME test with sending a string back.
				byte[] testSendData = this.institution.getBytes();
				//Get the books from a given server.
				//ConcordiaServer.bookMap
				
				DatagramPacket reply = new DatagramPacket(testSendData,
						testSendData.length, request.getAddress(),
						request.getPort());
				// Send the reply
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

	public void run() {
		try {
			start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
