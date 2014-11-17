package userInfo;


public class BookList {

	Book b;
	int numDays;
	
	public BookList(Book b, int numDays) {
		super();
		this.b = b;
		this.numDays = numDays;
	}
	public Book getB() {
		return b;
	}

	public void setB(Book b) {
		this.b = b;
	}

	public int getNumDays() {
		return numDays;
	}

	public void setNumDays(int numDays) {
		this.numDays = numDays;
	}



}

