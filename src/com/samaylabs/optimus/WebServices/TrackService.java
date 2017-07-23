package com.samaylabs.optimus.WebServices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.samaylabs.optimus.Dao.DbConnection;
import com.samaylabs.optimus.Dao.DbConstants;
import com.samaylabs.optimus.Track.Graph;
import com.samaylabs.optimus.Track.models.Edge;
import com.samaylabs.optimus.Track.models.Node;
import com.samaylabs.optimus.Track.models.NodeResolver;


/**
 * Methods in this Class is Called by clients which are related to Agv Track defination ad Manipulation  
 * @author Tulve Shabab Kasim
 *
 */
@WebService(name="TrackDefination",serviceName="TrackDefinationService", portName="TrackPort")
public class TrackService implements DbConstants{
	
	Graph graph = new Graph();
	List<Node> nodes = graph.getNodeFromDb();
	List<Edge> edges = graph.getEdgeFromDb(nodes);
	List<NodeResolver> noderesolvers = graph.getNodeResolverFromDb();

	
	TrackService(){
		Thread.currentThread().setName("TrackService");
	}

	@WebMethod
	private boolean initValues(){
		try{
			nodes = graph.getNodeFromDb();
			edges = graph.getEdgeFromDb(nodes);
			noderesolvers = graph.getNodeResolverFromDb();
		} catch (Exception e){
			return false;
		}
		return true;
	}

	@WebMethod
	public boolean createNode(long anchor,float xco, float yco, int type){
		Connection connection = new DbConnection().getConncetion();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(insertnode);
			ps.setInt(1, (int)anchor);
			ps.setString(2, "" + anchor);
			ps.setFloat(3, xco);
			ps.setFloat(4, yco);
			ps.setLong(5, anchor);
			ps.setInt(6, type);
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
	public boolean deleteNode(long anchor){
		Connection connection = new DbConnection().getConncetion();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(deletenode);
			ps.setLong(1, anchor);
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
	public Node retriveNode(){
		return new Node(1,"A",12,14,(long) 14,1);
	}

	@WebMethod
	public List<Node> retriveNodes(){
		initValues();
		return nodes;
	}
	
	@WebMethod
	public boolean updateNode(long prevanchor, long anchor,float xco, float yco, int type){
		Connection connection = new DbConnection().getConncetion();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updatenode);
			ps.setLong(1, anchor);
			ps.setFloat(2, xco);
			ps.setFloat(3, yco);
			ps.setInt(4, type);
			ps.setLong(5,prevanchor);
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
	public boolean createNodeResolver(int nrid, long anchor,  String Label){
		Connection connection = new DbConnection().getConncetion();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(insertnoderesolver);
			ps.setInt(1, nrid);
			ps.setLong(2, anchor);
			ps.setString(3, Label);
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
	public List<NodeResolver> retriveNodeResolver(){
		initValues();
		return noderesolvers;
	}

	@WebMethod
	public boolean updateNodeResolver(int pnrid, int nrid, long anchor,  String Label){
		Connection connection = new DbConnection().getConncetion();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updatenoderesolver);
			ps.setInt(1, nrid);
			ps.setLong(2, anchor);
			ps.setString(3, Label);
			ps.setInt(4, pnrid);
			
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
	public boolean deleteNodeResolver(int nrid){
		Connection connection = new DbConnection().getConncetion();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(deletenoderesolver);
			ps.setLong(1, nrid);
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
	public boolean createEdge(long src, long dest, double dist, float radius){
		Connection connection = new DbConnection().getConncetion();
		PreparedStatement ps = null;
		
		try {
			ps = connection.prepareStatement(insertedge);
			
			ps.setLong(1, src);
			ps.setLong(2, dest);
			ps.setDouble(3, dist);
			ps.setFloat(4, radius);
			ps.setBoolean(5, false);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Exception");
			e.printStackTrace();
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
	public List<Edge> retriveEdge(){
		initValues();
		return edges;
	}

	@WebMethod
	public boolean deleteEdge(long src, long dest){
		Connection connection = new DbConnection().getConncetion();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(deleteedge);
			ps.setLong(1, src);
			ps.setLong(2, dest);
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
	public boolean updateEdge(long prevsrc, long prevdest,long src, long dest, double dist, float radius){
		Connection connection = new DbConnection().getConncetion();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(updateedge);
			ps.setLong(1, src);
			ps.setLong(2, dest);
			ps.setDouble(3, dist);
			ps.setFloat(4, radius);
			ps.setLong(5, prevsrc);
			ps.setLong(6, prevdest);
			
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
	public List<Long> getAnchors(){
		List<Node> nodes = new Graph().getNodeFromDb();
		List<Long> anchors = new ArrayList<Long>();
		for(Node n : nodes){
			if(n.getNodeType() != 3)
				anchors.add(n.getAnchor_id());
		}
		return anchors;
	}

	@WebMethod
	public List<Long> getAllAnchors(){
		List<Node> nodes = new Graph().getNodeFromDb();
		List<Long> anchors = new ArrayList<Long>();
		for(Node n : nodes){
			anchors.add(n.getAnchor_id());
		}
		return anchors;
	}

	public static void main(String[] args) {
		TrackService s = new TrackService();
		s.updateEdge(3, 4, 3, 4, 21, 2100);
	}
		
}
