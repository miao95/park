package edu.buaa.sem.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.buaa.sem.po.SysAuthority;
import edu.buaa.sem.po.SysAuthorityResource;
import edu.buaa.sem.po.SysRoleAuthority;
import edu.buaa.sem.system.model.DatagridModel;
import edu.buaa.sem.system.service.SysAuthorityResourceService;
import edu.buaa.sem.system.service.SysAuthorityService;
import edu.buaa.sem.system.service.SysRoleAuthorityService;

@Controller
@RequestMapping("/admin/system/authority")
public class SysAuthorityController {

	@Autowired
	private SysAuthorityService sysAuthorityService;
	@Autowired
	private SysAuthorityResourceService sysAuthorityResourceService;
	@Autowired
	private SysRoleAuthorityService sysRoleAuthorityService;

	@RequestMapping(value = "/findByExampleForPagination")
	@ResponseBody
	public Map<String, Object> findByExampleForPagination(DatagridModel model,
			SysAuthority pojo) {
		List<SysAuthority> pojos = sysAuthorityService
				.findByExampleForPagination(pojo, model.getPage(),
						model.getRows(), model.getSort(), model.getOrder());
		long count = sysAuthorityService.countByExample(pojo);
		HashMap<String, Object> responseJson = new HashMap<String, Object>();
		responseJson.put("total", count);
		responseJson.put("rows", pojos);

		return responseJson;
	}

	/**
	 * 添加一条记录
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public String save(SysAuthority pojo) {
		sysAuthorityService.save(pojo);
		return "";
	}

	/**
	 * 修改一条记录
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public String update(SysAuthority pojo) {
		sysAuthorityService.update(pojo);
		return "";
	}

	// /**
	// * 根据上传的Excel文件批量导入数据库
	// */
	// public String importFromExcel() {
	// sysAuthorityService.importFromExcel(file);
	// return SUCCESS;
	// }
	//
	// /**
	// * 导出数据到Excel
	// */
	// public void exportToExcel() {
	// sysAuthorityService.exportToExcel(pojo);
	// }

	/**
	 * 删除一条或多条记录
	 */

	@RequestMapping(value = "/deleteByKeys")
	@ResponseBody
	public String deleteByKeys(String idCommaString) {
		sysAuthorityService.deleteByKeys(idCommaString);
		sysAuthorityResourceService.deleteByKeys("sysAuthorityId",
				idCommaString.split(","), "long");
		sysRoleAuthorityService.deleteByKeys("sysAuthorityId",
				idCommaString.split(","), "long");
		return "";
	}

	/**
	 * 更新一条或多条记录
	 */

	@RequestMapping(value = "/updateByKeys")
	@ResponseBody
	public String updateByKeys(SysAuthority pojo, String idCommaString) {
		sysAuthorityService.updateByKeys(pojo, idCommaString);
		return "";
	}

	/**
	 * 根据sysRoleId查找其对应的权限情况，并返回json
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findRoleAuthorityByRoleIdForPagination")
	@ResponseBody
	public Map<String, Object> findRoleAuthorityByRoleIdForPagination(
			DatagridModel model, SysAuthority pojo, long roleId) {
		pojo.setEnabled("是");
		// 查询相应的分页authority列表，以供特定role勾选
		List<SysAuthority> pojos = sysAuthorityService
				.findByExampleForPagination(pojo, model.getPage(),
						model.getRows(), model.getSort(), model.getOrder());
		long count = sysAuthorityService.countByExample(pojo);

		// 查询特定role与authority的关联情况
		List<SysRoleAuthority> jointPojos = sysRoleAuthorityService
				.findByPropertyEqual("sysRoleId", String.valueOf(roleId),
						"long");

		// 遍历pojos（authorityList）,对每个authority查看是否已与选定的role关联上了，再将需要的属性放入hashMap中
		List<HashMap<String, Object>> pojoList = sysAuthorityService
				.matchAuthorityRole(pojos, jointPojos);

		HashMap<String, Object> responseJson = new HashMap<>();
		responseJson.put("total", count);
		responseJson.put("rows", pojoList);

		return responseJson;
	}

	/**
	 * 根据sysResourceId查找其对应的权限情况，并返回json
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findAuthorityResourceByResourceIdForPagination")
	@ResponseBody
	public Map<String, Object> findAuthorityResourceByResourceIdForPagination(
			DatagridModel model, SysAuthority pojo, long resourceId) {
		pojo.setEnabled("是");
		// 查询相应的分页authority列表，以供特定resource勾选
		List<SysAuthority> pojos = sysAuthorityService
				.findByExampleForPagination(pojo, model.getPage(),
						model.getRows(), model.getSort(), model.getOrder());
		long count = sysAuthorityService.countByExample(pojo);

		// 查询特定authority与resource的关联情况
		List<SysAuthorityResource> jointPojos = sysAuthorityResourceService
				.findByPropertyEqual("sysResourceId",
						String.valueOf(resourceId), "long");

		// 遍历pojos（authorityList）,对每个authority查看是否已与选定的resource关联上了，再将需要的属性放入hashMap中
		List<HashMap<String, Object>> pojoList = sysAuthorityService
				.matchAuthorityResource(pojos, jointPojos);

		HashMap<String, Object> responseJson = new HashMap<>();
		responseJson.put("total", count);
		responseJson.put("rows", pojoList);

		return responseJson;
	}

	/**
	 * 根据角色ID查询已关联的权限
	 * 
	 * @return
	 */

	@RequestMapping(value = "/findAllByRoleId")
	@ResponseBody
	public Map<String, Object> findAllByRoleId(long roleId) {
		List<SysRoleAuthority> jointPojos = sysRoleAuthorityService
				.findByPropertyEqual("sysRoleId", String.valueOf(roleId),
						"long");
		List<HashMap<String, String>> pojoList = sysAuthorityService
				.matchAuthorityResource(jointPojos);
		HashMap<String, Object> responseJson = new HashMap<>();
		responseJson.put("total", 0);
		responseJson.put("rows", pojoList);

		return responseJson;
	}

}
