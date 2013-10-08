package com.linkage.audit.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.Spring;

import org.bson.types.ObjectId;

import com.linkage.audit.common.QueryCondition;
import com.linkage.audit.model.ComboCondition;
import com.linkage.audit.model.QueryPolicy;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class CompareRuleDao extends BaseDao {
	
	/**
	 * 添加对比策略
	 * @param compareRule
	 * @return
	 */
	public boolean addCompareRule(DBObject compareRule) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "audit");
		DBCollection compareRules = this.getCollection(db, "compareRules");
		compareRules.save(compareRule);
		this.destory(mg, db);
		return true;
	}
	
	/**
	 * 删除对比策略
	 * @param deleteRuleList
	 * @return
	 */
	public boolean deleteCompareRule(List<List> deleteRuleList) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "audit");
		DBCollection compareRules = this.getCollection(db, "compareRules");
		//遍历删除所有选中的策略compareRule
		Iterator iterator=deleteRuleList.iterator();
		while (iterator.hasNext()) {
			List compareRule = (List) iterator.next();
			DBObject deletecompareRule = new BasicDBObject();
			deletecompareRule.put("_id", new ObjectId(compareRule.get(1).toString()));
			compareRules.remove(deletecompareRule);
			}
		this.destory(mg, db);
		return true;
	}
	
	
	/**
	 * 显示对比策略列表
	 * @param queryCondition
	 * @return
	 */
	public List<List> selectCompareRuleList(QueryCondition queryCondition) {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "audit");
		DBCollection compareRules = this.getCollection(db, "compareRules");
		//List<String> tableHeads = new ArrayList<String>();
		//tableHeads=this.selectCompareRuleDaoTableHeads();
		List<List> list = new ArrayList<List>();
		
		DBCursor cur = compareRules.find();
		//DBCursor cur = this.find(compareRules,queryCondition);
		//compareRules.
		while (cur.hasNext()) {
			DBObject dbObject = cur.next();
			List row = new ArrayList();
			//第一列：选择列
			row.add(false);
			
		/*	for(int i=1;i<tableHeads.size();i++) {
				if(dbObject.get(tableHeads.get(i))!=null) {
					System.out.println(dbObject.get(tableHeads.get(i)));
					row.add(dbObject.get(tableHeads.get(i)).toString().trim());
					if(tableHeads.get(i).equals("数据1")) {
						DBObject dbObject1=(DBObject) dbObject.get(tableHeads.get(i));
						Set<String> set = dbObject1.keySet();
						Iterator<String> iterator = set.iterator();
						while(iterator.hasNext()) {
							
							String datakey=iterator.next();
						     
						
						}
					}
						System.out.println(dbObject.get(tableHeads.get(i)));
				 } else {
					row.add("");
				}
			}*/
			Set<String> set = dbObject.keySet();
			Iterator<String> iterator = set.iterator();
			while(iterator.hasNext()) {
				
				String dbObjectkey=iterator.next();
				
				//如果不是内嵌文档，可直接添加
				if(!"数据1".equals(dbObjectkey)&&!"数据2".equals(dbObjectkey)) {
					
					row.add(dbObject.get(dbObjectkey).toString().trim());
				}else{
				//如果是内嵌文档，要再次遍历内嵌文档的key,通过内嵌文档的key获得内嵌文档内的各个值
				//if("数据1".equals(dbObjectkey)||"数据2".equals(dbObjectkey)) {
					DBObject datadbObject=(DBObject) dbObject.get(dbObjectkey);
					Set<String> dataset = datadbObject.keySet();
					Iterator<String> dataiterator = dataset.iterator();
					while(dataiterator.hasNext()) {
						
						String datakey=dataiterator.next();
						row.add(datadbObject.get(datakey).toString().trim());
					}
				}
			}
			list.add(row);
			if(list.size()>100000) {
				break;
			}
		}
		this.destory(mg, db);
		return list;
	}
	
	/**
	 * 获取导入规则列表的列名
	 * @return
	 */
	public List<String> selectCompareRuleDaoTableHeads() {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "audit");
		DBCollection compareRules = this.getCollection(db, "compareRules");
		
		List<String> tableHeads = new ArrayList<String>();
		DBObject dbObject = compareRules.findOne();
		if(dbObject!=null) {
			//第一列：选择列
			tableHeads.add("");
			Set<String> set = dbObject.keySet();
			Iterator<String> iterator = set.iterator();
			while(iterator.hasNext()) {
				
				String tableHead=iterator.next();
				
				//如果不是内嵌文档，可直接添加
				if(!"_id".equals(tableHead)&&!"数据1".equals(tableHead)&&!"数据2".equals(tableHead)) {
					
					tableHeads.add(tableHead);
				}
				//如果是内嵌文档，要再次遍历内嵌文档的key
				if("数据1".equals(tableHead)||"数据2".equals(tableHead)) {
					DBObject datadbObject=(DBObject) dbObject.get(tableHead);
					Set<String> dataset = datadbObject.keySet();
					Iterator<String> dataiterator = dataset.iterator();
					while(dataiterator.hasNext()) {
						
						String datakey=dataiterator.next();
						tableHeads.add(datakey);
						
					}
				}
			}
		}
		this.destory(mg, db);
		return tableHeads;
	}
	
	/**
	 * 获取导入规则总数
	 * @return
	 */
	public long countCompareRules() {
		Mongo mg = this.getConn();
		DB db = this.getDB(mg, "audit");
		DBCollection compareRules = this.getCollection(db, "compareRules");
		long count = compareRules.count();
		this.destory(mg, db);
		return count;
	}

	
}
