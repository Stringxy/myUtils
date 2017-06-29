package util.excel;


import org.apache.poi.ss.usermodel.*;
import util.excel.entity.ExcelEntity;
import util.excel.entity.ExcelEntityField;
import util.excel.entity.ExcelProperty;
import util.excel.model.ExcelContext;
import util.excel.model.ExcelHeader;
import util.excel.validation.ValidateService;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/***
 * 
 * Description：<Excel导入 自动校验装填实体> 依懒POI
 * 
 * @author 陈科
 * @version [1.0.001]
 * @date [2015年6月1日]
 */

public class ExcelToEntity<T>
{
	private Class<T> entityClass;
	// 最小列数目
	final public static int MIN_ROW_COLUMN_COUNT = 1;
	// 最大列数目
	private int maxColumnIndex;
	// 从Excel中读取的标题栏
	private List<ExcelHeader> headers = new ArrayList<ExcelHeader>();
	// 从Excel中读取的数据
	private List<List<ExcelContext>> datas = new ArrayList<List<ExcelContext>>();

	private List<String> errorMessage = new ArrayList<String>();

	//实体字段信息
	private List<ExcelEntityField> eefields = new ArrayList<ExcelEntityField>();

	private Workbook wb = null;
	
	public ExcelToEntity(Class<T> classType) throws Exception
	{
		entityClass = classType;
		ExcelEntity excelEntity = entityClass.getAnnotation(ExcelEntity.class);
		if (excelEntity == null)
		{
//			throw new LogicException(ErrorCode.FAILED, "转换的实体必须存在@ExcelEntity!");
		}

		// 创建Excel实体字段信息
		eefields = getEntityFields(entityClass);
	}

	public void openExcel(String file) throws Exception
	{
		wb = WorkbookFactory.create(new File(file));
	}

	public void openExcel(InputStream is) throws Exception
	{
		wb = WorkbookFactory.create(is);
	}

