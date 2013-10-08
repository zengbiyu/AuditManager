package com.linkage.audit.common;

import java.util.HashMap;
import java.util.List;

import com.linkage.audit.model.QueryPolicy;

public class QueryCondition {
	
	/**
	 * 查询字段
	 */
	private List<String> queryFields;
	/**
	 * 查询条件
	 */
	private QueryPolicy queryPolicy;
	/**
	 * 分组字段
	 */
	private List<String> groupbyFields;
	/**
	 * 聚合函数
	 */
	private String function;
	/**
	 * 排序字段
	 */
	private List<HashMap<String,Integer>> sortFields;
	/**
	 * 跳过的数量
	 */
	private int numToSkip;
	/**
	 * 查询的数量
	 */
	private int batchSize;

	public List<String> getQueryFields() {
		return queryFields;
	}

	public void setQueryFields(List<String> queryFields) {
		this.queryFields = queryFields;
	}

	public QueryPolicy getQueryPolicy() {
		return queryPolicy;
	}

	public void setQueryPolicy(QueryPolicy queryPolicy) {
		this.queryPolicy = queryPolicy;
	}

	public List<String> getGroupbyFields() {
		return groupbyFields;
	}

	public void setGroupbyFields(List<String> groupbyFields) {
		this.groupbyFields = groupbyFields;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public List<HashMap<String, Integer>> getSortFields() {
		return sortFields;
	}

	public void setSortFields(List<HashMap<String, Integer>> sortFields) {
		this.sortFields = sortFields;
	}

	public int getNumToSkip() {
		return numToSkip;
	}

	public void setNumToSkip(int numToSkip) {
		this.numToSkip = numToSkip;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
}
