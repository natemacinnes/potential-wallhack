package transportLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPSequencerClient {
	
	public String fetch(int serverPort, int numDays){
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(); 
			String msg = "fetchNonReturners:" +  String.valueOf(numDays);
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, serverPort);
			aSocket.send(request);			                        
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byte [] byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String result = new String(byteReceive,"UTF-8");
			aSocket.close();
			return result;
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			}
		
		return "";
	}
	
	public boolean reserveBookRemotely(int serverPort, String bookTitle, String authorName){
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(); 
			String msg = "reserveBookRemotely:" + bookTitle + "," + authorName;
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");		                                                 
			DatagramPacket request =
			 	new DatagramPacket(m,  msg.length(), aHost, serverPort);
			aSocket.send(request);			                        
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			
			//get only the non-empty bit out of the byte array
			byte [] byteReceive = new byte[reply.getLength()];
			for(int i = 0; i < reply.getLength(); i++){
				byteReceive[i] = reply.getData()[i];
			}
			
			String result = new String(byteReceive,"UTF-8");
			aSocket.close();
			//if the server return the string pass, the reservation was done successfully
			if(result.equals("pass"))
			{
				return true;
			}
			//if the server return the string fail, the reservation was not done successfully
			else if(result.equals("fail"))
			{
				return false;
			}
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null) 
				aSocket.close(); 
			}
		
		return false;
	}

}
