package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import corba.FrontEnd;

public class ReplicaResultListener extends Thread {

	private FrontEnd fe;
	public String resultReplica1;
	public int UDPPortReplicaListener = 2021;
	public int RMPort = 7782;
	public String RMAddress = "132.205.95.190";

	public static int numOfReplicas = 3;// FIXME change back to three

	public static HashMap<String, String> networkInfoList = new HashMap<String, String>();

	public ReplicaResultListener(FrontEnd fe) {
		this.fe = fe;

		// FIXME add the
		networkInfoList.put("/132.205.95.189", "replica2");
		networkInfoList.put("/132.205.95.190", "replica1");
		networkInfoList.put("/132.205.95.191", "replica3");
		
		
	}

	/*
	 * Thread started once the Front End gets an acknowledgment from the
	 * sequencer that a message has been multicasted to the replicas
	 */
	public void run() {
		DatagramSocket aSocket = null;

		try {
			aSocket = new DatagramSocket(UDPPortReplicaListener);
			// FIXME change the flag for the loop
			boolean messagesReceived = false;
			ArrayList<String> repResultSet = new ArrayList<String>();
			ArrayList<String> repAddressSet = new ArrayList<String>();

			String repResultAddress;
			int msgReturnCount = 0;

			System.out.println("REPLICA LISTENER RUNNING");

			while (!messagesReceived) {
				byte[] buffer = new byte[1000]; // create socket at agreed port

				String repResultString = null;

				DatagramPacket request = new DatagramPacket(buffer,
						buffer.length);
				aSocket.receive(request);

				repResultString = new String(request.getData()).replaceAll(
						"\u0000.*", "");
				repResultAddress = request.getAddress().toString();

				System.out.println("ADDRESS OF Message #"
						+ (msgReturnCount + 1) + " " + request.getAddress());
				System.out.println("CONTENT OF Message #"
						+ (msgReturnCount + 1) + " " + extractMessage(request));

				// FIXME Decide on the result, based on the mode of the failure.

				if (repResultString != null) {

					repResultSet.add(repResultString);
					repAddressSet.add(repResultAddress);

					msgReturnCount++;
					if (msgReturnCount == numOfReplicas) {
						decide(repResultSet, repAddressSet);
						messagesReceived = true;// flag to exit the loop
						repResultSet = null;
						repResultString = null;

					}

				}

			}
			// repAddressSet.clear();
			// repAddressSet.clear();

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}

	}

	private String extractMessage(DatagramPacket request) {
		// get only the non-empty bit out of the byte array
		byte[] byteReceive = new byte[request.getLength()];
		for (int i = 0; i < request.getLength(); i++) {
			byteReceive[i] = request.getData()[i];
		}

		// convert the byte into a string
		return new String(byteReceive);
	}

	public void decide(ArrayList<String> repResultSet,
			ArrayList<String> repAddressSet) {
		// FIXME some logic to decide
		String failedReplicaNum = null;

		String[] resultSet = repResultSet.toArray(new String[repResultSet
				.size()]);
		String[] addressSet = repAddressSet.toArray(new String[repAddressSet
				.size()]);
		
		System.out.println("FE DEBUG: 3 IP Addresses " + addressSet[0] + " " + addressSet[1]
				
				+ " " + addressSet[2]);

		// Check to make sure replica addresses are unique
		if ((!addressSet[0].equals(addressSet[1]))
				&& (!addressSet[1].equals(addressSet[2]))
				&& (!addressSet[2].equals(addressSet[0]))) {
			// Check if the results are the same.
			if ((resultSet[0].equals(resultSet[1]))
					&& (resultSet[1].equals(resultSet[2]))
					&& (resultSet[2].equals(resultSet[0]))

			) {

				System.out
						.println("\n\nDEBUG: FE LISTENER: IDEAL CASE, no software failure");

				// send a result to front end to return to client
				System.out.println("Correct result to be returned to client: "
						+ resultSet[0]);
				returnResult(resultSet[0]);

			}
			// case where there is one value that is different

			else if ((resultSet[0].equals(resultSet[1]))
					|| (resultSet[1].equals(resultSet[2]))
					|| (resultSet[2].equals(resultSet[0]))) {
				// send the replica manager a notice that there is a software
				// failure on a specific replica

				System.out
						.println("\n\nDEBUG: FRONT END - SEND SW FAILURE for one replica");
				// Replica 1 software failure if
				if ((resultSet[2].equals(resultSet[1]))) {
					// Send result to the replica manager
					
					int replCounter = 0;
					String failedReplicaAddress = addressSet[replCounter];
					
					System.out.println("FE DEBUG: R1 Address" + failedReplicaAddress);
					failedReplicaNum = networkInfoList.get(failedReplicaAddress);
					System.out.println("FE DEBUG: R1 number" + failedReplicaNum);

					
					notifyRMAboutRepFailure(failedReplicaNum);
					returnResult(resultSet[2]);

				} // Replica 2 software failure else if
				else if ((resultSet[0].equals(resultSet[2]))) {
					int replCounter = 1;
					
					String failedReplicaAddress = addressSet[replCounter];
					System.out.println("FE DEBUG: R2 Address" + failedReplicaAddress);

					failedReplicaNum = networkInfoList.get(failedReplicaAddress);
					System.out.println("FE DEBUG: R2 number" + failedReplicaNum);
		
					notifyRMAboutRepFailure(failedReplicaNum);
					returnResult(resultSet[0]);
				}

				// Replica 3 software failure else
				else if ((resultSet[0].equals(resultSet[1]))) {

					int replCounter = 2;
					String failedReplicaAddress = addressSet[replCounter];
					System.out.println("FE DEBUG: R3 Address" + failedReplicaAddress);

					failedReplicaNum = networkInfoList.get(failedReplicaAddress);
					System.out.println("FE DEBUG: R3 number" + failedReplicaNum);

					notifyRMAboutRepFailure(failedReplicaNum);
					returnResult(resultSet[0]);
				}

			}

			// FIXME change back to bottom code afterwards
			else {

				/*
				 * // Replica 1 software failure // Send result to the replica
				 * manager failedReplicaNum = "Replica 1";
				 * notifyRMAboutRepFailure(failedReplicaNum);
				 * returnResult(resultSet[0]);
				 */

				System.out.println("\n\nDEBUG FE: more than 2 sw error");

			}
		}

	}

	private void notifyRMAboutRepFailure(String failedReplicaNum) {
		DatagramSocket aSocket = null;
		// String replica# send wrong result

		try {

			aSocket = new DatagramSocket();
			byte[] msg = null;

			String errorMsg = failedReplicaNum + " send wrong result";
			msg = errorMsg.getBytes();

			// FIXME: add the port and address of the RM
			DatagramPacket reply = new DatagramPacket(msg, msg.length,
					InetAddress.getByName(RMAddress), RMPort);

			aSocket.send(reply);

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}

	private void returnResult(String result) {
		System.out
				.println("\n\nDEBUG: FRONT END - SENDING CORRECT RESULT TO CLIENT");
		fe.sendResult(result);
	}

}
