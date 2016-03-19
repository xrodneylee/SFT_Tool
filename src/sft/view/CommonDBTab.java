package sft.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import sft.model.ConnectDB;
import sft.model.XMLConstructor;

public class CommonDBTab extends JPanel implements ActionListener{
	protected JTextField SFTSYS,SFT,ERP;
	protected JLabel SFTSYSLabel,SFTLabel,ERPLabel;
	protected JButton save;
	protected ConnectDB common;
	protected JTabbedPane sftTab;
	protected JFrame frame;
	XMLConstructor xml = new XMLConstructor();
	Map dataMap = new HashMap();
	JOptionPane worning = new JOptionPane();
	Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int hight,width;
	public CommonDBTab(JFrame frame,JTabbedPane sftTab,ConnectDB common){
		hight = (int)screenSize.getHeight();
		width = (int)screenSize.getWidth();
		
		setLayout(null);
		this.sftTab = sftTab;
		this.common = common;
		this.frame = frame;
		
		SFTSYSLabel = new JLabel("SFT共用資料庫");
		SFTSYSLabel.setBounds((int)(width*0.45)-110, 50, 110, 21);
		add(SFTSYSLabel);
		SFTSYS = new JTextField();
		SFTSYS.setBounds((int)(width*0.45), 50, 120, 21);
		add(SFTSYS);
		
		SFTLabel = new JLabel("SFT資料庫");
		SFTLabel.setBounds((int)(width*0.45)-110, 80, 110, 21);
		add(SFTLabel);
		SFT = new JTextField();
		SFT.setBounds((int)(width*0.45), 80, 120, 21);
		add(SFT);
		
		ERPLabel = new JLabel("ERP資料庫");
		ERPLabel.setBounds((int)(width*0.45)-110, 110, 110, 21);
		add(ERPLabel);
		ERP = new JTextField();
		ERP.setBounds((int)(width*0.45), 110, 120, 21);
		add(ERP);
		
		save = new JButton("儲存");
		save.setBounds((int)(width*0.42), 150, 87, 23);
		save.setActionCommand("save");
		save.addActionListener(this);
		add(save);
		
		if(new File("DATABASE.xml").exists()){
			dataMap = xml.XMLParser("DATABASE.xml");
			SFTSYS.setText(dataMap.get("SFTSYS").toString());
			SFT.setText(dataMap.get("SFT").toString());
			ERP.setText(dataMap.get("ERP").toString());
		}
	}
	
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		
		xml.createXmlDocument();
		common.setSFTSYS(SFTSYS.getText());
		common.setSFT(SFT.getText());
		common.setERP(ERP.getText());
		xml.addElement("SFTSYS", null, SFTSYS.getText());
		xml.addElement("SFT", null, SFT.getText());
		xml.addElement("ERP", null, ERP.getText());
		xml.saveToFile(new File("DATABASE.xml"));
		
		if(cmd.equals("save")){
			if(!common.exist(SFTSYS.getText())){
				worning.showMessageDialog(null, SFTSYS.getText()+"不存在", null, JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if(!common.exist(SFT.getText())){
				worning.showMessageDialog(null, SFT.getText()+"不存在", null, JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			if(!common.exist(ERP.getText())){
				worning.showMessageDialog(null, ERP.getText()+"不存在", null, JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			//刪除各tab
			frame.getContentPane().removeAll();
			frame.getContentPane().repaint();

			//設置主畫面
			MainPage mainpage = new MainPage(common);
			frame.add(mainpage);
			frame.setVisible(true);
		}
		
	}
}
