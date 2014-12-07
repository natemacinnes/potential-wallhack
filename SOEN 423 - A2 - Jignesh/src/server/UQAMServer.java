package server;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
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



public class UQAMServer implements LibraryServer {

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

	
	public UQAMServer(String location, String host, int port)
	{
		this.location = location;
		this.host = host;
		this.port = port;
	}
	
	
	public static void main (String args[]) throws InvalidName, ServantAlreadyActive, WrongPolicy, ObjectNotActive, FileNotFoundException, AdapterInactive {
		//start server here
		//FIXME determine how to implement the three servers. see boxOfficeServer.java
		
		//create a booklist
		Book conBook1 = new Book("game of thrones 1", "GRR Martin", 11);
		Book conBook2 = new Book("game of thrones 2", "GRR Martin", 22);
		Book conBook3 = new Book("game of thrones 3", "GRR Martin", 33);
	
		//add booklist to the server
		bookMap.put(conBook1.bookName, conBook1);
		bookMap.put(conBook2.bookName, conBook2);
		bookMap.put(conBook3.bookName, conBook3);

		
		String location = "UQAM";
		String host = "localhost";
		int port = 2022;
		
		//Obtain a reference
		//LibraryServerFunctionsImpl aBehavior = new LibraryServerFunctionsImpl(host, port, location, bookMap);

	
		System.out.println(location+" LibraryServer ready...\n\n");

	}

}
