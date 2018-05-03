package edu.buaa.sem.system.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import edu.buaa.sem.common.BaseDao;
import edu.buaa.sem.po.SysRole;

@Repository
public class SysRoleDao extends BaseDao<SysRole> {
	public SysRoleDao() {
		logger = Logger.getLogger(SysRoleDao.class);
	}

	public void updateByKeys(SysRole pojo, String key, long[] ids) {
		try {
			boolean updateFlag = false;
			String queryString = "update SysRole set";
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

	@SuppressWarnings("unchecked")
	public List<SysRole> findRoleForRecommendation(String page, String rows,
			String sort, String order) {
		try {
			int intPage = Integer
					.parseInt((page == null || page.equals("0")) ? DEFAULT_PAGE_START
							: page);
			int intRows = Integer
					.parseInt((rows == null || rows.equals("0")) ? DEFAULT_PAGE_ROWS
							: rows);
			String queryString = "";
			if (sort != null && order != null) {
				queryString = "from SysRole where name not like '%ADMIN%' order by "
						+ sort + " " + order;
			} else {
				queryString = "from SysRole where name not like '%ADMIN%'";
			}
			Query query = sessionFactory.getCurrentSession().createQuery(
					queryString);
			query.setFirstResult((intPage - 1) * intRows);
			query.setMaxResults(intRows);
			List<SysRole> sysRole = query.list();
			return sysRole;
		} catch (Exception e) {
			throw e;
		}
	}

	public long countRoleForRecommendation() {
		String queryString = "from SysRole where name not like '%ADMIN%'";
		Query query = sessionFactory.getCurrentSession().createQuery(
				queryString);
		List<SysRole> sysRole = query.list();
		return sysRole.size();
	}

}
