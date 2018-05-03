package edu.buaa.sem.system.model;

public class UserModel {
	private String name;
	private String password;

	private String idCommaString;

	private String userNameCheck;
	private String email;
	private long companyId;
	
	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	private String description;
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	private String enabled;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdCommaString() {
		return idCommaString;
	}

	public void setIdCommaString(String idCommaString) {
		this.idCommaString = idCommaString;
	}

	public String getUserNameCheck() {
		return userNameCheck;
	}

	public void setUserNameCheck(String userNameCheck) {
		this.userNameCheck = userNameCheck;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
