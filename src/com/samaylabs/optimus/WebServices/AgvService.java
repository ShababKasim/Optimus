package com.samaylabs.optimus.WebServices;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.samaylabs.optimus.Communication.Plc.AgvMethods;
import com.samaylabs.optimus.Dao.DbConnection;
import com.samaylabs.optimus.WebServices.models.AgvData;
import com.samaylabs.optimus.WebServices.models.AgvUtil;
import com.samaylabs.optimus.WebServices.models.ListWrapper;


/**
 * Methods in this Class is Called by clients which are related to Agv CURD operations and Ping related services
 * @author Tulve Shabab Kasim
 *
 */
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
	public List<AgvUtil> getUtilization(int id){
		String query = "select * from utilization where id=?";
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		List<AgvUtil> utils = new ArrayList<AgvUtil>();
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				utils.add(new AgvUtil(rs.getInt("id"),rs.getString("dated"),rs.getLong("disconnected"),rs.getLong("moving"),rs.getLong("idle"),rs.getLong("tickets"),rs.getLong("error")));
			
		} catch (SQLException e) {
			return utils;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return utils;
	}
	
	@WebMethod
	public AgvUtil getTodayUtil(int id){
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
		String query = "select * from utilization where dated=? and id=?";
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(2, id);
			ps.setString(1, ft.format(dNow));
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				return new AgvUtil(rs.getInt("id"),rs.getString("dated"),rs.getLong("disconnected"),rs.getLong("moving"),rs.getLong("idle"),rs.getLong("tickets"),rs.getLong("error"));
			
		} catch (SQLException e) {
			return null;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@WebMethod
	public List<AgvUtil> getTodayAllUtilization(){
		String query = "select * from utilization where dated=?";
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		List<AgvUtil> utils = new ArrayList<AgvUtil>();
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, ft.format(dNow));
			
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				utils.add(new AgvUtil(rs.getInt("id"),rs.getString("dated"),rs.getLong("disconnected"),rs.getLong("moving"),rs.getLong("idle"),rs.getLong("tickets"),rs.getLong("error")));
		} catch (SQLException e) {
			return utils;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return utils;
	}
	
	@WebMethod
	public AgvUtil getUtilizationByIdAndDate(int id,String start, String end){
		String query = "select id,sum(disconnected) as disconnected,sum(moving) as moving,sum(idle) as idle,sum(tickets) as tickets, sum(error) as error from utilization where dated>=? and dated<=? and id=?";
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, start);
			ps.setString(2, end);
			ps.setInt(3, id);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
				return new AgvUtil(rs.getInt("id"),rs.getLong("disconnected"),rs.getLong("moving"),rs.getLong("idle"),rs.getLong("tickets"),rs.getLong("error"));
			
				
		} catch (SQLException e) {
			return null;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@WebMethod
	public List<AgvUtil> getAllUtilization(){
		String query = "select * from utilization";
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		List<AgvUtil> utils = new ArrayList<AgvUtil>();
		try {
			ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				utils.add(new AgvUtil(rs.getInt("id"),rs.getString("dated"),rs.getLong("disconnected"),rs.getLong("moving"),rs.getLong("idle"),rs.getLong("tickets"),rs.getLong("error")));
		} catch (SQLException e) {
			return utils;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return utils;
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
