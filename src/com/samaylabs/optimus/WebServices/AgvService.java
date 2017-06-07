package com.samaylabs.optimus.WebServices;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.samaylabs.optimus.Communication.Plc.AgvMethods;
import com.samaylabs.optimus.Dao.DbConnection;

@WebService(name="AgvDeclaration",serviceName="AgvService", portName="AgvPort")
public class AgvService {

	protected DbConnection db;

	AgvService(){
		Thread.currentThread().setName("AgvService");
		db = new DbConnection();
	}

	@WebMethod
	public boolean createAgv(int id, String name, String ip, int port){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String insertAgv = "insert into agv values(?,?,?,?,?)";
		try {
			ps = connection.prepareStatement(insertAgv);
			ps.setInt(1, id);
			ps.setString(2, name);
			ps.setString(3, ip);
			ps.setInt(4, port);
			ps.setBoolean(5, false);
			ps.executeUpdate();
		} catch (SQLException e) {
			return false;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@WebMethod
	public boolean deleteAgv(int id){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String deleteAgv = "delete from agv where id=?";
		try {
			ps = connection.prepareStatement(deleteAgv);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			return false;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@WebMethod
	public List<AgvData> retriveAgv(){
		String selectAgv = "select * from agv";
		List<AgvData> agv = new ArrayList<AgvData>();
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(selectAgv);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				agv.add(new AgvData(rs.getInt("id"), rs.getString("name"), rs.getString("ipaddress"), rs.getInt("port"), rs.getBoolean("status")));
		} catch (SQLException e) {
			return agv;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return agv;
	}

	@WebMethod
	public boolean updateAgv(int pid, int id, String name, String ip, int port){
		String updateAgv = "update agv set id=?, name=?, ipaddress=?,port=? where id=? ";
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateAgv);
			ps.setInt(1, id);
			ps.setString(2, name);
			ps.setString(3, ip);
			ps.setInt(4, port);
			ps.setInt(5, pid);
			ps.executeUpdate();
		} catch (SQLException e) {
			return false;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	} 

	@WebMethod
	public ListWrapper pingAgv(String ip, int port){
		ListWrapper log = new ListWrapper();
		AgvMethods agvmethods = null;
		try {
			agvmethods = new AgvMethods(ip,port);
		} catch (UnknownHostException e) {
			log.add("Unable to find Agv");
			return log;
		}
		try {
			agvmethods.readCoil(0x800);
		} catch (Exception e) {
			log.add("Unable to create Modbus Session with Agv");
			return log;
		}
		log.add("Success");
		return log;
	}

}
