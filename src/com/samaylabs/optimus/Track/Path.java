package com.samaylabs.optimus.Track;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.samaylabs.optimus.Dao.DbLogger;


/**
 * 
 * @author Shabab
 *
 */
public class Path {

	
	private List<Node> nodes = null;
	private List<Edge> edges = null;
	private List<Milestone> milestones = null;
	private List<NodeResolver> nodesResolver = null;
	private Graph graph = null;
	

	/**
	 *  This constructor fetches the map info from database and calculates turns and types of map.
	 */
	public Path(){
		try {
			graph = new Graph();
			nodes = graph.getNodeFromDb();
			nodesResolver = graph.getNodeResolverFromDb();
			edges = graph.getEdgeFromDb(nodes);
			milestones = graph.generateMilestones(nodes,edges);
		}catch (NullPointerException n){
			new DbLogger().getLogger("Path").info("Path : Null pointer exception while getting track information from Database");;
			n.printStackTrace();
		}
	}

	/**
	 * @return List of all Nodes
	 */
	public List<Node> getNodes(){
		return nodes;
	}

	/**
	 * @return List of all the edges
	 */
	public List<Edge> getEdges(){
		return edges;
	}

	public List<Milestone> getMilestones(){
		return milestones;
	}
	
	public Node getNodeFromResolverById(int NRid) {
		for(NodeResolver n : nodesResolver)
			if(n.getNRid() == NRid) 
				return getNodeById((long)n.getAid());
		return null;
	}
	
	public Node getNodeFromResolverByAnchor(long id) {
		for(NodeResolver n : nodesResolver)
			if(n.getNRid() == id) 
				return getNodeById((long)n.getAid());
		return null;
	}
	
	/**
	 * 
	 * @param label name of node
	 * @return Node if node found else null
	 */
	public Node getNodeFromResolverByName(String label) {
		for(NodeResolver node : nodesResolver)
			if(node.getLabel().equals(label)) 
				return getNodeById((long)node.getAid());
		return null;
	}

	/**
	 * 
	 * @param name
	 * @return Node object of name
	 */
	public Node getNodeByName(String name){
		for(Node node : nodes)
			if(node.getName().equals(name))
				return  node;
		return null;
	}

	/**
	 * 
	 * @param id
	 * @return Node object of id
	 */
	public Node getNodeById(int id){
		for(Node node : nodes)
			if( node.getId() == id )
				return  node;
		return null;
	}

	/**
	 * 
	 * @param id
	 * @return Node object of id
	 */
	public Node getNodeById(long id){
		for(Node node : nodes)
			if(node.getAId()==id)
				return  node;
		return null;
	}

	/**
	 * 
	 * @return Dijkstra object
	 */
	public Dijkstra getDijkstra(){
		return new Dijkstra(nodes,edges);
	}

	/**
	 * 
	 * @return list of total anchors in defined map
	 */
	public List<Long> getAnchorIds() {
		List<Long> list = new ArrayList<Long>();
		for(Node node : nodes){
			list.add(node.getAId());
		}
		return list;
	} 

	/**
	 * 
	 * @param source station from map
	 * @param destination station from map
	 * @return list of milestones 
	 */
	public List<Milestone> getShortestPath(String source,String destination,Dijkstra dijkstra){
		dijkstra.execute(this.getNodeByName(source));
		return graph.getAssociatedMilestones(dijkstra.getPath(getNodeByName(destination)),milestones);
	}

	/**
	 * 
	 * @param source node
	 * @param destination node
	 * @return list of milestones associated between them
	 */
	public List<Milestone> getShortestPath(Node source,Node destination, Dijkstra dijkstra){
		dijkstra.execute(source);
		return graph.getAssociatedMilestones(dijkstra.getPath(destination),milestones);
	}
	/**
	 * 
	 * @param source node id
	 * @param destination node id
	 * @return list of milestones associated between them
	 */
	public List<Milestone> getShortestPath(int source,int destination, Dijkstra dijkstra){
		dijkstra.execute(this.getNodeById(source));
		return graph.getAssociatedMilestones(dijkstra.getPath(getNodeById(destination)),milestones);
	}

	/**
	 * 
	 * @param source anchor id 
	 * @param destination anchor id
	 * @return list of milestones associated between them
	 */
	public List<Milestone> getShortestPath(long source,long destination, Dijkstra dijkstra){
		dijkstra.execute(this.getNodeById(source));
		return graph.getAssociatedMilestones(dijkstra.getPath(getNodeById(destination)),milestones);
	}

	/**
	 * 
	 * @param source node object
	 * @param destination node object
	 * @return distance between those nodes
	 */
	public int getDistancebetweenNodes(Node source, Node destination, Dijkstra dijkstra) {
		int count = 0;
		if(source.getAId() == destination.getAId())
			return 0;
		dijkstra.execute(source);
		LinkedList<Node> pathNode = dijkstra.getPath(destination);
		for(int i=0 ; i<pathNode.size()-1 ; i++){
			for(Edge e : edges){
				if(e.getSource().equals(pathNode.get(i)) && e.getDestination().equals(pathNode.get(i+1))){
					count +=e.getDistance();
				}
			}
		}

		return count;
	}

	/**
	 * 
	 * @param miestones list
	 * @return iteration compitable format for transmission to AGV
	 */
	public List<Milestone> resolveMilestone(List<Milestone> milestones){
		List<Milestone> mstone = new ArrayList<Milestone>();

		for(int i=0 ; i <= milestones.size() ; i++){

			if(i != milestones.size()){
				Milestone m = milestones.get(i);
				mstone.add(new Milestone(m.getSource(),null,m.getType(),m.getAction(),m.getDistance()));
			} else { 
				Milestone m = milestones.get(i - 1);
				mstone.add(new Milestone(m.getDestination(),null,0,9,0));
			}
		}
		return mstone;
	}
	
	/**
	 * 
	 * @param node value of any station
	 * @return Resolver Id assosiated with that node
	 */
	public int getResolverIdByNode(Node node){
		for(NodeResolver n : nodesResolver){
			if(n.getAid() == node.getAId())
				return n.getNRid();
		}
		return -1;
	}
	
	
	public List<Node> getBusinessNodes(){
		List<Node> tmp = new ArrayList<Node>();
		for(Node n : nodes){
			if(n.getNodeType() == 1)
				tmp.add(n);
		}
		return tmp;
	}

	public List<Node> getParkingNodes(){
		List<Node> tmp = new ArrayList<Node>();
		for(Node n : nodes){
			if(n.getNodeType() == 2 || n.getNodeType() == 4)
				tmp.add(n);
		}
		return tmp;
	}
	
	public List<Node> getChargingNodes(){
		List<Node> tmp = new ArrayList<Node>();
		for(Node n : nodes){
			if(n.getNodeType() == 4)
				tmp.add(n);
		}
		return tmp;
	}
	
}
