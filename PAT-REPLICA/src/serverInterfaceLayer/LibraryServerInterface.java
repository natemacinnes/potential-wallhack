package serverInterfaceLayer;

import serverException.ItemNotAvailableException;
import serverException.ItemNotFoundException;
import serverException.TableUniqueIdException;
import serverException.validateCredentialsException;

public interface LibraryServerInterface {
	
	public String createAccount(String firstName, String lastName, String email, String phoneNumber,
			String username, String password, String institution);
	
	public void reserveBook(String username, String password, String institution, String bookTitle,
			String authorName) throws validateCredentialsException, ItemNotFoundException,
			ItemNotAvailableException;

	public String getNonReturners(String adminUsername, String adminPassword, int numDays) 
			throws validateCredentialsException;
	
	public void setDuration(String username, String bookTitle, int numDays) 
			throws validateCredentialsException;
	
	public void reserveInterLibrary(String username, String password, String bookTitle, String authorName)
			throws validateCredentialsException, ItemNotFoundException,ItemNotAvailableException;

}