	public void readExcel(int sheetIndex) throws Exception
	{
		try
		{
			if (sheetIndex > wb.getNumberOfSheets())
			{
//				throw new LogicException(ErrorCode.FAILED, "Sheet 超界");
			}
			Sheet sheet = wb.getSheetAt(sheetIndex);

			int rowStart = sheet.getFirstRowNum();

			int rowEnd = sheet.getLastRowNum();
			// 读取EXCEL标题栏
			parseExcelHeader(sheet.getRow(0));
			// 读取EXCEL数据区域内容
			parseExcelData(sheet, rowStart + 1, rowEnd);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	public List<T> toEntitys() throws Exception
	{
		// 创建实体对象集
		List<T> entitys = new ArrayList<T>();

		//记录当前行号
		int rowIndex = 2;

		for (List<ExcelContext> rowData : datas)
		{
			T obj = entityClass.newInstance();

			// 遍历实体对象的实体字段，通过反射为实体字段赋值
			for (ExcelEntityField eef : eefields)
			{
				for (ExcelContext data : rowData)
				{
					if (data.getName().equals(eef.getColumnName()))
					{
						// 实体数据填充
						Method method = obj.getClass().getDeclaredMethod("set" + toCapitalizeCamelCase(eef.getField().getName()), eef.getField().getType());
						try
						{
							method.invoke(obj, data.getValue());
						}
						catch (Exception e)
						{
//							throw new LogicException(ErrorCode.FAILED, "字段" + eef.getColumnName() + "出错!");
						}
					}
				}
			}

			//数据校验
			try
			{
				ValidateService.valid(obj);
				entitys.add(obj);
			}
			catch (Exception e)
			{
				errorMessage.add("第" + rowIndex + "行：" + e.getMessage());
//				Log.error("第" + rowIndex + "行：" + e.getMessage());
			}

			rowIndex++;
		}

		return entitys;
	}

	public List<String> getLoadDataErrorMessage()
	{
		return errorMessage;
	}

	private String toCapitalizeCamelCase(String name)
	{
		if (name == null)
		{
			return null;
		}

		StringBuilder sb = new StringBuilder(name.length());
		boolean upperCase = false;
		for (int i = 0; i < name.length(); i++)
		{
			char c = name.charAt(i);

			if (c == '_')
			{
				upperCase = true;
			}
			else if (upperCase)
			{
				sb.append(Character.toUpperCase(c));
				upperCase = false;
			}
			else
			{
				sb.append(c);
			}
		}
		name = sb.toString();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private List<ExcelEntityField> getEntityFields(Class<T> classType) throws Exception
	{
		List<ExcelEntityField> eefs = new ArrayList<ExcelEntityField>();
		// 遍历所有字段
		Field[] allFields = classType.getDeclaredFields();
		for (Field field : allFields)
		{
			ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
			// 只对含有@ExcelProperty注解的字段进行赋值
			if (excelProperty == null)
			{
				continue;
			}
			ExcelEntityField eef = new ExcelEntityField();

			String key = excelProperty.fieldName().trim();// Excel Header名
			int index = excelProperty.fieldIndex(); //Excel Header Index
			int type = excelProperty.fieldType(); //Excel 

			eef.setField(field);
			eef.setColumnIndex(index);
			eef.setColumnName(key);
			eef.setColumnType(type);

			eefs.add(eef);
		}
		return eefs;
	}

	private void parseExcelHeader(Row row)
	{
		maxColumnIndex = Math.max(row.getLastCellNum(), MIN_ROW_COLUMN_COUNT);
		// 初始化headers，每一列的标题
		for (int columnIndex = 0; columnIndex < maxColumnIndex; columnIndex++)
		{
			Cell cell = row.getCell(columnIndex, Row.RETURN_BLANK_AS_NULL);

			ExcelHeader header = new ExcelHeader();
			header.setName(getCellValue(cell).trim());
			header.setIndex(columnIndex);

			headers.add(header);
		}
	}

	private void parseExcelData(Sheet sheet, int rowStart, int rowEnd) throws Exception
	{
		for (int rowIndex = rowStart; rowIndex <= rowEnd; rowIndex++)
		{
			Row row = sheet.getRow(rowIndex);
			if (row != null)
			{
				//读取遍历一行中的每一列
				List<ExcelContext> rowData = null;
				for (ExcelHeader header : headers)
				{
					System.out.println("index:"+header.getIndex());
					Cell cell = row.getCell(header.getIndex(), Row.RETURN_BLANK_AS_NULL);
					if (cell != null)
						for (ExcelEntityField eef : eefields)
						{
							System.out.println(eef.getColumnName().indexOf(header.getName()));
							if (eef.getColumnName().indexOf(header.getName())>= 0 )
							{
								System.out.println("cell:"+cell.getCellType());
								System.out.println("eef :"+eef.getColumnType());
								if (eef.getColumnType() > -1 && cell.getCellType() != eef.getColumnType())
								{}
//									throw new LogicException(ErrorCode.FAILED, "单元格数据内容格式不正确：列名:" + header.getName() + ",行号:" + (rowIndex + 1) + "");

								ExcelContext data = new ExcelContext();
								data.setIndex(eef.getColumnIndex());
								data.setName(eef.getColumnName());
								data.setValue(getCellValue(cell).trim());
								if (rowData == null)
									rowData = new ArrayList<ExcelContext>();

								rowData.add(data);
								break;
							}
						}
				}
				if (rowData != null)
					datas.add(rowData);
			}
		}
	}

	private String getCellValue(Cell cell)
	{
		// 如果单元格为空的，则返回空字符串
		if (cell == null)
		{
			return "";
		}

		// 根据单元格类型，以不同的方式读取单元格的值
		String value = "";
		switch (cell.getCellType())
		{
			case Cell.CELL_TYPE_STRING:
				value = cell.getRichStringCellValue().getString();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell))
				{
					value = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(cell.getDateCellValue());
				}
				else
				{
					value = (long) cell.getNumericCellValue() + "";
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				value = cell.getBooleanCellValue() ? "TRUE" : "FALSE";
				break;
			case Cell.CELL_TYPE_FORMULA:
				value = cell.getCellFormula();
				break;
			default:
				break;
		}
		return value;
	}

}
