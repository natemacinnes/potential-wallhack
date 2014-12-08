/**
 * 	RUN THIS FIRST: 
 *  it sets up the webservice end points 
 *  for the three library services
 *  
 * 	Author: Nathan MacInnes 1957341
 * 	Date: November 18 2014
 */

package libraryserver;
import javax.xml.ws.Endpoint;


public class PublishWS {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Endpoint concordiaEndpoint = Endpoint.publish("http://localhost:7777/libraryserver", new LibraryServerImplBase("localhost", 4441, "concordia"));
		Endpoint mcgillEndpoint = Endpoint.publish("http://localhost:7778/libraryserver", new LibraryServerImplBase("localhost", 4442, "mcgill"));
		Endpoint queensEndpoint = Endpoint.publish("http://localhost:7779/libraryserver", new LibraryServerImplBase("localhost", 4443, "queens"));
		
		System.out.println(concordiaEndpoint.isPublished());
		System.out.println(mcgillEndpoint.isPublished());
		System.out.println(queensEndpoint.isPublished());
	}
}
