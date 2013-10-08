package com.linkage.audit.model;

public class ComboCondition {
	/**
	 * 关联关系 ：且，或
	 */
	private String assoCondition;
	/**
	 * 字段
	 */
	private String field;
	/**
	 * 操作符 ：>,>=,<,<=,=,!=,包含,不包含
	 */
	private String operation;
	/**
	 * 字段值
	 */
	private String fieldValue;
	public String getAssoCondition() {
		return assoCondition;
	}
	public void setAssoCondition(String assoCondition) {
		this.assoCondition = assoCondition;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
}
