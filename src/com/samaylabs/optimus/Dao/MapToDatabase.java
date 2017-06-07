package com.samaylabs.optimus.Dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.Sheet;
import jxl.Workbook;

/**
 * 
 * @author Shabab
 *
 */
public class MapToDatabase implements DbConstants{

	private static String path = "C:\\Users\\admin\\Desktop\\map.xls";
	private static Workbook wb = new ExcelCon().getExcel(path);
	private static DbConnection db = new DbConnection();
	private static Connection connection = db.getConncetion();
	private static Statement statement = db.getStatement();

	public static void insertNodeFromXl() {				
		Sheet sh = wb.getSheet("node");
		int totalNoOfRows = sh.getRows();
		int totalNoOfCols = sh.getColumns();
		int count = 1;

		for (int row = 1; row < totalNoOfRows; row++) {
			PreparedStatement ps = null;
			try {
				ps = connection.prepareStatement(insertnode);
				for (int col = 0; col < totalNoOfCols; col++) {
					ps.setString(count++,sh.getCell(col, row).getContents().toLowerCase());
					System.out.print(sh.getCell(col, row).getContents().toLowerCase() + "\t");
				}
				count = 1;
				ps.executeUpdate();
				System.out.println();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				try {
					if(ps!=null)
						ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}System.out.println("\n\n");

	}

	public static void insertEdgeFromXl() {				//DONE

		Sheet sh = wb.getSheet("edge");
		int totalNoOfRows = sh.getRows();
		int totalNoOfCols = sh.getColumns();
		int count = 1;

		for (int row = 1; row < totalNoOfRows; row++) {
			PreparedStatement ps = null;
			try {
				ps = connection.prepareStatement(insertedge);
				for (int col = 0; col < totalNoOfCols; col++) {
					ps.setString(count++,sh.getCell(col, row).getContents().toLowerCase());
					System.out.print(sh.getCell(col, row).getContents().toLowerCase() + "\t");
				}
				count = 1;
				ps.executeUpdate();
				System.out.println();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				try {
					if(ps!=null)
						ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}System.out.println("\n\n");

	}

	public static void insertNodeResolverFromXl() {				
		Sheet sh = wb.getSheet("noderesolver");
		int rows = sh.getRows();
		int cols = sh.getColumns();
		int count = 1;

		for (int row = 1; row < rows; row++) {
			PreparedStatement ps = null;
			try {
				ps = connection.prepareStatement(insertnoderesolver);
				for (int col = 0; col < cols; col++) {
					ps.setString(count++,sh.getCell(col, row).getContents().toLowerCase());
					System.out.print(sh.getCell(col, row).getContents().toLowerCase() + "\t");
				}
				count = 1;
				ps.executeUpdate();
				System.out.println();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				try {
					if(ps!=null)
						ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}System.out.println("\n\n");

	}

	public static void init(){
		try {
			statement.execute("delete from edge");
			//			statement.execute("delete from noderesolver");
			//			statement.execute("delete from node");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void backupDb(){                        //Partial

		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy_MM_dd_hh_mm");

		String mysqldump = "C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump"; 
		String duplicate1 = mysqldump + " -u developer --password=developer optimus -r D:\\sql\\optimus_" + ft.format(dNow) +".sql"; 
		String duplicate2 =	"mysql -u developer --password=developer optimus_" + ft.format(dNow) + " -e source D:\\sql\\optimus_" + ft.format(dNow) +".sql";
		String create = "mysql -u developer --password=developer -e " + " \"create database optimus_" + ft.format(dNow) + "\"";
		String test = "mysqldump -u developer --password=developer optimus > | mysql -u developer --password=developer -h localhost optimus_" + ft.format(dNow);

		String commands[]  = {create,test,duplicate1,duplicate2};

		BufferedReader stdInput = null;
		BufferedReader stdError = null;

		int i =0;
		for(int j =0 ; j<2 ; j++){
			try {
				Process p = Runtime.getRuntime().exec(commands[j]);
				int processComplete = p.waitFor();

				if (processComplete == 0) {
					System.out.println("Backup Successfull");
				} else { 
					System.out.println("Backup Failure!");
				} 

				stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

				// read the output from the command
				System.out.println("Here is the standard output of the command:\n");
				while ((commands[j] = stdInput.readLine()) != null) {
					System.out.println(commands[j]);
				}

				// read any errors from the attempted command
				System.out.println("Here is the standard error of the command (if any):\n");
				while ((commands[j] = stdError.readLine()) != null) {
					System.out.println(commands[j]);
				}
				System.out.println("Command done : " + i++);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally{
				try {
					stdInput.close();
					stdError.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {

		//		init();
		//		insertNodeFromXl();
		insertEdgeFromXl();
		//		insertNodeResolverFromXl();

	}
}
