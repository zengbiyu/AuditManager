package com.linkage.audit.query;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.linkage.audit.dao.DataDao;
import com.linkage.audit.main.AuditFrame;
import com.linkage.audit.main.BaseFrame;

public class QueryPolicyAdd extends BaseFrame{
	
	private static Logger logger = Logger.getLogger(QueryPolicyAdd.class);
	
	private AuditFrame auditFrame;
	
	private JTextField policyName;
	
	private DataDao dataDao = new DataDao();
	
	private int gridy=2;
	
	private JPanel mainJPanel;
	
	private JComboBox sheetName;
	
	public QueryPolicyAdd(AuditFrame auditFrame) {
		this.auditFrame = auditFrame;
		setIconifiable(true);
		setClosable(true);
		setTitle("添加筛选策略");
		setBounds(100, 50, 530, 400);
		setLayout(new GridBagLayout());
		setVisible(true);
		
	    mainJPanel = new JPanel(new GridBagLayout());
	    JScrollPane jScrollPane = new JScrollPane();
	    jScrollPane.setViewportView(mainJPanel);
	    setupComponet(jScrollPane, 0, 0, 0, 0, 1 ,1 ,GridBagConstraints.BOTH,GridBagConstraints.CENTER);
		
	    final JLabel sheetNameLb = new JLabel();
	    sheetNameLb.setText("sheet名称：");
		setupComponet(mainJPanel,sheetNameLb, 0, 0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		//文档列表
		sheetName = new JComboBox();
		List<String> collectionNameList = dataDao.getSheetNameList();
		for(int i=0;i<collectionNameList.size();i++) {
			sheetName.addItem(collectionNameList.get(i));
		}
		setupComponet(mainJPanel,sheetName, 1, 0, 3, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel policyNameLb = new JLabel();
		policyNameLb.setText("策略名称：");
		setupComponet(mainJPanel,policyNameLb, 0, 1, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		policyName = new JTextField();
		setupComponet(mainJPanel,policyName, 1, 1, 2, 160, 1 ,0 ,GridBagConstraints.BOTH,GridBagConstraints.CENTER);
		
		final JButton addCondiBtn = new JButton();
		addCondiBtn.addActionListener(new AddCondiActionListener());
		addCondiBtn.setText("添加条件");
		setupComponet(mainJPanel,addCondiBtn, 3, 1, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JButton saveButton = new JButton("保存");
		// 定位保存按钮
		setupComponet(mainJPanel,saveButton, 2, 100, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.EAST);
		final JButton resetButton = new JButton("重置");
		// 定位重置按钮
		setupComponet(mainJPanel,resetButton, 3, 100, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.EAST);
	}
	
	public class AddCondiActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			setVisible(false);
			if(gridy!=2) {
				JComboBox assoCondition = new JComboBox();
				assoCondition.addItem("且");
				assoCondition.addItem("或");
				setupComponet(mainJPanel,assoCondition, 1, gridy, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
				gridy++;
			}
			
			JComboBox fieldName = new JComboBox();
			List<String> fieldNameList = dataDao.selectTableHeads((String)sheetName.getSelectedItem());
			for(int i=2;i<fieldNameList.size();i++) {
				fieldName.addItem(fieldNameList.get(i));
			}
			setupComponet(mainJPanel,fieldName, 0, gridy, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
			
			JComboBox operation = new JComboBox();
			operation.addItem("包含");
			operation.addItem("不包含");
			operation.addItem("=");
			operation.addItem(">");
			operation.addItem(">=");
			operation.addItem("<");
			operation.addItem("<=");
			operation.addItem("!=");
			
			setupComponet(mainJPanel,operation, 1, gridy, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
			
			JTextField fieldValue = new JTextField();
			setupComponet(mainJPanel,fieldValue, 2, gridy, 1, 0, 0 ,0 ,GridBagConstraints.BOTH,GridBagConstraints.CENTER);
			
			final JButton delCondiBtn = new JButton();
			delCondiBtn.setText("删除条件");
			setupComponet(mainJPanel,delCondiBtn, 3, gridy, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
			setVisible(true);
			gridy++;
		}
	}
	
	public class DelCondiActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		}
		
	}
}
