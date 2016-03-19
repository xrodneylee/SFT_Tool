package sft.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SqlWindow extends JFrame implements WindowListener{
	private JTextArea grammar;
	private JScrollPane jscrollpanel;
	public SqlWindow(String sql){
		
		this.setTitle("顯示更新語法");
		this.setBounds(100, 100, 600, 300);
		this.setLocationRelativeTo(null);
		
		grammar = new JTextArea();
		grammar.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		grammar.setBackground(Color.BLACK);
		grammar.setForeground(Color.WHITE);
		jscrollpanel = new JScrollPane(grammar);
		jscrollpanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jscrollpanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.getContentPane().add(jscrollpanel);
		
		grammar.append(sql);
	}
	
	
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		this.dispose();
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
