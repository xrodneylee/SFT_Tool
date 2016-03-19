package sft.view;

import java.awt.Color;
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

public class MachineStatus extends JPanel implements ActionListener{
	ConnectDB conn;
	JTextField MachineStart,MachineEnd;
	JLabel MachineStartLabel,MachineEndLabel;
	JButton Verify;
	Border Edge = BorderFactory.createEtchedBorder();
	Border MachineStatusEdge;
	JTabbedPane jtb;
	JRadioButton realtime,batch;
	ButtonGroup group;
	JOptionPane worning = new JOptionPane();
	
	public MachineStatus(JTabbedPane jtb,ConnectDB conn){
		this.conn = conn;
		this.jtb =jtb;
		setLayout(null);
		//setBounds(30, 30, 650, 140);
		
		MachineStatusEdge = BorderFactory.createTitledBorder(Edge, "機台狀態檢查");
		MachineStart = new JTextField();
		MachineEnd = new JTextField();
		MachineStartLabel = new JLabel("機台代號 起：");
		MachineEndLabel = new JLabel("機台代號 迄：");
		Verify = new JButton("驗證");
		Verify.setActionCommand("Verify");
		Verify.addActionListener(this);
		setBorder(MachineStatusEdge);
		MachineStart.setBounds(150, 35, 110, 21);
		MachineEnd.setBounds(150, 70, 110, 21);
		MachineStartLabel.setBounds(70, 35, 110, 21);
		MachineEndLabel.setBounds(70, 70, 110, 21);
		Verify.setBounds(250, 120, 87, 23);
		add(MachineStart);
		add(MachineEnd);
		add(MachineStartLabel);
		add(MachineEndLabel);
		add(Verify);
		
		realtime = new JRadioButton("即時");
		realtime.setBounds(70, 105, 110, 21);
		realtime.setActionCommand("realtime");
		realtime.setSelected(true);
		add(realtime);
		
		batch = new JRadioButton("批次");
		batch.setBounds(70, 125, 110, 21);
		batch.setActionCommand("batch");
		add(batch);
		
		group = new ButtonGroup();
		group.add(realtime);
		group.add(batch);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		String condition ="";
		if(!MachineStart.getText().equals("") && MachineEnd.getText().equals("")){
			worning.showMessageDialog(null, "機台代號 迄 不可為空", "警告", JOptionPane.INFORMATION_MESSAGE);
			return;
		}else if(MachineStart.getText().equals("") && !MachineEnd.getText().equals("")){
			worning.showMessageDialog(null, "機台代號 起 不可為空", "警告", JOptionPane.INFORMATION_MESSAGE);
			return;
		}else if(!MachineStart.getText().equals("") && !MachineEnd.getText().equals("")){
			condition = " AND EQ.ID BETWEEN '"+MachineStart.getText()+"' AND '"+MachineEnd.getText()+"'";
		}
		if(cmd.equals("Verify")){
			MachineDetail md = new MachineDetail(conn,group.getSelection().getActionCommand(),condition);
			jtb.addTab("機台狀態明細", md);
			jtb.setSelectedComponent(md);
		}
	}
}
