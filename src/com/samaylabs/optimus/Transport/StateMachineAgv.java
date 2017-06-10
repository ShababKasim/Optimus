package com.samaylabs.optimus.Transport;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.samaylabs.optimus.Communication.Plc.AgvMethods;
import com.samaylabs.optimus.Communication.StationNode.models.Ticket;
import com.samaylabs.optimus.Dao.DbLogger;
import com.samaylabs.optimus.Dao.TicketDao;
import com.samaylabs.optimus.Track.Dijkstra;
import com.samaylabs.optimus.Track.Path;
import com.samaylabs.optimus.Track.models.Milestone;
import com.samaylabs.optimus.Track.models.Node;
import com.samaylabs.optimus.Transport.models.TransferPacket;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;


public class StateMachineAgv {

	private final int Id;
	volatile private States state;
	protected States prevState;
	volatile private boolean available;
	volatile private boolean stopped;
	volatile private Node position;
	private String ipAddress;
	int logc1 = 0;

	volatile private List<Milestone> currentPath;
	volatile private int pathIndex = 0;

	volatile private boolean pathComplete;
	volatile private boolean work;
	volatile private boolean abort;
	volatile private boolean uiAbort;
	volatile private boolean networkDisconnect;
	volatile private boolean manualMode;
	volatile private boolean hooked;
	volatile private boolean jobDone;
	volatile private boolean lowCharging;
	volatile private Ticket currentTicket;
	volatile private boolean shouldPark;
	volatile private List<Long> reserveList;
	volatile private boolean error;
	volatile private String errorInfo;
	volatile private long nextAnchor = 0;
	protected States returnState;
	protected boolean jump = false;

	private final TrafficManager tManager;
	private AgvMethods agvMethods;
	private final Path Path;
	private final Dijkstra dijkstra;

	protected Logger log;


