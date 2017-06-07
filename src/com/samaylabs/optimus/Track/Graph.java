package com.samaylabs.optimus.Track;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.samaylabs.optimus.Dao.DbConnection;
import com.samaylabs.optimus.Dao.DbConstants;
import com.samaylabs.optimus.Dao.EdgeTurnsDao;

/**
 * 
 * @author Shabab
 *
 */
public class Graph implements DbConstants {

	
	private List<Node> nodes = new ArrayList<Node>();
	private List<Edge> edges = new ArrayList<Edge>();
	private List<Milestone> milestones = new ArrayList<Milestone>();
	private List<NodeResolver> nodeRessolver = new ArrayList<NodeResolver>();
	
	private DbConnection db = new DbConnection();
	private ResultSet resultset;
	private ResultSetMetaData resultsetmetadata;


	/**
	 * 
	 * @return List of all node object
	 */
	public List<Node> getNodes() {
		return nodes;
	}

	/**
	 * 
	 * @return List of all Edge object
	 */
	public List<Edge> getEdges() {
		return edges;
	}

	public List<NodeResolver> getNodeRessolver() {
		return nodeRessolver;
	}

	public void setNodeRessolver(List<NodeResolver> nodeRessolver) {
		this.nodeRessolver = nodeRessolver;
	}

	/**
	 * 
	 * @param name of a node
	 * @return Node object corresponds to name
	 */
	public Node getNodeByName(String name, List<Node> nodes){
		for(Node v : nodes)
			if(v.getName().equals(name))
				return  v;
		return null;
	}

	/**
	 * 
	 * @param id of Node
	 * @return Node object corresponds to id
	 */
	public Node getNodeById(int id,List<Node> nodes){
		for(Node v : nodes)
			if(v.getId()==id)
				return  v;
		return null;
	}

	/**
	 * 
	 * @param id of Node
	 * @return Node object corresponds to id
	 */
	public Node getNodeById(long id,  List<Node> nodes){
		for(Node v : nodes)
			if(v.getAId()==id)
				return  v;
		return null;
	}

	/**
	 * 
	 * @param src of an edge
	 * @param dest of an edge
	 * @param edges list of edges
	 * @return edge id of an edge if match of src and dest is found
	 */
	public int getEdgeId(int src,int dest,List<Edge> edges){
		int ed = 0;
		for(Edge e1 : edges){
			if(e1.getSource().getId()== src && e1.getDestination().getId() == dest ){
				ed = e1.getId();
			}
		}
		return ed;
	}

	/**
	 * 
	 * @param src node in a map
	 * @param dest node in a map
	 * @param edges list to get edge
	 * @return edge which has source as src and destination as dest
	 */
	public Edge getEdge(Node src , Node dest ,List<Edge> edges){
		for(Edge e : edges){
			if(e.getSource() == src && e.getDestination() == dest){
				return e;
			}
		}
		return null;

	}

