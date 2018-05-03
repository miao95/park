package edu.buaa.sem.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.buaa.sem.utils.EncryptionUtils;

public class IdAndPassKeyInterceptor extends HandlerInterceptorAdapter {

	public static Logger logger = Logger
			.getLogger(IdAndPassKeyInterceptor.class);;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		// 取得请求相关的参数
		Map<String, String[]> parameters = request.getParameterMap();

		String[] ids = (String[]) parameters.get("id");
		String[] passKeys = (String[]) parameters.get("passKey");
		String[] passKeyTypes = (String[]) parameters.get("passKeyType");

		if (ids != null && passKeys != null && passKeyTypes != null
				&& !ids[0].equals("") && !passKeys[0].equals("")
				&& !passKeyTypes[0].equals("")) {
			if (EncryptionUtils.getMD5Front(ids[0] + passKeyTypes[0]).equals(
					passKeys[0])) {
				return true;
			}
			// 同时为空也放过
		} else if (ids == null && passKeys == null && passKeyTypes == null) {
			return true;
		}
		logger.warn("id not match passKey!");
		return false;
	}
}
