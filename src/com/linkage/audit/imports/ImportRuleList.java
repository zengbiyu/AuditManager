package com.linkage.audit.imports;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.linkage.audit.common.Constants;
import com.linkage.audit.common.QueryCondition;
import com.linkage.audit.dao.ImportRuleDao;
import com.linkage.audit.main.AuditFrame;
import com.linkage.audit.main.PagePane;
import com.linkage.audit.main.AuditFrame.OpenFrameAction;
import com.linkage.audit.main.BaseFrame;
import com.linkage.audit.model.ComboCondition;
import com.linkage.audit.model.QueryPolicy;
import com.linkage.audit.util.StringUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ImportRuleList extends BaseFrame {
	
	private static Logger logger = Logger.getLogger(ImportRuleList.class);
	
	/**
	 * 规则名称
	 */
	private JTextField ruleName;
	/**
	 * 字段名称
	 */
	private JTextField fieldName;
	
	private DefaultTableModel dftm;
	
	private JTable table;
	
	private ImportRuleDao importRuleDao = new ImportRuleDao();
	
	/**
	 * 主窗口
	 */
	private AuditFrame auditFrame;
	
	private PagePane pagePane;
	
	public ImportRuleList(final AuditFrame auditFrame) {
		super();
		this.auditFrame = auditFrame;
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setTitle("导入规则列表");
		getContentPane().setLayout(new GridBagLayout());
		setBounds(100, 100, 620, 375);
		//setVisible(true);
		
		final JLabel ruleNameLb = new JLabel();
		ruleNameLb.setText("规则名称：");
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		getContentPane().add(ruleNameLb, gridBagConstraints);
		
		ruleName = new JTextField();
		final GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.ipadx = 200;
		gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridwidth = 3;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.gridx = 1;
		getContentPane().add(ruleName, gridBagConstraints1);
		// 定位规则名称文本框
		
		final JLabel fieldNameLb = new JLabel();
		fieldNameLb.setText("字段名称：");
		final GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.gridx = 4;
		getContentPane().add(fieldNameLb, gridBagConstraints2);
		
		fieldName = new JTextField();
		// 定位字段名称文本框
		final GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.ipadx = 200;
		gridBagConstraints3.insets = new Insets(0, 0, 0, 0);
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.gridwidth = 3;
		gridBagConstraints3.gridy = 0;
		gridBagConstraints3.gridx = 5;
		getContentPane().add(fieldName, gridBagConstraints3);
		
		final JButton queryButton = new JButton("查询");
		// 定位保存按钮
		final GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridy = 1;
		gridBagConstraints4.gridx = 9;
		getContentPane().add(queryButton, gridBagConstraints4);
		
		final JButton queryAllButton = new JButton("显示全部数据");
		// 定位重置按钮
		final GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridy = 1;
		gridBagConstraints5.gridx = 10;
		getContentPane().add(queryAllButton, gridBagConstraints5);
		
		
		queryButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				pagePane.setCurrentPage(1);
				pagePane.setPageCount((Integer)pagePane.getComboBox().getSelectedItem());
			}
		});
		
		queryAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				selectImportRuleList();
				pagePane.setCurrentPage(1);
				clear();
				pagePane.setPageCount((int)importRuleDao.countImportRule());
			}
		});

		final JButton selectButton = new JButton(new SelectFrameAction(auditFrame, "导入数据", "imports.ImportData", null));
		selectButton.setText("选择");
		final JButton addButton = new JButton(auditFrame.new OpenFrameAction("添加导入规则", "imports.ImportRuleAdd", null));
		addButton.setText("添加");
		final JButton editButton = new JButton(new EditFrameAction(auditFrame, "修改导入规则", "imports.ImportRuleEdit", null));
		editButton.setText("修改");
		final JButton deleteButton = new JButton();
		deleteButton.setText("删除");
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRowCount()==0||table.getSelectedRowCount()>1) {
					JOptionPane.showMessageDialog(ImportRuleList.this, "请选择需要删除的记录",
							"删除", JOptionPane.CANCEL_OPTION);
				} else {
					int i = JOptionPane.showConfirmDialog(ImportRuleList.this,"确定删除吗?","删除",JOptionPane.OK_CANCEL_OPTION);
					//logger.debug("确定删除吗===>"+i);
					if(i==0) {
						//确定删除
						String _id = table.getValueAt(table.getSelectedRow(), 1).toString();
						DBObject query = new BasicDBObject();
						query.put("_id",new ObjectId(_id));
						importRuleDao.deleteImportRule(query);
						dftm.removeRow(table.getSelectedRow());
					}
				}
			}
		});
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(selectButton);
		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);
		final GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridy = 7;
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridwidth = 10;
		gridBagConstraints11.anchor=GridBagConstraints.WEST;
		getContentPane().add(buttonPanel, gridBagConstraints11);
		
		final JScrollPane scrollPane = new JScrollPane();
		final GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
		gridBagConstraints10.weighty = 1.0;
		gridBagConstraints10.anchor = GridBagConstraints.NORTH;
		gridBagConstraints10.insets = new Insets(0, 10, 0, 10);
		gridBagConstraints10.fill = GridBagConstraints.BOTH;
		gridBagConstraints10.gridwidth = 11;
		gridBagConstraints10.gridy = 10;
		gridBagConstraints10.gridx = 0;
		getContentPane().add(scrollPane, gridBagConstraints10);
		
		
		
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setShowGrid(true);
		
		pagePane = new PagePane(table){
			@Override
			public List<List> getPageData() {
				List<List> importRuleList = importRuleDao
						.selectImportRuleList(getQueryCondition());
				return importRuleList;
			}
			
			@Override
			public void initTable() {
				// 显示分页数据
				List<List> importRuleList = importRuleDao
						.selectImportRuleList(getQueryCondition());
				updateTable(ImportRuleList.this.dftm, importRuleList.iterator());
				setTotalRowCount((int) importRuleDao.countImportRule(getQueryCondition()));
				super.initTable();
			}
			
			@Override
			public void refreshTable() {
				setVisible(false);
				updateTable(dftm, getPageData().iterator());
				setVisible(true);
			}
		};
		final GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.weighty = 0;
		gridBagConstraints12.anchor = GridBagConstraints.WEST;
		gridBagConstraints12.insets = new Insets(0, 10, 0, 10);
		gridBagConstraints12.fill = GridBagConstraints.NONE;
		gridBagConstraints12.gridwidth = 11;
		gridBagConstraints12.gridy = 11;
		gridBagConstraints12.gridx = 0;
		gridBagConstraints12.weightx = 1;
		getContentPane().add(pagePane, gridBagConstraints12);
		
		//不可编辑
		table.setModel(new DefaultTableModel(){
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		
		dftm = (DefaultTableModel) table.getModel();
		//查询列名
		//初始化
		List<String> tableHeads = importRuleDao.selectImportRuleTableHeads();
		String[] tableHeads2 = new String[]{"序号","_id","规则名称", "编码", "开始列",
				"结束列", "开始行", "结束行", "字段表达式", "字段名称", "开始行是否为字段"};
		if(tableHeads!=null&&tableHeads.size()>0) {
			dftm.setColumnIdentifiers(tableHeads.toArray());
		} else {
			dftm.setColumnIdentifiers(tableHeads2);
		}
		
		scrollPane.setViewportView(table);
		//隐藏_id列
		hiddenColumn("_id",table);
		
		pagePane.setPageCount(pagePane.getPageCount());
	}

	public void clear() {
		ruleName.setText("");
		fieldName.setText("");
	}
	
	
	public void selectImportRuleList() {
		boolean firstLoad = true;
		long count=importRuleDao.countImportRule();
		
		if(count>10000) {
			int batchSize=1000;
			for(int i=0;i<count;) {
				QueryCondition queryCondition = new QueryCondition();
				queryCondition.setNumToSkip(i);
				queryCondition.setBatchSize(batchSize);
				List<List> importRuleList = importRuleDao.selectImportRuleList(queryCondition);
				//查询表格记录
				updateTable(ImportRuleList.this.dftm,importRuleList.iterator(),firstLoad);
				if(firstLoad==true) {
					firstLoad=false;
				}
				i=i+batchSize;
			}
		} else if(count!=0) {
			QueryCondition queryCondition = new QueryCondition();
			List<List> importRuleList = importRuleDao.selectImportRuleList(queryCondition);
			//查询表格记录
			updateTable(ImportRuleList.this.dftm,importRuleList.iterator(),firstLoad);
		}
	}
	
	/**
	 * 修改按钮 监听器
	 *
	 */
	public final class EditFrameAction extends OpenFrameAction {

		private static final long serialVersionUID = 1L;

		public EditFrameAction(AuditFrame auditFrame, String cname,
				String frameName, Icon icon) {
			auditFrame.super(cname, frameName, icon);
		}
		
		public void actionPerformed(final ActionEvent e) {
			ImportRuleEdit importRuleEdit = null;
			
			if(table.getSelectedRowCount()==0||table.getSelectedRowCount()>1) {
				JOptionPane.showMessageDialog(ImportRuleList.this, "请选择需要修改的记录",
						"修改", JOptionPane.CANCEL_OPTION);
			} else {
				JInternalFrame jf = auditFrame.getIFrame(frameName);
				// 在内部窗体闭关时，从内部窗体容器ifs对象中清除该窗体。
				jf.addInternalFrameListener(new InternalFrameAdapter() {
					public void internalFrameClosed(InternalFrameEvent e) {
						auditFrame.getIfs().remove(frameName);
					}
				});
				if (jf.getDesktopPane() == null) {
					auditFrame.getDesktopPane().add(jf);
					jf.setVisible(true);
				}
				try {
					jf.setSelected(true);
				} catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}
				importRuleEdit = (ImportRuleEdit)jf;
				
				
				String _id = table.getValueAt(table.getSelectedRow(), 1).toString();
				
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("_id", _id);
				//根据ID获得导入规则
				List<HashMap<String, Object>> list = importRuleDao.selectImportRule(paramsMap);
				if(list!=null&&list.size()>0) {
					//设置导入规则修改窗口
					HashMap<String, Object> hashMap = list.get(0);
					importRuleEdit.set_id(hashMap.get("_id").toString().trim());
					importRuleEdit.getRuleName().setText(hashMap.get("规则名称").toString().trim());
					String encode = hashMap.get("编码").toString().trim();
					for(int i=0;i<Constants.ENCODING.length;i++) {
						if(encode.equals(Constants.ENCODING[i])) {
							importRuleEdit.getEncondig().setSelectedIndex(i);
							break;
						}
					}
					importRuleEdit.getBeginCol().setText(StringUtil.getString(hashMap.get("开始列")));
					importRuleEdit.getEndCol().setText(StringUtil.getString(hashMap.get("结束列")));
					importRuleEdit.getBeginLine().setText(StringUtil.getString(hashMap.get("开始行")));
					importRuleEdit.getEndLine().setText(StringUtil.getString(hashMap.get("结束行")));
					importRuleEdit.getFieldExpre().setText(StringUtil.getString(hashMap.get("字段表达式")));
					importRuleEdit.getFieldName().setText(StringUtil.getString(hashMap.get("字段名称")));
					String fieldLine=StringUtil.getString(hashMap.get("开始行是否为字段"));
					if("否".equals(fieldLine)) {
						importRuleEdit.getNo().setSelected(true);
					} else {
						importRuleEdit.getYes().setSelected(true);
					}
				}
				importRuleEdit.setVisible(true);
			}
		}
	}
	
	public final class SelectFrameAction extends OpenFrameAction {

		private static final long serialVersionUID = 1L;

		public SelectFrameAction(AuditFrame auditFrame, String cname,
				String frameName, Icon icon) {
			auditFrame.super(cname, frameName, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JInternalFrame jf = null;
			if (auditFrame.getIfs().containsKey(frameName)) {
				if(table.getSelectedRowCount()==0||table.getSelectedRowCount()>1) {
					JOptionPane.showMessageDialog(ImportRuleList.this, "请选择一条记录",
							"选择", JOptionPane.CANCEL_OPTION);
				} else {
					jf = auditFrame.getIfs().get(frameName);
					ImportData importData = (ImportData)jf;
					String _id = table.getValueAt(table.getSelectedRow(), 1).toString();
					Map<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put("_id", _id);
					//根据ID获得导入规则
					List<HashMap<String, Object>> list = importRuleDao.selectImportRule(paramsMap);
					if(list!=null&&list.size()>0) {
						//设置导入规则修改窗口
						HashMap<String, Object> hashMap = list.get(0);
						importData.getRuleName().setText(hashMap.get("规则名称").toString().trim());
						String encode = hashMap.get("编码").toString().trim();
						for(int i=0;i<Constants.ENCODING.length;i++) {
							if(encode.equals(Constants.ENCODING[i])) {
								importData.getEncondig().setSelectedIndex(i);
								break;
							}
						}
						importData.getBeginCol().setText(StringUtil.getString(hashMap.get("开始列")));
						importData.getEndCol().setText(StringUtil.getString(hashMap.get("结束列")));
						importData.getBeginLine().setText(StringUtil.getString(hashMap.get("开始行")));
						importData.getEndLine().setText(StringUtil.getString(hashMap.get("结束行")));
						importData.getFieldExpre().setText(StringUtil.getString(hashMap.get("字段表达式")));
						importData.getFieldName().setText(StringUtil.getString(hashMap.get("字段名称")));
						String fieldLine=StringUtil.getString(hashMap.get("开始行是否为字段"));
						if("否".equals(fieldLine)) {
							importData.getNo().setSelected(true);
						} else {
							importData.getYes().setSelected(true);
						}
					}
					ImportRuleList.this.dispose();
				}
			}
		}
	}
	
	/**
	 * 获得查询条件
	 * @return
	 */
	private QueryCondition getQueryCondition() {
		QueryCondition queryCondition = new QueryCondition();
		QueryPolicy queryPolicy = new QueryPolicy();
		List<ComboCondition> comboConditions = new ArrayList<ComboCondition>();
		ComboCondition ruleNameCondition = new ComboCondition();
		
		if(!"".equals(ruleName.getText().trim())) {
			ruleNameCondition.setAssoCondition("且");
			ruleNameCondition.setField("规则名称");
			ruleNameCondition.setOperation("包含");
			ruleNameCondition.setFieldValue(ruleName.getText().trim());
			comboConditions.add(ruleNameCondition);
		}
		
		if(!"".equals(fieldName.getText().trim())) {
			ComboCondition fieldCondition = new ComboCondition();
			fieldCondition.setAssoCondition("且");
			fieldCondition.setField("字段");
			fieldCondition.setOperation("包含");
			fieldCondition.setFieldValue(fieldName.getText().trim());
			comboConditions.add(fieldCondition);
		}
		
		queryPolicy.setComboConditions(comboConditions);
		queryCondition.setQueryPolicy(queryPolicy);
		
		queryCondition.setBatchSize(pagePane.getPageCount());
		queryCondition.setNumToSkip((pagePane.getCurrentPage()-1)*pagePane.getPageCount());
		return queryCondition;
	}
}
