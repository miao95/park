package edu.buaa.sem.common;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Controller的基本类，用户设置一些公共属性和方法，每一个Controller类必须继承该类
 * 
 */
public class BaseController {

	/*@InitBinder
	public void initBinder(WebDataBinder binder) {
		// 自动将所有传入的String类型进行Xss过滤
		binder.registerCustomEditor(String.class,
				new StringAttackEscapePropertyEditor(false));

		// 将表单中的file自动映射成byte[],这样文件上传（如果使用blob）就无需写任何代码了。
		// binder.registerCustomEditor(byte[].class,
		// new ByteArrayMultipartFileEditor());

		// 将表单中的yyyy-MM-dd格式映射成java.util.Date
		binder.registerCustomEditor(Date.class, new DateEditor());
	}*/
}
