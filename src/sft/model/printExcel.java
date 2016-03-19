package sft.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class printExcel {
	String dir = System.getProperty("user.dir");
	public static void exportJTable (JTable table, String fileName)  throws Exception {
	    StringBuilder content = new StringBuilder("");
	    File file = new File(fileName);
	    PrintWriter writer = new PrintWriter(file,"Big5");
	    for (int i=0;i<table.getModel().getColumnCount();i++){
	    	content.append(table.getModel().getColumnName(i)+"\t");
	    }
	    content.append("\n");
	    for (int i=0; i<table.getModel().getRowCount(); i++){
	        for (int j=0; j<table.getModel().getColumnCount(); j++) {
	        	content.append((table.getModel().getValueAt(i,j)==null)?"":table.getModel().getValueAt(i,j).toString()+"\t");            
	        }
	        content.append("\n");
	    }
	    writer.println(content.toString());
	    writer.close();
	}
	
	public void export(String fileName){
		try {
			Thread.sleep(1000);
			Process p = Runtime.getRuntime().exec("cmd /c start "+dir+"/"+fileName);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
