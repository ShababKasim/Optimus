package com.samaylabs.optimus.Scheduler.Exceptions;

public class AgvInManualModeExeception extends Exception{

	
	private static final long serialVersionUID = 1L;

	public AgvInManualModeExeception() {
		System.out.println("Agv switched to manual mode");
	}
	
}
