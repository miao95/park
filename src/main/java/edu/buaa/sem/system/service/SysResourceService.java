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
import edu.buaa.sem.po.SysAuthorityResource;
import edu.buaa.sem.po.SysResource;
import edu.buaa.sem.system.dao.SysResourceDao;
import edu.buaa.sem.utils.ExcelExportUtils;

@Service
public class SysResourceService extends BaseService {

	@Autowired
	private SysResourceDao sysResourceDao;

	public List<SysResource> findByExampleForPagination(SysResource pojo,
			String page, String rows, String sort, String order) {
		List<SysResource> pojos;
		if (sort != null && !sort.equals("") && order != null
				&& !order.equals("")) {
			pojos = sysResourceDao.findByExampleForPagination(pojo, true, page,
					rows, sort, order);
		} else {
			pojos = sysResourceDao.findByExampleForPagination(pojo, true, page,
					rows, "name", "asc");
		}
		return pojos;

	}

	public long countByExample(SysResource pojo) {
		long count = sysResourceDao.countByExample(pojo, true);
		return count;
	}

	public void save(SysResource pojo) {
		sysResourceDao.save(pojo);
	}

	public void update(SysResource pojo) {
		sysResourceDao.update(pojo);
	}

	/**
	 * 根据上传的Excel文件批量导入数据库
	 * 
	 * @param file
	 * @return
	 */
	public String importFromExcel(File file) {
		// 导入Excel预定义的列名{"用户名","备注","是否启用"}
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
				SysResource pojo;
				List<SysResource> pojos = sysResourceDao.findByPropertyEqual(
						"name", handleCelltoString(row.getCell(0)), "String");
				if (!pojos.isEmpty()) {
					pojo = pojos.get(0);
					pojo.setDescription(handleCelltoString(row.getCell(1)));
					pojo.setType(handleCelltoString(row.getCell(2)));
					pojo.setAddress(handleCelltoString(row.getCell(3)));
					pojo.setEnabled(handleCelltoString(row.getCell(4)));

				} else {
					pojo = new SysResource();
					pojo.setName(handleCelltoString(row.getCell(0)));
					pojo.setDescription(handleCelltoString(row.getCell(1)));
					pojo.setType(handleCelltoString(row.getCell(2)));
					pojo.setAddress(handleCelltoString(row.getCell(3)));
					pojo.setEnabled(handleCelltoString(row.getCell(4)));
				}
				sysResourceDao.saveOrUpdate(pojo);
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
	public void exportToExcel(SysResource pojo) {
		List<SysResource> pojos = sysResourceDao.findByExample(pojo, true);

		List<ArrayList<Object>> pojoList = new ArrayList<ArrayList<Object>>();
		Iterator<SysResource> iterator = pojos.iterator();

		/* 遍历pojos,将需要的属性放入pojo中 */
		while (iterator.hasNext()) {
			SysResource temp = (SysResource) iterator.next();

			ArrayList<Object> tempPojo = new ArrayList<Object>();
			tempPojo.add(temp.getName());
			tempPojo.add(temp.getDescription());
			tempPojo.add(temp.getType());
			tempPojo.add(temp.getAddress());
			tempPojo.add(temp.getEnabled());

			pojoList.add(tempPojo);
		}
		ExcelExportUtils.exportToExcel(pojoList, "SysResource.xlsx");
	}

	public void deleteByKeys(String idCommaString) {
		sysResourceDao.deleteByKeys("id", idCommaString.split(","), "long");
	}

	public void updateByKeys(SysResource pojo, String idCommaString) {
		sysResourceDao.updateByKeys(pojo, "id",
				handleToIdLongArray(idCommaString));
	}

	/**
	 * 对所有SysResource按照关联实体SysAuthorityResource进行匹配标记
	 * 
	 */
	public List<HashMap<String, Object>> matchResourceAuthority(
			List<SysResource> pojos, List<SysAuthorityResource> jointPojos) {
		List<HashMap<String, Object>> pojoList = new ArrayList<>();
		for (int i = 0; i < pojos.size(); i++) {
			int flag = 0;// 用于标注此resource是否已经与authority关联，关联则为1，否则为0。每次对resourceList循环后要将flag重新赋值
			long relevanceId = 0;// 用于记录关联上的resource与authority关系的记录的自增id。如果最后放入json中为0，说明对于这个resource，这个指定的authority没有与他关联，所以在日后的save时，就根据relevanceId，判断是要插入还是更新。

			SysResource pojo = pojos.get(i);
			for (int j = 0; j < jointPojos.size() && flag == 0; j++) {
				SysAuthorityResource jointPojo = jointPojos.get(j);

				if (jointPojo.getSysResourceId().equals(pojo.getId())) {
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

	public List<HashMap<String, Object>> matchResourceAuthority(
			List<SysAuthorityResource> jointPojos) {
		List<HashMap<String, Object>> pojoList = new ArrayList<>();
		for (int i = 0; i < jointPojos.size(); i++) {
			SysResource pojo = sysResourceDao.findByKey(jointPojos.get(i)
					.getSysResourceId());
			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("name", pojo.getName());
			hashMap.put("description", pojo.getDescription());
			pojoList.add(hashMap);
		}
		return pojoList;
	}

}
