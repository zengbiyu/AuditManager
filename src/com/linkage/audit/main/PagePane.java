package com.linkage.audit.main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class PagePane extends JPanel {

	private JTable table;

	private JButton previousButton;

	private JButton nextButton;

	private JComboBox comboBox;

	private JLabel pageInfoLabel;

	private int currentPage = 1;// 当前页码

	private int pageCount = 10;// 表页显示行数

	private int totalPage = 0;// 结果集的总页数

	private int totalRowCount = 0;// 结果集的总数量
	
	public PagePane(JTable table) {
		this.table = table;
		init();
	}

	public void init() {
		this.setLayout(new FlowLayout());
		previousButton = new JButton("上一页");
		previousButton.setEnabled(false);
		this.add(previousButton);
		nextButton = new JButton("下一页");
		this.add(nextButton);
		Integer[] items = { 10, 50, 200, 500, 1000, 10000,100000 };
		comboBox = new JComboBox(items);
		comboBox.setSelectedIndex(0);
		this.add(comboBox);
		pageInfoLabel = new JLabel("总共" + this.totalRowCount + "记录|总共"
				+ this.totalPage + "页|当前第" + this.currentPage + "页");
		this.add(pageInfoLabel);

		comboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				Integer selectedItem = (Integer) cb.getSelectedItem();
				setCurrentPage(1);
				setPageCount(selectedItem);
				refreshLabel();
			}
		});
		nextButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				getNextPage();
				if (table != null) {
					refreshTable();
					refreshLabel();
				}
			}
		});
		previousButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				getPreviousPage();
				if (table != null) {
					refreshTable();
					refreshLabel();
				}
			}
		});
	}

	/*
	 * 对工具栏进行刷新，修改展示信息及翻页按钮的显示情况
	 */
	public void refreshLabel() {
		this.pageInfoLabel.setVisible(false);
		pageInfoLabel.setText("总共" + totalRowCount + "记录|总共" + totalPage
				+ "页|当前第" + currentPage + "页");
		pageInfoLabel.setVisible(true);
		if (currentPage == 1) {
			previousButton.setEnabled(false);
			if (totalPage == 1 || totalPage == 0) {
				nextButton.setEnabled(false);
			} else {
				nextButton.setEnabled(true);
			}
		} else if (currentPage == totalPage) {
			nextButton.setEnabled(false);
			previousButton.setEnabled(true);
		}
	}

	/**
	 * 获取下一页
	 */
	public int getNextPage() {
		if (this.currentPage != this.totalPage) {
			return ++currentPage;
		}
		return -1;
	}

	/**
	 * 获取上一页
	 */
	public int getPreviousPage() {
		if (this.currentPage != 1) {
			return --currentPage;
		}
		return -1;
	}

	/*
	 * 进行翻页操作时刷新表数据
	 */
	public void refreshTable() {
	}

	/*
	 * 提供表格修改页面显示行数的表格内容刷新方法
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
		this.initTable();
	}

	/**
	 * 初始化表格数据
	 */
	public void initTable() {
		this.totalPage = totalRowCount % pageCount == 0 ? totalRowCount
				/ pageCount : totalRowCount / pageCount + 1;
		refreshLabel();
		comboBox.setSelectedItem(pageCount);
	}

	/**
	 * 获取分页数据
	 * 
	 * @return
	 */
	public List<List> getPageData() {
		return null;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public JButton getPreviousButton() {
		return previousButton;
	}

	public void setPreviousButton(JButton previousButton) {
		this.previousButton = previousButton;
	}

	public JButton getNextButton() {
		return nextButton;
	}

	public void setNextButton(JButton nextButton) {
		this.nextButton = nextButton;
	}

	public JComboBox getComboBox() {
		return comboBox;
	}

	public void setComboBox(JComboBox comboBox) {
		this.comboBox = comboBox;
	}

	public JLabel getPageInfoLabel() {
		return pageInfoLabel;
	}

	public void setPageInfoLabel(JLabel pageInfoLabel) {
		this.pageInfoLabel = pageInfoLabel;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	public int getPageCount() {
		return pageCount;
	}
}
