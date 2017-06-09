package com.samaylabs.optimus.Scheduler;

import com.samaylabs.optimus.Communication.StationNode.models.Ticket;
import com.samaylabs.optimus.Transport.Agv;

public class Mapper {

	volatile private Ticket ticket;
	private final Agv agv;
	volatile private int failSafe = -1;
	private Tracker tracker;
	private Manager manager;

	public Mapper(Agv agv ) {
		this.agv = agv;
	}

	public int isFailSafe() {
		return failSafe;
	}

	public void setFailSafe(int failSafe) {
		this.failSafe = failSafe;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Agv getAgv() {
		return agv;
	}

	public int execute(Ticket ticket) {

		tracker = new Tracker(agv);
		manager = new Manager(tracker);

		agv.getStateMachine().setCurrentTicket(ticket);
		agv.getStateMachine().setWork(true);

		int status = manager.execute();
		
		if(status == 0){
			return 0;
		}	
		return 1;
	}
}
