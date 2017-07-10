package com.samaylabs.optimus.Track.models;

/**
 * Model to hold Edge info from database, use method is prototype
 * @author Tulve Shabab Kasim
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
	
	/**
	 * 
	 * @param eid
	 * @param source
	 * @param destination
	 * @param distance
	 * @param radius
	 * @param flag
	 */
	public Edge(int eid,Node source, Node destination, double distance,float radius, boolean flag){
		this.id = eid;
		this.source = source;
		this.destination = destination;
		this.distance = distance;
		this.radius = radius;
		this.flag = flag;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getDestination() {
		return destination;
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public float getRadius() {
		return radius;
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
