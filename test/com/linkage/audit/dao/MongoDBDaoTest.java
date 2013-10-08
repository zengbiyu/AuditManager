package com.linkage.audit.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class MongoDBDaoTest {

	private Mongo mg = null;
	private DB db;
	private DBCollection importRules;

	@Before
	public void init() {
		try {
			mg = new Mongo();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}

		// 获取temp DB；如果默认没有创建，mongodb会自动创建
		db = mg.getDB("audit");
		// 获取users DBCollection；如果默认没有创建，mongodb会自动创建
		importRules = db.getCollection("importRules");
	}

	@After
	public void destory() {
		if (mg != null)
			mg.close();
		mg = null;
		db = null;
		importRules = null;
		System.gc();
	}
	
	@Test
	public void testAddImportRule() {
		for(int i=0;i<10000;i++) {
			DBObject importRule = new BasicDBObject();
			importRule.put("规则名称", "规则名称");
			importRule.put("编码", "UTF-8");
			importRule.put("开始列", 1);
			importRule.put("结束列", 1);
			importRule.put("开始行", 1);
			importRule.put("结束行", 1);
			importRule.put("字段表达式", "时间管理 - 情绪管理");
			importRule.put("字段名称", "时间管理 情绪管理");
			importRule.put("开始行是否为字段", "是");
			importRules.save(importRule);
		}
	}
	
	@Test
	public void queryAll() {
		DBCursor cur = importRules.find();
		int i=0;
		while (cur.hasNext()) {
			System.out.println(cur.next());
			i++;
			if (i==10) {
				break;
			}
		}
	}
	
	@Test
	public void remove() {
		importRules.drop();
	}
	
	@Test
	public void testJson() {
		//String json="{\"$or\":[{\"规则名称\":\"规则1\",\"开始行\":{\"$ne\":\"0.3\"},\"规则名称\":{\"$in\":[/规则/,/规则2/]},\"规则名称\":{\"$in\":[/1/]}},{\"规则名称\":\"规则3\",\"规则名称\":\"规则4\"}]}";
		//DBObject dbObject1 = (DBObject)JSON.parse(json);
		
		try {
			/*String json2="{\"$or\":[{\"开始行\":{\"$ne\":\"0.3\"},\"$or\":[{\"规则名称\":/规则/},{\"规则名称\":/规则2/}],\"$or\":[{\"规则名称\":/1/}]},{\"规则名称\":\"规则3\",\"规则名称\":\"规则4\"}]}";
			DBObject dbObject2 = (DBObject)JSON.parse(json2);*/
			
			/*String json5="{\"开始行\":\"0.3\",$or:[{\"name\":\"yhb\",\"age\":18}]}";
			DBObject dbObject5 = (DBObject)JSON.parse(json5);*/
			
			/*String json2="{\"$or\":[{\"开始行\":{\"$ne\":\"0.3\"},\"$or\":[{\"规则名称\":{\"$regex\": {\"$regex\":\"^.*规则2.*$\",\"$options\":\"i\"}}],{\"规则名称\":\"规则3\",\"规则名称\":\"规则4\"}]}";
			DBObject dbObject2 = (DBObject)JSON.parse(json2);*/
			
			/*String json4="{$or:[{$or:[{\"name\":\"yhb\"},{\"age\":18}],\"开始行\":\"0.3\"},{$or:[{\"name\":\"lwy\"},{\"age\":19}]}]}";
			DBObject dbObject4 = (DBObject)JSON.parse(json4);*/
			
			/*List list =new ArrayList();
			list.add(9);
			list.add(10);
			DBObject dbObject3 = new BasicDBObject("$in", list);
			System.out.println(dbObject3.toString());
			JSON.parse(dbObject3.toString());*/
			
			
			/*Pattern pattern = Pattern.compile("^.*" + "规则" + ".*$", Pattern.MULTILINE);
			DBObject dbObject6 = new BasicDBObject("$regex", pattern);
			
			DBObject query = new BasicDBObject();
			query.put("real_name", dbObject6);
			System.out.println(JSON.serialize(dbObject6));
			System.out.println(query.toString());
			JSON.parse(query.toString());*/
			
			/*String query2="{ \"real_name\" :  { \"$regex\" : \"^.*规则.*$\" , \"$options\" : \"m\"}}";
			JSON.parse(query2);*/
			
			
			String query3="{\"$or\":[{\"规则名称\":{\"$or\":[{\"$in\":[{$regex:\"规则\",$options:\"i\"}],\"$in\":[{$regex:\"1\",$options:\"i\"}]}]}}]}";
			JSON.parse(query3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
