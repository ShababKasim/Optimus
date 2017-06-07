package com.samaylabs.optimus.WebServices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import com.samaylabs.optimus.Dao.DbConnection;

@WebService(name="LogsDefination",serviceName="LogsService", portName="LogsPort")
public class LogsService {

	protected DbConnection db;
	
	LogsService(){
		Thread.currentThread().setName("LogsService");
		db = new DbConnection();
	}
	
	public List<Logs> getOptimusLogs(){
		String query = "select * from logs";
		List<Logs> log = new ArrayList<Logs>();
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				log.add(new Logs(rs.getString("user_id"), rs.getString("dated"), rs.getString("level"), rs.getString("message")));
		} catch (SQLException e) {
			return log;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return log;
	}

	public ListWrapper getUsers(){
		String query = "select distinct user_id from logs";
		ListWrapper log = new ListWrapper();
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				log.add(rs.getString("user_id"));
		} catch (SQLException e) {
			return log;
		} finally {
			try {
				if(ps!=null)
					ps.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return log;
	}

	public List<Logs> getOptimusLogsDated(String userid, String start , String end){
		String query = null;
		List<Logs> log = new ArrayList<Logs>();
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		if(userid.equals("All")) {
			query = "select * from logs where Dated >= ? and dated <= ?";
			try {
				ps = connection.prepareStatement(query);
				ps.setString(1, start);
				ps.setString(2, end);
				ResultSet rs = ps.executeQuery();
				while(rs.next())
					log.add(new Logs(rs.getString("user_id"), rs.getString("dated"), rs.getString("level"), rs.getString("message")));
			} catch (SQLException e) {
				return log;
			} finally {
				try {
					if(ps!=null)
						ps.close();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			query = "select * from logs where user_id=? and dated >=? and dated <=?";
			try {
				ps = connection.prepareStatement(query);
				ps.setString(1, userid);
				ps.setString(2, start);
				ps.setString(3, end);
				ResultSet rs = ps.executeQuery();
				while(rs.next())
					log.add(new Logs(rs.getString("user_id"), rs.getString("dated"), rs.getString("level"), rs.getString("message")));
			} catch (SQLException e) {
				return log;
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
		return log;
	}
	
}
