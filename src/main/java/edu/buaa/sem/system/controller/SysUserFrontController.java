package edu.buaa.sem.system.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.buaa.sem.common.BaseController;
import edu.buaa.sem.common.BaseService.E;
import edu.buaa.sem.common.BasicMailSendService;
import edu.buaa.sem.po.SysEmailModel;
import edu.buaa.sem.po.SysUser;
import edu.buaa.sem.system.service.SysEmailModelService;
import edu.buaa.sem.system.service.SysUserRoleService;
import edu.buaa.sem.system.service.SysUserService;
import edu.buaa.sem.utils.EncryptionUtils;
import edu.buaa.sem.utils.PropertiesUtils;

@Controller
public class SysUserFrontController extends BaseController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	BasicMailSendService officialMailSendService;// 发送邮件service
	@Autowired
	SysEmailModelService sysEmailModelService;// 邮件模板service

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	@ResponseBody
	public String changePassword(
			@RequestParam(value = "oldPassword", required = true) String oldPassword,
			@RequestParam(value = "newPassword", required = true) String newPassword,
			@RequestParam(value = "checkPassword", required = true) String checkPassword) {
		String returnValue = " ";
		SysUser pojo = sysUserService.getCurrentUser();
		if (pojo == null) {
			return E.ILLEGALUSER();
		}
		if (!EncryptionUtils.getMD5(oldPassword).equals(pojo.getPassword())) {
			returnValue = "error1";
		} else if (newPassword.isEmpty() || newPassword.length() < 6) {
			returnValue = "error2";
		} else if (checkPassword.isEmpty()
				|| !checkPassword.equals(newPassword)) {
			returnValue = "error3";
		} else {
			pojo.setPassword(EncryptionUtils.getMD5(newPassword));
			sysUserService.update(pojo);
			returnValue = "success";
		}
		return returnValue;
	}

	@RequestMapping(value = "/changeNickname", method = RequestMethod.POST)
	public String changeNickname(
			@RequestParam(value = "name", required = true) String name,
			Model model) {
		SysUser pojo = sysUserService.getCurrentUser();
		String message = "";
		if (pojo == null) {
			return "/login";
		}
		if (StringUtils.isEmpty(name)) {
			message = "昵称不能为空！";
		} else if (sysUserService.findByUserName(name.toLowerCase()) != null) {
			message = "昵称" + name + "已被使用！";
		} else {
			pojo.setName(name);
			sysUserService.update(pojo);
			//更新spring security session
			sysUserService.getCurrentUserFromSpring().setNickname(name);
			message = "修改成功！";
		}
		model.addAttribute("message", message);
		return "/changeNickname";
	}

	/**
	 * 判断用户名是否注册过
	 * 
	 * @return
	 */
	@RequestMapping(value = "/checkUserName", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkUserName(String name) {
		Map<String, Object> responseJson = new HashMap<>();
		if (sysUserService.findByUserName(name.trim()) != null) {
			responseJson.put("valid", "false");// 代表用户名已经被注册过
		} else {
			responseJson.put("valid", "true");
		}
		return responseJson;
	}

	/**
	 * 判断用户邮箱是否已经注册过
	 * 
	 * @return
	 */
	@RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkUserEmail(String email) {
		Map<String, Object> responseJson = new HashMap<>();
		if (sysUserService.findByEmail(email.trim()) != null) {
			responseJson.put("valid", "false");// 代表用户名已经被注册过
		} else {
			responseJson.put("valid", "true");
		}
		return responseJson;
	}

	@RequestMapping(value = "/forgetPassword", method = RequestMethod.GET)
	public String forgetPassword() {
		return "/forgetPassword";
	}

	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
	public String forgetPasswordSendEmail(String email, Model model) {

		String message = "";
		if (StringUtils.isEmpty(email)) {
			message = "邮箱不存在！";
			model.addAttribute("message", message);
			return "/forgetPassword";
		}

		SysUser pojo = sysUserService.findByEmail(email.trim());

		if (pojo == null) {
			message = "邮箱不存在！";
			model.addAttribute("message", message);
			return "/forgetPassword";
		}

		Properties properties = PropertiesUtils
				.getProperties("system.properties");
		String resetUrl = "http://" + properties.getProperty("domainName")
				+ "/resetPassword";

		String emailTo = pojo.getEmail() + ';';
		String[] to = emailTo.split(";");

		String code = sysUserService.generateEmailCode();

		// TODO 重置密码邮件模板ID
		SysEmailModel emailModel = sysEmailModelService.findbyKey(22l);
		String emailModelSubject = emailModel.getEmailModelSubject();
		String emailModelContent = emailModel.getEmailModelContent()
				.replace("{resetUrl}", resetUrl).replace("{code}", code);
		emailModelContent = emailModelContent.replace("http://http://",
				"http://");// 修正bug

		try {
			officialMailSendService.sendHtmlEmailOfficial(to,
					emailModelSubject, emailModelContent);
			message = "邮件已发送，请登录您注册时填写的邮箱！";
			pojo.setEmailCode(code);
			pojo.setEmailTime(new Date());
			sysUserService.update(pojo);
		} catch (Exception e) {
			message = "邮件发送失败！";
			e.printStackTrace();
		}

		model.addAttribute("message", message);
		return "/resetPassword";
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public String resetPassword() {
		return "/resetPassword";
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public String resetPasswordConfirm(String email, String code,
			String password, Model model) {
		String message = "输入的信息有误，亲！";
		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code)
				|| StringUtils.isEmpty(password)) {
			model.addAttribute("message", message);
			return "/resetPassword";
		}

		SysUser pojo = sysUserService.findByEmail(email.trim());

		if (pojo == null) {
			model.addAttribute("message", message);
			return "/resetPassword";
		}

		if (pojo.getEmailTime() == null
				|| StringUtils.isEmpty(pojo.getEmailCode())) {
			model.addAttribute("message", message);
			return "/resetPassword";
		}

		if (!StringUtils.equals(code, pojo.getEmailCode())) {
			model.addAttribute("message", message);
			return "/resetPassword";
		}

		// TODO 过期时间为1小时
		Date now = new Date();
		Date emailTime = DateUtils.addHours(pojo.getEmailTime(), 1);
		// Date emailTime = DateUtils.addMinutes(pojo.getEmailTime(), 1);
		if (emailTime.before(now)) {
			message = "验证码已失效，请重新验证！";
			model.addAttribute("message", message);
			return "/resetPassword";
		}

		pojo.setPassword(EncryptionUtils.getMD5(password));
		sysUserService.update(pojo);

		message = "密码重置成功，请登录！";
		model.addAttribute("message", message);
		return "/login";
	}
}
