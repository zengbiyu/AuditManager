package com.linkage.audit.compare;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.linkage.audit.imports.ImportRuleList.EditFrameAction;
import com.linkage.audit.main.AuditFrame;
import com.linkage.audit.main.AuditFrame.OpenFrameAction;

public class CompareData extends JInternalFrame {
	
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
	 * 不同内容标示颜色
	 */
	private JTextField color;
	
	private JFileChooser openFile;
	private int result;
	private File file;
	InputStream is;
    List<List> sheet1Data=new ArrayList<List>();
    List<List> sheet2Data=new ArrayList<List>();
    TableModel model1,model2;
    JTable table1,table2;
    
    /**
	 * 主窗口
	 */
	private AuditFrame auditFrame;
	
	public CompareData(final AuditFrame auditFrame){
		super();
		this.auditFrame = auditFrame;
		setIconifiable(true);
		setMaximizable(true);
		setClosable(true);
		setTitle("数据对比");
		setBounds(0, 0, 780, 470);
		setLayout(new GridBagLayout());
		setVisible(true);
		System.out.println(getDesktopPane());
		
		final JButton compareRuleBtn = new JButton(auditFrame.new OpenFrameAction("对比策略载入", "compare.CompareRuleList", null));
		setupComponet(compareRuleBtn, 0, 0, 1,1,0,new Insets(10,5,0,0));
		
		JButton compareRuleCustomBtn=new JButton("自定义对比策略");
		setupComponet(compareRuleCustomBtn,1, 0,1,1,0,new Insets(10,0,0,0));
		
		JButton beginCompareBtn=new JButton("开始对比");
		beginCompareBtn.addActionListener(new BeginCompareButtonActionListener());
		setupComponet(beginCompareBtn, 2, 0, 1,1,0,new Insets(10,20,0,0));
		
		JButton formSheetBtn=new JButton("导出");
		setupComponet(formSheetBtn, 3, 0, 1,1,0,new Insets(10,0,0,0));
        
		
		final JLabel sheet1NumbLb = new JLabel();
		sheet1NumbLb.setText("sheet编号：");
		setupComponet(sheet1NumbLb, 0, 1, 1,1,0,new Insets(10,5,0,0));
		sheet1Numb = new JTextField();
		// 定位sheet1编号列文本框
		setupComponet(sheet1Numb, 1, 1, 1,1,100,new Insets(10,0,0,0));
		
		final JLabel sheet1BeginColLb = new JLabel();
		sheet1BeginColLb.setText("对比开始列：");
		setupComponet(sheet1BeginColLb, 0, 2,1,1,0,new Insets(10,5,0,0));
		sheet1BeginCol = new JTextField();
		// 定位对比开始列文本框
		setupComponet(sheet1BeginCol, 1, 2, 1,1,100,new Insets(10,0,0,0));
		
		final JLabel sheet1EndColLb = new JLabel();
		sheet1EndColLb.setText("对比结束列：");
		setupComponet(sheet1EndColLb, 2, 2,1, 1,0,new Insets(10,10,0,0));
		sheet1EndCol = new JTextField();
		// 定位对比结束列文本框
		setupComponet(sheet1EndCol, 3, 2, 1,1,100,new Insets(10,0,0,0));
		
		final JLabel sheet1BeginLineLb = new JLabel();
		sheet1BeginLineLb.setText("对比开始行：");
		setupComponet(sheet1BeginLineLb, 0, 3,1, 1,0,new Insets(10,5,0,0));
		sheet1BeginLine = new JTextField();
		// 定位对比开始行文本框
		setupComponet(sheet1BeginLine, 1, 3, 1,1,100, new Insets(10,0,0,0));
		
		final JLabel sheet1EndLineLb = new JLabel();
		sheet1EndLineLb.setText("对比结束行：");
		setupComponet(sheet1EndLineLb, 2, 3, 1,1,0,new Insets(10,10,0,0));
		sheet1EndLine = new JTextField();
		// 定位对比结束行文本框
		setupComponet(sheet1EndLine, 3, 3, 1,1,100,new Insets(10,0,0,0));
		
		final JLabel sheet2NumbLb = new JLabel();
		sheet2NumbLb.setText("sheet编号：");
		setupComponet(sheet2NumbLb, 6, 1, 1,1,0,new Insets(10,10,0,0));
		sheet2Numb = new JTextField();
		// 定位sheet2编号列文本框
		setupComponet(sheet2Numb, 7, 1, 1,1, 100, new Insets(10,10,0,0));
		
		final JLabel sheet2BeginColLb = new JLabel();
		sheet2BeginColLb.setText("对比开始列：");
		setupComponet(sheet2BeginColLb, 6, 2, 1,1,0,new Insets(10,10,0,0));
		sheet2BeginCol = new JTextField();
		// 定位对比开始列文本框
		setupComponet(sheet2BeginCol, 7, 2, 1,1,100,new Insets(10,10,0,0));
		
		final JLabel sheet2EndColLb = new JLabel();
		sheet2EndColLb.setText("对比结束列：");
		setupComponet(sheet2EndColLb, 8, 2, 1,1,0,new Insets(10,20,0,0));
		sheet2EndCol = new JTextField();
		// 定位对比结束列文本框
		setupComponet(sheet2EndCol, 9, 2, 1,1,100,new Insets(10,10,0,0));
		
		final JLabel sheet2BeginLineLb = new JLabel();
		sheet2BeginLineLb.setText("对比开始行：");
		setupComponet(sheet2BeginLineLb,6, 3, 1 , 1,0,new Insets(10,10,0,0));
		sheet2BeginLine = new JTextField();
		// 定位对比开始行文本框
		setupComponet(sheet2BeginLine, 7, 3, 1, 1,100,new Insets(10,10,0,0));
		
		final JLabel sheet2EndLineLb = new JLabel();
		sheet2EndLineLb.setText("对比结束行：");
		setupComponet(sheet2EndLineLb, 8, 3, 1,1,0,new Insets(10,20,0,0));
		sheet2EndLine = new JTextField();
		// 定位对比结束行文本框
		setupComponet(sheet2EndLine, 9, 3, 1,1,100,new Insets(10,10,0,0));
		
		final JLabel colorLb = new JLabel();
		colorLb.setText("不同内容标示颜色：");
		setupComponet(colorLb, 0, 5, 1,1, 0,new Insets(20,5,0,0));
		color = new JTextField();
		// 定位不同内容标示颜色文本框
		setupComponet(color, 1, 5, 1,1,100,new Insets(20,0,0,0));
		 


		//初始化sheet1的内容
		initSheet1();
		
		//初始化sheet2的内容
		initSheet2();
		
		
		
		
	}
	
