package libraryserver;


/**
 * Book class that represents a book
 * @author Nathan MacInnes
 */

public class Book {
	private String title;
	private String authorName;
	private int instance = 0;
	
	public Book() {
		instance = 1;
	}
	
	public Book(String title, String authorName){
		this.title = title;
		this.authorName = authorName;
		instance ++;
	}
	
	public Book(String title, String authorName, int instances){
		this.title = title;
		this.authorName = authorName;
		this.instance = instances;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	public void addInstance () {
		instance++;
	}
	
	public int getInstances() {
		// TODO Auto-generated method stub
		return instance;
	}
	
	public void reduceInstance() {
		instance--;
	}
	
	public String toString() {
		return title + ", " + authorName;
	}
}
