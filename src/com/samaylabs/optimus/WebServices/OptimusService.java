package com.samaylabs.optimus.WebServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.samaylabs.optimus.Communication.StationNode.NodeServer;
import com.samaylabs.optimus.Communication.StationNode.NodeWorker;
import com.samaylabs.optimus.Communication.StationNode.models.Ticket;
import com.samaylabs.optimus.Dao.AgvDao;
import com.samaylabs.optimus.Dao.TicketDao;
import com.samaylabs.optimus.Scheduler.Scheduler;
import com.samaylabs.optimus.Track.Path;
import com.samaylabs.optimus.Track.models.Node;
import com.samaylabs.optimus.Transport.Agv;
import com.samaylabs.optimus.Transport.TrafficManager;
import com.samaylabs.optimus.WebServices.models.AgvData;
import com.samaylabs.optimus.WebServices.models.ListWrapper;
import com.samaylabs.optimus.WebServices.models.MapWrapper;

@WebService(name="OptimusDefination",serviceName="OptimusService", portName="OptimusPort")
public class OptimusService {

	volatile  Vector<Ticket> queue;
	protected List<Agv> agvs;
	protected Map<Node,Boolean> parkingStations;
	protected List<NodeWorker> nodeWorkers;
	
	protected AtomicInteger ticketCount;
	protected Path path;
	protected NodeServer nodeServer;
	protected TrafficManager tManager ;
	protected Scheduler scheduler;
	private boolean power;
	private Gurdian gurdian;
	
	
	public OptimusService(){
		this.queue = new Vector<Ticket>();
		this.agvs = new ArrayList<Agv>();
		this.ticketCount = new AtomicInteger();
		this.path = new Path();
		this.parkingStations = new HashMap<Node,Boolean>();
		this.nodeWorkers = new ArrayList<NodeWorker>();
		this.gurdian = new Gurdian(this);
		
		for(Node node : path.getParkingNodes()){
			parkingStations.put(node, true);
		}
		gurdian.start();
	}

	@WebMethod(exclude=true)	
	public Gurdian getGurdian() {
		return gurdian;
	}

	@WebMethod(exclude=true)
	public void setGurdian(Gurdian gurdian) {
		this.gurdian = gurdian;
	}

	public boolean isPower() {
		return power;
	}

	@WebMethod(exclude=true)
	public void setPower(boolean power) {
		this.power = power;
	}

	/**Methods related to Powering On and Off optimus**/
	
	@WebMethod(exclude=true)
	public void initilizeAll() {
		List<AgvData> agvList = new AgvDao().retriveAgv();
		tManager = new TrafficManager(path.getAnchorIds(), agvs, parkingStations);
		for(AgvData d : agvList){
			agvs.add(new Agv(d.getId(),d.getName(),d.getIpaddress(),d.getPort(),tManager,path));
		}
		nodeServer = new NodeServer(9000,queue,ticketCount,nodeWorkers);
		scheduler = new Scheduler(agvs, queue, ticketCount, path, parkingStations,nodeWorkers);
	}
	
	@WebMethod(exclude=true)
	public void initilizeTrafficManager(){
		tManager = new TrafficManager(path.getAnchorIds(), this.agvs, this.parkingStations);
		tManager.start();
	}
	
	@WebMethod(exclude=true)
	public void initilizeScheduler(){
		scheduler = new Scheduler(agvs, queue, ticketCount, path, parkingStations,nodeWorkers);
		scheduler.start();
	}
	
	@WebMethod(exclude=true)
	public void initilizeNodeServer(){
		nodeServer = new NodeServer(9000,queue,ticketCount,nodeWorkers);
		nodeServer.start();
	}
	
	public ListWrapper InitilizeAndStart(){
		ListWrapper log = new ListWrapper();
		if(power == false){
			initilizeAll();
			try {
				nodeServer.start();
				log.add("Node Server Started successfully.");
			} catch (Exception e) {
				log.add("Unable to Start Node Server");
			}
			try {
				tManager.start();
				log.add("Traffic Manager Started");
			} catch (Exception e) {
				log.add("Unable to Start Traffic Manager");
			}
			try {
				scheduler.start();
				log.add("Started Scheduler, Check Logs for further Information");
			} catch (Exception e) {
				log.add("Unable to Start Scheduler");
			}
			power = true;
			if(gurdian.getState() == Thread.State.WAITING)
				gurdian.notify();
		} else {
			log = powerStatus();
		}
		return log;
	}
	
	public boolean stopAndDeinitilize() throws Exception {
		if(power == true){
			if(nodeServer.isAlive()){
				nodeServer.stopServer();
				
			}
			if(tManager.isAlive()){
				tManager.stoptManager();
			}
			if(scheduler.isAlive()){
				scheduler.stopScheduler();
			}	
			for(Agv a : agvs) {
				a.stopAgv();
			}
			nodeWorkers.clear();
			agvs.clear();
			queue.clear();
			ticketCount.set(0);
			new TicketDao().backupAndDeleteTickets();
			power = false;
			if(gurdian.getState() == Thread.State.RUNNABLE)
				gurdian.wait();
		}
		return true;
	}
	
