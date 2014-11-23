package modelLayer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BookReservation {
	private Book book;
	private int durationDays;
	
	public BookReservation(Book book){
		this.book = book;
		//default duration is 14 days
		durationDays = 14;
	}
	
	public BookReservation(Book book, int durationDays){
		this.book = book;
		this.durationDays = durationDays;
	}

	public Book getBook() {
		return book;
	}

	public int getDurationDays() {
		return durationDays;
	}

	public void setDurationDays(int durationDays) {
		this.durationDays = durationDays;
	}

}
