package com.samaylabs.optimus.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Tulve Shabab Kasim
 *
 */
public class AgvUtilDao {
	private DbConnection db;

	public AgvUtilDao(){
		db = new DbConnection();
	}

	/**
	 * Updates utilization details of agv
	 * @param id of agv
	 * @param state of agv 
	 * @param value no of serconds to be added
	 */
	public void updateUtilizationLog(int id,String state,long value){
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String query = "update utilization set "+state+"="+state+"+"+value+" where dated=?";
		try {
			
			checkDate(id);
			ps = connection.prepareStatement(query);
			ps.setString(1, ft.format(dNow));
			
			ps.executeUpdate();
		} catch (SQLException e) {
			return;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				return;
			}
		}
	}
	
	public void checkDate(int id) {
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String query = "INSERT INTO utilization (id, dated) SELECT * FROM (SELECT ?, ?) AS tmp WHERE NOT EXISTS (SELECT dated FROM utilization WHERE dated = ?') LIMIT 1;";
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ps.setString(2, ft.format(dNow));
			ps.setString(3, ft.format(dNow));
			
			ps.executeUpdate();
		} catch (SQLException e) {
			return;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				return;
			}
		}
	}
	
	
}
