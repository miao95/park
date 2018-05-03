package edu.buaa.sem.common;

import edu.buaa.sem.utils.RegexUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Dao的基本类，用户设置一些公共属性和方法，每一个Dao类必须继承该类
 */
public class BaseDao<T> {

    @Autowired
    public SessionFactory sessionFactory;

    protected final String DEFAULT_PAGE_ROWS = "20";// 默认分页时每页行数
    protected final String DEFAULT_PAGE_START = "1";// 默认分页时起始页数

    public Logger logger;

    /**
     * 取得泛型实现类型
     */
    @SuppressWarnings("unchecked")
    protected Class<T> typeClass() {
        return (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 执行原生SQL查询语句 ，由于安全原因，不允许用户传参调用
     *
     * @param sqlString 原生SQL语句,不是HQL语法
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> executeSelectSQL(String sqlString) {
        sessionFactory.getCurrentSession().flush();// 必须先刷新让一级缓存与数据库同步，后面手动执行查询语句才对【修改】的内容有效
        try {
            List<Object[]> pojos = sessionFactory.getCurrentSession()
                    .createSQLQuery(sqlString).list();
            logger.debug("executeSelectSQL successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("executeSelectSQL failed", re);
            throw re;
        }
    }

    /**
     * 执行原生SQL查询语句 ，由于安全原因，不允许用户传参调用
     *
     * @param sqlString 原生SQL语句,不是HQL语法
     * @return
     */
    public Object executeSelectSQLUniqueResult(String sqlString) {
        sessionFactory.getCurrentSession().flush();// 必须先刷新让一级缓存与数据库同步，后面手动执行查询语句才对【修改】的内容有效
        try {
            Object pojos = sessionFactory.getCurrentSession()
                    .createSQLQuery(sqlString).uniqueResult();
            logger.debug("executeSelectSQL successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("executeSelectSQL failed", re);
            throw re;
        }
    }

    /**
     * 执行原生SQL查询语句 ，由于安全原因，不允许用户传参调用
     *
     * @param sqlString 原生SQL语句,不是HQL语法
     * @return
     */
    public int countSelectSQL(String sqlString) {
        sessionFactory.getCurrentSession().flush();// 必须先刷新让一级缓存与数据库同步，后面手动执行查询语句才对【修改】的内容有效
        try {
            Object object = sessionFactory.getCurrentSession()
                    .createSQLQuery(sqlString).uniqueResult();
            int count = 0;
            if (object != null) {
                count = (Integer) object;
            }
            logger.debug("countSelectSQL successful");
            return count;
        } catch (RuntimeException re) {
            logger.error("countSelectSQL failed", re);
            throw re;
        }
    }

    /**
     * 执行原生SQL非查询语句 ，由于安全原因，不允许用户传参调用
     *
     * @param sqlString 原生SQL语句,不是HQL语法
     * @return
     */
    public void executeNoSelectSQL(String sqlString) {
        sessionFactory.getCurrentSession().flush();// 必须先刷新让一级缓存与数据库同步，后面手动执行查询语句才对【修改】的内容有效
        try {
            sessionFactory.getCurrentSession().createSQLQuery(sqlString)
                    .executeUpdate();
            logger.debug("executeNoSelectSQL successful");
        } catch (RuntimeException re) {
            logger.error("executeNoSelectSQL failed", re);
            throw re;
        }
    }

    /**
     * 保存一条记录
     *
     * @param pojo
     */
    public void save(T pojo) {
        try {
            sessionFactory.getCurrentSession().save(pojo);
            logger.debug("save successful");
        } catch (RuntimeException re) {
            logger.error("save failed", re);
            throw re;
        }
    }

    /**
     * 保存一条记录,手动加事务
     *
     * @param pojo
     */
    public void saveWithTransaction(T pojo) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(pojo);
            session.getTransaction().commit();
            session.close();
            logger.debug("save successful");
        } catch (RuntimeException re) {
            logger.error("save failed", re);
            throw re;
        }
    }


    /**
     * 更新一条记录
     *
     * @param pojo
     */
    public void update(T pojo) {
        try {
            sessionFactory.getCurrentSession().update(pojo);
            logger.debug("update successful");
        } catch (RuntimeException re) {
            logger.error("update failed", re);
            throw re;
        }
    }

    /**
     * 合并两个session中的同一对象
     *
     * @param pojo
     */
    public void merge(T pojo) {
        try {
            sessionFactory.getCurrentSession().merge(pojo);
            logger.debug("update successful");
        } catch (RuntimeException re) {
            logger.error("update failed", re);
            throw re;
        }
    }

    /**
     * 保存或者更新一条记录，如果确定知道是保存还是更新，请调用save或者update方法
     *
     * @param pojo
     */
    public void saveOrUpdate(T pojo) {
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(pojo);
            logger.debug("saveOrUpdate successful");
        } catch (RuntimeException re) {
            logger.error("saveOrUpdate failed", re);
            throw re;
        }
    }

    /**
     * 根据某个属性更新另一属性
     *
     * @param onePropertyName
     * @param onePropertyValue
     * @param onePropertyType
     * @param anotherPropertyName
     * @param anotherPropertyValue
     * @param anotherPropertyType
     */
    public void updateOnePropertyByAnotherProperty(String onePropertyName,
                                                   String onePropertyValue, String onePropertyType,
                                                   String anotherPropertyName, String anotherPropertyValue,
                                                   String anotherPropertyType) {
        try {
            String queryString = "update " + typeClass().getCanonicalName()
                    + " set " + onePropertyName + "= ?";
            queryString += " where " + anotherPropertyName + "= ?";
            Query query = sessionFactory.getCurrentSession().createQuery(
                    queryString);
            if (onePropertyType.equals("String")) {
                query.setString(0, onePropertyValue);
            } else if (onePropertyType.equals("long")) {
                query.setLong(0, Long.parseLong(onePropertyValue));
            } else if (onePropertyType.equals("int")) {
                query.setInteger(0, Integer.parseInt(onePropertyValue));
            }

            if (anotherPropertyType.equals("String")) {
                query.setString(1, anotherPropertyValue);
            } else if (anotherPropertyType.equals("long")) {
                query.setLong(1, Long.parseLong(anotherPropertyValue));
            } else if (anotherPropertyType.equals("int")) {
                query.setInteger(1, Integer.parseInt(anotherPropertyValue));
            }

            query.executeUpdate();
            logger.debug("updateOnePropertyByAnotherProperty successful");
        } catch (RuntimeException re) {
            logger.error("updateOnePropertyByAnotherProperty failed", re);
            throw re;
        }
    }

    /**
     * 删除一条记录
     */
    public void delete(T pojo) {
        try {
            sessionFactory.getCurrentSession().delete(pojo);
            logger.debug("delete successful");
        } catch (RuntimeException re) {
            logger.error("delete failed", re);
            throw re;
        }
    }

    /**
     * 根据主键或属性删除一条或多条记录
     */
    public void deleteByKeys(String key, String[] ids, String type) {
        try {
            String queryString = "delete " + typeClass().getCanonicalName()
                    + " where";
            for (int i = 0; i < ids.length; i++) {
                queryString += " " + key + "= ? or";
            }
            queryString = StringUtils.removeEnd(queryString, "or");
            Query query = sessionFactory.getCurrentSession().createQuery(
                    queryString);
            for (int i = 0; i < ids.length; i++) {
                if (type.equals("String")) {
                    query.setString(i, ids[i]);
                } else if (type.equals("long")) {
                    query.setLong(i, Long.parseLong(ids[i]));
                } else if (type.equals("int")) {
                    query.setInteger(i, Integer.parseInt(ids[i]));
                }
            }
            query.executeUpdate();
            logger.debug("deleteByKeys successful");
        } catch (RuntimeException re) {
            logger.error("deleteByKeys failed", re);
            throw re;
        }
    }

    /**
     * 根据主键查询一条或多条记录
     */
    public List<T> findByKeys(String key, String[] ids, String type) {
        try {
            String queryString = "from " + typeClass().getCanonicalName()
                    + " where ";
            for (int i = 0; i < ids.length; i++) {
                queryString += " " + key + "= ? or";
            }
            queryString = StringUtils.removeEnd(queryString, "or");
            Query query = sessionFactory.getCurrentSession().createQuery(
                    queryString);
            for (int i = 0; i < ids.length; i++) {
                if (type.equals("String")) {
                    query.setString(i, ids[i]);
                } else if (type.equals("long")) {
                    query.setLong(i, Long.parseLong(ids[i]));
                } else if (type.equals("int")) {
                    query.setInteger(i, Integer.parseInt(ids[i]));
                }
            }
            List<T> pojos = (List<T>) query.list();
            logger.debug("findByKeys successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("findByKeys failed", re);
            throw re;
        }
    }

    /**
     * 根据long型主键查询一条记录
     */
    @SuppressWarnings("unchecked")
    public T findByKey(long id) {
        try {
            T pojo = (T) sessionFactory.getCurrentSession()
                    .get(typeClass(), id);
            logger.debug("findByKey successful");
            return pojo;
        } catch (RuntimeException re) {
            logger.error("findByKey failed", re);
            throw re;
        }
    }

    /**
     * 根据int型主键查询一条记录
     */
    @SuppressWarnings("unchecked")
    public T findByKey(int id) {
        try {
            T pojo = (T) sessionFactory.getCurrentSession()
                    .get(typeClass(), id);
            logger.debug("findByKey successful");
            return pojo;
        } catch (RuntimeException re) {
            logger.error("findByKey failed", re);
            throw re;
        }
    }

    /**
     * 根据string型主键查询一条记录
     */
    @SuppressWarnings("unchecked")
    public T findByKey(String id) {
        try {
            T pojo = (T) sessionFactory.getCurrentSession()
                    .get(typeClass(), id);
            logger.debug("findByKey successful");
            return pojo;
        } catch (RuntimeException re) {
            logger.error("findByKey failed", re);
            throw re;
        }
    }

    /**
     * 单属性相等查询
     */
    @SuppressWarnings("unchecked")
    public List<T> findByPropertyEqualWithTransaction(String propertyName, String value,
                                                      String type) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            String queryString = "from " + typeClass().getCanonicalName()
                    + " as model where model." + propertyName + "= ?";
            Query query = session.createQuery(queryString);
            if (type.equals("String")) {
                query.setString(0, value.toLowerCase());
            } else if (type.equals("long")) {
                query.setLong(0, Long.parseLong(value));
            } else if (type.equals("int")) {
                query.setInteger(0, Integer.parseInt(value));
            }
            List<T> pojos = query.list();
            session.getTransaction().commit();
            session.close();
            logger.debug("findByPropertyEqual successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("findByPropertyEqual failed", re);
            throw re;
        }
    }

    /**
     * 单属性相等查询,手动加事务
     */
    @SuppressWarnings("unchecked")
    public List<T> findByPropertyEqual(String propertyName, String value,
                                       String type) {
        try {
            String queryString = "from " + typeClass().getCanonicalName()
                    + " as model where model." + propertyName + "= ?";
            Query query = sessionFactory.getCurrentSession().createQuery(
                    queryString);
            if (type.equals("String")) {
                query.setString(0, value.toLowerCase());
            } else if (type.equals("long")) {
                query.setLong(0, Long.parseLong(value));
            } else if (type.equals("int")) {
                query.setInteger(0, Integer.parseInt(value));
            }
            List<T> pojos = query.list();
            logger.debug("findByPropertyEqual successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("findByPropertyEqual failed", re);
            throw re;
        }
    }

    /**
     * 单属性相等查询
     */
    @SuppressWarnings("unchecked")
    public List<T> findByPropertyEqualWithOrder(String propertyName,
                                                String value, String type, String orderName, String sort) {
        try {
            StringBuilder queryString = new StringBuilder();
            queryString.append("from ");
            queryString.append(typeClass().getCanonicalName());
            queryString.append(" as model where model.");
            queryString.append(propertyName);
            queryString.append("=? order by model.");
            queryString.append(orderName);
            queryString.append(" ");
            queryString.append(sort);
            Query query = sessionFactory.getCurrentSession().createQuery(
                    queryString.toString());
            if (type.equals("String")) {
                query.setString(0, value.toLowerCase());
            } else if (type.equals("long")) {
                query.setLong(0, Long.parseLong(value));
            } else if (type.equals("int")) {
                query.setInteger(0, Integer.parseInt(value));
            }
            List<T> pojos = query.list();
            logger.debug("findByPropertyEqual successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("findByPropertyEqual failed", re);
            throw re;
        }
    }

    /**
     * 单属性模糊查询
     */
    @SuppressWarnings("unchecked")
    public List<T> findByPropertyLike(String propertyName, String value) {
        try {
            String queryString = " from " + typeClass().getCanonicalName()
                    + " as model where LOWER(model." + propertyName
                    + ") like ?";
            Query query = sessionFactory.getCurrentSession().createQuery(
                    queryString);
            query.setString(0, "%" + String.valueOf(value).toLowerCase() + "%");
            List<T> pojos = query.list();
            logger.debug("findByPropertyLike successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("findByPropertyLike failed", re);
            throw re;
        }
    }

    /**
     * 查询所有记录
     */
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        try {
            String queryString = "from " + typeClass().getCanonicalName();
            Query query = sessionFactory.getCurrentSession().createQuery(
                    queryString);
            List<T> pojos = query.list();
            logger.debug("findAll successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("findAll failed", re);
            throw re;
        }
    }

    /**
     * 查询所有记录，排序输出
     */
    @SuppressWarnings("unchecked")
    public List<T> findAllWithOrder(String sort, String order) {
        try {
            Criteria criteria = sessionFactory.getCurrentSession()
                    .createCriteria(typeClass());
            if (order != null && order.equals("asc") && sort != null) {
                criteria.addOrder(Order.asc(sort));
            }
            if (order != null && order.equals("desc") && sort != null) {
                criteria.addOrder(Order.desc(sort));
            }
            List<T> pojos = criteria.list();
            logger.debug("findAllWithOrder successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("findAllWithOrder failed", re);
            throw re;
        }
    }

    /**
     * 根据Example查询类似或相等记录
     */
    @SuppressWarnings("unchecked")
    public List<T> findByExample(T pojo, boolean enableLike) {
        try {
            Example example = Example.create(pojo);
            if (enableLike) {
                example.ignoreCase().enableLike(MatchMode.ANYWHERE);
            }
//            example.excludeZeroes();
            Criteria criteria = sessionFactory.getCurrentSession()
                    .createCriteria(typeClass()).add(example);
            List<T> pojos = criteria.list();
            logger.debug("findByExample successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("findByExample failed", re);
            throw re;
        }
    }

    /**
     * 根据Example查询类似或相等记录
     */
    @SuppressWarnings("unchecked")
    public List<T> findByExampleWithOrder(T pojo, boolean enableLike,
                                          String sort, String order) {
        try {
            Example example = Example.create(pojo);
            if (enableLike) {
                example.ignoreCase().enableLike(MatchMode.ANYWHERE);
            }
            example.excludeZeroes();
            Criteria criteria = sessionFactory.getCurrentSession()
                    .createCriteria(typeClass()).add(example);
            if (order != null && order.equals("asc") && sort != null) {
                criteria.addOrder(Order.asc(sort));
            }
            if (order != null && order.equals("desc") && sort != null) {
                criteria.addOrder(Order.desc(sort));
            }
            List<T> pojos = criteria.list();
            logger.debug("findByExampleWithOrder successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("findByExampleWithOrder failed", re);
            throw re;
        }
    }

    /**
     * 根据Example查询类似或相等记录(多字段排序)
     */
    @SuppressWarnings("unchecked")
    public List<T> findByExampleWithOrders(T pojo, boolean enableLike,
                                           String[] sort, String[] order) {
        try {
            Example example = Example.create(pojo);
            if (enableLike) {
                example.ignoreCase().enableLike(MatchMode.ANYWHERE);
            }
            example.excludeZeroes();
            Criteria criteria = sessionFactory.getCurrentSession()
                    .createCriteria(typeClass()).add(example);
            if (sort != null) {
                for (int i = 0; i < sort.length; i++) {
                    if (order[i] != null && order[i].equals("asc")
                            && sort[i] != null) {
                        criteria.addOrder(Order.asc(sort[i]));
                    }
                    if (order[i] != null && order[i].equals("desc")
                            && sort[i] != null) {
                        criteria.addOrder(Order.desc(sort[i]));
                    }
                }
            }

            List<T> pojos = criteria.list();
            logger.debug("findByExampleWithOrder successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("findByExampleWithOrder failed", re);
            throw re;
        }
    }

    /**
     * 根据Example查询类似或相等记录总数
     */
    public long countByExample(T pojo, boolean enableLike) {
        try {
            Example example = Example.create(pojo);
            if (enableLike) {
                example.ignoreCase().enableLike(MatchMode.ANYWHERE);
            }
            example.excludeZeroes();
            Criteria criteria = sessionFactory.getCurrentSession()
                    .createCriteria(typeClass()).add(example)
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

    /**
     * 根据Example和分页参数查询类似或相等记录
     */
    @SuppressWarnings("unchecked")
    public List<T> findByExampleForPagination(T pojo, boolean enableLike,
                                              String page, String rows, String sort, String order) {
        try {
            int intPage = Integer
                    .parseInt((page == null || page.equals("0")) ? DEFAULT_PAGE_START
                            : page);
            int intRows = Integer
                    .parseInt((rows == null || rows.equals("0")) ? DEFAULT_PAGE_ROWS
                            : rows);

            Example example = Example.create(pojo);
            if (enableLike) {
                example.ignoreCase().enableLike(MatchMode.ANYWHERE);
            }
            example.excludeZeroes();
            Criteria criteria = sessionFactory.getCurrentSession()
                    .createCriteria(typeClass()).add(example);
            if (order != null && order.equals("asc") && sort != null) {
                criteria.addOrder(Order.asc(sort));
            }
            if (order != null && order.equals("desc") && sort != null) {
                criteria.addOrder(Order.desc(sort));
            }
            criteria.setFirstResult((intPage - 1) * intRows);
            criteria.setMaxResults(intRows);
            List<T> pojos = criteria.list();
            logger.debug("findByExampleForPagination successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("findByExampleForPagination failed", re);
            throw re;
        }
    }

    /**
     * 根据Example和分页参数查询类似或相等记录
     */
    @SuppressWarnings("unchecked")
    public List<T> findByExampleForPagination(T pojo, boolean enableLike,
                                              String page, String rows, String[] sort, String[] order) {
        try {
            int intPage = Integer
                    .parseInt((page == null || page.equals("0")) ? DEFAULT_PAGE_START
                            : page);
            int intRows = Integer
                    .parseInt((rows == null || rows.equals("0")) ? DEFAULT_PAGE_ROWS
                            : rows);

            Example example = Example.create(pojo);
            if (enableLike) {
                example.ignoreCase().enableLike(MatchMode.ANYWHERE);
            }
            example.excludeZeroes();
            Criteria criteria = sessionFactory.getCurrentSession()
                    .createCriteria(typeClass()).add(example);
            if (order != null && sort != null) {
                for (int i = 0; i < Math.min(order.length, sort.length); i++) {
                    if (order[i] != null && order[i].equals("asc")
                            && sort[i] != null) {
                        criteria.addOrder(Order.asc(sort[i]));
                    }
                    if (order[i] != null && order[i].equals("desc")
                            && sort[i] != null) {
                        criteria.addOrder(Order.desc(sort[i]));
                    }
                }
            }
            criteria.setFirstResult((intPage - 1) * intRows);
            criteria.setMaxResults(intRows);
            List<T> pojos = criteria.list();
            logger.debug("findByExampleForPagination successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("findByExampleForPagination failed", re);
            throw re;
        }
    }

    /**
     * 查询分页记录
     *
     * @param list                待分页的list对象
     * @param isForeign           是否根据关联数组(list)查询外键的list记录
     * @param foreignPropertyName 外键在关联pojo中的属性名称,默认为调用者dao的泛型实现名称,如sysUserDao.findForPagination
     *                            (..)的foreignPropertyName为sysUser
     * @param page                页码
     * @param rows                条数
     * @param sort                排序字段
     * @param order               排序方式
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findForPagination(List list, boolean isForeign,
                                     String foreignPropertyName, String page, String rows, String sort,
                                     String order) {
        try {
            int intPage = Integer
                    .parseInt((page == null || page.equals("0")) ? DEFAULT_PAGE_START
                            : page);
            int intRows = Integer
                    .parseInt((rows == null || rows.equals("0")) ? DEFAULT_PAGE_ROWS
                            : rows);
            Session session = sessionFactory.getCurrentSession();
            String queryString = "";
            if (sort != null && !sort.isEmpty()) {
                boolean sortAble = Pattern.matches("[a-z_A-Z]+", sort);
                boolean orderAble = order == null || order.isEmpty()
                        || order.equals("desc");
                if (sortAble && orderAble) {
                    queryString += " order by " + sort + " " + order;
                }
            }
            Query query = session.createFilter(list, queryString);
            query.setFirstResult((intPage - 1) * intRows);
            query.setMaxResults(intRows);
            List pojos = query.list();
            if (!isForeign) {
                return pojos;
            }
            List<T> result = new ArrayList<T>();
            if (pojos != null && pojos.size() > 0) {
                String className = "get" + typeClass().getSimpleName();
                if (foreignPropertyName != null
                        && !foreignPropertyName.isEmpty()) {
                    className = "get"
                            + foreignPropertyName.substring(0, 1).toUpperCase()
                            + foreignPropertyName.substring(1);
                }
                for (int i = 0; i < pojos.size(); i++) {
                    try {
                        Method sourceMethod = pojos.get(i).getClass()
                                .getMethod(className);
                        // 取出source的值并赋给target
                        Object pojo = sourceMethod.invoke(pojos.get(i));
                        result.add((T) pojo);
                    } catch (IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
            logger.debug("findForeignsForPagination successful");
            return result;
        } catch (RuntimeException re) {
            logger.error("findForeignsForPagination failed", re);
            throw re;
        }
    }

    /**
     * 查询分页记录
     *
     * @param list                待分页的list对象
     * @param isForeign           是否根据关联数组(list)查询外键的list记录
     * @param foreignPropertyName 外键在关联pojo中的属性名称,默认为调用者dao的泛型实现名称,如sysUserDao.findForPagination
     *                            (..)的foreignPropertyName为sysUser
     * @param sort                排序字段
     * @param order               排序方式
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findWithOrders(List list, boolean isForeign,
                                  String foreignPropertyName, String sort, String order) {
        try {
            Session session = sessionFactory.getCurrentSession();
            String queryString = "";
            if (sort != null && !sort.isEmpty()) {
                boolean sortAble = Pattern.matches("[a-z_A-Z]+", sort);
                boolean orderAble = order == null || order.isEmpty()
                        || order.equals("desc");
                if (sortAble && orderAble) {
                    queryString += " order by " + sort + " " + order;
                }
            }
            Query query = session.createFilter(list, queryString);
            List pojos = query.list();
            if (!isForeign) {
                return pojos;
            }
            List<T> result = new ArrayList<T>();
            if (pojos != null && pojos.size() > 0) {
                String className = "get" + typeClass().getSimpleName();
                if (foreignPropertyName != null
                        && !foreignPropertyName.isEmpty()) {
                    className = "get"
                            + foreignPropertyName.substring(0, 1).toUpperCase()
                            + foreignPropertyName.substring(1);
                }
                for (int i = 0; i < pojos.size(); i++) {
                    try {
                        Method sourceMethod = pojos.get(i).getClass()
                                .getMethod(className);
                        // 取出source的值并赋给target
                        Object pojo = sourceMethod.invoke(pojos.get(i));
                        result.add((T) pojo);
                    } catch (IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
            logger.debug("findForeignsWithOrders successful");
            return result;
        } catch (RuntimeException re) {
            logger.error("findForeignsWithOrders failed", re);
            throw re;
        }
    }

    /**
     * 通过两个外键查询数据记录
     *
     * @param pojo1 外键1对象
     * @param pojo2 外键2对象
     * @return
     */
    public List<T> findByTwoForeignKeys(String firstForeignKeyName,
                                        String secondForeignKeyName, Object pojo1, Object pojo2) {

        try {
            Criteria criteria = sessionFactory.getCurrentSession()
                    .createCriteria(typeClass())
                    .add(Restrictions.eq(firstForeignKeyName, pojo1))
                    .add(Restrictions.eq(secondForeignKeyName, pojo2));
            List<T> pojos = criteria.list();
            logger.debug("findByTwoForeignKeys successful");
            return pojos;
        } catch (RuntimeException re) {
            logger.error("findByTwoForeignKeys failed", re);
            throw re;
        }
    }

    /**
     * 后台查询分页
     *
     * @param params 查询参数:HashMap型
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @param count  查询总数语句
     * @param select 查询记录语句
     * @return
     */
    public HashMap<String, Object> findPaginationForAdmin(
            HashMap<String, Object> params, String page, String rows,
            String sort, String order, String count, String select,
            String groupBy) {
        // 定义页码和条数
        int intPage = Integer
                .parseInt((page == null || page.equals("0")) ? DEFAULT_PAGE_START
                        : page);
        int intRows = Integer
                .parseInt((rows == null || rows.equals("0")) ? DEFAULT_PAGE_ROWS
                        : rows);
        // 验证sort和order是否含有恶意sql注入代码
        if (!RegexUtils.matchLetterAndNumber(sort)
                || !RegexUtils.matchLetterAndNumber(order)) {
            return null;
        }
        if (params != null) {
            // 针对count语句和select语句拼接字符串
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                if (val != null && !val.isEmpty()) {
                    count += " and " + key + " like :" + key;
                    select += " and " + key + " like :" + key;
                }
            }
        }

        if (groupBy != null && !groupBy.isEmpty()) {
            select = select + " " + groupBy;
        }

        if (sort != null && !sort.isEmpty() && order != null
                && !order.isEmpty()) {
            select += " order by " + sort + " " + order;
        }
        // 定义SQLQuery语句
        SQLQuery selectQuery = this.sessionFactory.getCurrentSession()
                .createSQLQuery(select);
        SQLQuery countQuery = this.sessionFactory.getCurrentSession()
                .createSQLQuery(count);
        if (params != null) {
            // 为拼接的字符串中的变量设置值
            Iterator iterA = params.entrySet().iterator();
            while (iterA.hasNext()) {
                Map.Entry entry = (Map.Entry) iterA.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                if (val != null && !val.isEmpty()) {
                    selectQuery.setString(key, "%" + val + "%");
                    countQuery.setString(key, "%" + val + "%");
                }
            }
        }
        Object resultCount = countQuery.uniqueResult();
        selectQuery.setFirstResult((intPage - 1) * intRows);
        selectQuery.setMaxResults(intRows);
        List<Object[]> list = selectQuery.list();
        List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
        String[] resultParams = select.substring(7, select.indexOf("from "))
                .trim().split(",");
        // 处理select后的结果集,封装为List<HashMap<String, Object>>结构
        for (Object[] obj : list) {
            HashMap<String, Object> hm = new HashMap<String, Object>();
            for (int i = 0; i < resultParams.length; i++) {
                String temp = resultParams[i];
                if (temp.toLowerCase().indexOf(" as ") != -1) {
                    temp = temp.substring(
                            temp.toLowerCase().indexOf(" as ") + 4,
                            temp.length()).trim();
                }
                hm.put(temp, obj[i]);
            }
            resultList.add(hm);
        }
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("rows", resultList);
        result.put("total", resultCount);
        return result;
    }
}
