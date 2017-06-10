package com.samaylabs.optimus.Scheduler;

import com.samaylabs.optimus.Communication.StationNode.models.Ticket;
import com.samaylabs.optimus.Transport.Agv;

public class Assigner {

	private Agv agv;

	public Assigner(Agv agv) {
		super();
		this.agv = agv;
	}

	public Agv getAgv() {
		return agv;
	}

	public void setAgv(Agv agv) {
		this.agv = agv;
	}

	public int execute(Ticket ticket){

		agv.getStateMachine().setCurrentTicket(ticket);
		agv.getStateMachine().setWork(true);

		while(!agv.getStateMachine().isJobDone()) {

			if(agv.getStateMachine().isNetworkDisconnect()){
				agv.getStateMachine().setNetworkDisconnect(false);
				return 3;
			} if(agv.getStateMachine().isManualMode()){
				return 2;
			} if(agv.getStateMachine().isAbort()) {
				return 1;
			}
		}
		agv.getStateMachine().setJobDone(false);
		return 0;
	}

}


