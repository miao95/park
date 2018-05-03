package edu.buaa.sem.system.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import edu.buaa.sem.common.BaseService;
import edu.buaa.sem.common.MyUser;
import edu.buaa.sem.po.SysRole;
import edu.buaa.sem.po.SysUser;
import edu.buaa.sem.po.SysUserRole;
import edu.buaa.sem.system.dao.SysRoleDao;
import edu.buaa.sem.system.dao.SysUserDao;
import edu.buaa.sem.utils.EncryptionUtils;

@Service
public class SysUserService extends BaseService {

	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleDao sysRoleDao;

	/**
	 * 根据邮箱生成用户名
	 * 
	 * @param email
	 * @return
	 */
	public String generateUserNameByEmail(String email) {
		int i = email.indexOf('@');
		String name = "";
		if (i > 0) {
			name = email.substring(0, i);
		}

		while (findByUserName(name) != null) {
			Random random = new Random();
			int suffix = 1000 + random.nextInt(1000);
			name += suffix;
		}

		return name;
	}

	public List<SysUser> findByExampleForPagination(SysUser pojo, String page,
			String rows, String sort, String order) {
		List<SysUser> pojos;
		if (sort != null && !sort.equals("") && order != null
				&& !order.equals("")) {
			pojos = sysUserDao.findByExampleForPagination(pojo, true, page,
					rows, sort, order);
		} else {
			pojos = sysUserDao.findByExampleForPagination(pojo, true, page,
					rows, "name", "asc");
		}
		return pojos;
	}

	public long countByExample(SysUser pojo) {
		long count = sysUserDao.countByExample(pojo, true);
		return count;
	}

	public List<SysUser> findByExampleForPaginationWithCompany(SysUser pojo,
			String page, String rows, String sort, String order,
			HashMap<String, Object> conditionString) {
		List<SysUser> pojos;
		if (sort != null && !sort.equals("") && order != null
				&& !order.equals("")) {
			pojos = sysUserDao.findByExampleForPaginationWithComnapy(pojo,
					true, page, rows, sort, order, conditionString);
		} else {
			pojos = sysUserDao.findByExampleForPaginationWithComnapy(pojo,
					true, page, rows, "name", "asc", conditionString);
		}
		return pojos;
	}

	public long countByExampleWithCompany(SysUser pojo,
			HashMap<String, Object> conditionString) {
		long count = sysUserDao.countByExampleWithComnapy(pojo, true,
				conditionString);
		return count;
	}

	/**
	 * 岸端管理员查询方法
	 * 
	 * @param pojo
	 * @param conditionString
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	public HashMap<String, Object> findByParamsFoAdminrPagination(SysUser pojo,
			HashMap conditionString, String page, String rows, String sort,
			String order) {
		List list;
		if (sort != null && !sort.equals("") && order != null
				&& !order.equals("")) {
			list = sysUserDao.findByParamsForAdminPagination(pojo,
					conditionString, page, rows, sort, order);
		} else {
			list = sysUserDao.findByParamsForAdminPagination(pojo,
					conditionString, page, rows, "name", "asc");
		}
		long count = sysUserDao.countAdminByParams(pojo, conditionString);
		List<HashMap> pojoList = new ArrayList<HashMap>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();

			SysUser sysUser = (SysUser) obj[0];
			//InfShippingCompany company = (InfShippingCompany) obj[1];
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", sysUser.getId());
			map.put("name", sysUser.getName());
			map.put("email", sysUser.getEmail());
			map.put("password", sysUser.getPassword());
			map.put("checkStatus", sysUser.getCheckStatus());
			map.put("selfIntroduction", sysUser.getSelfIntroduction());
			map.put("creatTime", sysUser.getCreatTime());
			map.put("headImageUrl", sysUser.getHeadImageUrl());
			map.put("userType", sysUser.getUserType());
			map.put("description", sysUser.getDescription());
			map.put("enabled", sysUser.getEnabled());
			/*if (company != null) {
				map.put("companyName", company.getCompanyName());
				map.put("companyId", company.getId());
			}*/

