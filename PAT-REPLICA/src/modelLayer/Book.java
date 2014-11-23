package modelLayer;

public class Book {
	private String bookTitle;
	private String author;
	private int copiesAvailable;
	
	public Book(String bookTitle, String author, int copiesAvailable){
		this.bookTitle = bookTitle;
		this.author = author;
		this.copiesAvailable = copiesAvailable;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getCopiesAvailable() {
		return copiesAvailable;
	}

	public void setCopiesAvailable(int copiesAvailable) {
		this.copiesAvailable = copiesAvailable;
	}
	
	@Override
	public int hashCode(){
		return bookTitle.hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		return bookTitle.equals(obj);
	}

}
