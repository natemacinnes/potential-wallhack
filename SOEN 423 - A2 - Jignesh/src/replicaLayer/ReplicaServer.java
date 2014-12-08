package replicaLayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;

import server.*;
import userInfo.Book;

public class ReplicaServer extends Thread{
	
	private String replicaName;
	private int replicaPort;
	private ReplicaInformation replicaInfo;
	//TODO: modify LibraryServerInterface type
	private HashMap<String,LibraryServerFunctionsImpl> serversMap;
	private HashMap<Integer,String> deliveryQueue;
	private HashMap<Integer,String> holdbackQueue;
	private boolean runReplica = false;
	private int messageSequenceNumber;
	private String logFileName;
	private HeartbeatListener listener;
	private HeartbeatClient client;
	private boolean supportHighAvailability;
	private int numOperationBeforeCrash;
	
	
	public ReplicaServer(String replicaName)
	{
		System.setProperty("java.net.preferIPv4Stack" , "true");
		supportHighAvailability = false;  //default mode is software failures tolerant
		this.replicaName = replicaName;
		replicaInfo = new ReplicaInformation();
		replicaPort = replicaInfo.getReplicaPort(replicaName);
		//TODO: modify LibraryServerInterface type
		serversMap = new HashMap<String,LibraryServerFunctionsImpl>();
		deliveryQueue = new HashMap<Integer, String>();
		holdbackQueue = new HashMap<Integer, String>();
		logFileName = "replicaLog.txt";
		listener = new HeartbeatListener(replicaName,replicaPort);
		client = new HeartbeatClient(replicaName);
	}
	
	public String getReplicaName()
	{
		return replicaName;
	}
	
	public int getReplicaPort()
	{
		return replicaPort;
	}
	
	public void stopReplica()
	{
		runReplica = false;
	}
	
