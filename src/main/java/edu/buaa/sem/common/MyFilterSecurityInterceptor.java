package edu.buaa.sem.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import edu.buaa.sem.utils.IpUtils;

/**
 * URL或者方法过滤拦截器：用来拦截URL或者方法资源对其进行验证，其抽象基类为AbstractSecurityInterceptor
 * 
 * 该过滤器的主要作用就是通过Spring著名的IoC生成securityMetadataSource。
 * securityMetadataSource相当于本包中自定义的MyInvocationSecurityMetadataSourceService。
 * 
 */
public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor
		implements Filter {

	private FilterInvocationSecurityMetadataSource securityMetadataSource;

	public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public void setSecurityMetadataSource(
			FilterInvocationSecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String userName = "";
		Object userDetails = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (userDetails instanceof UserDetails) {
			userName = ((UserDetails) userDetails).getUsername();
		} else {
			userName = userDetails.toString();
		}
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		// 获取请求用户的真实IP
		String ip = IpUtils.getIpAddr(httpServletRequest);
		// 获取请求用户想访问的页面地址
		String url = httpServletRequest.getRequestURL().toString().trim();
		String userAgent = httpServletRequest.getHeader("User-Agent");
		// 输出日志
		logger.info("IP{" + ip + "}USER{" + userName + "}AGENT{" + userAgent
				+ "}URL{" + url + "}");

		FilterInvocation fi = new FilterInvocation(request, response, chain);
		invoke(fi);
	}

	public Class<? extends Object> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	public void invoke(FilterInvocation fi) throws IOException,
			ServletException {
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public void destroy() {

	}

	public void init(FilterConfig filterconfig) throws ServletException {

	}

}