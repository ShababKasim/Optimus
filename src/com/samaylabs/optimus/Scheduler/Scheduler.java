package com.samaylabs.optimus.Scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.samaylabs.optimus.Communication.StationNode.NodeWorker;
import com.samaylabs.optimus.Communication.StationNode.Ticket;
import com.samaylabs.optimus.Dao.DbLogger;
import com.samaylabs.optimus.Dao.TicketDao;
import com.samaylabs.optimus.Track.Dijkstra;
import com.samaylabs.optimus.Track.Node;
import com.samaylabs.optimus.Track.Path;
import com.samaylabs.optimus.Transport.Agv;

public class Scheduler extends Thread {


	private Vector<Ticket> queue;
	private List<Agv> agvs;
	private AtomicInteger ticketCount;
	volatile private boolean stop;

	private List<SchedulerService> schedulerServices;
	private Map<Node,Boolean> parkingStations;
	private Dijkstra dijkstra;
	protected Path path;
	protected List<NodeWorker> nodeWorkers;

	Logger log;

	public Scheduler(List<Agv> agvs, Vector<Ticket> queue, AtomicInteger ticketCount, Path path , Map<Node,Boolean> parkingStations) {
		super("Scheduler");
		this.agvs = agvs;
		this.queue = queue;
		this.ticketCount = ticketCount;
		this.path = path;
		this.parkingStations = parkingStations;

		dijkstra = path.getDijkstra();		

		initSchedulerServices();
	}

	public Scheduler(List<Agv> agvs, Vector<Ticket> queue, AtomicInteger ticketCount, Path path , Map<Node,Boolean> parkingStations, List<NodeWorker> nodeWorkers) {
		super("Scheduler");
		this.agvs = agvs;
		this.queue = queue;
		this.ticketCount = ticketCount;
		this.path = path;
		this.parkingStations = parkingStations;
		this.nodeWorkers = nodeWorkers;
		dijkstra = path.getDijkstra();		

		initSchedulerServices();
	}

	public List<SchedulerService> getSchedulerServices() {
		return schedulerServices;
	}

	public void setSchedulerServices(List<SchedulerService> schedulerServices) {
		this.schedulerServices = schedulerServices;
	}

	public AtomicInteger getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(AtomicInteger ticketCount) {
		this.ticketCount = ticketCount;
	}

	public List<Agv> getAgvs() {
		return agvs;
	}

	public Vector<Ticket> getQueue() {
		return queue;
	}

	public void setQueue(Vector<Ticket> queue) {
		this.queue = queue;
	}

	public boolean isStopped() {
		return stop;
	}

	public void stopScheduler() {
		this.stop = true;
	}

	public void addNewAgv(Agv agv){
		agvs.add(agv);
		SchedulerService ss = new SchedulerService(agv, ticketCount, parkingStations, path);
		schedulerServices.add(ss);
		ss.start();
	}

	private void initSchedulerServices(){

		schedulerServices = new ArrayList<SchedulerService>();

		for(int i=0; i<agvs.size() ; i++) {
			schedulerServices.add(new SchedulerService(agvs.get(i), ticketCount, parkingStations, path));
		}
	}

	public void startSchedulerServices() {
		for(SchedulerService s: schedulerServices)
			s.start();
	}

	public void stopSchedulerServices() {
		for(SchedulerService s: schedulerServices){
			s.stopSchedulerService();
		}	
	}

	/*public void executeRoundRobin() {

		int robinNo = agvs.size();
		//		startSchedulerServices();
		while(!stop) {
			robinNo = (robinNo %  agvs.size());
			try{
				Ticket servingTicket = queue.get(0);
				schedulerServices.get(robinNo++ % schedulerServices.size()).setServingTicket(servingTicket);
				queue.remove(0);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("No tickets in queue :(");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		//		stopSchedulerServices();
	}*/

	private List<Agv> getAvailableAgvs(){
		List<Agv> temp = new ArrayList<Agv>();
		for(Agv agv : agvs){
			if(agv.getStateMachine().isAvailable())
				temp.add(agv);
		}
		return temp;
	}

	private Agv getBestAgv(List<Agv> agvs, Ticket ticket){
		Map<Agv,Integer> distMap = new HashMap<Agv,Integer>();
		for(Agv agv : agvs){
			Node dest = path.getNodeFromResolverById(ticket.getPdestination());
			Node src = agv.getStateMachine().getPosition();
			int dist = path.getDistancebetweenNodes(src, dest, dijkstra);
			distMap.put(agv,dist);
		} 
		Entry<Agv, Integer> min = Collections.min(distMap.entrySet(), Comparator.comparingDouble(Entry::getValue));
		return min.getKey();
	} 


