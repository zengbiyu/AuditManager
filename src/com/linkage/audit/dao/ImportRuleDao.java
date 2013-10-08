package com.linkage.audit.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.types.ObjectId;

import com.linkage.audit.common.QueryCondition;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class ImportRuleDao extends BaseDao {
	
	/**
	 * 添加导入规则
	 * @param importRule
	 * @return
	 */
	public boolean addImportRule(DBObject importRule) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "audit");
		DBCollection importRules = this.getCollection(db, "importRules");
		importRules.save(importRule);
		this.destory(mg, db);
		return true;
	}
	
	/**
	 * 显示导入规则列表，Jtable使用
	 * @param queryCondition
	 * @return
	 */
	public List<List> selectImportRuleList(QueryCondition queryCondition) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "audit");
		DBCollection importRules = this.getCollection(db, "importRules");
		List<String> tableHeads = new ArrayList<String>();
		tableHeads=this.selectImportRuleTableHeads();
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
	 * 根据查询参数查询导入规则列表
	 * @param paramsMap
	 * @return
	 */
	public List<HashMap<String,Object>> selectImportRule(Map<String,Object> paramsMap) {
		List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "audit");
		DBCollection importRules = this.getCollection(db, "importRules");
		DBObject query = new BasicDBObject();
		if(paramsMap!=null) {
			Iterator<Entry<String, Object>> entrys =paramsMap.entrySet().iterator();
			while(entrys.hasNext()) {
				Entry<String, Object> entry = entrys.next();
				if("_id".equals(entry.getKey())) {
					query.put(entry.getKey(),new ObjectId(entry.getValue().toString()));
				} else {
					query.put(entry.getKey(),entry.getValue());
				}
				
			}
		}
		DBCursor cur = importRules.find(query);
		while(cur.hasNext()) {
			HashMap<String,Object> hashMap = new HashMap<String, Object>();
			DBObject dbObject = cur.next();
			Set<String> set = dbObject.keySet();
			Iterator<String> iterator = set.iterator();
			while(iterator.hasNext()) {
				String tableHead=iterator.next();
				if(dbObject.get(tableHead)!=null) {
					hashMap.put(tableHead, dbObject.get(tableHead));
				} else {
					hashMap.put(tableHead, "");
				}
			}
			list.add(hashMap);
		}
		return list;
	}
	
	/**
	 * 获取导入规则列表的列名
	 * @return
	 */
	public List<String> selectImportRuleTableHeads() {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "audit");
		DBCollection importRules = this.getCollection(db, "importRules");
		
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
	 * 获取导入规则总数
	 * @return
	 */
	public long countImportRule() {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "audit");
		DBCollection importRules = this.getCollection(db, "importRules");
		long count = importRules.count();
		this.destory(mg, db);
		return count;
	}
	
	/**
	 * 获取导入规则总数
	 * @return
	 */
	public long countImportRule(QueryCondition queryCondition) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "audit");
		DBCollection importRules = this.getCollection(db, "importRules");
		long count = this.count(importRules,queryCondition);
		this.destory(mg, db);
		return count;
	}

	/**
	 * 更新导入规则
	 * @param importRule
	 * @param query
	 */
	public void updateImportRule(DBObject query,DBObject importRule) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "audit");
		DBCollection importRules = this.getCollection(db, "importRules");
		importRules.update(query,importRule);
		this.destory(mg, db);
	}

	/**
	 * 删除导入规则
	 * @param query
	 */
	public void deleteImportRule(DBObject query) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "audit");
		DBCollection importRules = this.getCollection(db, "importRules");
		importRules.remove(query);
		this.destory(mg, db);
	}
}
