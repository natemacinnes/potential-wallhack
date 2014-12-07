package serverLayer;

import java.util.Collection;

import serverException.*;
import serverInterfaceLayer.LibraryServerInterface;
import dataLayer.AccountsTableGateway;
import dataLayer.BooksTableGateway;
import modelLayer.Account;
import modelLayer.Accounts;
import modelLayer.Book;
import modelLayer.BookReservation;
import transportLayer.*;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import javax.jws.*;

@WebService(endpointInterface="serverLayer.LibraryServerInterface",
            serviceName="libraryServerService",
            portName="libraryServerPort",
            targetNamespace="http://serverlayer")
public class LibraryServerImpl implements LibraryServerInterface, InterServerOperation {
	
	private AccountsTableGateway accountsTable;
	private BooksTableGateway booksTable;
	private UDPServer server;
	private UDPClient client;
	private String institution;
	private ServerInformation serverConfig;
	private Logger LOGGER;
	
	public LibraryServerImpl(){}
	
	public LibraryServerImpl(String institution){
		accountsTable = new AccountsTableGateway(institution);
		booksTable = new BooksTableGateway();
		serverConfig = new ServerInformation();
		client = new UDPClient();
		this.institution = institution;
		//set up logger
		LOGGER = Logger.getLogger(LibraryServer.class.getName());
		setupLogger();
		//set up udp server
		server = new UDPServer(this, serverConfig.getServerPort(institution));
		server.start();
	}
	
	@Override
	public String reserveInterLibrary(String username, String password, String bookTitle, String authorName){
		
		String result = reserveBook(username, password, this.institution, bookTitle, authorName);
		
		if(result.contains("succeed"))
		{
			result = "Operation reserveInterLibrary succeed in "+ institution + " library";
			return result;
		}
		else if(result.contains("username or password is wrong"))
		{
			return result;
		}
		else
		{
			result =  reserveBookRemotely(username,password, bookTitle, authorName);
			return result;
		}
	}
	
	private String reserveBookRemotely(String username, String password, String bookTitle, String authorName)
	{
		String result;
		//get the ports of the udp servers of all the other servers
		Collection<String> serverNameList = serverConfig.getServersNames();
		//get our own udp server port
		int serverPort = serverConfig.getServerPort(this.institution);
		boolean isReservationDone;
		
		//iterate through all the available ports
		for(String name: serverNameList){
			//only send a request to remote server, not to ourself
			if(name != institution){
				isReservationDone = client.reserveBookRemotely(serverConfig.getServerPort(name),bookTitle, authorName);
				// if the reservation was sucessfully done remotely, add the book to the local account
				if(isReservationDone)
				{
					Account account = accountsTable.getAccount(username, password);
					Book book = booksTable.getBook(bookTitle, authorName);
					BookReservation reservation = new BookReservation(book);
					account.addReservation(reservation);
					result = "Operation reserveInterLibrary succeed: Book reserve at " + name;
					return result;
				}
			}
		}
		
		result = "Operation reserveInterLibrary failed: No more copies available or book not found";
		return result;
	}
	
	@Override
	public boolean ISOReserveBook(String bookTitle, String authorName) 
	{
		LOGGER.entering(this.getClass().getName(), "ISOReserveBook", new Object[]{new String(bookTitle), new String(authorName)});
		
		try
		{
			Book book = fetchBook(bookTitle, authorName);
			
			//coordinate book reservation among students
			synchronized(book){
				if(book.getCopiesAvailable() == 0){
					LOGGER.exiting(this.getClass().getName(), "ISOReserveBook", new Boolean(false));
					return false;
				}
				book.setCopiesAvailable(book.getCopiesAvailable() - 1);
				LOGGER.exiting(this.getClass().getName(), "ISOReserveBook", new Boolean(true));
				return true;
			}
		}
		catch(ItemNotFoundException e)
		{
			LOGGER.exiting(this.getClass().getName(), "ISOReserveBook", new Boolean(false));
			return false;
		}
	}
	
