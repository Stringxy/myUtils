package util.excel;



import entity.Member;
import util.excel.model.HeaderData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestExcel
{

	private static String ID = "57175";
	private static String NAME = "姚辉";

	public static void main(String[] args)
	{
		createExcel(2017, 4);
	}

	public static void createExcel(int year, int month)
	{
		EntityToExcel e2e = new EntityToExcel();
		List<HeaderData> hdList = new ArrayList<HeaderData>();

		hdList.add(new HeaderData("idCard", "身份证号"));
		hdList.add(new HeaderData("name", "会员姓名"));
		hdList.add(new HeaderData("mobile", "手机号"));
		hdList.add(new HeaderData("qq", "qq"));
		hdList.add(new HeaderData("weixin", "微信号"));
		hdList.add(new HeaderData("email", "邮箱"));
		hdList.add(new HeaderData("sex", "性别"));
		e2e.createTableHeader(hdList);

		// 装填数据
		try
		{
			e2e.fillData(genList());

			String filePath = "e:\\testexcel\\" ;
			File file = new File(filePath);
			if(!file.exists())
			{
				file.mkdirs();
			}
			String fileName = getYearMonth(year, month) + "_考勤_重庆物联网_姚辉.xlsx";
			e2e.toExcel(filePath+fileName);
			System.out.println(filePath+fileName);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String getYearMonth(int year, int month)
	{
		return year + "-" + (month > 9 ? (month + "") : ("0" + month));
	}

	public static List<Member> genList()
	{
		List<Member> list = new ArrayList<Member>();
		Member m=new Member();
		m.setIdCard("111111111111");
		m.setName("222222222");
		list.add(m);
		return list;
	}

//	public static List<Member> genList(int year, int month)
//	{
//		List<Member> list = new ArrayList<Member>();
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.MONTH, month - 1);
//		calendar.set(Calendar.YEAR, year);
//		calendar.set(Calendar.DAY_OF_MONTH, 1);
//
//		boolean end = true;
//		while (end)
//		{
//			KaoQin kq = new Member(ID, NAME);
//			kq.setDate(dateFormat(calendar.getTime()));
//			kq.setSbTime(getSbTime());
//			kq.setXbTime(getXbTime());
//			if (calendar.get(Calendar.DAY_OF_WEEK) == 7)
//			{
//				kq.setNote("周六");
//				kq.setSbTime("");
//				kq.setXbTime("");
//			}
//			else if (calendar.get(Calendar.DAY_OF_WEEK) == 1)
//			{
//				kq.setNote("周日");
//				kq.setSbTime("");
//				kq.setXbTime("");
//			}
//			else
//			{
//				kq.setNote("");
//			}
//			list.add(kq);
//			calendar.add(Calendar.DAY_OF_MONTH, 1);
//			if (calendar.get(Calendar.DAY_OF_MONTH) == 1)
//			{
//				end = false;
//			}
//
//		}
//		return list;
//	}

	private static String dateFormat(Date date)
	{
		SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd");
		return sfd.format(date);
	}

	private static String getSbTime()
	{
		int random = new Random().nextInt(11) + 7;
		return "08:" + (random > 9 ? (random + "") : ("0" + random));
	}

	private static String getXbTime()
	{
		int random = new Random().nextInt(31) + 3;
		return "18:" + (random > 9 ? (random + "") : ("0" + random));
	}

}
