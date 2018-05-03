package edu.buaa.sem.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.buaa.sem.common.BaseService.E;
import edu.buaa.sem.po.SysUserRole;
import edu.buaa.sem.system.service.SysUserRoleService;

@Controller
@RequestMapping("/admin/system/userRole")
public class SysUserRoleController {

	@Autowired
	private SysUserRoleService sysUserRoleService;

	@RequestMapping(value = "/saveOrUpdate")
	@ResponseBody
	public String saveOrUpdate(SysUserRole pojo) {
		if (sysUserRoleService.isLegal(pojo)) {
			sysUserRoleService.saveOrUpdate(pojo);
			return E.SUCCESS();
		}
		return "ilegal";
	}

	@RequestMapping(value = "/deleteByKeys")
	@ResponseBody
	public String deleteByKeys(String idCommaString) {
		sysUserRoleService.deleteLegalByPropertyValues("id", idCommaString);
		return E.SUCCESS();
	}

	@RequestMapping(value = "/deleteBySysUserId")
	@ResponseBody
	public String deleteBySysUserId(String idCommaString) {
		sysUserRoleService.deleteLegalByPropertyValues("sysUserId",
				idCommaString);
		return E.SUCCESS();
	}
}
