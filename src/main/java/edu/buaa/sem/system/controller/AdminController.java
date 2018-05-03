package edu.buaa.sem.system.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.buaa.sem.po.SysUser;
import edu.buaa.sem.system.service.SysUserService;
import edu.buaa.sem.utils.EncryptionUtils;
import edu.buaa.sem.utils.PropertiesUtils;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private SysUserService sysUserService;

	@RequestMapping(value = "/changeLoggerLevel")
	@ResponseBody
	public String changeLoggerLevel(String level) {
		if (level != null && level.equals("debug")) {
			Logger.getRootLogger().setLevel(Level.DEBUG);
		} else if (level != null && level.equals("warn")) {
			Logger.getRootLogger().setLevel(Level.WARN);
		} else {
			Logger.getRootLogger().setLevel(Level.INFO);
		}

		return "success";
	}

	@RequestMapping(value = "/fast", method = RequestMethod.GET)
	public String adminLogin(Model model) {
		if (sysUserService.isLogin()) {
			return "redirect:/admin/index";
		}
		
		return "/admin/login";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String changePassword() {
		return "/admin/changePassword";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(
			@RequestParam(value = "type", required = false, defaultValue = "system") String type,
			Model model) {
		model.addAttribute("type", type);
		return "/admin/index";
	}
	
	@RequestMapping(value = "/system/adminUser", method = RequestMethod.GET)
	public String adminUser(Model model) {
		Properties properties = PropertiesUtils
				.getProperties("system.properties");
		String pwdRegex = properties.getProperty("adminPwdRegex");
		model.addAttribute("adminPwdRegex", pwdRegex);
		return "/admin/system/adminUser";
	}

	@RequestMapping(value = "/system/commonUser", method = RequestMethod.GET)
	public String commonUser() {
		return "/admin/system/commonUser";
	}

	@RequestMapping(value = "/system/realNameUser", method = RequestMethod.GET)
	public String realNameUser() {
		return "/admin/system/realNameUser";
	}

	@RequestMapping(value = "/system/roleManagement", method = RequestMethod.GET)
	public String roleManagement() {
		return "/admin/system/roleManagement";
	}

	@RequestMapping(value = "/system/authorityManagement", method = RequestMethod.GET)
	public String authorityManagement() {
		return "/admin/system/authorityManagement";
	}

	@RequestMapping(value = "/system/resourceManagement", method = RequestMethod.GET)
	public String resourceManagement() {
		return "/admin/system/resourceManagement";
	}

	@RequestMapping(value = "/system/systemProperty", method = RequestMethod.GET)
	public String systemProperty() {
		return "/admin/system/systemProperty";
	}

	
	

	

}