package corba;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.omg.CORBA.ORB;

public class corbaClient {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// Client Orb for calling frontend

		// initialize client ORB
		ORB orb = ORB.init(args, null);

		// fetch the IOR
		BufferedReader br = new BufferedReader(new FileReader("ior.txt"));
		String IOR = br.readLine();
		br.close();

		// Transform IOR into a CORBA object
		org.omg.CORBA.Object orbObject = orb.string_to_object(IOR);

		corbaOperations frontEnd = corbaOperationsHelper.narrow(orbObject);

		String testReply = frontEnd.createAccount("Jignesh", "Patel", "jigneshp23@gmail.com", "514-652-4729", "jig_pa", "password1", "Concordia");

		// String testReply = frontEnd.reserveBook("j", "pa", "book", "ge",
		// "test");
		System.out.println(testReply);
	}

}
