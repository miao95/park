package edu.buaa.sem.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.buaa.sem.po.SysRoleAuthority;
import edu.buaa.sem.system.service.SysRoleAuthorityService;

@Controller
@RequestMapping("/admin/system/roleAuthority")
public class SysRoleAuthorityController {

	@Autowired
	private SysRoleAuthorityService sysRoleAuthorityService;

	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public String saveOrUpdate(SysRoleAuthority pojo) {
		sysRoleAuthorityService.saveOrUpdate(pojo);
		return "";
	}

	@RequestMapping(value = "/deleteByKeys")
	@ResponseBody
	public String deleteByKeys(String idCommaString) {
		sysRoleAuthorityService.deleteByKeys("id", idCommaString.split(","),
				"long");
		return "";
	}
}
