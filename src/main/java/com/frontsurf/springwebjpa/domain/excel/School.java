package com.frontsurf.springwebjpa.domain.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.frontsurf.springwebjpa.common.base.entity.DataEntity;
import com.frontsurf.springwebjpa.common.constant.CommonConstant;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author xu.xiaojing
 * @Date 2019/10/24 8:38
 * @Email xu.xiaojing@frontsurf.com
 * @Description 此demo下的类 不仅仅是 导入excel，还包含了 JPA 的 onetoone,onetoMany的 双向关联 、插入、更新等
 */
@Table(name = "school")
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLDelete(sql = "update school set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
@SQLDeleteAll(sql = "update school set del_flag = " + CommonConstant.DEL_FLAG_1 + " where id = ?")
@Where(clause = "del_flag=" + CommonConstant.DEL_FLAG_0)
public class School extends DataEntity<String> {


    @Excel(name="学校名称",isImportField = "true")
    private String name;


    private String province;

    private String city;

    private String district;

    private Integer type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getType() {
        return type;
    }


    public void setType(Integer type) {
        this.type = type;
    }

}
