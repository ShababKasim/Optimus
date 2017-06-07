package com.samaylabs.optimus.Scheduler;

import com.samaylabs.optimus.Scheduler.Exceptions.AgvInManualModeExeception;
import com.samaylabs.optimus.Scheduler.Exceptions.NetworkDisconnectedException;
import com.samaylabs.optimus.Scheduler.Exceptions.OperationAbortedException;
import com.samaylabs.optimus.Transport.Agv;

public class Tracker {

	private Agv agv;

	Tracker(Agv agv) {
		this.setAgv(agv);
	}

	public Agv getAgv() {
		return agv;
	}

	public void setAgv(Agv agv) {
		this.agv = agv;
	}	

	public final void execute() throws NetworkDisconnectedException, AgvInManualModeExeception, OperationAbortedException {

		while(!agv.getStateMachine().isJobDone()) {

			if(agv.getStateMachine().isNetworkDisconnect()){
				agv.getStateMachine().setNetworkDisconnect(false);
				throw new NetworkDisconnectedException();
			} if(agv.getStateMachine().isManualMode()){
				throw new AgvInManualModeExeception();
			} if(agv.getStateMachine().isAbort()) {
				System.out.println("Ticket Aborted");
				throw new OperationAbortedException();
			}
		}
		agv.getStateMachine().setJobDone(false);
	}

}
