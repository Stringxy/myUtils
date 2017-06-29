package util.excel.model;

public class HeaderData
{

	private String filed;
	private String name;

	public HeaderData(String filed, String name)
	{
		this.filed = filed;
		this.name = name;
	}

	public String getFiled()
	{
		return filed;
	}

	public void setFiled(String filed)
	{
		this.filed = filed;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
