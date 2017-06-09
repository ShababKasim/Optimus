package com.samaylabs.optimus.WebServices;

import java.util.List;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.samaylabs.optimus.Communication.StationNode.models.Ticket;
import com.samaylabs.optimus.WebServices.models.AgvData;
import com.samaylabs.optimus.WebServices.models.ListWrapper;
import com.samaylabs.optimus.WebServices.models.MapWrapper;

@WebService(name="OptimusDefination",serviceName="OptimusService", portName="OptimusPort")
public class OptimusService {

	OptimusInitilizer init = new OptimusInitilizer();
	
	OptimusService(){
		Thread.currentThread().setName("OptimusService");
	}
	
	public ListWrapper startScheduler(){
		ListWrapper log = new ListWrapper();
		try{
			init.initAgv();
			log.add("Initilized Agv from Database");
		} catch (Exception e){
			log.add("Failled to initilize Agv Need to break, Exception in Creating Agv");
			return log;
		}
		try {
			init.startNodeServer();
			log.add("Node Server Started successfully.");
		} catch (Exception e) {
			log.add("Unable to Start Node Server");
		}
		try {
			init.startTrafficManager();
			log.add("Traffic Manager Started");
		} catch (Exception e) {
			log.add("Unable to Start Traffic Manager");
		}
		try {
			init.startScheduler();
			log.add("Started Scheduler, Check Logs for further Information");
		} catch (Exception e) {
			log.add("Unable to Start Scheduler");
		}
		return log;
	}
	
	public boolean stopNodeServer(){
		try {
			init.stopNodeServer();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean stopScheduler(){
		try {
			init.stopScheduler();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public ListWrapper powerStatus(){
		return init.powerStatus();
	}
	
	public int powerStatusKey(){
		return init.powerStatusKey();
	} 
	
	@WebMethod(action="insert_ticket")
	public boolean insertTicket(int Id, long  uid, int pdest, String type, String status) {
		Ticket t = new Ticket( Id,uid,pdest,type,status);
		init.getQueue().add(t);
		return true;
	}
	
	@WebMethod
	public boolean removeTicket(int index){
		return init.removeTicket(index);
	}
	
	@WebMethod
	public boolean alterTicket(int index1, int index2) {
		Ticket temp1 = init.getQueue().get(index1);
		Ticket temp2 = init.getQueue().get(index2);
		init.getQueue().set(index1, temp2);
		init.getQueue().set(index2, temp1);
		return true;
	}

	public Vector<Ticket> getQueue() {
		return init.getQueue();
	}
	
	public boolean setTicketToAgv(int index,int Agvno,String status){
		init.getQueue().get(index).setAgvno(Agvno);
		init.getQueue().get(index).setStatus(status);
		return true;
	}
	
	public List<AgvData> getAgvInfo(){
		return init.getAgvInfo();
	}
	
	public int getActiveNode(){
		return init.getActiveNode();
	}

	public ListWrapper areErrors(){
		return init.areErrors();
	}
	
	public boolean uiAbort(int id){
		return init.uiAbort(id);
	}
	
	public MapWrapper getActiveSignals(){
		return init.getActiveSignals();
	}
	
	public boolean isErrors(){
		return init.isErrors();
	}
}

