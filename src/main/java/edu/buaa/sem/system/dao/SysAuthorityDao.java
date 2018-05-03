package edu.buaa.sem.system.dao;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import edu.buaa.sem.common.BaseDao;
import edu.buaa.sem.po.SysAuthority;

@Repository
public class SysAuthorityDao extends BaseDao<SysAuthority>{
	public SysAuthorityDao() {
		logger = Logger.getLogger(SysAuthorityDao.class);
	}

	public void updateByKeys(SysAuthority pojo, String key, long[] ids) {
		try {
			boolean updateFlag = false;
			String queryString = "update SysAuthority set";
			if (pojo.getName() != null && !pojo.getName().isEmpty()) {
				queryString += " name = :name,";
				updateFlag = true;
			}
			if (pojo.getDescription() != null
					&& !pojo.getDescription().isEmpty()) {
				queryString += " description = :description,";
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

				if (pojo.getName() != null && !pojo.getName().isEmpty()) {
					query.setString("name", pojo.getName());
				}
				if (pojo.getDescription() != null
						&& !pojo.getDescription().isEmpty()) {
					query.setString("description", pojo.getDescription());
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
