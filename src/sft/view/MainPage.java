package sft.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.xnap.commons.gui.CloseableTabbedPane;

import sft.model.ConnectDB;

public class MainPage extends JPanel implements TreeSelectionListener{
	JTree tree;
	CloseableTabbedPane funSetPane;
	JLabel title;
	ConnectDB conn;
	JButton close;
	Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int hight,width;
	public MainPage(ConnectDB common){
		hight = (int)screenSize.getHeight();
		width = (int)screenSize.getWidth();
		
		this.conn = common;
		setLayout(null);
		setBounds(10, 10, width, hight);
		
		//------------tree內容設置------------
		DefaultMutableTreeNode toplevel = new DefaultMutableTreeNode("功能列");
		DefaultMutableTreeNode[] secondlevel = new DefaultMutableTreeNode[3];
		DefaultMutableTreeNode[][] thirdlevel = new DefaultMutableTreeNode[3][3];
		String[] secondItem = {"狀態重計","數量重計","工時重計"};	
		String[][] thirdItem = {
								{"機台狀態檢核","製程狀態檢核","製令狀態檢核"},//狀態重計
								{"比對移轉單據筆數","比對移轉單據數量"},//數量重計
								{"製程工時重計"}//工時重計
							   };
		for(int i=0;i<secondItem.length;i++){
			secondlevel[i] = new DefaultMutableTreeNode(secondItem[i]);
			toplevel.add(secondlevel[i]);
			for(int j=0;j<thirdItem[i].length;j++){
				thirdlevel[i][j] = new DefaultMutableTreeNode(thirdItem[i][j]);
				secondlevel[i].add(thirdlevel[i][j]);
			}
		}
		//------------------------------------
		
		//------------功能列-------------------
		title = new JLabel("功能列");
		title.setBounds(0, 0, 200, 20);
		title.setOpaque(true);
		title.setBackground(Color.PINK);
		add(title);
		//------------------------------------
		
		//------------將tree加入畫面------------
		tree = new JTree(toplevel);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);  
		tree.setLayout(null);
		tree.addTreeSelectionListener(this);
		JScrollPane jscrollpane = new JScrollPane(tree);
		jscrollpane.setWheelScrollingEnabled(true);
		jscrollpane.setBounds(0, 20, 200, (int)(hight*0.88));
		add(jscrollpane);
		
		MouseListener ml = new MouseAdapter() {
		     public void mousePressed(MouseEvent e) {
		    	 doMouseClick(e);
		     }
		 };
		 tree.addMouseListener(ml);
		//------------------------------------
		
		//------------將JTabbedPane加入畫面------------
		funSetPane = new CloseableTabbedPane();//JTabbedPane.TOP
		funSetPane.setBounds(205, 0, (int)(width*0.84), (int)(hight*0.9));
		add(funSetPane);
		//------------------------------------------
		
	}

	@Override
	public void valueChanged(TreeSelectionEvent e1) {
	
	}
	
	
	private void doMouseClick(MouseEvent e){
		int selRow = tree.getRowForLocation(e.getX(), e.getY());
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if(node == null)
			return;
        System.out.println(selRow);
        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
        if(selRow != -1) {
            if(e.getClickCount() == 2) {
           	//葉節點產生各個tab
    		String task = selPath.getLastPathComponent().toString();
    		if(node.isLeaf()){
    			if(task.equals("機台狀態檢核")){
    				MachineStatus ms = new MachineStatus(funSetPane,conn);
    				funSetPane.addTab("機台狀態",ms);
    				funSetPane.setSelectedComponent(ms);
    			}else if(task.equals("製程狀態檢核")){
    				WIPStatus ws = new WIPStatus(funSetPane,conn);
    				funSetPane.addTab("製程狀態",ws);
    				funSetPane.setSelectedComponent(ws);
    			}else if(task.equals("製令狀態檢核")){
    				CmoidStatus cs = new CmoidStatus(funSetPane,conn);
    				funSetPane.addTab("製令狀態",cs);
    				funSetPane.setSelectedComponent(cs);
    			}else if(task.equals("比對移轉單據筆數")){
    				TransOrderSingle tos = new TransOrderSingle(funSetPane,conn);
    				funSetPane.addTab("比對移轉單據筆數",tos);
    				funSetPane.setSelectedComponent(tos);
    			}else if(task.equals("比對移轉單據數量")){
    				TransOrderAmount toa = new TransOrderAmount(funSetPane,conn);
    				funSetPane.addTab("比對移轉單據數量",toa);
    				funSetPane.setSelectedComponent(toa);
    			}else if(task.equals("製程工時重計")){
    				
    			}
    		}
          }
       }
	}
}
