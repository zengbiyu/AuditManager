package com.linkage.audit.model;

import java.util.List;

public class QueryPolicy {
	/**
	 * 策略名称
	 */
	private String policyName;
	/**
	 * 组合条件
	 */
	private List<ComboCondition> comboConditions;
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	public List<ComboCondition> getComboConditions() {
		return comboConditions;
	}
	public void setComboConditions(List<ComboCondition> comboConditions) {
		this.comboConditions = comboConditions;
	}
}
