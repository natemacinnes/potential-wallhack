package transportLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import serverLayer.InterServerOperation;

public class RequestHandler extends Thread {
	
	private DatagramPacket request;
	private InterServerOperation server;
	private DatagramSocket socket;
	
	public RequestHandler(InterServerOperation server, DatagramPacket request, DatagramSocket socket){
		this.request = request;
		this.server = server;
		this.socket = socket;
		this.start();
	}
	
	public void run(){
		
		//get only the non-empty bit out of the byte array
		byte [] byteReceive = new byte[request.getLength()];
		for(int i = 0; i < request.getLength(); i++){
			byteReceive[i] = request.getData()[i];
		}
		
		//convert the byte into a string
		String textReceive = new String(byteReceive);
		if(textReceive.toLowerCase().contains("fetchNonReturners".toLowerCase()))
		{
			getNonReturners(textReceive);
		}
		else if(textReceive.toLowerCase().contains("reserveBookRemotely".toLowerCase()))
		{
			reserveInterLibrary(textReceive);
		}
	}
	
	public void getNonReturners(String message)
	{
		try
		{
			String[] parts = message.split(":");
			String arg = parts[1];
			//convert the argument obtained into an integer
			int numDays = Integer.parseInt(arg);
			//get the non returners
			String result = server.ISOGetNonReturners(numDays);
			//send result back to calling server
		DatagramPacket reply = new DatagramPacket(result.getBytes(), result.length(), 
				request.getAddress(), request.getPort());
		socket.send(reply);
		}
	
	catch(NumberFormatException e){
		System.out.println("Formatting number: " + e.getMessage());
	}
	catch (SocketException e){System.out.println("Socket: " + e.getMessage());
	}catch (IOException e) {System.out.println("IO: " + e.getMessage());
	}
		
	}
	
	public void reserveInterLibrary(String message)
	{
		String result;
		try
		{
			String[] parts = message.split(":");
			String[] args = parts[1].split(",");
			//reserve the book
			boolean operationDone = server.ISOReserveBook(args[0], args[1]);
			if(operationDone)
			{
				result = "pass";
			}
			else
			{
				result = "fail";
			}
			//send result back to calling server
		DatagramPacket reply = new DatagramPacket(result.getBytes(), result.length(), 
				request.getAddress(), request.getPort());
		socket.send(reply);
		}
		catch(SocketException e)
		{
			System.out.println("Socket: " + e.getMessage());
		}
		catch(IOException e)
		{
			System.out.println("IO: " + e.getMessage());
		}
	}

}
