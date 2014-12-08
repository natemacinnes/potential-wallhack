/**
 * 	This is the webservice implementation that implements the 
 * 	LibraryServer interface webmethods and contains other 
 *  Library Server logic and data structures.
 *  
 * 	Author: Nathan MacInnes 1957341
 * 	Date: November 18 2014
 */

package libraryserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

import udp.UDPServer;

public class LibraryServerImplBase implements LibraryServer {
	
	public String institution = null;
	public String udpHost = null;
	public int udpPort = 0;
	public UDPServer udpServer;
	String delim = "-"; // delimiter for USP messages.
	private Hashtable<Character, ArrayList<StudentAccount>> accounts = new Hashtable<Character, ArrayList<StudentAccount>>();
	private BookCatalog catalog = new BookCatalog();
	private static ReadWriteTextFileWithEncoding io = new ReadWriteTextFileWithEncoding("src/LibraryServer/library_server.txt", "UTF8");
	
	private String message; 
	
	public LibraryServerImplBase() {
		
	}
	
	public LibraryServerImplBase(String udpHost, int udpPort, String institution){
		this.institution = institution;
		this.udpHost = udpHost;
		this.udpPort = udpPort;
		
		// creates three accounts on each server
		// puts three books in the mcgill library
		fillLibraryTestData();
		
		// Starts the USP server thread
		udpServer();
	}
	
