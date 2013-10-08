package com.linkage.audit.imports;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.linkage.audit.common.Constants;
import com.linkage.audit.common.QueryCondition;
import com.linkage.audit.dao.DataDao;
import com.linkage.audit.exception.AuditException;
import com.linkage.audit.main.AuditFrame;
import com.linkage.audit.main.AuditFrame.OpenFrameAction;
import com.linkage.audit.main.BaseFrame;
import com.linkage.audit.main.PagePane;
import com.linkage.audit.query.QueryPane;
import com.linkage.audit.util.NumberUtil;
import com.linkage.audit.util.RegexUtil;
import com.linkage.audit.util.StringUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ImportData extends BaseFrame {
	
	private static Logger logger = Logger.getLogger(ImportData.class);
	/**
	 * 主窗口
	 */
	private AuditFrame auditFrame;
	
	private JTextField filePath;
	
	private JButton fileChooserBtn;
	
	private JButton dirChooserBth;
	
	private JButton policyLoadBth;
	
	private JButton policyAddBth;
	
	private JTextField sheetName;
	
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
	
	private JButton importBtn;
	
	private JCheckBox createTab;
	/**
	 * 数据填充的目的窗口
	 */
	private QueryPane distPane;
	
	private String distSheetName;
	
	private PagePane pagePane;
	
	private DataDao dataDao = new DataDao();
	
	public ImportData(final AuditFrame auditFrame) {
		super();
		this.auditFrame = auditFrame;
		setIconifiable(true);
		setClosable(true);
		setTitle("导入数据");
		setBounds(100, 100, 460, 350);
		setLayout(new GridBagLayout());
		setVisible(true);
		
		filePath = new JTextField();
		filePath.setEditable(false);
		setupComponet(filePath, 0, 0, 2, 240, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		fileChooserBtn = new JButton(new FileChooserBtnActionListener());
		fileChooserBtn.setText("选择文件");
		setupComponet(fileChooserBtn, 2, 0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		dirChooserBth = new JButton(new DirChooserBthActionListener());
		dirChooserBth.setText("选择目录");
		setupComponet(dirChooserBth, 3, 0, 1, 0, 1 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		policyLoadBth = new JButton(auditFrame.new OpenFrameAction("导入规则列表", "imports.ImportRuleList", null));
		policyLoadBth.setText("导入规则载入");
		setupComponet(policyLoadBth, 0, 1, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST,1,new Insets(0, 20, 0, 0));
		
		policyAddBth = new JButton();
		policyAddBth.setText("自定义规则");
		setupComponet(policyAddBth, 1, 1, 1, 0, 1 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST,1,new Insets(0, 20, 0, 0));
		
		final JLabel sheetNameLb = new JLabel();
		sheetNameLb.setText("Sheet名称：");
		setupComponet(sheetNameLb, 2, 1, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		sheetName = new JTextField();
		setupComponet(sheetName, 3, 1, 1, 80, 1 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		createTab = new JCheckBox();
		createTab.setText("新建选项卡");
		// 定位字段名称文本框
		setupComponet(createTab, 3, 6, 0, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST);
		
		importBtn = new JButton(new ImportBtnFrameAction(auditFrame,"查询筛选", "imports.QueryPolicy", null));
		importBtn.setText("开始导入");
		setupComponet(importBtn, 3, 7, 1, 0, 1 ,0 ,GridBagConstraints.NONE,GridBagConstraints.WEST,1,new Insets(0, 0, 20, 20));
		
		JPanel panel = new JPanel(new GridBagLayout());
		setupComponet(panel, 0, 2, 5, 0, 1 ,1 ,GridBagConstraints.BOTH,GridBagConstraints.WEST);
		
		final JLabel ruleNameLb = new JLabel();
		ruleNameLb.setText("规则名称：");
		setupComponet(panel,ruleNameLb, 0, 0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		ruleName = new JTextField();
		// 定位规则名称文本框
		setupComponet(panel,ruleName, 1, 0, 1, 100, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel encondigLb = new JLabel();
		encondigLb.setText("编码：");
		setupComponet(panel,encondigLb, 2, 0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		encondig = new JComboBox(Constants.ENCODING);
		// 定位编码组合框
		setupComponet(panel,encondig, 3, 0, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel beginColLb = new JLabel();
		beginColLb.setText("开始列：");
		setupComponet(panel,beginColLb, 0, 1, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		beginCol = new JTextField();
		// 定位开始列文本框
		setupComponet(panel,beginCol, 1, 1, 1, 100, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel endColLb = new JLabel();
		endColLb.setText("结束列：");
		setupComponet(panel,endColLb, 2, 1, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		endCol = new JTextField();
		// 定位结束列文本框
		setupComponet(panel,endCol, 3, 1, 1, 100, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel beginLineLb = new JLabel();
		beginLineLb.setText("开始行：");
		setupComponet(panel,beginLineLb, 0, 2, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		beginLine = new JTextField();
		// 定位开始行文本框
		setupComponet(panel,beginLine, 1, 2, 1, 100, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel endLineLb = new JLabel();
		endLineLb.setText("结束行：");
		setupComponet(panel,endLineLb, 2, 2, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		endLine = new JTextField();
		// 定位结束行文本框
		setupComponet(panel,endLine, 3, 2, 1, 100, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel fieldExpreLb = new JLabel();
		fieldExpreLb.setText("字段表达式：");
		setupComponet(panel,fieldExpreLb, 0, 3, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		fieldExpre = new JTextField();
		// 定位字段表达式文本框
		setupComponet(panel,fieldExpre, 1, 3, 4, 300, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel fieldNameLb = new JLabel();
		fieldNameLb.setText("字段名称：");
		setupComponet(panel,fieldNameLb, 0, 4, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		fieldName = new JTextField();
		// 定位字段名称文本框
		setupComponet(panel,fieldName, 1, 4, 4, 300, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		
		final JLabel fieldLineLb = new JLabel();
		fieldLineLb.setText("开始行是否为字段：");
		setupComponet(panel,fieldLineLb, 0, 5, 1, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		fieldLine = new ButtonGroup();
		yes = new JRadioButton("是");
		no = new JRadioButton("否");
		fieldLine.add(yes);
		fieldLine.add(no);
		yes.setSelected(true);
		// 定位开始行是否为字段单选框
		setupComponet(panel,yes, 1, 5, 0, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
		setupComponet(panel,no, 2, 5, 0, 0, 0 ,0 ,GridBagConstraints.NONE,GridBagConstraints.CENTER);
	}
	
	/**
	 * 文件选择按钮 监听器
	 * @author LimingYue
	 *
	 */
	private class FileChooserBtnActionListener extends AbstractAction {
		private static final long serialVersionUID = -7268039106856120381L;

		public void actionPerformed(final ActionEvent e) {
			JFileChooser jfc = new JFileChooser("E:/工作资料/广东移动/材料/数据材料");// 文件选择器
			jfc.setFileSelectionMode(0);// 设定只能选择到文件
			jfc.setFileFilter(new FileFilter() {
				
				@Override
				public String getDescription() {
					return "*.xls;*.xlsx;*.csv;*.log;*.txt;*.xml";
				}
				
				@Override
				public boolean accept(File f) {
					String fileName = f.getName();
					if(fileName.endsWith(".xls")
							||fileName.endsWith(".xlsx")
							||fileName.endsWith(".csv")
							||fileName.endsWith(".log")
							||fileName.endsWith(".txt")
							||fileName.endsWith(".xml")) {
						return true;
					} else {
						return false;
					}
				}
			});
			int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句  
			if (state == 1) {
				return;// 撤销则返回 
			} else {
				File f = jfc.getSelectedFile();// f为选择到的文件
				filePath.setText(f.getAbsolutePath());  
			}
		}
	}
	
	/**
	 * 选择目录按钮监听器
	 * @author LimingYue
	 *
	 */
	private class DirChooserBthActionListener extends AbstractAction {
		private static final long serialVersionUID = 1640198911585474159L;
		
		public void actionPerformed(final ActionEvent e) {
			JFileChooser jfc = new JFileChooser();// 文件选择器
			jfc.setFileSelectionMode(1);// 设定只能选择到文件夹
			int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;// 撤销则返回
			} else {
				File f = jfc.getSelectedFile();// f为选择到的文件
				filePath.setText(f.getAbsolutePath());
			}
		}
	}
	
	/**
	 * 开始导入按钮 监听器
	 * @author LimingYue
	 *
	 */
	private class ImportBtnFrameAction extends OpenFrameAction {

		public ImportBtnFrameAction(AuditFrame auditFrame, String cname,
				String frameName, Icon icon) {
			auditFrame.super(cname, frameName, icon);
		}
		
		public void actionPerformed(final ActionEvent e) {
			try {
				//获取导入规则
				Map<String,String> importRule = new HashMap<String, String>();
				importRule.put("Sheet名称",StringUtil.getString(sheetName.getText()));
				importRule.put("规则名称", StringUtil.getString(ruleName.getText()));
				importRule.put("编码", StringUtil.getString(encondig.getSelectedItem()));
				importRule.put("开始列", StringUtil.getString(beginCol.getText()));
				importRule.put("结束列", StringUtil.getString(endCol.getText()));
				importRule.put("开始行", StringUtil.getString(beginLine.getText()));
				importRule.put("结束行", StringUtil.getString(endLine.getText()));
				importRule.put("字段表达式", StringUtil.getString(fieldExpre.getText()));
				importRule.put("字段名称", StringUtil.getString(fieldName.getText()));
				Enumeration<AbstractButton> enu = fieldLine.getElements();
				while (enu.hasMoreElements()) {
					AbstractButton radioButton = enu.nextElement();
					if(radioButton.isSelected()) {
						importRule.put("开始行是否为字段", StringUtil.getString(radioButton.getText()));
					}
				}
				
				//判断导入的文件类型
				String filePath = StringUtil.getString(ImportData.this.filePath.getText());
				File file = new File(filePath);
				//如果导入的是文件则将选项卡命名为：文件名称_Sheet名称_导入时间
				//如果导入的是文件夹则将选项卡命名为：文件夹名称_Sheet名称_导入时间
				if (file.isFile()) {
					distSheetName = file.getName()+"_"+importRule.get("Sheet名称")+"_"+com.linkage.audit.util.DateUtil.getCurTime();
				}
				List<File> dataFiles= new ArrayList<File>();
				try {
					listFile(file,dataFiles);
				} catch (Exception e1) {
					logger.error("遍历文件失败",e1);
					return;
				}
				//选择不同方式导入
				for(int i=0;i<dataFiles.size();i++) {
					File dataFile = dataFiles.get(i);
					if(dataFile.getPath().endsWith(".xls")) {
						//2003excel导入
						importExcel03(importRule,dataFile);
					} else if(dataFile.getPath().endsWith(".xlsx")) {
						//2007excel导入
						importExcel07(importRule,dataFile);
					} else if(dataFile.getPath().endsWith(".csv")){
						//csv导入
						importCsv(importRule,dataFile);
					} else if(dataFile.getPath().endsWith(".log")){
						//log导入
						importLog(importRule,dataFile);
					} else if(dataFile.getPath().endsWith(".txt")){
						//txt导入
						importTxt(importRule,dataFile);
					} else if(dataFile.getPath().endsWith(".xml")){
						//xml导入
						importXml(importRule,dataFile);
					}
					ImportData.this.dispose();
				}
			} catch (AuditException e1) {
				logger.error(e1.getErrorMsg(),e1);
				JOptionPane.showMessageDialog(ImportData.this, e1.getErrorMsg(),
						"导入", JOptionPane.CANCEL_OPTION);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(ImportData.this, e1.getMessage(),
						"导入", JOptionPane.CANCEL_OPTION);
				logger.error(e1.getMessage(),e1);
			}
		}
	}
	
	/**
	 * 遍历文件夹及子文件夹
	 * @param file
	 * @param dataFiles
	 * @throws Exception
	 */
	public  void listFile(File file,List<File> dataFiles) throws Exception {
        //如果输入的是一个文件就直接将文件的全路径直接打印出来
        if (file.isFile()) {
            logger.debug("File :" + file.getAbsolutePath());
            dataFiles.add(file);

        } else {
            //如果是一个目录就递归调用 listFile 方法将当前目录已经其子目录下的文件都列出来
            logger.debug("Dir :" + file.getAbsolutePath());
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                listFile(files[i],dataFiles);
            }
        }
    }
	
	/**
	 * Excel2003根据导入规则导入数据
	 * @param importRule
	 * @param dataFiles
	 * @throws AuditException,Exception 
	 */
	public void importExcel03(Map<String,String> importRule,File dataFile) throws AuditException,Exception {
		InputStream in = null;
		
        try {
        	String encodig = importRule.get("编码");
        	if("".equals(encodig)) {
        		encodig = "UTF-8";
        	}
        	in = new FileInputStream(dataFile);
        	//根据输入流创建Workbook对象   
             HSSFWorkbook wb = (HSSFWorkbook) WorkbookFactory.create(in);
			// 得到sheet对象
			HSSFSheet sheet = null;
			String sheetName = StringUtil.getString(importRule.get("Sheet名称"));
			if ("".equals(sheetName)) {
				sheet = wb.getSheetAt(0); // 读第一个sheet
			} else {
				sheet = wb.getSheet(sheetName); // 读指定的sheet
			}
			
			String beginCol = importRule.get("开始列");
        	String endCol = importRule.get("结束列");
        	String beginLine = importRule.get("开始行");
        	String endLine = importRule.get("结束行");
        	
        	Integer beginColNum = NumberUtil.getInteger(beginCol);
        	Integer endColNum = NumberUtil.getInteger(endCol);
        	Integer beginLineNum = NumberUtil.getInteger(beginLine);
        	Integer endLineNum = NumberUtil.getInteger(endLine);
        	
        	if(beginLineNum!=null) {
        		beginLineNum = beginLineNum-1;
        	} else {
        		beginLineNum=0;
        	}
        	int rowIndex = beginLineNum;
        	
        	//结束行大于总行数，以总行数为准
        	int rowNum = sheet.getLastRowNum()+1;
        	logger.debug("开始行rowNUm=====>"+rowNum);
        	if(endLineNum!=null&&endLineNum>rowNum) {
        		rowNum = endLineNum;
        	}
        	
        	if(beginColNum!=null) {
        		beginColNum = beginColNum-1;
        	} else {
				beginColNum = 0;
			}
        	
        	//开始列、结束列  每行的单元格数量不满足列的数量，则填空
        	//结束列可不填，结束列-开始列，视为首行的字段或者指定的字段数量
        	//如果结束列-开始列<字段数量，则以结束列为主
        	//如果结束列-开始列>字段数量，则以字段为主
        	//开始行是否为字段
        	boolean isFieldLine = false;
			if("是".equals(importRule.get("开始行是否为字段"))) {
				isFieldLine = true;
			}
			String[] fieldNames = null;
			if(isFieldLine) {
				//开始行为字段
				Row row =  sheet.getRow(rowIndex);
				int cellLen = row.getLastCellNum();
				logger.debug("cellLen===>"+cellLen);
				if(cellLen==0) {
					logger.error("开始行信息为空");
					return;
				} else {
					if(endColNum==null||endColNum>cellLen) {
						endColNum = cellLen;
					}
					fieldNames = new String[cellLen];
				}
			} else {
				//开始行不为字段
				String fieldNameValue = importRule.get("字段名称");
				if("".equals(fieldNameValue)) {
					throw new AuditException("0","字段名称不能为空");
				}
				fieldNames = fieldNameValue.split(" ");
				logger.debug("字段名称====》"+fieldNames);
				int cellLen = fieldNames.length;
				
				if(endColNum==null||endColNum-beginColNum>cellLen) {
					endColNum = cellLen+beginColNum;
				}
			}
			List<DBObject> dataList = new ArrayList<DBObject>();
			List<List> tableDataList = new ArrayList<List>();
			
        	for (; rowIndex < rowNum; rowIndex++) {
	    		try {
	    			int cellLength = 0;
	    			//行没有数据
	    			if(sheet.getRow(rowIndex)!=null) {
	    				cellLength = sheet.getRow(rowIndex).getLastCellNum();
	    			}
	    			if(cellLength==0) {
	    				logger.debug("========"+rowIndex+"空");
	    				continue;
	    			} else {
	    				//开始每行单元格迭代
	    				int colIndex = beginColNum;
	    	        	DBObject dataObject = new BasicDBObject();
	    	        	List tableData = new ArrayList();
	    				for(;colIndex<endColNum;colIndex++) {
	    					Row row =  sheet.getRow(rowIndex);
	    					int fieldNamesIdx = colIndex-beginColNum;
	    					//首行为字段
	    					if(isFieldLine&&rowIndex==beginLineNum) {
	    						if(colIndex>=cellLength) {
	    							//结束列超出第一列的行数，则补全临时字段名称
	    							fieldNames[fieldNamesIdx]="Field"+fieldNamesIdx;
	        					} else {
	        						fieldNames[fieldNamesIdx]=getCellValue(wb, sheet, row, row.getCell(colIndex));
	        					}
	    					} else {
	    						if(colIndex>=cellLength) {
	        						dataObject.put(fieldNames[fieldNamesIdx], "");
	        						tableData.add("");
	        					} else {
	        						Cell cell = row.getCell(colIndex);
	        						dataObject.put(fieldNames[fieldNamesIdx], getCellValue(wb,sheet,row,cell));
	        						tableData.add(getCellValue(wb,sheet,row,cell));
	        					}
	    					}
	    				}
	    				if(rowIndex!=beginLineNum||!isFieldLine) {
	    					dataList.add(dataObject);
		    				tableDataList.add(tableData);
	    				}
	    			}
	    			if(rowIndex%1000==0) {
						//当组装1000条记录时批量更新入库并且写入表格
	    				saveDate(dataList, tableDataList, fieldNames);
	    				dataList = new ArrayList<DBObject>();		
						tableDataList = new ArrayList<List>();
					}
				} catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    continue; // 出现异常继续
                }
	    		System.out.println();
        	}
        	saveDate(dataList, tableDataList, fieldNames);
		} catch (AuditException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Excel2007根据导入规则导入数据
	 * @param importRule
	 * @param dataFiles
	 * @throws AuditException,Exception 
	 */
	public void importExcel07(Map<String,String> importRule,File dataFile) throws AuditException,Exception {
		InputStream in = null;
		
        try {
        	String encodig = importRule.get("编码");
        	if("".equals(encodig)) {
        		encodig = "UTF-8";
        	}
        	in = new FileInputStream(dataFile);
        	//根据输入流创建Workbook对象   
        	XSSFWorkbook wb = (XSSFWorkbook) WorkbookFactory.create(in);
			// 得到sheet对象
			XSSFSheet sheet = null;
			String sheetName = importRule.get("Sheet名称");
			if ("".equals(sheetName)) {
				sheet = wb.getSheetAt(0); // 读第一个sheet
			} else {
				sheet = wb.getSheet(sheetName); // 读指定的sheet
			}
			
			
			String beginCol = importRule.get("开始列");
        	String endCol = importRule.get("结束列");
        	String beginLine = importRule.get("开始行");
        	String endLine = importRule.get("结束行");
        	
        	Integer beginColNum = NumberUtil.getInteger(beginCol);
        	Integer endColNum = NumberUtil.getInteger(endCol);
        	Integer beginLineNum = NumberUtil.getInteger(beginLine);
        	Integer endLineNum = NumberUtil.getInteger(endLine);
        	
        	if(beginLineNum!=null) {
        		beginLineNum = beginLineNum-1;
        	} else {
        		beginLineNum=0;
        	}
        	int rowIndex = beginLineNum;
        	
        	//结束行大于总行数，以总行数为准
        	int rowNum = sheet.getLastRowNum()+1;
        	logger.debug("开始行rowNUm=====>"+rowNum);
        	if(endLineNum!=null&&endLineNum>rowNum) {
        		rowNum = endLineNum;
        	}
        	
        	if(beginColNum!=null) {
        		beginColNum = beginColNum-1;
        	} else {
				beginColNum = 0;
			}
        	
        	//开始列、结束列  每行的单元格数量不满足列的数量，则填空
        	//结束列可不填，结束列-开始列，视为首行的字段或者指定的字段数量
        	//如果结束列-开始列<字段数量，则以结束列为主
        	//如果结束列-开始列>字段数量，则以字段为主
        	//开始行是否为字段
        	boolean isFieldLine = false;
			if("是".equals(importRule.get("开始行是否为字段"))) {
				isFieldLine = true;
			}
			String[] fieldNames = null;
			if(isFieldLine) {
				//开始行为字段
				Row row =  sheet.getRow(rowIndex);
				int cellLen = row.getLastCellNum();
				logger.debug("cellLen===>"+cellLen);
				if(cellLen==0) {
					logger.error("开始行信息为空");
					return;
				} else {
					if(endColNum==null||endColNum>cellLen) {
						endColNum = cellLen;
					}
					fieldNames = new String[cellLen];
					
				}
			} else {
				//开始行不为字段
				String fieldNameValue = importRule.get("字段名称");
				if("".equals(fieldNameValue)) {
					throw new AuditException("0","字段名称不能为空");
				}
				fieldNames = fieldNameValue.split(" ");
				logger.debug("字段名称====》"+fieldNames);
				int cellLen = fieldNames.length;
				
				if(endColNum==null||endColNum-beginColNum>cellLen) {
					endColNum = cellLen+beginColNum;
				}
			}
			List<DBObject> dataList = new ArrayList<DBObject>();
			List<List> tableDataList = new ArrayList<List>();
			
        	for (; rowIndex < rowNum; rowIndex++) {
	    		try {
	    			int cellLength = 0;
	    			//行没有数据
	    			if(sheet.getRow(rowIndex)!=null) {
	    				cellLength = sheet.getRow(rowIndex).getLastCellNum();
	    			}
	    			if(cellLength==0) {
	    				logger.debug("========"+rowIndex+"空");
	    				continue;
	    			} else {
	    				//开始每行单元格迭代
	    				int colIndex = beginColNum;
	    	        	DBObject dataObject = new BasicDBObject();
	    	        	List tableData = new ArrayList();
	    	        	tableData.add(rowIndex-beginColNum+1);
	    				for(;colIndex<endColNum;colIndex++) {
	    					Row row =  sheet.getRow(rowIndex);
	    					int fieldNamesIdx = colIndex-beginColNum;
	    					//首行为字段
	    					if(isFieldLine&&rowIndex==beginLineNum) {
	    						if(colIndex>=cellLength) {
	    							//结束列超出第一列的行数，则补全临时字段名称
	    							fieldNames[fieldNamesIdx]="Field"+fieldNamesIdx;
	        					} else {
	        						fieldNames[fieldNamesIdx]=getCellValue(wb, sheet, row, row.getCell(colIndex));
	        					}
	    					} else {
	    						if(colIndex>=cellLength) {
	        						dataObject.put(fieldNames[fieldNamesIdx], "");
	        						tableData.add("");
	        					} else {
	        						Cell cell = row.getCell(colIndex);
	        						dataObject.put(fieldNames[fieldNamesIdx], getCellValue(wb,sheet,row,cell));
	        						tableData.add(getCellValue(wb,sheet,row,cell));
	        					}
	    					}
	    				}
	    				if(rowIndex!=beginLineNum||!isFieldLine) {
	    					dataList.add(dataObject);
		    				tableDataList.add(tableData);
	    				}
	    			}
	    			if(rowIndex%1000==0) {
						//当组装1000条记录时批量更新入库并且写入表格
	    				saveDate(dataList, tableDataList, fieldNames);
	    				dataList = new ArrayList<DBObject>();		
						tableDataList = new ArrayList<List>();
						
					}
				} catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    continue; // 出现异常继续
                }
	    		System.out.println();
        	}
        	saveDate(dataList, tableDataList, fieldNames);
        	pagePane.setCurrentPage(1);
        	pagePane.setPageCount(rowNum-beginLineNum-1);
        	pagePane.setTotalPage(1);
		} catch (AuditException ex) {
			throw ex;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 保存数据
	 * @param dataList
	 * @param tableDataList
	 * @param fieldNames
	 * @throws AuditException
	 */
	public void saveDate(List<DBObject> dataList,List<List> tableDataList,String[] fieldNames) throws AuditException {
		//更新到哪一张表中？
		//判断是否是追加数据，如果不是则新建选项卡
		JTabbedPane jTabbedPane = distPane.getjTabbedPane();
		int tabCount = jTabbedPane.getTabCount();
		JTable distJtable = null;
		if(tabCount==0||createTab.isSelected()) {
			distJtable = createTab(fieldNames,distSheetName);
			createTab.setSelected(false);
		} else {
			distSheetName = jTabbedPane.getTitleAt(jTabbedPane.getSelectedIndex());
			JPanel jPanle = (JPanel)jTabbedPane.getSelectedComponent();
			distJtable = (JTable)((JScrollPane)jPanle.getComponent(1)).getViewport().getView();
			//如果不是追加数据，则判断导入的字段与表格的表头是否一致：比较长度和内容
			//如果不一致，则提示导入的数据与已存在数据字段不一致，请确认是否新建选项卡
			DefaultTableModel dftm = (DefaultTableModel) distJtable.getModel();
			String[] fieldNames2 = new String[dftm.getColumnCount()-1];
			for(int i=1;i<dftm.getColumnCount();i++) {
				fieldNames2[i-1]=dftm.getColumnName(i);
			}
			Arrays.sort(fieldNames2);
			Arrays.sort(fieldNames);
			if(!Arrays.deepEquals(fieldNames2, fieldNames)) {
				String msg = "导入的数据与已存在数据字段不一致，请确认是否新建选项卡";
				logger.error(msg);
				throw new  AuditException("-1",msg);
			}
		}
		DefaultTableModel dftm = (DefaultTableModel) distJtable.getModel();
		dataDao.addData(dataList, distSheetName);
    	updateTable(dftm, tableDataList.iterator());
    	distJtable.repaint();
	}
	
	/**
	 * 新建选项卡
	 * @param fieldNames
	 * @param distSheetName
	 * @return
	 */
	public JTable createTab(String[] fieldNames,final String distSheetName) {
		
		String[] fieldNames2 = new String[fieldNames.length+1];
		fieldNames2[0] = "序号";
		for(int i=1;i<fieldNames2.length;i++) {
			fieldNames2[i] = fieldNames[i-1];
		}
		
		JPanel jPanel = new JPanel(new GridBagLayout());
		
		final JScrollPane scrollPane = new JScrollPane();
		final JTable jTable = new JTable();
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jTable.setShowGrid(true);
		
		distPane.getjTabbedPane().addTab(distSheetName, jPanel);
		distPane.getjTabbedPane().setSelectedIndex(distPane.getDistTabIdx());
		distPane.setDistTabIdx(distPane.getDistTabIdx()+1);
		//distPane.getjTabbedPane()
		DefaultTableModel dftm = (DefaultTableModel) jTable.getModel();
		dftm.setColumnIdentifiers(fieldNames2);
		scrollPane.setViewportView(jTable);
		
		pagePane = new PagePane(jTable){
			@Override
			public List<List> getPageData() {
				List<List> dataList = dataDao
						.selectList(new QueryCondition(),distSheetName);
				return dataList;
			}
			
			@Override
			public void initTable() {
				// 显示分页数据
				List<List> importRuleList = dataDao
						.selectList(new QueryCondition(),distSheetName);
				updateTable((DefaultTableModel)jTable.getModel(), importRuleList.iterator());
				setTotalRowCount((int) dataDao.count(new QueryCondition(),distSheetName));
				super.initTable();
			}
			
			@Override
			public void refreshTable() {
				setVisible(false);
				updateTable((DefaultTableModel)jTable.getModel(), getPageData().iterator());
				setVisible(true);
			}
		};
		setupComponet(jPanel, pagePane, 0, 0, 0, 0, 1, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
		setupComponet(jPanel, scrollPane, 0, 1, 0, 0, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.WEST);
		
		return jTable;
	}
	
	/**
	 * 2003Excel获取单元格的值
	 * @param wb
	 * @param sheet
	 * @param row
	 * @param cell
	 * @return
	 */
	public String getCellValue(HSSFWorkbook wb,HSSFSheet sheet,Row row,Cell cell) {
		String cellValue = ",";
		if(cell!=null) {
		//cell.getCellType是获得cell里面保存的值的type   
        //如Cell.CELL_TYPE_STRINGl
		 switch(cell.getCellType()) {   
         case Cell.CELL_TYPE_BOOLEAN:   
             //得到Boolean对象的方法   
         	System.out.print(cell.getBooleanCellValue());
         	cellValue = StringUtil.getString(cell.getBooleanCellValue());
             break;   
         case Cell.CELL_TYPE_NUMERIC:   
             //先看是否是日期格式   
             if(DateUtil.isCellDateFormatted(cell)){   
                 //读取日期格式   
             	System.out.print(cell.getDateCellValue());
             	cellValue = StringUtil.getString(cell.getDateCellValue());
             }else{   
                 //读取数字   
             	cell.setCellType(Cell.CELL_TYPE_STRING);
             	System.out.print(cell.getStringCellValue());
             	cellValue = StringUtil.getString(cell.getStringCellValue());
             }   
             break;   
         case Cell.CELL_TYPE_FORMULA:   //当单元为公式的时候
         	//获取公式评估工具
         	FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();  
         	//获取公式在计算时，所引用的行列，并声明。
         	CellReference cellReference = new CellReference("Y"+row.getRowNum());  
         	Cell c = row.getCell(cellReference.getCol());   
     		//获得公式计算后的结果
     		CellValue cellValue2 = evaluator.evaluate(c); 
     		
     		System.out.print(cellValue2.getNumberValue());   
     		cellValue = StringUtil.getString(cellValue2.getNumberValue());
             break;   
         case Cell.CELL_TYPE_STRING:   
             //读取String   
         	System.out.print(cell.getRichStringCellValue().toString());
         	cellValue = StringUtil.getString(cell.getRichStringCellValue().toString());
             break;                     
		 }
		 System.out.print(",");
		}
		return cellValue;
	}
	
	/**
	 * 2007Excel获取单元格的值 
	 * @param wb
	 * @param sheet
	 * @param row
	 * @param cell
	 * @return
	 */
	public String getCellValue(XSSFWorkbook wb,XSSFSheet sheet,Row row,Cell cell) {
		String cellValue = "";
		if(cell!=null) {
		 switch(cell.getCellType()) {   
         case Cell.CELL_TYPE_BOOLEAN:   
             //得到Boolean对象的方法   
         	System.out.print(cell.getBooleanCellValue());
         	cellValue = StringUtil.getString(cell.getBooleanCellValue());
             break;   
         case Cell.CELL_TYPE_NUMERIC:   
             //先看是否是日期格式   
             if(DateUtil.isCellDateFormatted(cell)){   
                 //读取日期格式   
             	System.out.print(cell.getDateCellValue());
             	cellValue = StringUtil.getString(cell.getDateCellValue());
             }else{   
                 //读取数字   
             	cell.setCellType(Cell.CELL_TYPE_STRING);
             	System.out.print(cell.getStringCellValue());
             	cellValue = StringUtil.getString(cell.getStringCellValue());
             }   
             break;   
         case Cell.CELL_TYPE_FORMULA:   //当单元为公式的时候
         	//获取公式评估工具
         	FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();  
         	//获取公式在计算时，所引用的行列，并声明。
         	CellReference cellReference = new CellReference("Y"+row.getRowNum());  
         	Cell c = row.getCell(cellReference.getCol());   
     		//获得公式计算后的结果
     		CellValue cellValue2 = evaluator.evaluate(c); 
     		
     		System.out.print(cellValue2.getNumberValue());   
     		cellValue = StringUtil.getString(cellValue2.getNumberValue());
             break;   
         case Cell.CELL_TYPE_STRING:   
             //读取String   
         	System.out.print(cell.getRichStringCellValue().toString());
         	cellValue = StringUtil.getString(cell.getRichStringCellValue().toString());
             break;                     
		 }
		 System.out.print(",");
		}
		return cellValue;
	}
	
	/**
	 * 根据导入规则导入数据
	 * @param importRule
	 * @param dataFiles
	 */
	public void importCsv(Map<String,String> importRule,File dataFile) {
        
	}
	
	/**
	 * 根据导入规则导入数据
	 * @param importRule
	 * @param dataFiles
	 * @throws Exception 
	 */
	public void importLog(Map<String,String> importRule,File dataFile) throws Exception,AuditException {
		BufferedReader reader = null;
		
		try {
			String regex =  "(.*) - (.*) - \\[(.*)\\] (.*) - (.*)";
			//importRule.put("字段表达式", StringUtil.getString(fieldExpre.getText()));
			//importRule.put("字段名称", StringUtil.getString(fieldName.getText()));
			String fieldExpre = importRule.get("字段表达式");
			String fieldNameValue = importRule.get("字段名称");
			if("".equals(fieldNameValue)) {
				throw new AuditException("0","字段名称不能为空");
			}
			String[] fieldNames = null;
			fieldNames = fieldNameValue.split(" ");
			logger.debug("字段名称====》"+fieldNames);
			fieldExpre = RegexUtil.replaceSpeChar(fieldExpre);
			logger.debug("字段表达式====>"+fieldExpre);
			for(int i=0;i<fieldNames.length;i++) {
				fieldExpre = fieldExpre.replaceFirst(fieldNames[i], "(.*)");
			}
			regex=fieldExpre;
			
			logger.debug("字段表达式====>"+regex);
			
			
			String encodig = importRule.get("编码");
        	if("".equals(encodig)) {
        		encodig = "UTF-8";
        	}
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile),encodig));
			
			String beginCol = importRule.get("开始列");
        	String endCol = importRule.get("结束列");
        	String beginLine = importRule.get("开始行");
        	String endLine = importRule.get("结束行");
        	
        	Integer beginColNum = NumberUtil.getInteger(beginCol);
        	Integer endColNum = NumberUtil.getInteger(endCol);
        	Integer beginLineNum = NumberUtil.getInteger(beginLine);
        	Integer endLineNum = NumberUtil.getInteger(endLine);
        	
        	if(beginLineNum==null) {
        		beginLineNum=1;
        	}
        	
        	if(beginColNum!=null) {
        		beginColNum = beginColNum-1;
        	} else {
				beginColNum = 0;
			}
        	
        	//开始列、结束列  每行的单元格数量不满足列的数量，则填空
        	//结束列可不填，结束列-开始列，视为首行的字段或者指定的字段数量
        	//如果结束列-开始列<字段数量，则以结束列为主
        	//如果结束列-开始列>字段数量，则以字段为主
        	//开始行是否为字段
        	boolean isFieldLine = false;
			if("是".equals(importRule.get("开始行是否为字段"))) {
				isFieldLine = true;
			}
			
			if(!isFieldLine) {
				//开始行不为字段
				int cellLen = fieldNames.length;
				if(endColNum==null||endColNum-beginColNum>cellLen) {
					endColNum = cellLen+beginColNum;
				}
			}
			List<DBObject> dataList = new ArrayList<DBObject>();
			List<List> tableDataList = new ArrayList<List>();
			
			
			String tempString = null;
			int line = 1;
            //一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				if(endLineNum!=null&&line>endLineNum) {
					//大于结束行
					break;
				} else if(line<beginLineNum) {
					//小于开始行
					continue;
				} else if(line==beginLineNum&&isFieldLine) {
					if(RegexUtil.isMatches(regex, tempString)){
	    				String[] strs=RegexUtil.getGroupValues(regex,tempString);
	    				int cellLen = strs.length;
	    				logger.debug("cellLen===>"+cellLen);
	    				if(cellLen==0) {
	    					logger.error("开始行信息为空");
	    					return;
	    				} else {
	    					if(endColNum==null||endColNum>cellLen) {
	    						endColNum = cellLen;
	    					}
	    					fieldNames = new String[cellLen];
	    				}
					}
				} 
				DBObject user = new BasicDBObject();
    			if(RegexUtil.isMatches(regex, tempString)){
    				String[] strs=RegexUtil.getGroupValues(regex,tempString);
    				int cellLength = strs.length;
    				//开始每行单元格迭代
    				int colIndex = beginColNum;
    	        	DBObject dataObject = new BasicDBObject();
    	        	List tableData = new ArrayList();
    				for(;colIndex<endColNum;colIndex++) {
    					int fieldNamesIdx = colIndex-beginColNum;
    					//首行为字段
    					if(isFieldLine&&line==beginLineNum) {
    						if(colIndex>=cellLength) {
    							//结束列超出第一列的行数，则补全临时字段名称
    							fieldNames[fieldNamesIdx]="Field"+fieldNamesIdx;
        					} else {
        						fieldNames[fieldNamesIdx]=strs[colIndex];
        					}
    					} else {
    						if(colIndex>=cellLength) {
        						dataObject.put(fieldNames[fieldNamesIdx], "");
        						tableData.add("");
        					} else {
        						dataObject.put(fieldNames[fieldNamesIdx], strs[colIndex]);
        						tableData.add(strs[colIndex]);
        					}
    					}
    				}
    				if(line!=beginLineNum||!isFieldLine) {
    					dataList.add(dataObject);
	    				tableDataList.add(tableData);
    				}
    			}
    			if(line%100000==0) {
					//当组装1000条记录时批量更新入库并且写入表格
    				logger.debug("saveDate start ......................");
    				saveDate(dataList, tableDataList, fieldNames);
    				logger.debug("saveDate end ......................");
    				logger.debug("line====》"+line);
    				dataList = new ArrayList<DBObject>();
					tableDataList = new ArrayList<List>();
					System.gc();
				}
    			line++;
			}
			saveDate(dataList, tableDataList, fieldNames);
		} catch (AuditException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据导入规则导入数据
	 * @param importRule
	 * @param dataFiles
	 */
	public void importTxt(Map<String,String> importRule,File dataFile) {
       
	}
	
	/**
	 * 根据导入规则导入数据
	 * @param importRule
	 * @param dataFiles
	 */
	public void importXml(Map<String,String> importRule,File dataFile) {

	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ImportData.logger = logger;
	}

	public AuditFrame getAuditFrame() {
		return auditFrame;
	}

	public void setAuditFrame(AuditFrame auditFrame) {
		this.auditFrame = auditFrame;
	}

	public JTextField getFilePath() {
		return filePath;
	}

	public void setFilePath(JTextField filePath) {
		this.filePath = filePath;
	}

	public JButton getFileChooserBtn() {
		return fileChooserBtn;
	}

	public void setFileChooserBtn(JButton fileChooserBtn) {
		this.fileChooserBtn = fileChooserBtn;
	}

	public JButton getDirChooserBth() {
		return dirChooserBth;
	}

	public void setDirChooserBth(JButton dirChooserBth) {
		this.dirChooserBth = dirChooserBth;
	}

	public JButton getPolicyLoadBth() {
		return policyLoadBth;
	}

	public void setPolicyLoadBth(JButton policyLoadBth) {
		this.policyLoadBth = policyLoadBth;
	}

	public JButton getPolicyAddBth() {
		return policyAddBth;
	}

	public void setPolicyAddBth(JButton policyAddBth) {
		this.policyAddBth = policyAddBth;
	}

	public JTextField getSheetName() {
		return sheetName;
	}

	public void setSheetName(JTextField sheetName) {
		this.sheetName = sheetName;
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

	public JButton getImportBtn() {
		return importBtn;
	}

	public void setImportBtn(JButton importBtn) {
		this.importBtn = importBtn;
	}

	public QueryPane getDistPane() {
		return distPane;
	}

	public void setDistPane(QueryPane distPane) {
		this.distPane = distPane;
	}

	public DataDao getDataDao() {
		return dataDao;
	}

	public void setDataDao(DataDao dataDao) {
		this.dataDao = dataDao;
	}
}