			pojoList.add(map);
		}
		HashMap<String, Object> responseJson = new HashMap<String, Object>();
		responseJson.put("total", count);
		responseJson.put("rows", pojoList);
		return responseJson;
	}

	public HashMap<String, Object> findByParamsForPagination(SysUser pojo,
			HashMap conditionString, String page, String rows, String sort,
			String order) {
		List list;

		/*if (!this.isShipAdmin()) {
			List<Long> companyIds = findEnabledCompanyIdsWithAuth();
			conditionString.put("companyIds", companyIds);
		} else {*/
			String queryString2 = "from SysUser as a ,InfCrew as b where a.staffIdCard = b.cardNumber";
			conditionString.put("sqlString", queryString2);
			String queryString3 = "select count(*) from SysUser as a ,InfCrew as b where a.staffIdCard = b.cardNumber";
			conditionString.put("sqlCountString", queryString3);
		/*}*/
		if (sort != null && !sort.equals("") && order != null
				&& !order.equals("")) {
			list = sysUserDao.findByParamsForPagination(pojo, conditionString,
					page, rows, sort, order);
		} else {
			list = sysUserDao.findByParamsForPagination(pojo, conditionString,
					page, rows, "name", "asc");
		}
		long count = sysUserDao.countByParams(pojo, conditionString);
		List<HashMap> pojoList = new ArrayList<HashMap>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			SysUser sysUser = (SysUser) obj[0];
			//InfCrew crew = (InfCrew) obj[1];
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", sysUser.getId());
			map.put("name", sysUser.getName());
			map.put("email", sysUser.getEmail());
			map.put("password", sysUser.getPassword());
			map.put("checkStatus", sysUser.getCheckStatus());
			map.put("selfIntroduction", sysUser.getSelfIntroduction());
			map.put("creatTime", sysUser.getCreatTime());
			map.put("headImageUrl", sysUser.getHeadImageUrl());
			map.put("userType", sysUser.getUserType());
			map.put("description", sysUser.getDescription());
			map.put("enabled", sysUser.getEnabled());
			/*if (crew != null) {
				map.put("realName", crew.getName());
				map.put("sex", crew.getSex());
				map.put("idCard", crew.getCardNumber());
				map.put("mobile", crew.getPhoneContact());
			}*/

			pojoList.add(map);
		}
		HashMap<String, Object> responseJson = new HashMap<String, Object>();
		responseJson.put("total", count);
		responseJson.put("rows", pojoList);
		return responseJson;
	}

	public void save(SysUser pojo) {
		sysUserDao.save(pojo);
	}

	public void update(SysUser pojo) {
		sysUserDao.update(pojo);
	}

	public void merge(SysUser pojo) {
		sysUserDao.merge(pojo);
	}

	public void cancelByKeys(String idCommaString) {

		Logger logger = Logger.getLogger(SysUserService.class);
		SysUser userCurrent = this.getCurrentUser();
		if (idCommaString != null && !idCommaString.equals("")) {
			String[] userids = idCommaString.split(",");
			for (int i = 0; i < userids.length; i++) {
				SysUser pojo = this.findByKey(Long.parseLong(userids[i]));
				String userName = pojo.getName();
				String userEmail = pojo.getEmail();
				// pojo.setName("**(已注销)");
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String cancelTime = df.format(new Date());
				pojo.setDescription(cancelTime + "_" + userName + "_"
						+ userEmail + "_被注销");
				pojo.setEnabled("否");
				pojo.setEmail(RandomStringUtils.randomAlphanumeric(32));
				pojo.setName(RandomStringUtils.randomAlphanumeric(32));
				sysUserDao.save(pojo);
				logger.info(userCurrent.getName() + " cancel commonUser id="
						+ pojo.getId().toString() + " and name=" + userName
						+ " successful");

			}
		}
	}

	public void deleteByKeys(String idCommaString) {
		Logger logger = Logger.getLogger(SysUserService.class);
		SysUser userCurrent = this.getCurrentUser();
		if (idCommaString != null && !idCommaString.equals("")) {
			String[] userids = idCommaString.split(",");
			for (int i = 0; i < userids.length; i++) {
				SysUser pojo = this.findByKey(Long.parseLong(userids[i]));
				sysUserDao.delete(pojo);
				logger.info(userCurrent.getName() + "delete commonUser id="
						+ pojo.getId().toString() + "and email="
						+ pojo.getEmail() + "successful");
			}
		}

	}

	public void updateByKeys(SysUser pojo, String idCommaString) {
		sysUserDao.updateByKeys(pojo, "id", handleToIdLongArray(idCommaString));
	}

	public List<SysUser> findSysUserByExample(SysUser pojo) {
		return sysUserDao.findByExample(pojo, false);
	}

	public List<SysUser> findSysUserByExampleEnableLike(SysUser pojo) {
		return sysUserDao.findByExample(pojo, true);
	}

	public SysUser findByKey(long id) {
		return sysUserDao.findByKey(id);
	}

	/**
	 * 对所有SysUser按照关联实体SysUserRole进行匹配标记
	 */
	public List<HashMap<String, Object>> matchUserRole(List<SysUser> pojos,
			List<SysUserRole> jointPojos) {
		List<HashMap<String, Object>> pojoList = new ArrayList<>();
		for (int i = 0; i < pojos.size(); i++) {
			int flag = 0;// 用于标注此user是否已经与role关联，关联则为1，否则为0。每次对userList循环后要将flag重新赋值
			long relevanceId = 0;// 用于记录关联上的user与role关系的记录的自增id。如果最后放入json中为0，说明对于这个user，这个指定的role没有与他关联，所以在日后的save时，就根据relevanceId，判断是要插入还是更新。

			SysUser pojo = pojos.get(i);
			for (int j = 0; j < jointPojos.size() && flag == 0; j++) {
				SysUserRole jointPojo = jointPojos.get(j);

				if (jointPojo.getSysUserId().equals(pojo.getId())) {
					// 已经关联上，则将标志改为1
					flag = 1;
					// 已经关联上，则将关联记录的id记录下来，并放入json（以便在日后的存储是可以判断是进行插入还是更新）
					relevanceId = jointPojo.getId();
				}
			}

			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("id", pojo.getId());
			hashMap.put("name", pojo.getName());
			hashMap.put("description", pojo.getDescription());
			hashMap.put("relevance", flag);
			hashMap.put("relevanceId", relevanceId);

			pojoList.add(hashMap);
		}
		return pojoList;
	}

	/**
	 * 前台表单验证函数
	 * 
	 * @param pojo
	 * @return
	 */
	public String checkRegistForm(SysUser pojo) {
		if (pojo.getName() == null || pojo.getName().trim().length() == 0
				|| pojo.getName().trim().length() > 30) {
			return "用户名不正确";
		}

		// TODO待定
		/*
		 * // 密码只能是字母、数字、点和下划线，长度6至30 String regex = "^[a-z0-9A-Z_\\.]{6,30}$";
		 * if (pojo.getPassword() == null || !Pattern.matches(regex,
		 * pojo.getPassword().trim())) { return "密码不正确"; }
		 */

		if (!EmailValidator.getInstance().isValid(pojo.getEmail().trim())) {
			return "邮箱不正确";
		}

		if (pojo.getEmail().trim().length() > 30) {
			return "邮箱不正确";
		}

		if (findByUserName(pojo.getName().trim().toLowerCase()) != null) {
			return "该用户名已被注册";
		}

		if (findByEmail(pojo.getEmail().trim()) != null) {
			return "该邮箱已被注册";
		}

		return "pass";
	}

	/**
	 * 保存用户头像进入数据库和SpringSecuritySession
	 */
	public boolean saveHeadImgPath(String headImgPath, SysUser pojo) {
		if (pojo != null) {
			pojo.setHeadImageUrl(headImgPath);
			Object userDetails = SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			if (userDetails instanceof UserDetails) {
				((MyUser) userDetails).setHeadUrl(headImgPath);
			} else {
				return false;
			}
			this.save(pojo);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 更新当前用户的公司管理权限SpringSecuritySession
	 */
	public boolean saveCompanyIds(Long newCompanyId) {
		if (newCompanyId != null) {
			Object userDetails = SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			if (userDetails instanceof UserDetails) {
				//List<Long> companyIds = ((MyUser) userDetails).getCompanyIds();
				//companyIds.add(newCompanyId);
				//((MyUser) userDetails).setCompanyIds(companyIds);
			} else {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查找当前用户role并返回角色名称字符串
	 * 
	 * @return
	 */
	public String findCurrentUserRole() {
		SysUser user = this.getCurrentUser();
		if (user != null) {
			List<SysUserRole> userRoleList = sysUserRoleService
					.findByPropertyEqual("sysUserId",
							String.valueOf(user.getId()), "long");

			List<String> rolesList = new ArrayList<String>();
			Iterator it = userRoleList.iterator();
			while (it.hasNext()) {
				SysUserRole sysUserRole = (SysUserRole) it.next();
				SysRole rolePojo = sysRoleDao.findByKey(sysUserRole
						.getSysRoleId());
				rolesList.add(rolePojo.getName());
			}
			return StringUtils.join(rolesList, ",");
		}
		return null;
	}

	public SysUser findByUserName(String name) {
		List<SysUser> pojos = sysUserDao.findByPropertyEqual("name", name,
				"String");
		if (pojos != null && pojos.size() == 1) {
			return pojos.get(0);
		} else {
			return null;
		}
	}

	public SysUser findByEmail(String email) {
		List<SysUser> pojos = sysUserDao.findByPropertyEqual("email", email,
				"String");
		if (pojos != null && pojos.size() == 1) {
			return pojos.get(0);
		} else {
			return null;
		}
	}

	public SysUser findByEmailCode(String emailCode) {
		List<SysUser> pojos = sysUserDao.findByPropertyEqual("emailCode",
				emailCode, "String");
		if (pojos != null && pojos.size() == 1) {
			return pojos.get(0);
		} else {
			return null;
		}
	}

	public List<SysUser> findAll() {
		return sysUserDao.findAll();
	}

	
	

	/**
	 * 创建船员生成普通用户
	 * 
	 * @param idNumber
	 * @param email
	 * @param crewId
	 * @return
	 */
	public String generateUserFromCrew(String idNumber, String email,
			Long crewId) {

		SysUser pojo = new SysUser();
		if (!idNumber.equals("") && !idNumber.equals("") && crewId != null) {
			// 如果存在用户，则更新关联船员主键
			SysUser userCheck = findByUserName(idNumber.trim());
			if (userCheck != null) {
				userCheck.setStaffRelate(crewId);
				sysUserDao.saveOrUpdate(userCheck);
				return E.SUCCESS();
			}
			String password = idNumber.substring(idNumber.length() - 8,
					idNumber.length());
			// String name = generateUserNameByEmail(email);

			String name = idNumber.trim();
			pojo.setPassword(EncryptionUtils.getMD5(password));// 添加时要对密码加密
			pojo.setName(name);
			if (email != null && !email.equals("")) {
				pojo.setEmail(email.toLowerCase());
			}
			pojo.setEnabled("否");
			pojo.setUserType("普通用户");
			pojo.setDescription("（创建船员自动生成）");
			pojo.setHeadImageUrl("/file/avatar/default-avatar.jpg");
			pojo.setStaffRelate(crewId);// 关联船员表主键
			//pojo.setStaffIdCard(idNumber);// 关联船员id
			pojo.setCreatTime(new Date());
			sysUserDao.save(pojo);

			SysUserRole temp = new SysUserRole();
			temp.setSysUserId(pojo.getId());
			temp.setSysRoleId(2l);// 默认为普通用户
			temp.setEnabled("是");
			sysUserRoleService.saveOrUpdate(temp);
			return E.SUCCESS();
		}
		return E.ERROR();
	}

	/**
	 * 根据身份证号查找用户
	 * 
	 * @param cardNum
	 * @return
	 */
	public SysUser findByCrewIdCard(String cardNum) {
		if (cardNum != null && cardNum.equals("")) {
			List<SysUser> pojoList = sysUserDao.findByPropertyEqual(
					"staffIdCard", cardNum, "String");
			if (pojoList != null && pojoList.size() != 0) {
				SysUser user = pojoList.get(0);
				return user.getEnabled().equals("是") ? user : null;
			}
			return null;
		}
		return null;

	}


	

	
	/**
	 * 生成邮件校验码，用户重置密码
	 * 
	 * @return
	 */
	public String generateEmailCode() {
		String code = RandomStringUtils.randomAlphanumeric(8);

		while (findByEmailCode(code) != null) {
			code = RandomStringUtils.randomAlphanumeric(8);
		}

		return code;
	}

	/**
	 * 设为实名用户
	 * 
	 * @param idCommaString
	 */
	public void toRealUser(String idCommaString) {
		SysUser pojo = new SysUser();
		pojo.setCheckStatus("是");
		pojo.setUserType("实名用户");
		updateByKeys(pojo, idCommaString);

		// 删除原有角色
		sysUserRoleService.deleteByKeys("sysUserId", idCommaString.split(","),
				"long");

		// 添加实名用户角色
		String[] ids = idCommaString.split(",");
		for (String id : ids) {
			SysUserRole role = new SysUserRole(Long.valueOf(id), 18l, "是");
			sysUserRoleService.saveOrUpdate(role);
		}
	}

	public List<SysUser> findById2(String typeNumber) {
		SysUserDao sysUserDao = new SysUserDao();
		return sysUserDao.findById2(typeNumber);
	}

	/**
	 * 判断用户是否为管理员
	 * 
	 * @return
	 */
	public boolean isAdmin() {
		String roles = this.findCurrentUserRole();
		String[] rolesList = roles.split(",");
		for (String s : rolesList) {
			return !s.equals("ROLE_NORMAL");
		}
		return true;
	}

	/**
	 * 判断用户是否为超级管理员
	 * 
	 * @return
	 */
	public boolean isSuperAdmin() {
		String roles = this.findCurrentUserRole();
		String[] rolesList = roles.split(",");
		for (String s : rolesList) {
			if (s.equals("ROLE_ADMIN_ADMINISTRATOR")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断用户是否为普通用户
	 * 
	 * @return
	 */
	public boolean isNormalUser() {
		String roles = this.findCurrentUserRole();
		String[] rolesList = roles.split(",");
		for (String s : rolesList) {
			if (s.equals("ROLE_NORMAL")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据属性查找，手动创建Dao和session
	 * 
	 * @author YY
	 * @param propertyName
	 * @param value
	 * @param type
	 * @return
	 */
	public List<SysUser> findByPropertyEqualWithNoAutowired(
			String propertyName, String value, String type) {
		SysUserDao sysUserDao = new SysUserDao();
		return sysUserDao.findByPropertyEqualWithNoAutowired(propertyName,
				value, type);
	}

	/**
	 * 手动创建session 保存用户
	 * 
	 * @author YY
	 * @param sysUser
	 */
	public void saveWithNoAutowired(SysUser sysUser) {
		SysUserDao sysUserDao = new SysUserDao();
		sysUserDao.saveWithNoAutowired(sysUser);
	}
}