	@Override
	public void run()
	{
		Collection<String> replicaNames = replicaInfo.getReplicaName();
		runReplica = true;
    	//DatagramSocket aSocket = null;
    	MulticastSocket aSocket = null;
    	
		try{
			// create socket at agreed port
	    	//aSocket = new DatagramSocket(replicaPort);
			aSocket = new MulticastSocket(replicaInfo.getMulticastPort());
			
			aSocket.joinGroup(InetAddress.getByName(replicaInfo.getMulticastAddr()));
	    	
			byte[] buffer = new byte[1000];
		    //keep server alive
 			while(runReplica){
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request);
  				
  				String operationReceived = extractMessage(request);
  				
  				//If the message is an operation to be performed on the library
  				if(isLibraryOperation(operationReceived))
  				{
  					String[] msgParts = operationReceived.split("\\.");
  					int msgNumber = Integer.parseInt(msgParts[0]);
  					System.out.println("received " + operationReceived);
  					
  					//If the replica received that message for the first time
  					if(!holdbackQueue.containsKey(msgNumber) && !deliveryQueue.containsKey(msgNumber))
  					{
  	  					/*
  	  					 * Send message to all replica if actual sequence number is greater or equal
  	  					 * than expected sequence number. If it is smaller, it means it is a previous
  	  					 * message and we don't care
  	  					 */
  	  					if(msgNumber >= messageSequenceNumber)
  	  					{
  	  						//Send the message received to all replica
  	  						for(String name: replicaNames)
  	  						{
  	  							
  								InetAddress replicaIp = InetAddress.getByName(replicaInfo.getReplicaIp(replicaName));
  	  				    		DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), 
  	  				    				replicaIp, replicaInfo.getReplicaPort(name));
  	  				    		aSocket.send(reply);
  	  						}
  	  						
  	  						/*
  	  						 * If actual sequence number is greater than expected sequence number,
  	  						 * It is a future message. Put it in holdbackQueue and ask sequencer to resend
  	  						 * expected sequence number.
  	  						 */
  	  						if(msgNumber > messageSequenceNumber)
  	  						{
  	  							holdbackQueue.put(msgNumber, operationReceived);
  	  							
  	  							String msg = "resend." + messageSequenceNumber;
  								InetAddress sequencerIp = InetAddress.getByName(replicaInfo.getSequencerIp());
  	  				    		DatagramPacket reply = new DatagramPacket(msg.getBytes(), msg.length(), 
  	  				    				sequencerIp, replicaInfo.getSequencerPort());
  	  				    		aSocket.send(reply);
  	  						}
  	  						/*
  	  						 * If actual sequence number is equal to expected sequence number,
  	  						 * It is the value expected. Put it in delivery queue, perform operation
  	  						 * and write it in log
  	  						 */
  	  						else
  	  						{
  	  							deliveryQueue.put(msgNumber, operationReceived);
  	  							String result = performLibraryOperation(operationReceived);
  	  						    recordOperation(operationReceived);
  	  						    messageSequenceNumber++;
  	  						    
  	  						    //Send result to front end
  	  						    InetAddress frontEndIp = InetAddress.getByName(replicaInfo.getFrontEndIp());
  	  				    		DatagramPacket reply = new DatagramPacket(result.getBytes(), result.length(), 
  	  				    			frontEndIp, replicaInfo.getFrontEndPort());
  	  				    		aSocket.send(reply);
  	  				    		System.out.println("result was sent to " + replicaInfo.getFrontEndIp() + " on port " + replicaInfo.getFrontEndPort());
  	  						}
  	  					}
  					}
  				}
  				//If the operation should be performed by the replica
  				else if(isReplicaOperation(operationReceived))
  				{
  					String result = performReplicaOperation(operationReceived);
  					System.out.println("received " + operationReceived);
  					
					//Send result to replica Manager
					InetAddress replicaManagerIp = InetAddress.getByName(replicaInfo.getReplicaManagerIp());
			        DatagramPacket reply = new DatagramPacket(result.getBytes(), result.length(), 
			        		replicaManagerIp, replicaInfo.getReplicaManagerPort());
			    	aSocket.send(reply);
  				}
  				//Operation is not recognized. Inform front end.
  				else
  				{
  					String result = "This operation was not recognized by the system";
  					
					//Send result to front end
					InetAddress frontEndIp = InetAddress.getByName(replicaInfo.getFrontEndIp());
			    	DatagramPacket reply = new DatagramPacket(result.getBytes(), result.length(), 
			    		frontEndIp, replicaInfo.getFrontEndPort());
			    	aSocket.send(reply);
  				}
    		}
 			
 	    	if(supportHighAvailability)
 	    	{
 	 			listener.stopListener();
 	 			client.stopClient();
 	    	}
		}
		catch(NumberFormatException e){
			System.out.println("Formatting number: " + e.getMessage());
		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
	}
	
	private String performReplicaOperation(String operation)
	{
		boolean isStartSFOperation = operation.equals("startSoftareFailureTolerantServers");
		boolean isRestartSFOperation = operation.equals("restartSoftareFailureTolerantServers");
		boolean isStartHAOperation = operation.equals("startHighlyAvailableServers");
		boolean isRestartHAOperation = operation.equals("restartHighlyAvailableServers");
		
		if(isStartSFOperation)
		{
			supportHighAvailability = false;
			return startServers(false);
		}
		else if(isRestartSFOperation)
		{
			supportHighAvailability = false;
			return restartServers();
		}
		else if(isStartHAOperation)
		{
			supportHighAvailability = true;
        	listener.start();
        	client.start();
			return startServers(false);
		}
		else if(isRestartHAOperation)
		{
			supportHighAvailability = true;
			return restartServers();
		}
		else
		{
			return "This operation was not recognized by the system";
		}
	}
	
	private String performLibraryOperation(String operation)
	{
		boolean isCreateAccountOperation = operation.contains("createAccount");
		boolean isReserveInterLibraryOperation = operation.contains("reserveInterLibrary");
		boolean isSetDurationOperation = operation.contains("setDuration");
		boolean isGetNonReturnersOperation = operation.contains("getNonReturners");
		boolean isReserveBookOperation = operation.contains("reserveBook");
		
		//replica1 is going to be the one that crashes for the purpose of the demo
		if(numOperationBeforeCrash == 0 && supportHighAvailability && replicaName.equals("replica1"))
		{
			stopHeartbeatClient();
		}
		
		if(numOperationBeforeCrash == 0 && !supportHighAvailability && replicaName.equals("replica1"))
		{
			return "wrong result";
		}
		else if(isCreateAccountOperation)
		{
			numOperationBeforeCrash--;
			return invokeCreateAccount(operation);
		}
		else if(isReserveBookOperation)
		{
			numOperationBeforeCrash--;
			return invokeReserveBook(operation);
		}
		else if(isReserveInterLibraryOperation)
		{
			numOperationBeforeCrash--;
			return invokeReserveInterLibrary(operation);
		}
		else if(isSetDurationOperation)
		{
			numOperationBeforeCrash--;
			return invokeSetDuration(operation);
		}
		else if(isGetNonReturnersOperation)
		{
			numOperationBeforeCrash--;
			return invokeGetNonReturners(operation);
		}
		else
		{
			return "Library operation not recognized";
		}
	}
	
	private String startServers(boolean isRestart)
	{
		//TODO: modify LibraryServerImpl type
		
		String host = "localhost";

		int conPort = 2022;
		int MCGPort = 2023;
		int UQAMPort = 2024;
		HashMap<String, Book> ConBookMap = new HashMap<String, Book>();
		HashMap<String, Book> mcgBookMap = new HashMap<String, Book>();
		HashMap<String, Book> uqamBookMap = new HashMap<String, Book>();

		//Add Book instance to hashmap for this server
		Book conBook1 = new Book("game of thrones 1", "GRR Martin", 1);
		Book conBook2 = new Book("game of thrones 2", "GRR Martin", 2);
		Book conBook3 = new Book("game of thrones 3", "GRR Martin", 3);
	
		//add booklist to the server
		ConBookMap.put(conBook1.bookName, conBook1);
		ConBookMap.put(conBook2.bookName, conBook2);
		ConBookMap.put(conBook3.bookName, conBook3);
		
		Book McgBook1 = new Book("game of thrones 1", "GRR Martin", 1);
		Book McgBook2 = new Book("game of thrones 2", "GRR Martin", 2);
		Book McgBook3 = new Book("game of thrones 3", "GRR Martin", 3);
	
		//add booklist to the server
		mcgBookMap.put(McgBook1.bookName, McgBook1);
		mcgBookMap.put(McgBook2.bookName, McgBook2);
		mcgBookMap.put(McgBook3.bookName, McgBook3);
		
		Book uqamBook1 = new Book("game of thrones 1", "GRR Martin", 1);
		Book uqamBook2 = new Book("game of thrones 2", "GRR Martin", 2);
		Book uqamBook3 = new Book("game of thrones 3", "GRR Martin", 3);
	
		//add booklist to the server
		uqamBookMap.put(uqamBook1.bookName, uqamBook1);
		uqamBookMap.put(uqamBook2.bookName, uqamBook2);
		uqamBookMap.put(uqamBook3.bookName, uqamBook3);

		serversMap.put("concordia", new LibraryServerFunctionsImpl(host, conPort, "concordia", new HashMap<String,Book>(ConBookMap)));
		serversMap.put("mcgill", new LibraryServerFunctionsImpl( host, MCGPort, "mcgill", new HashMap<String,Book>(mcgBookMap)));
		serversMap.put("uqam", new LibraryServerFunctionsImpl(host, UQAMPort, "uqam", new HashMap<String,Book>(uqamBookMap)));
		
		messageSequenceNumber = 1;
		numOperationBeforeCrash = 100;
		holdbackQueue.clear();
		deliveryQueue.clear();

		if(!isRestart)
		{
			//Delete existing log file
			File file  = new File(logFileName);
			file.delete();
		}
		
		return "Replica " + replicaName + " started its servers";
	}
	
	private String restartServers()
	{
		startServers(true);
		messageSequenceNumber = 1;
		holdbackQueue.clear();
		deliveryQueue.clear();
		updateServers();
		listener = new HeartbeatListener(replicaName,replicaPort);
		client = new HeartbeatClient(replicaName);
    	listener.start();
    	client.start();
		numOperationBeforeCrash = 100;
		return "Replica " + replicaName + " restarted its servers";
	}
	
	private boolean isLibraryOperation(String operation)
	{
		boolean isLibraryOperation = operation.contains("createAccount") || operation.contains("reserveInterLibrary")
				|| operation.contains("setDuration") || operation.contains("getNonReturners") || 
				operation.contains("reserveBook");
		
		return isLibraryOperation;
	}
	
	private boolean isReplicaOperation(String operation)
	{
		boolean isStartSFOperation = operation.equals("startSoftareFailureTolerantServers");
		boolean isRestartSFOperation = operation.equals("restartSoftareFailureTolerantServers");
		boolean isStartHAOperation = operation.equals("startHighlyAvailableServers");
		boolean isRestartHAOperation = operation.equals("restartHighlyAvailableServers");
		boolean isSendFalseResult = operation.equals("sendFalseResult");
		
		return (isStartSFOperation || isRestartSFOperation || isStartHAOperation || isRestartHAOperation ||  isSendFalseResult);
		
	}
	
	private String extractMessage(DatagramPacket request)
	{
			//get only the non-empty bit out of the byte array
			byte [] byteReceive = new byte[request.getLength()];
			for(int i = 0; i < request.getLength(); i++){
				byteReceive[i] = request.getData()[i];
			}
			
			//convert the byte into a string
			return new String(byteReceive);
	}
	
	/*
	 * Debug method
	 */
	public int getHoldbackQueueSize()
	{
		return holdbackQueue.size();
	}
	
	/*
	 * Debug method
	 */
	public int getDeliveryQueueSize()
	{
		return deliveryQueue.size();
	}
	
	/*
	 * Debug method
	 */
	public String getLogFileName()
	{
		return logFileName;
	}
	
	/*
	 * Debug method
	 */
	public int getMessageSequenceNumber()
	{
		return messageSequenceNumber;
	}
	
	private String invokeCreateAccount(String operation)
	{
		String firstName;
		String lastName;
		String email;
		String phoneNumber;
		String username;
		String password;
		String institution;
		String result;
		String[] msgParts = operation.split("\\.");
		
		/*
		 *  operation should have the sequence number, the name of the method
		 *  and 7 arguments
		 */
		if(msgParts.length < 9)
		{
			result = "Operation createAccount is missing arguments";
			return result;
		}
		else if(msgParts.length > 9)
		{
			result =  "Operation createAccount has too many arguments";
			return result;
		}
		
		firstName = msgParts[2];
		lastName = msgParts[3];
		email = msgParts[4];
		phoneNumber = msgParts[5];
		username = msgParts[6];
		password = msgParts[7];
		institution = msgParts[8];
		
		result = serversMap.get(institution).createAccount(firstName, lastName, email, phoneNumber, username, password, institution);
		return result;
	}
	
	
	private String invokeReserveBook(String operation)
	{
		String username;
		String password;
		String bookTitle;
		String authorName;
		String institution;
		String result;
		
		String[] msgParts = operation.split("\\.");
		
		/*
		 *  operation should have the sequence number, the name of the method
		 *  and 5 arguments
		 */
		if(msgParts.length < 7)
		{
			result = "Operation reserveBook is missing arguments";
			return result;
		}
		else if(msgParts.length > 7)
		{
			result =  "Operation reserveBook has too many arguments";
			return result;
		}
		

		username = msgParts[2];
		password = msgParts[3];
		bookTitle = msgParts[4];
		authorName = msgParts[5];
		institution = msgParts[6];
		
		
		result = serversMap.get(institution).reserveBook(username, password, bookTitle, authorName, institution);
		return result;
	}
	
	private String invokeReserveInterLibrary(String operation)
	{
		String username;
		String password;
		String bookTitle;
		String authorName;
		String institution;
		String result;
		
		String[] msgParts = operation.split("\\.");
		
		/*
		 *  operation should have the sequence number, the name of the method
		 *  and 5 arguments
		 */
		if(msgParts.length < 7)
		{
			result = "Operation reserveInterLibrary is missing arguments";
			return result;
		}
		else if(msgParts.length > 7)
		{
			result =  "Operation reserveInterLibrary has too many arguments";
			return result;
		}
		

		username = msgParts[2];
		password = msgParts[3];
		bookTitle = msgParts[4];
		authorName = msgParts[5];
		institution = msgParts[6];


		result = serversMap.get(institution).reserveInterLibrary(username, password, bookTitle, authorName);
		return result;	
	}
	
	
	private String invokeSetDuration(String operation)
	{
		String username;
		String bookTitle;
		int numDays;
		String institution;
		String result;
		
		String[] msgParts = operation.split("\\.");
		
		/*
		 *  operation should have the sequence number, the name of the method
		 *  and 4 arguments
		 */
		if(msgParts.length < 6)
		{
			result = "Operation setDuration is missing arguments";
			return result;
		}
		else if(msgParts.length > 6)
		{
			result =  "Operation setDuration has too many arguments";
			return result;
		}
		
		try
		{
			username = msgParts[2];
			bookTitle = msgParts[3];
			numDays = Integer.parseInt(msgParts[4]);
			institution = msgParts[5];
			
			//FIXME change return params
			//result = serversMap.get(institution).setDuration(username, bookTitle, numDays);
			return result = "";
		}
		catch(NumberFormatException e)
		{
			result =  "Operation setDuration failed: argument numOfDays cannot be converted to number";
			return result;
		}	
	}
	
	private String invokeGetNonReturners(String operation)
	{
		String adminUsername;
		String adminPassword;
		int numDays;
		String institution;
		String result;
		
		String[] msgParts = operation.split("\\.");
		
		/*
		 *  operation should have the sequence number, the name of the method
		 *  and 4 arguments
		 */
		if(msgParts.length < 6)
		{
			result = "Operation setDuration is missing arguments";
			return result;
		}
		else if(msgParts.length > 6)
		{
			result =  "Operation setDuration has too many arguments";
			return result;
		}
		
		try
		{
			adminUsername = msgParts[2];
			adminPassword = msgParts[3];
			institution = msgParts[4];
			numDays = Integer.parseInt(msgParts[5]);
			
			//FIXME change return params
			//result = serversMap.get(institution).getNonReturners(adminUsername, adminPassword, numDays);
			return result = "";
		}
		catch(NumberFormatException e)
		{
			result =  "Operation getNonReturners failed: argument numOfDays cannot be converted to number";
			return result;
		}
		
	}
	
	
	private void updateServers()
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(logFileName));
			String operation;
			
			while((operation = reader.readLine()) != null)
			{
				numOperationBeforeCrash = 100; //reset to 3 each time to be sure that all the operation are performed
				performLibraryOperation(operation);
				deliveryQueue.put(messageSequenceNumber, operation);
				messageSequenceNumber++;
			}
			
			reader.close();
		}
		catch(IOException e){}
		finally
		{
			if(reader != null)
			{
				try
				{
					reader.close();
				}
				catch(IOException e) {}
			}
		}	
	} 
	
	//Method to record each operation performed
	private void recordOperation(String operation)
	{
		PrintWriter printWriter = null;
		try
		{
			FileWriter fileWriter = new FileWriter(logFileName, true);
			printWriter = new PrintWriter(new BufferedWriter(fileWriter));
			printWriter.println(operation);
			printWriter.close();
		}
		catch(IOException e) {}
		finally
		{
			if(printWriter != null)
			{
				printWriter.close();
			}
		}
	}
	
	private void stopHeartbeatClient()
	{
		client.stopClient();
	}

}
