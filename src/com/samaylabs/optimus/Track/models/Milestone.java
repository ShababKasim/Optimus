package com.samaylabs.optimus.Track.models;

/**
 * 
 * @author Shabab
 *
 */
public class Milestone {

	
	private Node source;
	private Node destination;
	private int type;
	private int action;
	private double distance;


	/**
	 * 
	 * @param id of a milestone
	 * @param source of an edge
	 * @param destination of an edge
	 * @param type information of an edges i.e 1.>Linear 2.> split 3.>Merge 4.> Fork 5.> Turn 
	 * @param action 1.> Straight 2.> Left 3.> Right
	 */
	public Milestone(Node source, Node destination, int type, int action,double distance){
		this.source = source;
		this.destination = destination;
		this.type = type;
		this.action = action;
		this.distance = distance;
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
	 * @return type of an milestone i.e 1.>Linear 2.> split 3.>Merge 4.> Fork 5.> Turn 6.> Edge
	 */
	public int getType() {
		return type;
	}

	/**
	 * 
	 * @return distance between two nodes
	 */
	public double getDistance() {
		return distance;
	}
	
	/**
	 * 
	 * @return action taken on an milestone i.e 1.> Straight 2.> Left 3.> Right
	 */
	public int getAction() {
		return action;
	}

	public void setSource(Node source) {
		this.source = source;
	}


	public void setDestination(Node destination) {
		this.destination = destination;
	}


	public void setType(int type) {
		this.type = type;
	}


	public void setAction(int action) {
		this.action = action;
	}


	public void setDistance(double distance) {
		this.distance = distance;
	}


	public String toString() {
		return "(" + source.getName() + "," + destination.getName() + "," + type
				+ "," + action +", " + distance +")";
	}


}
