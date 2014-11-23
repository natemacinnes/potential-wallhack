package dataLayer;

import java.util.Collection;
import java.util.HashMap;

import modelLayer.Account;
import modelLayer.Accounts;
import serverException.TableUniqueIdException;

public class AccountsTableGateway {
	
	private HashMap<String,Accounts> table;
	
	//This constructor has a parameter only for testing purposes
	public AccountsTableGateway(String institution){
		table = new HashMap<String,Accounts>();
		table.put("a", new Accounts());
		table.put("b", new Accounts());
		table.put("c", new Accounts());
		table.put("d", new Accounts());
		table.put("e", new Accounts());
		table.put("f", new Accounts());
		table.put("g", new Accounts());
		table.put("h", new Accounts());
		table.put("i", new Accounts());
		table.put("j", new Accounts());
		table.put("k", new Accounts());
		table.put("l", new Accounts());
		table.put("m", new Accounts());
		table.put("n", new Accounts());
		table.put("o", new Accounts());
		table.put("p", new Accounts());
		table.put("q", new Accounts());
		table.put("r", new Accounts());
		table.put("s", new Accounts());
		table.put("t", new Accounts());
		table.put("u", new Accounts());
		table.put("v", new Accounts());
		table.put("w", new Accounts());
		table.put("x", new Accounts());
		table.put("y", new Accounts());
		table.put("z", new Accounts());
		
		createInitialAccounts(institution);
	}
	
	public void addAccount(Account acc) throws TableUniqueIdException{
		//get first letter of username
		String username = acc.getUsername();
		String firstLetter = String.valueOf(username.charAt(0)).toLowerCase();
		
		Accounts accounts = table.get(firstLetter);
		
		//coordinate account creation among students
		synchronized(accounts){
			//Verify that the username doesn't already exist
			if(accounts.usernameExist(username)){
				throw new TableUniqueIdException("username already exist. Choose another one");
			}
			accounts.addAccount(acc);
		}
	}
	
	//this method perform the same thing as getAccount(username, password) except that
	//it doesn't try to match the password. This function is only used for debugging purpose
	//and for the demo
	public Account getAccount(String username){
		//get accounts list starting with the first letter of the username
		String firstLetter = String.valueOf(username.charAt(0)).toLowerCase();
		Accounts accounts = table.get(firstLetter);
		//get the particular account associated with that username
		Account account = accounts.getAccount(username);
		
		return account;
	}
	
	//Validate the credentials of a given user and fetch its account.
	//This function returns an account instance if the credentials are valid
	//and null otherwise
	public Account getAccount(String username, String password){
		Account account = getAccount(username);
		//account doesn't exist
		if(account == null){
			return account;
		}
		else{
			//return the account if the password is correct
			if(account.getPassword().equals(password)){
				return account;
			}
			else{
				return null;
			}
		}
	}
	
	public Collection<Accounts> getAllAccounts(){
		return table.values();
	}
	
	//only there for testing purposes
	private void createInitialAccounts(String institution){
		Account account1 = new Account("Cloud", "Strife", "test@gmail.com","5142312345","clouds","clouds",institution);
		Account account2 = new Account("Tifa", "Lockheart", "test@gmail.com","5144352198","tifalo","tifalo",institution);
		Account account3 = new Account("Vincent", "Valentine", "test@gmail.com","5149823476","vincent","vincent",institution);
		Account account4 = new Account("Barret", "Wallace", "test@gmail.com","5147632134","barret","barret",institution);
		Account account5 = new Account("Aeris", "Gainsborough", "test@gmail.com","5144753921","aerisg","aerisg",institution);
		
		try{
			addAccount(account1);
			addAccount(account2);
			addAccount(account3);
			addAccount(account4);
			addAccount(account5);
		}catch(TableUniqueIdException e){}

	}

}
