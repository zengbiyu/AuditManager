package com.linkage.audit.imports;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.linkage.audit.common.Constants;
import com.linkage.audit.dao.ImportRuleDao;
import com.linkage.audit.main.AuditFrame;
import com.linkage.audit.main.BaseFrame;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ImportRuleAdd extends BaseFrame{
	
	private static Logger logger = Logger.getLogger(ImportRuleList.class);
	/**
	 * 规则名称
	 */
	private JTextField ruleName;
	/**
	 * 编码
	 */
	private JComboBox encondig;
	/**
	 * 开始列
	 */
	private JTextField beginCol;
	/**
	 * 结束列
	 */
	private JTextField endCol;
	/**
	 * 开始行
	 */
	private JTextField beginLine;
	/**
	 * 结束行
	 */
	private JTextField endLine;
	/**
	 * 字段表达式
	 */
	private JTextField fieldExpre;
	/**
	 * 字段名称
	 */
	private JTextField fieldName;
	/**
	 * 开始行是否为字段
	 */
	private  ButtonGroup fieldLine;
	
	private JRadioButton yes;
	
	private JRadioButton no;
	/**
	 * 重置按钮
	 */
	private JButton resetButton;
	
	private ImportRuleDao importRuleDao = new ImportRuleDao();
	
	private AuditFrame auditFrame;
	
	public ImportRuleAdd(final AuditFrame auditFrame) {
		super();
		this.auditFrame = auditFrame;
		setIconifiable(true);
		setClosable(true);
		setTitle("添加导入规则");
		setBounds(100, 100, 460, 300);
		setLayout(new GridBagLayout());
		setVisible(true);
		
		final JLabel ruleNameLb = new JLabel();
		ruleNameLb.setText("规则名称：");
		setupComponet(ruleNameLb, 0, 0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		ruleName = new JTextField();
		// 定位规则名称文本框
		setupComponet(ruleName, 1, 0, 1, 100, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel encondigLb = new JLabel();
		encondigLb.setText("编码：");
		setupComponet(encondigLb, 2, 0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		encondig = new JComboBox(Constants.ENCODING);
		// 定位编码组合框
		setupComponet(encondig, 3, 0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel beginColLb = new JLabel();
		beginColLb.setText("开始列：");
		setupComponet(beginColLb, 0, 1, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		beginCol = new JTextField();
		// 定位开始列文本框
		setupComponet(beginCol, 1, 1, 1, 100, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel endColLb = new JLabel();
		endColLb.setText("结束列：");
		setupComponet(endColLb, 2, 1, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		endCol = new JTextField();
		// 定位结束列文本框
		setupComponet(endCol, 3, 1, 1, 100, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel beginLineLb = new JLabel();
		beginLineLb.setText("开始行：");
		setupComponet(beginLineLb, 0, 2, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		beginLine = new JTextField();
		// 定位开始行文本框
		setupComponet(beginLine, 1, 2, 1, 100, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel endLineLb = new JLabel();
		endLineLb.setText("结束行：");
		setupComponet(endLineLb, 2, 2, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		endLine = new JTextField();
		// 定位结束行文本框
		setupComponet(endLine, 3, 2, 1, 100, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel fieldExpreLb = new JLabel();
		fieldExpreLb.setText("字段表达式：");
		setupComponet(fieldExpreLb, 0, 3, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		fieldExpre = new JTextField();
		// 定位字段表达式文本框
		setupComponet(fieldExpre, 1, 3, 4, 300, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel fieldNameLb = new JLabel();
		fieldNameLb.setText("字段名称：");
		setupComponet(fieldNameLb, 0, 4, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		fieldName = new JTextField();
		// 定位字段名称文本框
		setupComponet(fieldName, 1, 4, 4, 300, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel fieldLineLb = new JLabel();
		fieldLineLb.setText("开始行是否为字段：");
		setupComponet(fieldLineLb, 0, 5, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		fieldLine = new ButtonGroup();
		yes = new JRadioButton("是");
		no = new JRadioButton("否");
		fieldLine.add(yes);
		fieldLine.add(no);
		yes.setSelected(true);
		// 定位开始行是否为字段单选框
		setupComponet(yes, 1, 5, 0, 1, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		setupComponet(no, 2, 5, 0, 1, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JButton saveButton = new JButton("保存");
		// 定位保存按钮
		setupComponet(saveButton, 2, 7, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		saveButton.addActionListener(new SaveButtonActionListener());
		resetButton = new JButton("重置");
		// 定位重置按钮
		setupComponet(resetButton, 3, 7, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		resetButton.addActionListener(new ResetButtonActionListener());
	}
	
	// 保存按钮的事件监听类
	public final class SaveButtonActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			DBObject importRule = new BasicDBObject();
			importRule.put("规则名称", ruleName.getText().trim());
			importRule.put("编码", encondig.getSelectedItem().toString());
			importRule.put("开始列", beginCol.getText().trim());
			importRule.put("结束列", endCol.getText().trim());
			importRule.put("开始行", beginLine.getText().trim());
			importRule.put("结束行", endLine.getText().trim());
			importRule.put("字段表达式", fieldExpre.getText().trim());
			importRule.put("字段名称", fieldName.getText().trim());
			Enumeration<AbstractButton> enu = fieldLine.getElements();
			while (enu.hasMoreElements()) {
				AbstractButton radioButton = enu.nextElement();
				if(radioButton.isSelected()) {
					importRule.put("开始行是否为字段", radioButton.getText().trim());
				}
			}
			importRuleDao.addImportRule(importRule);
			JOptionPane.showMessageDialog(ImportRuleAdd.this, "已成功添加导入规则",
					"导入规则添加信息", JOptionPane.INFORMATION_MESSAGE);
			reset();
		}
	}
	// 重置按钮的事件监听类
	public class ResetButtonActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			reset();
		}
	}
	
	public void reset() {
		this.ruleName.setText("");
		this.encondig.setSelectedIndex(0);
		this.beginCol.setText("");
		this.endCol.setText("");
		this.beginLine.setText("");
		this.endLine.setText("");
		this.fieldExpre.setText("");
		this.fieldName.setText("");
		this.yes.setSelected(true);
	}
	
	public JTextField getRuleName() {
		return ruleName;
	}

	public void setRuleName(JTextField ruleName) {
		this.ruleName = ruleName;
	}

	public JComboBox getEncondig() {
		return encondig;
	}

	public void setEncondig(JComboBox encondig) {
		this.encondig = encondig;
	}

	public JTextField getBeginCol() {
		return beginCol;
	}

	public void setBeginCol(JTextField beginCol) {
		this.beginCol = beginCol;
	}

	public JTextField getEndCol() {
		return endCol;
	}

	public void setEndCol(JTextField endCol) {
		this.endCol = endCol;
	}

	public JTextField getBeginLine() {
		return beginLine;
	}

	public void setBeginLine(JTextField beginLine) {
		this.beginLine = beginLine;
	}

	public JTextField getEndLine() {
		return endLine;
	}

	public void setEndLine(JTextField endLine) {
		this.endLine = endLine;
	}

	public JTextField getFieldExpre() {
		return fieldExpre;
	}

	public void setFieldExpre(JTextField fieldExpre) {
		this.fieldExpre = fieldExpre;
	}

	public JTextField getFieldName() {
		return fieldName;
	}

	public void setFieldName(JTextField fieldName) {
		this.fieldName = fieldName;
	}

	public ButtonGroup getFieldLine() {
		return fieldLine;
	}

	public void setFieldLine(ButtonGroup fieldLine) {
		this.fieldLine = fieldLine;
	}

	public JRadioButton getYes() {
		return yes;
	}

	public void setYes(JRadioButton yes) {
		this.yes = yes;
	}

	public JRadioButton getNo() {
		return no;
	}

	public void setNo(JRadioButton no) {
		this.no = no;
	}

	public JButton getResetButton() {
		return resetButton;
	}

	public void setResetButton(JButton resetButton) {
		this.resetButton = resetButton;
	}

	public ImportRuleDao getImportRuleDao() {
		return importRuleDao;
	}

	public void setImportRuleDao(ImportRuleDao importRuleDao) {
		this.importRuleDao = importRuleDao;
	}

	public AuditFrame getAuditFrame() {
		return auditFrame;
	}

	public void setAuditFrame(AuditFrame auditFrame) {
		this.auditFrame = auditFrame;
	}
}
