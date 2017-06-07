package com.samaylabs.optimus.Transport;

import com.samaylabs.optimus.Dao.AgvDao;
import com.samaylabs.optimus.Track.Path;

/**
 * 
 * @author Shabab
 *
 */
public class Agv extends Thread {

	private int Id;
	private String name;
	private String ipaddr;
	private int port;
	private StateMachineAgv stateMachine;

	public Agv(int Id,String name,String ipaddr,int port){
		this.Id = Id;
		this.name = name;
		this.ipaddr = ipaddr;
		this.port = port;
	}
	

	public Agv(int Id,String name,String ipaddr,int port, TrafficManager tmanager, Path path){
		super(name);
		this.Id = Id;
		this.name = name;
		this.port = port;
		this.ipaddr = ipaddr;
		setStateMachine(new StateMachineAgv(Id,ipaddr,port,tmanager,path));
//		agvDaoObj = new AgvDao();
//		agvDaoObj.insert( Id, name, ipaddr, port);
	}

	public int getAgvId() {
		return Id;
	}

	public String getIpaddr() {
		return ipaddr;
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

	public void stopAgv() {
		stateMachine.setStop();
	}

	public void setId(int id) {
		Id = id;
//		stateMachine.setid
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
		stateMachine.execute();
		new AgvDao().updateStatus(Id, false);
	}


}