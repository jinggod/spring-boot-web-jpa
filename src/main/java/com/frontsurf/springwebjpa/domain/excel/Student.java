package com.frontsurf.springwebjpa.domain.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.frontsurf.springwebjpa.common.base.entity.DataEntity;
import com.frontsurf.springwebjpa.common.constant.CommonConstant;
import com.frontsurf.springwebjpa.common.validate.Import;
import com.frontsurf.springwebjpa.common.validate.Save;
import com.frontsurf.springwebjpa.common.validate.Update;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Author xu.xiaojing
 * @Date 2019/10/23 19:06
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */
@Table(name = "student")
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLDelete(sql = "update student set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
@SQLDeleteAll(sql = "update student set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
@Where(clause = "del_flag=" + CommonConstant.DEL_FLAG_0)
@ExcelTarget("student")
public class Student extends DataEntity<String> {
    /**
     * 学生名称
     */
    @Excel(name = "姓名_jtyStudent", isImportField = "true,false_students")
    @NotBlank(message = "学生名称不能为空", groups = {Save.class,Import.class})
    // @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]{0,50}", groups = {Save.class, Update.class}, message = "学校名称长度不能超过50，可以是英文或数字或中文")
    @Length(min = 0, max = 50, message = "姓名不能超过255",groups = {Save.class,Update.class,Import.class})
    private String name;

    /**
     * 学生学号
     */
    @Excel(name = "学号_jtyStudent,学号_students", isImportField = "true")
    @NotBlank(message = "学生学号不能为空", groups = {Save.class,Import.class})
    private String studentNum;
    /**
     * 性别：0-男；1-女
     */
    @Excel(name = "性别", replace = {"男_0", "女_1"}, isImportField = "true,false_students")
    @NotNull(message = "性别不能为空", groups = {Save.class,Import.class})
    private Integer sex;

    /**
     * 生日
     */
    @Excel(name = "出生日期", isImportField = "true,false_students")
    @NotNull(message = "生日不能为空", groups = {Save.class,Import.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Pattern(regexp="^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$",message = "日期不符合yyyy-MM-dd格式", groups = {Save.class,Update.class,Import.class})
    private String birthday;

    /**
     * 身高
     */
    @Excel(name = "身高", isImportField = "true,false_students")
    @NotNull(message = "身高不能为空", groups = {Save.class,Import.class})
    @Range(min = 0, max = 500, message = "身高必须在0-500之间", groups = {Save.class, Update.class})
    private Integer height;

    /**
     * 体重
     */
    @Excel(name = "体重", isImportField = "true,false_students")
    @NotNull(message = "体重不能为空", groups = {Save.class,Import.class})
    @Range(min = 0, max = 500, message = "体重必须在0-500之间", groups = {Save.class, Update.class})
    private Double weight;

    /**
     * 设备:@ExcelEntity 會导致导入时候循环解析，将Device类里面的注解直接也解析出来了。@ExcelIgnore 是直接忽略掉这个实体，用于导出时候的循环引用，没啥用
     */
    @ExcelIgnore
    @ExcelEntity
    private Device device;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @JsonIgnoreProperties(value = {"students"})
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
    @NotFound(action=NotFoundAction.IGNORE)
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
