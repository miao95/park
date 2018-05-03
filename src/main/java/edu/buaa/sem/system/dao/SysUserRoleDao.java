package edu.buaa.sem.system.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import edu.buaa.sem.common.BaseDao;
import edu.buaa.sem.po.SysUserRole;

@Repository
public class SysUserRoleDao extends BaseDao<SysUserRole>{
	public SysUserRoleDao() {
		logger = Logger.getLogger(SysUserRoleDao.class);
	}

	

}
