package com.linkage.audit.compare;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.linkage.audit.dao.CompareRuleDao;
import com.linkage.audit.main.AuditFrame;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class CompareRuleAdd extends JInternalFrame{
	/**
	 * 规则名称
	 */
	private JTextField ruleName;
	
	/**
	 * sheet1编号
	 */
	private JTextField sheet1Numb;
	/**
	 * sheet1对比开始列
	 */
	private JTextField sheet1BeginCol;
	/**
	 * sheet1对比结束列
	 */
	private JTextField sheet1EndCol;
	/**
	 * sheet1对比开始行
	 */
	private JTextField sheet1BeginLine;
	/**
	 * sheet1对比结束行
	 */
	private JTextField sheet1EndLine;
	/**
	/**
	 * sheet2编号
	 */
	private JTextField sheet2Numb;
	/**
	 * sheet2对比开始列
	 */
	private JTextField sheet2BeginCol;
	/**
	 * sheet2对比结束列
	 */
	private JTextField sheet2EndCol;
	/**
	 * sheet2对比开始行
	 */
	private JTextField sheet2BeginLine;
	/**
	 * sheet2对比结束行
	 */
	private JTextField sheet2EndLine;

	
	/**
	 * 大小写内容标示颜色
	 */
	private JTextField letterColor;
	
	/**
	 * 空格内容标示颜色
	 */
	private JTextField trimColor;
	
	/**
	 * 完全不同内容标示颜色
	 */
	private JTextField differentColor;
	
	
	/**
	 * 重置按钮
	 */
	private JButton resetButton;
	
	private Color color=new Color(0,0,0);
	
	private CompareRuleDao compareRuleDao = new CompareRuleDao();
	
	/**
	 * 主窗口
	 */
	private AuditFrame auditFrame;
	
	public CompareRuleAdd(final AuditFrame auditFrame) {
		super();
		this.auditFrame = auditFrame;
		setIconifiable(true);
		setClosable(true);
		setTitle("添加对比策略");
		setBounds(30, 10, 560, 450);
		setLayout(new GridBagLayout());
		setVisible(true);
		
		final JLabel ruleNameLb = new JLabel();
		ruleNameLb.setText("策略名称：");
		setupComponet(ruleNameLb, 0, 0, 1, 0, false,new Insets(5, 0, 3, 1));
		ruleName = new JTextField();
		// 定位策略名称文本框
		setupComponet(ruleName, 1, 0, 2, 100, true,new Insets(5, 0, 3, 1));
		
		final JLabel data1Lb = new JLabel();
		data1Lb.setText("数据1");
		setupComponet(data1Lb, 0, 1, 1, 0, false,new Insets(5, 0, 3, 1));
		
		final JLabel sheet1NumbLb = new JLabel();
		sheet1NumbLb.setText("sheet编号：");
		setupComponet(sheet1NumbLb, 0, 2, 1, 0, false,new Insets(5, 0, 3, 1));
		sheet1Numb = new JTextField();
		// 定位sheet1编号列文本框
		setupComponet(sheet1Numb, 1, 2, 1, 100, false,new Insets(5, 0, 3, 1));
		
		final JLabel sheet1BeginColLb = new JLabel();
		sheet1BeginColLb.setText("对比开始列：");
		setupComponet(sheet1BeginColLb, 0, 3, 1, 0, false,new Insets(5, 0, 3, 1));
		sheet1BeginCol = new JTextField();
		// 定位对比开始列文本框
		setupComponet(sheet1BeginCol, 1, 3, 1, 100, false,new Insets(5, 0, 3, 1));
		
		final JLabel sheet1EndColLb = new JLabel();
		sheet1EndColLb.setText("对比结束列：");
		setupComponet(sheet1EndColLb, 2, 3, 1, 0, false,new Insets(5, 25, 3, 1));
		sheet1EndCol = new JTextField();
		// 定位对比结束列文本框
		setupComponet(sheet1EndCol, 3, 3, 1, 100, false,new Insets(5, 10, 3, 1));
		
		final JLabel sheet1BeginLineLb = new JLabel();
		sheet1BeginLineLb.setText("对比开始行：");
		setupComponet(sheet1BeginLineLb, 0, 4, 1, 0, false,new Insets(5, 0, 3, 1));
		sheet1BeginLine = new JTextField();
		// 定位对比开始行文本框
		setupComponet(sheet1BeginLine, 1, 4, 1, 100, false,new Insets(5, 0, 3, 1));
		
		final JLabel sheet1EndLineLb = new JLabel();
		sheet1EndLineLb.setText("对比结束行：");
		setupComponet(sheet1EndLineLb, 2, 4, 1, 0, false,new Insets(5, 25, 3, 1));
		sheet1EndLine = new JTextField();
		// 定位对比结束行文本框
		setupComponet(sheet1EndLine, 3, 4, 1, 100, false,new Insets(5, 10, 3, 1));
	
		final JLabel data2Lb = new JLabel();
		data2Lb.setText("数据2");
		setupComponet(data2Lb, 0, 5, 1, 0, false,new Insets(5, 0, 3, 1));
		
		final JLabel sheet2NumbLb = new JLabel();
		sheet2NumbLb.setText("sheet编号：");
		setupComponet(sheet2NumbLb, 0, 6, 1, 0, false,new Insets(5, 0, 3, 1));
		sheet2Numb = new JTextField();
		// 定位sheet2编号列文本框
		setupComponet(sheet2Numb, 1, 6, 1, 100, false,new Insets(5, 0, 3, 1));
		
		final JLabel sheet2BeginColLb = new JLabel();
		sheet2BeginColLb.setText("对比开始列：");
		setupComponet(sheet2BeginColLb, 0, 7, 1, 0, false,new Insets(5, 0, 3, 1));
		sheet2BeginCol = new JTextField();
		// 定位对比开始列文本框
		setupComponet(sheet2BeginCol, 1,7, 1, 100, false,new Insets(5, 0, 3, 1));
		
		final JLabel sheet2EndColLb = new JLabel();
		sheet2EndColLb.setText("对比结束列：");
		setupComponet(sheet2EndColLb, 2, 7, 1, 0, false,new Insets(5, 25, 3, 1));
		sheet2EndCol = new JTextField();
		// 定位对比结束列文本框
		setupComponet(sheet2EndCol, 3, 7, 1, 100, false,new Insets(5,10, 3, 1));
		
		final JLabel sheet2BeginLineLb = new JLabel();
		sheet2BeginLineLb.setText("对比开始行：");
		setupComponet(sheet2BeginLineLb, 0, 8, 1, 0, false,new Insets(5, 0, 3, 1));
		sheet2BeginLine = new JTextField();
		// 定位对比开始行文本框
		setupComponet(sheet2BeginLine, 1, 8, 1, 100, false,new Insets(5, 0, 3, 1));
		
		final JLabel sheet2EndLineLb = new JLabel();
		sheet2EndLineLb.setText("对比结束行：");
		setupComponet(sheet2EndLineLb, 2, 8, 1, 0, false,new Insets(5, 25, 3, 1));
		sheet2EndLine = new JTextField();
		// 定位对比结束行文本框
		setupComponet(sheet2EndLine, 3, 8, 1, 100, false,new Insets(5, 10, 3, 1));
		
		
		final JLabel letterColorLb = new JLabel();
		letterColorLb.setText("大小写标示颜色：");
		setupComponet(letterColorLb, 0, 9, 1, 0, false,new Insets(5, 0, 3, 1));
		letterColor = new JTextField();
		// 定位大小写内容标示颜色文本框
		setupComponet(letterColor, 1, 9, 2, 150, false,new Insets(5, 0, 3, 1));
		//添加鼠标点击的监听器
	    letterColor.addMouseListener(new ChooseColor());
		
		final JLabel trimColorLb = new JLabel();
		trimColorLb.setText("空格内容标示颜色：");
		setupComponet(trimColorLb, 0, 10, 1, 0, false,new Insets(5, 0, 3, 1));
		trimColor = new JTextField();
		// 定位空格内容标示颜色文本框
		setupComponet(trimColor, 1, 10, 2, 150, false,new Insets(5, 0, 3, 1));
		trimColor.addMouseListener(new ChooseColor());
		
		final JLabel differentColorLb = new JLabel();
		differentColorLb.setText("完全不同内容标示颜色：");
		setupComponet(differentColorLb, 0, 11, 1, 0, false,new Insets(5, 0, 3, 1));
		differentColor = new JTextField();
		// 定位不同内容完全标示颜色文本框
		setupComponet(differentColor, 1, 11, 2, 150, false,new Insets(5, 0, 3, 1));
		differentColor.addMouseListener(new ChooseColor());
		
		
		final JButton saveButton = new JButton("保存");
		// 定位保存按钮
		setupComponet(saveButton, 2, 12, 1, 10, false,new Insets(10, 15, 3, 0));
		saveButton.addActionListener(new SaveButtonActionListener());
		resetButton = new JButton("重置");
		// 定位重置按钮
		setupComponet(resetButton, 3, 12, 1, 10, false,new Insets(10, 0, 3,0));
		resetButton.addActionListener(new resetButtonActionListener());
	}
	
	// 设置组件位置并添加到容器中
	private void setupComponet(JComponent component, int gridx, int gridy,
			int gridwidth, int ipadx, boolean fill,Insets insets) {
		final GridBagConstraints gridBagConstrains = new GridBagConstraints();
		gridBagConstrains.gridx = gridx;
		gridBagConstrains.gridy = gridy;
		gridBagConstrains.insets = insets;
		if (gridwidth > 1)
			gridBagConstrains.gridwidth = gridwidth;
		if (ipadx > 0)
			gridBagConstrains.ipadx = ipadx;
		if (fill)
			gridBagConstrains.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstrains.anchor=GridBagConstraints.WEST;
		add(component, gridBagConstrains);
	}
	
	// 保存按钮的事件监听类
	private final class SaveButtonActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			DBObject compareRule = new BasicDBObject();
			compareRule.put("策略名称", ruleName.getText().trim());
			DBObject data1 = new BasicDBObject();
			data1.put("sheet编号", sheet1Numb.getText().toString().trim());
			data1.put("对比开始列", sheet1BeginCol.getText().trim());
			data1.put("对比结束列", sheet1EndCol.getText().trim());
			data1.put("对比开始行", sheet1BeginLine.getText().trim());
			data1.put("对比结束行", sheet1EndLine.getText().trim());
			compareRule.put("数据1", data1);
			DBObject data2 = new BasicDBObject();
			data2.put("sheet编号", sheet2Numb.getText().toString().trim());
			data2.put("对比开始列", sheet2BeginCol.getText().trim());
			data2.put("对比结束列", sheet2EndCol.getText().trim());
			data2.put("对比开始行", sheet2BeginLine.getText().trim());
			data2.put("对比结束行", sheet2EndLine.getText().trim());
			compareRule.put("数据2", data2);
			compareRule.put("完全不同内容标示颜色", differentColor.getText().trim());
			compareRule.put("大小写标示颜色", letterColor.getText().trim());
			compareRule.put("空格标示颜色", trimColor.getText().trim());
			
			compareRuleDao.addCompareRule(compareRule);
			JOptionPane.showMessageDialog(CompareRuleAdd.this, "已成功添加对比策略",
					"对比策略添加信息", JOptionPane.INFORMATION_MESSAGE);
			reset();
		}
	}
	// 重置按钮的事件监听类
	private class resetButtonActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			reset();
		}
	}
	
	private void reset(){
		ruleName.setText("");
		sheet1Numb.setText("");
		sheet1BeginCol.setText("");
		sheet1EndCol.setText("");
		sheet1BeginLine.setText("");
		sheet1EndLine.setText("");
		sheet2Numb.setText("");
		sheet2BeginCol.setText("");
		sheet2EndCol.setText("");
		sheet2BeginLine.setText("");
		sheet2EndLine.setText("");
		letterColor.setText("");
		trimColor.setText("");
		differentColor.setText("");
	}
		
		// 标示颜色文本的鼠标点击事件监听类
				private class ChooseColor extends MouseAdapter {
					@Override
					public void mouseClicked(MouseEvent e) {
						color=JColorChooser.showDialog(CompareRuleAdd.this, "标示颜色", color);
						JTextField colorText=(JTextField)e.getComponent();
						colorText.setText(color.getRGB()+"");
					}

				}
		
	     
}
