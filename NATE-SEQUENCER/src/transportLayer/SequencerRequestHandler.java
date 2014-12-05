package transportLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import serverLayer.SequencerImpl;
import serverLayer.SequencerInterface;

public class SequencerRequestHandler extends Thread {
	
	private DatagramPacket request;
	private SequencerInterface sequencer;
	private DatagramSocket socket;
	private int j;
	
	public SequencerRequestHandler(SequencerInterface sequencer, DatagramPacket request, DatagramSocket socket){
		this.sequencer = sequencer;
		this.request = request;
		this.socket = socket;
		this.start();
	}
	
	public void run(){
		
		//get only the non-empty bit out of the byte array
		byte [] byteReceived = new byte[request.getLength()];
		for(int i = 0; i < request.getLength(); i++) {
			byteReceived[i] = request.getData()[i];
		}
		
		//convert the byte into a string
		String textReceived = new String(byteReceived);
		System.out.println("Broadcast message.");
		
		broadcastMessage(textReceived);
	
	}
		
	public void broadcastMessage(String message) {
		try
		{
			// String[] parts = message.split(".");
			String result = message;
			//send result back to calling server
			System.out.println("Broadcasting message...");
			
			sequencer.receiveMessage(result);
			
		} catch(NumberFormatException e) {
			System.out.println("Formatting number: " + e.getMessage());
		}	
	}
	
	public void receiveErrorMessage(String message) {
		// TO-DO: error handling when a replica does not receive a message
	}

}