	@Override
	public String setDuration(String username, String bookTitle, int numDays){
		LOGGER.entering(this.getClass().getName(), "setDuration", 
				new Object[]{new String(username), new String(bookTitle), new Integer(numDays)});
		
		String result;
		Account account = accountsTable.getAccount(username);
		if(account == null){
			result = "Operation setDuration failed: Username is wrong";
			LOGGER.exiting(this.getClass().getName(), "setDuration", new String(result));
			return result;
		}
		account.changeReservation(bookTitle, numDays);
		
		result = "Operation setDuration succeed in " + institution + " library";
		LOGGER.exiting(this.getClass().getName(), "setDuration", new String(result));
		return result;
	}

	
	@Override
	public String getNonReturners(String adminUsername, String adminPassword, int numDays){
		
		String result = "";
		LOGGER.entering(this.getClass().getName(), "getNonReturners", 
				new Object[]{new String(adminUsername), new String(adminPassword), new Integer(numDays)});
		
		//validate admin credentials
		if(!adminUsername.equals("Admin") || !adminPassword.equals("Admin")){
			result = "Operation getNonReturners failed: Invalid credentials";
			LOGGER.exiting(this.getClass().getName(), "getNonReturners", new String(result));
			return result;
		}
		
		//get the ports of the udp servers of all the other servers
		Collection<Integer> portList = serverConfig.getServersPorts();
		//get our own udp server port
		int serverPort = serverConfig.getServerPort(this.institution);
		String remoteServerResult;
		
		//iterate through all the available ports
		for(int port: portList){
			//only send a request to remote server, not to ourself
			if(port != serverPort){
				remoteServerResult = client.fetchNonReturners(port,numDays);
				result += remoteServerResult + "\n";
			}
			else
			{
				result += ISOGetNonReturners(numDays) + "\n";
			}
		}
		
		LOGGER.exiting(this.getClass().getName(), "getNonReturners", new String(result));
		return result;
	}
	
	//method that get all the non returners students for that particular server
	public String ISOGetNonReturners(int numDays){
		LOGGER.entering(this.getClass().getName(), "ISOGetNonReturners", new Integer(numDays));
		
		String result = institution + " university:\n";
		
		Collection<Accounts> accountsList =  accountsTable.getAllAccounts();
		//iterate through all accounts (which contains a subset of the total amount of account)
		for(Accounts accounts : accountsList){
			//iterate through all account
			for(Account account : accounts.getAllAccount()){
				if(account.hasLateBook(numDays)){
					result += account.getFirstName() + " " + account.getLastName() + " " + account.getPhoneNumber() + "\n";
				}
			}
		}
		
		LOGGER.exiting(this.getClass().getName(), "ISOGetNonReturners", new String(result));
		return result;
	}
	
	@Override 
	public String createAccount(String firstName, String lastName, String email, String phoneNumber,
			String username, String password, String institution) {
		
		String result;
		
		LOGGER.entering(this.getClass().getName(), "createAccount", 
				new Object[]{new String(firstName), new String(lastName), new String(email),
			new String(phoneNumber), new String(username), new String(password), new String(institution)});
		
		boolean accountInfoValid = areAccountInfoValid(firstName,lastName,email,phoneNumber,username,password, institution);
		if(!accountInfoValid){
			result = "Operation createAccount failed: One of the arguments is not valid";
			LOGGER.exiting(this.getClass().getName(), "createAccount", new String(result));
			return result;
		}
		Account acc = new Account(firstName,lastName,email,phoneNumber,username,password, institution);
		try{
			accountsTable.addAccount(acc);
		}catch(TableUniqueIdException e){
			result = "Operation createAccount failed: username already exists";
			LOGGER.exiting(this.getClass().getName(), "createAccount", new String(result));
			return result;
		}
		
		result = "Operation createAccount succeed in " + institution + " library";
		LOGGER.exiting(this.getClass().getName(), "createAccount", new String(result));
		return result;
	}
	
