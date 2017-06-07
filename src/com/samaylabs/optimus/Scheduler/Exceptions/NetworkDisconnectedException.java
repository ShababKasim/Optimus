package com.samaylabs.optimus.Scheduler.Exceptions;

public class NetworkDisconnectedException extends Exception{

	private static final long serialVersionUID = 1L;

	public NetworkDisconnectedException() {
		System.out.println("Took too long to reconnect with Agv!");
	}
	
	
}
