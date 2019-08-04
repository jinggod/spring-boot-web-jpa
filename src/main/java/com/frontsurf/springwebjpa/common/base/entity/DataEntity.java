package com.frontsurf.springwebjpa.common.base.entity;

import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.frontsurf.springwebjpa.common.constant.CommonConstant;
import com.frontsurf.springwebjpa.common.constant.DataBaseConstant;
import com.frontsurf.springwebjpa.common.utils.BeanUtil;
import com.frontsurf.springwebjpa.common.utils.web.UserInfo;
import com.frontsurf.springwebjpa.domain.user.User;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 数据Entity类
 *
 * @MappedSuperclass 表明此类是个父类，可以用于实体类的继承
 * @Where 为所有的查询、更新、删除操作，都添加此条件，此处是实现逻辑删除
 * Note：此处还继承了IExcelModel、IExcelDataModel，为了easypoi的导入导出
 */


@Where(clause = "del_flag=" + CommonConstant.DEL_FLAG_0)
@MappedSuperclass
public abstract class DataEntity<ID extends Serializable> extends BaseEntity<ID> implements IExcelModel, IExcelDataModel {

    private static final long serialVersionUID = 1L;

    /**
     * 创建者
     */
    protected User createBy;
    /**
     * 创建者名称
     **/
    private String createName;
    /**
     * 创建日期
     */
    protected LocalDateTime createDate;
    /**
     * 更新者
     */
    protected User updateBy;
    /**
     * 更新日期
     */
    protected LocalDateTime updateDate;
    /**
     * 删除标记（0：正常；1：删除）
     */
    protected Integer delFlag;

    private String errorMsg;
    private int rowNum;


    public DataEntity() {
        super();
        this.delFlag = CommonConstant.DEL_FLAG_0;
    }

    public DataEntity(ID id) {
        super(id);
    }

    /**
     * 插入之前执行方法
     */
    @Override
    public void preInsert() {
        // 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
//		if (!this.isNewRecord){
//			setId(IdGen.uuid());
//		}
        User user = UserInfo.getCurrentUser();
        if (null != user.getId()) {
            this.updateBy = user;
            this.createBy = user;
        }
        this.updateDate = LocalDateTime.now();
        this.createDate = this.updateDate;
    }

    /**
     * 更新之前执行方法
     * <p>
     * note：更新时，可能不一定符合业务需求，查看 BaseService.save方法
     */
    @Override
    public void preUpdate() {
        User user = UserInfo.getCurrentUser();
        if (null != user.getId()) {
            this.updateBy = user;
        }
        this.updateDate = LocalDateTime.now();

    }

    /**
     * @return
     * @JsonIgnoreProperties 序列化时忽略掉此属性的指定的字段，避免造成自关联，导致序列化失败
     */
    @JsonIgnoreProperties(value = {"createBy", "updateBy"})
    @ManyToOne
    @JoinColumn(name = "create_by")
    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    /**
     * @return
     * @JsonIgnoreProperties 序列化时忽略掉此属性的指定的字段，避免造成自关联，导致序列化失败
     *
     */
    @JsonIgnoreProperties(value = {"createBy", "updateBy"})
    @ManyToOne
    @JoinColumn(name = "update_by")
    public User getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(User updateBy) {
        this.updateBy = updateBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }


    public static String getDelFlagName() {
        return DataBaseConstant.DELETE_FLAG;
    }

    @JsonIgnore
    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @Override
    @Transient
    @JsonIgnore
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {

        this.errorMsg = errorMsg;
    }

    @Override
    @Transient
    @JsonIgnore
    public int getRowNum() {
        return rowNum;
    }

    @Override
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
}
