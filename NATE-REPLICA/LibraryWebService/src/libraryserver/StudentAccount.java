package libraryserver;


import java.util.ArrayList;

public class StudentAccount{
	private String firstName, lastName, emailAddress, phoneNumber, username, password, edInstitution;
	private double fines;
	private ArrayList<Loan> loans = new ArrayList<Loan>();
	//To do add borrowed books to concordia library server. finish the borrowbook method
	
	public StudentAccount(String firstName, String lastName, String emailAddress, String phoneNumber, String username, String password, String ed) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.username = username;
		this.password = password;
		this.edInstitution = ed;
	}
	
	public ArrayList<Loan> getLoans() {
		return loans;
	}

	public void setLoans(ArrayList<Loan> loans) {
		this.loans = loans;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserName() {
		return username;
	}
	public void setUserName(String username) {
		this.username = username;
	}
	public boolean isPassword(String password) {
		return 0 == this.password.compareTo(password);
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEdInstitution() {
		return edInstitution;
	}
	public void setEdInstitution(String edInstitution) {
		this.edInstitution = edInstitution;
	}
	public double getFines() {
		return fines;
	}
	public void setFines(double fines) {
		this.fines = fines;
	}
	
	public void setDuration(String book) {
		for (int i = 0; i < loans.size(); i ++ ) {
			if (loans.get(i).getBook().getTitle().equalsIgnoreCase(book)) {
				loans.get(i).setDuration(-1);
				this.fines += 1;
			}
		}
	}
	
	public boolean setDuration(String book, int numDays) {
		for (int i = 0; i < loans.size(); i ++ ) {
			if (loans.get(i).getBook().getTitle().equalsIgnoreCase(book)) {
				loans.get(i).setDuration(numDays);
				return true;
			}
		}
		return false;
	}
	
	public void addLoan(Loan loan) {
		this.loans.add(loan);
	}
	
	public String toString() {
		return "username: " + this.username + ", " + this.edInstitution;
	}
	
	public boolean isUsername(String username) {
		return 0 == username.compareToIgnoreCase(this.username);
	}
}
