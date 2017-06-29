package util.excel.entity;

import java.lang.reflect.Field;

public class ExcelEntityField
{
	private String columnName;
	
	private int columnIndex;
	
	private int columnType;
	
	private Field field;


	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public int getColumnIndex()
	{
		return columnIndex;
	}
	public void setColumnIndex(int columnIndex)
	{
		this.columnIndex = columnIndex;
	}
	public int getColumnType()
	{
		return columnType;
	}
	public void setColumnType(int columnType)
	{
		this.columnType = columnType;
	}
	

}
