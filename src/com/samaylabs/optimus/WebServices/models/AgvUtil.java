package com.samaylabs.optimus.WebServices.models;

public class AgvUtil {

	private int id;
	private String dated;
	private long disconnected;
	private long working;
	private long idle;
	private long tickets;
	private long error;
	
	public AgvUtil(int id, long disconnected, long working, long idle, long tickets, long error) {
		super();
		this.id = id;
		this.disconnected = disconnected;
		this.working = working;
		this.idle = idle;
		this.tickets = tickets;
		this.error = error;
	}
	
	public AgvUtil(int id, String dated, long disconnected, long working, long idle, long tickets, long error) {
		super();
		this.id = id;
		this.dated = dated;
		this.disconnected = disconnected;
		this.working = working;
		this.idle = idle;
		this.tickets = tickets;
		this.error = error;
	}
	
	public String getDated() {
		return dated;
	}
	public void setDated(String dated) {
		this.dated = dated;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getDisconnected() {
		return disconnected;
	}
	public void setDisconnected(long disconnected) {
		this.disconnected = disconnected;
	}
	public long getWorking() {
		return working;
	}
	public void setWorking(long working) {
		this.working = working;
	}
	public long getIdle() {
		return idle;
	}
	public void setIdle(long idle) {
		this.idle = idle;
	}
	public long getTickets() {
		return tickets;
	}
	public void setTickets(long tickets) {
		this.tickets = tickets;
	}
	public long getError() {
		return error;
	}
	public void setError(long error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "AgvUtil [id=" + id + ", disconnected=" + disconnected + ", working=" + working
				+ ", idle=" + idle + ", tickets=" + tickets + ", error=" + error + "]";
	}
	
	
	
}