	public List<NodeResolver> getNodeResolverFromDb(){
		Statement statement = db.getStatement();
		List<NodeResolver> nodesResolver = null;
		try {
			resultset = statement.executeQuery(selectnodeResolver);
			resultsetmetadata = resultset.getMetaData();
			nodesResolver = new ArrayList<NodeResolver>();

			int col_count = resultsetmetadata.getColumnCount();
			while(resultset.next()){
				for(int i = 1 ; i <= col_count; i+=3){
					nodesResolver.add(new NodeResolver(Integer.parseInt(resultset.getObject(i).toString()),Long.parseLong(resultset.getObject(i+1).toString()),
							resultset.getObject(i+2).toString()));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return nodesResolver;

	}


	/**
	 * 
	 * @return List of nodes stored in database.
	 */
	public List<Node> getNodeFromDb() {
		List<Node> nodes = null;
		Statement statement = db.getStatement();
		try {
			resultset = statement.executeQuery(selectnode);
			resultsetmetadata = resultset.getMetaData();
			nodes = new ArrayList<Node>();

			while(resultset.next()){
				nodes.add(new Node(resultset.getInt("n_id"),resultset.getString("n_name"),resultset.getFloat("x_co"),resultset.getFloat("y_co"),resultset.getLong("anchor"),resultset.getInt("nodetype")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return nodes;
	}

	/**
	 * @method gets Edges information stored in database
	 * @param nodes Needs list of vertex so that it can extract legitimate data
	 * @return List of edges after fetching it from database
	 */
	public List<Edge> getEdgeFromDb(List<Node> nodes) {
		List<Edge> Edges = new ArrayList<Edge>();
		Statement statement = db.getStatement();
		try {
			resultset = statement.executeQuery(selectedge);
			resultsetmetadata = resultset.getMetaData();

			while(resultset.next()){
				Edges.add(new Edge(resultset.getInt("e_id"),this.getNodeById(resultset.getLong("source"),nodes),this.getNodeById(resultset.getLong("destination"),nodes),resultset.getDouble("distance"),resultset.getFloat("radius"),resultset.getBoolean("cflag")));
			}
		} catch (SQLException | NullPointerException e1) {
			e1.printStackTrace();
		}
		return Edges;
	}

	/**
	 * this Method Calculates angle formed by intersection of two lines meet at common point (x2,y2)
	 * @param edge1 which is line 1
	 * @param edge2 which is line 2
	 * @return angle formed by two line meeting on one point
	 */
	public double angleBetween2Lines(Edge edge1 , Edge edge2){
		double x1 = edge1.getSource().getX();
		double y1 = edge1.getSource().getY();
		double x2 = edge1.getDestination().getX();
		double y2 = edge1.getDestination().getY();
		double x3 = edge2.getDestination().getX();
		double y3 = edge2.getDestination().getY();
		double radian = Math.atan2(y1-y2,x1-x2) - Math.atan2(y2-y3,x2-x3);
		return (radian > Math.PI) ? (360 - (180*(radian)/Math.PI)) * -1 : (180*(radian)/Math.PI);
	}

	public double getTypeAngle(Node n1 , Node n2, Node n3){
		float x1 = n1.getX();
		float y1 = n1.getY();
		float x2 = n2.getX();
		float y2 = n2.getY();
		float x3 = n3.getX(); 
		float y3 = n3.getY();
		double radian = Math.atan2(y1-y2,x1-x2) - Math.atan2(y1-y3,x1-x3);
		return (radian > Math.PI) ? (360 - (180*(radian)/Math.PI)) * -1 : (180*(radian)/Math.PI);
	}	

	public int getType(double angle){
		angle *= (angle > 0) ? 1 : -1;
		return (angle > 90) ? 2 : 3;
	}

	/**
	 * 
	 * @param angle between two lines
	 * @return 2 if left turn and 3 if right turn
	 */
	public int getDirection(double angle) {
		return (angle > 0) ? 3 : (angle == 0) ? 1 : 2 ;
	}

	/**
	 * 
	 * @param path gained after dijkstra in form of nodes.
	 * @param milestones list to extract exact milestones according to path
	 * @return list of milestones to proceed.
	 */
	public List<Milestone> getAssociatedMilestones(LinkedList<Node> path,List<Milestone> milestones){
		List<Milestone> way = new ArrayList<Milestone>();

		for(int i = 0 ; i < path.size()-1 ; i++){
			for(int j = 0 ; j < milestones.size() ; j++){
				if(path.get(i).equals(milestones.get(j).getSource()) && path.get(i+1).equals(milestones.get(j).getDestination())){
					way.add(milestones.get(j));
				}
			}
		}
		return way;
	}

	/**
	 * 
	 * @param nodes list of nodes
	 * @param edges list of edges
	 * @return list of milestones with action code and type
	 */
	public List<Milestone> generateMilestones(List<Node> nodes ,List<Edge> edges){

		List<Node> slist = null;
		List<Node> dlist = null;

		for( Node node : nodes ) {

			slist = new ArrayList<Node>();
			dlist = new ArrayList<Node>();

			for(int i = 0 ; i<edges.size() ; i++){
				if(node.equals(edges.get(i).getSource())){
					slist.add(edges.get(i).getDestination());
				}
			}

			for(int i = 0 ; i<edges.size() ; i++){
				if(node.equals(edges.get(i).getDestination())){
					dlist.add(edges.get(i).getSource());
				}
			}

			int sc = slist.size() ,dc = dlist.size();

			//System.out.println("("+ sc +" , " + dc + ")" );


			if(sc == 1 && dc == 1) {
				Edge prev = getEdge(dlist.get(0), node, edges);
				Edge next = getEdge(node ,slist.get(0), edges);

				if(next.getRadius() > 0){
					int action = getDirection(angleBetween2Lines(prev,next));
					milestones.add(new Milestone( node , slist.get(0),5,action,next.getDistance()));
				} else{
					milestones.add(new Milestone( node, slist.get(0),1,1,next.getDistance()));
				}

			} else if( sc == 2 && dc == 1 ) {
				List<Edge> nexts = new ArrayList<Edge>();
				Edge prev = getEdge(dlist.get(0),node , edges);

				for(Node n : slist)
					nexts.add(getEdge(node,n,edges));

				if(nexts.get(0).getRadius() > 0 && nexts.get(1).getRadius() > 0){
					for(int i = 0; i < nexts.size() ; i++){
						int action = getDirection(angleBetween2Lines(prev,nexts.get(i)));
						milestones.add(new Milestone( node,slist.get(i),4,action,getEdge(node,slist.get(i), edges).getDistance()));
					}
				} else{
					int type = 0;
					for(Edge e : nexts){
						if(e.getRadius() > 0){
							Node third = (prev.getDestination().equals(e.getDestination()) ? e.getSource() : e.getDestination() );
							type = getType(getTypeAngle(prev.getDestination(),prev.getSource(),third));
						}
					}
					for(Edge e : nexts){
						if(e.getRadius() > 0){
							int action = getDirection(angleBetween2Lines(prev,e));
							milestones.add(new Milestone( node,e.getDestination(),type,action,getEdge(node,e.getDestination(), edges).getDistance()));
						} else{
							milestones.add(new Milestone( node,e.getDestination(),type,1,getEdge(node,e.getDestination(), edges).getDistance()));
						}

					}
				}

			} else if( sc == 1 && dc == 2 ) {

				Edge next = getEdge(node,slist.get(0),edges);
				List<Edge> prevs = new ArrayList<Edge>();

				for(Node n : dlist)
					prevs.add(getEdge(n,node,edges));

				int type = 0;
				Node third = null;
				for(Edge e : prevs){
					if(e.getRadius() > 0){
						third = (next.getSource().equals(e.getDestination()) ? e.getSource() : e.getDestination() );
					}
				}

				for(Edge e : prevs){
					if(!(e.getRadius() > 0)){
						type = getType(getTypeAngle(e.getDestination(),e.getSource(),third));
					}
				}

				milestones.add(new Milestone(node ,slist.get(0),type,1,next.getDistance()));

			} else if( sc == 2 && dc == 2 ) {

				Node common = null;
				for(Node n : slist)
					for(Node n1 : dlist)
						if(n.equals(n1))
							common = n;

				Node third = null;
				for(Node n : dlist)
					if(!(n.equals(common)))
						third = n;

				Node second = null;
				for(Node n : slist)
					if(!(n.equals(common)))
						second = n;

				for(Node ni : slist){
					if(ni.equals(common)){
						int type = getType(getTypeAngle(node,second,third));
						milestones.add(new Milestone(node ,ni,type,1,getEdge(node ,ni,edges).getDistance()));
					} else {
						int type = getType(getTypeAngle(node,common,third));
						milestones.add(new Milestone(node ,ni,type,1,getEdge(node ,ni,edges).getDistance()));
					}
				}
			}
		}
		
		/** A small Patch placed here to push turn info in Db so it can reflect in live Map */
		EdgeTurnsDao et = new EdgeTurnsDao();
		for(Milestone m : milestones){
			if(m.getAction() == 3){
				et.updateFlag(m.getSource().getAnchor_id(), m.getDestination().getAnchor_id(),true);
			}
				
		}
		
		/** A small Patch placed here to to write opposite action in split. */
		for(Milestone m : milestones){
			if(m.getType() == 2 && m.getAction() == 1) {
				for(Milestone m1 : milestones) {
					if(m.getSource().getAnchor_id() == m1.getSource().getAnchor_id() && m1.getAction() != 1)
						m.setAction((m1.getAction() == 3 ? 2 : 3 ));
				}
			}
				
		}
		
		return milestones;
	}


}






