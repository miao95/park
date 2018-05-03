package edu.buaa.sem.system.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import edu.buaa.sem.common.BaseDao;
import edu.buaa.sem.po.SysAuthority;
import edu.buaa.sem.po.SysEmailModel;

@Repository
public class SysEmailModelDao extends BaseDao<SysEmailModel> {
	public SysEmailModelDao() {
		logger = Logger.getLogger(SysEmailModelDao.class);
	}

	public void updateByKeys(SysEmailModel pojo, String key, long[] ids){
		
	}

}
