package util.excel;


import org.apache.poi.xssf.usermodel.*;
import util.excel.model.HeaderData;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 
 * Description：<类装填Excel 2003版>
 * 
 * @author 陈科
 * @version [1.0]
 * @date [2015年8月24日]
 */
public class EntityToExcel
{

	public List<HeaderData> header;

	private XSSFWorkbook hWorkbook;
	private XSSFSheet hSheet;

	public EntityToExcel()
	{
		hWorkbook = new XSSFWorkbook();
		hSheet = hWorkbook.createSheet();
	}

	public void createTableHeader(List<HeaderData> list)
	{
		header = list;

		int[] columnWidth = new int[]{2980,2290,2980,3310,4140,3110,3110};
		XSSFCellStyle cellStyle = hWorkbook.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中 
//		cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);// 设置背景色
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框

		XSSFFont font = hWorkbook.createFont();
		font.setFontName("宋体");
		font.setBold(true); //粗体
		font.setFontHeightInPoints((short) 12);//设置字体大小
		cellStyle.setFont(font);

		XSSFRow firstrow = hSheet.createRow(0); // 下标为0的行开始
		XSSFCell[] firstcell = new XSSFCell[header.size()];
		for (int i = 0; i < header.size(); i++)
		{
			hSheet.setColumnWidth(i, columnWidth[i]); //设置列宽

			HeaderData hd = header.get(i);
			firstcell[i] = firstrow.createCell(i);
			firstcell[i].setCellValue(new XSSFRichTextString(hd.getName()));
			firstcell[i].setCellStyle(cellStyle);
		}

	}

	public void fillData(List<?> data) throws Exception
	{
		
		XSSFCellStyle cellStyle = hWorkbook.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中 
//		cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);// 设置背景色
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);

		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
		try
		{
			int indexRow = 1;
			for (Object item : data)
			{
				XSSFRow row = hSheet.createRow(indexRow);
				int indexCell = 0;
				if (header != null)
				{
					for (HeaderData hd : header)
					{
						//Method method1 = item.getClass().getDeclaredMethod("get" + toCapitalizeCamelCase(hd.getFiled()));
						String methodName = "get" + toCapitalizeCamelCase(hd.getFiled());
						Method method = this.getDeclaredMethod(item, methodName);
						if (method == null)
						{
							throw new NoSuchMethodException(item.getClass().getName() + "." + methodName);
						}
						Object d = method.invoke(item);

						XSSFCell cell = row.createCell(indexCell);

						cell.setCellStyle(cellStyle);
						cell.setCellValue(String.valueOf(d));

						indexCell++;
					}
				}
				indexRow++;
			}
		}
		catch (Exception e)
		{
			throw e;
		}

	}

	public void toExcel(String path) throws Exception
	{
		try
		{
			OutputStream out = new FileOutputStream(path);
			hWorkbook.write(out);
		}
		catch (FileNotFoundException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			throw e;
		}
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

	/**
	 * 
	 * Description：循环向上转型，获得对象的DeclaredMethod
	 * 
	 * @param object
	 *            子类对象
	 * @param name
	 *            方法名
	 * @param parameterTypes
	 *            方法参数类型
	 * @return
	 */
	public Method getDeclaredMethod(Object object, String name, Class<?>... parameterTypes)
	{
		Method method = null;
		for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass())
		{
			try
			{
				method = clazz.getDeclaredMethod(name, parameterTypes);
				return method;
			}
			catch (Exception e)
			{

			}
		}
		return null;
	}

	

}
