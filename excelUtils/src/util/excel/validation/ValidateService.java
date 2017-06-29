package util.excel.validation;


import org.apache.commons.lang3.StringUtils;
import util.excel.entity.ExcelProperty;

import java.lang.reflect.Field;

/**
 * 注解解析
 */
public class ValidateService
{

	private static ExcelProperty dv;

	public ValidateService()
	{
		super();
	}

	//解析的入口
	public static void valid(Object object) throws Exception
	{
		//获取object的类型
		Class<? extends Object> clazz = object.getClass();
		//获取该类型声明的成员
		Field[] fields = clazz.getDeclaredFields();
		//遍历属性
		for (Field field : fields)
		{
			//对于private私有化的成员变量，通过setAccessible来修改器访问权限
			field.setAccessible(true);
			validate(field, object);
			//重新设置会私有权限
			field.setAccessible(false);
		}
	}

	public static void validate(Field field, Object object) throws Exception
	{

		String description;
		Object value;

		//获取对象的成员的注解信息
		dv = field.getAnnotation(ExcelProperty.class);
		value = field.get(object);

		if (dv == null)
			return;

		description = dv.fieldName().equals("") ? field.getName() : dv.fieldName();

		/************* 注解解析工作开始 ******************/
		if (!dv.nullable())
		{
			if (value == null || StringUtils.isBlank(value.toString()))
			{
				throw new Exception(description + "不能为空");
			}
		}
		if (value != null)
		{
			if (length(value.toString()) > dv.maxLength() && dv.maxLength() != 0)
			{
				throw new Exception(description + "长度不能超过" + dv.maxLength());
			}

			if (length(value.toString()) < dv.minLength() && dv.minLength() != 0)
			{
				throw new Exception(description + "长度不能小于" + dv.minLength());
			}

			if (dv.regexType() != RegexType.NONE)
			{
				switch (dv.regexType())
				{
					case NONE:
						break;
					case SPECIALCHAR:
						if (RegexUtils.hasSpecialChar(value.toString()))
						{
							throw new Exception(description + "不能含有特殊字符");
						}
						break;
					case CHINESE:
						if (RegexUtils.isChinese2(value.toString()))
						{
							throw new Exception(description + "不能含有中文字符");
						}
						break;
					case EMAIL:
						if (!RegexUtils.isEmail(value.toString()))
						{
							throw new Exception(description + "地址格式不正确");
						}
						break;
					case IP:
						if (!RegexUtils.isIp(value.toString()))
						{
							throw new Exception(description + "地址格式不正确");
						}
						break;
					case NUMBER:
						if (!RegexUtils.isNumber(value.toString()))
						{
							throw new Exception(description + "不是数字");
						}
						break;
					case PHONENUMBER:
						if (!RegexUtils.isPhoneNumber(value.toString()))
						{
							throw new Exception(description + "不是手机号码");
						}
						break;
					default:
						break;
				}
			}

			if (!dv.regexExpression().equals(""))
			{
				String regex = dv.regexExpression();
				if (!value.toString().matches(regex))
				{
					throw new Exception(description + "格式不正确");
				}
			}
		}
		/************* 注解解析工作结束 ******************/
	}
	
	 /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为3，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为3 */
                valueLength += 3;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
}
