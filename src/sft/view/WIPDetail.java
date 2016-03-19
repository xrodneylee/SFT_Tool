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
import sft.model.printExcel;

public class WIPDetail extends JPanel implements ActionListener{
	ConnectDB conn;
	JButton excel,modify,sqlGrammar;
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
	public WIPDetail(ConnectDB conn,String type,String con){
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
		
		String[] title = null;
		if(type.equals("A")){
			title = new String[]{"製令編號", "數量", "品號","規格","專案代號","單位","狀態",
					  			 "加工順序","製程代號","製程名稱","生產線代號","生產線名稱","完工碼",
					             "進站數量","出站數量","重工數量","報廢數量","短少數量","多餘數量","進站撥轉數量","出站撥轉數量","生產數量"};
			countsql = " SELECT count(*) "+
					   " FROM MODETAIL MO "+
					   " left join SFT_OP_REALRUN SOR ON MO.CMOID=SOR.ID AND SOR.SEQUENCE=0 "+
					   " left join OPERATION OP on SOR.ERP_OPID=OP.ERP_OPID and SOR.ERP_WSID=OP.ERP_WSID "+
					   " left join WORKSTATION WS on SOR.ERP_WSID=WS.ID "+
					   " left join LOT on SOR.ID=LOT.MOID AND SOR.ERP_OPSEQ=LOT.ERP_OPSEQ AND SOR.ERP_OPID=LOT.ERP_OPID AND SOR.ERP_WSID=LOT.ERP_WSID "+
					   " WHERE SOR.TA032='N' "+
					   " and (SOR.OUTQTY+SOR.DEFECTQTY+SOR.UNKNOWQTY-SOR.SURPLUSQTY-SOR.REWORKQTY>=SOR.ARRIVEQTY+ISNULL(LOT.LOTSIZE,0)+OR038) "+
				   	   " AND (SOR.ARRIVEQTY<>0 OR (SOR.ARRIVEQTY<>0 AND SOR.OUTQTY<>0)) "+con;
			sql = " SELECT MO.CMOID,MO.QTY,MO.ITEMID,MO.MO022,MO.MO023,MO.UNIT,MO.STATUS,SOR.ERP_OPSEQ,SOR.ERP_OPID,OP.NAME,SOR.ERP_WSID,WS.NAME,SOR.TA032 "+
				  " ,SOR.ARRIVEQTY,SOR.OUTQTY,SOR.REWORKQTY,SOR.DEFECTQTY,SOR.UNKNOWQTY,SOR.SURPLUSQTY,OR038,OR039,ISNULL(LOTSIZE,0) AS LOTSIZE "+
				  " FROM MODETAIL MO "+
				  " left join SFT_OP_REALRUN SOR ON MO.CMOID=SOR.ID AND SOR.SEQUENCE=0 "+
				  " left join OPERATION OP on SOR.ERP_OPID=OP.ERP_OPID and SOR.ERP_WSID=OP.ERP_WSID "+
				  " left join WORKSTATION WS on SOR.ERP_WSID=WS.ID "+
				  " left join LOT on SOR.ID=LOT.MOID AND SOR.ERP_OPSEQ=LOT.ERP_OPSEQ AND SOR.ERP_OPID=LOT.ERP_OPID AND SOR.ERP_WSID=LOT.ERP_WSID "+
				  " WHERE SOR.TA032='N' "+
				  " and (SOR.OUTQTY+SOR.DEFECTQTY+SOR.UNKNOWQTY-SOR.SURPLUSQTY-SOR.REWORKQTY>=SOR.ARRIVEQTY+ISNULL(LOT.LOTSIZE,0)+OR038) "+
				  " AND (SOR.ARRIVEQTY<>0 OR (SOR.ARRIVEQTY<>0 AND SOR.OUTQTY<>0)) "+con;
			updateSql = " UPDATE SFT_OP_REALRUN SET TA032='Y' \n"+
					" FROM MODETAIL MO \n"+
					" left join SFT_OP_REALRUN SOR ON MO.CMOID=SOR.ID AND SOR.SEQUENCE=0 \n"+
					" left join OPERATION OP on SOR.ERP_OPID=OP.ERP_OPID and SOR.ERP_WSID=OP.ERP_WSID \n"+
					" left join WORKSTATION WS on SOR.ERP_WSID=WS.ID \n"+
					" left join LOT on SOR.ID=LOT.MOID AND SOR.ERP_OPSEQ=LOT.ERP_OPSEQ AND SOR.ERP_OPID=LOT.ERP_OPID AND SOR.ERP_WSID=LOT.ERP_WSID \n"+
					" WHERE SOR.TA032='N' and (SOR.OUTQTY+SOR.DEFECTQTY+SOR.UNKNOWQTY-SOR.SURPLUSQTY-SOR.REWORKQTY>=SOR.ARRIVEQTY+ISNULL(LOT.LOTSIZE,0)+OR038) \n"+
					" AND (SOR.ARRIVEQTY<>0 OR SOR.OUTQTY<>0) \n"+con;
		}else if(type.equals("B")){
			title = new String[]{"製令編號", "數量", "品號","規格","專案代號","單位","狀態",
		  			 "加工順序","製程代號","製程名稱","生產線代號","生產線名稱","完工碼",
		             "進站數量","出站數量","重工數量","報廢數量","短少數量","多餘數量","撥轉數量","報廢入庫數","線上報廢數","在製數量(待進)","在製數量(待出)"};
			countsql = "; with a as ( "+
					   " SELECT MO.CMOID,MO.QTY,MO.ITEMID,MO.MO022,MO.MO023,MO.UNIT,(CASE WHEN MO.STATUS='99' then '已完工' else '' end) as STATUS, "+
					   " SOR.ERP_OPSEQ,SOR.ERP_OPID,OP.NAME as OPNAME,SOR.ERP_WSID,WS.NAME as WSNAME,SOR.TA032,SOR.ARRIVEQTY,SOR.OUTQTY,SOR.REWORKQTY,SOR.DEFECTQTY,SOR.UNKNOWQTY,SOR.SURPLUSQTY,SOR.TA015,OR018,OR019,OR020,OR021 "+
					   " FROM MODETAIL MO "+
					   " left join SFT_OP_REALRUN SOR ON MO.CMOID=SOR.ID AND SOR.SEQUENCE=0 "+
					   " left join OPERATION OP on SOR.ERP_OPID=OP.ERP_OPID and SOR.ERP_WSID=OP.ERP_WSID "+
					   " left join WORKSTATION WS on SOR.ERP_WSID=WS.ID "+
					   " WHERE MO.STATUS='99' AND  SOR.TA032='N' "+con+
					   " union  "+
					   " SELECT MO.CMOID,MO.QTY,MO.ITEMID,MO.MO022,MO.MO023,MO.UNIT,(CASE WHEN MO.STATUS='99' then '已完工' else '' end) as STATUS, "+
					   " SOR.ERP_OPSEQ,SOR.ERP_OPID,OP.NAME as OPNAME,SOR.ERP_WSID,WS.NAME as WSNAME,SOR.TA032,SOR.ARRIVEQTY,SOR.OUTQTY,SOR.REWORKQTY,SOR.DEFECTQTY,SOR.UNKNOWQTY,SOR.SURPLUSQTY,SOR.TA015,OR018,OR019,OR020,OR021 "+
					   " FROM MODETAIL MO "+
					   " left join SFT_OP_REALRUN SOR ON MO.CMOID=SOR.ID AND SOR.SEQUENCE=0 "+
					   " left join OPERATION OP on SOR.ERP_OPID=OP.ERP_OPID and SOR.ERP_WSID=OP.ERP_WSID "+
					   " left join WORKSTATION WS on SOR.ERP_WSID=WS.ID "+
					   " left join "+conn.getERP()+"..SFCTA on MO.MO004=TA001 AND MO.MO005=TA002 AND SOR.ERP_OPSEQ=TA003 AND SOR.ERP_OPID=TA004 AND SOR.ERP_WSID=TA006  "+
					   " WHERE MO.STATUS='99' AND  SFCTA.TA032='N' AND MO.ORIGINAL_CMOID IS  NULL "+con+
					   " ) "+
					   " select count(*) from a ";
			sql = " SELECT MO.CMOID,MO.QTY,MO.ITEMID,MO.MO022,MO.MO023,MO.UNIT,(CASE WHEN MO.STATUS='99' then '已完工' else '' end), "+
					   " SOR.ERP_OPSEQ,SOR.ERP_OPID,OP.NAME,SOR.ERP_WSID,WS.NAME,SOR.TA032,SOR.ARRIVEQTY,SOR.OUTQTY,SOR.REWORKQTY,SOR.DEFECTQTY,SOR.UNKNOWQTY,SOR.SURPLUSQTY,SOR.TA015,OR018,OR019,OR020,OR021 "+
					   " FROM MODETAIL MO "+
					   " left join SFT_OP_REALRUN SOR ON MO.CMOID=SOR.ID AND SOR.SEQUENCE=0 "+
					   " left join OPERATION OP on SOR.ERP_OPID=OP.ERP_OPID and SOR.ERP_WSID=OP.ERP_WSID "+
					   " left join WORKSTATION WS on SOR.ERP_WSID=WS.ID "+
					   " WHERE MO.STATUS='99' AND  SOR.TA032='N' "+con+
					   " union  "+
					   " SELECT MO.CMOID,MO.QTY,MO.ITEMID,MO.MO022,MO.MO023,MO.UNIT,(CASE WHEN MO.STATUS='99' then '已完工' else '' end), "+
					   " SOR.ERP_OPSEQ,SOR.ERP_OPID,OP.NAME,SOR.ERP_WSID,WS.NAME,SOR.TA032,SOR.ARRIVEQTY,SOR.OUTQTY,SOR.REWORKQTY,SOR.DEFECTQTY,SOR.UNKNOWQTY,SOR.SURPLUSQTY,SOR.TA015,OR018,OR019,OR020,OR021 "+
					   " FROM MODETAIL MO "+
					   " left join SFT_OP_REALRUN SOR ON MO.CMOID=SOR.ID AND SOR.SEQUENCE=0 "+
					   " left join OPERATION OP on SOR.ERP_OPID=OP.ERP_OPID and SOR.ERP_WSID=OP.ERP_WSID "+
					   " left join WORKSTATION WS on SOR.ERP_WSID=WS.ID "+
					   " left join "+conn.getERP()+"..SFCTA on MO.MO004=TA001 AND MO.MO005=TA002 AND SOR.ERP_OPSEQ=TA003 AND SOR.ERP_OPID=TA004 AND SOR.ERP_WSID=TA006  "+
					   " WHERE MO.STATUS='99' AND  SFCTA.TA032='N' AND MO.ORIGINAL_CMOID IS  NULL "+con;
			updateSql = " UPDATE SFT_OP_REALRUN SET TA032='Y' \n"+
					" FROM MODETAIL MO \n"+
					" left join SFT_OP_REALRUN SOR ON MO.CMOID=SOR.ID AND SOR.SEQUENCE=0 \n"+
					" left join OPERATION OP on SOR.ERP_OPID=OP.ERP_OPID and SOR.ERP_WSID=OP.ERP_WSID \n"+
					" left join WORKSTATION WS on SOR.ERP_WSID=WS.ID \n"+
					" left join LOT on SOR.ID=LOT.MOID AND SOR.ERP_OPSEQ=LOT.ERP_OPSEQ AND SOR.ERP_OPID=LOT.ERP_OPID AND SOR.ERP_WSID=LOT.ERP_WSID \n"+
					" WHERE SOR.TA032='N' and (SOR.OUTQTY+SOR.DEFECTQTY+SOR.UNKNOWQTY-SOR.SURPLUSQTY-SOR.REWORKQTY>=SOR.ARRIVEQTY+ISNULL(LOT.LOTSIZE,0)+OR038) \n"+
					" AND (SOR.ARRIVEQTY<>0 OR SOR.OUTQTY<>0) \n"+con;
		}else if(type.equals("C")){
			title = new String[]{"製令編號", "狀態", "製令單別", "製令單號","完成數量","品號","品名","規格","數量","加工順序","製程代號","製程名稱","生產線代號","生產線名稱","SFT完工碼","ERP完工碼"};
			countsql = " SELECT count(*) "+
					  " from MODETAIL MO"+
					  " inner join SFT_OP_REALRUN SOR on CMOID=ID AND SOR.SEQUENCE=0 "+
					  " inner join "+conn.getERP()+"..SFCTA on MO004=TA001 and MO005=TA002 AND SOR.ERP_OPSEQ=TA003 AND SOR.ERP_OPID=TA004 AND SOR.ERP_WSID=TA006 "+
					  " left join OPERATION on SOR.ERP_OPID=OPERATION.ERP_OPID AND SOR.ERP_WSID=OPERATION.ERP_WSID "+
					  " left join WORKSTATION on  SOR.ERP_WSID=WORKSTATION.ID "+
					  " where 1=1 and ORIGINAL_CMOID is null and HASLOTSPLIT=0 and SOR.TA032 IN('Y','y') AND MO.STATUS IN('99','100') and SFCTA.TA032='N' "+con;
			sql = " select MO.CMOID,MO.STATUS,TA001,TA002,TA011,MO.ITEMID,MO.MO021,MO.MO022,MO.QTY,SOR.ERP_OPSEQ,SOR.ERP_OPID,OPERATION.NAME,SOR.ERP_WSID,WORKSTATION.NAME,SOR.TA032,SFCTA.TA032 "+
				  " from MODETAIL MO"+
				  " inner join SFT_OP_REALRUN SOR on CMOID=ID AND SOR.SEQUENCE=0 "+
				  " inner join "+conn.getERP()+"..SFCTA on MO004=TA001 and MO005=TA002 AND SOR.ERP_OPSEQ=TA003 AND SOR.ERP_OPID=TA004 AND SOR.ERP_WSID=TA006 "+
				  " left join OPERATION on SOR.ERP_OPID=OPERATION.ERP_OPID AND SOR.ERP_WSID=OPERATION.ERP_WSID "+
				  " left join WORKSTATION on  SOR.ERP_WSID=WORKSTATION.ID "+
				  " where 1=1 and ORIGINAL_CMOID is null and HASLOTSPLIT=0 and SOR.TA032 IN('Y','y') AND MO.STATUS IN('99','100') and SFCTA.TA032='N' "+con;	
			updateSql = " UPDATE SFT_OP_REALRUN \n"+
					" SET SFCTA.TA032=SOR.TA032 \n"+
					" FROM MODETAIL MO \n"+
					" inner join SFT_OP_REALRUN SOR on MO.CMOID=SOR.ID AND SOR.SEQUENCE=0 \n"+
					" inner join "+conn.getERP()+"..SFCTA on MO.MO004=TA001 and MO.MO005=TA002 AND SOR.ERP_OPSEQ=TA003 AND SOR.ERP_OPID=TA004 AND SOR.ERP_WSID=TA006 \n"+
					" left join OPERATION on SOR.ERP_OPID=OPERATION.ERP_OPID AND SOR.ERP_WSID=OPERATION.ERP_WSID  \n"+
					" left join WORKSTATION on  SOR.ERP_WSID=WORKSTATION.ID  \n"+
					" WHERE 1=1 \n"+
					" and MO.ORIGINAL_CMOID is null and MO.HASLOTSPLIT=0 and SOR.TA032 IN('Y','y') AND MO.STATUS IN('99','100') \n"+
					" and SFCTA.TA032='N' \n"+con;
			modify.setEnabled(false);
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
			data.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
				print.exportJTable(data, "製程狀態明細.xls");
				print.export("製程狀態明細.xls");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(cmd.equals("modify")){
			try{
				System.out.println(updateSql);
				conn.executeSQL(updateSql);
			}catch(SQLException e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(cmd.equals("sqlGrammar")){
			SqlWindow sqlWindow = new SqlWindow(updateSql);
			sqlWindow.setVisible(true);
		}
	}
}
