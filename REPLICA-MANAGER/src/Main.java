import java.util.Scanner;

public class Main {
	
	public static void showMenu(){
		System.out.println("\n****Welcome!****\n");
		System.out.println("Please select an option (1-2)");
		System.out.println("1. Run RM in high availability mode");
		System.out.println("2. Run RM in software failures tolerance mode");
		System.out.println("3. Exit");
		
	}

	public static void main(String[] args) {
		
		int userChoice=0;
		ReplicaManagerImpl rm = null;
		Scanner keyboard = new Scanner(System.in);
		
		showMenu();
		
		while(true)
		{
			Boolean valid = false;
			
			// Enforces a valid integer input.
			while(!valid)
			{
				try{
					userChoice=keyboard.nextInt();
					valid=true;
					keyboard.nextLine();
				}
				catch(java.lang.Exception e)
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
				rm = new ReplicaManagerImpl(false);
				rm.start();
				break;
			case 2:
				rm = new ReplicaManagerImpl(true);
				rm.start();
				break;
			case 3:
				System.out.println("Have a nice day!");
				keyboard.close();
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Input, please try again.");
			}
		}
	}

}
