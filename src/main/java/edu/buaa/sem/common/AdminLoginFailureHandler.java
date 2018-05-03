package edu.buaa.sem.common;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import edu.buaa.sem.utils.IpUtils;
import edu.buaa.sem.utils.PropertiesUtils;

public class AdminLoginFailureHandler implements AuthenticationFailureHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(AdminLoginFailureHandler.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		String userName = request.getParameter("j_username");
		int maxLogin = 0;
		if (StringUtils.isNotEmpty(userName)) {
			Session session = sessionFactory.openSession();

			List<Object[]> list = session
					.createSQLQuery(
							"select max_login,enabled from sys_user where name=:name")
					.setString("name", userName).list();

			if (list != null && list.size() > 0) {
				Object[] obj = (Object[]) list.get(0);
				Integer obj1 = (Integer) obj[0];
				Character obj2 = (Character) obj[1];
				maxLogin = (obj1 == null ? 0 : obj1) + 1;
				String enabled = obj2.toString();
				if (maxLogin >= 5) {
					enabled = "否";
				}
				String ip = IpUtils.getIpAddr(request);
				session.createSQLQuery(
						"update sys_user set max_login=:maxLogin, enabled=:enabled, last_login_time=:loginTime, login_ip=:ip where name=:name")
						.setString("name", userName).setString("ip", ip)
						.setCalendar("loginTime", Calendar.getInstance())
						.setString("enabled", enabled)
						.setInteger("maxLogin", maxLogin).executeUpdate();
				logger.info(userName + " login failure times: " + maxLogin);
			}

			response.sendRedirect(request.getContextPath()
					+ "/admin/fast?error=" + maxLogin);

			session.close();
			return;
		}

		response.sendRedirect(request.getContextPath() + "/admin/fast?error=0");
	}

}
