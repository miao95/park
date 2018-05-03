package edu.buaa.sem.common;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import edu.buaa.sem.po.SysUser;
import edu.buaa.sem.system.dao.SysUserDao;
import edu.buaa.sem.utils.PropertiesUtils;

/**
 * Service的基本类，用户设置一些公共属性和方法，每一个Service类必须继承该类
 */
@Service
public class BaseService {
	private String issuerAccount = "eL8WMBrVwxy44p2fsKZc1vrQrfe2VgpubA";// TODO
	// 转到数据库，系统启动后加载到全局变量
	private String issuerKey = "sh7YX1rPsnY23HhJuTJWy3MFxcPRS";// TODO
	// 转到数据库，系统启动后加载到全局变量
	
	
	class A {

	}

	// 注：枚举写在最前面，否则编译出错
	public enum E {
		NULLID("主键为空"), NULLFIELD("字段为空"), // 含参
		NULLENTITY("实体为空"), NOTNULLID("主键非空"), NOTNULLFIELD("字段非空"), // 含参
		NOTNULLENTITY("实体非空"), ILLEGALUSER("非法用户"), ILLEGALFIELD("非法字段"), // 含参
		ILLEGALOPERATE("非法操作"), EXIST("已存在"), // 含参
		NOTEXIST("不存在"), // 含参
		RENAMEFAIL("重命名失败"), ERROR("错误"), // 含参
		SUCCESS("SUCCESS");// 含参

		// 成员变量
		private String name;

		// 构造方法
		private E(String name) {
			this.name = name;
		}

		public static String NULLID() {
			return (E.NULLID.name.toUpperCase());
		}

		public static String NULLFIELD(String name) {
			return (name + " " + E.NULLFIELD.name).toUpperCase();
		}

		public static String NULLENTITY() {
			return (E.NULLENTITY.name.toUpperCase());
		}

		public static String NOTNULLID() {
			return (E.NOTNULLID.name.toUpperCase());
		}

		public static String NOTNULLFIELD(String name) {
			return (name + " " + E.NOTNULLFIELD.name).toUpperCase();
		}

		public static String NOTNULLENTITY() {
			return (E.NOTNULLENTITY.name.toUpperCase());
		}

		public static String ILLEGALUSER() {
			return (E.ILLEGALUSER.name.toUpperCase());
		}

		public static String ILLEGALFIELD(String name) {
			return (name + " " + E.ILLEGALFIELD.name).toUpperCase();
		}

		public static String EXIST(String name) {
			return (name + " " + E.EXIST.name).toUpperCase();
		}

		public static String NOTEXIST(String name) {
			return (name + " " + E.NOTEXIST.name).toUpperCase();
		}

		public static String SUCCESS() {
			return (E.SUCCESS.name.toUpperCase());
		}

		public static String RENAMEFAIL(String name) {
			return (name + " " + E.RENAMEFAIL.name).toUpperCase();
		}

		public static String SUCCESS(String name) {
			return (name + " " + E.SUCCESS.name).toUpperCase();
		}

		public static String ERROR() {
			return (E.ERROR.name.toUpperCase());
		}

		public static String ERROR(String name) {
			return (name + " " + E.ERROR.name).toUpperCase();
		}

		public static String ILLEGALOPERATE() {
			return (E.ILLEGALOPERATE.name.toUpperCase());
		}
	}

	protected String propertiesFileName = "system.properties";
	protected Properties properties = PropertiesUtils
			.getProperties(propertiesFileName);
	
	@Autowired
	protected SysUserDao sysUserDao;

