package com.frontsurf.springwebjpa.domain.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.frontsurf.springwebjpa.common.base.entity.DataEntity;
import com.frontsurf.springwebjpa.common.constant.CommonConstant;
import com.frontsurf.springwebjpa.common.validate.Import;
import com.frontsurf.springwebjpa.common.validate.Save;
import com.frontsurf.springwebjpa.common.validate.Update;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author xu.xiaojing
 * @Date 2019/10/23 19:06
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */
@Table(name = "device")
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLDelete(sql = "update device set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
@SQLDeleteAll(sql = "update device set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
@Where(clause = "del_flag=" + CommonConstant.DEL_FLAG_0)
public class Device extends DataEntity<String> {

    /**
     * 设备编码
     */
    @Excel(name = "设备编码", needMerge = true, isImportField = "true")
    @NotBlank(message = "设备编码不能为空", groups = {Save.class, Import.class})
    @Length(min = 0, max = 255, message = "设备编码不能超过255", groups = {Save.class, Update.class, Import.class})
    private String code;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称", needMerge = true, isImportField = "true")
    @NotBlank(message = "设备名称不能为空", groups = {Save.class, Import.class})
    @Length(min = 0, max = 255, message = "设备名称不能超过255", groups = {Save.class, Update.class, Import.class})
    private String name;

    /**
     * 设备类型名称
     */
    @Excel(name = "设备类型", needMerge = true, isImportField = "true")
    @NotBlank(message = "设备类型不能为空", groups = {Import.class})
    private String type;

    /**
     * 设备分类：1 - 学生个人设备；2-学生共享设备；3-社会个人设备
     */
    @Excel(name = "分类", needMerge = true, replace = {"个人_1", "共享_2"})
    @NotNull(message = "设备分类不能为空", groups = {Save.class})
    private Integer classification;

    /**
     * 学校设备使用：学生列表,
     * <p>
     * todo 这里有个列表导入字段,所以其他字段必须加上 needMerge = true
     */
    @ExcelCollection(name = "学生列表", id = "students", type = HashSet.class)
    private Set<Student> students;

    @NotNull(message = "学校不能为空", groups = {Import.class, Save.class})
    @ExcelEntity(name = "学校")
    private School school;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getClassification() {
        return classification;
    }

    public void setClassification(Integer classification) {
        this.classification = classification;
    }


    @JsonIgnoreProperties(value = {"jtyDevice"})
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "school_id")
    @NotFound(action = NotFoundAction.IGNORE)
    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