	//表格的数据模型
     class TableModel extends AbstractTableModel{
    	private List<List> sheetData;
        public TableModel(){}
        public TableModel(List<List> sheetData){
        	this.sheetData=sheetData;
        }
		@Override
		public int getRowCount() {
			return sheetData.size()-1;
		}

		@Override
		public int getColumnCount() {
			return sheetData.get(0).size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return sheetData.get(rowIndex+1).get(columnIndex);
		}
		@Override
		public String getColumnName(int column) {
			return (String) sheetData.get(0).get(column);
		}
    	 
     }
     
  // 设置组件位置并添加到容器中
 	private void setupComponet(JComponent component, int gridx, int gridy,
 			int gridwidth,int gridheight,int ipadx,Insets insets) {
 		final GridBagConstraints gridBagConstrains = new GridBagConstraints();
 		gridBagConstrains.gridx = gridx;
 		gridBagConstrains.gridy = gridy;
 		gridBagConstrains.gridwidth = gridwidth;
 		gridBagConstrains.gridheight = gridheight;	
 		gridBagConstrains.ipadx = ipadx;
 		gridBagConstrains.anchor=GridBagConstraints.WEST;
 		gridBagConstrains.insets = insets;
 		if(component instanceof JTabbedPane){
 			gridBagConstrains.fill = GridBagConstraints.BOTH;
 			gridBagConstrains.weightx=1;
 			gridBagConstrains.weighty=1;
 		}
 		this.add(component, gridBagConstrains);
 	}
 	//设置sheet1里组件的位置并添加到sheet1里
 	private void setupComponetIntoSheet(JPanel jp,JComponent component, int gridx, int gridy,
 			int gridwidth,int gridheight,int ipadx,Insets insets) {
 		final GridBagConstraints gridBagConstrains = new GridBagConstraints();
 		gridBagConstrains.gridx = gridx;
 		gridBagConstrains.gridy = gridy;
 		gridBagConstrains.gridwidth = gridwidth;
 		gridBagConstrains.gridheight = gridheight;
 		gridBagConstrains.ipadx = ipadx;
 		gridBagConstrains.anchor=GridBagConstraints.WEST;
 		if(component instanceof JScrollPane){
 			gridBagConstrains.fill = GridBagConstraints.BOTH;
 			gridBagConstrains.weightx=1;
 			gridBagConstrains.weighty=1;
 		}
 		gridBagConstrains.insets = insets;
 		jp.add(component, gridBagConstrains);
 	}
 	
