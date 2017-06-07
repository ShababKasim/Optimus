package com.samaylabs.optimus.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EdgeTurnsDao {
	
private DbConnection db;
	
	public EdgeTurnsDao(){
		db = new DbConnection();
	}
	
	public void updateFlag(Long source, Long destination, boolean flag){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String UpdateAgv = "update edge set cflag=? where source=? and destination=?";
		try {
			ps = connection.prepareStatement(UpdateAgv);
			ps.setBoolean(1, flag);
			ps.setLong(2, source);
			ps.setLong(3, destination);
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
