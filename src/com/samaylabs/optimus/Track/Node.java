package com.samaylabs.optimus.Track;

/**
 * 
 * @author Shabab
 *
 */
public class Node {

	private int id;
	private String name;
	private float x_co;
	private float y_co;
	private long anchor_id;
	private int nodeType;
	
	/**
	 * 
	 * @param id of node
	 * @param name of node
	 * @param x_co of node
	 * @param y_co of node
	 * @param anchor of node
	 * @param nodeType type of node 1> business 2>parking 3>other 4>Charging
	 */
	public Node(int id,String name,float x_co,float y_co,long anchor,int nodeType){
		this.id = id;
		this.name = name;
		this.x_co = x_co;
		this.y_co = y_co;
		this.anchor_id = anchor;
		this.nodeType = nodeType;
	}
	
	/**
	 * 
	 * @return
	 */
	public float getX_co() {
		return x_co;
	}

	/**
	 * 
	 * @return
	 */
	public float getY_co() {
		return y_co;
	}

	/**
	 * 
	 * @return
	 */
	public long getAnchor_id() {
		return anchor_id;
	}
	
	/**
	 * 
	 * @return name of node
	 */
	public String getName(){
		return name;
	}

	/**
	 * 
	 * @return id of node
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @return anchor id of node
	 */
	public long getAId() {
		return anchor_id;
	}

	/**
	 * 
	 * @return x-co ordinate of node
	 */
	public float getX(){
		return x_co;
	}

	/**
	 * 
	 * @return y-co ordinate of node
	 */
	public float getY(){
		return y_co;
	}
	
	/**
	 * 
	 * @return node type 0.>Other node 1.> Business node 2.> Parking node 
	 */
	public int getNodeType() {
		return nodeType;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setX_co(float x_co) {
		this.x_co = x_co;
	}

	public void setY_co(float y_co) {
		this.y_co = y_co;
	}

	public void setAnchor_id(long anchor_id) {
		this.anchor_id = anchor_id;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public String toString(){
		return "(" + id +","+ name + ","+x_co+ "," + y_co+ "," + anchor_id + "," + nodeType + ")";
	}




}
