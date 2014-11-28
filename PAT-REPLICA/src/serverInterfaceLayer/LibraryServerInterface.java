package serverInterfaceLayer;

public interface LibraryServerInterface {
	
	public String createAccount(String firstName, String lastName, String email, String phoneNumber,
			String username, String password, String institution);
	
	public String reserveBook(String username, String password, String institution, String bookTitle,
			String authorName);

	public String getNonReturners(String adminUsername, String adminPassword, int numDays);
	
	public String setDuration(String username, String bookTitle, int numDays);
	
	public String reserveInterLibrary(String username, String password, String bookTitle, String authorName);

}
