package edu.buaa.sem.system.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.stereotype.Repository;

import edu.buaa.sem.common.BaseDao;
import edu.buaa.sem.po.SysUser;

@Repository
public class SysUserDao extends BaseDao<SysUser> {
	public SysUserDao() {
		logger = Logger.getLogger(SysUserDao.class);
	}

	public void updateByKeys(SysUser pojo, String key, long[] ids) {
		try {
			boolean updateFlag = false;
			String queryString = "update SysUser set";
			if (pojo.getPassword() != null && !pojo.getPassword().isEmpty()) {
				queryString += " password = :password,";
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
			if (pojo.getCheckStatus() != null
					&& !pojo.getCheckStatus().isEmpty()) {
				queryString += " checkStatus = :checkStatus,";
				updateFlag = true;
			}

			if (StringUtils.isNotEmpty(pojo.getUserType())) {
				queryString += " userType = :userType,";
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

				if (pojo.getPassword() != null && !pojo.getPassword().isEmpty()) {
					query.setString("password", pojo.getPassword());
				}
				if (pojo.getDescription() != null
						&& !pojo.getDescription().isEmpty()) {
					query.setString("description", pojo.getDescription());
				}
				if (pojo.getEnabled() != null && !pojo.getEnabled().isEmpty()) {
					query.setString("enabled", pojo.getEnabled());
				}
				if (pojo.getCheckStatus() != null
						&& !pojo.getCheckStatus().isEmpty()) {
					query.setString("checkStatus", pojo.getCheckStatus());
				}
				if (StringUtils.isNotEmpty(pojo.getUserType())) {
					query.setString("userType", pojo.getUserType());
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

	public List<SysUser> findAllForSearch() {
		try {
			Criteria criteria = sessionFactory.getCurrentSession()
					.createCriteria(typeClass());
			criteria.add(Restrictions.ne("userType", "游客"));
			criteria.add(Restrictions.ne("enabled", "否"));
			List<SysUser> pojos = criteria.list();
			logger.debug("findAllForSearch successful");
			return pojos;
		} catch (RuntimeException re) {
			logger.error("findAllForSearch failed", re);
			throw re;
		}
	}

	/*--------------------------岸端管理员查询用户数据方法--------------------------------*/
	public List findByParamsForAdminPagination(SysUser pojo,
			HashMap conditionString, String page, String rows, String sort,
			String order) {
		try {
			int intPage = Integer
					.parseInt((page == null || page.equals("0")) ? DEFAULT_PAGE_START
							: page);
			int intRows = Integer
					.parseInt((rows == null || rows.equals("0")) ? DEFAULT_PAGE_ROWS
							: rows);
			String queryString = "from SysUser as a ,InfShippingCompany as b where a.companyId = b.id";
			Query query = contrustStringForAdmin(pojo, conditionString,
					queryString, sort, order);
			query.setFirstResult((intPage - 1) * intRows);
			query.setMaxResults(intRows);
			List pojos = query.list();
			logger.debug("findByParamsForPagination successful");
			return pojos;
		} catch (RuntimeException re) {
			logger.error("findByParamsForPagination failed", re);
			throw re;
		}
	}

	public long countAdminByParams(SysUser pojo, HashMap conditionString) {
		try {
			String queryString = "select count(*) from SysUser as a ,InfShippingCompany as b where a.companyId = b.id";
			Query query = contrustStringForAdmin(pojo, conditionString,
					queryString, null, null);
			List<Object> countList = query.list();
			long count = 0;
			if (countList != null) {
				count = (Long) countList.get(0);
			}
			logger.debug("countByParamsForPagination successful");
			return count;
		} catch (HibernateException e) {
			logger.error("countByParamsForSize failed", e);
			throw e;
		}
	}

	public Query contrustStringForAdmin(SysUser pojo, HashMap conditionString,
			String queryString, String sort, String order) {
		if (pojo.getId() != null) {
			queryString += " and a.id =:id";
		}
		if (pojo.getName() != null && !pojo.getName().isEmpty()) {
			queryString += " and a.name like:name";
		}
		if (pojo.getEmail() != null && !pojo.getEmail().isEmpty()) {
			queryString += " and a.email like:email";
		}
		if (pojo.getDescription() != null && !pojo.getDescription().isEmpty()) {
			queryString += " and a.description like:description";
		}
		if (pojo.getCreatTime() != null) {
			queryString += " and a.creatTime =:creatTime ";
		}
		if (pojo.getUserType() != null && !pojo.getUserType().isEmpty()) {
			queryString += " and a.userType =:userType ";
		}
		if (pojo.getCheckStatus() != null && pojo.getCheckStatus().isEmpty()) {
			queryString += " and a.checkStatus like:checkStatus ";
		}
		if (pojo.getSelfIntroduction() != null
				&& !pojo.getSelfIntroduction().isEmpty()) {
			queryString += " and a.selfIntroduction like:selfIntroduction ";
		}
		if (pojo.getEnabled() != null && !pojo.getEnabled().isEmpty()) {
			queryString += " and a.enabled =:enabled";
		}
		if (pojo.getHeadImageUrl() != null && !pojo.getHeadImageUrl().isEmpty()) {
			queryString += " and a.headImageUrl like:headImageUrl";
		}
		if (conditionString != null && !conditionString.isEmpty()) {
			if (conditionString.containsKey("companyIds")) {
				queryString += " and b.id in(:ids)";
			}
		}
		if (sort != null && !sort.isEmpty()) {
			queryString += " order by a." + sort + "  " + order;
		}
		Query query = sessionFactory.getCurrentSession().createQuery(
				queryString);
		// ---------------------下面是赋值操作------------------------

		if (pojo.getId() != null) {
			query.setLong("id", pojo.getId());
		}
		if (pojo.getName() != null && !pojo.getName().isEmpty()) {
			query.setString("name", '%' + pojo.getName() + '%');
		}
		if (pojo.getEmail() != null && !pojo.getEmail().isEmpty()) {
			query.setString("email", '%' + pojo.getEmail() + '%');
		}
		if (pojo.getDescription() != null && !pojo.getDescription().isEmpty()) {
			query.setString("description", '%' + pojo.getDescription() + '%');
		}
		if (pojo.getCreatTime() != null) {
			query.setDate("creatTime", pojo.getCreatTime());
		}
		if (pojo.getUserType() != null && !pojo.getUserType().isEmpty()) {
			query.setString("userType", pojo.getUserType());
		}
		if (pojo.getCheckStatus() != null && pojo.getCheckStatus().isEmpty()) {
			query.setString("checkStatus", '%' + pojo.getCheckStatus() + '%');
		}
		if (pojo.getSelfIntroduction() != null
				&& !pojo.getSelfIntroduction().isEmpty()) {
			query.setString("selfIntroduction",
					'%' + pojo.getSelfIntroduction() + '%');
		}
		if (pojo.getEnabled() != null && !pojo.getEnabled().isEmpty()) {
			query.setString("enabled", pojo.getEnabled());
		}
		if (pojo.getHeadImageUrl() != null && !pojo.getHeadImageUrl().isEmpty()) {
			query.setString("headImageUrl", '%' + pojo.getHeadImageUrl() + '%');
		}
		if (conditionString != null && !conditionString.isEmpty()) {
			if (conditionString.containsKey("companyIds")) {
				query.setParameterList("ids",
						(List) conditionString.get("companyIds"));
			}
		}
		return query;
	}

	/*--------------------------岸端管理员查询用户数据方法结束----------------------------------------*/

	/*
	 * --------------------------以下方法用于查询普通用户数据----------------------------------
	 * -----
	 */

	public List findByParamsForPagination(SysUser pojo,
			HashMap conditionString, String page, String rows, String sort,
			String order) {
		try {
			int intPage = Integer
					.parseInt((page == null || page.equals("0")) ? DEFAULT_PAGE_START
							: page);
			int intRows = Integer
					.parseInt((rows == null || rows.equals("0")) ? DEFAULT_PAGE_ROWS
							: rows);
			String queryString = "from SysUser as a ,InfCrew as b, InfShippingCompany as c where  a.staffIdCard = b.cardNumber and b.companyId = c.id";

			//船端和岸端查询方法不同，船端无公司表
			if (conditionString.containsKey("sqlString")) {
				String sqlString = conditionString.get("sqlString").toString();
				if (sqlString != null && !sqlString.equals("")) {
					queryString = sqlString;
				}

			}
			Query query = contrustString(pojo, conditionString, queryString,
					sort, order);
			query.setFirstResult((intPage - 1) * intRows);
			query.setMaxResults(intRows);
			List pojos = query.list();
			logger.debug("findByParamsForPagination successful");
			return pojos;
		} catch (RuntimeException re) {
			logger.error("findByParamsForPagination failed", re);
			throw re;
		}
	}

	public long countByParams(SysUser pojo, HashMap conditionString) {
		try {
			String queryString = "select count(*) from SysUser as a ,InfCrew as b, InfShippingCompany as c where  a.staffIdCard = b.cardNumber and b.companyId = c.id";
			
			//船端和岸端查询方法不同，船端无公司表
			if (conditionString.containsKey("sqlCountString")) {
				String sqlString = conditionString.get("sqlCountString").toString();
				if (sqlString != null && !sqlString.equals("")) {
					queryString = sqlString;
				}

			}
			Query query = contrustString(pojo, conditionString, queryString,
					null, null);
			List<Object> countList = query.list();
			long count = 0;
			if (countList != null) {
				count = (Long) countList.get(0);
			}
			logger.debug("countByParamsForPagination successful");
			return count;
		} catch (HibernateException e) {
			logger.error("countByParamsForSize failed", e);
			throw e;
		}
	}

	public Query contrustString(SysUser pojo, HashMap conditionString,
			String queryString, String sort, String order) {
		if (pojo.getId() != null) {
			queryString += " and a.id =:id";
		}
		if (pojo.getName() != null && !pojo.getName().isEmpty()) {
			queryString += " and a.name like:name";
		}
		if (pojo.getEmail() != null && !pojo.getEmail().isEmpty()) {
			queryString += " and a.email like:email";
		}
		if (pojo.getDescription() != null && !pojo.getDescription().isEmpty()) {
			queryString += " and a.description like:description";
		}
		if (pojo.getCreatTime() != null) {
			queryString += " and a.creatTime =:creatTime ";
		}
		if (pojo.getUserType() != null && !pojo.getUserType().isEmpty()) {
			queryString += " and a.userType =:userType ";
		}
		if (pojo.getCheckStatus() != null && pojo.getCheckStatus().isEmpty()) {
			queryString += " and a.checkStatus like:checkStatus ";
		}
		if (pojo.getSelfIntroduction() != null
				&& !pojo.getSelfIntroduction().isEmpty()) {
			queryString += " and a.selfIntroduction like:selfIntroduction ";
		}
		if (pojo.getEnabled() != null && !pojo.getEnabled().isEmpty()) {
			queryString += " and a.enabled =:enabled";
		}
		if (pojo.getHeadImageUrl() != null && !pojo.getHeadImageUrl().isEmpty()) {
			queryString += " and a.headImageUrl like:headImageUrl";
		}
		if (conditionString != null && !conditionString.isEmpty()) {
			if (conditionString.containsKey("sex")) {
				queryString += " and b.sex like:sex";
			}
			if (conditionString.containsKey("idCard")) {
				queryString += " and b.cardNumber like:idCard";
			}
			if (conditionString.containsKey("realName")) {
				queryString += " and b.name like:realName ";
			}
			if (conditionString.containsKey("mobile")) {
				queryString += " and b.phoneContact like:mobile";
			}
			if (conditionString.containsKey("companyIds")) {
				queryString += " and b.companyId in(:companyIds)";
			}
		}
		if (sort != null && !sort.isEmpty()) {
			queryString += " order by a." + sort + "  " + order;
		}
		Query query = sessionFactory.getCurrentSession().createQuery(
				queryString);
		// ---------------------下面是赋值操作------------------------

		if (pojo.getId() != null) {
			query.setLong("id", pojo.getId());
		}
		if (pojo.getName() != null && !pojo.getName().isEmpty()) {
			query.setString("name", '%' + pojo.getName() + '%');
		}
		if (pojo.getEmail() != null && !pojo.getEmail().isEmpty()) {
			query.setString("email", '%' + pojo.getEmail() + '%');
		}
		if (pojo.getDescription() != null && !pojo.getDescription().isEmpty()) {
			query.setString("description", '%' + pojo.getDescription() + '%');
		}
		if (pojo.getCreatTime() != null) {
			query.setDate("creatTime", pojo.getCreatTime());
		}
		if (pojo.getUserType() != null && !pojo.getUserType().isEmpty()) {
			query.setString("userType", pojo.getUserType());
		}
		if (pojo.getCheckStatus() != null && pojo.getCheckStatus().isEmpty()) {
			query.setString("checkStatus", '%' + pojo.getCheckStatus() + '%');
		}
		if (pojo.getSelfIntroduction() != null
				&& !pojo.getSelfIntroduction().isEmpty()) {
			query.setString("selfIntroduction",
					'%' + pojo.getSelfIntroduction() + '%');
		}
		if (pojo.getEnabled() != null && !pojo.getEnabled().isEmpty()) {
			query.setString("enabled", pojo.getEnabled());
		}
		if (pojo.getHeadImageUrl() != null && !pojo.getHeadImageUrl().isEmpty()) {
			query.setString("headImageUrl", '%' + pojo.getHeadImageUrl() + '%');
		}
		if (conditionString != null && !conditionString.isEmpty()) {
			if (conditionString.containsKey("sex")) {
				query.setString("sex", '%' + conditionString.get("sex")
						.toString() + '%');
			}
			if (conditionString.containsKey("idCard")) {
				query.setString("idCard", '%' + conditionString.get("idCard")
						.toString() + '%');
			}
			if (conditionString.containsKey("realName")) {
				query.setString("realName",
						'%' + conditionString.get("realName").toString() + '%');
			}
			if (conditionString.containsKey("mobile")) {
				query.setString("mobile", '%' + conditionString.get("mobile")
						.toString() + '%');
			}
			if (conditionString.containsKey("workPhone")) {
				query.setString("workPhone",
						'%' + conditionString.get("workPhone").toString() + '%');
			}
			if (conditionString.containsKey("companyIds")) {
				query.setParameterList("companyIds",
						(List) conditionString.get("companyIds"));
			}
		}
		return query;
	}

	/*--------------------------用于查询普通用户数据方法结束-----------------------------------------*/

	public List findUserByScoreOrder(int amount) {
		String sqlString = "from SysUser as a, InfCrew as b where a.staffIdCard = b.cardNumber and a.enabled = '是' order by a.points desc";
		Query query = sessionFactory.getCurrentSession().createQuery(sqlString);
		query.setMaxResults(amount);
		List userList = query.list();
		return userList;
	}

	public List<SysUser> findById2(String typeNumber) {
		try {
			String queryString = "from SysUser as a where id in ( "
					+ typeNumber + ")";

			Configuration configuration = null;
			SessionFactory sessionFactory = null;
			ServiceRegistry serviceRegistry = null;
			configuration = new Configuration().configure().addClass(
					SysUser.class);
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			Query query = sessionFactory.openSession().createQuery(queryString);
			List pojos = query.list();
			logger.debug("findByParamsForPagination successful");
			return pojos;
		} catch (RuntimeException re) {
			logger.error("findByParamsForPagination failed", re);
			throw re;
		}
	}

	/**
	 * @author YY
	 * @param propertyName
	 * @param value
	 * @param type
	 * @return
	 */
	public List<SysUser> findByPropertyEqualWithNoAutowired(
			String propertyName, String value, String type) {
		try {
			Configuration configuration = null;
			SessionFactory sessionFactory = null;
			ServiceRegistry serviceRegistry = null;
			configuration = new Configuration().configure().addClass(
					SysUser.class);
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			String queryString = "from " + typeClass().getCanonicalName()
					+ " as model where model." + propertyName + "= ?";
			Query query = sessionFactory.openSession().createQuery(queryString);
			if (type.equals("String")) {
				query.setString(0, value.toLowerCase());
			} else if (type.equals("long")) {
				query.setLong(0, Long.parseLong(value));
			} else if (type.equals("int")) {
				query.setInteger(0, Integer.parseInt(value));
			}
			List<SysUser> pojos = query.list();
			logger.debug("findByPropertyEqual successful");
			return pojos;
		} catch (RuntimeException re) {
			logger.error("findByPropertyEqual failed", re);
			throw re;
		}
	}

	/**
	 * 手动创建session保存用户
	 * 
	 * @author YY
	 * @param sysUser
	 */
	public void saveWithNoAutowired(SysUser sysUser) {
		try {
			Configuration configuration = null;
			SessionFactory sessionFactory = null;
			ServiceRegistry serviceRegistry = null;
			configuration = new Configuration().configure().addClass(
					SysUser.class);
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(sysUser);
			session.getTransaction().commit();
			session.close();
			sessionFactory.close();
			logger.debug("saveSysUserWithNoAutowired successful");
		} catch (RuntimeException re) {
			logger.error("saveSysUserWithNoAutowired failed", re);
			throw re;
		}

	}

	@SuppressWarnings("unchecked")
	public List<SysUser> findByExampleForPaginationWithComnapy(SysUser pojo,
			boolean enableLike, String page, String rows, String sort,
			String order, HashMap<String, Object> conditionString) {
		try {
			int intPage = Integer
					.parseInt((page == null || page.equals("0")) ? DEFAULT_PAGE_START
							: page);
			int intRows = Integer
					.parseInt((rows == null || rows.equals("0")) ? DEFAULT_PAGE_ROWS
							: rows);

			Example example = Example.create(pojo);
			Object[] companyIds = null;
			if (conditionString.containsKey("companyIds")) {
				List l = (List) conditionString.get("companyIds");
				companyIds = l.toArray();
			}
			if (enableLike) {
				example.ignoreCase().enableLike(MatchMode.ANYWHERE);
			}
			example.excludeZeroes();
			Criteria criteria = sessionFactory.getCurrentSession()
					.createCriteria(SysUser.class).add(example)
					.add(Restrictions.in("companyId", companyIds));
			if (order != null && order.equals("asc") && sort != null) {
				criteria.addOrder(Order.asc(sort));
			}
			if (order != null && order.equals("desc") && sort != null) {
				criteria.addOrder(Order.desc(sort));
			}
			criteria.setFirstResult((intPage - 1) * intRows);
			criteria.setMaxResults(intRows);
			List<SysUser> pojos = criteria.list();
			logger.debug("findByExampleForPagination successful");
			return pojos;
		} catch (RuntimeException re) {
			logger.error("findByExampleForPagination failed", re);
			throw re;
		}
	}

	public long countByExampleWithComnapy(SysUser pojo, boolean enableLike,
			HashMap<String, Object> conditionString) {
		try {
			Example example = Example.create(pojo);
			Object[] companyIds = null;
			if (conditionString.containsKey("companyIds")) {
				List l = (List) conditionString.get("companyIds");
				companyIds = l.toArray();
			}
			if (enableLike) {
				example.ignoreCase().enableLike(MatchMode.ANYWHERE);
			}

			example.excludeZeroes();
			Criteria criteria = sessionFactory.getCurrentSession()
					.createCriteria(SysUser.class).add(example)
					.add(Restrictions.in("companyId", companyIds))
					.setProjection(Projections.rowCount());
			Object object = (Long) criteria.uniqueResult();
			long count = 0;
			if (object != null) {
				count = (Long) object;
			}
			logger.debug("countByExample successful");
			return count;
		} catch (RuntimeException re) {
			logger.error("countByExample failed", re);
			throw re;
		}
	}
}