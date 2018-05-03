package edu.buaa.sem.system.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.buaa.sem.common.BaseService;
import edu.buaa.sem.po.SysEmailModel;
import edu.buaa.sem.system.dao.SysEmailModelDao;

@Service
public class SysEmailModelService extends BaseService {

	@Autowired
	private SysEmailModelDao sysEmailModelDao;

	/**
	 * 查询结果并分页处理
	 * 
	 * @param pojo
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	public List<SysEmailModel> findByExampleForPagination(SysEmailModel pojo,
			String page, String rows, String sort, String order) {
		List<SysEmailModel> pojos = sysEmailModelDao
				.findByExampleForPagination(pojo, true, page, rows, sort, order);
		return pojos;
	}

	public SysEmailModel findbyKey(long id) {

		SysEmailModel pojo = sysEmailModelDao.findByKey(id);
		return pojo;
	}

	/**
	 * 查询总的数目，分页使用
	 * 
	 * @param pojo
	 * @return
	 */
	public long countByExample(SysEmailModel pojo) {
		long count = sysEmailModelDao.countByExample(pojo, true);
		return count;
	}

	/**
	 * 保存Email模版
	 * 
	 * @param pojo
	 */
	public void save(SysEmailModel pojo) {
		// 替换多媒体正文中的“\r\n”特殊字符
		if (pojo.getEmailModelContent() != null
				&& !pojo.getEmailModelContent().equals("")) {
			pojo.setEmailModelContent(pojo.getEmailModelContent().replace(
					"\r\n", ""));
			pojo.setEmailModelContent(pojo.getEmailModelContent().replace("\n",
					""));
		}
		sysEmailModelDao.save(pojo);
	}

	/**
	 * 更新Email模版
	 * 
	 * @param pojo
	 */
	public void update(SysEmailModel pojo) {
		// 替换多媒体正文中的“\r\n”特殊字符
		if (pojo.getEmailModelContent() != null
				&& !pojo.getEmailModelContent().equals("")) {
			pojo.setEmailModelContent(pojo.getEmailModelContent().replace(
					"\r\n", ""));
			pojo.setEmailModelContent(pojo.getEmailModelContent().replace("\n",
					""));
		}
		sysEmailModelDao.update(pojo);
	}

	/**
	 * 批量删除邮件模版
	 * 
	 * @param idCommaString
	 */
	public void deleteByKeys(String idCommaString) {
		String notDelete = "(22,?|25,?|26,?|27,?|28,?|29,?|30,?)";// TODO 不能删除的模板ID
		idCommaString = idCommaString.replaceAll(notDelete, "");
		idCommaString = StringUtils.removeEnd(idCommaString, ",");
		if (StringUtils.isNotEmpty(idCommaString)) {
			sysEmailModelDao.deleteByKeys("id", idCommaString.split(","),
					"long");
		}
	}

	/**
	 * 批量更新邮件模版
	 * 
	 * @param pojo
	 * @param idCommaString
	 */
	public void updateByKeys(SysEmailModel pojo, String idCommaString) {
		sysEmailModelDao.updateByKeys(pojo, "id",
				handleToIdLongArray(idCommaString));
	}
}
