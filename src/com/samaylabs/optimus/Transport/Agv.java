package com.samaylabs.optimus.Transport;

import com.samaylabs.optimus.Dao.AgvDao;
import com.samaylabs.optimus.Track.Path;

/**
 * This is threaded class which initilizes statemachine and executes it.
 * @author Tulve Shabab Kasim
 *
 */
public class Agv extends Thread {

	private int Id;
	private String name;
	private String ipaddr;
	private int port;
	private boolean stop;
	private StateMachineAgv stateMachine;

	/**
	 * Constructor with input parameters
	 * @param Id of Agv 
	 * @param name of Agv
	 * @param ipaddr of Agv
	 * @param port of Agv
	 */
	public Agv(int Id,String name,String ipaddr,int port){
		this.Id = Id;
		this.name = name;
		this.ipaddr = ipaddr;
		this.port = port;
	}
	
	/**
	 * Constructor with input parameters
	 * @param Id of Agv 
	 * @param name of Agv
	 * @param ipaddr of Agv
	 * @param port of Agv
	 * @param tmanager Traffic Manager Singleton Object 
	 * @param path Path Object
	 */
	public Agv(int Id,String name,String ipaddr,int port, TrafficManager tmanager, Path path){
		super(name);
		this.Id = Id;
		this.name = name;
		this.port = port;
		this.ipaddr = ipaddr;
		setStateMachine(new StateMachineAgv(Id,ipaddr,port,tmanager,path));
	}

	public int getAgvId() {
		return Id;
	}

	public String getIpaddr() {
		return ipaddr;
	}
	
	public boolean isStop() {
		return stop;
	}


	public void setStop(boolean stop) {
		this.stop = stop;
	}


	public int getPort() {
		return port;
	}
	
	public StateMachineAgv getStateMachine() {
		return stateMachine;
	}

	public void setStateMachine(StateMachineAgv stateMachine) {
		this.stateMachine = stateMachine;
	}

	/**
	 * this method makes the stop variables of Agv and StateMachine false to stop its loop.
	 */
	public void stopAgv() {
		stateMachine.setStop();
		this.stop = true;
		this.interrupt();
	}

	public void setId(int id) {
		Id = id;
	}

	public String getAgvName() {
		return name;
	}

	public void setAgvName(String name) {
		this.name = name;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
		stateMachine.setIpAddress(ipaddr);
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void run() {
		new AgvDao().updateStatus(Id, true);
		while(!isStop()){
			try{
				stateMachine.execute();
			} catch(Exception e){
				continue;
			}
		}
		new AgvDao().updateStatus(Id, false);
	}


}