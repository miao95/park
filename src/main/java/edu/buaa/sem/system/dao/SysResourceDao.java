package edu.buaa.sem.system.dao;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import edu.buaa.sem.common.BaseDao;
import edu.buaa.sem.po.SysResource;

@Repository
public class SysResourceDao extends BaseDao<SysResource>{
	public SysResourceDao() {
		logger = Logger.getLogger(SysResourceDao.class);
	}

	public void updateByKeys(SysResource pojo, String key, long[] ids) {
		try {
			boolean updateFlag = false;
			String queryString = "update SysResource set";
			if (pojo.getDescription() != null
					&& !pojo.getDescription().isEmpty()) {
				queryString += " description = :description,";
				updateFlag = true;
			}
			if (pojo.getType() != null && !pojo.getType().isEmpty()) {
				queryString += " type = :type,";
				updateFlag = true;
			}
			if (pojo.getAddress() != null && !pojo.getAddress().isEmpty()) {
				queryString += " address = :address,";
				updateFlag = true;
			}
			if (pojo.getEnabled() != null && !pojo.getEnabled().isEmpty()) {
				queryString += " enabled = :enabled,";
				updateFlag = true;
			}
			if (updateFlag) {
				queryString = StringUtils.removeEnd(queryString, ",");
				queryString += " where";
				for (int i = 0; i < ids.length; i++) {
					queryString += " " + key + "= :key" + i + " or";
				}

				queryString = StringUtils.removeEnd(queryString, "or");
				Query query = sessionFactory.getCurrentSession().createQuery(
						queryString);

				if (pojo.getDescription() != null
						&& !pojo.getDescription().isEmpty()) {
					query.setString("description", pojo.getDescription());
				}
				if (pojo.getType() != null && !pojo.getType().isEmpty()) {
					query.setString("type", pojo.getType());
				}
				if (pojo.getAddress() != null && !pojo.getAddress().isEmpty()) {
					query.setString("address", pojo.getAddress());
				}
				if (pojo.getEnabled() != null && !pojo.getEnabled().isEmpty()) {
					query.setString("enabled", pojo.getEnabled());
				}
				for (int i = 0; i < ids.length; i++) {
					query.setLong("key" + i, ids[i]);
				}

				query.executeUpdate();
				logger.debug("updateByKeys successful");
			}
		} catch (RuntimeException re) {
			logger.error("updateByKeys failed", re);
			throw re;
		}
	}

}
