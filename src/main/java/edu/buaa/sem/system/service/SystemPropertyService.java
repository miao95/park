package edu.buaa.sem.system.service;

//Auto Genereted 2015-6-21 16:57:01

import java.util.Properties;

import org.springframework.stereotype.Service;

import edu.buaa.sem.utils.PropertiesUtils;
import edu.buaa.sem.po.SystemProperty;

@Service
public class SystemPropertyService {
	private String propertiesFileName = "system.properties";

	public SystemProperty getProperty() {
		Properties properties = PropertiesUtils.getProperties(propertiesFileName);
		
		String domainName = properties.getProperty("domainName");
		String jdbcIP = properties.getProperty("jdbcIP");
		String jdbcUser = properties.getProperty("jdbcUser");
		String jdbcPassword = properties.getProperty("jdbcPassword");
		String emailHost = properties.getProperty("emailHost");
		String emailPort = properties.getProperty("emailPort");
		String emailOfficialUsername = properties.getProperty("emailOfficialUsername");
		String emailOfficialPassword = properties.getProperty("emailOfficialPassword");
		String adminPwdRegex = properties.getProperty("adminPwdRegex");
		
		return new SystemProperty(domainName, jdbcIP, jdbcUser, jdbcPassword, emailHost, emailPort, emailOfficialUsername, emailOfficialPassword, adminPwdRegex);
	}
	public void setProperty(SystemProperty pojo) {
		Properties properties = new Properties();
		
		properties.setProperty("domainName", pojo.getDomainName());
		properties.setProperty("jdbcIP", pojo.getJdbcIP());
		properties.setProperty("jdbcUser", pojo.getJdbcUser());
		properties.setProperty("jdbcPassword", pojo.getJdbcPassword());
		properties.setProperty("emailHost", pojo.getEmailHost());
		properties.setProperty("emailPort", pojo.getEmailPort());
		properties.setProperty("emailOfficialUsername", pojo.getEmailOfficialUsername());
		properties.setProperty("emailOfficialPassword", pojo.getEmailOfficialPassword());
		properties.setProperty("adminPwdRegex", pojo.getAdminPwdRegex());
		
		PropertiesUtils.saveProperties(propertiesFileName, properties);
	}

}