	public synchronized String createAccount(String firstName, String lastName,
			String emailAddress, String phoneNumber, String username,
			String password, String educationalInstitution) {
		
		//Parse information username first character
		char initial = username.toLowerCase().charAt(0);
		String newUsername = username.toLowerCase();
		String invalidArg  = "Operation createAccount failed: One of the arguments is not valid";
		
		// Check that the username and password is the correct length
		try {
			if (username.length() < 6) {
				 return invalidArg; 
			} else if (username.length() > 15) {
				return invalidArg;
			} else if (password.length() < 6) {
				return invalidArg;
			}
			//Check that user does not already exist
			if (accounts.isEmpty() || !accounts.containsKey(initial)) {
				ArrayList<StudentAccount> sa = new ArrayList<StudentAccount>();
				sa.add(new StudentAccount(firstName, lastName, emailAddress, phoneNumber, newUsername, password, educationalInstitution));
				accounts.put(initial, sa);
				message = "Operation createAccount succeed in " + institution + "library";
				return message;
				
			} else if (accounts.get(initial) != null) {
				for (int i = 0; i < accounts.get(initial).size(); i++) {
					if (accounts.get(initial).get(i).isUsername(newUsername)) {
						message = ("Operation createAccount failed: username already exists");
						return message;
					}
				}
			} else {
				accounts.get(initial).add(new StudentAccount(firstName, lastName, emailAddress, phoneNumber, newUsername, password, educationalInstitution));
				accounts.put(initial, accounts.get(initial));
				System.out.println(accounts.get(initial).toString());
				message = "Operation createAccount succeed in " + institution + "library";
				return message;
			} 
		
			message =  ("Operation createAccount failed: username already exists.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	public synchronized String reserveBook(String username, String password,
			String bookName, String authorName) {
		StudentAccount student = null;
		// Check credentials
		if (authenticate(username, password) != null)
				{
					student = authenticate(username, password);
					// Begin borrowing process
					return borrowBook(student, bookName, authorName);
				}
		return "Operation reserveInterLibrary failed: username or password is wrong";
	}
	
	@Override
	public String intitutionReserve(String username, String password,
			String bookName, String authorName, String institution) {
		StudentAccount student = null;
		// Check credentials
		if (authenticate(username, password) != null)
				{
					student = authenticate(username, password);
					// Begin borrowing process
					return borrowBook(student, bookName, authorName);
				}
		return "The credentials entered are incorrect.";
	}

	@Override
	public synchronized String reserveInterLibrary(String username, String password,
			String book, String author) {
		
		Book remoteBook = null;
		DatagramSocket aSocket = null;
		byte[] buffer = new byte[1000];
		int[] remoteServerPorts = new int[2];
		
		if (authenticate(username, password) == null)
		{
			return "Operation reserveInterLibrary failed: username or password is wrong";
		}
		
		//Call reserveBook method on local server
		String message = intitutionReserve(username, password, book, author, institution);
		
		String result = "Operation reserveInterLibrary failed: No more copies or book not found";
		
		if (message.contains("succeed")) {
			return "Operation reserveInterLibrary succeed in "+ institution + " library";
		}
		
		// if message first character is not 0 check the other servers
		if(message.contains("Book doesn't exists") || message.contains("No more copies available"))
		{
		
			try {
			// Create a datagram socket
			aSocket = new DatagramSocket();

			// String will contain arguments to pass to server to determine which
			// operation to run
			char serverMethod = '0';// stands for getNonReturners
			
			String serverArgs = serverMethod + delim
					+ book + delim +  author + 
					delim + username + delim + password + delim;

			byte[] reqMsg = serverArgs.getBytes(); // will receive the data
			InetAddress aHost = InetAddress.getByName("localhost");

			
			if (this.udpPort == 4441) {
				remoteServerPorts[0] = 4442;
				remoteServerPorts[1] = 4443;
			} else if (this.udpPort == 4442) {
				remoteServerPorts[0] = 4441;
				remoteServerPorts[1] = 4443;
			} else if (this.udpPort == 4443) {
				remoteServerPorts[0] = 4441;
				remoteServerPorts[1] = 4442;
			} else {
				System.out.println("server not found");
			}

			// Bind to a port
			// Call remote server(s)
			for (int j = 0; j < remoteServerPorts.length; j++) {
				DatagramPacket request = new DatagramPacket(reqMsg,
						serverArgs.length(), aHost, remoteServerPorts[j]);
				aSocket.send(request);

				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

				// get the reply
				aSocket.receive(reply);

				String rep = new String(reply.getData());
				
				// format string in order to read method arguments
				String response = rep.split("\\-")[0]; 
				
				if (response.charAt('0') == '0'){
					remoteBook = new Book(book, author, 1);
		
					if (getStudentAccount(username) == null) {
						result = "User does not exist";
					} else {
						StudentAccount s = getStudentAccount(username);
						
						Loan l = new Loan();
						l.setBook(remoteBook);
						s.addLoan(l);
			
					}
					
					System.out.println(username + ", has remote"
							+ "ly reserved the book "
						+ remoteBook.getTitle() + " for 14 days \n");
				}	
				
				System.out.println("\nRemote Institution " + (j + 1) 
						+ ": \n");
				
				System.out.println(rep);
				
				if (rep.charAt(0) == 'B') {
						return "Operation reserveInterLibrary succeed: Book reserve at " + institution;
				} else {
					result = "Operation reserveInterLibrary failed: No more copies or book not found";
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
		return result;
	}

	@Override
	public synchronized String getNonreturners(String adminUsername, String adminPassword,
			String educationalInstitution, String numDays) {
		ArrayList<String> accountsOverdue = new ArrayList<String>();
		ArrayList<String> localAccountsOverdue = new ArrayList<String>();
		String rep = " ";
		//localAccountsOverdue.add(this.institution + " university : \n");
		//accountsOverdue.add(rep);
		
		if (0 != adminUsername.compareToIgnoreCase("admin") || 0 != adminPassword.compareTo("admin")) {
			return "Operation getNonReturners failed: invalid credentials";
		}
			DatagramSocket aSocket = null;
			int serverPort = 0;// = this.UDPPort;
			byte[] buffer = new byte[1000];
			int[] remoteServerPorts = new int[3];
			
			try {
				// Create a datagram socket
				aSocket = new DatagramSocket();

				// String will contain argumentss to pass to server to determine which
				// operation to run
				char serverMethod = '1';// represents getNonReturners method
				String days = String.valueOf(14);
														

				String serverInvArgs = serverMethod + delim
						+ days + delim;
				System.out.println(serverInvArgs);
				byte[] reqMsg = serverInvArgs.getBytes();// will receive the data
				InetAddress aHost = InetAddress.getByName("localhost");
				// Print the results
				if (getOverdueAccounts() != null) {
					localAccountsOverdue.addAll(getOverdueAccounts());	
					System.out.println("\n" + this.institution + " nonreturners: " + localAccountsOverdue.toString());
				}
				
				
					remoteServerPorts[0] = 4441;
					remoteServerPorts[1] = 4442;
					remoteServerPorts[2] = 4443;
				
				
				// Call the remote servers
				for (int j = 0; j < remoteServerPorts.length; j++) {
					
					if (this.udpPort == remoteServerPorts[j]) {
						accountsOverdue.addAll(localAccountsOverdue);
						continue;
					}
					DatagramPacket request = new DatagramPacket(reqMsg,
							serverInvArgs.length(), aHost, remoteServerPorts[j]);
					aSocket.send(request);
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
					
					aSocket.receive(reply);

					rep = new String(reply.getData());
					
					System.out.println("\nremote insitution " + (j+1) + ": \n");
					
					// created temp String variable, because using rep directly in accountsOverdue Method broke SOAP
					String temp = rep.replaceAll("\u0000-*", "");
					
					System.out.println(temp);
					
					accountsOverdue.add(temp);
				}
				
			} catch (SocketException e) {
				System.out.println("Socket: " + e.getMessage());
			} catch (IOException e) {
				System.out.println("IO: " + e.getMessage());
			} finally {
				if (aSocket != null)
					aSocket.close();
			}
		try {  
			return localAccountsOverdue.toString() + "\n" + accountsOverdue.toString();
		} catch (Exception e) {
				return localAccountsOverdue.toString() + "\n" + accountsOverdue.toString();
		}	
	}

	public String setDuration(String username, String bookTitle, int numDays) {
		
		StudentAccount s = getStudentAccount("natemacinnes"); 
		
		if (s != null) {
			if (s.setDuration(bookTitle, numDays)) {
				return "Operation setDuration succeed in " + institution + "library";
			} else {
				return "Operation setDuration failed: loan doesn't exist";
			}
		}
		return "Operation setDuration failed: username is wrong";
	}
	
	// Authenticates the student user and returns the user object
	private StudentAccount authenticate(String username, String password) {
		int i = 0;	
		if(accounts.get(username.charAt(0)) != null)
			for(StudentAccount student : accounts.get(username.charAt(0))) {
				if (student.isUsername(username) && student.isPassword(password)) {
					return student;
				}
			} else {
				return null;
			}
		return null;
	}
	
	// Borrow book helper method
	private String borrowBook(StudentAccount student, String bookTitle, String authorName) {
		switch (catalog.borrow(bookTitle)) {
		case 0:
			student.addLoan(new Loan(catalog.getBook(bookTitle)));
			try {
			io.write(catalog.bookToString(bookTitle) + " borrowed by " + student.getUserName() 
					+ " Instances remaining: " + catalog.getBook(bookTitle).getInstances());
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unable to log actions");
			}
			return "Operation reserveBook succeed in "+ institution + " library";
		case 1:
			return "Operation reserveBook failed: No more copies available";
		default:
			return "Operation reserveBook failed: Book doesn't exists";
		}
	}
	
	private void createAccount(StudentAccount s) {
		ArrayList<StudentAccount> sa = new ArrayList<StudentAccount>();
		sa.add(s);
		accounts.put(s.getUserName().toLowerCase().charAt(0), sa);
	}
	
	private void fillLibraryTestData() {
		StudentAccount[] students = new StudentAccount[3]; 
		Loan[] loans = new Loan[3];
		Book[] books = new Book[3];
		
		createAccount("Nathan", "MacInnes", "natemacinnes@gmail.com", "5555555555", "natemacinnes", "phil13", this.institution);
		createAccount("Abe", "Lincoln", "example@example.com", "5555555555", "alincoln", "phil13", this.institution);
		createAccount("George", "Washington", "example@example.com", "5555555555", "gwashington", "phil13", this.institution);
		
		students[0] = getStudentAccount("natemacinnes");
		students[1] = getStudentAccount("alincoln");
		students[2] = getStudentAccount("gwashington");
		
		books[0] = new Book("game of thrones 1", "GRR Martin", 1);
		books[1] = new Book("game of thrones 2", "GRR Martin", 2);
		books[2] = new Book("game of thrones 3", "GRR Martin", 3);
		
		loans[0] = new Loan(books[0]);
		loans[1] = new Loan(books[1]);
		loans[2] = new Loan(books[2]);
		
		students[0].addLoan(loans[0]);
		students[1].addLoan(loans[1]);
		students[2].addLoan(loans[2]);
		
		students[0].getLoans().get(0).setDuration(14);
		students[1].getLoans().get(0).setDuration(14);
		students[2].getLoans().get(0).setDuration(14);
		
		students[0].setFines(1);
		students[1].setFines(1);
		students[2].setFines(1);
		
		fillBooksCatalog(true);
		
	}
	
	private void fillBooksCatalog(boolean debug) {
		if (debug) {
			for (int i = 0; i < 10; i++) {
				if (this.institution.equals("mcgill")) {
					this.catalog.addBook("Book" + i, "Author" + i);
				}
			}
		}
	}	
	
	private ArrayList<String> getOverdueAccounts () {
		ArrayList<String> accountsOverdue = new ArrayList<String>();
		
		accountsOverdue.add(this.institution + " university:\n");
		
		for (Entry<Character, ArrayList<StudentAccount>> entry : accounts.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); i++) {
				if (entry.getValue().get(i).getFines() > 0)
					accountsOverdue.add(entry.getValue().get(i).getFirstName() +" "+ entry.getValue().get(i).getLastName() + " " +
							entry.getValue().get(i).getPhoneNumber() + "\n");
			}
		}
		return accountsOverdue;
	}
	
	private String getAccount(int i,ArrayList<String> overdueAccounts) {
		if (!overdueAccounts.isEmpty() && overdueAccounts.get(i) != null) {
			return overdueAccounts.get(i);
		}
		return "END";
	}
	
	private StudentAccount getStudentAccount(String username) {
		char initial = username.charAt(0);
		for (int i = 0; i < accounts.get(initial).size(); i++) {
			if (accounts.get(initial).get(i).isUsername(username)) {
				return accounts.get(initial).get(i);
			}
		}
		return null;
	}
	
	public synchronized String udpServer() {

		Thread t = new Thread(new Runnable() {
			// Server code
			@Override
			public void run() {
				DatagramSocket aSocket = null;

				try {
					aSocket = new DatagramSocket(udpPort);
					
					// create socket at agreed port
					byte[] buffer = new byte[1000];
					byte[] sendData = null;

					while (true) {
						DatagramPacket request = new DatagramPacket(buffer,
								buffer.length);
						
						// get request
						aSocket.receive(request);

						String requestString = new String(request.getData());
						requestString.replaceAll("\u0000-*", "");

						// Server returns the late students account information.
						if (requestString.charAt(0) == '1')// select getNonReturners method to call
						{
							//String split = requestString.substring(2, 4);
							sendData = getOverdueAccounts().toString().getBytes();
						
						} 
						// '0' represents the reaserveInterLibrary method
						else if(requestString.charAt(0) == '0')
						{
							String[] reqStrings = requestString.split("\\-");

							String bookName = reqStrings[1];
							String authorName = reqStrings[2];
							String userName = reqStrings[3];
							String password = reqStrings[4];
							
							String retResult = intitutionReserve(userName, password, bookName, authorName, "McGill");
						
							sendData = retResult.getBytes();
						}
						
						else {
							System.out.println("substring wrong");
						}

						DatagramPacket reply = new DatagramPacket(sendData,
								sendData.length, request.getAddress(),
								request.getPort());
						// Send the reply
						aSocket.send(reply);
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
		});
		
		t.start();
		
		System.out.println("UDP Server with port " 
		+ udpPort + " for " +  this.institution + " is up");
		
		return "UDP Server with port " 
		+ udpPort + " for " +  this.institution + " is up";
	}
}
		

 //end class

