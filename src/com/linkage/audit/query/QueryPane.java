package com.linkage.audit.query;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.linkage.audit.dao.DataDao;
import com.linkage.audit.imports.ImportData;
import com.linkage.audit.main.AuditFrame;
import com.linkage.audit.main.AuditFrame.OpenFrameAction;
import com.linkage.audit.main.PagePane;

public class QueryPane extends JPanel{
	
private AuditFrame auditFrame;
	
	private JTabbedPane jTabbedPane;
	
	/**
	 * 数据填充的目的窗口选项卡索引
	 */
	private int distTabIdx;
	
	private PagePane pagePane;
	
	private DataDao dataDao = new DataDao();
	
	public QueryPane(final AuditFrame auditFrame) {
		this.auditFrame = auditFrame;
		setLayout(new GridBagLayout());
		
		final JButton importBtn = new JButton(new ImportBtnActionListener(auditFrame, "导入数据", "imports.ImportData", null));
		importBtn.setText("新增导入数据");
		setupComponet(importBtn, 0, 0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		final JButton policyLoadBtn = new JButton();
		policyLoadBtn.setText("筛选策略载入");
		setupComponet(policyLoadBtn, 1, 0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		final JButton addPolicyBtn = new JButton();
		addPolicyBtn.setText("自定义筛选策略");
		setupComponet(addPolicyBtn, 2, 0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		final JButton sortBtn = new JButton();
		sortBtn.setText("排序");
		setupComponet(sortBtn, 3, 0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		final JButton queryBtn = new JButton();
		queryBtn.setText("查询");
		setupComponet(queryBtn, 4, 0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		final JButton globalQueryBtn = new JButton();
		globalQueryBtn.setText("新增导入数据");
		setupComponet(globalQueryBtn, 5,  0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		final JButton samplingBtn = new JButton();
		samplingBtn.setText("抽样");
		setupComponet(samplingBtn, 6,  0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		final JButton statisticBtn = new JButton();
		statisticBtn.setText("统计分析");
		setupComponet(statisticBtn, 7,  0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		final JButton exportBtn = new JButton();
		exportBtn.setText("导出");
		setupComponet(exportBtn, 8, 0, 1, 0, 1.0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		jTabbedPane = new JTabbedPane();
		setupComponet(jTabbedPane, 0, 1, 9, 0, 0 ,1.0 ,GridBagConstraints.BOTH,GridBagConstraints.NORTH);
	}
	
	private class ImportBtnActionListener extends OpenFrameAction {

		public ImportBtnActionListener(AuditFrame auditFrame, String cname,
				String frameName, Icon icon) {
			auditFrame.super(cname, frameName, icon);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			super.actionPerformed(e);
			JInternalFrame jf = auditFrame.getIFrame(frameName);
			ImportData importData = (ImportData)jf;
			importData.setDistPane(QueryPane.this);
		}
	}

	public AuditFrame getAuditFrame() {
		return auditFrame;
	}

	public void setAuditFrame(AuditFrame auditFrame) {
		this.auditFrame = auditFrame;
	}

	public JTabbedPane getjTabbedPane() {
		return jTabbedPane;
	}

	public void setjTabbedPane(JTabbedPane jTabbedPane) {
		this.jTabbedPane = jTabbedPane;
	}
	
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

	public int getDistTabIdx() {
		return distTabIdx;
	}

	public void setDistTabIdx(int distTabIdx) {
		this.distTabIdx = distTabIdx;
	}

	public PagePane getPagePane() {
		return pagePane;
	}

	public void setPagePane(PagePane pagePane) {
		this.pagePane = pagePane;
	}

	public DataDao getDataDao() {
		return dataDao;
	}

	public void setDataDao(DataDao dataDao) {
		this.dataDao = dataDao;
	}
}
