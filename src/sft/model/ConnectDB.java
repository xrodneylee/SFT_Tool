package sft.model;
import java.sql.*;

public class ConnectDB {
	private String URL;
	private String USER;
	private char[] PASSWORD;
	private String SFTSYS;
	private String SFT;
	private String ERP;
	private Connection conn=null;
	private Statement stmt;
	
	public void setURL(String URL){
		this.URL = URL;
	}
	public void setUSER(String USER){
		this.USER = USER;
	}
	public void setPASSWORD(char[] PASSWORD){
		this.PASSWORD = PASSWORD;
	}
	public void setSFTSYS(String SFTSYS){
		this.SFTSYS=SFTSYS;
	}
	public void setSFT(String SFT){
		this.SFT=SFT;
	}
	public void setERP(String ERP){
		this.ERP=ERP;
	}
	public String getURL(){
		return URL;
	}
	public String getUSER(){
		return USER;
	}
	public char[] getPASSWORD(){
		return PASSWORD;
	}
	public String getSFTSYS(){
		return SFTSYS;
	}
	public String getSFT(){
		return SFT;
	}
	public String getERP(){
		return ERP;
	}
	
	public static String convertString(char[] password){
		String passwordString = "";
		int num = password.length;
		for(int i=0 ; i<num ; i++){
			passwordString += password[i];
		}
		return passwordString;
	}
	
	public String Connect(){
		String message = "";
		try {
			conn=DriverManager.getConnection("jdbc:sqlserver://"+getURL(),getUSER(),convertString(getPASSWORD()));
			System.out.printf("已%s資料庫連線\n",conn.isClosed()?"關閉":"開啟");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			message = "輸入有誤";
			e.printStackTrace();
		}
		return message;
	}
	public ResultSet Query(String sql) throws SQLException{
		stmt=conn.createStatement();
		return stmt.executeQuery(" USE "+SFT+sql);
	}
	public int getCount(String sql){
		ResultSet rs,rows;
		int rowCount = 0;
		try {
			rows = Query(sql);
			while(rows.next()){
				rowCount = rows.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rowCount;
	}
	public String[][] getRecord(String[] title,int rowCount,String sql){
		ResultSet rs,rows;
		String[][] record = null;
				
		try {
			rs = Query(sql);
			record = new String[rowCount][title.length];
			int i = 0;
			while(rs.next()){
				for (int j = 1; j <= title.length; j++) {
					record[i][j - 1] = rs.getString(j);
				}
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return record;
	}
	
	public void executeSQL(String sql) throws SQLException{
		stmt=conn.createStatement();
		stmt.executeUpdate(sql);
	}
	
	public boolean exist(String sft){
		boolean flag = true;
		String sql = " select * from sys.sysdatabases where name='"+sft+"'";
		try {
			stmt=conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(!rs.next()){
				flag = false;
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return flag;
	}
}
