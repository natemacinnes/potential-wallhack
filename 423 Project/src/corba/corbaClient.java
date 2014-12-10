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
		String choice = null;

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
			choice = sc.nextLine();
			

			if (choice.equals("1")) {

				String firstname;
				String lastname;
				String email;
				String phonenumber;
				String username;
				String password;
				String institution;

				System.out.println("Enter your first name: ");
				firstname = sc.nextLine();

				System.out.println("Enter your lastname: ");
				lastname = sc.nextLine();

				System.out.println("Enter your email: ");
				email = sc.nextLine();

				System.out.println("Enter your phonenumber: ");
				phonenumber = sc.nextLine();

				System.out.println("Enter your username: ");
				username = sc.nextLine();

				System.out.println("Enter your password: ");
				password = sc.nextLine();

				System.out.println("Enter your institution: ");
				institution = sc.nextLine();

				System.out.println("you inputted: " + firstname + " "
						+ lastname + " " + email + " " + phonenumber + " "
						+ username + " " + password + " " + institution);

				String testReply = frontEnd.createAccount(firstname, lastname,
						email, phonenumber, username,
						password, institution);

				System.out.println("Result: " + testReply);

			} 
			else if (choice.equals("2")) {

				String bookname;
				String author;
				String password;
				String username;
				String institution;

				System.out.println("Enter your username: ");
				username = sc.nextLine();

				System.out.println("Enter your password: ");
				password = sc.nextLine();

				System.out.println("Enter your book's name: ");
				bookname = sc.nextLine();

				System.out.println("Enter your book's author: ");
				author = sc.nextLine();

				System.out.println("Enter your institution: ");
				institution = sc.nextLine();

				System.out.println("You inputted: " + username + " " + password
						+ " " + bookname + " " + author + " " + institution);
				
				String reply = frontEnd.reserveBook(username, password,
						bookname, author, institution);
				System.out.println("Result: " + reply);

			} 
			else if (choice.equals("4")) {

				String adminUser;
				String adminPass;
				String institution;
				String numDays;

				System.out.println("Enter your admin username: ");
				adminUser = sc.nextLine();

				System.out.println("Enter your admin password: ");
				adminPass = sc.nextLine();

				System.out.println("Enter your institution: ");
				institution = sc.nextLine();

				System.out.println("Enter the number of days: ");
				numDays = sc.nextLine();// Convert to int for params

				System.out.println("You inputted: " + " " + adminUser + " "
						+ adminPass + " " + institution + " " + numDays);
				
				String reply = frontEnd.getNonReturners(adminUser, adminPass, institution, Integer.parseInt(numDays));
				System.out.println("Result: " + reply);
			}else if (choice.equals("5")) {

				String username;
				String password;
				String institution;
				String bookname;
				String author;

				System.out.println("Enter your username: ");
				username = sc.nextLine();

				System.out.println("Enter your password: ");
				password = sc.nextLine();

				System.out.println("Enter your bookname: ");
				bookname = sc.nextLine();

				System.out.println("Enter your author name: ");
				author = sc.nextLine();

				System.out.println("Enter your institution: ");
				institution = sc.nextLine();

				System.out.println("You inputted : " + username + " "
						+ password + " " + bookname + " " + author + " "
						+ institution);
				
				String reply = frontEnd.reserveInterLibrary(username, password, bookname, author, institution);
				System.out.println("Result: " + reply);
				
			} else if (choice.equals("3")) {
				String username;
				String institution;
				String bookname;
				String numDays;

				System.out.println("Enter your username: ");
				username = sc.nextLine();

				System.out.println("Enter your bookname: ");
				bookname = sc.nextLine();

				System.out.println("Enter the num days: ");
				numDays = sc.nextLine();

				System.out.println("Enter your institution: ");
				institution = sc.nextLine();
				
				String reply = frontEnd.setDuration( username, bookname
						, Integer.parseInt(numDays) , institution);
				System.out.println("Result: " + reply);

				System.out.println("You inputted: " + username + " " + bookname
						+ " " + numDays + " " + institution);
				
			} else if (choice.equals("6")) {
				System.exit(0);
			}
			sc.nextLine();

			System.out.println();

		}
	}

}
