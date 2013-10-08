package com.linkage.audit.main;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class BaseFrame extends JInternalFrame {
	// 设置组件位置并添加到容器中
	public void setupComponet(JComponent component, int gridx, int gridy,
			int gridwidth, int ipadx, double weightx,double weighty,int fill,int anchor) {
		setupComponet(this,component,gridx,gridy,
				gridwidth,ipadx,weightx,weighty,fill,anchor,1,null);
	}
	
	// 设置组件位置并添加到容器中
	public void setupComponet(JComponent component, int gridx, int gridy,
			int gridwidth, int ipadx, double weightx,double weighty,int fill,int anchor,int gridheight,Insets insets) {
		setupComponet(this,component,gridx,gridy,
				gridwidth,ipadx,weightx,weighty,fill,anchor,gridheight,insets);
	}
	
	public void setupComponet(Container container,JComponent component, int gridx, int gridy,
			int gridwidth,int ipadx, double weightx,double weighty,int fill,int anchor) {
		setupComponet(container,component,gridx,gridy,
				gridwidth,ipadx,weightx,weighty,fill,anchor,1,null);
	}
	
	public void setupComponet(Container container,JComponent component, int gridx, int gridy,
			int gridwidth,int ipadx, double weightx,double weighty,int fill,int anchor,int gridheight,Insets insets) {
		final GridBagConstraints gridBagConstrains = new GridBagConstraints();
		gridBagConstrains.gridx = gridx;
		gridBagConstrains.gridy = gridy;
		if(insets==null) {
			gridBagConstrains.insets = new Insets(5, 2, 2, 5);
		} else {
			gridBagConstrains.insets = insets;
		}
		if (gridwidth > 1)
			gridBagConstrains.gridwidth = gridwidth;
		if (gridheight > 1)
			gridBagConstrains.gridwidth = gridheight;
		if (ipadx > 0)
			gridBagConstrains.ipadx = ipadx;
		if(weightx>0) {
			gridBagConstrains.weightx = weightx;
		} 
		if(weighty>0) {
			gridBagConstrains.weighty = weighty;
		}
		if (fill!=0)
			gridBagConstrains.fill = fill;
		if(anchor!=10) {
			gridBagConstrains.anchor = anchor;
		}
		container.add(component, gridBagConstrains);
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
	
	/**
	 * 更新表格内容
	 * @param iterator
	 * @param firstLoad
	 */
	public void updateTable(DefaultTableModel dftm,Iterator iterator,boolean firstLoad) {
		if(firstLoad) {
			int rowCount = dftm.getRowCount();
			for (int i = 0; i < rowCount; i++) {
				dftm.removeRow(0);
			}
		}
		
		while (iterator.hasNext()) {
			Vector vector = new Vector();
			List view = (List) iterator.next();
			vector.addAll(view);
			dftm.addRow(vector);
		}
	}
	
	/**
	 * 更新表格内容
	 * @param iterator
	 * @param firstLoad
	 */
	public void updateTable(DefaultTableModel dftm,Iterator iterator) {
		int rowCount = dftm.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			dftm.removeRow(0);
		}
		while (iterator.hasNext()) {
			Vector vector = new Vector();
			List view = (List) iterator.next();
			vector.addAll(view);
			dftm.addRow(vector);
		}
	}
}
