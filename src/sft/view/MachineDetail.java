package sft.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import sft.model.ConnectDB;
import sft.model.printExcel;

public class MachineDetail extends JPanel implements ActionListener{
	ConnectDB conn;
	JButton excel,modify,sqlGrammar;;
	JTable data;
	ResultSet rs,rows;
	int rowCount = 0;
	String[][] record;
	String type;
	String con;
	JLabel recordsCount;
	JOptionPane worning = new JOptionPane();
	Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int hight,width;
	String updateSql = "";
	public MachineDetail(ConnectDB conn,String type,String con){
		hight = (int)screenSize.getHeight();
		width = (int)screenSize.getWidth();
		
		this.conn = conn;
		this.type = type;
		this.con = con;
		setLayout(null);
		String countsql = " ",sql = " ";
		
		excel = new JButton("匯出EXCEL");
		excel.setActionCommand("excel");
		excel.addActionListener(this);
		excel.setBounds(10, 10, 120, 23);
		add(excel);
		
		modify = new JButton("修正");
		modify.setActionCommand("modify");
		modify.addActionListener(this);
		modify.setBounds((int)(width*0.35), (int)(hight*0.8), 120, 23);
		add(modify);
		
		sqlGrammar = new JButton("顯示更新語法");
		sqlGrammar.setActionCommand("sqlGrammar");
		sqlGrammar.addActionListener(this);
		sqlGrammar.setBounds((int)(width*0.25), (int)(hight*0.8), 120, 23);	
		add(sqlGrammar);
		
		String[] title = new String[]{"機台代號", "機台名稱", "線別代號", "線別名稱", "機台資料檔狀態", "設備保修狀態"};
		if(type.equals("realtime")){
			countsql = " SELECT count(*) "+
					 " from EQUIPMENT EQ "+
					 " JOIN EQUCAPEXCPAT EQUC on EQ.ID=EQUC.EQUIPMENTID and EQUC.ENDEFFECTDATETIME is null"+
					 " left join  WORKSTATION WS on EQ.LOCATION=WS.ID "+
					 " where 1=1 and EQ.EQ001<>EQUC.STATUS ";
			sql = " SELECT EQ.ID ,EQ.NAME ,WS.ID ,WS.NAME, "+
					 	 " (case when EQ.EQ001='-1' then '加工中' when  EQ.EQ001='0' then '閒置'  when  EQ.EQ001='1' then '故障'  when  EQ.EQ001='2' then '設置' when EQ.EQ001='3' then '下班' when EQ.EQ001='4' then '暫停'  end ),"+
					 	 " (case when EQUC.STATUS='-1' then '加工中' when  EQUC.STATUS='0' then '閒置'  when  EQUC.STATUS='1' then '故障'  when  EQUC.STATUS='2' then '設置' when EQUC.STATUS='3' then '下班' when EQUC.STATUS='4' then '暫停'  end ) "+
						 " from EQUIPMENT EQ "+
						 " JOIN EQUCAPEXCPAT EQUC on EQ.ID=EQUC.EQUIPMENTID and EQUC.ENDEFFECTDATETIME is null"+
						 " left join  WORKSTATION WS on EQ.LOCATION=WS.ID "+
						 " where 1=1 and EQ.EQ001<>EQUC.STATUS ";
			updateSql = " update EQUIPMENT set EQ001=EQUC.STATUS \n"+
					" from EQUIPMENT EQ JOIN EQUCAPEXCPAT EQUC on EQ.ID=EQUC.EQUIPMENTID and EQUC.ENDEFFECTDATETIME is null \n"+
					" left join  WORKSTATION WS on EQ.LOCATION=WS.ID \n"+
					" where 1=1 and EQ.EQ001<>EQUC.STATUS \n"+con;
		}else if(type.equals("batch")){
			countsql = " SELECT count(*) "+
					 " from EQUIPMENT EQ "+
					 " JOIN EQUCAPEXCPAT_BATCH EB on EQ.ID=EB.EQUIPMENTID and EB.ENDEFFECTDATETIME is null"+
					 " left join  WORKSTATION WS on EQ.LOCATION=WS.ID "+
					 " where 1=1 and EQ.EQ001<>EB.STATUS ";
			sql = " SELECT EQ.ID ,EQ.NAME ,WS.ID ,WS.NAME, "+
					 " (case when EQ.EQ001='-1' then '加工中' when  EQ.EQ001='0' then '閒置'  when  EQ.EQ001='1' then '故障'  when  EQ.EQ001='2' then '設置' when EQ.EQ001='3' then '下班' when EQ.EQ001='4' then '暫停'  end ), "+
					 " (case when EB.STATUS='-1' then '加工中' when  EB.STATUS='0' then '閒置'  when  EB.STATUS='5' then '故障'  when  EB.STATUS='6' then '設置' when EB.STATUS='7' then '下班' when EB.STATUS='8' then '暫停'  end ) "+
					 " from EQUIPMENT EQ "+
					 " JOIN EQUCAPEXCPAT_BATCH EB on EQ.ID=EB.EQUIPMENTID and EB.ENDEFFECTDATETIME is null"+
					 " left join  WORKSTATION WS on EQ.LOCATION=WS.ID "+
					 " where 1=1 and EQ.EQ001<>EB.STATUS ";
			updateSql = " update EQUIPMENT set EQ001=EB.STATUS \n"+
					" from EQUIPMENT EQ JOIN EQUCAPEXCPAT_BATCH EB on EQ.ID=EB.EQUIPMENTID and EB.ENDEFFECTDATETIME is null \n"+
					" left join  WORKSTATION WS on EQ.LOCATION=WS.ID \n"+
					" where 1=1 and EQ.EQ001<>EB.STATUS \n"+con;
		}else{
			
		}
		
		try {
			rows = conn.Query(countsql+con);
			while(rows.next()){
				rowCount = rows.getInt(1);
			}
			rs = conn.Query(sql+con);
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
			//data.setEnabled(false);//不可編輯
			data.setPreferredScrollableViewportSize(new Dimension(400,90));
			data.setFillsViewportHeight(true);
			//data.setRowHeight(50);
			JScrollPane scrollPane = new JScrollPane(data);
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
				print.exportJTable(data, "機台狀態明細.xls");
				print.export("機台狀態明細.xls");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(cmd.equals("modify")){
			try {
				conn.executeSQL(updateSql);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(cmd.equals("sqlGrammar")){
			SqlWindow sqlWindow = new SqlWindow(updateSql);
			sqlWindow.setVisible(true);
		}
	}
}
