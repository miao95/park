package edu.buaa.sem.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.buaa.sem.common.BaseService;
import edu.buaa.sem.po.SysRoleAuthority;
import edu.buaa.sem.system.dao.SysRoleAuthorityDao;

@Service
public class SysRoleAuthorityService extends BaseService {

	@Autowired
	private SysRoleAuthorityDao sysRoleAuthorityDao;

	public void saveOrUpdate(SysRoleAuthority pojo) {
		sysRoleAuthorityDao.saveOrUpdate(pojo);
	}

	public void deleteByKeys(String propertyName, String[] value, String type) {
		sysRoleAuthorityDao.deleteByKeys(propertyName, value, type);
	}

	public List<SysRoleAuthority> findByPropertyEqual(String propertyName,
			String value, String type) {
		List<SysRoleAuthority> pojos = sysRoleAuthorityDao.findByPropertyEqual(
				propertyName, value, type);
		return pojos;
	}
}
