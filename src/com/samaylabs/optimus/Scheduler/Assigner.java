package com.samaylabs.optimus.Scheduler;

import com.samaylabs.optimus.Communication.StationNode.models.Ticket;
import com.samaylabs.optimus.Transport.Agv;

public class Assigner {

	private Agv agv;
	private boolean stop;
	
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

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public int execute(Ticket ticket){

		agv.getStateMachine().setCurrentTicket(ticket);
		agv.getStateMachine().setWork(true);

		while(!isStop()) {

			if(agv.getStateMachine().isNetworkDisconnect()) {
				agv.getStateMachine().setJobDone(false);
				agv.getStateMachine().setNetworkDisconnect(false);
				return 3;
			} else if(agv.getStateMachine().isManualMode()) {
				agv.getStateMachine().setJobDone(false);
				return 2;
			} else if(agv.getStateMachine().isAbort() || agv.getStateMachine().isUiAbort()) {
				agv.getStateMachine().setUiAbort(false);
				agv.getStateMachine().setAbort(false);
				agv.getStateMachine().setJobDone(false);
				return 1;
			} else if(agv.getStateMachine().isJobDone()) {
				agv.getStateMachine().setJobDone(false);
				return 0;
			}
		}
		return 0;
	}

}


