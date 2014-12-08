/**
 *  RUN THIS AFTER RUNNING PublishWS
 * 	This is the client implementation for a student.
 * 	Allows the user to call the web service web methods.
 *  
 * 	Author: Nathan MacInnes 1957341
 * 	Date: November 18 2014
 */

package libraryclienttest;

import java.io.IOException;
import java.util.Scanner;

import libraryclient.LibraryClientServerProxy;

import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class TestClient {
	private static Integer i = 0;
	private static String message;
	public static libraryserver.ReadWriteTextFileWithEncoding io = 
			new libraryserver.ReadWriteTextFileWithEncoding("src/libraryclient/ClientLog"+ i.toString() + ".txt", "UTF8");

	
	
	public static void main(String[] args) throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		
		LibraryClientServerProxy.lookupServers();
		run();
	}	
	
	public static void run() {
		i++;
		int userChoice=0;
		String userInput="";
		Scanner keyboard = new Scanner(System.in);
		
		try {
			showMenu();
			while(true)
			{
				Boolean valid = false;
				String username = "";
				String password = "";
				String authorName = "";
				String book = "";
				String school = "";
				String firstName = "";
				String lastName = "";
				String phoneNumber = "";
				String email = "";
				// Enforces a valid integer input.
				while(!valid)
				{
					try{
						userChoice=keyboard.nextInt();
						valid=true;
					}
					catch(Exception e)
					{
						System.out.println("Invalid Input, please enter an Integer");
						valid=false;
						keyboard.nextLine();
					}
				}
				
				// Manage user selection.
				switch(userChoice)
				{
				case 1: 
					System.out.println("Follow steps to create a new account:");
					keyboard.nextLine();
					System.out.println("Please enter your first name:");
					firstName = keyboard.nextLine();
					System.out.println("Please enter your last name:");
					lastName = userInput=keyboard.nextLine();
					System.out.println("Please enter your email:");
					email=keyboard.nextLine();
					System.out.println("Please enter your phone number:");
					phoneNumber=keyboard.nextLine();
					System.out.println("Please enter your username:");
					username=keyboard.nextLine().toLowerCase();
					System.out.println("Please enter your password:");
					password=keyboard.nextLine();
					System.out.println("Please enter your educational institution:");
					school=keyboard.nextLine().toLowerCase();
					message = LibraryClientServerProxy.lookupServer(school).createAccount(firstName, lastName, email, phoneNumber, username, password, school);
					io.write(school + ": " + message);
					
					showMenu();
					break;
				case 2:
					System.out.println("Follow the steps to reserve a book from your school library:");
					keyboard.nextLine();
					System.out.println("Please enter your username:");
					username=keyboard.nextLine().toLowerCase();
					System.out.println("Please enter your password:");
					password=keyboard.nextLine();
					System.out.println("Please enter the book title:");
					book=keyboard.nextLine();
					System.out.println("Please enter the author name");
					authorName=keyboard.nextLine();
					System.out.println("Please enter your educational institution:");
					school=keyboard.nextLine().toLowerCase();
					io.write("User account information sent to " + school + " server");
					message = LibraryClientServerProxy.lookupServer(school).reserveBook(username, password, book, authorName);
					System.out.println();
					io.write(school + ": " + message);
					showMenu();
					break;
				case 3:
					System.out.println("Follow the steps to reserve a book on interlibrary loan:");
					keyboard.nextLine();
					System.out.println("Please enter your username:");
					username=keyboard.nextLine().toLowerCase();
					System.out.println("Please enter your password:");
					password=keyboard.nextLine();
					System.out.println("Please enter the book title:");
					book=keyboard.nextLine();
					System.out.println("Please enter the author name");
					authorName=keyboard.nextLine();
					io.write("User account information sent to " + school + " server");
					message = LibraryClientServerProxy.lookupServer("concordia").reserveInterLibrary(username, password, book, authorName);
					System.out.println();
					io.write(school + ": " + message);
					showMenu();
					break;
				case 4:
					System.out.println("Quiting library system.");
					System.out.println("Have a good day.");
					System.exit(0);
				default:
					System.out.println("Invalid Input, please try again.");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in Student client: " + e);
			e.printStackTrace();
		} finally {
			System.out.println("Exception Student Client: Unknown");
		}
	}
	
	public static void showMenu() {
		System.out.println("\n****Welcome to the Inter-University Library Server System****\n");
		System.out.println("Please select an option (1 or 2)");
		System.out.println("1. Create Account");
		System.out.println("2. Reserve Book");
		System.out.println("3. Interlibrary Book Reserve");
		System.out.println("4. Quit");
	}

	/*
	private static boolean checkUsername(String user) {
		return (user.length() > 6 || user.length() < 15);
	}
	private static boolean checkPassword(String pass) {
		return (pass.length() > 6);
	}
	*/
	
	public static void fillLibrary(libraryserver.StudentAccount sc) throws IOException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		LibraryClientServerProxy.lookupServers();
		System.out.println(LibraryClientServerProxy.lookupServer(sc.getEdInstitution()).createAccount(sc.getFirstName(), sc.getLastName(), sc.getEmailAddress(), sc.getPhoneNumber(), sc.getUserName(), "phil13", sc.getEdInstitution()));
	}
	
}
