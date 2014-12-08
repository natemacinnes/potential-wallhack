package libraryserver;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface LibraryServer
{
	
	@WebMethod
	public String createAccount(String firstName, String lastName,
		String emailAddress, String phoneNumber, String username,
		String password, String educationalInstitution);

	@WebMethod
	public String reserveBook(String username, String password,
		String bookName, String authorName);

	@WebMethod
	public String reserveInterLibrary(String username, String password,
		String book, String author);
	
	@WebMethod
	public String getNonreturners(String adminUsername, String adminPassword,
		String educationalInstitution, String numDays);
	
	@WebMethod
	public String intitutionReserve(String username, String password, String bookName,
			String authorName, String institution);
	
	@WebMethod
	public String udpServer();
}
