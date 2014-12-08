package libraryserver;



public interface LibraryServer
{
	
	public String createAccount(String firstName, String lastName,
		String emailAddress, String phoneNumber, String username,
		String password, String educationalInstitution);

	public String reserveBook(String username, String password,
		String bookName, String authorName);

	public String reserveInterLibrary(String username, String password,
		String book, String author);

	public String getNonreturners(String adminUsername, String adminPassword,
		String educationalInstitution, String numDays);

	public String intitutionReserve(String username, String password, String bookName,
			String authorName, String institution);

	public String udpServer();
}
