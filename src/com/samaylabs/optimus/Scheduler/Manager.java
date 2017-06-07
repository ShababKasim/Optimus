package com.samaylabs.optimus.Scheduler;

import com.samaylabs.optimus.Scheduler.Exceptions.AgvInManualModeExeception;
import com.samaylabs.optimus.Scheduler.Exceptions.NetworkDisconnectedException;
import com.samaylabs.optimus.Scheduler.Exceptions.OperationAbortedException;

public class Manager {

	private Tracker tracker;
	volatile private int status  = -1;

	Manager(Tracker tracker) {
		this.tracker = tracker;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Tracker getTracker() {
		return tracker;
	}

	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}

	public int execute()  {

		try {
			tracker.execute();
		} catch (NetworkDisconnectedException e) {
			return 0;
		} catch (AgvInManualModeExeception e) {
			return 0;
		} catch (OperationAbortedException e) {
			return 0;
		}
		return 1;
		
	}
}
