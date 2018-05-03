package edu.buaa.sem.system.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import edu.buaa.sem.common.BaseDao;
import edu.buaa.sem.po.SysAuthorityResource;

@Repository
public class SysAuthorityResourceDao extends BaseDao<SysAuthorityResource>{
	public SysAuthorityResourceDao() {
		logger = Logger.getLogger(SysAuthorityResourceDao.class);
	}


}
