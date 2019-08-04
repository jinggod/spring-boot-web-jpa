package com.frontsurf.springwebjpa.domain.systemconfig;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/23 13:57
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */
@Table(name = "system_config")
@Entity
public class SystemConfiguration {

    @Id
    private String name;

    private String value;

    private String type;

    /**
     * 创建者
     */
    protected Integer createBy;
    protected String createName;   // 创建者名称
    protected Date createDate;    // 创建日期
    protected Integer updateBy;    // 更新者
    protected Date updateDate;    // 更新日期

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @JsonIgnore
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
