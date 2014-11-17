package userInfo;


import java.util.ArrayList;
import java.util.HashMap;

public class StudentAccount {
	
	public String firstName;
	public String lastName;
	public String emailAddress;
	public String phoneNumber;
	public String userName;
	public String password;
	public String institution;
	public int finesAccumulated;
	public static HashMap<String, ArrayList<BookList>> studentBookList;


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
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public int getFinesAccumulated() {
		return finesAccumulated;
	}

	public void setFinesAccumulated(int finesAccumulated) {
		this.finesAccumulated = finesAccumulated;
	}

	public HashMap<String, ArrayList<BookList>> getStudentBookList() {
		return studentBookList;
	}

	public void setStudentBookList(HashMap<String, ArrayList<BookList>> studentBookList) {
		StudentAccount.studentBookList = studentBookList;
	}


	public StudentAccount(String firstName, String lastName,
			String emailAddress, String phoneNumber, String userName,
			String password, String institution) {
		super();
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.userName = userName;
		this.password = password;
		this.institution = institution;
		
		StudentAccount.studentBookList = new HashMap<String,ArrayList<BookList>>();
	}

}
