package edu.buaa.sem.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.buaa.sem.po.SysAuthorityResource;
import edu.buaa.sem.po.SysResource;
import edu.buaa.sem.system.model.DatagridModel;
import edu.buaa.sem.system.service.SysAuthorityResourceService;
import edu.buaa.sem.system.service.SysResourceService;

@Controller
@RequestMapping("/admin/system/resource")
public class SysResourceController {

	@Autowired
	private SysResourceService sysResourceService;
	@Autowired
	private SysAuthorityResourceService sysAuthorityResourceService;

	/**
	 * 根据分页参数查询所有信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findByExampleForPagination")
	@ResponseBody
	public Map<String, Object> findByExampleForPagination(DatagridModel model,
			SysResource pojo) {
		List<SysResource> pojos = sysResourceService
				.findByExampleForPagination(pojo, model.getPage(),
						model.getRows(), model.getSort(), model.getOrder());
		long count = sysResourceService.countByExample(pojo);
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
	public String save(SysResource pojo) {
		sysResourceService.save(pojo);
		return "";
	}

	/**
	 * 修改一条记录
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public String update(SysResource pojo) {
		sysResourceService.update(pojo);
		return "";
	}

	// /**
	// * 根据上传的Excel文件批量导入数据库
	// */
	// @RequestMapping(value = "import")
	// @ResponseBody
	// public String importFromExcel(File file) {
	// sysResourceService.importFromExcel(file);
	// return "";
	// }

	// /**
	// * 导出数据到Excel
	// */
	// @RequestMapping(value = "export")
	// @ResponseBody
	// public void exportToExcel(SysUser pojo) {
	// sysResourceService.exportToExcel(pojo);
	// }

	/**
	 * 删除一条或多条记录
	 */
	@RequestMapping(value = "/deleteByKeys")
	@ResponseBody
	public String deleteByKeys(String idCommaString) {
		sysResourceService.deleteByKeys(idCommaString);
		sysAuthorityResourceService.deleteByKeys("sysResourceId",
				idCommaString.split(","), "long");
		return "";
	}

	/**
	 * 更新一条或多条记录
	 */

	@RequestMapping(value = "/updateByKeys")
	@ResponseBody
	public String updateByKeys(SysResource pojo, String idCommaString) {
		sysResourceService.updateByKeys(pojo, idCommaString);
		return "";
	}

	/**
	 * 根据sysAuthorityId查找其对应的资源情况，并返回json
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findAuthorityResourceByAuthorityIdForPagination")
	@ResponseBody
	public Map<String, Object> findAuthorityResourceByAuthorityIdForPagination(
			DatagridModel model, SysResource pojo, long authorityId) {
		pojo.setEnabled("是");
		// 查询相应的分页resource列表，以供特定authority勾选
		List<SysResource> pojos = sysResourceService
				.findByExampleForPagination(pojo, model.getPage(),
						model.getRows(), model.getSort(), model.getOrder());
		long count = sysResourceService.countByExample(pojo);

		// 查询特定resource与authority的关联情况
		List<SysAuthorityResource> jointPojos = sysAuthorityResourceService
				.findByPropertyEqual("sysAuthorityId",
						String.valueOf(authorityId), "long");

		// 遍历pojos（resourceList）,对每个resource查看是否已与选定的authority关联上了，再将需要的属性放入hashMap中
		List<HashMap<String, Object>> pojoList = sysResourceService
				.matchResourceAuthority(pojos, jointPojos);

		HashMap<String, Object> responseJson = new HashMap<String, Object>();
		responseJson.put("total", count);
		responseJson.put("rows", pojoList);

		return responseJson;
	}

	/**
	 * 根据权限ID查询已关联的资源
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findAllByAuthorityId")
	@ResponseBody
	public Map<String, Object> findAllByAuthorityId(long authorityId) {
		List<SysAuthorityResource> jointPojos = sysAuthorityResourceService
				.findByPropertyEqual("sysAuthorityId",
						String.valueOf(authorityId), "long");
		List<HashMap<String, Object>> pojoList = sysResourceService
				.matchResourceAuthority(jointPojos);
		HashMap<String, Object> responseJson = new HashMap<String, Object>();
		responseJson.put("total", 0);
		responseJson.put("rows", pojoList);

		return responseJson;
	}
}
