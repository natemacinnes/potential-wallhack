package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import udp.UDPServer;
import userInfo.Book;



public class ConcordiaServer implements LibraryServer{

	/**
	 * @param args
	 * @throws InvalidName 
	 * @throws FileNotFoundException 
	 * @throws WrongPolicy 
	 * @throws ServantAlreadyActive 
	 * @throws ObjectNotActive 
	 * @throws AdapterInactive 
	 */
	
	String location = null;
	String host = null;
	int port = -1;
	public static HashMap<String, Book> bookMap = new HashMap<String, Book>();

	public ConcordiaServer(String location, String host, int port)
	{
		this.location = location;
		this.host = host;
		this.port = port;
	}
	
	public static void main (String args[]) throws InvalidName, ServantAlreadyActive, WrongPolicy, ObjectNotActive, FileNotFoundException, AdapterInactive {
		//start server here
		//FIXME determine how to implement the three servers. see boxOfficeServer.java
		
		//Add Book instance to hashmap for this server
		Book conBook1 = new Book("game of thrones 1", "GRR Martin", 11);
		Book conBook2 = new Book("game of thrones 2", "GRR Martin", 22);
		Book conBook3 = new Book("game of thrones 3", "GRR Martin", 33);
	
		//add booklist to the server
		bookMap.put(conBook1.bookName, conBook1);
		bookMap.put(conBook2.bookName, conBook2);
		bookMap.put(conBook3.bookName, conBook3);

		
		final String institution = "Concordia";
		String host = "localhost";
		final int port = 2021;
		
		//initialize ORB

		//Obtain a reference
		//FIXME add the orb, location, host and port here when creating servant instance
//		LibraryServerFunctionsImpl aBehavior = new LibraryServerFunctionsImpl(orb, host, port, institution, bookMap);


		System.out.println(institution+" LibraryServer ready...\n\n");

	}

	

}
