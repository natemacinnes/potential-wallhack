package drms;

/**
 * Interface definition: operations.
 * 
 * @author OpenORB Compiler
 */
public interface operationsOperations
{
    /**
     * Operation createAccount
     */
    public void createAccount(String firstName, String lastName, String emailAddress, String phoneNumber, String userName, String password, String institution);

    /**
     * Operation reserveBook
     */
    public void reserveBook(String userName, String password, String bookName, String bookAuthor, String institution);

    /**
     * Operation getNonReturners
     */
    public void getNonReturners(String AdminUsername, String adminPassword, String educationalInstitution, int numdays);

    /**
     * Operation reserveInterLibrary
     */
    public void reserveInterLibrary(String Username, String Password, String BookName, String AuthorName);

}
