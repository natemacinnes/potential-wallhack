package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.omg.CORBA.ORB;

import udp.UDPServer;
import userInfo.Book;
import userInfo.BookList;
import userInfo.StudentAccount;

//FIXME add the proxy methods that call actual methods
//for createAccount, reserveBook, getNonReturners, reserveInterlibrary

public class LibraryServerFunctionsImpl {

	public UDPServer udpServer = null;
	public String UDPHost = null;
	public int UDPPort = 0;
	public String institution = null;
	public static HashMap<String, ArrayList<StudentAccount>> accTable = new HashMap<String, ArrayList<StudentAccount>>();
	private String serverMethod = null;
	private int serverMethodArgsIndex = 3;
	private String serverCommandDelim = ".";
	private String serverArgs = null;
	private int numRemoteServers = 2;
	public static String resBookResult = null;

	//
	public  HashMap<String, Book> bookMap = new HashMap<String, Book>();

	public LibraryServerFunctionsImpl(String uDPHost, int uDPPort,
			String institution, HashMap<String, Book> bookMap) {
		super();

		this.institution = institution;
		// UDP details
		this.UDPHost = uDPHost;
		this.UDPPort = uDPPort;
		// List of books for a given server
		this.bookMap = bookMap;

		// Start UDP server
		testUDPServer();

	}

