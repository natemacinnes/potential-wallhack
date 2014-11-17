package userInfo;

public class Book {


	public String bookName;
	public String authorName;
	public int numberOfCopies;
	
	public int getNumberOfCopies() {
		return numberOfCopies;
	}

	public void setNumberOfCopies(int numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}

	public Book(String bookName, String authorName, int numberOfCopies) {
		super();
		this.bookName = bookName;
		this.authorName = authorName;
		this.numberOfCopies = numberOfCopies;
	}
	
	
}
