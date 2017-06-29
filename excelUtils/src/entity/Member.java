package entity;

import org.apache.poi.ss.usermodel.Cell;
import util.excel.entity.ExcelEntity;
import util.excel.entity.ExcelProperty;

import java.io.Serializable;

/**
 * Created by xy on 2017/6/28.
 */
@ExcelEntity
public class Member implements Serializable {
    private static final long serialVersionUID = 2861317560659750439L;

    @ExcelProperty(fieldName = "身份证号,身份证", fieldIndex = 0, fieldType = Cell.CELL_TYPE_STRING, nullable = true, minLength = 1, maxLength = 100)
    private String idCard;//身份证号
    @ExcelProperty(fieldName = "会员姓名，姓名", fieldIndex = 1, fieldType = Cell.CELL_TYPE_STRING, nullable = true, minLength = 1, maxLength = 50)
    private String name;//会员姓名
    @ExcelProperty(fieldName = "手机号，手机，联系电话", fieldIndex = 2, fieldType = Cell.CELL_TYPE_STRING, nullable = true, minLength = 1, maxLength = 50)
    private String mobile;//手机号
    @ExcelProperty(fieldName = "qq", fieldIndex = 3, fieldType = Cell.CELL_TYPE_STRING, nullable = true, minLength = 1, maxLength = 50)
    private String qq;//qq
    @ExcelProperty(fieldName = "微信号", fieldIndex = 4, fieldType = Cell.CELL_TYPE_STRING, nullable = true, minLength = 1, maxLength = 50)
    private String weixin;//微信号
    @ExcelProperty(fieldName = "邮箱", fieldIndex = 5, fieldType = Cell.CELL_TYPE_STRING, nullable = true, minLength = 1, maxLength = 50)
    private String email;//邮箱
    @ExcelProperty(fieldName = "性别", fieldIndex = 6, fieldType = Cell.CELL_TYPE_STRING, nullable = true, minLength = 1, maxLength = 50)
    private Integer sex;//性别 0男 1女


    @Override
    public String toString() {
        return "Member{" +
                "idCard='" + idCard + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", qq='" + qq + '\'' +
                ", weixin='" + weixin + '\'' +
                ", email='" + email + '\'' +
                ", sex=" + sex +
                '}';
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
