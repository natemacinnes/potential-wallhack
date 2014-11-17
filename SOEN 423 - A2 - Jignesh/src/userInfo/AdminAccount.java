package userInfo;

public class AdminAccount {

	public String username;
	public String password;
	public String institution;	
	
	public AdminAccount(String username, String password,
			String institution) {
		super();
		this.username = username;
		this.password = password;
		this.institution = institution;
	}

	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	

}
