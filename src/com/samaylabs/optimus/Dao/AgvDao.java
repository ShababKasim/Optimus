package com.samaylabs.optimus.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.samaylabs.optimus.WebServices.AgvData;

public class AgvDao {

	private DbConnection db;
	
	public AgvDao(){
		db = new DbConnection();
	}
	
	public void insert(int id, String name, String ipaddr, int port){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String insertAgv = "insert into agv values (?,?,?,?,?)";
		try {
			ps = connection.prepareStatement(insertAgv);
			ps.setInt(1, id);
			ps.setString(2, name);
			ps.setString(3, ipaddr);
			ps.setInt(4, port);
			ps.setBoolean(5, false);
			
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
	
	public void updateIp(int id, String ip){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String UpdateAgv = "update agv set ipaddress=?  where id=?";
		try {
			ps = connection.prepareStatement(UpdateAgv);
			ps.setString(1, ip);
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
	
	public void updatePort(int id, int port){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String UpdateAgv = "update agv set port=?  where id=?";
		try {
			ps = connection.prepareStatement(UpdateAgv);
			ps.setInt(1, port);
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
	
	public void updateId(int id, String ipaddr){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String UpdateAgv = "update agv set id=?  where ipaddress=?";
		try {
			ps = connection.prepareStatement(UpdateAgv);
			ps.setInt(1, id);
			ps.setString(2, ipaddr);
			
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
	
	public void updateName(int id, String name){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String UpdateAgv = "update agv set name=?  where id=?";
		try {
			ps = connection.prepareStatement(UpdateAgv);
			ps.setString(1, name);
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
