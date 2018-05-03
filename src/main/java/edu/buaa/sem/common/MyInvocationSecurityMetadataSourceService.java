package edu.buaa.sem.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

/**
 * 资源权限获取器：用来取得访问某个URL或者方法所需要的权限，接口为SecurityMetadataSource
 * 
 */
public class MyInvocationSecurityMetadataSourceService implements
		FilterInvocationSecurityMetadataSource {

	private SessionFactory sessionFactory;// 构造方法传值

	private RegexRequestMatcher urlMatcher;

	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

	/**
	 * 此类在初始化时，应该取到所有资源及其对应权限的定义。
	 * 
	 */
	public MyInvocationSecurityMetadataSourceService(
			SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		loadResourceDefine();
	}

	@SuppressWarnings("unchecked")
	private void loadResourceDefine() {
		if (resourceMap == null) {
			// 资源为key， 权限为value。 资源通常为url， 权限就是那些以AUTH_为前缀的角色。一个资源可以由多个权限来访问
			resourceMap = new HashMap<String, Collection<ConfigAttribute>>();

			Session session = sessionFactory.openSession();
			// 在Web服务器启动时，提取系统中的所有权限
			List<String> authorityName = session.createSQLQuery(
					"select name from sys_authority").list();
			// 对每一个权限获得其资源
			for (String auth : authorityName) {
				ConfigAttribute ca = new SecurityConfig(auth);
				List<String> resourceString = session
						.createSQLQuery(
								"select b.address "
										+ "from sys_authority_resource a, sys_resource b, "
										+ "sys_authority c where a.sys_resource_id = b.id "
										+ "and a.sys_authority_id=c.id and c.name='"
										+ auth
										+ "' and a.enabled='是' and b.enabled='是' and c.enabled='是' "
										+ "and b.type='页面和操作'").list();
				for (String res : resourceString) {
					String url = res;
					// 判断资源文件和权限的对应关系，如果已经存在相关的资源url，则要通过该url为key提取出权限集合，将权限增加到权限集合中
					if (resourceMap.containsKey(url)) {
						Collection<ConfigAttribute> value = resourceMap
								.get(url);
						value.add(ca);
						resourceMap.put(url, value);
					} else {
						Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
						atts.add(ca);
						resourceMap.put(url, atts);
					}
				}
			}
			session.close();
		}
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	/**
	 * 最核心的地方，根据被被用户请求的URL，找到相关的权限配置。
	 */
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		// object 是被用户请求的URL。
		HttpServletRequest request = ((FilterInvocation) object).getRequest();

		Iterator<String> ite = resourceMap.keySet().iterator();
		while (ite.hasNext()) {
			String resURL = ite.next();
			urlMatcher = new RegexRequestMatcher(resURL, request.getMethod());
			if (urlMatcher.matches(request)) {
				return resourceMap.get(resURL);
			}
		}

		return null;
	}

	public boolean supports(Class<?> arg0) {
		return true;
	}

}