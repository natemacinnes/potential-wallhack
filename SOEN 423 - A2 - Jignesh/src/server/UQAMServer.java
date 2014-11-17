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



public class UQAMServer {

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
		
		//initialize ORB
		ORB orb = ORB.init(args,null);
		POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
	
		//Obtain a reference
		//FIXME add the orb, location, host and port here whencreating servant instance
		LibraryServerFunctionsImpl aBehavior = new LibraryServerFunctionsImpl(orb, host, port, location, bookMap);
		byte[] id = rootPOA.activate_object(aBehavior);
		org.omg.CORBA.Object ref = rootPOA.id_to_reference(id);
		
		//Translate to IOR and write to a file
		String ior = orb.object_to_string(ref);
		System.out.println(ior);
		
		PrintWriter file = new PrintWriter(location + "ior.txt");
		file.println(ior);
		file.close();
	
		System.out.println(location+" LibraryServer ready...\n\n");
		//System.out.println(location+" UDP Server ready...");
		
		//active and run the ORB
		rootPOA.the_POAManager().activate();
		orb.run();
	}

}
