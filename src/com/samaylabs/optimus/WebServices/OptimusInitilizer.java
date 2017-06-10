package com.samaylabs.optimus.WebServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

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

public class OptimusInitilizer {

	volatile  Vector<Ticket> queue;
	protected List<Agv> agvs;
	protected Map<Node,Boolean> parkingStations;
	protected List<NodeWorker> nodeWorkers;
	
	protected AtomicInteger ticketCount;
	protected Path path;
	protected NodeServer ns;
	protected TrafficManager tManager ;
	protected Scheduler scheduler;
	
	
	OptimusInitilizer(){
		queue = new Vector<Ticket>();
		agvs = new ArrayList<Agv>();
		ticketCount = new AtomicInteger();
		path = new Path();
		parkingStations = new HashMap<Node,Boolean>();
		nodeWorkers = new ArrayList<NodeWorker>();
		
		for(Node node : path.getParkingNodes()){
			parkingStations.put(node, true);
		}
	}

	public Vector<Ticket> getQueue() {
		return queue;
	}

	public MapWrapper getActiveSignals(){
		if(tManager != null)
			return new MapWrapper(tManager.getSignals());
		else
			return new MapWrapper();
	}
	
	public void initAgv() throws Exception{
		List<AgvData> agvList = new AgvDao().retriveAgv();
		tManager = new TrafficManager(path.getAnchorIds(), agvs, parkingStations);
		for(AgvData d : agvList){
			agvs.add(new Agv(d.getId(),d.getName(),d.getIpaddress(),d.getPort(),tManager,path));
		}
	}
		
	public int getActiveNode(){
		return nodeWorkers.size();
	}
	
	public void startNodeServer() throws Exception{
		ns = new NodeServer(9000,queue,ticketCount,nodeWorkers);
		ns.start();
	}
	
	public void stopNodeServer() throws Exception{
		ns.stopServer();
	}
	
	public void startTrafficManager() throws Exception {
		tManager.start();
	}
	
	public void startScheduler() throws Exception{
		scheduler = new Scheduler(agvs, queue, ticketCount, path, parkingStations,nodeWorkers);
		scheduler.start();
	}
	
	public void stopScheduler() throws Exception{
		if(!ns.isStopped())
			ns.stopServer();
		if(!tManager.isStop())
			tManager.stoptManager();
		if(!scheduler.isStopped())
			scheduler.stopScheduler();
		queue.removeAll(queue);
		ticketCount.set(0);
		for(Agv a : agvs) {
			a.getStateMachine().resetAgv();
			a.getStateMachine().setShouldPark(false);
			a.getStateMachine().setJobDone(true);
			a.interrupt();
			a.getStateMachine().setStop();
		}
		agvs.clear();
		new TicketDao().deleteAllTickets();
	}
	
	public ListWrapper powerStatus(){
		ListWrapper log = new ListWrapper();
		try {
			if(ns.isAlive())
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
		return log;
	}
	
	public int powerStatusKey(){
		try {
			if(!ns.isStopped() && !tManager.isStop() && !scheduler.isStopped())
				return 1;
			else
				return -1;
		} catch (Exception e) {
			return -1;
		}
	}
	
	public List<AgvData> getAgvInfo(){
		List<AgvData> data = new ArrayList<AgvData>();
		for(Agv agv : agvs){
			data.add(new AgvData(agv.getAgvId(), agv.getAgvName(), agv.getIpaddr(), agv.getPort(), agv.getStateMachine().getCurrentTicket(), agv.getStateMachine().getPosition()));
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
	
}
