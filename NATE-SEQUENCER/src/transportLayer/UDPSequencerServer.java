package transportLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import serverLayer.SequencerInterface;

public class UDPSequencerServer extends Thread {

	private SequencerInterface sequencer;
	private int port;

	public UDPSequencerServer(SequencerInterface sequencer, int port){
		this.sequencer = sequencer;
		this.port = port;
	}
	
	@Override
	public void run(){
    	DatagramSocket aSocket = null;
		try{
			// create socket at agreed port
	    	System.out.println("Listening on udp:" + port);
			aSocket = new DatagramSocket(port);
	    	
			byte[] buffer = new byte[1000];
		    
			//keep server alive
 			while(true){
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request);
  				
  					System.out.println("Received request from client. \n"
  							+ "Sending request to request handler.");
  					SequencerRequestHandler handler = new SequencerRequestHandler(sequencer, request, aSocket);
    		}
		} catch(NumberFormatException e){
			System.out.println("Formatting number: " + e.getMessage());
			e.printStackTrace();
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
			e.printStackTrace();
		}catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
			e.printStackTrace();
		}finally {
			if(aSocket != null) aSocket.close();
		}
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
