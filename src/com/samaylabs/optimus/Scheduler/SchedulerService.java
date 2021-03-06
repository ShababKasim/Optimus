package com.samaylabs.optimus.Scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.samaylabs.optimus.Communication.StationNode.Ticket;
import com.samaylabs.optimus.Dao.DbLogger;
import com.samaylabs.optimus.Dao.TicketDao;
import com.samaylabs.optimus.Track.Dijkstra;
import com.samaylabs.optimus.Track.Node;
import com.samaylabs.optimus.Track.Path;
import com.samaylabs.optimus.Transport.Agv;

public class SchedulerService  extends Thread {

	private final Agv agv;
	private Mapper mapper;
	private  AtomicInteger ticketCount;
	volatile private boolean stop;
	volatile private Ticket servingTicket;
	volatile private Ticket provisionTicket;
	private Map<Node,Boolean> parkingStations;
	private Path path;
	private Dijkstra dijkstra;

	Logger log;

	public SchedulerService(Agv agv, AtomicInteger ticketCount, Map<Node,Boolean> parkingStations, Path path) {
		super("SService " + agv.getAgvId());
		this.agv = agv;
		this.ticketCount = ticketCount;
		this.mapper = new Mapper(agv);
		this.parkingStations = parkingStations;
		this.path = path;
		this.dijkstra = path.getDijkstra();
		servingTicket = null;
	}

	public AtomicInteger getTicketCount() {
		return ticketCount;
	}

	public boolean isStop() {
		return stop;
	}

	public Ticket getProvisionTicket() {
		return provisionTicket;
	}

	public void setProvisionTicket(Ticket provisionTicket) {
		this.provisionTicket = provisionTicket;
	}

	public void stopSchedulerService() {
		this.stop = true;
	}

	public Agv getAgv() {
		return agv;
	}

	public Mapper getMapper() {
		return mapper;
	}

	public Ticket getServingTicket() {
		return servingTicket;
	}

	public void setServingTicket(Ticket servingTicket) {
		this.servingTicket = servingTicket;
	}

	public void abortTicket(){
		agv.getStateMachine().setAbort(true);
	}

	@Override
	public void run() {

		log = new DbLogger().getLogger("SService " + this.agv.getAgvId());

		log.info("Starting Agv " + agv.getAgvId());
		agv.start();

		while(!stop) {

			if(agv.getStateMachine().isAvailable())  {


				if(servingTicket != null) {
					log.info("Got Ticket " + servingTicket.getTid() + " from scheduler, Assigned to Agv " + agv.getAgvId());
					doBusiness();
					log.info("Managed assigned ticket");
				}


				if(agv.getStateMachine().isLowCharging()) {
					int status = goCharging();  
					if(status == 2 || status == 1 ){
						//TODO code if parking fails and succeeds
					}

				} else if(agv.getStateMachine().isShouldPark()) {
					int status = goParking();
					if(status == 2 || status == 1 ){
						// Parking Failed or no Parking spots were available So below code will abort Parking.
						//						System.out.println(Thread.currentThread().getName() + "..> Parking Failed");
						agv.getStateMachine().setShouldPark(false);
					} else {
						agv.getStateMachine().setShouldPark(false);
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
		agv.stopAgv();
		log.info("Stopped Agv " + agv.getAgvId());
	}

	private long getUid(){
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddhhmmss");
		return Long.parseLong(formatter.format(date) + agv.getAgvId());
	}

	/**
	 * 
	 * @return 2.. if no parking available 1.. if parking fails 0.. if success
	 */
	private int goParking(){

		Node destin = getNearestParkingStation();
		if(destin == null)
			return 2;

		// Add a condition to check destination is valid, if its invalid getResolverIdByNode() will return -1 
		int tcount = ticketCount.incrementAndGet();
		Ticket ticket = new Ticket(tcount,getUid(),path.getResolverIdByNode(destin),"Parking","Alloted");      // Generate parking ticket

		provisionTicket = ticket;
		ticket.setType("Parking");
		ticket.setAgvno(agv.getAgvId());

		new TicketDao().insertTicket(ticket);
		new TicketDao().updateAgvinfo(ticket.getTid(), agv.getAgvId(), "Parking");
		log.info("Generating and Assigning parking ticket "+ tcount +" to Agv " + agv.getAgvId());

		int status = mapper.execute(ticket);

		setProvisionTicket(null);
		if(status == 0){
			return 1;
		}
		return 0;
	}

	private int doBusiness(){

		int status = mapper.execute(servingTicket);

		setServingTicket(null);

		if(status == 0)
			return 1;
		return 0;
	}

	private int goCharging(){

		Node destin = getNearestParkingStation();
		if(destin == null)
			return 2;
		int tcount = ticketCount.incrementAndGet();
		Ticket ticket = new Ticket(tcount,getUid(),path.getResolverIdByNode(destin));      // Generate charging ticket
		provisionTicket = ticket;
		ticket.setType("Charging");
		ticket.setAgvno(agv.getAgvId());
		new TicketDao().insertTicket(ticket);
		new TicketDao().updateAgvinfo(ticket.getTid(), agv.getAgvId(), "Charging");
		log.info("Generating and Assigning parking ticket "+ tcount +" to Agv " + agv.getAgvId());

		int status = mapper.execute(ticket);

		setProvisionTicket(null);
		if(status == 0){
			new TicketDao().updateStatus(ticket.getTid(), "Charging Failed");
			return 1;
		}
		return 0;
	}

	private Node getNearestParkingStation(){

		int freeParkCount = 0;
		Node unOccupied = null;
		List<Node> unoccupiedPark = new ArrayList<Node>();

		for(Map.Entry<Node, Boolean> entry : parkingStations.entrySet() ){
			if(entry.getValue()){
				freeParkCount++;
				unOccupied = entry.getKey();
				unoccupiedPark.add(entry.getKey());
			}
		}

		if(freeParkCount == 1){
			return unOccupied;
		} else {
			return getShortestParkingFromAvailable(unoccupiedPark);
		}
	}

	private Node getShortestParkingFromAvailable(List<Node> parkingNodes){
		Map<Node,Integer> distMap = new HashMap<Node,Integer>();
		for(Node node : parkingNodes){
			Node dest = node;
			Node src = agv.getStateMachine().getPosition();
			int dist = path.getDistancebetweenNodes(src, dest, dijkstra);
			distMap.put(node,dist);
		}
		Entry<Node, Integer> min = Collections.min(distMap.entrySet(), Comparator.comparingDouble(Entry::getValue));
		return min.getKey();
	}

}