	/**End : Methods related to Powering On and Off optimus**/
	
	
	/**Methods related to TrafficManager and Power**/
	
	public MapWrapper getActiveSignals(){
		if(tManager != null)
			return new MapWrapper(tManager.getSignals());
		else
			return new MapWrapper();
	}

	public int getActiveNode(){
		return nodeWorkers.size();
	}
	
	public ListWrapper powerStatus(){
		ListWrapper log = new ListWrapper();
		try {
			if(nodeServer.isAlive())
				log.add("Node Server: ON");
			else
				log.add("Node Server: OFF");
		} catch (Exception e) {
			log.add("Node Server: OFF");
		}
		try {
			if(tManager.isAlive())
				log.add("Traffic Manager: ON");
			else
				log.add("Traffic Manager: OFF");
		} catch (Exception e) {
			log.add("Traffic Manager: OFF");
		}
		try {
			if(scheduler.isAlive())
				log.add("Scheduler : ON");
			else
				log.add("Scheduler : OFF");
		} catch (Exception e) {
			log.add("Scheduler : OFF");
		}
		for(Agv agv : agvs){
			if(agv.isAlive())
				log.add(agv.getAgvName() + " : ON");
			else
				log.add(agv.getAgvName() + " : OFF");
		}
		return log;
	}
	
	public int powerStatusKey(){
		return power ? 1 : -1;
	}
	
	
	/**End : Methods related to TrafficManager and Power**/
	
	/**Methods related to Agv**/

	public List<AgvData> getAgvInfo(){
		List<AgvData> data = new ArrayList<AgvData>();
		for(Agv agv : agvs){
			/*if(agv.getStateMachine().getCurrentTicket().getStatus() == "Success" || agv.getStateMachine().getCurrentTicket().getStatus() == "Aborted from UI"
					|| agv.getStateMachine().getCurrentTicket().getStatus() == "Aborted by Scheduler"
					|| agv.getStateMachine().getCurrentTicket().getStatus() == "Failed"){
				data.add(new AgvData(agv.getAgvId(), agv.getAgvName(), agv.getIpaddr(), agv.getPort(), null, agv.getStateMachine().getPosition()));
			} else{*/
				data.add(new AgvData(agv.getAgvId(), agv.getAgvName(), agv.getIpaddr(), agv.getPort(), agv.getStateMachine().getCurrentTicket(), agv.getStateMachine().getPosition()));
//			} 
			
		}
		return data;
	}
	
	public ListWrapper areErrors(){
		ListWrapper log = new ListWrapper();
		for(Agv agv : agvs){
			if(agv.getStateMachine().isError()){
				log.add(agv.getAgvName() + "-> InActive : " + agv.getStateMachine().getErrorInfo());
			} else {
				log.add(agv.getAgvName() + "-> Active");
			}
		}	
		return log;
	}
	
	public boolean isErrors(){
		boolean temp = false;
		for(Agv agv : agvs){
			if(agv.getStateMachine().isError()){
				temp = true;
			} 
		}	
		return temp;
	}
	
	public boolean uiAbort(int id){
		Long uid = (long)0;
		for(Agv a : agvs){
			if(a.getAgvId() == id){
				uid = a.getStateMachine().getCurrentTicket().getUid();
				a.getStateMachine().setUiAbort(true);
				return true;
			}	
		}
		for(NodeWorker nw : nodeWorkers){
			if( nw.gettUid() != null && nw.gettUid() == uid){
				nw.setInputreq("drop");
			}
		}
		return false;
	}

	/**End : Methods related to Agv**/
	
	
	/**Methods related with queue and ticket**/
	
	public Vector<Ticket> getQueue() {
		return queue;
	}

	public boolean removeTicket(int index){
		int id = queue.get(index).getTid();
		Long uid = (long)queue.get(index).getUid();
		new TicketDao().updateStatus(id, "Deleted");
		queue.remove(index);
		
		for(NodeWorker nw : nodeWorkers){
			if( nw.gettUid() != null && nw.gettUid() == uid){
				nw.setInputreq("drop");
			}
		}
		return true;
	}
	
	public boolean alterTicket(int index1, int index2) {
		Ticket temp1 = queue.get(index1);
		Ticket temp2 = queue.get(index2);
		queue.set(index1, temp2);
		queue.set(index2, temp1);
		return true;
	}
	
	public boolean setTicketToAgv(int index,int Agvno,String status){
		queue.get(index).setAgvno(Agvno);
		queue.get(index).setStatus(status);
		return true;
	}

	public boolean insertTicket(int Id, long  uid, int pdest, String type, String status) {
		Ticket t = new Ticket( Id,uid,pdest,type,status);
		queue.add(t);
		return true;
	}
	
	/**End : Methods related with queue and ticket**/
}
