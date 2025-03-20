package com.essence.dto;



/**
 * @author zhy
 * @since 2022/5/11 16:28
 */
public class Criterion {
    /**
     * 等于
     */
    public static final String EQ = "EQ";
    /**
     * 不等于
     */
    public static final String NE = "NE";
    /**
     * 模糊
     */
    public static final String LIKE = "LIKE";
    /**
     * 大于
     */
    public static final String GT = "GT";
    /**
     * 小于
     */
    public static final String LT = "LT";
    /**
     * 大于等于
     */
    public static final String GTE = "GTE";
    /**
     * 小于等于
     */
    public static final String LTE = "LTE";
    /**
     * 并且（默认）
     */
    public static final String AND = "AND";
    /**
     * 或者
     */
    public static final String OR = "OR";
    /**
     * 包含
     */
    public static final String IN = "IN";
    /**
     * 升序
     */
    public static final String ASC = "ASC";
    /**
     * 降序
     */
    public static final String DESC = "DESC";
    /**
     * 字段
     * @mock type
     */
    private String fieldName;
    /**
     * 值
     * @mock 1
     */
    private Object value;
    /**
     * 操作
     * @mock EQ/DESC
     */
    private String operator;

    public Criterion() {
    }

    public Criterion(String fieldName, Object value, String operator) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public Criterion(String operator) {
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
