package sft.model;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumn;

public class TableView extends JTable{
	Object[][] record;
	Object[] title;
	JScrollPane scrollPane;
	public TableView(Object[][] rowData,Object[] columnNames){
		super(rowData,columnNames);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn col = this.getColumnModel().getColumn(0);
		col.setPreferredWidth(150);
		//setEnabled(false);//不可編輯
		setPreferredScrollableViewportSize(new Dimension(400,90));
		setFillsViewportHeight(true);
	}
	
	public JScrollPane getView(){
		scrollPane = new JScrollPane(this);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(10, 40, 675, 300);
		return scrollPane;
	}
}
