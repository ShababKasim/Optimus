package com.samaylabs.optimus.WebServices;

public class Gurdian extends Thread{

	protected OptimusService optimusService;

	public Gurdian(OptimusService optimusService) {
		this.optimusService = optimusService;
	}
	
	@Override
	public void run() {
		if(!optimusService.tManager.isAlive())
			optimusService.initilizeTrafficManager();
		if(!optimusService.nodeServer.isAlive())
			optimusService.initilizeNodeServer();
		if(!optimusService.scheduler.isAlive())
			optimusService.initilizeScheduler();
	}
	
	
}
