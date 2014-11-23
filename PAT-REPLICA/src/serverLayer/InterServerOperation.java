package serverLayer;

//interface that defines operation possible only between servers
public interface InterServerOperation {
	
	public String ISOGetNonReturners(int numDays);
	
	public boolean ISOReserveBook(String bookTitle, String authorName);

}
