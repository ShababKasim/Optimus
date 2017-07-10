package com.samaylabs.optimus.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.samaylabs.optimus.Communication.StationNode.models.Ticket;


/**
 * This class is used for CURD operation of ticket into database
 * @author Tulve Shabab Kasim
 *
 */
public class TicketDao {

	private DbConnection db;
	
	
	public TicketDao(){
		db = new DbConnection();
	}
	
	/**
	 * 
	 * @param id
	 * @param Uid
	 * @param Pdest
	 * @param type
	 * @param status
	 */
	public void insertTicket(int id, long Uid, int Pdest, String type, String status){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String insertTicket = "insert into Ticket(id,Uid,Pdestination,Type,Status) values (?,?,?,?,?)";
		try {
			ps = connection.prepareStatement(insertTicket);
			ps.setInt(1, id);
			ps.setLong(2, Uid);
			ps.setInt(3, Pdest);
			ps.setString(4, type);
			ps.setString(5,status);
			
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
	
	/**
	 * 
	 * @param ticket
	 */
	public void insertTicket(Ticket ticket){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String insertTicket = "insert into Ticket(id,Uid,Pdestination,Type,Status) values (?,?,?,?,?)";
		try {
			ps = connection.prepareStatement(insertTicket);
			ps.setInt(1, ticket.getTid());
			ps.setLong(2, ticket.getUid());
			ps.setInt(3, ticket.getPdestination());
			ps.setString(4, ticket.getType());
			ps.setString(5,ticket.getStatus());
			
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
	
	/**
	 * 
	 * @param id
	 * @param Agvno
	 * @param status
	 */
	public void updateAgvinfo(int id, int Agvno, String status){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String UpdateAgv = "update Ticket set Agvno=?, status=? where id=?";
		try {
			ps = connection.prepareStatement(UpdateAgv);
			ps.setInt(1, Agvno);
			ps.setString(2, status);
			ps.setInt(3, id);
			
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
	
	/**
	 * 
	 * @param id
	 * @param source
	 */
	public void updateSource(int id, long source){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String UpdateAgv = "update Ticket set Source=?  where id=?";
		try {
			ps = connection.prepareStatement(UpdateAgv);
			ps.setLong(1, source);
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
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param id
	 * @param Sdest
	 */
	public void updateSdest(int id, int Sdest){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String UpdateAgv = "update Ticket set Sdestination=?  where id=?";
		try {
			ps = connection.prepareStatement(UpdateAgv);
			ps.setInt(1, Sdest);
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
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param id
	 * @param status
	 */
	public void updateStatus(int id, String status){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String UpdateAgv = "update Ticket set status=?  where id=?";
		try {
			ps = connection.prepareStatement(UpdateAgv);
			ps.setString(1, status);
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
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param id
	 */
	public void deleteTicket(int id){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String query = "delete from ticket where id=?";
		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, id);
			
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
	
	/**
	 * Creates backup of existing ticket table into new table and deletes all intry in ticket table
	 */
	public void backupAndDeleteTickets(){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String query = "insert into ticket_old(select * from ticket)";
		try {
			ps = connection.prepareStatement(query);
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
		deleteAllTickets();
	}
	
	/**
	 *  Deletes all tickets in table
	 */
	public void deleteAllTickets(){
		Connection connection = db.getConncetion();
		PreparedStatement ps = null;
		String query = "delete from ticket";
		try {
			ps = connection.prepareStatement(query);
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
