package libraryserver;


/**
 * Catalog of books that tracks the books that are in a library.
 * Keeps track of book instances.
 *
 * @author Nathan MacInnes
 */

import java.util.Hashtable;

class BookCatalog {
	private Hashtable<String, Book> catalog = new Hashtable<String, Book>();
	
	BookCatalog() {
		
	}
	
	void addBook(String bookTitle, String authorName) {
		if (this.catalog.get(bookTitle)!=null) {
			this.catalog.get(bookTitle).addInstance();
		} else {
			this.catalog.put(bookTitle, new Book(bookTitle, authorName));
		}
	}
	
	int borrow(String bookTitle) {
		if (this.catalog.get(bookTitle)!=null) {
			System.out.println("Book borrow contains book.");
			if (this.catalog.get(bookTitle).getInstances() > 0) {
				System.out.println("Book borrow updating instance.");
				this.catalog.get(bookTitle).reduceInstance();
				return 0;
			} else {
				return 1;
			} 
		} 
	return 2;
	}
	
	String bookToString(String bookTitle) {
		return this.catalog.get(bookTitle).toString();
	}
	
	Book getBook(String bookTitle) {
		return catalog.get(bookTitle);
	}
	 
	boolean available(String bookTitle) {
		
		if (catalog.contains(bookTitle) && catalog.get(bookTitle).getInstances()>0) {
			return true;
		}
		
		return false;
		
	}
	
}
