package libraryclienttest;

/**
 *  RUN THI AFTER RUNNING PublishWS
 *  This is the admin client that allows administrators to getnonreturners
 *	Username and password combination: admin, admin
 *  @author Nathan MacInnes
 */
	
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import libraryclient.LibraryClientServerProxy;
import libraryserver.ReadWriteTextFileWithEncoding;

public class AdminClient {
	
	private static Integer i = 0;	
	private static String message;
	private static ReadWriteTextFileWithEncoding io = new ReadWriteTextFileWithEncoding("src/Client/AdminLog"+i.toString()+".txt", "UTF8");
	
public static void main(String[] args) {
		
		LibraryClientServerProxy.lookupServers();
		run();
		
		//connectedLibrary.shutdown();
		
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
				String numDays = "";
				String school = "";
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
					System.out.println("Get non-returners, enter the following information: ");
					keyboard.nextLine();
					System.out.println("Please enter your username:");
					username=keyboard.nextLine();
					System.out.println("Please enter your password:");
					password=keyboard.nextLine();
					System.out.println("Please enter your educational institution:");
					school=keyboard.nextLine().toLowerCase();
					System.out.println("Please enter the number of days:");
					numDays=keyboard.nextLine();
					System.out.println(LibraryClientServerProxy.lookupServer(school).getNonreturners(username, password, school, numDays));
					showMenu();
					break;
				case 2:
					System.out.println("Quiting library system.");
					System.out.println("Have a good day.");
					System.exit(0);
				default:
					
					System.out.println("Invalid Input, please try again.");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in Admin Client: " + e);
			e.printStackTrace();
		} finally {
			//System.out.println("Exception Student Client: Unknown");
		}
	}
	
	public static void showMenu() {
		System.out.println("\n****Admin, welcome to the Inter-University Library Server System****\n");
		System.out.println("Please select an option (1 or 2)");
		System.out.println("1. Get Nonreturners");
		System.out.println("2. Quit");
	}

}
