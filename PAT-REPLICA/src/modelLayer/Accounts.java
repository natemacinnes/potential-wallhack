package modelLayer;

import java.util.Collection;
import java.util.HashMap;


public class Accounts {
	
	private HashMap<String,Account> accountList;
	
	public Accounts(){
		accountList = new HashMap<String,Account>();
	}
	
	public void addAccount(Account a){
		accountList.put(a.getUsername(), a);
	}
	
	public Account getAccount(String username){
		return accountList.get(username);
	}
	
	public boolean usernameExist(String username){
		return accountList.containsKey(username);
	}
	
	public Collection<Account> getAllAccount(){
		return accountList.values();
	}

}
