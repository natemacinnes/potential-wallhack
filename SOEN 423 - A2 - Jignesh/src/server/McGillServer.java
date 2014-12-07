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

import userInfo.Book;



public class McGillServer implements LibraryServer {

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

	public McGillServer(String location, String host, int port)
	{
		this.location = location;
		this.host = host;
		this.port = port;
	}
	
	
	public static void main (String args[]) throws InvalidName, ServantAlreadyActive, WrongPolicy, ObjectNotActive, FileNotFoundException, AdapterInactive {
		//start server here
		//FIXME determine how to implement the three servers. see boxOfficeServer.java
		
		//create a few books to test application
		Book mcGBook1 = new Book("Harry Potter 1", "JK Rowling", 11);
		Book mcGBook2 = new Book("Manhood", "Terry Crews", 2);
		Book mcGBook3 = new Book("Mcgill sucks", "everyone", 13);
	
		bookMap.put(mcGBook1.bookName, mcGBook1);
		bookMap.put(mcGBook2.bookName, mcGBook2);
		bookMap.put(mcGBook3.bookName, mcGBook3);
		
		final String institution = "McGill";
		String host = "localhost";
		final int port = 2020;
		

		//Obtain a reference
		//LibraryServerFunctionsImpl aBehavior = new LibraryServerFunctionsImpl(host, port, institution, bookMap);

		
		System.out.println(institution+" LibraryServer ready...\n\n");

	}

}
