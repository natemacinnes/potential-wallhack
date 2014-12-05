package transportLayer;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer {
	
	int multicastPort;
	String multicastAddr;
	String message;
	
	public MulticastServer() {
		this.multicastPort = 5000;
		this.multicastAddr = "224.24.24.24";
	}
	
	public MulticastServer(int multicastPort, String multicastAddr) {
		this.multicastPort = multicastPort;
		this.multicastAddr = multicastAddr;
	}
	
	public void run() {
		MulticastSocket s = null;
		try {
			s = new MulticastSocket(multicastPort);
			
			DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(multicastAddr), multicastPort);
			
			System.out.println("Sending messages to relicas...");
			s.send(packet);
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
