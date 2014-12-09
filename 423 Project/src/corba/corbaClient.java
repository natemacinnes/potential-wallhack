package corba;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

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

		Scanner sc = new Scanner(System.in);
		int choice = 0;

		while (true) {
			System.out.println("\n\nWelcome. Your choices are: ");
			System.out
					.println("Choice 1: Create 2 Concordia, McGill, and UQAM Students");
			System.out
					.println("Choice 2: Reserve a Local book at Concordia which works. Reserve the same nonavailable title, which doesn't work at McGill");
			System.out
					.println("Choice 3: setDuration for Concordia " +
							"\nChoice 4: getNonReturners for Concordia and McGill");
			System.out.println("Choice 5: Reserve Interlibrary at Concordia");
			choice = sc.nextInt();

			if (choice == 1) {
				String testReply = frontEnd.createAccount("Jignesh", "Patel",
						"jigneshp23@gmail;com", "514-652-4729", "jig_pa",
						"password1", "concordia");

				System.out.println(testReply);

			} else if (choice == 2) {
				String reply = frontEnd.reserveBook("jig_pa", "password1",
						"game of thrones 1", "GRR Martin", "concordia");
				System.out.println(reply);

			} else if (choice == 3) {
				
				String reply = frontEnd.setDuration( "jig_pa", "game of thrones 1", 14, "concordia");
				System.out.println(reply);
			} else if (choice ==4) {

				String reply = frontEnd.getNonReturners("Admin", "Admin", "concordia", 14);
				System.out.println(reply);
				
			} else if (choice == 5) {

				String reply = frontEnd.reserveInterLibrary("jig_pa",
						"password1", "game of thrones 1", "GRR Martin",
						"concordia");
				System.out.println(reply);
			}
			
			
			/*
			 * 
			 * CreateAccount
String firstname;
System.out.print("Enter your first name: ");
firstname = user_input.next( );

String lastname;
System.out.print("Enter your lastname: ");
lastname = user_input.next( );

String email;
System.out.print("Enter your email: ");
email = user_input.next( );

String phonenumber;
System.out.print("Enter your phonenumber: ");
phonenumber = user_input.next( );

String username;
System.out.print("Enter your username: ");
username = user_input.next( );

String password;
System.out.print("Enter your password: ");
password = user_input.next( );

String institution;
System.out.print("Enter your institution: ");
institution = user_input.next( );

			 * */
			
			System.out.println();

		}
	}

}
