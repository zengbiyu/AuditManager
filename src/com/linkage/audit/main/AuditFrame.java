package com.linkage.audit.main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class AuditFrame {
	
	private static Logger logger = Logger.getLogger(AuditFrame.class);
	
	private JDesktopPane desktopPane;
	private JFrame frame;
	private JLabel backLabel;
	// 创建窗体的Map类型集合对象
	private Map<String, JInternalFrame> ifs = new HashMap<String, JInternalFrame>();
	
	static {
		String sptr = System.getProperty("file.separator");
		//File f = new File(System.getProperty("server.root"));
		File f=new File("D:/work/SEclipse/workspace-audit/AuditManager");
		String confPath=new StringBuffer(f.getPath()).append(sptr)
				.append("etc").append(sptr).toString();
		String log4jconf=confPath+"log4j.properties";
		PropertyConfigurator.configure(log4jconf);
	}
	
	public AuditFrame() {
		frame = new JFrame("审计分析工具");
		frame.getContentPane().setBackground(new Color(170, 188, 120));
		frame.addComponentListener(new FrameListener());
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		backLabel = new JLabel();// 背景标签
		backLabel.setVerticalAlignment(SwingConstants.TOP);
		backLabel.setHorizontalAlignment(SwingConstants.CENTER);
		updateBackImage(); // 更新或初始化背景图片
		desktopPane = new JDesktopPane();
		desktopPane.add(backLabel, new Integer(Integer.MIN_VALUE));
		frame.getContentPane().add(desktopPane);
		JTabbedPane navigationPanel = createNavigationPanel(); // 创建导航标签面板
		frame.getContentPane().add(navigationPanel, BorderLayout.NORTH);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new AuditFrame();
			}
		});
	}
	
	private JTabbedPane createNavigationPanel() { // 创建导航标签面板的方法
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFocusable(false);
		tabbedPane.setBackground(new Color(211, 230, 192));
		tabbedPane.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		JPanel importManagePanel = new JPanel(); // 导入管理面板
		importManagePanel.setBackground(new Color(215, 223, 194));
		importManagePanel.setLayout(new BoxLayout(importManagePanel,
				BoxLayout.X_AXIS));
		importManagePanel.add(createFrameButton("添加导入规则", "imports.ImportRuleAdd"));
		importManagePanel.add(createFrameButton("导入规则列表", "imports.ImportRuleList"));
		
		JPanel queryManagePanel = new JPanel(); // 查询筛选管理面板
		queryManagePanel.setBackground(new Color(215, 223, 194));
		queryManagePanel.setLayout(new BoxLayout(queryManagePanel,
				BoxLayout.X_AXIS));
		queryManagePanel.add(createFrameButton("查询筛选", "query.QueryData"));
		queryManagePanel.add(createFrameButton("添加筛选策略", "query.QueryPolicyAdd"));
		queryManagePanel.add(createFrameButton("筛选策略列表", "query"));
		
		JPanel compareManagePanel = new JPanel(); // 数据对比管理面板
		compareManagePanel.setBackground(new Color(215, 223, 194));
		compareManagePanel.setLayout(new BoxLayout(compareManagePanel,
				BoxLayout.X_AXIS));
		compareManagePanel.add(createFrameButton("数据对比", "compare.CompareData"));
		compareManagePanel.add(createFrameButton("添加对比策略", "compare.CompareRuleAdd"));
		compareManagePanel.add(createFrameButton("对比策略列表", "compare.CompareRuleList"));
		
		JPanel matchManagePanel = new JPanel(); // 数据匹配管理面板
		matchManagePanel.setBackground(new Color(215, 223, 194));
		matchManagePanel.setLayout(new BoxLayout(matchManagePanel,
				BoxLayout.X_AXIS));
		matchManagePanel.add(createFrameButton("数据匹配", "compare"));
		matchManagePanel.add(createFrameButton("添加匹配策略", "compare"));
		matchManagePanel.add(createFrameButton("匹配策略列表", "compare"));
		
		JPanel databaseManagePanel = new JPanel(); // 数据库配置管理面板
		databaseManagePanel.setBackground(new Color(215, 223, 194));
		databaseManagePanel.setLayout(new BoxLayout(databaseManagePanel,
				BoxLayout.X_AXIS));
		databaseManagePanel.add(createFrameButton("MongoDB配置", "compare"));
		
		tabbedPane.addTab("   导入管理   ", null, importManagePanel, "导入管理");
		tabbedPane.addTab("  查询筛选管理   ", null, queryManagePanel, "查询筛选管理");
		tabbedPane.addTab("  数据对比管理   ", null, compareManagePanel, "数据对比管理");
		tabbedPane.addTab("  数据匹配管理   ", null, matchManagePanel, "数据匹配管理");
		tabbedPane.addTab("  数据库配置   ", null, databaseManagePanel, "数据库配置管理");

		return tabbedPane;
	}
	/** *********************辅助方法************************* */
	// 为内部窗体添加Action的方法
	private JButton createFrameButton(String fName, String cname) {
		String imgUrl = "res/ActionIcon/" + fName + ".png";
		String imgUrl_roll = "res/ActionIcon/" + fName	+ "_roll.png";
		String imgUrl_down = "res/ActionIcon/" + fName	+ "_down.png";
		Icon icon = new ImageIcon(imgUrl);
		Icon icon_roll = null;
		if (imgUrl_roll != null)
			icon_roll = new ImageIcon(imgUrl_roll);
		Icon icon_down = null;
		if (imgUrl_down != null)
			icon_down = new ImageIcon(imgUrl_down);
		Action action = new OpenFrameAction(fName, cname, icon);
		JButton button = new JButton(action);
		//button.setMargin(new Insets(0, 0, 0, 0));
		//button.setHideActionText(true); 
		button.setHideActionText(false);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		/*if (icon_roll != null)
			button.setRolloverIcon(icon_roll);
		if (icon_down != null)
			button.setPressedIcon(icon_down);*/
		return button;
	}
	// 获取内部窗体的唯一实例对象
	public JInternalFrame getIFrame(String frameName) {
		JInternalFrame jf = null;
		if (!ifs.containsKey(frameName)) {
			try {
				Class fClass = Class.forName("com.linkage.audit." + frameName);
				Constructor constructor = fClass.getConstructor(AuditFrame.class);
				jf = (JInternalFrame) constructor.newInstance(AuditFrame.this);
				ifs.put(frameName, jf);
			} catch (Exception e) {
				String errMsg="创建内部窗口失败，请联系管理员";
				logger.error(errMsg,e);
				e.printStackTrace();
			}
		} else {
			jf = ifs.get(frameName);
		}
		return jf;
	}
	// 更新背景图片的方法
	private void updateBackImage() {
		if (backLabel != null) {
			int backw = AuditFrame.this.frame.getWidth();
			int backh = frame.getHeight();
			backLabel.setSize(backw, backh);
			backLabel.setText("<html><body><image width='" + backw
					+ "' height='" + (backh - 110) + "' src="
					+ AuditFrame.this.getClass().getResource("welcome.jpg")
					+ "'></img></body></html>");
		}
	}
	// 窗体监听器
	private final class FrameListener extends ComponentAdapter {
		public void componentResized(final ComponentEvent e) {
			updateBackImage();
		}
	}
	// 主窗体菜单项的单击事件监听器
	public class OpenFrameAction extends AbstractAction {
		protected String frameName = null;
		private OpenFrameAction() {
		}
		public OpenFrameAction(String cname, String frameName, Icon icon) {
			this.frameName = frameName;
			putValue(Action.NAME, cname);
			putValue(Action.SHORT_DESCRIPTION, cname);
			//putValue(Action.SMALL_ICON, icon);
		}
		public void actionPerformed(final ActionEvent e) {
			JInternalFrame jf = getIFrame(frameName);
			// 在内部窗体闭关时，从内部窗体容器ifs对象中清除该窗体。
			jf.addInternalFrameListener(new InternalFrameAdapter() {
				public void internalFrameClosed(InternalFrameEvent e) {
					ifs.remove(frameName);
				}
			});
			if (jf.getDesktopPane() == null) {
				desktopPane.add(jf);
				jf.setVisible(true);
			}
			try {
				jf.setSelected(true);
			} catch (PropertyVetoException e1) {
				e1.printStackTrace();
			}
		}
	}
	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JDesktopPane getDesktopPane() {
		return desktopPane;
	}

	public void setDesktopPane(JDesktopPane desktopPane) {
		this.desktopPane = desktopPane;
	}

	public Map<String, JInternalFrame> getIfs() {
		return ifs;
	}

	public void setIfs(Map<String, JInternalFrame> ifs) {
		this.ifs = ifs;
	}
}