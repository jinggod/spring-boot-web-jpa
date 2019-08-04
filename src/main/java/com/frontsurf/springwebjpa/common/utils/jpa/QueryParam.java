package com.frontsurf.springwebjpa.common.utils.jpa;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/25 15:24
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public class QueryParam{

   /**
    * 必须是实体类的属性，而非数据库字段
    */
   private String paramName;
   private QueryTypeEnum queryType;
   private Object value;

   public QueryParam(String paramName, QueryTypeEnum queryType, Object value) {
      this.paramName = paramName;
      this.queryType = queryType;
      this.value = value;
   }

   public String getParamName() {
      return paramName;
   }

   public void setParamName(String paramName) {
      this.paramName = paramName;
   }

   public QueryTypeEnum getQueryType() {
      return queryType;
   }

   public void setQueryType(QueryTypeEnum queryType) {
      this.queryType = queryType;
   }

   public Object getValue() {
      return value;
   }

   public void setValue(Object value) {
      this.value = value;
   }
}
