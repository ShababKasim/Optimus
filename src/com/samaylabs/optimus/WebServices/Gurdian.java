package com.samaylabs.optimus.WebServices;

public class Gurdian extends Thread{

	protected OptimusService optimusService;
	private boolean stop;
	
	public Gurdian(OptimusService optimusService) {
		this.optimusService = optimusService;
		Thread.currentThread().setName("Gurdian");
	}
	
	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	@Override
	public void run() {
		while(!isStop()){
			if(optimusService.isPower()){
				if(!optimusService.tManager.isAlive())
					optimusService.initilizeTrafficManager();
				if(!optimusService.nodeServer.isAlive())
					optimusService.initilizeNodeServer();
				if(!optimusService.scheduler.isAlive())
					optimusService.initilizeScheduler();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
