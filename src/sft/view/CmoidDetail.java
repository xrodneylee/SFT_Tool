package sft.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumn;

import sft.model.ConnectDB;
import sft.model.Log;
import sft.model.TableView;
import sft.model.printExcel;

public class CmoidDetail extends JPanel implements ActionListener{
	ConnectDB conn;
	JButton excel,modify,sqlGrammar;
	ResultSet rs,rows;
	JTable data;
	String[][] record;
	TableView tableview;
	int rowCount = 0;
	JLabel recordsCount;
	JOptionPane worning = new JOptionPane();
	Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int hight,width;
	public CmoidDetail(ConnectDB conn,String con){
		hight = (int)screenSize.getHeight();
		width = (int)screenSize.getWidth();
		
		this.conn = conn;
		setLayout(null);
		String countsql = " ",sql = " ";
		
		excel = new JButton("匯出EXCEL");
		excel.setActionCommand("excel");
		excel.addActionListener(this);
		excel.setBounds(10, 10, 120, 23);
		add(excel);
		
		modify = new JButton("修正");
		modify.setBounds((int)(width*0.35), (int)(hight*0.8), 120, 23);
		add(modify);
		
		sqlGrammar = new JButton("顯示更新語法");
		sqlGrammar.setActionCommand("sqlGrammar");
		sqlGrammar.addActionListener(this);
		sqlGrammar.setBounds((int)(width*0.25), (int)(hight*0.8), 120, 23);
		add(sqlGrammar);
		
		String[] title = new String[]{"製令編號", "狀態", "單別", "單號","狀態碼","品號","產品品名","產品規格","數量"};
		countsql = "; with a as ( "+
				   " SELECT CMOID,"+
				   " (case when STATUS='99' then '99.已完工' when STATUS='100' then '100.指定完工' end ) as STATUS,"+
				   " TA001,TA002,"+
				   " (case when TA011='1' then '1.未生產' when TA011='2' then '2.已發料' when TA011='3' then '3.生產中' end ) AS TA011,"+
				   " ITEMID,MO021,MO022,QTY "+
				   " from MODETAIL  "+
				   " inner join "+conn.getERP()+"..MOCTA on TA001=MO004 and TA002=MO005 "+
				   " where STATUS in ('99','100') and TA011 not in('Y','y') and ORIGINAL_CMOID is null and HASLOTSPLIT=0 "+con+
				   " union all "+
				   " SELECT CMOID,"+
				   " (case when STATUS='99' then '99.已完工' when STATUS='100' then '100.指定完工' end ) as STATUS,"+
				   " TA001,TA002,"+
				   " (case when TA011='1' then '1.未生產' when TA011='2' then '2.已發料' when TA011='3' then '3.生產中' end ) AS TA011,"+
				   " ITEMID,MO021,MO022,QTY "+
				   " from MODETAIL  "+
				   " inner join "+conn.getERP()+"..MOCTA on TA001=MO004 and TA002=MO005 "+
				   " where STATUS not in ('99','100') and TA011 in('Y','y') and ORIGINAL_CMOID is null and HASLOTSPLIT=0 "+con+
				   " ) "+
				   " select count(*) from a ";

		sql = " SELECT CMOID,"+
			  " (case when STATUS='99' then '99.已完工' when STATUS='100' then '100.指定完工' end ) as STATUS,"+
			  " TA001,TA002,"+
			  " (case when TA011='1' then '1.未生產' when TA011='2' then '2.已發料' when TA011='3' then '3.生產中' end ) AS TA011,"+
			  " ITEMID,MO021,MO022,QTY "+
			  " from MODETAIL  "+
			  " inner join "+conn.getERP()+"..MOCTA on TA001=MO004 and TA002=MO005 "+
			  " where STATUS in ('99','100') and TA011 not in('Y','y') and ORIGINAL_CMOID is null and HASLOTSPLIT=0 "+con+
			  " union all "+
			  " SELECT  CMOID,"+
			  " (case when STATUS='99' then '99.已完工' when STATUS='100' then '100.指定完工' end ) as STATUS,"+
			  " TA001,TA002,"+
			  " (case when TA011='1' then '1.未生產' when TA011='2' then '2.已發料' when TA011='3' then '3.生產中' end ) AS TA011,"+
			  " ITEMID,MO021,MO022,QTY "+
			  " from MODETAIL  "+
			  " inner join "+conn.getERP()+"..MOCTA on TA001=MO004 and TA002=MO005 "+
			  " where STATUS not in ('99','100') and TA011 in('Y','y') and ORIGINAL_CMOID is null and HASLOTSPLIT=0 "+con;

		//取得所有records筆數
		//rowCount = conn.getCount(countsql);
		try {
			rows = conn.Query(countsql);
			while(rows.next()){
				rowCount = rows.getInt(1);
			}
			rs = conn.Query(sql);
			record = new String[rowCount][title.length];
			int i = 0;
			while(rs.next()){
				for (int j = 1; j <= title.length; j++) {
					record[i][j - 1] = rs.getString(j);
				}
				i++;
			}
			
			recordsCount = new JLabel("共"+rowCount+"筆");
			recordsCount.setBounds(10, (int)(hight*0.8), 120, 23);
			add(recordsCount);
			
			data = new JTable(record,title);
			//data.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			TableColumn col = data.getColumnModel().getColumn(0);
			col.setPreferredWidth(150);
			//data.setEnabled(false);//不可編輯
			data.setPreferredScrollableViewportSize(new Dimension(400,90));
			data.setFillsViewportHeight(true);
			//data.setRowHeight(50);
			JScrollPane scrollPane = new JScrollPane(data);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setBounds(10, 40, (int)(width*0.8), (int)(hight*0.7));
			add(scrollPane);
			//取得所有records
			//record = conn.getRecord(title, rowCount, sql);
			//將record傳入JTable，並回傳JScrollPane
			//tableview = new TableView(record,title); 
			
			//add(tableview.getView());
			modify.setEnabled(false);
			
		} catch (SQLException e) {
			worning.showMessageDialog(null, "無資料須修正", null, JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		String updateSql = "";
		if(cmd.equals("excel")){
			System.out.println("excel");
			printExcel print = new printExcel();
			try {
				print.exportJTable(data, "製令狀態明細.xls");
				print.export("製令狀態明細.xls");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else if(cmd.equals("modify")){
			
		}else if(cmd.equals("sqlGrammar")){
			SqlWindow sqlWindow = new SqlWindow(updateSql);
			sqlWindow.setVisible(true);
		}
	}
}
