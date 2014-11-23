package dataLayer;

import java.util.HashMap;

import modelLayer.Book;


public class BooksTableGateway {
	private HashMap<String,Book> bookList;
	
	public BooksTableGateway(){
		bookList = new HashMap<String,Book>();
		createInitialBooks();
	}
	
	public void addBook(Book book){
		//if the book already exist, just add the new copies to the existing amount of copies
		if(bookList.containsKey(book.getBookTitle() + book.getAuthor())){
			Book b = bookList.get(book.getBookTitle() + book.getAuthor());
			b.setCopiesAvailable(b.getCopiesAvailable() + book.getCopiesAvailable());
			bookList.put(b.getBookTitle() + b.getAuthor(), b);
		}else{
			bookList.put(book.getBookTitle() + book.getAuthor(),book);
		}
	}
	
	//This function returns a book instance if the book is found and null otherwise
	public Book getBook(String bookTitle, String authorName){
		return bookList.get(bookTitle + authorName);
	}
	
	//only there for testing purposes
	private void createInitialBooks(){
		Book book1 = new Book("Introduction to parallel computing", "Gosvan", 3);
		Book book2 = new Book("Distributed System", "Kumar", 1);
		Book book3 = new Book("Database architecture", "Bipin", 2);
		
		addBook(book1);
		addBook(book2);
		addBook(book3);
	}
}
