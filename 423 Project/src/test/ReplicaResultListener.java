package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import corba.FrontEnd;

public class ReplicaResultListener extends Thread {

	private FrontEnd fe;
	public String resultReplica1;
	public static int TestUDPPortReplicaListener = 2021;
	public static int numOfReplicas = 3;
	
	public static HashMap<String,String> networkInfoList = new HashMap<String,String>();


	public ReplicaResultListener(FrontEnd fe) {
		this.fe = fe;	
		
		//FIXME add the 
		networkInfoList.put("replica1", "7777-132.205.95.21");
		networkInfoList.put("replica2", "7778-127.0.0.1");
		networkInfoList.put("replica3", "7779-127.0.0.1");
	}

	/*
	 * Thread started once the Front End gets an acknowledgment from the
	 * sequencer that a message has been multicasted to the replicas
	 */
	public void run() {
		DatagramSocket aSocket = null;

		try {
			aSocket = new DatagramSocket(TestUDPPortReplicaListener);
			byte[] buffer = new byte[1000]; // create socket at agreed port
			String repResultString;
			// FIXME change the flag for the loop
			boolean messagesReceived = false;
			ArrayList<String> repResultSet = new ArrayList<String>();
			ArrayList<String> repAddressSet = new ArrayList<String>();
			String repResultAddress;
			String[] ipAddressSet = new String[3];
			int numRes = 0;
			
			System.out.println("REPLICA LISTENER RUNNING");

			while (!messagesReceived) {

				DatagramPacket request = new DatagramPacket(buffer,
						buffer.length);
				aSocket.receive(request);

				repResultString = new String(request.getData()).replaceAll("\u0000.*", "");
				repResultAddress = request.getAddress().toString();
				
				// FIXME Decide on the result, based on the mode of the failure.

				if (repResultString != null) {
					
					repResultSet.add(repResultString);
					repAddressSet.add(repResultAddress);
					
					if(repResultSet.size() == numOfReplicas)
					{
						decide(repResultSet, repAddressSet);
						messagesReceived = true;//flag to exit the loop						
					}


				}
				
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

	public void decide(ArrayList<String> repResultSet, ArrayList<String> repAddressSet) {
		// FIXME some logic to decide

		String[] resultSet = (String[]) repResultSet.toArray();
		String[] addressSet = (String[]) repAddressSet.toArray();

		//Check to make sure replica addresses are unique
		//FIXME add a case to handle the false case of this as needed
		
		if( (!addressSet[0].equals(addressSet[1]) ) &&
				(!addressSet[1].equals(addressSet[2]) ) &&
				(!addressSet[2].equals(addressSet[0]) ) )
		{
			//Check if the results are the same. 
			if ((resultSet[0].equals(resultSet[1]) ) &&
			(resultSet[1].equals(resultSet[2]) ) &&
			(resultSet[2].equals(resultSet[0]) ) ){
			
				//ideal case, no software failure
				//send a result to front end to return to client
				System.out.println("Correct result to be returned to client: " + resultSet[0]);
				fe.sendResult(resultSet[0]);

			}
			//case where there is one that is different
			else if ((resultSet[0].equals(resultSet[1]) ) ||
					(resultSet[1].equals(resultSet[2]) ) ||
					(resultSet[2].equals(resultSet[0]) ))
					{
						//send the replica manager a notice that there is a software failure on a specific replica
				
						//Replica 1 software failure
						if ((resultSet[2].equals(resultSet[1]) )){
						//Send result to the replica manager
							//networkInfoList.containsValue(value)g
							
						}
						//Replica 2 software failure
						else if ((resultSet[0].equals(resultSet[2]))){
							
						}
						//Replica 3 software failure
						else if((resultSet[0].equals(resultSet[1]) )){
						
						}
						
					}
			
		}
		
		
		//FIXME copy the mapping from Paterson's code
		//NOTE: to send if there's an error. 
		///replica# send wrong result
	}

}
