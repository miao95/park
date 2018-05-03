package edu.buaa.sem.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.buaa.sem.common.BaseService;
import edu.buaa.sem.po.SysAuthorityResource;
import edu.buaa.sem.system.dao.SysAuthorityResourceDao;

@Service
public class SysAuthorityResourceService extends BaseService {

	@Autowired
	private SysAuthorityResourceDao sysAuthorityResourceDao;

	public void saveOrUpdate(SysAuthorityResource pojo) {
		sysAuthorityResourceDao.saveOrUpdate(pojo);
	}

	public void deleteByKeys(String propertyName, String[] value, String type) {
		sysAuthorityResourceDao.deleteByKeys(propertyName, value, type);
	}

	public List<SysAuthorityResource> findByPropertyEqual(String propertyName,
			String value, String type) {
		List<SysAuthorityResource> pojos = sysAuthorityResourceDao
				.findByPropertyEqual(propertyName, value, type);
		return pojos;
	}

	public List<SysAuthorityResource> findByExample(SysAuthorityResource pojo) {
		return sysAuthorityResourceDao.findByExample(pojo, true);
	}
}
