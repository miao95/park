package edu.buaa.sem.po;

//Auto Genereted 2015-6-21 16:57:01

public class SystemProperty {

    private String domainName;
    private String jdbcIP;
    private String jdbcUser;
    private String jdbcPassword;
    private String emailHost;
    private String emailPort;
    private String emailOfficialUsername;
    private String emailOfficialPassword;
    private String adminPwdRegex;
	
    public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
    public String getJdbcIP() {
		return jdbcIP;
	}

	public void setJdbcIP(String jdbcIP) {
		this.jdbcIP = jdbcIP;
	}
    public String getJdbcUser() {
		return jdbcUser;
	}

	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}
    public String getJdbcPassword() {
		return jdbcPassword;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}
    public String getEmailHost() {
		return emailHost;
	}

	public void setEmailHost(String emailHost) {
		this.emailHost = emailHost;
	}
    public String getEmailPort() {
		return emailPort;
	}

	public void setEmailPort(String emailPort) {
		this.emailPort = emailPort;
	}
    public String getEmailOfficialUsername() {
		return emailOfficialUsername;
	}

	public void setEmailOfficialUsername(String emailOfficialUsername) {
		this.emailOfficialUsername = emailOfficialUsername;
	}
    public String getEmailOfficialPassword() {
		return emailOfficialPassword;
	}

	public void setEmailOfficialPassword(String emailOfficialPassword) {
		this.emailOfficialPassword = emailOfficialPassword;
	}
    public String getAdminPwdRegex() {
		return adminPwdRegex;
	}

	public void setAdminPwdRegex(String adminPwdRegex) {
		this.adminPwdRegex = adminPwdRegex;
	}

	public SystemProperty() {

	}
	public SystemProperty(String domainName, String jdbcIP, String jdbcUser, String jdbcPassword, String emailHost, String emailPort, String emailOfficialUsername, String emailOfficialPassword, String adminPwdRegex){
	
	    this.domainName = domainName;
	    this.jdbcIP = jdbcIP;
	    this.jdbcUser = jdbcUser;
	    this.jdbcPassword = jdbcPassword;
	    this.emailHost = emailHost;
	    this.emailPort = emailPort;
	    this.emailOfficialUsername = emailOfficialUsername;
	    this.emailOfficialPassword = emailOfficialPassword;
	    this.adminPwdRegex = adminPwdRegex;
	}
}