	/**
	 * 对Excel的单元行处理，获得字符型结果
	 * 
	 * @param cell
	 * @return
	 */
	public String handleCelltoString(Cell cell) {
		if (cell == null) {
			return null;
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA
				|| cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {// 表达式或数字类型
			DecimalFormat df = new DecimalFormat("0");// 学号过长可能会变成科学计数法,需format
			String temp = df.format(cell.getNumericCellValue());
			// String temp = String.valueOf(cell.getNumericCellValue());
			// 去除小数点和后面的0
			if (temp.indexOf(".") > 0) {
				temp = temp.replaceAll("0+?$", "");// 去掉多余的0
				temp = temp.replaceAll("[.]$", "");// 如最后一位是.则去掉
			}
			return temp;
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {// 字符串类型
			return cell.getStringCellValue();
		} else {
			return null;
		}
	}

	/**
	 * 对Excel的单元行处理，获得Date类型结果
	 * 
	 * @param cell
	 * @return
	 */
	public Date handleCelltoDate(Cell cell) {
		if (cell == null) {
			return null;
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				return new Date(cell.getDateCellValue().getTime());
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 对按分号连接的id字符串转换为数组
	 * 
	 * @param idCommaString
	 * @return
	 */
	public long[] handleToIdLongArray(String idCommaString) {
		String[] idStrings = idCommaString.split(",");
		long[] ids = new long[idStrings.length];
		for (int i = 0; i < idStrings.length; i++) {
			ids[i] = Long.parseLong(idStrings[i]);
		}
		return ids;
	}


	/**
	 * 利用反射检验pojo中的属性是否为空(String或者Long)
	 * 
	 * @param pojo
	 * @param strings
	 * @return
	 */
	public String validFieldOfPojo(Object pojo, String... strings) {
		Class<?> example = pojo.getClass();
		Method method;
		if (strings != null && strings.length > 0) {
			for (int i = 0; i < strings.length; i++) {
				try {
					// 首字母大写
					String s = strings[i];
					s = s.replaceFirst(s.substring(0, 1), s.substring(0, 1)
							.toUpperCase());
					String getName = "get" + s;
					method = example.getMethod(getName);
					try {
						if (method.getReturnType().equals(String.class)) {
							String result = (String) method.invoke(pojo);
							if (result == null || result.trim().isEmpty()) {
								return E.NULLFIELD(strings[i]);
							}
						} else if (method.getReturnType().equals(Long.class)) {
							Long result = (Long) method.invoke(pojo);
							if (result == null) {
								return E.NULLFIELD(strings[i]);
							}
						} else if (method.getReturnType().equals(long.class)) {
							long result = (long) method.invoke(pojo);
							if (result == -1) {
								return E.NULLFIELD(strings[i]);
							}
						}
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
					}
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		return "VALID";
	}

	/**
	 * 利用反射设置pojo中的属性
	 * 
	 * @param strings
	 * @return
	 */
	public void setFieldOfPojo(Object source, Object target, String... strings) {
		if (strings != null && strings.length > 0) {
			for (int i = 0; i < strings.length; i++) {
				try {
					// 首字母大写
					String s = strings[i];
					s = s.replaceFirst(s.substring(0, 1), s.substring(0, 1)
							.toUpperCase());
					// 取得setter和getter的名字
					String setName = "set" + s;
					String getName = "get" + s;
					Method sourceMethod = source.getClass().getMethod(getName);
					try {
						// 取出source的值并赋给target
						Object result = sourceMethod.invoke(source);
						// 若值为空,则跳过
						if (result == null) {
							continue;
						}
						Method targetMethod = target.getClass().getMethod(
								setName, sourceMethod.getReturnType());
						targetMethod.invoke(target, result);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
					}
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getIssuerAccount() {
		return issuerAccount;
	}

	public String getIssuerKey() {
		return issuerKey;
	}
	
	
	/**
	 * 判断传过来的 request 里是否缺失参或参数为空
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */
	public boolean checkObjFieldIsNull(Object obj) {
	    boolean flag = false;
	    try {
		    for(Field f : obj.getClass().getDeclaredFields()){
		        f.setAccessible(true);
				if(f.get(obj) == null || f.get(obj).toString().isEmpty()){
				    flag = true;
				    return flag;
				}
				
		    }
	    } catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return flag;
	}
	
	
	public MyUser getCurrentUserFromSpring() {
		Object userDetails = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (userDetails instanceof UserDetails) {
			return ((MyUser) userDetails);
		} else {
			return null;
		}
	}

	public SysUser getCurrentUser() {
		MyUser my = getCurrentUserFromSpring();
		if (my == null) {
			return null;
		}

		String userName = my.getUsername();
		List<SysUser> pojos = sysUserDao.findByPropertyEqual("name", userName,
				"String");
		if (pojos != null && pojos.size() == 1) {
			return pojos.get(0);
		} else {
			return null;
		}
	}

	public boolean isLogin() {
		MyUser my = getCurrentUserFromSpring();
		if (my == null) {
			return false;
		}
		return true;
	}

	
}
