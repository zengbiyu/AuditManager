package com.linkage.audit.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.linkage.audit.common.QueryCondition;
import com.linkage.audit.model.ComboCondition;
import com.linkage.audit.model.QueryPolicy;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class BaseDao {
	private static Logger logger = Logger.getLogger(BaseDao.class);
	/**
	 * 建立数据库连接
	 * @param dbName
	 * @return
	 */
	public Mongo getConn() {
		Mongo mg = null;
		try {
			//mg = new Mongo();
			mg = new Mongo("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		return mg;
	}
	
	/**
	 * 获得数据库
	 * @param mg
	 * @param dbName
	 * @return
	 */
	public DB getDB(Mongo mg,String dbName) {
		//获取temp DB；如果默认没有创建，mongodb会自动创建
		DB db = mg.getDB(dbName);
		return db;
	}
	
	/**
	 * 获取文档
	 * @param db
	 * @param name
	 * @return
	 */
	public DBCollection getCollection(DB db,String name) {
		DBCollection dbCollection = db.getCollection(name);
		return dbCollection;
	}

	
	/**
	 * 释放资源
	 * @param mg
	 * @param db
	 */
	public void destory (Mongo mg,DB db) {
		if (mg != null) {
			mg.close();
			mg = null;
		}
		db = null;
	}
	
	/**
	 * 根据查询条件进行查询
	 * @param dbCollection
	 * @param queryCondition
	 * @return
	 */
	public DBCursor find(DBCollection dbCollection,QueryCondition queryCondition) {
		DBObject dbObject= (DBObject) JSON.parse("{}"); 
		return this.find(dbCollection,dbObject,queryCondition);
	}
	
	/**
	 * 根据查询条件进行查询
	 * @param dbCollection
	 * @param dbObject
	 * @param queryCondition
	 * @return
	 */
	public DBCursor find(DBCollection dbCollection,DBObject dbObject,QueryCondition queryCondition) {
		String queryStr=this.getQueryStr(queryCondition);
		DBObject dbObject1 = (DBObject)JSON.parse(queryStr);
		return dbCollection.find(dbObject1,dbObject).skip(queryCondition.getNumToSkip()).limit(queryCondition.getBatchSize());
	}
	
	/**
	 * 根据查询条件进行查询
	 * @param dbCollection
	 * @param queryCondition
	 * @return
	 */
	public long count(DBCollection dbCollection,QueryCondition queryCondition) {
		String queryStr=this.getQueryStr(queryCondition);
		DBObject dbObject1 = (DBObject)JSON.parse(queryStr);
		return dbCollection.count(dbObject1);
	}
	
	/**
	 * 组装查询条件
	 * @param queryCondition
	 * @return
	 */
	public String getQueryStr(QueryCondition queryCondition) {
		String queryStr="";
		if(queryCondition!=null) {
			QueryPolicy queryPolicy = queryCondition.getQueryPolicy();
			if(queryPolicy!=null) {
				List<ComboCondition> comboConditions = queryPolicy.getComboConditions();
				if(comboConditions!=null&&comboConditions.size()>0) {
					queryStr="{\"$or\":[";
					String queryStr1="";
					for(int i=0;i<comboConditions.size();i++) {
						ComboCondition comboCondition = comboConditions.get(i);
						String fieldValue = comboCondition.getFieldValue();
						String field = comboCondition.getField();
						String assoCondition= comboCondition.getAssoCondition();
						String operation = comboCondition.getOperation();
						
						
						field="\""+field+"\"";
						fieldValue="\""+fieldValue+"\"";
						
						String queryStr2="";
						if("或".equals(assoCondition)){
							queryStr1=queryStr1.substring(0,queryStr1.length()-1);
							queryStr=queryStr+"{"+queryStr1+"},";
							queryStr1="";
						}
						
						if("<".equals(operation)) {
							queryStr2=field+":{\"$lt\":"+fieldValue+"}";
						} else if("<=".equals(operation)) {
							queryStr2=field+":{\"$lte\":"+fieldValue+"}";
						} else if(">".equals(operation)) {
							queryStr2=field+":{\"$gt\":"+fieldValue+"}";
						} else if(">=".equals(operation)) {
							queryStr2=field+":{\"$gte\":"+fieldValue+"}";
						} else if("=".equals(operation)) {
							queryStr2=field+":"+fieldValue;
						} else if("!=".equals(operation)) {
							queryStr2=field+":{\"$ne\":"+fieldValue+"}";
						}else if("包含".equals(operation)) {
							fieldValue=fieldValue.substring(1,fieldValue.length()-1);
							String[] strs1 = fieldValue.split("%");
							List<List> arrayList = new ArrayList<List>();
							for(int j=0;j<strs1.length;j++) {
								if(strs1[j].startsWith("[")&&strs1[j].endsWith("]")) {
									strs1[j]=strs1[j].substring(1, strs1[j].length()-1);
								}
								String[] str2=strs1[j].split(",");
								arrayList.add(Arrays.asList(str2));
							}
							List<String> combieList = combie(arrayList);
							queryStr2=queryStr2+field+":{\"$in\":[";
							for(int si=0;si<combieList.size();si++) {
								queryStr2+="{$regex:\""+combieList.get(si)+"\",$options:\"i\"},";
							}
							queryStr2=queryStr2.substring(0,queryStr2.length()-1);
							queryStr2+="]}";
							
						} else if("不包含".equals(operation)) {
							fieldValue=fieldValue.substring(1,fieldValue.length()-1);
							String[] strs1 = fieldValue.split("%");
							List<List> arrayList = new ArrayList<List>();
							for(int j=0;j<strs1.length;j++) {
								if(strs1[j].startsWith("[")&&strs1[j].endsWith("]")) {
									strs1[j]=strs1[j].substring(1, strs1[j].length()-1);
								}
								String[] str2=strs1[j].split(",");
								arrayList.add(Arrays.asList(str2));
							}
							List<String> combieList = combie(arrayList);
							queryStr2=queryStr2+field+":{\"$nin\":[";
							for(int si=0;si<combieList.size();si++) {
								queryStr2+="{$regex:\""+combieList.get(si)+"\",$options:\"i\"},";
							}
							queryStr2=queryStr2.substring(0,queryStr2.length()-1);
							queryStr2+="]}";
						}
						queryStr1=queryStr1+queryStr2+",";	
					}
					
					if(!"".equals(queryStr1)){
						queryStr1=queryStr1.substring(0,queryStr1.length()-1);
						queryStr=queryStr+"{"+queryStr1+"},";
					}
					queryStr=queryStr.substring(0,queryStr.length()-1)+"]}";
				}
			}
		}
		logger.debug("查询语句====》"+queryStr);
		return queryStr;
	}
	
	/**
	 * 组装模糊匹配的条件
	 * @param s
	 * @return
	 */
	private  List<String> combie(List<List> s) {
		List<String> arrayList = new ArrayList<String>();
	    int[] dig = new int[s.size()];                   //用来模拟进位
	    while (dig[0] < s.get(0).size()) {                  //进位最高位不满最大的时候循环
	    	String combieStr="^.*";
	        for (int i=0; i<s.size(); i++) {
	            //System.out.print(s[i][dig[i]]);          //打印每个数组的当前进位位置的元素
	            combieStr=combieStr+s.get(i).get(dig[i])+".*";
	        }
	        combieStr+="$";
	        //System.out.println(combieStr);             //换行
	        arrayList.add(combieStr);
	        dig[dig.length-1]++;                         //模拟进位，末位+1
	        for (int i=dig.length-1; i>0; i--) {
	            if (dig[i] == s.get(i).size()) {             //当某进位位置达到最大时往前进位
	                dig[i] = 0;                          //当前位清0恢复最小状态
	                dig[i-1]++;                          //进位
	            } else {
	                break;                               //不满足进位时break
	            }
	        }
	    }
	    return arrayList;
	}
}
