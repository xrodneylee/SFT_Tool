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

import sft.model.ConnectDB;
import sft.model.printExcel;

public class TransOrderAmountDetail extends JPanel implements ActionListener{
	ConnectDB conn;
	JButton excel,modify;
	JTable data;
	ResultSet rs,rows;
	int rowCount = 0;
	String[][] record;
	JLabel recordsCount;
	JOptionPane worning = new JOptionPane();
	Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int hight,width;
	public TransOrderAmountDetail(ConnectDB conn,String type,String con){
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
		
//		modify = new JButton("修正");
//		modify.setBounds((int)(width*0.35), (int)(hight*0.8), 120, 23);
//		add(modify);
		
		String[] title = new String[]{"SFT移轉單別", "SFT移轉單號", "ERP移轉單別","ERP移轉單號","移轉單身序號","SFT移轉數量","ERP移轉數量",
									  "SFT移轉包裝數量","ERP移轉包裝數量","SFT報廢數量","ERP報廢數量","SFT驗收數量","ERP驗收數量"};
		if(type.equals("WF")){
			countsql = " SELECT count(*) "+
					   " FROM SFT_TRANSORDER_LINE  INNER JOIN "+conn.getERP()+"..SFCTC  "+
					   " ON TRANSORDERTYPE=TC001 AND TRANSNO=TC002 AND SN=TC003 "+
					   " WHERE TRANSQTY<>TC036 OR TL007<>TC042 "+con;
			sql = " SELECT TRANSORDERTYPE,TRANSNO,TC001,TC002,TC003,TRANSQTY,TC036,TL007,TC042,SCRAPQTY,TC016,TL002,TC014 "+
				  " FROM SFT_TRANSORDER_LINE  INNER JOIN "+conn.getERP()+"..SFCTC  "+
				  " ON TRANSORDERTYPE=TC001 AND TRANSNO=TC002 AND SN=TC003 "+
				  " WHERE TRANSQTY<>TC036 OR TL007<>TC042 "+con;		   
		}else if(type.equals("SM")){
			//title[8] = "產品品號";
			countsql = " SELECT count(*) "+
					   " FROM SFT_TRANSORDER_LINE  INNER JOIN "+conn.getERP()+"..SFCTC  "+
					   " ON TRANSORDERTYPE=TC001 AND TRANSNO=TC002 AND SN=TC003 "+
					   " WHERE TRANSQTY<>TC041 OR TL007<>TC047 "+con;
			sql = " SELECT TRANSORDERTYPE,TRANSNO,TC001,TC002,TC003,TRANSQTY,TC041,TL007,TC047,SCRAPQTY,TC016,TL002,TC014 "+
				  " FROM SFT_TRANSORDER_LINE  INNER JOIN "+conn.getERP()+"..SFCTC  "+
				  " ON TRANSORDERTYPE=TC001 AND TRANSNO=TC002 AND SN=TC003 "+
				  " WHERE TRANSQTY<>TC041 OR TL007<>TC047 "+con;
		}else{
			
		}
		
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
			//data.setEnabled(false);//不可編輯
			data.setPreferredScrollableViewportSize(new Dimension(400,90));
			data.setFillsViewportHeight(true);
			//data.setRowHeight(50);
			JScrollPane scrollPane = new JScrollPane(data);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setBounds(10, 40, (int)(width*0.8), (int)(hight*0.7));
			add(scrollPane);
		} catch (SQLException e) {
			worning.showMessageDialog(null, "無資料須修正", null, JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}
		
	}
	
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		if(cmd.equals("excel")){
			System.out.println("excel");
			printExcel print = new printExcel();
			try {
				print.exportJTable(data, "移轉數量比對明細.xls");
				print.export("移轉數量比對明細.xls");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
