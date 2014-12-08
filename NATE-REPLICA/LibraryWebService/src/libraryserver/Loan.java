package libraryserver;


/**
 * Loan class that relates books to students in terms of a loan
 *
 * @author Nathan MacInnes
 */

public class Loan {
	private int duration;
	private Book book;
	
	public Loan () {
		this.setBook(null);
	}
	
	public Loan (Book book) {
		this.setDuration(14);
		this.setBook(book);
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
}
