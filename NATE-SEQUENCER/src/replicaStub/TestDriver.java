package replicaStub;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class TestDriver {
	public static void main(String[] args) throws InterruptedException {
		
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(); 
			String msg = "reserveBook.natemacinnes.natemacinnes.bookname.authorname.concordia";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, 4545);
			aSocket.send(request);
			
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
		}
	}
}
