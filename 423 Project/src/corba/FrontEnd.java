package corba;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import test.ReplicaResultListener;

//Front end receives CORBA invocations from Client.
//The FrontEnd class will forward client requests to the Sequencer via UDP

//FIXME refactor the send udp message into a new method, then add code for the remaining 3 methods.

public class FrontEnd extends corbaOperationsPOA {

	public static int TestUDPPortSequencer = 2020;
	public static int TestUDPPortReplicaListener = 2021;
	public static String argDelim = ".";
	public final String confirmMsg = "confirm";
	public static String replicaResult;

	@Override
	public String createAccount(String firstName, String lastName, String emailAddress, String phoneNumber, String userName, String password, String institution) {

		System.out.println("Test calling create account method from frontEnd");
		// sends a UDP message to the Sequencer

		DatagramSocket aSocket = null;
		byte[] buffer = new byte[1000];
		String result = null;
		// Format arguments according to coding convention
		String sequencerArgs = "cra" + argDelim + firstName + argDelim + lastName + argDelim + emailAddress + argDelim + phoneNumber + argDelim + userName
				+ argDelim + password + argDelim + institution;
		try {
			aSocket = new DatagramSocket();

			byte[] reqMsg = sequencerArgs.getBytes();// will receive the data
			InetAddress aHost = InetAddress.getByName("localhost");

			DatagramPacket request = new DatagramPacket(reqMsg, reqMsg.length, aHost, TestUDPPortSequencer);

			// SEND REQUEST TO SEQUENCER
			aSocket.send(request);

			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

			// Get the reply
			aSocket.receive(reply);

			// add formatting to string
			String rep = new String(reply.getData()).replaceAll("\u0000.*", "");

			System.out.println(rep);

			// Start a UDP server to get the results from the three replicas

			if (rep.equals(confirmMsg)) {// rep is an ACK from the sequencer

				// start server
				ReplicaResultListener rep1 = new ReplicaResultListener(this);
				rep1.start();
				rep1.join();
			}

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (aSocket != null)
				aSocket.close();
		}

		return replicaResult;
	}

	public void sendResult(String result) {
		replicaResult = result;
	}

	@Override
	public String reserveBook(String userName, String password, String bookName, String bookAuthor, String institution) {
		return "TEST";

	}

	@Override
	public void getNonReturners(String adminUsername, String adminPassword, String educationalInstitution, int numdays) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reserveInterLibrary(String username, String password, String bookName, String authorName) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 * @throws InvalidName
	 * @throws WrongPolicy
	 * @throws ServantAlreadyActive
	 * @throws ObjectNotActive
	 * @throws FileNotFoundException
	 * @throws AdapterInactive
	 */
	public static void main(String[] args) throws InvalidName, ServantAlreadyActive, WrongPolicy, ObjectNotActive, FileNotFoundException, AdapterInactive {
		// FIXME add initialization for getting IOR reference
		ORB orb = ORB.init(args, null);
		POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));

		// Obtain a reference
		// FIXME add the orb, location, host and port here when creating servant
		// instance
		FrontEnd aBehavior = new FrontEnd();
		byte[] id = rootPOA.activate_object(aBehavior);
		org.omg.CORBA.Object ref = rootPOA.id_to_reference(id);

		// Translate to IOR and write to a file
		String ior = orb.object_to_string(ref);
		System.out.println(ior);

		PrintWriter file = new PrintWriter("ior.txt");
		file.println(ior);
		file.close();

		System.out.println("Front End ready...\n\n");

		// active and run the ORB
		rootPOA.the_POAManager().activate();
		orb.run();
	}

	public void setReplicaResult(String repRes) {
		replicaResult = repRes;
		System.out.println("test setter" + replicaResult);
	}

	public String getReplicaResult() {
		return replicaResult;
	}

}