 	//初始化initsheet1的内容
 	private void initSheet1(){
		JButton addData1Btn=new JButton("新增导入数据");
		setupComponet(addData1Btn, 0, 6, 1,1,0,new Insets(10,5,0,0));
		
		JButton queryPolicyBtn=new JButton("筛选策略载入");
		setupComponet(queryPolicyBtn, 1,6, 1,1,0,new Insets(10,0,0,0));
		
		JButton queryPolicyCustomBtn=new JButton("自定义筛选策略");
		setupComponet(queryPolicyCustomBtn,2, 6,1,1,0,new Insets(10,0,0,0));
		
		JPanel sheet1BtnJPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton sortData1Btn=new JButton("排序");
		JButton searchData1Btn=new JButton("查询");
		JButton AdvancedSearchBtn=new JButton("高级查询");
		JButton formSheet1Btn=new JButton("导出");
		sheet1BtnJPanel.add(sortData1Btn);
		sheet1BtnJPanel.add(searchData1Btn);
		sheet1BtnJPanel.add(AdvancedSearchBtn);
		sheet1BtnJPanel.add(formSheet1Btn);
		//将sheet1BtnJPanel添加到容器里
		setupComponet(sheet1BtnJPanel, 0, 7, 4,1,0,new Insets(0,0,0,0));
		
		
		
 		        //sheet1的选项面板添加到容器中
 				final JTabbedPane tp1=new JTabbedPane();
 				JPanel sheet1Jp=new JPanel(new GridBagLayout());
 				tp1.add("sheet1Jp", sheet1Jp);
 				tp1.setEnabledAt(0, true);
 				tp1.setTitleAt(0, "sheet");
 				setupComponet(tp1, 0, 8, 6,7,0,new Insets(0,5,0,0));
 				
 				//以下是sheet1面板里的内容
 			  	 JLabel sheet1PolicyName=new JLabel(" 策略名称：");
 		         setupComponetIntoSheet(sheet1Jp, sheet1PolicyName, 0,0, 1, 1, 0,new Insets(0, 5, 0, 0));
 			   	 JTextField pNameField1=new JTextField();
 			   	 setupComponetIntoSheet(sheet1Jp, pNameField1, 1,0, 2, 1, 185,new Insets(0, 5, 0, 0));
 			     JButton addConditionBtn1=new JButton("添加条件");
 			     setupComponetIntoSheet(sheet1Jp, addConditionBtn1, 3,0, 1, 1, 0,new Insets(0, 5, 0, 0));
 			    	
 			   	 String[] column1={"列1","列2"};
 			   	 JComboBox column1JBOx=new JComboBox(column1);
 			   	setupComponetIntoSheet(sheet1Jp, column1JBOx, 0,1, 1, 1, 50,new Insets(5, 5, 0, 0));
 			   	 
 			   	 String[] column2={"包含","去除"};
 			   	 JComboBox column2JBOx=new JComboBox(column2);
 			   	 setupComponetIntoSheet(sheet1Jp, column2JBOx,1,1, 1, 1, 0,new Insets(5, 5, 0, 0));
 			     	 
 			     JTextField column1Field1=new JTextField();
 			     setupComponetIntoSheet(sheet1Jp, column1Field1, 2,1, 1, 1, 130,new Insets(5, 5, 0, 0));
 			     JButton delConditionBtn1=new JButton("删除条件");
 			     setupComponetIntoSheet(sheet1Jp, delConditionBtn1, 3,1, 1, 1, 0,new Insets(5, 5, 0, 0));
 			     
 			     String[] column3={"且","或"};
 			     JComboBox column3JBOx=new JComboBox(column3);
 			     setupComponetIntoSheet(sheet1Jp, column3JBOx, 1,3, 1, 1, 0,new Insets(5, 5, 0, 0));

 			   	String[] column4={"列1","列2"};
 			    JComboBox column4JBOx=new JComboBox(column4);
 			    setupComponetIntoSheet(sheet1Jp, column4JBOx, 0,4, 1, 1, 50,new Insets(5, 5, 0, 0));
 			     	
 			    String[] column5={"包含","去除"};
 			   	 JComboBox column5JBOx=new JComboBox(column5);
 			   	 setupComponetIntoSheet(sheet1Jp, column5JBOx,1,4, 1, 1, 0,new Insets(5, 5, 0, 0));
 			     	 
 			     JTextField column2Field1=new JTextField();
 			     setupComponetIntoSheet(sheet1Jp, column2Field1, 2,4, 1, 1, 130,new Insets(5, 5, 0, 0));
 			     JButton delConditionBtn2=new JButton("删除条件");
 			     setupComponetIntoSheet(sheet1Jp, delConditionBtn2, 3,4, 1, 1, 0,new Insets(5, 5, 0, 0));
 			     
 			      	 
 				table1=new JTable(5,11);
 		    	 JScrollPane table1ScrollJp=new  JScrollPane(table1);
 		    	 sheet1Jp.add(table1ScrollJp);
 		          setupComponetIntoSheet(sheet1Jp, table1ScrollJp, 0,5, 5, 7, 0,new Insets(10, 5, 10, 5));
 		         addData1Btn.addActionListener(new ActionListener() {
 					
 					@Override
 					public void actionPerformed(ActionEvent e) {
 						openFile=new JFileChooser();
 						openFile.setDialogTitle("打开文件");
 						result=openFile.showOpenDialog(CompareData.this);
 						
 						if(result==JFileChooser.APPROVE_OPTION){
 							file=openFile.getSelectedFile();
 							try {
 								is = new FileInputStream(file);
 							     sheet1Data=daoExcel(is);
 						       
 						} catch (Exception e1) {
 								// TODO Auto-generated catch block
 								e1.printStackTrace();
 							} 
 							 model1=new TableModel(sheet1Data);
 							 table1.setModel(model1);
 							 
 							 tp1.setTitleAt(0, file.getName());
 					    	//重绘表格，载入sheet的数据
 							 table1.repaint();
 						}
 					}
 				});
 		    	 
 				
 	}
 	    //初始化initsheet2的内容
 	 	private void initSheet2(){
 			JButton addData2Btn=new JButton("新增导入数据");
 			setupComponet(addData2Btn, 6, 6, 1,1,0,new Insets(10,5,0,0));
 			
 			JButton queryPolicyBtn=new JButton("筛选策略载入");
 			setupComponet(queryPolicyBtn,7,6, 1,1,0,new Insets(10,0,0,0));
 			
 			JButton queryPolicyCustomBtn=new JButton("自定义筛选策略");
 			setupComponet(queryPolicyCustomBtn,8, 6,1,1,0,new Insets(10,0,0,0));
 			
 			JPanel sheet2BtnJPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
 			JButton sortData1Btn=new JButton("排序");
 			JButton searchData1Btn=new JButton("查询");
 			JButton AdvancedSearchBtn=new JButton("高级查询");
 			JButton formSheet1Btn=new JButton("导出");
 			sheet2BtnJPanel.add(sortData1Btn);
 			sheet2BtnJPanel.add(searchData1Btn);
 			sheet2BtnJPanel.add(AdvancedSearchBtn);
 			sheet2BtnJPanel.add(formSheet1Btn);
 			//将sheet2BtnJPanel添加到容器里
 			setupComponet(sheet2BtnJPanel, 6, 7, 4,1,0,new Insets(0,0,0,0));
 		
 			
 	 		        //sheet2的选项面板添加到容器中
 	 				final JTabbedPane tp2=new JTabbedPane();
 	 				JPanel sheet2Jp=new JPanel(new GridBagLayout());
 	 				tp2.add("sheet1Jp", sheet2Jp);
 	 				tp2.setEnabledAt(0, true);
 	 				tp2.setTitleAt(0, "sheet");
 	 				setupComponet(tp2, 6, 8, 8,7,0,new Insets(0,5,0,0));
 	 				
 	 				//以下是sheet2面板里的内容
 	 			  	 JLabel sheet2PolicyName=new JLabel(" 策略名称：");
 	 		         setupComponetIntoSheet(sheet2Jp, sheet2PolicyName, 0,0, 1, 1, 0,new Insets(0, 5, 0, 0));
 	 			   	 JTextField pNameField1=new JTextField();
 	 			   	 setupComponetIntoSheet(sheet2Jp, pNameField1, 1,0, 2, 1, 185,new Insets(0, 5, 0, 0));
 	 			     JButton addConditionBtn1=new JButton("添加条件");
 	 			     setupComponetIntoSheet(sheet2Jp, addConditionBtn1, 3,0, 1, 1, 0,new Insets(0, 5, 0, 0));
 	 			    	
 	 			   	 String[] column1={"列1","列2"};
 	 			   	 JComboBox column1JBOx=new JComboBox(column1);
 	 			   	setupComponetIntoSheet(sheet2Jp, column1JBOx, 0,1, 1, 1, 50,new Insets(5, 5, 0, 0));
 	 			   	 
 	 			   	 String[] column2={"包含","去除"};
 	 			   	 JComboBox column2JBOx=new JComboBox(column2);
 	 			   	 setupComponetIntoSheet(sheet2Jp, column2JBOx,1,1, 1, 1, 0,new Insets(5, 5, 0, 0));
 	 			     	 
 	 			     JTextField column1Field1=new JTextField();
 	 			     setupComponetIntoSheet(sheet2Jp, column1Field1, 2,1, 1, 1, 130,new Insets(5, 5, 0, 0));
 	 			     JButton delConditionBtn1=new JButton("删除条件");
 	 			     setupComponetIntoSheet(sheet2Jp, delConditionBtn1, 3,1, 1, 1, 0,new Insets(5, 5, 0, 0));
 	 			     
 	 			     String[] column3={"且","或"};
 	 			     JComboBox column3JBOx=new JComboBox(column3);
 	 			     setupComponetIntoSheet(sheet2Jp, column3JBOx, 1,3, 1, 1, 0,new Insets(5, 5, 0, 0));

 	 			   	String[] column4={"列1","列2"};
 	 			    JComboBox column4JBOx=new JComboBox(column4);
 	 			    setupComponetIntoSheet(sheet2Jp, column4JBOx, 0,4, 1, 1, 50,new Insets(5, 5, 0, 0));
 	 			     	
 	 			    String[] column5={"包含","去除"};
 	 			   	 JComboBox column5JBOx=new JComboBox(column5);
 	 			   	 setupComponetIntoSheet(sheet2Jp, column5JBOx,1,4, 1, 1, 0,new Insets(5, 5, 0, 0));
 	 			     	 
 	 			     JTextField column2Field1=new JTextField();
 	 			     setupComponetIntoSheet(sheet2Jp, column2Field1, 2,4, 1, 1, 130,new Insets(5, 5, 0, 0));
 	 			     JButton delConditionBtn2=new JButton("删除条件");
 	 			     setupComponetIntoSheet(sheet2Jp, delConditionBtn2, 3,4, 1, 1, 0,new Insets(5, 5, 0, 0));
 	 			     
 	 			      	 
 	 				table2=new JTable(5,11);
 	 		    	JScrollPane table1ScrollJp=new  JScrollPane(table2);
 	 		    	sheet2Jp.add(table1ScrollJp);
 	 		        setupComponetIntoSheet(sheet2Jp, table1ScrollJp, 0,5, 5, 7, 0,new Insets(10, 5, 10, 5));
 	 		 	   
 	 		        
 	 		  	
 	 	 			addData2Btn.addActionListener(new ActionListener() {
 	 	 				
 	 	 				@Override
 	 	 				public void actionPerformed(ActionEvent e) {
 	 	 					openFile=new JFileChooser();
 	 	 					openFile.setDialogTitle("打开文件");
 	 	 					result=openFile.showOpenDialog(CompareData.this);
 	 	 					
 	 	 					if(result==JFileChooser.APPROVE_OPTION){
 	 	 						file=openFile.getSelectedFile();
 	 	 						try {
 	 	 							is = new FileInputStream(file);
 	 	 						     sheet2Data=daoExcel(is);
 	 	 					       
 	 	 					} catch (Exception e1) {
 	 	 							// TODO Auto-generated catch block
 	 	 							e1.printStackTrace();
 	 	 						} 
 	 	 						model2=new TableModel(sheet2Data);
 	 	 						table2.setModel(model2);
 	 	 						tp2.setTitleAt(0, file.getName());
 	 	 				    	//重绘表格，载入sheet的数据
 	 	 						 table2.repaint();
 	 	 					}
 	 	 				}
 	 	 			});
 	 				
 	 	}
 	
 	
 	//导入excel数据
 	private List<List> daoExcel(InputStream is){
 		List<List> sheetData=new ArrayList<List>();
 		//根据输入流创建Workbook对象   
        XSSFWorkbook wb = null;
		try {
			wb = (XSSFWorkbook) WorkbookFactory.create(is);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //得到sheet对象
        XSSFSheet sheet=wb.getSheetAt(0);
        for(Row row: sheet){
        	List rowList=new ArrayList();
        	for(Cell cell: row){
        		 //cell.getCellType是获得cell里面保存的值的type   
                //如Cell.CELL_TYPE_STRING   
                switch(cell.getCellType()){   
                    case Cell.CELL_TYPE_BOOLEAN:   
                        //得到Boolean对象的方法   
                    	rowList.add(cell.getBooleanCellValue());   
                        break;   
                    case Cell.CELL_TYPE_NUMERIC:   
                        //先看是否是日期格式   
                        if(DateUtil.isCellDateFormatted(cell)){   
                            //读取日期格式   
                        	rowList.add(cell.getDateCellValue());   
                        }else{   
                            //读取数字   
                        	rowList.add(cell.getNumericCellValue());   
                        }   
                        break;   
                    case Cell.CELL_TYPE_FORMULA:   //当单元为公式的时候
                    	//获取公式评估工具
                    	FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();  
                    	//获取公式在计算时，所引用的行列，并声明。
                    	CellReference cellReference = new CellReference("Y"+row.getRowNum());  
                    	Row r = sheet.getRow(cellReference.getRow());  
                		Cell c = row.getCell(cellReference.getCol());   
                		//获得公式计算后的结果
                		CellValue cellValue = evaluator.evaluate(c); 
                		
                		rowList.add(cellValue.getNumberValue());   
                        break;   
                    case Cell.CELL_TYPE_STRING:   
                        //读取String   
                    	rowList.add(cell.getRichStringCellValue().toString());   
                        break;                     
                }
          } 
        	
          sheetData.add(rowList);
        }
		  return sheetData;
 	}
 	
   // 开始对比按钮的事件监听类
 	private class BeginCompareButtonActionListener implements ActionListener {
 		public void actionPerformed(final ActionEvent e) {
 		    List<List> addUsers=new ArrayList<List>();
 		    List<List> deleteUsers=new ArrayList<List>();
 		    if(sheet1Data.size()!=0&&sheet2Data.size()!=0){
 		    	
 		    compareUsers(addUsers,sheet2Data,sheet1Data);
 		    compareUsers(deleteUsers,sheet1Data,sheet2Data);
 		
 		    //设置对比结果显示的颜色
	    	 TableCellRenderer tcrTable1=new TableCellRenderer(deleteUsers,addUsers);
		     table1.getColumn(model1.getColumnName(8)).setCellRenderer(tcrTable1);
		     //对比结束重新绘制table显示颜色
		     table1.repaint();
 		    
 		    //设置对比结果显示的颜色
 	    	 TableCellRenderer tcrTable2=new TableCellRenderer(addUsers,deleteUsers);
 		     table2.getColumn(model2.getColumnName(8)).setCellRenderer(tcrTable2);
 		     
 		     //对比结束重新绘制table显示颜色
 		     table2.repaint();
 		     //revalidate()有延迟或是需点击才能重绘
 		     //table2.revalidate();
 		    JOptionPane.showMessageDialog(CompareData.this, "新增账户数："+addUsers.size()+"  删除账户数："+deleteUsers.size(),
					"对比结果", JOptionPane.INFORMATION_MESSAGE);
 		     
 		   }else{
 			  JOptionPane.showMessageDialog(CompareData.this, "请添加两组对比数据",
 						"提示信息", JOptionPane.INFORMATION_MESSAGE);
 		   }
 		   
 	
 		}
 	}
 	
 	//对比用户
 	public void compareUsers(List<List> result, List<List> sheet1, List<List> sheet2){
		for(int i=0; i<sheet1.size(); i++){
			if(!sheet2.contains(sheet1.get(i))){
				result.add(sheet1.get(i));
			}
		}
	}
 	
 	class TableCellRenderer extends DefaultTableCellRenderer{
 		  private List<List> users;
 		  private List<List> otherUsers;
 		  private List name=new ArrayList();
 		  private List otherName=new  ArrayList();;
 		  public TableCellRenderer(){}
 		  public TableCellRenderer(List<List> users,List<List> otherUsers){
 			 this.users=users;
 			 this.otherUsers=otherUsers;
 			 init();
 		  }
 		  private void init(){
 			  for(int i=0;i<=users.size()-1;i++){
 				name.add(users.get(i).get(8));
		       } 
 			 for(int i=0;i<=otherUsers.size()-1;i++){
 				otherName.add(otherUsers.get(i).get(8)); 
 		      } 
 		  }
 		   
 		  public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,
  				 boolean hasFocus,int row,int column){
  			      if(name.contains(value)){
  				     setBackground(new Color(206,231,255));
 			      }
 			       else{
 			    	 setBackground(Color.white);
 			      }
 			      //System.out.println("o"+otherName.size());
				     for(int i=0;i<otherName.size();i++){
	 			    	// System.out.println("o"+otherName.get(i).toString());
				       if(otherName.get(i).toString().equalsIgnoreCase(value.toString())){
				    		  setBackground(Color.red);
				    		  break;
				    		  
				    	}
				       if(otherName.get(i).toString().trim().equalsIgnoreCase(value.toString().trim())){
				    		  setBackground(Color.yellow);
				    		  break;
				    		  
				    	} 
				       
 		
				    }
		    	return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		      
  		  }
  	   
 	}
 	
	
}
