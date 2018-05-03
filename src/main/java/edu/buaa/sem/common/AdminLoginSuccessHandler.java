package edu.buaa.sem.common;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import edu.buaa.sem.utils.IpUtils;

@Service
public class AdminLoginSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(AdminLoginSuccessHandler.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {

		String userName = request.getParameter("j_username");
		if (StringUtils.isNotEmpty(userName)) {
			Session session = sessionFactory.openSession();
			String ip = IpUtils.getIpAddr(request);
			session.createSQLQuery(
					"update sys_user set max_login=:maxLogin, last_login_time=:loginTime, login_ip=:ip where name=:name")
					.setString("name", userName).setString("ip", ip)
					.setCalendar("loginTime", Calendar.getInstance())
					.setInteger("maxLogin", 0).executeUpdate();
			session.close();
		}

		// Use the DefaultSavedRequest URL
		String targetUrl = request.getParameter("redirectUrl");
		if (targetUrl == null || targetUrl.isEmpty()) {
			targetUrl = "/admin/index";
		}
		logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
