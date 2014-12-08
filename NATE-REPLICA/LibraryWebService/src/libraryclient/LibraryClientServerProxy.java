/**
 * 	Acts as an intermediary between clients 
 *  and server for setting up the library servers.
 * 
 * 	Author: Nathan MacInnes 1957341
 * 	Date: November 18 2014
 */
package libraryclient;

import java.io.IOException;
import java.net.URL;

import libraryclient.LibraryServerImplBaseService;
import libraryclienttest.TestClient;


public class LibraryClientServerProxy {

	public static LibraryServer concordia;
	public static LibraryServer mcgill;
	public static LibraryServer queens;

	public static void lookupServers() {
		
		try {
		URL concordiaURL = new URL("http://localhost:7777/libraryserver?wsdl"); 
		LibraryServerImplBaseService concordiaService = new LibraryServerImplBaseService(concordiaURL);
		concordia = concordiaService.getLibraryServerImplBasePort();
		System.out.println(concordiaService.getLibraryServerImplBasePort());
		
		URL mcgillURL =  new URL("http://localhost:7778/libraryserver?wsdl");
		LibraryServerImplBaseService mcgillService = new LibraryServerImplBaseService(mcgillURL);
		mcgill = mcgillService.getLibraryServerImplBasePort();
		System.out.println(mcgillService.getLibraryServerImplBasePort());
		
		URL queensURL =  new URL("http://localhost:7779/libraryserver?wsdl");
		LibraryServerImplBaseService queensService =  new LibraryServerImplBaseService(queensURL);
		queens = queensService.getLibraryServerImplBasePort();
		System.out.println(queensService.getLibraryServerImplBasePort());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static libraryclient.LibraryServer lookupServer(String school) throws IOException {
		if (school.equalsIgnoreCase("concordia")) {
			// Connect to Concordia Library Server 
			TestClient.io.write("Successfully connected to the Concordia Library Server.");
			return concordia;
		}
		if (school.equalsIgnoreCase("mcgill")) {
			// Connect to McGill Library Server 
			TestClient.io.write("Successfully connected to the McGill Library Server.");
			return mcgill;
		}
		if (school.equalsIgnoreCase("queens")) {
			// Connect to Queens Library Server 
			TestClient.io.write("Successfully connected to the Queens Library Server.");
			return queens;
		}
		
		System.out.println("Could not locate " + school + " library.");
		return null;
	}
	
}