	public synchronized String createAccount(String firstName, String lastName,
			String emailAddress, String phoneNumber, String userName,
			String password, String institution) {
		boolean accExists = false;
		String errorMsg = null;

		// FIXME add a check to see if the username exists already
		try {
			// Create StudentAccount object instance

			StudentAccount s = new StudentAccount(firstName, lastName,
					emailAddress, phoneNumber, userName, password, institution);
			// s.studentBookList.
			// Add to instance to HashMap's ArrayList

			ArrayList<StudentAccount> tempList = new ArrayList<StudentAccount>();

			// Add the StudentAccount to the HashMap if it's not already there
			if (accTable.get(userName.substring(0, 1)) == null) {
				tempList.add(s);
				accTable.put(s.userName.substring(0, 1), tempList);

				ArrayList<StudentAccount> listOfStudents = accTable
						.get(userName.substring(0, 1));

				for (StudentAccount studentEntry : listOfStudents) {

					if (studentEntry.userName.equals(userName)) {
						// FIXME do something about this.
						System.out.println("Student account created is "
								+ studentEntry.userName + "\n");

						test_printStudentAccountWithaKey(userName);
					}
				}
			} else {
				tempList = accTable.get(userName.substring(0, 1));

				// search if account exists
				for (StudentAccount studentEntry : tempList) {

					if (studentEntry.userName.equals(userName)) {
						// FIXME do something about this.
						System.out.println("Student account for " + userName
								+ " already created\n");
						errorMsg = "Operation createAccount failed: username already exists";
						accExists = true;
					}
				}

				if (accExists == false) {
					// add student account to hashmap
					tempList.add(s);
					accTable.put(userName.substring(0, 1), tempList);
					System.out.println("Student account created is "
							+ s.userName + "\n");

					test_printStudentAccountWithaKey(userName);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (accExists == true)
			return errorMsg;
		else
			return "Operation createAccount succeed in " + institution
					+ " library";
	}

	private void test_printStudentAccountWithaKey(String userName) {
		ArrayList<StudentAccount> tempList;
		tempList = accTable.get(userName.substring(0, 1));

		for (StudentAccount studentEntry : tempList) {

			// FIXME do something about this.
			System.out.println("Total Student account for "
					+ studentEntry.userName + " created");

		}
	}

	public synchronized String reserveBook(String userName, String password,
			String bookName, String bookAuthor, String institution) {

		System.out.println("Testing reserved book method " + userName + "\n");
		// Check if book exists
		Book desiredBook = null;

		StudentAccount s = null;

		// Get student account to update.
		s = getStudentAccount(userName, s);

		if (s == null) {
			System.out
					.println("Your student account is not found, " + userName);
		} else {

			desiredBook = searchForBookInServer(bookName, institution,
					desiredBook, s);

			// Book Reservation

			// If book is not available at library
			if (desiredBook == null) {
				bookNotAvailableMsg(bookName);
				resBookResult = "bookNull";
				// No more copies of book
			} else if (desiredBook.numberOfCopies == 0) {
				// Get number of copies available for book
				bookOutOfStockMessage(bookName);
				resBookResult = "bookzero";
				// Book available, reserve it
			} else {
				updateBookReserveInfo(userName, desiredBook);
				resBookResult = "bookreserved";

			}

		}

		if (resBookResult.equals("booknull")) {
			return "Operation reserveBook failed: Book doesn't exists";
		} else if (resBookResult.equals("bookzero")) {
			return "Operation reserveBook failed: no more copies available";

		} else if (resBookResult.equals("bookreserved")) {
			return "Operation reserveBook succeed in " + institution
					+ " libary";
		} else
			return "neither of the cases";

	}

	private Book searchForBookInServer(String bookName, String institution,
			Book desiredBook, StudentAccount s) {
		System.out.println("Found student account " + s.userName + "\n");

		if (institution.equals("concordia")) {
			desiredBook = bookMap.get(bookName);
		} else if (institution.equals("mcgill")) {
			desiredBook = bookMap.get(bookName);
		} else if ((institution.equals("uqam"))) {
			desiredBook = bookMap.get(bookMap);
		} else {
			System.out.println("The server doesn't exist!");
		}
		return desiredBook;
	}

	private void bookOutOfStockMessage(String bookName) {
		System.out.println("The book " + bookName
				+ " is currently out of stock at your library");
	}

	private void bookNotAvailableMsg(String bookName) {
		System.out.println("The book " + bookName
				+ " is not available at your library");
	}

	private StudentAccount getStudentAccount(String userName, StudentAccount s) {
		ArrayList<StudentAccount> listOfStudents = accTable.get(userName
				.substring(0, 1));

		// find the correct student
		for (StudentAccount studentEntry : listOfStudents) {

			if (studentEntry.userName.equals(userName)) {
				// FIXME do something about this.
				// System.out.println("Student account created is " +
				// studentEntry.userName);
				s = studentEntry;
			}
		}
		return s;
	}

	private void updateBookReserveInfo(String userName, Book desiredBook) {
		// Withdraw book from library
		int numCopies = desiredBook.numberOfCopies;

		// update book count in library
		desiredBook.setNumberOfCopies(numCopies - 1);

		// update student account's booklist
		BookList bl = new BookList(desiredBook, 14);

		// get student booklist
		ArrayList<BookList> studentBooks = new ArrayList<BookList>();

		if (StudentAccount.studentBookList.get(userName) == null) {

			StudentAccount.studentBookList.put(userName, studentBooks);
			studentBooks = StudentAccount.studentBookList.get(userName);
			studentBooks.add(bl);

			StudentAccount.studentBookList.put(userName, studentBooks);

		} else {
			studentBooks = StudentAccount.studentBookList.get(userName);
			studentBooks.add(bl);
			StudentAccount.studentBookList.put(userName, studentBooks);
		}

		System.out.println(userName + ", you have reserved the book "
				+ desiredBook.bookName + " for 14 days\n");
		// BookList b = StudentAccount.studentBookList.get(s.userName);
		System.out.println("Book count now " + desiredBook.numberOfCopies);
	}

	// FIXME rename the methods.
	public synchronized void testUDPServer() {

		Thread t = new Thread(new Runnable() {
			// Server code
			@Override
			public void run() {
				DatagramSocket aSocket = null;

				try {

					aSocket = new DatagramSocket(UDPPort);
					// create socket at agreed port
					byte[] buffer = new byte[1000];
					byte[] testSendData = null;

					while (true) {
						DatagramPacket request = new DatagramPacket(buffer,
								buffer.length);
						// get request
						aSocket.receive(request);

						String testRequest = new String(request.getData());
						System.out.println("\nTEST remote Request MSG: "
								+ testRequest.replaceAll("\u0000.*", ""));

						// Server returns the late students account information.
						if (testRequest.substring(0, serverMethodArgsIndex)
								.equals("gnr"))// select getNonReturners method
												// to call
						{

							String testSplit = testRequest.substring(
									serverMethodArgsIndex + 1,
									serverMethodArgsIndex + 2);


							System.out.println("Test Numdays " + testSplit
									+ "\n");

							testSendData = getAccountsStrings().getBytes();

						}
						// FIXME Add a case for the RIL method
						else if (testRequest
								.substring(0, serverMethodArgsIndex).equals(
										"ril")) {
							// FIXME add some text processing for getting the
							// book names
							String[] reqStrings = testRequest.split("\\.");

							String bookName = reqStrings[1];
							String authorName = reqStrings[2];
							String userName = reqStrings[3];
							String password = reqStrings[4];

							//FIXME add for uqam in correct order.
							String retResult = reserveBookNoLogin(userName,
									password, bookName, authorName);

							System.out
									.println("TEST REMOTE reserve Book string "
											+ retResult);
							testSendData = retResult.getBytes();
						}
						// FIXME add a call to 2 remote servers, return results.
						else {
							System.out.println("substring wrong");
						}

						// FIXME test with sending a string back.

						// Get the books from a given server.

						DatagramPacket reply = new DatagramPacket(testSendData,
								testSendData.length, request.getAddress(),
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

			private String reserveBookNoLogin(String userName, String password,
					String bookName, String bookAuthor) {

				Book desiredBook = null;
				if (institution.equals("concordia")) {
					desiredBook = bookMap.get(bookName);
				} else if (institution.equals("mcgill")) {

					desiredBook = bookMap.get(bookName);
				} else if ((institution.equals("uqam"))) {
					desiredBook = bookMap.get(bookName);
					System.out.println("DEBUG: CALL UQAM? INST TEST: " + LibraryServerFunctionsImpl.this.institution);

				} else {
					System.out.println("The server doesn't exist!");
				}

				// Book Reservation

				// If book is not available at library
				if (desiredBook == null) {
					bookNotAvailableMsg(bookName);
					return "bookNull";
					
					// No more copies of book
				} else if (desiredBook.numberOfCopies == 0) {
					// Get number of copies available for book
					// bookOutOfStockMessage(bookName);
					return "bookzero";
					
					// Book available, reserve it
				} else {

					int numCopies = desiredBook.numberOfCopies;

					// update book count in library
					desiredBook.setNumberOfCopies(numCopies - 1);

					System.out.println(userName
							+ ", you have reserved the book "
							+ desiredBook.bookName + " for 14 days\n");
					// BookList b =
					// StudentAccount.studentBookList.get(s.userName);
					System.out.println("Book count now "
							+ desiredBook.numberOfCopies);

					return "bookreserved" + serverCommandDelim;

				}

				// return institution;
			}
		});

		t.start();

		System.out.println("Test UDP Server" + "with port " + UDPPort + " for "
				+ institution + " is up");
	}

	public synchronized void getNonReturners(String AdminUsername,
			String adminPassword, String educationalInstitution, int numdays) {

		DatagramSocket aSocket = null;
		int serverPort = 0;// = this.UDPPort;
		byte[] buffer = new byte[1000];
		int[] remoteServerPorts = new int[numRemoteServers];

		try {
			// Create a datagram socket
			aSocket = new DatagramSocket();

			// String will contain args to pass to server to determine which
			// operation to run
			serverMethod = "gnr";// stands for getNonReturners
			serverArgs = String.valueOf(numdays);// arguments to server are the
													// number of days.

			String serverInvArgs = serverMethod + serverCommandDelim
					+ serverArgs + serverCommandDelim;

			byte[] reqMsg = serverInvArgs.getBytes();// will receive the data
			InetAddress aHost = InetAddress.getByName("localhost");

			// FIXME test calling one server from another
			if (this.UDPPort == 2020) {
				remoteServerPorts[0] = 2021;
				remoteServerPorts[1] = 2022;
			} else if (this.UDPPort == 2021) {
				remoteServerPorts[0] = 2020;
				remoteServerPorts[1] = 2022;
			} else if (this.UDPPort == 2022) {
				remoteServerPorts[0] = 2021;
				remoteServerPorts[1] = 2020;
			} else {
				System.out.println("no server exists");
			}

			// Bind to a port
			// FIXME multithread this.

			// Call the remote servers

			for (int j = 0; j < remoteServerPorts.length; j++) {
				DatagramPacket request = new DatagramPacket(reqMsg,
						serverInvArgs.length(), aHost, remoteServerPorts[j]);
				aSocket.send(request);

				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

				// get the reply
				aSocket.receive(reply);

				// add formatting to string
				String rep = new String(reply.getData());

				System.out.println("\nFOR THE REMOTE INSITUTION " + (j + 1)
						+ " for " + institution + ": \n");
				System.out.println(rep.replaceAll("\u0000.*", ""));

			}

			// Print the results
			System.out.println("\nLOCALNON RETURNERS " + getAccountsStrings());
			// System.out.println("Reply: " + new String(reply.getData()));

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}

	}

	protected synchronized String getAccountsStrings() {

		String accountString = this.institution + ": \n";

		ArrayList<StudentAccount> aListOfAccounts = accTable.get("a");

		StudentAccount s1 = aListOfAccounts.get(0);// simply gets one of the
													// StudentAccount instances
													// in the arraylist for demo
													// purposes
		StudentAccount s2 = aListOfAccounts.get(1);
		accountString = accountString + s1.firstName + " " + s1.lastName + " "
				+ s1.phoneNumber + "\n" + s2.firstName + " " + s2.lastName
				+ " " + s2.phoneNumber + "\n";

		return accountString;
	}

	public synchronized String reserveInterLibrary(String Username,
			String Password, String BookName, String AuthorName) {

		DatagramSocket aSocket = null;
		byte[] buffer = new byte[1000];
		int[] remoteServerPorts = new int[numRemoteServers];
		String remoteReservationInstitution = null; 
		// Call reserveBook method on local server
		reserveBook(Username, Password, BookName, AuthorName, institution);

		// Code to call servers, if needed.
		if (resBookResult.equalsIgnoreCase("booknull")
				|| resBookResult.equalsIgnoreCase("bookzero")) {
			try {
				// Create a datagram socket
				aSocket = new DatagramSocket();

				// String will contain args to pass to server to determine which
				// operation to run
				serverMethod = "ril";// stands for getNonReturners

				// ril.bookname.authorname.username.password

				String serverInvArgs = serverMethod + serverCommandDelim
						+ BookName + serverCommandDelim + AuthorName
						+ serverCommandDelim + Username + serverCommandDelim
						+ Password + serverCommandDelim;

				byte[] reqMsg = serverInvArgs.getBytes();// will receive the
															// data
				InetAddress aHost = InetAddress.getByName("localhost");

				// FIXME test calling one server from another
				if (this.UDPPort == 2022) {
					remoteServerPorts[0] = 2023;
					remoteServerPorts[1] = 2024;
				} else if (this.UDPPort == 2023) {
					remoteServerPorts[0] = 2022;
					remoteServerPorts[1] = 2024;
				} else if (this.UDPPort == 2024) {
					remoteServerPorts[0] = 2023;
					remoteServerPorts[1] = 2022;
				} else {
					System.out.println("no server exists");
				}


				// Call the remote servers if needed
				for (int j = 0; j < remoteServerPorts.length; j++) {
					DatagramPacket request = new DatagramPacket(reqMsg,
							serverInvArgs.length(), aHost, remoteServerPorts[j]);
					aSocket.send(request);

					DatagramPacket reply = new DatagramPacket(buffer,
							buffer.length);

					// get the reply
					aSocket.receive(reply);

					// add formatting to string
					String rep = new String(reply.getData());

					String response = rep.split("\\.")[0];

					// FIXME add proper string splitting
					if (response.equalsIgnoreCase("bookreserved")) {
						
						if (remoteServerPorts[j] == 2022) {
						remoteReservationInstitution = "concordia";
						} else if (remoteServerPorts[j] == 2023) {
							remoteReservationInstitution = "mcgill";
						} else if (remoteServerPorts[j] == 2024) {
							remoteReservationInstitution = "uqam";

						}

						resBookResult="bookreserved";
						
						// System.out.println()
						Book remoteBook = new Book(BookName, AuthorName, 1);
						// update student account's booklist
						BookList bl = new BookList(remoteBook, 14);

						// get student booklist
						ArrayList<BookList> studentBooks = new ArrayList<BookList>();

						if (StudentAccount.studentBookList.get(Username) == null) {

							// Add username if needed
							StudentAccount.studentBookList.put(Username,
									studentBooks);
							studentBooks = StudentAccount.studentBookList
									.get(Username);
							// add booklist
							studentBooks.add(bl);

							StudentAccount.studentBookList.put(Username,
									studentBooks);

						} else {
							studentBooks = StudentAccount.studentBookList
									.get(Username);
							studentBooks.add(bl);
							StudentAccount.studentBookList.put(Username,
									studentBooks);

							// FIXME test the account
							System.out.println("RESERVED BOOKS FOR: " + Username);
							
							for (int i = 0; i < studentBooks.size(); i++) {
								System.out.println("\t Book#" + (i + 1)
										+ " for " + Username + " "
										+ studentBooks.get(i).getB().bookName);
							}
						}
						System.out.println(Username
								+ ", you have REMOTELY reserved the book "
								+ remoteBook.bookName + " for 14 days\n");

						break;

					}
					System.out.println("\nFOR THE REMOTE INSITUTION " + j
							+ ": \n");
					System.out.println(rep);
					

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

		if (resBookResult.equals("booknull") || (resBookResult.equals("bookzero"))) {
			return "Operation reserveInterlibrary failed: No more copies available or book not found";
		} else if (resBookResult.equals("bookreserved")) {
			return "Operation reserveInterlibrary succeed: Book reserve at " + remoteReservationInstitution;
		} else
			return "neither of the cases";	}
}
