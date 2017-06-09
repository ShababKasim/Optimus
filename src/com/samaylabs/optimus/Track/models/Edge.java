package com.samaylabs.optimus.Track.models;

/**
 * 
 * @author Shabab
 *
 */
public class Edge{

	
	private int id;
	private Node source;
	private Node destination;
	private double distance;
	private float radius;
	private boolean flag;
	
	
	/**
	 * 
	 * @param eid Edge Id
	 * @param source node
	 * @param destination node
	 * @param distance distance between source and destination
	 * @param radius if curve then radius else 0
	 */
	public Edge(int eid,Node source, Node destination, double distance,float radius){
		this.id = eid;
		this.source = source;
		this.destination = destination;
		this.distance = distance;
		this.radius = radius;
	}
	
	public Edge(int eid,Node source, Node destination, double distance,float radius, boolean flag){
		this.id = eid;
		this.source = source;
		this.destination = destination;
		this.distance = distance;
		this.radius = radius;
		this.flag = flag;
	}
	
	/**
	 * 
	 * @return id of an edge
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @return distance from source to destination
	 */
	public double getDistance() {
		return distance;
	}
	
	/**
	 * 
	 * @return source object of a edge in form of node
	 */
	public Node getSource() {
		return source;
	}
	
	/**
	 * 
	 * @return destination object of a edge in form of node
	 */
	public Node getDestination() {
		return destination;
	}
	
	/**
	 * 
	 * @return radius associated to an edge
	 */
	public float getRadius(){
		return radius;
	}


	public void setId(int id) {
		this.id = id;
	}


	public void setSource(Node source) {
		this.source = source;
	}


	public void setDestination(Node destination) {
		this.destination = destination;
	}


	public void setDistance(double distance) {
		this.distance = distance;
	}


	public void setRadius(float radius) {
		this.radius = radius;
	}

	public boolean isFlag() {
		return flag;
	}


	public void setFlag(boolean flag) {
		this.flag = flag;
	}


	@Override
	public String toString() {
		return "[" + id + "," + source.getName() + ", " + destination.getName() + ", " + distance
				+ "," + radius + "]";
	}


	
}
