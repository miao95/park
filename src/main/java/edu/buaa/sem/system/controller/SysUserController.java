package edu.buaa.sem.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.buaa.sem.common.BaseService.E;
import edu.buaa.sem.po.SysUser;
import edu.buaa.sem.po.SysUserRole;
import edu.buaa.sem.system.model.DatagridModel;
import edu.buaa.sem.system.model.UserModel;
import edu.buaa.sem.system.service.SysUserRoleService;
import edu.buaa.sem.system.service.SysUserService;
import edu.buaa.sem.utils.EncryptionUtils;
import edu.buaa.sem.utils.PropertiesUtils;

@Controller
@RequestMapping("/admin/system/user")
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 船端查找管理员方法
	 * 
	 * @param model
	 * @param pojo
	 * @return
	 */
	@RequestMapping(value = "/adminuserShip")
	@ResponseBody
	public Map<String, Object> findAdminUserByExampleForPagination(
			DatagridModel model, SysUser pojo) {
		pojo.setUserType("系统用户");
		List<SysUser> pojos = sysUserService.findByExampleForPagination(pojo,
				model.getPage(), model.getRows(), model.getSort(),
				model.getOrder());
		long count = sysUserService.countByExample(pojo);
		HashMap<String, Object> responseJson = new HashMap<String, Object>();
		responseJson.put("total", count);
		responseJson.put("rows", pojos);
		return responseJson;
	}

	/**
	 * 岸端查找管理员方法
	 * 
	 * @param model
	 * @param pojo
	 * @return
	 */
	@RequestMapping(value = "/adminuser")
	@ResponseBody
	public Map<String, Object> findAdminUserByExampleForAdminPagination(
			DatagridModel model,
			SysUser pojo,
			@RequestParam(value = "companyIds[]", required = false) String[] companyIdsString) {
		pojo.setUserType("系统用户");
		HashMap<String, Object> conditionString = new HashMap<String, Object>();
		if (companyIdsString != null && companyIdsString.length != 0) {
			List<Long> idsList = new ArrayList();
			for (int i = 0; i < companyIdsString.length; i++) {
				idsList.add(Long.parseLong(companyIdsString[i]));
			}
			conditionString.put("companyIds", idsList);
		} else {
			// 如果是超级管理员，就可以全查
			//conditionString.put("companyIds",sysUserService.findEnabledCompanyIdsWithAuth());
		}
		HashMap<String, Object> responseJson = sysUserService
				.findByParamsFoAdminrPagination(pojo, conditionString,
						model.getPage(), model.getRows(), model.getSort(),
						model.getOrder());
		return responseJson;
	}

	@RequestMapping(value = "/CommonUser")
	@ResponseBody
	public Map<String, Object> findCommonUserByExampleForPagination(
			DatagridModel model,
			SysUser pojo,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "idCard", required = false) String cardNumber,
			@RequestParam(value = "realName", required = false) String realName,
			@RequestParam(value = "mobile", required = false) String mobile) {
		pojo.setUserType("普通用户");
		HashMap<String, Object> conditionString = new HashMap<String, Object>();
		if (sex != null && !sex.isEmpty()) {
			conditionString.put("sex", sex);
		}
		if (cardNumber != null && !cardNumber.isEmpty()) {
			conditionString.put("cardNumber", cardNumber);
		}
		if (realName != null && !realName.isEmpty()) {
			conditionString.put("realName", realName);
		}
		if (mobile != null && !mobile.isEmpty()) {
			conditionString.put("mobile", mobile);
		}

		HashMap<String, Object> responseJson = sysUserService
				.findByParamsForPagination(pojo, conditionString,
						model.getPage(), model.getRows(), model.getSort(),
						model.getOrder());
		return responseJson;
	}

	/**
	 * 添加一条记录
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public String save(UserModel model) {
		SysUser pojo = new SysUser();
		Properties properties = PropertiesUtils
				.getProperties("system.properties");
		String pwdRegex = properties.getProperty("adminPwdRegex");
		if (!model.getPassword().matches(pwdRegex)) {
			return "weakPwd";
		}

		pojo.setPassword(EncryptionUtils.getMD5(model.getPassword()));// 添加时要对密码加密
		pojo.setName(model.getName().trim().toLowerCase());
		pojo.setEmail(model.getEmail().trim().toLowerCase());
		//pojo.setCompanyId(model.getCompanyId());
		String des = model.getDescription();
		String enabled = model.getEnabled();
		if (des == null || des.isEmpty()) {
			pojo.setDescription("(系统保留用户名)");
		} else {
			pojo.setDescription(model.getDescription());
		}
		if (enabled == null || enabled.isEmpty()) {
			pojo.setEnabled("否");
		} else {
			pojo.setEnabled(model.getEnabled());
		}
		pojo.setUserType("系统用户");
		sysUserService.save(pojo);

		SysUserRole temp = new SysUserRole();
		temp.setSysUserId(pojo.getId());
		temp.setSysRoleId(10l);// 默认为普通用户
		temp.setEnabled("是");
		sysUserRoleService.saveOrUpdate(temp);

		return "";
	}

	/**
	 * 添加一条普通用户记录
	 */
	@RequestMapping(value = "/saveCommonUser")
	@ResponseBody
	public String saveCommonUser(UserModel model) {
		Logger logger = Logger.getLogger(SysUserController.class);
		SysUser pojo = new SysUser();
		Properties properties = PropertiesUtils
				.getProperties("system.properties");
		String pwdRegex = properties.getProperty("adminPwdRegex");
		if (!model.getPassword().matches(pwdRegex)) {
			return "weakPwd";
		}

		pojo.setPassword(EncryptionUtils.getMD5(model.getPassword()));// 添加时要对密码加密
		pojo.setName(model.getName().trim().toLowerCase());
		pojo.setEmail(model.getEmail().trim().toLowerCase());
		String des = model.getDescription();
		String enabled = model.getEnabled();

		pojo.setDescription(des);

		if (enabled == null || enabled.isEmpty()) {
			pojo.setEnabled("否");
		} else {
			pojo.setEnabled(model.getEnabled());
		}
		pojo.setUserType("普通用户");
		sysUserService.save(pojo);
		if (pojo.getId() != null) {
			logger.info("add commonUser id=" + pojo.getId().toString()
					+ "and email =" + pojo.getEmail() + "successful");
		}

		SysUserRole temp = new SysUserRole();
		temp.setSysUserId(pojo.getId());
		temp.setSysRoleId(10l);// 默认为普通用户
		temp.setEnabled("是");
		sysUserRoleService.saveOrUpdate(temp);

		// TODO 专家相关字段初始化设置

		return "";
	}

	/**
	 * 删除一条或多条记录
	 */
	@RequestMapping(value = "/deleteCommonUser")
	@ResponseBody
	public String deleteByKeys(String idCommaString) {
		sysUserService.deleteByKeys(idCommaString);
		sysUserRoleService.deleteByKeys("sysUserId", idCommaString.split(","),
				"long");
		return E.SUCCESS();
	}

	@RequestMapping(value = "/userCancellation")
	@ResponseBody
	public String userCancellation(String idCommaString) {
		sysUserService.cancelByKeys(idCommaString);

		return E.SUCCESS();

	}

	/**
	 * 修改一条记录
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public String update(SysUser pojo) {

		boolean result = this.checkDuplication(pojo.getId(), pojo.getEmail(),
				pojo.getName());
		if (result) {
			return "repeat";
		} else {
			pojo.setName(pojo.getName().trim().toLowerCase());
			pojo.setEmail(pojo.getEmail().trim());
			pojo.setEmail(pojo.getEmail().trim());
			sysUserService.merge(pojo);
			return "";

		}
	}

	private boolean checkDuplication(long id, String email, String name) {
		SysUser pojo1 = sysUserService.findByEmail(email.trim());
		SysUser pojo2 = sysUserService.findByUserName(name.trim());

		if ((pojo1 != null && !pojo1.getId().equals(id))
				|| (pojo2 != null && !pojo2.getId().equals(id))) {
			return true;
		} else
			return false;

	}

	/**
	 * 重置密码
	 * 
	 * @return
	 */
	@RequestMapping(value = "/resetPassword")
	@ResponseBody
	public String resetPassword(UserModel model) {
		SysUser pojo = new SysUser();
		Properties properties = PropertiesUtils
				.getProperties("system.properties");
		String pwdRegex = properties.getProperty("adminPwdRegex");
		if (!model.getPassword().matches(pwdRegex)) {
			return "weakPwd";
		}
		pojo.setPassword(EncryptionUtils.getMD5(model.getPassword()));
		sysUserService.updateByKeys(pojo, model.getIdCommaString());
		return "";
	}

	/**
	 * 判断用户名是否注册过
	 * 
	 * @return
	 */
	@RequestMapping(value = "/checkUserName")
	@ResponseBody
	public Map<String, Object> checkUserName(UserModel model) {
		SysUser temp = new SysUser();
		temp.setName(model.getUserNameCheck().trim().toLowerCase());
		List<SysUser> list = sysUserService.findSysUserByExample(temp);

		SysUser temp1 = new SysUser();
		temp1.setEmail(model.getEmail().trim().toLowerCase());
		List<SysUser> list1 = sysUserService.findSysUserByExample(temp1);

		HashMap<String, Object> responseJson = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			responseJson.put("result", "true");// 代表用户名已经被注册过
		} else if (list1 != null && list1.size() > 0) {
			responseJson.put("result", "true");// 代表用户名已经被注册过
		} else {
			responseJson.put("result", "false");
		}
		return responseJson;
	}

	/**
	 * 禁用一条或多条记录
	 */
	@RequestMapping(value = "/disabled")
	@ResponseBody
	public String disabled(String idCommaString) {
		if (idCommaString != null && !idCommaString.isEmpty()) {
			String[] ids = idCommaString.split(",");
			for (int i = 0; i < ids.length; i++) {
				SysUser sysUser = sysUserService.findByKey(Long
						.parseLong(ids[i]));
				if (sysUser != null) {
					sysUser.setEnabled("否");
					sysUserService.update(sysUser);
				}
			}
		}
		return "";
	}

	/**
	 * 更新一条或多条记录
	 */
	@RequestMapping(value = "/updateByKeys")
	@ResponseBody
	public String updateByKeys(SysUser pojo, String idCommaString) {
		sysUserService.updateByKeys(pojo, idCommaString);
		return "";
	}

	/**
	 * 根据sysRoleId查找其对应的用户情况，并返回json
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findUserRoleByRoleIdForPagination")
	@ResponseBody
	public Map<String, Object> findUserRoleByRoleIdForPagination(
			DatagridModel model, String roleId) {
		SysUser pojo = new SysUser();
		// 查询相应的分页user列表，以供特定role勾选
		pojo.setUserType("系统用户");
		
		//配置公司权限
		HashMap<String, Object> conditionString = new HashMap<String, Object>();
		//conditionString.put("companyIds",sysUserService.findEnabledCompanyIdsWithAuth());
		
		List<SysUser> pojos = sysUserService.findByExampleForPaginationWithCompany(pojo,
				model.getPage(), model.getRows(), model.getSort(),
				model.getOrder(),conditionString);
		long count = sysUserService.countByExampleWithCompany(pojo,conditionString);

		// 查询特定user与role的关联情况
		List<SysUserRole> jointPojos = sysUserRoleService.findByPropertyEqual(
				"sysRoleId", String.valueOf(roleId), "long");

		// 遍历pojos（userList）,对每个user查看是否已与选定的role关联上了，再将需要的属性放入hashMap中
		List<HashMap<String, Object>> pojoList = sysUserService.matchUserRole(
				pojos, jointPojos);

		Map<String, Object> responseJson = new HashMap<String, Object>();
		responseJson.put("total", count);
		responseJson.put("rows", pojoList);

		return responseJson;
	}

	@RequestMapping(value = "/toRealUser")
	@ResponseBody
	public String toRealUser(String idCommaString) {
		sysUserService.toRealUser(idCommaString);
		return "";
	}

}
