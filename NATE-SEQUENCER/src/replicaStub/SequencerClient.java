package replicaStub;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SequencerClient implements Runnable {
	int mulitcastPort;
	String multicastAddr;
	
	public SequencerClient () {
		System.setProperty("java.net.preferIPv4Stack" , "true");
		mulitcastPort = 5000;
		multicastAddr = "224.24.24.24";
	}
	
	public SequencerClient (int port, String group) {
		super();
		this.mulitcastPort = port;
		this.multicastAddr = group;
	}
	
	public void run() {
		MulticastSocket s = null;
		try {
			
			s = new MulticastSocket(this.mulitcastPort);
			
			s.joinGroup(InetAddress.getByName(multicastAddr));
			
			byte buf[] = new byte[1024];
			
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			while (true) {
				s.receive(packet);
				
				System.out.println("Received data from: " + packet.getAddress().toString() +
						":" + packet.getPort() + " with length: " +
						packet.getLength());
				System.out.write(packet.getData(), 0, packet.getLength());
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			s.close();
			
		}
		
	}
	
	
	
}
