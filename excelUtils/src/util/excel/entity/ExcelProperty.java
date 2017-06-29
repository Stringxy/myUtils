package util.excel.entity;


import util.excel.validation.RegexType;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelProperty
{
	String fieldName() default "";
	
	/**EXCEL 列号*/
	int fieldIndex() default -1;
	
	/**EXCEl CELL 类型*/
	int fieldType() default -1;

	/**是否可以为空*/
	boolean nullable() default false;

	/**最大长度*/
	int maxLength() default 0;

	/**最小长度*/
	int minLength() default 0;

	/**
	 * 
	 * Description：<正则表达式校验规则>
	 *
	 * @return
	 */
	String regexExpression() default "";

	//提供几种常用的正则验证
	RegexType regexType() default RegexType.NONE;

	/**
	 * Description：<错误提示信息>
	 * 
	 * @return
	 */
	String errorMessage() default "";

}
