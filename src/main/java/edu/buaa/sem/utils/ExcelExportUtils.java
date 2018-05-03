package edu.buaa.sem.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.context.ContextLoader;

public class ExcelExportUtils {

	/**
	 * 无模板导出
	 * 
	 * @param pojoList
	 * @param response
	 */
	public static void exportToExcel(List<ArrayList<Object>> pojoList,
			HttpServletResponse response) {
		int listSize = pojoList.size();

		try {
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("sheet1");

			/* 遍历数据集pojoList */
			for (int i = 0; i < listSize; i++) {
				ArrayList<Object> pojo = pojoList.get(i);
				// 创建row
				Row row = (Row) sheet.getRow(i);
				if (row == null) {
					row = sheet.createRow(i);
				}
				Iterator<Object> iter = pojo.iterator();
				int entrySize = 0;
				while (iter.hasNext()) {
					Object val = iter.next();
					Cell cell = row.getCell(entrySize);
					if (cell == null) {
						cell = row.createCell(entrySize);
					}
					if (val != null) {
						cell.setCellValue(val.toString());
					}
					entrySize++;
				}
			}

			for (int i = 0; i < pojoList.get(0).size(); i++) {
				sheet.autoSizeColumn(i);
			}

			// HttpServletResponse response = null;
			// TODO HttpServletResponse response =
			// ServletActionContext.getResponse();
			response.reset();
			// 设置response流信息的头类型，MIME码
			response.setContentType("application/msexcel;charset=UTF-8");

			response.addHeader(
					"Content-Disposition",
					"attachment;filename=\""
							+ new String(
									(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss")
											.format(new Date()) + ".xlsx")
											.getBytes("GBK"), "ISO8859_1")
							+ "\"");
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.flush();
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据模板生成excel
	 * 
	 * @param pojoList
	 *            需要导出的数据
	 * @param template
	 *            模板名称,在"/excel/export-template"下的excel文件
	 * @return
	 */
	public static void exportToExcel(List<ArrayList<Object>> pojoList,
			String template) {
		int listSize = pojoList.size();

		// 获取excel模板路径
		String templatePath = "";
		String directory = "/excel/export-template";
		ArrayList<String> excelList = getAllFiles(directory);
		int excelSize = excelList.size();
		for (int i = 0; i < excelSize; i++) {
			templatePath = excelList.get(i);
			if (templatePath.endsWith(template)) {
				break;
			}
		}

		try {
			FileInputStream file = new FileInputStream(templatePath);
			Workbook wb = new XSSFWorkbook(file);
			Sheet sheet = wb.getSheetAt(0);

			/* 遍历数据集pojoList */
			for (int i = 0; i < listSize; i++) {
				ArrayList<Object> pojo = pojoList.get(i);
				// 创建row
				Row row = (Row) sheet.getRow(i + 1);
				if (row == null) {
					row = sheet.createRow(i + 1);
				}
				Iterator<Object> iter = pojo.iterator();
				int entrySize = 0;
				while (iter.hasNext()) {
					Object val = iter.next();
					Cell cell = row.getCell(entrySize);
					if (cell == null) {
						cell = row.createCell(entrySize);
					}
					if (val != null) {
						cell.setCellValue(val.toString());
					}
					entrySize++;
				}
			}

			HttpServletResponse response = null;
			// TODO HttpServletResponse response =
			// ServletActionContext.getResponse();
			response.reset();
			// 设置response流信息的头类型，MIME码
			response.setContentType("application/msexcel;charset=UTF-8");

			response.addHeader(
					"Content-Disposition",
					"attachment;filename=\""
							+ new String(
									(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss")
											.format(new Date()) + ".xlsx")
											.getBytes("GBK"), "ISO8859_1")
							+ "\"");
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.flush();
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 培训素材Excel导出
	 * 
	 * @param pojoList
	 * @param response
	 */
	public static void exportExcelToLocal(List<ArrayList<Object>> pojoList,
			String fileName, String path) {
		int listSize = pojoList.size();

		try {
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("sheet1");

			/* 遍历数据集pojoList */
			for (int i = 0; i < listSize; i++) {
				ArrayList<Object> pojo = pojoList.get(i);
				// 创建row
				Row row = (Row) sheet.getRow(i);
				if (row == null) {
					row = sheet.createRow(i);
				}
				Iterator<Object> iter = pojo.iterator();
				int entrySize = 0;
				while (iter.hasNext()) {
					Object val = iter.next();
					Cell cell = row.getCell(entrySize);
					if (cell == null) {
						cell = row.createCell(entrySize);
					}
					if (val != null) {
						cell.setCellValue(val.toString());
					}
					entrySize++;
				}
			}

			for (int i = 0; i < pojoList.get(0).size(); i++) {
				sheet.autoSizeColumn(i);
			}
			String realPath = FileUtils.getRealPath(path);
			String name = fileName
					+ '-'
					+ new SimpleDateFormat("yyyy-MM-dd HH-mm-ss")
							.format(new Date()) + ".xlsx";
			FileOutputStream fout = new FileOutputStream(realPath + '/' + name);
			wb.write(fout);
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 培训素材Excel导出带表头
	 * 
	 * @param pojoList
	 * @param response
	 */
	public static void exportExcelToLocalWithHeader(List<ArrayList<Object>> pojoList,
			String fileName, String path,List<String> headerList) {
		int listSize = pojoList.size();

		try {
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("sheet1");
			//生成表头
			Row headerRow = (Row) sheet.createRow(0);
			 for(int j=0;j<headerList.size();j++){  
		            //设置列宽   基数为256  
		            //sheet.setColumnWidth(j, 30*256);  
		            Cell cell = headerRow.createCell(j);  
		            cell.setCellValue(headerList.get(j).toString());  
		        }  
		          
			

			/* 遍历数据集pojoList */
			for (int i = 0; i < listSize; i++) {
				ArrayList<Object> pojo = pojoList.get(i);
				// 创建row
				Row row = (Row) sheet.getRow(i+1);
				if (row == null) {
					row = sheet.createRow(i+1);
				}
				Iterator<Object> iter = pojo.iterator();
				int entrySize = 0;
				while (iter.hasNext()) {
					Object val = iter.next();
					Cell cell = row.getCell(entrySize);
					if (cell == null) {
						cell = row.createCell(entrySize);
					}
					if (val != null) {
						cell.setCellValue(val.toString());
					}
					entrySize++;
				}
			}

			for (int i = 0; i < pojoList.get(0).size(); i++) {
				sheet.autoSizeColumn(i);
			}
			String realPath = FileUtils.getRealPath(path);
			String name = fileName
					+ '-'
					+ new SimpleDateFormat("yyyy-MM-dd HH-mm-ss")
							.format(new Date()) + ".xlsx";
			FileOutputStream fout = new FileOutputStream(realPath + '/' + name);
			wb.write(fout);
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取指定目录下的文件,只包含一级目录下的
	 * 
	 * @param directory
	 * @return
	 */
	public static ArrayList<String> getAllFiles(String directory) {
		String filePath = ContextLoader.getCurrentWebApplicationContext()
				.getServletContext().getRealPath(directory);
		ArrayList<String> fileList = new ArrayList<String>();
		File dir = new File(filePath);
		File[] files = dir.listFiles();
		for (File file : files) {
			if (!file.isDirectory()) {
				fileList.add(file.getAbsolutePath());
			}
		}
		return fileList;
	}

}
