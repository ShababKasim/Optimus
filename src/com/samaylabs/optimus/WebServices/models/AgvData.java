package com.samaylabs.optimus.WebServices.models;

import com.samaylabs.optimus.Communication.StationNode.models.Ticket;
import com.samaylabs.optimus.Track.models.Node;

public class AgvData {

	private int id;
	private String name;
	private String ipaddress;
	private int port;
	private boolean status;
	private Ticket servingTicket;
	private Node position;
	private long dest;
	
	public AgvData(int id,String name, String ipaddress, int port, Ticket servingTicket, Node position){
		this.id = id;
		this.name = name;
		this.ipaddress = ipaddress;
		this.port = port;
		this.servingTicket = servingTicket;
		this.position = position;
	}
	
	public AgvData(int id, String name, String ipaddress, int port, boolean status){
		this.id = id;
		this.name = name;
		this.ipaddress = ipaddress;
		this.port = port;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Ticket getServingTicket() {
		return servingTicket;
	}

	public void setServingTicket(Ticket servingTicket) {
		this.servingTicket = servingTicket;
	}

	public Node getPosition() {
		return position;
	}

	public void setPosition(Node position) {
		this.position = position;
	}

	public long getDest() {
		return dest;
	}

	public void setDest(long dest) {
		this.dest = dest;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
