package com.linkage.audit.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.linkage.audit.common.QueryCondition;
import com.linkage.audit.util.DateUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class DataDao extends BaseDao {
	/**
	 * 添加文档数据
	 * @param importRule
	 * @return
	 */
	public boolean addData(List<DBObject> dataList,String sheetName) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "data");
		String collectionName = getCollectionName(sheetName);
		if("".equals(collectionName)) {
			//如果数据字典中没有数据，则添加数据字典
			collectionName = "data"+DateUtil.getCurTime();
			DBObject dbObject = new BasicDBObject();
			dbObject.put("文档名称", collectionName);
			dbObject.put("Sheet名称", sheetName);
			addDataDict(dbObject);
		}
		DBCollection dbCollection = this.getCollection(db, collectionName);
		dbCollection.insert(dataList);
		this.destory(mg, db);
		return true;
	}
	
	/**
	 * 显示文档数据列表，Jtable使用
	 * @param queryCondition
	 * @return
	 */
	public List<List> selectList(QueryCondition queryCondition,String sheetName) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "data");
		String collectionName = getCollectionName(sheetName);
		DBCollection importRules = this.getCollection(db, collectionName);
		List<String> tableHeads = new ArrayList<String>();
		tableHeads=this.selectTableHeads(sheetName);
		List<List> list = new ArrayList<List>();
		
		DBCursor cur = this.find(importRules,queryCondition);
		int count=queryCondition.getNumToSkip();
		while (cur.hasNext()) {
			DBObject dbObject = cur.next();
			List row = new ArrayList();
			//第一列：选择列
			tableHeads.remove("序号");
			row.add(++count);
			//row.add(false);
			for(int i=0;i<tableHeads.size();i++) {
				if(dbObject.get(tableHeads.get(i))!=null) {
					row.add(dbObject.get(tableHeads.get(i)).toString().trim());
				} else {
					row.add("");
				}
			}
			list.add(row);
		}
		this.destory(mg, db);
		return list;
	}
	
	/**
	 * 获取总数
	 * @return
	 */
	public long count(String sheetName) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "data");
		String collectionName = getCollectionName(sheetName);
		DBCollection importRules = this.getCollection(db, collectionName);
		long count = importRules.count();
		this.destory(mg, db);
		return count;
	}
	
	/**
	 * 获取总数
	 * @return
	 */
	public long count(QueryCondition queryCondition,String sheetName) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "data");
		String collectionName = getCollectionName(sheetName);
		DBCollection importRules = this.getCollection(db, collectionName);
		long count = this.count(importRules,queryCondition);
		this.destory(mg, db);
		return count;
	}
	
	/**
	 * 获取文档的列名
	 * @return
	 */
	public List<String> selectTableHeads(String sheetName) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "data");
		String collectionName = getCollectionName(sheetName);
		DBCollection importRules = this.getCollection(db, collectionName);
		
		List<String> tableHeads = new ArrayList<String>();
		DBObject dbObject = importRules.findOne();
		if(dbObject!=null) {
			//第一列：选择列
			tableHeads.add("序号");
			//tableHeads.add("");
			Set<String> set = dbObject.keySet();
			Iterator<String> iterator = set.iterator();
			while(iterator.hasNext()) {
				String tableHead=iterator.next();
				/*if(!"_id".equals(tableHead)) {
					tableHeads.add(tableHead);
				}*/
				tableHeads.add(tableHead);
			}
		}
		this.destory(mg, db);
		return tableHeads;
	}
	
	/**
	 * 添加数据字典
	 * @param importRule
	 * @return
	 */
	public boolean addDataDict(DBObject dbObject) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "data");
		DBCollection dbCollection = this.getCollection(db, "dataDict");
		dbCollection.save(dbObject);
		this.destory(mg, db);
		return true;
	}
	
	/**
	 * 根据sheetName获得文档名称
	 * @param sheetName
	 * @return
	 */
	public String getCollectionName(String sheetName) {
		String collectionName = "";
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "data");
		//数据字典表
		DBCollection dbCollection = this.getCollection(db, "dataDict");
		DBObject query = new BasicDBObject();
		query.put("Sheet名称", sheetName);
		DBObject dbObject = dbCollection.findOne(query);
		if(dbObject!=null) {
			collectionName = (String)dbObject.get("文档名称");
		}
		this.destory(mg, db);
		return collectionName;
	}
	
	/**
	 * 根据sheetName获得文档名称
	 * @param sheetName
	 * @return
	 */
	public List<String> getSheetNameList() {
		List<String> sheetNameList = new ArrayList<String>();
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "data");
		//数据字典表
		DBCollection dbCollection = this.getCollection(db, "dataDict");
		DBCursor cur = dbCollection.find();
		while (cur.hasNext()) {
			DBObject dbObject = cur.next();
			String sheetName = (String)dbObject.get("Sheet名称");
			sheetNameList.add(sheetName);
		}
		
		this.destory(mg, db);
		return sheetNameList;
	}
}