	StateMachineAgv(int Id,String ipAddress,int port,TrafficManager tmanager, Path path){
		this.Id = Id;
		this.ipAddress = ipAddress;
		this.tManager = tmanager;
		try {
			this.agvMethods = new AgvMethods(ipAddress,port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.Path = path;
		state = States.Connection;
		prevState = States.Connection;
		dijkstra = Path.getDijkstra();
		Long[] l = {(long)0,(long)0,(long)0,(long)0};
		reserveList =  new ArrayList<Long>(Arrays.asList(l));
	}

	public States getState() {
		return state;
	}

	public void setState(States state) {
		this.state = state;
	}

	public boolean isHooked() {
		return hooked;
	}

	public void setHooked(boolean hooked) {
		this.hooked = hooked;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isJobDone() {
		return jobDone;
	}

	public void setJobDone(boolean jobDone) {
		this.jobDone = jobDone;
	}

	public Node getPosition() {
		return position;
	}

	public Node getCurrentPosition() throws ModbusException{
		return  Path.getNodeById(agvMethods.getCurrentAnchor());
	}

	public void setPosition(Node position) {
		this.position = position;
	}

	public TrafficManager getTmanager() {
		return tManager;
	}

	public AgvMethods getAgvmethods() {
		return agvMethods;
	}

	public Path getPath() {
		return Path;
	}

	public long getNextAnchor() {
		return nextAnchor;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void setStop() {
		this.stopped = true;
	}

	public boolean isShouldPark() {
		return shouldPark;
	}

	public void setShouldPark(boolean shouldPark) {
		this.shouldPark = shouldPark;
	}

	public boolean isAbort() {
		return abort;
	}

	public void setAbort(boolean abort) {
		this.abort = abort;
	}

	public Ticket getCurrentTicket() {
		return currentTicket;
	}

	public void setCurrentTicket(Ticket currentTicket) {
		this.currentTicket = currentTicket;
	}

	public List<Long> getReserveList() {
		return reserveList;
	}

	public void setReserveList(List<Long> reserveList) {
		this.reserveList = reserveList;
	}

	public List<Milestone> getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(List<Milestone> currentPath) {
		this.currentPath = currentPath;
	}

	public int getPathIndex() {
		return pathIndex;
	}

	public void setPathIndex(int pathIndex) {
		this.pathIndex = pathIndex;
	}

	public boolean isWork() {
		return work;
	}

	public void setWork(boolean work) {
		this.work = work;
	}

	public boolean isUiAbort() {
		return uiAbort;
	}

	public void setUiAbort(boolean uiAbort) {
		this.uiAbort = uiAbort;
	}

	public boolean isLowCharging() {
		return lowCharging;
	}

	public void setLowCharging(boolean lowCharging) {
		this.lowCharging = lowCharging;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public boolean isNetworkDisconnect() {
		return networkDisconnect;
	}

	public void setNetworkDisconnect(boolean networkDisconnect) {
		this.networkDisconnect = networkDisconnect;
	}

	public boolean isManualMode() {
		return manualMode;
	}

	public void setManualMode(boolean manualMode) {
		this.manualMode = manualMode;
	}

	public int getId() {
		return Id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Id;
		result = prime * result + ((agvMethods == null) ? 0 : agvMethods.hashCode());
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof StateMachineAgv)) {
			return false;
		}
		StateMachineAgv other = (StateMachineAgv) obj;
		if (Id != other.Id) {
			return false;
		}
		if (agvMethods == null) {
			if (other.agvMethods != null) {
				return false;
			}
		} else if (!agvMethods.equals(other.agvMethods)) {
			return false;
		}
		if (ipAddress == null) {
			if (other.ipAddress != null) {
				return false;
			}
		} else if (!ipAddress.equals(other.ipAddress)) {
			return false;
		}
		return true;
	}

	public void resetAgv() throws ModbusIOException, ModbusSlaveException, ModbusException{
		agvMethods.resetAgv();
	}

	public void execute() {

		int retrycount = 0;
		log = new DbLogger().getLogger("Agv " + Id);
		main : while(!isStopped()){

			switch(state) {

			case Connection :
				log.info("Connection");
				this.available = false;
				error = true;
				try {
					agvMethods.connect();
					try{
						Long currentanchor = agvMethods.getCurrentAnchor();
						if(prevState != States.Connection) {
							log.info("Connection re-established");
							state = prevState;
							position = Path.getNodeById(currentanchor);
							reserveList.set(0,position.getAId());reserveList.set(1,position.getAId());
							reserveList.set(2,position.getAId());reserveList.set(3,position.getAId());
							agvMethods.setCurrentMove(false);
						} else {
							log.info("Connection : Connection established with Agv " + Id);
							state = States.Idle;
							logc1 = 1;
							position = Path.getNodeById(currentanchor);
							reserveList.set(0,position.getAId());reserveList.set(1,position.getAId());
							reserveList.set(2,position.getAId());reserveList.set(3,position.getAId());
							agvMethods.setCurrentMove(false);
						}
					} catch (NullPointerException e){
						errorInfo = "Place Agv on Anchor!";
					} catch(Exception e) {
						errorInfo = "Agv Disconnected";
						state = States.Connection;
						agvMethods.close();
						Thread.sleep(2000);
					}
				} catch (Exception e) {
					state = States.Connection;
					System.out.print(".");
					retrycount++;
					if(retrycount == 15){
						networkDisconnect = true;
						retrycount = 0;
					}
				}
				error = false;
				break;

			case Idle : 
				this.available = true;
				error = false;
				if(logc1 == 1){
					log.debug("Idle");
					logc1 = 0;
				}
				if(!work) {
					try {
						lowCharging = agvMethods.lowCharging();
						position = Path.getNodeById(agvMethods.getCurrentAnchor());
						reserveList.set(0,position.getAId());reserveList.set(1,position.getAId());
						reserveList.set(2,position.getAId());reserveList.set(3,position.getAId());
					} catch (Exception e) {
						prevState = state;
						state = States.Connection;
						log.error("Idle : Modbus exception  while getting charging info");
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				log.info("Idle : Starting working for ticket no:" + currentTicket.getTid() + " Agv on position: " + position.getAId());
				try {
					agvMethods.setInfoDest(currentTicket.getPdestination());
				} catch (Exception e3) {
					prevState = state;
					state = States.Connection;
					log.error("Idle : Modbus exception  while transfering destination info to HMI");
				}
				ticketResolver(currentTicket);	
				break; 


			case Pickup :
				log.debug("Pickup");
				this.available = false;
				log.info("Pickup : Moving agv");
				agvMover();
				if(pathComplete) {
					log.info("Pickup : Destination reached!");
					pathComplete = false;
					state = States.Drop;
					currentTicket.setStatus("Pickup-Complete");
					new TicketDao().updateStatus(currentTicket.getTid(), "Pickup-Complete");
				} else if(uiAbort){
					log.info("Pickup : Ticket Aborted from UI");
				}
				break;

			case Drop :
				log.debug("Drop");
				this.available = false;
				try {
					log.info("Drop : Hooking Agv.");
					while(!(agvMethods.isHooked() && agvMethods.dropAnchor() > 0)){
						agvMethods.startHooking();
						position = Path.getNodeById(agvMethods.getCurrentAnchor());
						if(uiAbort){
							agvMethods.resetAgv();
							state = States.Idle;
							logc1 = 1;
							new TicketDao().updateStatus(currentTicket.getTid(), "Aborted from UI");
							currentTicket.setStatus("Aborted from UI");
							continue main;
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							jobDone = true;
						}
					};     // Wait  until Agv get Hooked with Trolley
					log.info("Drop : Agv hooked");
				} catch (Exception e) {
					prevState = state;
					state = States.Connection;
					log.error("Drop : Modbus exception while getting hooking info");
					continue;
				}

				// Start : Path finding code Block
				if(!jump) {
					Node source = null;
					Node destination = null;

					try {
						source = this.getCurrentPosition();
						currentTicket.setSdestination((int)agvMethods.dropAnchor());
						new TicketDao().updateSdest(currentTicket.getTid() , (int)agvMethods.dropAnchor());
						agvMethods.setInfoDest(currentTicket.getSdestination());
						agvMethods.stopHooking();
						log.info("Drop : Secondary destination calculated, Destination is..> " + currentTicket.getSdestination());
					}  catch (Exception e2) {
						prevState = state;
						state = States.Connection;
						log.error("Drop : Modbus exception  while getting Drop Anchor info & Current Position");
						continue;
					}

					try{
						destination = Path.getNodeFromResolverById(currentTicket.getSdestination());
						this.currentPath = Path.resolveMilestone(Path.getShortestPath(source, destination,dijkstra));
					} catch (Exception e) {
						try {
							agvMethods.resetDropAnchor();
						} catch (Exception e1) {
							continue;
						}
						log.error("Drop : Invalid Destination given from HMI");
						continue;
					}

					log.info("Drop : Moving agv");
					currentTicket.setStatus("Serving Drop");
					new TicketDao().updateStatus(currentTicket.getTid(), "Serving Drop");
				}
				// End : Path finding code Block

				agvMover();
				if(!pathComplete){
					continue;
				} else if(uiAbort){
					log.info("TraveltoPark : Ticket Aborted from UI");
					continue;
				}
				jump = false;

				log.info("Drop : Destination reached");	
				currentTicket.setStatus("Drop Complete, Unhooking");
				try {
					log.info("Drop : UnHooking Agv");
					while(agvMethods.isHooked()){
						agvMethods.startHooking();
						position = Path.getNodeById(agvMethods.getCurrentAnchor());
						if(uiAbort){
							agvMethods.resetAgv();
							state = States.Idle;
							logc1 = 1;
							new TicketDao().updateStatus(currentTicket.getTid(), "Aborted from UI");
							currentTicket.setStatus("Aborted from UI");
							continue main;
						}
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							jobDone = true; // Interrupt will be externally given from Gui to stop this execution.
						}
					}    // Wait until get UnHooked info from HMI
					agvMethods.stopHooking();
					agvMethods.resetInfoDest();
				} catch (Exception e1) {
					prevState = state;
					state = States.Connection;
					log.error("Drop : Modbus exception  while getting hooking info");
					continue;
				}
				log.info("Drop : Pickup ticket " + currentTicket.getTid() + " served" );
				if(pathComplete){
					jobDone = true;
					log.info("Pickup : Destination reached");
					state = States.Idle;
					logc1 = 1;
				}
				new TicketDao().updateStatus(currentTicket.getTid(), "Success");
				currentTicket.setStatus("Success");
				break;

			case TravelToPark : 
				log.debug("Travelling to Park");
				this.available = true;
				log.info("TraveltoPark : Moving Agv");
				try {
					agvMethods.setInfoDest(currentTicket.getPdestination());
				} catch (Exception e) {
					prevState = state;
					state = States.Connection;
					log.error("Idle : Modbus exception  while transfering destination info to HMI");
				}
				agvMover();
				if(pathComplete){
					log.info("TraveltoPark : Destination reached");
					jobDone = true;
					state = States.Idle;
					logc1 = 1;
					pathComplete = false;
				} else if(abort){
					log.info("TraveltoPark : Ticket Aborted by Scheduler");
				} else if(uiAbort){
					log.info("TraveltoPark : Ticket Aborted from UI");
				} 
				/* A very very dirty patch to ensure that if parking misses go to idle, 
				 * Just because the paint is not proper in Plant*/
				if(state == States.Pause){
					log.info("TraveltoPark : Destination reached");
					state = States.Idle;
					logc1 = 1;
					jobDone = true;
					currentTicket.setStatus("Dropped, Parking missed");
					new TicketDao().updateStatus(currentTicket.getTid(), "Dropped, Parking missed");
				}
				/*Patch ends*/
				break;

			case TravelToCharge :
				log.debug("Travelling to Charge");
				this.available = false;
				log.info("TraveltoCharge : Moving Agv");
				try {
					agvMethods.setInfoDest(currentTicket.getPdestination());
				} catch (Exception e) {
					prevState = state;
					state = States.Connection;
					log.error("Idle : Modbus exception  while transfering destination info to HMI");
				}
				agvMover();
				if(pathComplete){
					log.info("TraveltoCharge : Destination reached");
					state = States.Charging;
					pathComplete = false;
				} else if(uiAbort){
					log.info("TraveltoPark : Ticket Aborted from UI");
				}
				break;

			case Charging :
				log.debug("Charging");
				this.available = false;
				try {
					if(agvMethods.isCharged()){
						state = States.Idle;
					} else {
						try{
							Thread.sleep(30000);
						} catch (InterruptedException e){
							jobDone = true;
						}
					}
				} catch (Exception e) {
					prevState = state;
					state = States.Connection;
				}
				break;

			case Pause :
				log.debug("Pause");
				this.available = false;
				error = true;
				errorInfo = "Agv Took wrong path";
				try {
					agvMethods.pauseScreen();
					if(agvMethods.isResume()){
						log.info(" Resumed from Hmi ");
						state = returnState;
						error = false;
						agvMethods.resetPauseScreen();
						continue;
					} else if(agvMethods.isAbort()){
						state = States.Idle;
						logc1 = 1;
						jobDone = true;
						agvMethods.resetAgv();
						new TicketDao().updateStatus(currentTicket.getTid(), "Aborted from HMI");
						currentTicket.setStatus("Aborted from HMI");
						error = false;
						continue;
					} else if(uiAbort){
						agvMethods.resetAgv();
						state = States.Idle;
						logc1 = 1;
						new TicketDao().updateStatus(currentTicket.getTid(), "Aborted from UI");
						currentTicket.setStatus("Aborted from UI");
					}
				} catch (Exception e) {
					prevState = state;
					state = States.Connection;
					log.error("Idle : Modbus exception  while transfering destination info to HMI");
				}
				break;

			case Notification :
				log.debug("Notification");
				this.available = false;
				error = true;
				try {
					if(!agvMethods.isObstacle()){
						log.info(" Obstacle Removed, Continuing ");
						state = returnState;
						error = false;
						continue;
					} else {
						errorInfo = "Obstacle error!";
					}
					if(!agvMethods.isOutOfLine()){
						log.info(" Resetted on Line, Continuing ");
						state = returnState;
						error = false;
						continue;
					} else {
						errorInfo = "Agv is Out of Line error!";
					} 
					if(uiAbort){
						agvMethods.resetAgv();
						state = States.Idle;
						logc1 = 1;
						new TicketDao().updateStatus(currentTicket.getTid(), "Aborted from UI");
						currentTicket.setStatus("Aborted from UI");
					}
				} catch (Exception e) {
					prevState = state;
					state = States.Connection;
					log.error("Idle : Modbus exception  while transfering destination info to HMI");
				}
				break;

			case Parking:
				log.debug("Parking");
				this.available = true;
				break;

			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		log.info("StateMachine Stopped!");
	}


	/**
	 * get the list of milestones from ticket and set the state based on ticket
	 * @param ticket from scheduler
	 */
	private final void ticketResolver(Ticket ticket){

		String type = ticket.getType();
		/* get the list of milestones from ticket and set the state based on ticket */

		Node source = getPosition(); 
		Node destination = Path.getNodeFromResolverById(currentTicket.getPdestination());

		if(source == null){
			work = false;
			log.info(state + " -->Resolver : Current value of anchor does not exists in defined map");
			return;
		}

		if(destination == null){
			work =false;
			log.info(state + " -->Resolver : destination value stated in ticket does not exists in defined map");
			return;
		} 

		currentTicket.setSource((int)source.getAId());
		currentTicket.setStatus("Serving");
		new TicketDao().updateSource(currentTicket.getTid(), source.getAId());
		new TicketDao().updateStatus(currentTicket.getTid(), "Serving");

		if(source.equals(destination)) {
			if(type == "Pickup") {
				state = States.Drop;
				work =false;
				log.info("Resolver : Already on destination");
				currentTicket.setStatus("Pickup Complete");
				return;
			} else if(type == "Parking") {
				state = States.Idle;
				logc1 = 1;
				work = false;
				log.info("Resolver : Already on destination");
				currentTicket.setStatus("Parking Complete");
				return;
			} else if(type == "Charging") {
				state = States.Charging;
				work = false;
				log.info("Resolver : Already on destination");
				currentTicket.setStatus("Charging Travel Complete");
				return;
			}
		}

		this.pathIndex = 0;
		this.currentPath = Path.resolveMilestone(Path.getShortestPath(source, destination, dijkstra));

		if(type == "Pickup"){
			state = States.Pickup;
		} else if(type == "Parking") {
			state = States.TravelToPark;
		} else if(type == "Charging") {
			state = States.TravelToCharge;
		}
		work = false;

	}

	public TransferPacket pathGiver() {

		/* current = sigs[1],
		 * next = sigs[2],
		 * prev = sigs[0],
		 * next2next = sigs[3];
		 */

		boolean cMove,nMove;

		reserveList.set(1, currentPath.get(pathIndex).getSource().getAId());
		reserveList.set(0, (pathIndex == 0) ? reserveList.get(1) : currentPath.get(pathIndex - 1).getSource().getAId());
		reserveList.set(2,(pathIndex == currentPath.size() - 1) ? reserveList.get(1) : currentPath.get(pathIndex + 1).getSource().getAId());
		reserveList.set(3,(pathIndex == currentPath.size() - 1 || pathIndex == currentPath.size() - 2 ) ? reserveList.get(2) : currentPath.get(pathIndex + 2).getSource().getAId());

		Milestone cMile = currentPath.get(pathIndex);
		Milestone nMile = (currentPath.size() - 1 == pathIndex) ? cMile : currentPath.get(pathIndex + 1) ;

		//		reserveList.removeAll(reserveList);
		//		reserveList.addAll(Arrays.asList(sigs));

		if(cMile.getAction() == 9)
			cMove = false;
		else {	
			cMove = !(tManager.isBlocked(reserveList.get(2),Id));
			//			System.out.println("Agv " + Id + "Cmove..> " + cMove);
		}
		nMove = !(tManager.isBlocked(reserveList.get(3),Id));

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return new TransferPacket(cMile.getSource().getAId(),cMile.getAction(),cMile.getType(),cMove,nMile.getSource().getAId(),nMile.getAction(),nMile.getType(),nMove);
	}

	public void agvMover() {

		long pid = 0;
		pathIterator : while( currentPath.get(currentPath.size()-1).getSource().getAId() != position.getAId() )  {

			/*Start: Check if Scheduler aborted ticket*/
			if(abort){
				try {
					agvMethods.setCurrentMove(false);
				} catch (Exception e) {
					prevState = state;
					state = States.Connection;
					log.error(this.state + " -->Mover : breaking main due to modbus exception while setting current move false made by scheduler");
					break pathIterator;
				}
				state = States.Idle;
				logc1 = 1;
				new TicketDao().updateStatus(currentTicket.getTid(), "Aborted by Scheduler");
				currentTicket.setStatus("Aborted by Scheduler");
				break pathIterator;
			}
			/*End: Check if Scheduler aborted ticket*/

			/*Start: Check if Scheduler aborted ticket*/
			if(uiAbort){
				try {
					agvMethods.setCurrentMove(false);
				} catch (Exception e) {
					prevState = state;
					state = States.Connection;
					log.error(this.state + " -->Mover : breaking main due to modbus exception while setting current move false made by UI");
					break pathIterator;
				}
				state = States.Idle;
				logc1 = 1;
				new TicketDao().updateStatus(currentTicket.getTid(), "Aborted from UI");
				currentTicket.setStatus("Aborted from UI");
				break pathIterator;
			}
			/*End: Check if Scheduler aborted ticket*/

			/*Start: Check if Agv Confronts Error like out of track and Obstacle*/
			try {
				if(agvMethods.isError()){
					returnState = state;
					pathComplete = false;
					state = States.Notification;
					break pathIterator;
				}
			} catch (Exception e2) {
				prevState = state;
				state = States.Connection;
				log.error(this.state + " --> Mover : breaking main due to modbus exception while getting Error info");
				pathComplete = false;
				break pathIterator;
			}
			/*End: Check if Agv Confronts Error like out of track and Obstacle*/

			try {
				position = Path.getNodeById(agvMethods.getCurrentAnchor());
			} catch (Exception e1) {
				prevState = state;
				state = States.Connection;
				log.error(this.state + " -->Mover : breaking main due to modbus exception while getting current position");
				pathComplete = false;
				break pathIterator;
			}

			/*Start: Check if Agv is on right Path*/
			int expectedAnchor = 0;
			for(Milestone m : currentPath) {
				if(m.getSource().equals(position)) {
					pathIndex = currentPath.indexOf(m);
					expectedAnchor = 1;
				}
			}

			if(expectedAnchor < 1) {
				try {
					agvMethods.setCurrentMove(false);
				} catch (Exception e) {
					prevState = state;
					state = States.Connection;
					log.error( this.state + " -->Mover :  modbus Exception while setting current move off due to unexpected path");
					pathComplete = false;
					break pathIterator;
				}
				if(state == States.Drop)
					jump = true;
				returnState = state;
				pathComplete = false;
				state = States.Pause;
				break pathIterator;
			}
			/*End: Check if Agv is on right Path*/



			/*Start transfer packet to Agv*/
			TransferPacket tp = pathGiver();
			nextAnchor = tp.getNextAnchor();
			try {
				agvMethods.setCurrentAnchor(tp.getCurrentAnchor());
				agvMethods.setCurrentTypeAction(tp.getCurrentType(), tp.getCurrentAction());
				agvMethods.setCurrentMove(tp.getcMove());
				agvMethods.setNextAnchor(tp.getNextAnchor());
				agvMethods.setNextTypeAction(tp.getNextType(), tp.getNextAction());
				agvMethods.setNextMove(tp.getnMove()); 
				agvMethods.setHeartbeat();
				if(pid != tp.getCurrentAnchor()){
					log.info(tp.toString());  // Conditional logging only after value change
					pid = tp.getCurrentAnchor();
				}
			} catch (Exception e) {
				prevState = state;
				state = States.Connection;
				log.info( this.state + " -->Mover : breaking main due to modbus exception! while transferring packet to agv");
				pathComplete = false;
				break pathIterator;
			}
			/*End transfer packet to Agv*/


			/*Start break after reaching final anchor*/
			if(pathIndex == currentPath.size()-1){
				pathComplete = true;
				break pathIterator;					// Condition to break after reaching final anchor 
			}
			/*End break after reaching final anchor*/
		}

	}

}

