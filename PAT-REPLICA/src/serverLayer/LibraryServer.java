package serverLayer;

import javax.xml.ws.Endpoint;


public class LibraryServer  {
	
	public static void main(String args[]){
		if(args.length > 0){
			try
			{
				LibraryServerImpl server = new LibraryServerImpl(args[0]);
				
				//have a couple of books already reserved
				server.reserveBook("clouds", "clouds", args[0], "Introduction to parallel computing", "Gosvan");
				server.reserveBook("tifalo", "tifalo", args[0], "Introduction to parallel computing", "Gosvan");
				server.reserveBook("vincent", "vincent", args[0], "Introduction to parallel computing", "Gosvan");
				server.reserveBook("barret", "barret", args[0], "Database architecture", "Bipin");
				server.reserveBook("aerisg", "aerisg", args[0], "Database architecture", "Bipin");
				
				//Published the server endpoint
				Endpoint endPoint = Endpoint.publish("http://localhost:7777/" + args[0], new LibraryServerImpl(args[0]));
				System.out.println("Endpoint for " + args[0] + " library server is published: " + endPoint.isPublished());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			

		}	
	}
}
