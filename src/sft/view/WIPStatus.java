package sft.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import sft.model.ConnectDB;

public class WIPStatus extends JPanel implements ActionListener{
	ConnectDB conn;
	JTextField CMOIDStart,CMOIDEnd,tip;
	JLabel CMOIDStartLabel,CMOIDEndLabel,tipLabel;
	JButton Verify,A,B,C;
	Border Edge = BorderFactory.createEtchedBorder();
	Border CMOIDStatusEdge;
	JTabbedPane jtb;
	JOptionPane worning = new JOptionPane();
	JPanel inside;
	String type = "None";
	String title = "";
	
	public WIPStatus(JTabbedPane jtb,ConnectDB conn){
		this.conn = conn;
		this.jtb =jtb;
		setLayout(null);
		//setBounds(30, 30, 650, 140);
		
		inside = new JPanel();
		inside.setLayout(null);
		inside.setBounds(0, 80, 695, 385);
		
		A = new JButton("製程檢核A");
		A.setActionCommand("A");
		A.addActionListener(this);
		A.setBounds(10, 10, 120, 23);
		add(A);
		
		B = new JButton("製程檢核B");
		B.setActionCommand("B");
		B.addActionListener(this);
		B.setBounds(140, 10, 120, 23);
		add(B);
		
		C = new JButton("製程檢核C");
		C.setActionCommand("C");
		C.addActionListener(this);
		C.setBounds(270, 10, 120, 23);
		add(C);
		
		tipLabel = new JLabel("檢核說明");
		tipLabel.setBounds(10, 50, 120, 23);
		add(tipLabel);
		
		tip = new JTextField();
		tip.setBounds(75, 50, 400, 21);
		tip.setEditable(false);
		tip.setForeground(Color.RED);
		add(tip);
		
		CMOIDStatusEdge = BorderFactory.createTitledBorder(Edge, "製令狀態檢查");
		CMOIDStart = new JTextField();
		CMOIDEnd = new JTextField();
		CMOIDStartLabel = new JLabel("製令編號 起：");
		CMOIDEndLabel = new JLabel("製令編號 迄：");
		Verify = new JButton("驗證");
		Verify.setActionCommand("Verify");
		Verify.addActionListener(this);
		inside.setBorder(CMOIDStatusEdge);
		CMOIDStart.setBounds(150, 35, 110, 21);
		CMOIDEnd.setBounds(150, 70, 110, 21);
		CMOIDStartLabel.setBounds(70, 35, 110, 21);
		CMOIDEndLabel.setBounds(70, 70, 110, 21);
		Verify.setBounds(250, 120, 87, 23);
		inside.add(CMOIDStart);
		inside.add(CMOIDEnd);
		inside.add(CMOIDStartLabel);
		inside.add(CMOIDEndLabel);
		inside.add(Verify);
		add(inside);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		String condition ="";
		if(!CMOIDStart.getText().equals("") && CMOIDEnd.getText().equals("")){
			worning.showMessageDialog(null, "製令編號 迄 不可為空", "警告", JOptionPane.INFORMATION_MESSAGE);
			return;
		}else if(CMOIDStart.getText().equals("") && !CMOIDEnd.getText().equals("")){
			worning.showMessageDialog(null, "製令編號 起 不可為空", "警告", JOptionPane.INFORMATION_MESSAGE);
			return;
		}else if(!CMOIDStart.getText().equals("") && !CMOIDEnd.getText().equals("")){
			condition = " AND MO.CMOID BETWEEN '"+CMOIDStart.getText()+"' AND '"+CMOIDEnd.getText()+"'";
		}
		
		if(cmd.equals("Verify")){
			if(type.equals("None")){
				worning.showMessageDialog(null, "請選擇檢核類型", "警告", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			WIPDetail wd = new WIPDetail(conn,type,condition);
			jtb.addTab("製程狀態明細"+title, wd);
			jtb.setSelectedComponent(wd);
		}else if(cmd.equals("A")){
			tip.setText("SFT的數量正確，但是製程完工碼=N");
			type = "A";
			title = "(製程檢核A)";
		}else if(cmd.equals("B")){
			tip.setText("製令狀態=Y.已完工，製程完工碼=N");
			type = "B";
			title = "(製程檢核B)";
		}else if(cmd.equals("C")){
			tip.setText("SFT製程完工碼=Y,y，但是ERP完工碼還是N");
			type = "C";
			title = "(製程檢核C)";
		}
	}
}
