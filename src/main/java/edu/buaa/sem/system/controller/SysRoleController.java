package edu.buaa.sem.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.buaa.sem.po.SysRole;
import edu.buaa.sem.po.SysRoleAuthority;
import edu.buaa.sem.po.SysUserRole;
import edu.buaa.sem.system.model.DatagridModel;
import edu.buaa.sem.system.service.SysRoleAuthorityService;
import edu.buaa.sem.system.service.SysRoleService;
import edu.buaa.sem.system.service.SysUserRoleService;

@Controller
@RequestMapping("/admin/system/role")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleAuthorityService sysRoleAuthorityService;

	@RequestMapping(value = "/findByExampleForPagination")
	@ResponseBody
	public Map<String, Object> findByExampleForPagination(DatagridModel model,
			SysRole pojo) {
		List<SysRole> pojos = sysRoleService.findByExampleForPagination(pojo,
				model.getPage(), model.getRows(), model.getSort(),
				model.getOrder());
		long count = sysRoleService.countByExample(pojo);
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
	public String save(SysRole pojo) {
		sysRoleService.save(pojo);
		return "";
	}

	/**
	 * 修改一条记录
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public String update(SysRole pojo) {
		sysRoleService.update(pojo);
		return "";
	}

	// /**
	// * 根据上传的Excel文件批量导入数据库
	// */
	// public String importFromExcel() {
	// sysRoleService.importFromExcel(file);
	// return SUCCESS;
	// }

	// /**
	// * 导出数据到Excel
	// */
	// public void exportToExcel() {
	// sysRoleService.exportToExcel(pojo);
	// }

	// /**
	// * 删除一条或多条记录
	// */
	@RequestMapping(value = "/deleteByKeys")
	@ResponseBody
	public String deleteByKeys(String idCommaString) {
		sysRoleService.deleteByKeys(idCommaString);
		sysUserRoleService.deleteByKeys("sysRoleId", idCommaString.split(","),
				"long");
		sysRoleAuthorityService.deleteByKeys("sysRoleId",
				idCommaString.split(","), "long");
		return "";
	}

	/**
	 * 更新一条或多条记录
	 */
	@RequestMapping(value = "/updateByKeys")
	@ResponseBody
	public String updateByKeys(SysRole pojo, String idCommaString) {
		sysRoleService.updateByKeys(pojo, idCommaString);
		return "";
	}

	/**
	 * 根据sysUserId查找其对应的角色情况，并返回json
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findUserRoleByUserIdForPagination")
	@ResponseBody
	public Map<String, Object> findUserRoleByUserIdForPagination(
			DatagridModel model, SysRole pojo, long userId) {
		// 查询相应的分页role列表，以供特定user勾选
		pojo.setEnabled("是");
		List<SysRole> pojos = sysRoleService.findByExampleForPagination(pojo,
				model.getPage(), model.getRows(), model.getSort(),
				model.getOrder());
		long count = sysRoleService.countByExample(pojo);

		// 查询特定user与role的关联情况
		List<SysUserRole> jointPojos = sysUserRoleService.findByPropertyEqual(
				"sysUserId", String.valueOf(userId), "long");

		// 遍历pojos（roleList）,对每个role查看是否已与选定的user关联上了，再将需要的属性放入hashMap中
		List<HashMap<String, Object>> pojoList = sysRoleService.matchRoleUser(
				pojos, jointPojos);

		HashMap<String, Object> responseJson = new HashMap<String, Object>();
		responseJson.put("total", count);
		responseJson.put("rows", pojoList);

		return responseJson;
	}

	/**
	 * 根据sysAuthorityId查找其对应的角色情况，并返回json
	 * 
	 * @return
	 */

	@RequestMapping(value = "/findRoleAuthorityByAuthorityIdForPagination")
	@ResponseBody
	public Map<String, Object> findRoleAuthorityByAuthorityIdForPagination(
			DatagridModel model, SysRole pojo, long authorityId) {
		pojo.setEnabled("是");
		// 查询相应的分页role列表，以供特定authority勾选
		List<SysRole> pojos = sysRoleService.findByExampleForPagination(pojo,
				model.getPage(), model.getRows(), model.getSort(),
				model.getOrder());
		long count = sysRoleService.countByExample(pojo);

		// 查询特定role与authority的关联情况
		List<SysRoleAuthority> jointPojos = sysRoleAuthorityService
				.findByPropertyEqual("sysAuthorityId",
						String.valueOf(authorityId), "long");

		// 遍历pojos（roleList）,对每个role查看是否已与选定的authority关联上了，再将需要的属性放入hashMap中
		List<HashMap<String, Object>> pojoList = sysRoleService
				.matchRoleAuthority(pojos, jointPojos);

		HashMap<String, Object> responseJson = new HashMap<String, Object>();
		responseJson.put("total", count);
		responseJson.put("rows", pojoList);

		return responseJson;
	}
}
