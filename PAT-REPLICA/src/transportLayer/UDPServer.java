package transportLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import serverLayer.InterServerOperation;

public class UDPServer extends Thread {
	
	private InterServerOperation server;
	private int port;
	
	public UDPServer(InterServerOperation server, int port){
		this.server = server;
		this.port = port;
	}
	
	@Override
	public void run(){
    	DatagramSocket aSocket = null;
		try{
			// create socket at agreed port
	    	aSocket = new DatagramSocket(port);
	    	
			byte[] buffer = new byte[1000];
		    //keep server alive
 			while(true){
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request);
  				RequestHandler handler = new RequestHandler(server,request,aSocket);
  				
  				//get only the non-empty bit out of the byte array
  				/*byte [] byteReceive = new byte[request.getLength()];
  				for(int i = 0; i < request.getLength(); i++){
  					byteReceive[i] = request.getData()[i];
  				}*/
  				
  				//convert the byte into a string
  				//String textReceive = new String(byteReceive);
  				//convert the text obtained into an integer
  				//int numDays = Integer.parseInt(textReceive);
  				//get the non returners
  				//String result = server.ISOGetNonReturners(numDays);
  				//System.out.println(result);
  				//send result back to calling server
    			/*DatagramPacket reply = new DatagramPacket(result.getBytes(), result.length(), 
        				request.getAddress(), request.getPort());
    			aSocket.send(reply);*/
    		}
		}
		catch(NumberFormatException e){
			System.out.println("Formatting number: " + e.getMessage());
		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
	}

}
