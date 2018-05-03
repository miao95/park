package edu.buaa.sem.system.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.buaa.sem.common.BaseService;
import edu.buaa.sem.common.ErrorMessage;
import edu.buaa.sem.po.SysRole;
import edu.buaa.sem.po.SysRoleAuthority;
import edu.buaa.sem.po.SysUserRole;
import edu.buaa.sem.system.dao.SysRoleDao;
import edu.buaa.sem.utils.ExcelExportUtils;

@Service
public class SysRoleService extends BaseService {

	@Autowired
	private SysRoleDao sysRoleDao;

	public List<SysRole> findByExampleForPagination(SysRole pojo, String page,
			String rows, String sort, String order) {
		List<SysRole> pojos;
		if (sort != null && !sort.equals("") && order != null
				&& !order.equals("")) {
			pojos = sysRoleDao.findByExampleForPagination(pojo, true, page,
					rows, sort, order);
		} else {
			pojos = sysRoleDao.findByExampleForPagination(pojo, true, page,
					rows, "name", "asc");
		}
		return pojos;
	}

	public long countByExample(SysRole pojo) {
		long count = sysRoleDao.countByExample(pojo, true);
		return count;
	}

	public void save(SysRole pojo) {
		sysRoleDao.save(pojo);
	}

	public void update(SysRole pojo) {
		sysRoleDao.update(pojo);
	}

	/**
	 * 根据上传的Excel文件批量导入数据库
	 * 
	 * @param file
	 * @return
	 */
	public String importFromExcel(File file) {
		// 导入Excel预定义的列名{"角色名","备注","启用"}
		if (file == null) {
			return ErrorMessage.IMPORT_FAILURE;
		}
		try {
			InputStream inputStream = new FileInputStream(file);
			Workbook workBook = WorkbookFactory.create(inputStream);
			Sheet sheet = workBook.getSheetAt(0);

			int rowCount = sheet.getPhysicalNumberOfRows();// 一共多少行
			for (int r = 1; r < rowCount; r++) {// 从第二行循环获得数据（第一行为列名）
				Row row = sheet.getRow(r);
				if (row.getCell(0) == null) {// 如果主键为空则不导入本行记录
					continue;
				}
				// 以name作为副主键，判断是更新还是插入
				SysRole pojo;
				List<SysRole> pojos = sysRoleDao.findByPropertyEqual("name",
						handleCelltoString(row.getCell(0)), "String");
				if (!pojos.isEmpty()) {
					pojo = pojos.get(0);
					pojo.setDescription(handleCelltoString(row.getCell(1)));
					pojo.setEnabled(handleCelltoString(row.getCell(2)));
				} else {
					pojo = new SysRole();
					pojo.setName(handleCelltoString(row.getCell(0)));
					pojo.setDescription(handleCelltoString(row.getCell(1)));
					pojo.setEnabled(handleCelltoString(row.getCell(2)));
				}
				sysRoleDao.saveOrUpdate(pojo);
			}
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return ErrorMessage.IMPORT_FAILURE;
		}
		return ErrorMessage.IMPORT_SUCCESS;
	}

	/**
	 * 导出数据到Excel
	 * 
	 */
	public void exportToExcel(SysRole pojo) {
		List<SysRole> pojos = sysRoleDao.findByExample(pojo, true);

		List<ArrayList<Object>> pojoList = new ArrayList<ArrayList<Object>>();
		Iterator<SysRole> iterator = pojos.iterator();

		/* 遍历pojos,将需要的属性放入pojo中 */
		while (iterator.hasNext()) {
			SysRole temp = (SysRole) iterator.next();

			ArrayList<Object> tempPojo = new ArrayList<Object>();
			tempPojo.add(temp.getName());
			tempPojo.add(temp.getDescription());
			tempPojo.add(temp.getEnabled());

			pojoList.add(tempPojo);
		}
		ExcelExportUtils.exportToExcel(pojoList, "SysRole.xlsx");
	}

	public void deleteByKeys(String idCommaString) {
		sysRoleDao.deleteByKeys("id", idCommaString.split(","), "long");
	}

	public void updateByKeys(SysRole pojo, String idCommaString) {
		sysRoleDao.updateByKeys(pojo, "id", handleToIdLongArray(idCommaString));
	}

	/**
	 * 对所有SysRole按照关联实体SysUserRole进行匹配标记
	 * 
	 */
	public List<HashMap<String, Object>> matchRoleUser(List<SysRole> pojos,
			List<SysUserRole> jointPojos) {
		List<HashMap<String, Object>> pojoList = new ArrayList<>();
		for (int i = 0; i < pojos.size(); i++) {
			int flag = 0;// 用于标注此role是否已经与user关联，关联则为1，否则为0。每次对roleList循环后要将flag重新赋值
			long relevanceId = 0;// 用于记录关联上的role与user关系的记录的自增id。如果最后放入json中为0，说明对于这个role，这个指定的user没有与他关联，所以在日后的save时，就根据relevanceId，判断是要插入还是更新。

			SysRole pojo = pojos.get(i);
			for (int j = 0; j < jointPojos.size() && flag == 0; j++) {
				SysUserRole jointPojo = jointPojos.get(j);

				if (jointPojo.getSysRoleId().equals(pojo.getId())) {
					// 已经关联上，则将标志改为1
					flag = 1;
					// 已经关联上，则将关联记录的id记录下来，并放入json（以便在日后的存储是可以判断是进行插入还是更新）
					relevanceId = jointPojo.getId();
				}
			}

			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("id", pojo.getId());
			hashMap.put("name", pojo.getName());
			hashMap.put("description", pojo.getDescription());
			hashMap.put("relevance", flag);
			hashMap.put("relevanceId", relevanceId);

			pojoList.add(hashMap);
		}
		return pojoList;
	}

	/**
	 * 对所有SysRole按照关联实体SysRoleAuthority进行匹配标记
	 * 
	 */
	public List<HashMap<String, Object>> matchRoleAuthority(
			List<SysRole> pojos, List<SysRoleAuthority> jointPojos) {
		List<HashMap<String, Object>> pojoList = new ArrayList<>();
		for (int i = 0; i < pojos.size(); i++) {
			int flag = 0;// 用于标注此role是否已经与authority关联，关联则为1，否则为0。每次对roleList循环后要将flag重新赋值
			long relevanceId = 0;// 用于记录关联上的role与authority关系的记录的自增id。如果最后放入json中为0，说明对于这个role，这个指定的authority没有与他关联，所以在日后的save时，就根据relevanceId，判断是要插入还是更新。

			SysRole pojo = pojos.get(i);
			for (int j = 0; j < jointPojos.size() && flag == 0; j++) {
				SysRoleAuthority jointPojo = jointPojos.get(j);

				if (jointPojo.getSysRoleId().equals(pojo.getId())) {
					// 已经关联上，则将标志改为1
					flag = 1;
					// 已经关联上，则将关联记录的id记录下来，并放入json（以便在日后的存储是可以判断是进行插入还是更新）
					relevanceId = jointPojo.getId();
				}
			}

			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("id", pojo.getId());
			hashMap.put("name", pojo.getName());
			hashMap.put("description", pojo.getDescription());
			hashMap.put("relevance", flag);
			hashMap.put("relevanceId", relevanceId);

			pojoList.add(hashMap);

		}
		return pojoList;
	}

	public List<SysRole> findRoleForRecommendation(String page, String rows,
			String sort, String order) {
		return sysRoleDao.findRoleForRecommendation(page, rows, sort, order);
	}

	public long countRoleForRecommendation() {
		return sysRoleDao.countRoleForRecommendation();
	}
}
