package edu.buaa.sem.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.buaa.sem.po.SysAuthorityResource;
import edu.buaa.sem.system.service.SysAuthorityResourceService;

@Controller
@RequestMapping("/admin/system/authorityResource")
public class SysAuthorityResourceController {

	@Autowired
	private SysAuthorityResourceService sysAuthorityResourceService;

	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public String saveOrUpdate(SysAuthorityResource pojo) {
		sysAuthorityResourceService.saveOrUpdate(pojo);
		return "";
	}

	@RequestMapping(value = "/deleteByKeys")
	@ResponseBody
	public String deleteByKeys(String idCommaString) {
		sysAuthorityResourceService.deleteByKeys("id",
				idCommaString.split(","), "long");
		return "";
	}
}
