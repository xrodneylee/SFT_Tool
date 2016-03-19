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

public class TransOrderSingleDetail extends JPanel implements ActionListener{
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
	public TransOrderSingleDetail(ConnectDB conn,String con){
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
		
		String[] title = new String[]{"類型", "SFT移轉單別", "SFT移轉單號", "ERP移轉單別","ERP移轉單號","ERP移轉單單別","ERP移轉單單號","SFT移轉單別","SFT移轉單號",
									  "移轉單單據類型","製令編號","製令單別","製令單號","移出類別","移出部門","移出部門名稱","移出工序","移出製程","製程名稱","移入部門","移入部門名稱","移入工序","移入製程","製程名稱"};
		countsql = "; with a as ( "+
				" SELECT 'SFT' AS A,TRANSORDERTYPE,STL.TRANSNO,TL011,TL012,TB001,TB002,TB038,TB039,TL018,STL.KEYID,MOTYPE,MONO,OUTTYPE,ST.OUTDEPID,"+
				   " (CASE WHEN OUTTYPE='1' then D.MD002  WHEN OUTTYPE='2' then A.MA002  WHEN OUTTYPE='3' then C.MC002 END ) AS OUTDEPNAME, "+
				   " OUTOPSEQ,OUTOP,W.MW002,INDEPID,"+
				   " (CASE WHEN INTYPE='1' then D2.MD002  WHEN INTYPE='2' then A2.MA002  WHEN INTYPE='3' then C2.MC002 END ) AS INDEPNAME, "+
				   " INOPSEQ,INOP"+//,W2.MW002 "+
				   " FROM SFT_TRANSORDER_LINE STL  "+
				   " left join "+conn.getERP()+"..SFCTB on STL.TRANSORDERTYPE=TB038 and STL.TRANSNO=TB039"+
				   " left join SFT_TRANSORDER ST on STL.TRANSORDERTYPE=ST.TRANSTYPE and STL.TRANSNO=ST.TRANSNO "+
				   " left join "+conn.getERP()+"..CMSMC C on OUTDEP=MC001 "+
				   " left join "+conn.getERP()+"..CMSMD D on OUTDEP=MD001 "+
				   " left join "+conn.getERP()+"..PURMA A on OUTDEP=MA001 "+
				   " left join "+conn.getERP()+"..CMSMW W on OUTOP=W.MW001 "+
				   " left join "+conn.getERP()+"..CMSMC C2 on OUTDEP=C2.MC001 "+
				   " left join "+conn.getERP()+"..CMSMD D2 on OUTDEP=D2.MD001 "+
				   " left join "+conn.getERP()+"..PURMA A2 on OUTDEP=A2.MA001 "+
				   " left join "+conn.getERP()+"..CMSMW W2 on INOP=W2.MW001 "+
				   " WHERE  TB038 IS NULL AND TL018='1' and TL013='N' "+con+
				   " union  "+
				   " SELECT 'ERP' AS A,TRANSORDERTYPE,TRANSNO,TL011,TL012,TB001,TB002,TB038,TB039,TL018,STL.KEYID,TC004,TC005,TB004,TB005,"+
				   " (CASE WHEN TB004='1' then D.MD002  WHEN TB004='2' then A.MA002  WHEN TB004='3' then C.MC002 END ) AS OUTDEPNAME, "+
				   " TC006,TC007,W.MW002,TB008,"+
				   " (CASE WHEN TB007='1' then D2.MD002  WHEN TB007='2' then A2.MA002  WHEN TB007='3' then C2.MC002 END ) AS INDEPNAME, "+
				   " TC008,TC009"+//,W2.MW002 "+
				   " FROM "+conn.getERP()+"..SFCTB  "+
				   " left join  SFT_TRANSORDER_LINE STL on TB038=TRANSORDERTYPE  and  TB039=TRANSNO "+
				   " left join "+conn.getERP()+"..SFCTC on TB001=TC001 AND TB002=TC002 "+
				   " left join "+conn.getERP()+"..CMSMC C on TB005=MC001 "+
				   " left join "+conn.getERP()+"..CMSMD D on TB005=MD001 "+
				   " left join "+conn.getERP()+"..PURMA A on TB005=MA001 "+
				   " left join "+conn.getERP()+"..CMSMW W on TC007=W.MW001 "+
				   " left join "+conn.getERP()+"..CMSMC C2 on TB008=C2.MC001 "+
				   " left join "+conn.getERP()+"..CMSMD D2 on TB008=D2.MD001 "+
				   " left join "+conn.getERP()+"..PURMA A2 on TB008=A2.MA001 "+
				   " left join "+conn.getERP()+"..CMSMW W2 on TC009=W2.MW001 "+
				   " WHERE  TL011 IS NULL AND TB013<>'V' "+con+
				   " ) "+
				   " select count(*) from a ";
		System.out.println(countsql);
		sql = " SELECT 'SFT' AS A,TRANSORDERTYPE,STL.TRANSNO,TL011,TL012,TB001,TB002,TB038,TB039,TL018,STL.KEYID,MOTYPE,MONO,OUTTYPE,ST.OUTDEPID,"+
				   " (CASE WHEN OUTTYPE='1' then D.MD002  WHEN OUTTYPE='2' then A.MA002  WHEN OUTTYPE='3' then C.MC002 END ) AS OUTDEPNAME, "+
				   " OUTOPSEQ,OUTOP,W.MW002,INDEPID,"+
				   " (CASE WHEN INTYPE='1' then D2.MD002  WHEN INTYPE='2' then A2.MA002  WHEN INTYPE='3' then C2.MC002 END ) AS INDEPNAME, "+
				   " INOPSEQ,INOP,W2.MW002 "+
				   " FROM SFT_TRANSORDER_LINE STL  "+
				   " left join "+conn.getERP()+"..SFCTB on STL.TRANSORDERTYPE=TB038 and STL.TRANSNO=TB039"+
				   " left join SFT_TRANSORDER ST on STL.TRANSORDERTYPE=ST.TRANSTYPE and STL.TRANSNO=ST.TRANSNO "+
				   " left join "+conn.getERP()+"..CMSMC C on OUTDEP=MC001 "+
				   " left join "+conn.getERP()+"..CMSMD D on OUTDEP=MD001 "+
				   " left join "+conn.getERP()+"..PURMA A on OUTDEP=MA001 "+
				   " left join "+conn.getERP()+"..CMSMW W on OUTOP=W.MW001 "+
				   " left join "+conn.getERP()+"..CMSMC C2 on OUTDEP=C2.MC001 "+
				   " left join "+conn.getERP()+"..CMSMD D2 on OUTDEP=D2.MD001 "+
				   " left join "+conn.getERP()+"..PURMA A2 on OUTDEP=A2.MA001 "+
				   " left join "+conn.getERP()+"..CMSMW W2 on INOP=W2.MW001 "+
				   " WHERE  TB038 IS NULL AND TL018='1' and TL013='N' "+con+
				   " union  "+
				   " SELECT 'ERP' AS A,TRANSORDERTYPE,TRANSNO,TL011,TL012,TB001,TB002,TB038,TB039,TL018,STL.KEYID,TC004,TC005,TB004,TB005,"+
				   " (CASE WHEN TB004='1' then D.MD002  WHEN TB004='2' then A.MA002  WHEN TB004='3' then C.MC002 END ) AS OUTDEPNAME, "+
				   " TC006,TC007,W.MW002,TB008,"+
				   " (CASE WHEN TB007='1' then D2.MD002  WHEN TB007='2' then A2.MA002  WHEN TB007='3' then C2.MC002 END ) AS INDEPNAME, "+
				   " TC008,TC009,W2.MW002 "+
				   " FROM "+conn.getERP()+"..SFCTB  "+
				   " left join  SFT_TRANSORDER_LINE STL on TB038=TRANSORDERTYPE  and  TB039=TRANSNO "+
				   " left join "+conn.getERP()+"..SFCTC on TB001=TC001 AND TB002=TC002 "+
				   " left join "+conn.getERP()+"..CMSMC C on TB005=MC001 "+
				   " left join "+conn.getERP()+"..CMSMD D on TB005=MD001 "+
				   " left join "+conn.getERP()+"..PURMA A on TB005=MA001 "+
				   " left join "+conn.getERP()+"..CMSMW W on TC007=W.MW001 "+
				   " left join "+conn.getERP()+"..CMSMC C2 on TB008=C2.MC001 "+
				   " left join "+conn.getERP()+"..CMSMD D2 on TB008=D2.MD001 "+
				   " left join "+conn.getERP()+"..PURMA A2 on TB008=A2.MA001 "+
				   " left join "+conn.getERP()+"..CMSMW W2 on TC009=W2.MW001 "+
				   " WHERE  TL011 IS NULL AND TB013<>'V' "+con;
		System.out.println(sql);
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
			//data.setEnabled(false);//不可編輯
			TableColumn col = data.getColumnModel().getColumn(10);
			col.setPreferredWidth(150);
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
				print.exportJTable(data, "移轉單個數比對明細.xls");
				print.export("移轉單個數比對明細.xls");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