	@Override
	public String reserveBook(String username, String password, String institution, String bookTitle,
			String authorName){
		
		String result;
		LOGGER.entering(this.getClass().getName(), "reserveBook", 
				new Object[]{new String(username), new String(password), new String(institution),
			new String(bookTitle), new String(authorName)});
		
		try
		{
			Account account = validateAndGetAccount(username, password);
			Book book = fetchBook(bookTitle, authorName);
			
			//coordinate book reservation among students
			synchronized(book){
				if(book.getCopiesAvailable() == 0){
					result = "Operation reserveBook failed: No more copies available";
					LOGGER.exiting(this.getClass().getName(), "reserveBook", new String(result));
					return result;
				}
				
				BookReservation reservation = new BookReservation(book);
				account.addReservation(reservation);
				book.setCopiesAvailable(book.getCopiesAvailable() - 1);
				
				result = "Operation reserveBook succeed in " + institution + " library";
				LOGGER.exiting(this.getClass().getName(), "reserveBook");
				return result;
			}
		}
		catch(Exception e)
		{
			result = "Operation reserveBook failed: Book doesn' exists ";
			LOGGER.exiting(this.getClass().getName(), "reserveBook", new String(result));
			return result;
		}
	}
	
	private Account validateAndGetAccount(String username, String password) throws validateCredentialsException{
		
		LOGGER.entering(this.getClass().getName(), "validateAndGetAccount", 
				new Object[]{new String(username), new String(password)});
		
		Account account = accountsTable.getAccount(username, password);
		if(account == null){
			validateCredentialsException e = new validateCredentialsException("username or password is wrong. Try again");
			LOGGER.throwing(this.getClass().getName(), "validateAndGetAccount", e);
			throw e;
		}
		
		LOGGER.exiting(this.getClass().getName(), "validateAndGetAccount", account);
		return account;
	}
	
	private Book fetchBook(String bookTitle, String authorName) throws ItemNotFoundException{
		
		LOGGER.entering(this.getClass().getName(), "fetchBook", 
				new Object[]{new String(bookTitle), new String(authorName)});
		
		Book book = booksTable.getBook(bookTitle, authorName);
		if(book == null){
			ItemNotFoundException e = new ItemNotFoundException("The book was not found. Verify the book title and author name");
			LOGGER.throwing(this.getClass().getName(), "fetchBook", e);
			throw e;
		}
		
		LOGGER.exiting(this.getClass().getName(), "fetchBook", book);
		return book;
	}
	
	private boolean areAccountInfoValid(String firstName,String lastName,String email, String phoneNumber,
			String username,String password, String institution){
		
		LOGGER.entering(this.getClass().getName(), "areAccountInfoValid", 
				new Object[]{new String(firstName), new String(lastName), new String(email),
			new String(phoneNumber), new String(username), new String(password), new String(institution)});
		
		boolean areInfoValid = true;
		
		if(firstName == null || firstName.equals(""))
			areInfoValid = false;
		if(lastName == null || lastName.equals(""))
			areInfoValid = false;
		if(email == null || email.equals(""))
			areInfoValid = false;
		if(phoneNumber == null || phoneNumber.equals(""))
			areInfoValid = false;
		if(username == null || username.length() < 6 || username.length() > 15)
			areInfoValid = false;
		if(password == null || password.length() < 6)
			areInfoValid = false;
		if(email == null || email.equals(""))
			areInfoValid = false;
		
		
		LOGGER.exiting(this.getClass().getName(), "areAccountInfoValid", new Boolean(areInfoValid));
		return areInfoValid;
	}
	
	
	private void setupLogger(){
		try{
			FileHandler fileTxt = new FileHandler("ServerLog/" + this.institution + ".log");
			
		    // create a TXT formatter
		    SimpleFormatter formatterTxt = new SimpleFormatter();
		    fileTxt.setFormatter(formatterTxt);
		    LOGGER.addHandler(fileTxt);
		    
		    // suppress the logging output to the console
		    LOGGER.setUseParentHandlers(false);


		    LOGGER.setLevel(Level.ALL);
		    
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	

	
	

}

