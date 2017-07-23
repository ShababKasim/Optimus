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
import com.samaylabs.optimus.Communication.StationNode.models.Ticket;
import com.samaylabs.optimus.Dao.DbLogger;
import com.samaylabs.optimus.Dao.TicketDao;
import com.samaylabs.optimus.Track.Dijkstra;
import com.samaylabs.optimus.Track.Path;
import com.samaylabs.optimus.Track.models.Node;
import com.samaylabs.optimus.Transport.Agv;


/**
 * This class implements Scheduling of tickets form sources to agvs. 
 * @author Tulve Shabab Kasim
 *
 */
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

	/**
	 * This method is used to add new agv while the program is running.
	 * @param agv Agv Object
	 */
	public void addNewAgv(Agv agv){
		agvs.add(agv);
		SchedulerService ss = new SchedulerService(agv, ticketCount, parkingStations, path);
		schedulerServices.add(ss);
		ss.start();
	}

	/**
	 * This method is called when scheduler is started, It creates Scheduler Service objects for each Agv in List.
	 */
	private void initSchedulerServices(){

		schedulerServices = new ArrayList<SchedulerService>();

		for(int i=0; i<agvs.size() ; i++) {
			schedulerServices.add(new SchedulerService(agvs.get(i), ticketCount, parkingStations, path));
		}
	}

	/**
	 * This method starts all the schduler service objects
	 */
	public void startSchedulerServices() {
		for(SchedulerService s: schedulerServices)
			s.start();
	}

	/**
	 * This method stops all the schduler service objects
	 */
	public void stopSchedulerServices() {
		for(SchedulerService s: schedulerServices){
			s.stopSchedulerService();
		}	
	}

	/**
	 * This method returns all the Agv of whcih available variable is true
	 * @return List of Agv
	 */
	private List<Agv> getAvailableAgvs(){
		List<Agv> temp = new ArrayList<Agv>();
		for(Agv agv : agvs){
			if(agv.getStateMachine().isAvailable())
				temp.add(agv);
		}
		return temp;
	}

	/**
	 * This method takes input as List of agvs and a Ticket Object, and based on current locations of both agvs
	 *  it decides which agv is nearest to serve that ticket and return that Agv
	 * @param agvs List of Agvs
	 * @param ticket Ticeket Object 
	 * @return Agv object optimal to server Ticket
	 */
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

	/**
	 * This method takes agv id as parameter, and checks ticket queue for ticket associated with this id, and returns that Ticket object
	 * @param Id Agv Id
	 * @return Ticket Object associated to Agv Id
	 */
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

	/**
	 * This Method fetches all available Agvs and stamps tickets with agv Id in FCFS manner
	 */
	private void ticketPoller() { 
		List<Agv> availableAgvs = getAvailableAgvs();
		if(availableAgvs.size() > 0) {
			outer : for(Agv agv : availableAgvs) {
				for(Ticket ticket : queue){
					if(ticket.getStatus().equals("Queued") && ticket.getAgvno() == agv.getAgvId()){
						continue outer;
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
	
	/**
	 * This method executes when ticket queue has a Ticket, finds best agv to that ticket and assigns.
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
								if(ss.getProvisionTicket().getType().equals("Parking")) {
									ss.abortTicket();
								}
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
						} else {
							ss.getAgv().getStateMachine().setShouldPark(false);
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
					for(NodeWorker nw : nodeWorkers) {
						if( nw.gettUid() == Uid) {
							if(ss.getServingTicket().getStatus().equals("Alloted") || ss.getServingTicket().getStatus().equals("Queued")){
								nw.setInputreq("booked");
							} else if(ss.getServingTicket().getStatus().equals("Picking Up")){
								nw.setInputreq("serving");
							} else{
								nw.setInputreq("drop");
							}
						}
					}
				}
			}
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				if(stop)
					this.interrupt();
			}
		}
		stopSchedulerServices();	
	}

	@Override
	public void run() {
		executor();
	}

}
