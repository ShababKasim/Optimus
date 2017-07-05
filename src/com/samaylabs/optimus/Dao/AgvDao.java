package com.samaylabs.optimus.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.samaylabs.optimus.WebServices.models.AgvData;

/**
 * 
 * @author Tulve Shabab Kasim
 *
 */
public class AgvDao {

	private DbConnection db;
	
	public AgvDao(){
		db = new DbConnection();
	}
	
	/**
	 * Insert agv information
	 * @param id of agv
	 * @param name of agv
	 * @param ipaddr of agv
	 * @param port of agv
	 */
	public void insert(int id, String name, String ipaddr, int port){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		String insertAgv = "insert into agv values (?,?,?,?,?)";
		String insertUtil = "insert into utilization values (?,0,0,0,0,0)";
		try {
			ps = connection.prepareStatement(insertAgv);
			ps.setInt(1, id);
			ps.setString(2, name);
			ps.setString(3, ipaddr);
			ps.setInt(4, port);
			ps.setBoolean(5, false);
			
			ps.executeUpdate();
			
			ps1 = connection.prepareStatement(insertUtil);
			ps1.setInt(1, id);
			ps1.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @return all agv's from database
	 */
	public List<AgvData> retriveAgv(){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String getAgv = "select * from agv";
		List<AgvData> agvs = new ArrayList<AgvData>();
		try {
			ps = connection.prepareStatement(getAgv);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				agvs.add(new AgvData(rs.getInt("id"),rs.getString("name"),rs.getString("ipaddress"),rs.getInt("port"),rs.getBoolean("status")));
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return agvs;
	}
	
	/**
	 * Updates existing agv values in database
	 * @param previd of agv to be edited
	 * @param id new
	 * @param name new 
	 * @param ipaddress new 
	 * @param port new
	 * @return
	 */
	public boolean updateAgv(int previd, int id, String name, String ipaddress, int port){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String UpdateAgv = "update agv set id=?,name=?,ipaddress=?,port=?  where id=?";
		try {
			ps = connection.prepareStatement(UpdateAgv);
			ps.setInt(1, id);
			ps.setString(2, name);
			ps.setString(2, ipaddress);
			ps.setInt(4, port);
			ps.setInt(5, previd);
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			return false;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/**
	 * Updates boolean of agv if working sets true else false
	 * @param id of agv  
	 * @param status to be set
	 */
	public void updateStatus(int id, boolean status){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String UpdateAgv = "update agv set status=?  where id=?";
		try {
			ps = connection.prepareStatement(UpdateAgv);
			ps.setBoolean(1, status);
			ps.setInt(2, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
}
