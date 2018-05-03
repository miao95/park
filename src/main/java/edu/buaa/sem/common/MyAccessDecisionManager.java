package edu.buaa.sem.common;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * 访问决策器：用来决定用户是否拥有访问权限的关键类，其接口为AccessDecisionManager。这个
 * AccessDecisionManager被AbstractSecurityInterceptor调用， 它用来作最终访问控制的决定。
 * 
 * 所有的Authentication实现需要保存在一个GrantedAuthority对象数组中。 这就是赋予给用户的权限。
 * GrantedAuthority对象通过AuthenticationManager
 * 保存到Authentication对象里，然后从AccessDecisionManager读出来，进行授权判断。
 * 
 */
public class MyAccessDecisionManager implements AccessDecisionManager {

	/**
	 * AccessDecisionManager使用方法参数传递所有信息，如果访问被拒绝，将抛出一个AccessDeniedException异常。
	 */
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		// 默认如果该页面的configAttributes为空，则此方法不会被调用，所以这里不用进行空判断
		// if (configAttributes == null) {
		// return;
		// }
		Iterator<ConfigAttribute> ite = configAttributes.iterator();
		while (ite.hasNext()) {
			ConfigAttribute ca = ite.next();
			String needAuthority = ((SecurityConfig) ca).getAttribute();

			// ga 为用户所被赋予的权限。needAuthority为访问相应的资源应该具有的权限。
			for (GrantedAuthority ga : authentication.getAuthorities()) {
				if (needAuthority.trim().equals(ga.getAuthority().trim())) {
					return;
				}
			}
		}

		throw new AccessDeniedException("没有权限访问");
	}

	/**
	 * supports(ConfigAttribute) 方法在启动的时候被
	 * AbstractSecurityInterceptor调用，来决定AccessDecisionManager
	 * 是否可以执行传递ConfigAttribute。
	 * 
	 */
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	/**
	 * 
	 * 这个 supports(Class)方法被安全拦截器实现调用，
	 * 包含安全拦截器将显示的AccessDecisionManager支持安全对象的类型。
	 */
	public boolean supports(Class<?> clazz) {
		return true;

	}
}