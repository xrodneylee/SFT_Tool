package sft.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import sft.model.ConnectDB;

public class TransOrderAmount extends JPanel implements ActionListener{
	ConnectDB conn;
	JTextField CMOIDStart,CMOIDEnd;
	JLabel CMOIDStartLabel,CMOIDEndLabel;
	JButton Verify;
	Border Edge = BorderFactory.createEtchedBorder();
	Border CMOIDStatusEdge;
	JTabbedPane jtb;
	JOptionPane worning = new JOptionPane();
	JRadioButton WF,SM;
	ButtonGroup group;
	
	public TransOrderAmount(JTabbedPane jtb,ConnectDB conn){
		this.conn = conn;
		this.jtb =jtb;
		setLayout(null);
		//setBounds(30, 30, 650, 140);
		
		CMOIDStatusEdge = BorderFactory.createTitledBorder(Edge, "製令編號");
		CMOIDStart = new JTextField();
		CMOIDEnd = new JTextField();
		CMOIDStartLabel = new JLabel("製令編號 起：");
		CMOIDEndLabel = new JLabel("製令編號 迄：");
		Verify = new JButton("驗證");
		Verify.setActionCommand("Verify");
		Verify.addActionListener(this);
		setBorder(CMOIDStatusEdge);
		CMOIDStart.setBounds(150, 35, 110, 21);
		CMOIDEnd.setBounds(150, 70, 110, 21);
		CMOIDStartLabel.setBounds(70, 35, 110, 21);
		CMOIDEndLabel.setBounds(70, 70, 110, 21);
		Verify.setBounds(250, 120, 87, 23);
		add(CMOIDStart);
		add(CMOIDEnd);
		add(CMOIDStartLabel);
		add(CMOIDEndLabel);
		add(Verify);
		
		WF = new JRadioButton("WF");
		WF.setBounds(70, 105, 110, 21);
		WF.setActionCommand("WF");
		WF.setSelected(true);
		add(WF);
		
		SM = new JRadioButton("SM");
		SM.setBounds(70, 125, 110, 21);
		SM.setActionCommand("SM");
		add(SM);
		
		group = new ButtonGroup();
		group.add(WF);
		group.add(SM);
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
			condition = " AND KEYID BETWEEN '"+CMOIDStart.getText()+"' AND '"+CMOIDEnd.getText()+"'";
		}
		if(cmd.equals("Verify")){
			TransOrderAmountDetail toad = new TransOrderAmountDetail(conn,group.getSelection().getActionCommand(),condition);
			jtb.addTab("移轉數量比對明細", toad);
			jtb.setSelectedComponent(toad);
		}
	}
}
