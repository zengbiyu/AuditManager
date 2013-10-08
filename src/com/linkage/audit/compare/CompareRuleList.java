package com.linkage.audit.compare;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.xml.ws.handler.MessageContext.Scope;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.SCLRecord;
import org.bson.types.ObjectId;

import com.linkage.audit.common.QueryCondition;
import com.linkage.audit.dao.CompareRuleDao;
import com.linkage.audit.imports.ImportRuleList;
import com.linkage.audit.main.AuditFrame;
import com.linkage.audit.main.AuditFrame.OpenFrameAction;
import com.linkage.audit.model.ComboCondition;
import com.linkage.audit.model.QueryPolicy;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class CompareRuleList extends JInternalFrame {
	
	private static Logger logger = Logger.getLogger(CompareRuleList.class);
	
	/**
	 * 策略名称
	 */
	private JTextField ruleName;
	
	private ListAbstractTableModel ltm;
	
	private JTable table;
	
	String[] tableHeads={"","_id","策略名称","sheet1编号","sheet1对比开始列","sheet1对比结束列","sheet1对比开始行",
			"sheet1对比结束行","sheet2编号","sheet2对比开始列","sheet2对比结束列","sheet2对比开始行",
			"sheet2对比结束行","大小写标示颜色","空格内容标示颜色","完全不同内容标示颜色"};
	
	private CompareRuleDao compareRuleDao = new CompareRuleDao();
	
	//查询的得到的策略列表
	List<List> compareRuleList;
	/**
	 * 主窗口
	 */
	private AuditFrame auditFrame;
	
	public CompareRuleList(final AuditFrame auditFrame) {
		super();
		this.auditFrame = auditFrame;
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setTitle("对比策略列表");
		getContentPane().setLayout(new GridBagLayout());
		setBounds(0, 0, 780, 470);
		//setVisible(true);
		
		final JLabel ruleNameLb = new JLabel();
		ruleNameLb.setText("策略名称：");
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(10, 10, 0, 10);
		getContentPane().add(ruleNameLb, gridBagConstraints);
		
		ruleName = new JTextField();
		final GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.ipadx = 200;
		gridBagConstraints1.insets = new Insets(10, 10, 0, 10);
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridwidth = 3;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.gridx = 1;
		getContentPane().add(ruleName, gridBagConstraints1);
		
		final JButton searchButton = new JButton("查询");
		// 定位查询按钮
		final GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.gridx = 6;
		gridBagConstraints2.insets = new Insets(10, 10, 0, 0);
		getContentPane().add(searchButton, gridBagConstraints2);
		
		final JButton allDataButton = new JButton("全量查询");
		// 定位查询按钮
		final GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridy = 0;
		gridBagConstraints3.gridx = 7;
		gridBagConstraints3.insets = new Insets(10, 10, 0, 0);
		getContentPane().add(allDataButton, gridBagConstraints3);
		
		
		allDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				
				long count=compareRuleDao.countCompareRules();
				QueryCondition q=null;
				compareRuleList = compareRuleDao.selectCompareRuleList(q);
				//查询表格记录
				ltm=new ListAbstractTableModel(compareRuleList);
				table.setModel(ltm);
				hiddenColumn("_id",table);
				System.out.println(count);
			}
		});
		
		
		
		final JScrollPane scrollPane = new JScrollPane();
		final GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
		gridBagConstraints6.weighty = 1.0;
		gridBagConstraints6.weightx = 1.0;
		gridBagConstraints6.anchor = GridBagConstraints.CENTER;
		gridBagConstraints6.insets = new Insets(10, 10, 0, 10);
		gridBagConstraints6.fill = GridBagConstraints.BOTH;
		gridBagConstraints6.gridwidth = 10;
		gridBagConstraints6.gridy = 1;
		gridBagConstraints6.gridx = 0;
		getContentPane().add(scrollPane, gridBagConstraints6);
		
		//初始化获取全部的数据显示
		QueryCondition queryall=null;
		compareRuleList = compareRuleDao.selectCompareRuleList(queryall);
		ltm=new ListAbstractTableModel(compareRuleList);
		table = new JTable(ltm);
		//table.setEnabled(false); 不可编辑
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setShowGrid(true);
		//隐藏_id列
		hiddenColumn("_id",table);
		//table.getColumn("").setCellEditor(table.getDefaultEditor(Boolean.class));
		//设置绘制单元格的绘制器
		//table.getColumn("").setCellRenderer(table.getDefaultRenderer(Boolean.class));
		scrollPane.setViewportView(table);
		/*
		 * 不用mongodb数据库查询，否则在没有数据的情况下没有表头的显示
		 * 查询列名
		List<String> tableHeads = compareRuleDao.selectCompareRuleDaoTableHeads();
		dftm.setColumnIdentifiers(tableHeads.toArray());
		*/
		//dftm = (DefaultTableModel) table.getModel();
		//监听数据变化
		/*		ltm.addTableModelListener(new TableModelListener()
				{	
					@Override
					public void tableChanged(TableModelEvent e) {
						// TODO Auto-generated method stub
						int row = e.getFirstRow();
						int column = e.getColumn();
						System.out.println(" "+row+" "+column+compareRuleList.get(row).get(column));
						System.out.println(" "+row+" "+column+compareRuleList.get(row).get(column));
                      
					}
				});*/
        
		
		
		final JButton compareButton = new JButton("载入对比策略对比");
		final JButton deleteButton = new JButton("删除");
		final JButton modifyButton = new JButton("修改");
		final JButton detailButton = new JButton("详细信息");
		final JButton addButton = new JButton(auditFrame.new OpenFrameAction("添加", "compare.CompareRuleAdd", null));
		JPanel bottomBtn=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomBtn.add(compareButton);
		bottomBtn.add(deleteButton);
		bottomBtn.add(modifyButton);
		bottomBtn.add(detailButton);
		bottomBtn.add(addButton);
		
		// 定位底部按钮位置
		final GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridy = 8;
		gridBagConstraints4.gridx = 6;
		gridBagConstraints4.insets=new Insets(10, 30, 10, 10);
		gridBagConstraints4.weightx=1;
		gridBagConstraints4.gridwidth=3;
		gridBagConstraints4.anchor=GridBagConstraints.EAST;
		getContentPane().add(bottomBtn, gridBagConstraints4);
		
		compareButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Iterator iterator=compareRuleList.iterator();
				//要删除的策略
				List<List> selectRule=new ArrayList<List>();
				while (iterator.hasNext()) {
					List compareRule = (List) iterator.next();
					if(compareRule.get(0).equals(true)){
						selectRule.add(compareRule);
					}
				}
			if(selectRule.size()==1){
				selectRule.get(0);
				JOptionPane.showMessageDialog(CompareRuleList.this, "好！",
						"提示信息", JOptionPane.INFORMATION_MESSAGE);
				
			}else{
				JOptionPane.showMessageDialog(CompareRuleList.this, "恰当地选择一个策略",
						"提示信息", JOptionPane.INFORMATION_MESSAGE);
			
				
			    }
			}	
		});
		
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				Iterator iterator=compareRuleList.iterator();
				//要删除的策略
				List<List> deleteRuleList=new ArrayList<List>();
				while (iterator.hasNext()) {
					List compareRule = (List) iterator.next();
					if(compareRule.get(0).equals(true)){
						deleteRuleList.add(compareRule);
					}
				}
				if(deleteRuleList.size()==0) {
					JOptionPane.showMessageDialog(CompareRuleList.this, "请选择需要删除的记录",
							"删除", JOptionPane.CANCEL_OPTION);
				} else {
					int i = JOptionPane.showConfirmDialog(CompareRuleList.this,"确定要删除这"+deleteRuleList.size()+"项策略吗?","删除",JOptionPane.OK_CANCEL_OPTION);
					//logger.debug("确定删除吗===>"+i);
					if(i==0) {
						//确定删除
						compareRuleDao.deleteCompareRule(deleteRuleList);
						QueryCondition q=null;
						compareRuleList = compareRuleDao.selectCompareRuleList(q);
						table.setModel(new ListAbstractTableModel(compareRuleList));
						hiddenColumn("_id", table);
						JOptionPane.showMessageDialog(CompareRuleList.this, "删除成功！",
								"提示信息", JOptionPane.INFORMATION_MESSAGE);
						table.repaint();

					}
				}
			}
		});
		
		
		modifyButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				Iterator iterator=compareRuleList.iterator();
				//要删除的策略
				List<List> modifyRuleList=new ArrayList<List>();
				while (iterator.hasNext()) {
					List compareRule = (List) iterator.next();
					if(compareRule.get(0).equals(true)){
						modifyRuleList.add(compareRule);
					}
				}
				if(modifyRuleList.size()==0){
					JOptionPane.showMessageDialog(CompareRuleList.this, "请选择修改的策略",
							"提示信息", JOptionPane.INFORMATION_MESSAGE);
				}else if(modifyRuleList.size()>1){
					JOptionPane.showMessageDialog(CompareRuleList.this, "一次只能修改一个策略",
							"提示信息", JOptionPane.INFORMATION_MESSAGE);
				}else{
					DBObject compareRule = new BasicDBObject();
					compareRule.put("策略名称", modifyRuleList.get(0).get(1));
					//compareRuleDao.getCompareRule(compareRule);
				}
			}
		});
	
	}
	/**
	 * 隐藏列
	 * @param field
	 * @param table
	 */
	public void hiddenColumn(String field,JTable table){   
        TableColumn tc=table.getColumn(field);   
        tc.setWidth(0);   
        tc.setPreferredWidth(0);   
        tc.setMaxWidth(0);   
        tc.setMinWidth(0);
        int columnIndex=table.getColumn(field).getModelIndex();
        table.getTableHeader().getColumnModel().getColumn(columnIndex)  
              .setMaxWidth(0);   
        table.getTableHeader().getColumnModel().getColumn(columnIndex)
             .setMinWidth(0);   
    }
	
	
	private QueryCondition getQueryCondition() {
		QueryCondition queryCondition = new QueryCondition();
		QueryPolicy queryPolicy = new QueryPolicy();
		List<ComboCondition> comboConditions = new ArrayList<ComboCondition>();
		ComboCondition comboCondition = new ComboCondition();
		comboCondition.setAssoCondition("且");
		comboCondition.setField("规则名称");
		comboCondition.setOperation("=");
		comboCondition.setFieldValue("规则名称");
		comboConditions.add(comboCondition);
		queryPolicy.setComboConditions(comboConditions);
		queryCondition.setQueryPolicy(queryPolicy);
		return queryCondition;
	}
	
	//使用AbstractTableModel来创建表格模型
	class ListAbstractTableModel extends AbstractTableModel {
		List<List> compareRuleList;
		public ListAbstractTableModel(List<List> compareRuleList)
		{    
			if(compareRuleList.size()>0)
			  this.compareRuleList = compareRuleList;
			else{
				this.compareRuleList=new ArrayList<List>();
			    List noData=new ArrayList();
			    for(int i=0;i<tableHeads.length;i++){
			    	if(i==7)
			    		noData.add("没有数据...");
			    	else
			    		noData.add("没有数据...");
			    }
			    this.compareRuleList.add(noData);
			}
		}

		public List<List> getCompareRuleList() {
			return compareRuleList;
		}

		public void setCompareRuleList(List<List> compareRuleList) {
			this.compareRuleList = compareRuleList;
		}

		public String getColumnName(int c)
		{  
			
				return tableHeads[c];
		
		}

		public int getColumnCount()
		{  
			 
		     return tableHeads.length;
			
		}
		
		public Object getValueAt(int r, int c)
		{  
			   return compareRuleList.get(r).get(c);
		}
		
		public int getRowCount()
		{  
				return compareRuleList.size();
		}
		
		public boolean isCellEditable(int rowIndex, int columnIndex) 
		{
			    if(columnIndex==0)
				  return true;
			    else
			      return true;
		}

	   @Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		   compareRuleList.get(rowIndex).set(columnIndex, aValue);
		   fireTableCellUpdated(rowIndex, columnIndex);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			// TODO Auto-generated method stub
			return getValueAt(0,columnIndex).getClass();
		}
		
	}
	
}
