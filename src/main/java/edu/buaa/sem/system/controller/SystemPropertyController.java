package edu.buaa.sem.system.controller;

//Auto Genereted 2015-5-18 11:39:46

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.buaa.sem.po.SystemProperty;
import edu.buaa.sem.system.service.SystemPropertyService;

@Controller
@RequestMapping("/admin/system/systemproperty")
public class SystemPropertyController {

	@Autowired
	private SystemPropertyService systemPropertyService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String getProperty(Model model) {

		model.addAttribute("data", systemPropertyService.getProperty());
		return "/admin/system/systemProperty";
	}

	@RequestMapping(value = "/setProperty", method = RequestMethod.POST)
	@ResponseBody
	public String setProperty(SystemProperty pojo) {
		systemPropertyService.setProperty(pojo);
		return "success";
	}

	@RequestMapping(value = "/changeLoggerLevel", method = RequestMethod.POST)
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
}
