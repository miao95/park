package edu.buaa.sem.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * 
 * PropertiesUtil.java
 * 
 * @desc properties 资源文件获取和保存工具
 * 
 */
public class PropertiesUtils {

	/**
	 * 获取properties
	 */
	public static Properties getProperties(String propertiesFileName) {

		Properties properties = new Properties();
		Class<PropertiesUtils> clazz = PropertiesUtils.class;
		String path = clazz.getResource("/").getPath();// 得到包含/WEB-INF/classes/的路径
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().endsWith("linux")
				|| osName.toLowerCase().endsWith("mac os x")) {
			path = path.substring(0);
		} else {
			path = path.substring(1);
		}
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(path + propertiesFileName);
			properties.load(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null)
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return properties;
	}

	/**
	 * 保存properties
	 */
	public static void saveProperties(String propertiesFileName,
			Properties properties) {
		Class<PropertiesUtils> clazz = PropertiesUtils.class;
		String path = clazz.getResource("/").getPath();// 得到包含/WEB-INF/classes/的路径
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().endsWith("linux")
				|| osName.toLowerCase().endsWith("mac os x")) {
			path = path.substring(0);
		} else {
			path = path.substring(1);
		}
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(path + propertiesFileName);
			properties.store(outputStream, "");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
