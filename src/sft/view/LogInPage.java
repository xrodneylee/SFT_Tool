package sft.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import sft.model.ConnectDB;
import sft.model.XMLConstructor;

public class LogInPage implements ActionListener{
	protected JFrame sftFrame;
	protected JTextField ip,user;
	protected JPasswordField password;
	protected JLabel ipLabel,userLabel,passwordLabel;
	protected JButton connect;
	protected JTabbedPane sftTab;
	protected JPanel connectSetTab;
	protected CommonDBTab commonDBTab;
	Border eborder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	Border dataBaseSet;
	ConnectDB conn = new ConnectDB();
	XMLConstructor xml = new XMLConstructor();
	Map dataMap = new HashMap();
	Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int hight,width;
	public LogInPage(){
		hight = (int)screenSize.getHeight();
		width = (int)screenSize.getWidth();
		
		sftFrame = new JFrame();
		sftFrame.setExtendedState(Frame.MAXIMIZED_BOTH); 
		sftFrame.setTitle("SFT問題處理工具  (Version:1.0  author:Guan Pu Lee)");
		sftFrame.setBounds(100, 100, 950, 580);
		sftFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sftFrame.getContentPane().setLayout(null);

		sftTab = new JTabbedPane(JTabbedPane.TOP);
		sftTab.setBounds(0, 0, width, (int)(hight*0.9));
		sftFrame.getContentPane().add(sftTab);
		
		dataBaseSet = BorderFactory.createTitledBorder(eborder,"資料庫設定");

		connectSetTab = new JPanel();
		connectSetTab.setBorder(dataBaseSet);
		connectSetTab.setLayout(null);
		sftTab.addTab("連線設定", connectSetTab);
		
		ipLabel = new JLabel("資料庫伺服器位址");
		ipLabel.setBounds((int)(width*0.45)-110, 50, 110, 21);
		connectSetTab.add(ipLabel);
		ip = new JTextField();
		ip.setBounds((int)(width*0.45), 50, 120, 21);
		ip.setColumns(10);
		connectSetTab.add(ip);
		
		userLabel = new JLabel("資料庫登入帳號");
		userLabel.setBounds((int)(width*0.45)-110, 80, 110, 21);
		connectSetTab.add(userLabel);
		user = new JTextField();
		user.setBounds((int)(width*0.45), 80, 120, 21);
		user.setColumns(10);
		connectSetTab.add(user);
		
		passwordLabel = new JLabel("資料庫登入密碼");
		passwordLabel.setBounds((int)(width*0.45)-110, 110, 110, 21);
		connectSetTab.add(passwordLabel);
		password = new JPasswordField();
		password.setBounds((int)(width*0.45), 110, 120, 21);
		password.setColumns(10);
		connectSetTab.add(password);
		
		connect = new JButton("DB連線");
		connect.setBounds((int)(width*0.42), 150, 87, 23);
		connect.setActionCommand("connect");
		connect.addActionListener(this);
		connectSetTab.add(connect);
		
		if(new File("CONNECT.xml").exists()){
			dataMap = xml.XMLParser("CONNECT.xml");
			ip.setText(dataMap.get("IP").toString());
			user.setText(dataMap.get("USER").toString());
			password.setText(dataMap.get("PASSWORD").toString());
		}
			
	}	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		String message = "";
		System.out.println(cmd);
		xml.createXmlDocument();
		if(cmd.equals("connect")){
			conn.setURL(ip.getText());
			conn.setUSER(user.getText());
			conn.setPASSWORD(password.getPassword());
			message = conn.Connect();
			xml.addElement("IP", null, ip.getText());
			xml.addElement("USER", null, user.getText());
			xml.addElement("PASSWORD", null, ConnectDB.convertString(password.getPassword()));
			xml.saveToFile(new File("CONNECT.xml"));
		}
		
		if(message != ""){
			JOptionPane worning = new JOptionPane();
			worning.showMessageDialog(null, message, "警告", JOptionPane.ERROR_MESSAGE);
		}else{
			/**
			 * 產生[資料庫設定]
			 **/
			commonDBTab = new CommonDBTab(sftFrame,sftTab,conn);
			sftTab.addTab("資料庫設定", commonDBTab);
			sftTab.setSelectedIndex(1);
			
		}
		
	}
	
}
