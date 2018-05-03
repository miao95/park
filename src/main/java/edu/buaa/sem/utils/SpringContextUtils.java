package edu.buaa.sem.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring的工具类，用来获得配置文件中的bean
 * 
 * 
 */
public class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;

	/***
	 * 当继承了ApplicationContextAware类之后，那么程序在调用 getBean(String)的时候会自动调用该方法，不用自己操作
	 */
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/***
	 * 根据一个bean的id获取配置文件中相应的bean
	 * 
	 * @param id
	 * @return
	 * @throws BeansException
	 */
	public static Object getBean(String id) throws BeansException {
		return applicationContext.getBean(id);
	}

	/**
	 * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
	 * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
	 * 
	 * @param id
	 * @return boolean
	 * @throws NoSuchBeanDefinitionException
	 */
	public static boolean isSingleton(String id)
			throws NoSuchBeanDefinitionException {
		return applicationContext.isSingleton(id);
	}

	/**
	 * @param id
	 * @return Class 注册对象的类型
	 * @throws NoSuchBeanDefinitionException
	 */
	public static Class getType(String id) throws NoSuchBeanDefinitionException {
		return applicationContext.getType(id);
	}
}