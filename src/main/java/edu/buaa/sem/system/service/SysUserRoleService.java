package edu.buaa.sem.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.buaa.sem.common.BaseService;
import edu.buaa.sem.po.SysUserRole;
import edu.buaa.sem.system.dao.SysUserRoleDao;

@Service
public class SysUserRoleService extends BaseService {

	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private SysUserService sysUserService;

	public void saveOrUpdate(SysUserRole pojo) {
		sysUserRoleDao.saveOrUpdate(pojo);
	}

	public void deleteByKeys(String propertyName, String[] value, String type) {
		sysUserRoleDao.deleteByKeys(propertyName, value, type);
	}

	public List<SysUserRole> findByPropertyEqual(String propertyName,
			String value, String type) {
		List<SysUserRole> pojos = sysUserRoleDao.findByPropertyEqual(
				propertyName, value, type);
		return pojos;
	}

	public boolean isLegal(SysUserRole pojo) {
		if (pojo.getSysRoleId() != 3) {
			return true;
		}
		if (pojo.getSysRoleId() == 3 && sysUserService.isSuperAdmin()) {
			return true;
		}
		return false;
	}

	public void deleteLegalByPropertyValues(String type, String idCommaString) {
		if (type != null && type.equals("id")) {
			String[] ids = idCommaString.split(",");
			for (String id : ids) {
				SysUserRole pojo = sysUserRoleDao.findByKey(Long.parseLong(id));
				if (isLegal(pojo)) {
					sysUserRoleDao.delete(pojo);
				}
			}
		}
		if (type != null && type.equals("sysUserId")) {
			String[] sysUserId = idCommaString.split(",");
			for (String id : sysUserId) {
				List<SysUserRole> pojos = sysUserRoleDao.findByPropertyEqual(
						"sysUserId", id, "long");
				for (SysUserRole p : pojos) {
					if (isLegal(p)) {
						sysUserRoleDao.delete(p);
					}
				}
			}
		}
	}
}
