package util.excel;


import org.apache.poi.ss.usermodel.Cell;
import util.excel.entity.ExcelEntity;
import util.excel.entity.ExcelProperty;

@ExcelEntity
public class KaoQin
{

	@ExcelProperty(fieldName = "工号", fieldIndex = 0, fieldType = Cell.CELL_TYPE_STRING, nullable = true, minLength = 1, maxLength = 50)
	private String id;
	@ExcelProperty(fieldName = "姓名", fieldIndex = 0, fieldType = Cell.CELL_TYPE_STRING, nullable = true, minLength = 1, maxLength = 50)
	private String name;
	@ExcelProperty(fieldName = "出勤日期", fieldIndex = 0, fieldType = Cell.CELL_TYPE_STRING, nullable = true, minLength = 1, maxLength = 50)
	private String date;
	@ExcelProperty(fieldName = "上班时间", fieldIndex = 0, fieldType = Cell.CELL_TYPE_STRING, nullable = true, minLength = 1, maxLength = 50)
	private String sbTime;
	@ExcelProperty(fieldName = "下班时间", fieldIndex = 0, fieldType = Cell.CELL_TYPE_STRING, nullable = true, minLength = 1, maxLength = 50)
	private String xbTime;
	@ExcelProperty(fieldName = "说明", fieldIndex = 0, fieldType = Cell.CELL_TYPE_STRING, nullable = true, minLength = 1, maxLength = 50)
	private String note;

	public KaoQin(String id, String name)
	{
		super();
		this.id = id;
		this.name = name;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getSbTime()
	{
		return sbTime;
	}

	public void setSbTime(String sbTime)
	{
		this.sbTime = sbTime;
	}

	public String getXbTime()
	{
		return xbTime;
	}

	public void setXbTime(String xbTime)
	{
		this.xbTime = xbTime;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

}
