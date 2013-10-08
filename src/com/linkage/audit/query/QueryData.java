package com.linkage.audit.query;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.linkage.audit.main.AuditFrame;
import com.linkage.audit.main.BaseFrame;

public class QueryData extends BaseFrame{
	private static Logger logger = Logger.getLogger(QueryData.class);
	
	private AuditFrame auditFrame;
	public QueryData(AuditFrame auditFrame) {
		super();
		this.auditFrame = auditFrame;
		setIconifiable(true);
		setMaximizable(true);
		setClosable(true);
		setTitle("查询筛选");
		setBounds(0, 0, 800, 600);
		setLayout(new GridBagLayout());
		setVisible(true);
		
		QueryPane queryPane = new QueryPane(auditFrame);
		setupComponet(queryPane, 0, 0, 0, 0, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		QueryData.logger = logger;
	}
}
