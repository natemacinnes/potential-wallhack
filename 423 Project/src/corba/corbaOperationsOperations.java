package corba;

/**
 * Interface definition: corbaOperations.
 * 
 * @author OpenORB Compiler
 */
public interface corbaOperationsOperations
{
    /**
     * Operation createAccount
     */
    public String createAccount(String firstName, String lastName, String emailAddress, String phoneNumber, String userName, String password, String institution);

    /**
     * Operation reserveBook
     */
    public String reserveBook(String userName, String password, String bookName, String bookAuthor, String institution);

    /**
     * Operation getNonReturners
     */
    public String getNonReturners(String adminUsername, String adminPassword, String educationalInstitution, int numdays);

    /**
     * Operation reserveInterLibrary
     */
    public String reserveInterLibrary(String username, String password, String bookName, String authorName, String institution);

    /**
     * Operation setDuration
     */
    public String setDuration(String username, String bookname, int numDays, String institution);

}
