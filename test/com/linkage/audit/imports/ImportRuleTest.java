package com.linkage.audit.imports;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.junit.Test;

import com.linkage.audit.common.QueryCondition;
import com.linkage.audit.dao.ImportRuleDao;
import com.linkage.audit.model.ComboCondition;
import com.linkage.audit.model.QueryPolicy;

public class ImportRuleTest {
	
	private ImportRuleDao importRuleDao = new ImportRuleDao();

	@Test
	public void query() {
		QueryCondition queryCondition = new QueryCondition();
		QueryPolicy queryPolicy = new QueryPolicy();
		List<ComboCondition> comboConditions = new ArrayList<ComboCondition>();
		/*ComboCondition comboCondition = new ComboCondition();
		comboCondition.setAssoCondition("且");
		comboCondition.setField("规则名称");
		comboCondition.setOperation("=");
		comboCondition.setFieldValue("规则1");
		comboConditions.add(comboCondition);*/
		
		/*ComboCondition comboCondition4 = new ComboCondition();
		comboCondition4.setAssoCondition("且");
		comboCondition4.setField("开始行");
		comboCondition4.setOperation("=");
		comboCondition4.setFieldValue("0.3");
		comboConditions.add(comboCondition4);*/
		
		ComboCondition comboCondition5 = new ComboCondition();
		comboCondition5.setAssoCondition("且");
		comboCondition5.setField("开始行");
		comboCondition5.setOperation("=");
		comboCondition5.setFieldValue("0.4");
		comboConditions.add(comboCondition5);
		
		ComboCondition comboCondition6 = new ComboCondition();
		comboCondition6.setAssoCondition("或");
		comboCondition6.setField("开始行");
		comboCondition6.setOperation("=");
		comboCondition6.setFieldValue("0.3");
		comboConditions.add(comboCondition6);
		
		ComboCondition comboCondition1 = new ComboCondition();
		comboCondition1.setAssoCondition("或");
		comboCondition1.setField("规则名称");
		comboCondition1.setOperation("包含");
		//comboCondition1.setFieldValue("[规则,规则]%[1,2]");
		comboCondition1.setFieldValue("[规则1,规则2]%[1,2]");
		comboConditions.add(comboCondition1);
		
		
		
		/*ComboCondition comboCondition1 = new ComboCondition();
		comboCondition1.setAssoCondition("且");
		comboCondition1.setField("规则名称");
		comboCondition1.setOperation("不包含");
		//comboCondition1.setFieldValue("[规则,规则]%[1,2]");
		comboCondition1.setFieldValue("[规则3,规则4]");
		comboConditions.add(comboCondition1);
		
		
		ComboCondition comboCondition2 = new ComboCondition();
		comboCondition2.setAssoCondition("或");
		comboCondition2.setField("规则名称");
		comboCondition2.setOperation("=");
		comboCondition2.setFieldValue("规则3");
		comboConditions.add(comboCondition2);
		
		ComboCondition comboCondition3 = new ComboCondition();
		comboCondition3.setAssoCondition("且");
		comboCondition3.setField("开始行");
		comboCondition3.setOperation("=");
		comboCondition3.setFieldValue("0.3");
		comboConditions.add(comboCondition3);*/
		
		
		queryPolicy.setComboConditions(comboConditions);
		queryCondition.setQueryPolicy(queryPolicy);
		
		List<List> importRuleList = importRuleDao.selectImportRuleList(queryCondition);
		
		System.out.println(importRuleList);
	}
	
	@Test
	public void test() {
		try {
			final JScrollPane scrollPane = new JScrollPane();
			JTable jTable = new JTable();
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTable.setShowGrid(true);
			//scrollPane.setViewportView(jTable);
			//System.out.println(scrollPane.getViewport().getView());
			
			scrollPane.add(jTable,1);
			JTable jjj = (JTable)scrollPane.getComponent(1);
			System.out.println(jjj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
