package drms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.omg.CORBA.ORB;

import userInfo.AdminAccount;
import userInfo.StudentAccount;

import drms.operations;
import drms.operationsHelper;

public class ClientDRMS {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String cs = "Concordia";
		String us = "UQAM";
		String ms = "McGill";
		
		final StudentAccount s1 = new StudentAccount("aConcordia1", "Student", "pk@habs.com", "514-341-7676", "aTestUserName1", "careyprice", "Concordia");
		
		final StudentAccount s2 = new StudentAccount("a2Concordia2", "Patel1", "jigneshp@mail.com", "524-123-4589", "bTestUserName2", "password", "Concordia");
		final StudentAccount s7 = new StudentAccount("a2TestLateReturn", "Patel2", "guy@mail.com", "524-123-4589", "aTestName4", "password", "Concordia");

		final StudentAccount s3 = new StudentAccount("aMcGill1", "James", "lbj@cavs.com", "260-598-9685", "aTestUserName3", "kyrie", "McGill");		
		final StudentAccount s4 = new StudentAccount("aMcGill2", "love", "lbj@cavs.com", "260-598-9685", "bTestUserName3", "kyrie", "McGill");		
		final StudentAccount s8 = new StudentAccount("aMcGill3TestLateReturn", "irving", "lbj@cavs.com", "260-598-9685", "aTestName5", "kyrie", "McGill");		

		final StudentAccount s5 = new StudentAccount("aUQAM1", "Griffin", "bg@clippers.com", "523-958-9685", "aTestUserName2", "DeAndre", "UQAM");
		final StudentAccount s6 = new StudentAccount("aUQAM2", "paul", "bg@clippers.com", "523-958-9685", "bTestUserName2", "DeAndre", "UQAM");
		final StudentAccount s9 = new StudentAccount("aUQAM3", "jordan", "bg@clippers.com", "523-958-9685", "aTestUser2", "DeAndre", "UQAM");

		//Create Admin Accounts
		
		final AdminAccount adm1 = new AdminAccount("adminConcordia",  "password", "Concordia");
		final AdminAccount adm2 = new AdminAccount("adminMcGill",  "password", "McGill");
		AdminAccount adm3 = new AdminAccount("adminUQAM",  "password", "UQAM");

		//Client Orb for Concordia
		
		//initialize client ORB
		ORB conOrb = ORB.init(args,null);
		
		
		//fetch the IOR
		BufferedReader br = new BufferedReader(new FileReader(cs + "Ior.txt"));
		String conIOR = br.readLine();
		br.close();
		
		//Transform IOR into a CORBA object
		org.omg.CORBA.Object conOrbObj = conOrb.string_to_object(conIOR);
		
		final operations conServer = operationsHelper.narrow(conOrbObj);
		
		//McGill ORB for client
		ORB mcgOrb = ORB.init(args,null);
		
		
		//fetch the IOR
		BufferedReader br1 = new BufferedReader(new FileReader(ms + "Ior.txt"));
		String mcgIOR = br1.readLine();
		br1.close();
		
		//Transform IOR into a CORBA object
		org.omg.CORBA.Object mcgOrbObj = mcgOrb.string_to_object(mcgIOR);
		
		final operations mcgServer = operationsHelper.narrow(mcgOrbObj);
		
		//UQAM ORB for client
		ORB uqamOrb = ORB.init(args,null);
		
		//fetch the IOR
		BufferedReader br2 = new BufferedReader(new FileReader(us + "Ior.txt"));
		String uqamIOR = br2.readLine();
		br2.close();
		
		//Transform IOR into a CORBA object
		org.omg.CORBA.Object uqamOrbObj = uqamOrb.string_to_object(uqamIOR);
		
		final operations uqamServer = operationsHelper.narrow(uqamOrbObj);
		
		//Call methods with inputs
		
		Scanner sc = new Scanner(System.in);
		int choice = 0;

		while(true)
		{
			System.out.println("Welcome. Your choices are: ");
			System.out.println("Choice 1: Create 2 Concordia, McGill, and UQAM Students");
			System.out.println("Choice 2: Reserve a Local book at Concordia which works. Reserve the same nonavailable title, which doesn't work at McGill");
			System.out.println("Choice 3: getNonReturners for Concordia and McGill");
			System.out.println("Choice 4: Reserve Interlibrary at Concordia");
			choice = sc.nextInt();
			
			if (choice == 1)
			{
				
				Thread t1 = new Thread()
				{
					public void run()
					{
				
				//Create Concordia Students
				conServer.createAccount(s1.firstName, s1.lastName, s1.emailAddress, s1.phoneNumber, s1.userName, s1.password, s1.institution);
				conServer.createAccount(s2.firstName, s2.lastName, s2.emailAddress, s2.phoneNumber, s2.userName, s2.password, s2.institution);
				conServer.createAccount(s7.firstName, s7.lastName, s7.emailAddress, s7.phoneNumber, s7.userName, s7.password, s7.institution);

				
				//Create McGill Students
				mcgServer.createAccount(s3.firstName, s3.lastName, s3.emailAddress, s3.phoneNumber, s3.userName, s3.password, s3.institution);
				mcgServer.createAccount(s4.firstName, s4.lastName, s4.emailAddress, s4.phoneNumber, s4.userName, s4.password, s4.institution);
				mcgServer.createAccount(s8.firstName, s8.lastName, s8.emailAddress, s8.phoneNumber, s8.userName, s8.password, s8.institution);

				//Create UQAM Students
				uqamServer.createAccount(s5.firstName, s5.lastName, s5.emailAddress, s5.phoneNumber, s5.userName, s5.password, s5.institution);
				uqamServer.createAccount(s6.firstName, s6.lastName, s6.emailAddress, s6.phoneNumber, s6.userName, s6.password, s6.institution);
				uqamServer.createAccount(s9.firstName, s9.lastName, s9.emailAddress, s9.phoneNumber, s9.userName, s9.password, s9.institution);

				}
				};
				
				t1.start();
				
			}
			else if(choice ==2)
			{
				
				Thread t2 = new Thread()
				{
					public void run()
					{
				//Reserve at Concordia, will Work
				conServer.reserveBook(s1.userName, s1.password, "game of thrones 1", "GRR Martin", s1.institution);
				
				//Reserve at McGill, won't work
				mcgServer.reserveBook(s3.userName, s3.password, "game of thrones 1", "GRR Martin", s3.institution);
				
			}
			};
			
			t2.start();

			}
			else if (choice ==3)
			{
				
				Thread t3 = new Thread()
				{
					public void run()
					{
				//Get non returners
				conServer.getNonReturners(adm1.username, adm1.password, adm1.institution, 7);
				mcgServer.getNonReturners(adm2.username, adm2.password, adm2.institution, 14);

			}
			};
			
			t3.start();
			}
			else if(choice ==4)
			{
				Thread t4 = new Thread()
				{
					public void run()
					{
				//For Concordia Uni, reserve interlibrary at McGill 
				//book is not at Concordia, but at McGill.
				conServer.reserveInterLibrary(s1.userName, s1.password, "Manhood", "Terry Crews");

			}
			};
			
			t4.start();
			}
			else{
				break;
			}
			
			
			//sc.next();
		}
		
		
		

		
		
		//Create uqam instances

		//mcgServer.testUdpClient();
		
		
	}

}
