package modelLayer;

import java.util.ArrayList;
import java.util.HashMap;


public class Account {
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String username;
	private String password;
	private String institution;
	private double fines;
	//using an arrayList is not optimal but hen
	private ArrayList<BookReservation> reservationList;
	
	
	public Account(String firstName, String lastName, String email, String phoneNumber, String username,
			String password, String institution){
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.username = username;
		this.password = password;
		this.institution = institution;
		reservationList = new ArrayList<BookReservation>();
		fines = 0.0;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUsername() {
		return username;
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
	
	public void addReservation(BookReservation reservation){
		
		reservationList.add(reservation);
	}
	
	
	
	public void changeReservation(String bookTitle, int numDays){
		for(BookReservation reservation: reservationList){
			Book book = reservation.getBook();
			//change reservation duration
			if(book.getBookTitle().equals(bookTitle)){
				reservation.setDurationDays(numDays);
			}
		}
	}
	
	//check if the user has a book late for the amount of time specified
	public boolean hasLateBook(int numDays){
		for(BookReservation reservation: reservationList){
			//change reservation duration
			if(reservation.getDurationDays() == numDays){
				return true;
			}
		}
		return false;
	}

	public double getFines() {
		return fines;
	}
	
}