	/*private void getBestTicket(Agv agv){

		int ticketDepth = 1;
		Map<Ticket,Integer> distTickMap = new HashMap<Ticket,Integer>();

		for(Ticket ticket : queue){

			if(ticketDepth == 5)
				break;

			if(ticket.getStatus().equals("Unalloted")){
				Node src = agv.getStateMachine().getPosition();
				Node dest = path.getNodeFromResolverById(ticket.getPdestination());
				int dist = path.getDistancebetweenNodes(src, dest, dijkstra);
				distTickMap.put(ticket,dist);
				ticketDepth++;
			}
		}
		Ticket best = Collections.min(distTickMap.entrySet(), Comparator.comparingDouble(Entry::getValue)).getKey();
		for(Ticket ticket : queue){
			if(ticket.getStatus().equals("Unalloted")){
				if(ticket.getTid() == best.getTid()){
					ticket.setAgvno(agv.getAgvId());
					ticket.setStatus("Queued");
				}
			}
		}
	}*/

	private Ticket getFirstTicket(int Id){
		for(Ticket tick : queue){
			if(tick.getAgvno() == Id){
				Ticket tmp = tick;
				queue.remove(tick);
				return tmp;
			}
		}
		return null;
	}

	private void ticketPoller() { 
		List<Agv> availableAgvs = getAvailableAgvs();
		if(availableAgvs.size() > 0) {
			for(Agv agv : availableAgvs){
				for(Ticket ticket : queue){
					if(ticket.getStatus().equals("Queued") && ticket.getAgvno() == agv.getAgvId()){
						break;
					} else if(ticket.getStatus().equals("Unalloted")){
						ticket.setAgvno(getBestAgv(availableAgvs,ticket).getAgvId());
						ticket.setStatus("Queued");
						new TicketDao().updateAgvinfo(ticket.getTid(), ticket.getAgvno(), ticket.getStatus());
						break;
					}
				}
			} 
		} 
	} 

	/*private void ticketPoller() {
		List<Agv> availableAgvs = getAvailableAgvs();
		System.out.println("Available Agvs are : " + availableAgvs.size());
		for(Agv agv : availableAgvs){
			getBestTicket(agv);
		}
	}
	 */
	public void executor() {

		log = new DbLogger().getLogger("Scheduler");
		startSchedulerServices();

		int lc =0, lc1=0, lc2 = 0;
		while(!stop) {

			if(queue.size() > 0){
				lc = 0; 


				if(lc2 == 0){
					for(Ticket ticket : queue){
						log.info(ticket.toString());
					}
					lc2 = 1;
				}

				for(SchedulerService ss : schedulerServices) {

					if(ss.getAgv().getStateMachine().isAvailable()) {
						if(ss.getServingTicket() == null ) {
							ticketPoller();

							if( ss.getProvisionTicket() != null ){
								if(ss.getProvisionTicket().getType().equals("Parking"))
									ss.abortTicket();
							}

							Ticket toServeTicket = getFirstTicket(ss.getAgv().getAgvId());

							if(toServeTicket != null){
								lc1 = 0;
								toServeTicket.setStatus("Alloted");
								ss.setServingTicket(toServeTicket);
								log.info("Ticket " + toServeTicket.getTid() + " assigned to " + ss.getAgv().getAgvName());
							} else{
								if(lc1 == 0) {
									log.info( ss.getAgv().getAgvName() + " is Available but no ticket In queue");
									lc1 = 1;
								}
							}
						}
					}
				}
			} else {
				if(lc == 0){
					log.info("No tickets in queue");
					lc = 1;
				}
				lc2 = 0;
			}

			// Code to change states of Station Nodes
			for(SchedulerService ss : schedulerServices) {
				if(ss.getServingTicket() != null){
					long Uid = ss.getServingTicket().getUid();


					for(NodeWorker nw : nodeWorkers){
						if( nw.gettUid() != null && nw.gettUid() == Uid){
							if(!ss.getServingTicket().getStatus().equals("Serving")) 
								nw.setInputreq("drop");
							else{
								nw.setInputreq("serving");
								//								System.out.println("Setted " + nw.getInputreq());
							}
						}

						/*for(Ticket ticket : queue){
							if(ticket.getUid() == nw.gettUid() && ticket.getSdestination() < 1){
								continue;
							} else {
								nw.setInputreq("drop");
							}
						}*/
					}

				}
			}
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		stopSchedulerServices();	
	}

	@Override
	public void run() {
		executor();
	}

}
