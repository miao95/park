package edu.buaa.sem.common;

import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 防止CSRF攻击的RequestMatcher，除排除列表外，全站有效。_csfr参数错误会提示403禁止
 * 
 * 全部用POST提交请求，因为GET必须要把参数写到地址栏，容易保存泄漏。
 */
public class CsrfRegexRequestMatcher implements RequestMatcher {
	private Pattern excludeMethods = Pattern
			.compile("^(GET|HEAD|TRACE|OPTIONS)$");// 需要排除的方法列表

	private List<String> excludeUrls = null;// 需要排除的Url列表
	private RegexRequestMatcher urlMatcher;

	@Override
	public boolean matches(HttpServletRequest request) {
		if (excludeUrls != null && excludeUrls.size() != 0) {
			for (String excludeUrl : excludeUrls) {
				urlMatcher = new RegexRequestMatcher(excludeUrl,
						request.getMethod());

				if (urlMatcher.matches(request)) {
					return false;
				}
			}
		}
		return !excludeMethods.matcher(request.getMethod()).matches();

	}

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

}
