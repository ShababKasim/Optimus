package com.samaylabs.optimus.Communication.StationNode;



import java.io.Serializable;
import java.util.Comparator;

/**
 * 
 * @Info This class is POJO class for Ticket structure 
 * @author Tulve Shabab Kasim
 *
 */
public class Ticket implements Comparator<Ticket>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 598282503306852583L;
	private int Tid;
	private long Uid;
	private int Source;
	private int Pdestination;
	private int Sdestination;
	private int Agvno;
	private String Type;
	private String Status;

	/**
	 * 
	 * @param Tid Id of ticket
	 * @param Uid TimeStamp of ticket
	 * @param Pdestination destination node Resolver Id
	 */
	public Ticket(int Tid,long Uid ,int Pdestination) {
		this.Tid = Tid;
		this.Uid = Uid;
		this.Pdestination = Pdestination;
	}

	/**
	 * 
	 * @param Tid Id of ticket
	 * @param Uid TimeStamp of ticket
	 * @param Pdestination destination node Resolver Id
	 * @param Type of ticket "Pickup" , "parking"
	 */
	public Ticket(int Tid,long Uid ,int Pdestination,String Type) {
		this.Tid = Tid;
		this.Uid = Uid;
		this.Pdestination = Pdestination;
		this.Type = Type;
	}
	
	/**
	 * 
	 * @param Tid Id of ticket
	 * @param Uid TimeStamp of ticket
	 * @param Pdestination destination node Resolver Id
	 * @param Type of ticket "Pickup" , "parking"
	 * @param Status of ticket
	 */
	public Ticket(int Tid,long Uid ,int Pdestination,String Type,String Status) {
		this.Tid = Tid;
		this.Uid = Uid;
		this.Pdestination = Pdestination;
		this.Type = Type;
		this.Status = Status;
	}
	
	/**
	 * 
	 * @return ticket id
	 */
	public int getTid() {
		return Tid;
	}
	/**
	 * 
	 * @param ticket id
	 */
	public void setTid(int tid) {
		Tid = tid;
	}
	
	/**
	 * 
	 * @return source value of ticket
	 */
	public int getSource() {
		return Source;
	}
	
	/**
	 * 
	 * @param source value of ticket
	 */
	public void setSource(int source) {
		Source = source;
	}
	
	/**
	 * 
	 * @return primary destination value of ticket
	 */
	public int getPdestination() {
		return Pdestination;
	}
	
	/**
	 * 
	 * @param pdestination value of a ticket
	 */
	public void setPdestination(int pdestination) {
		Pdestination = pdestination;
	}
	
	/**
	 * 
	 * @return Secondary destination value of a ticket
	 */
	public int getSdestination() {
		return Sdestination;
	}
	
	/**
	 * 
	 * @param sdestination value of a ticket
	 */
	public void setSdestination(int sdestination) {
		Sdestination = sdestination;
	}
	
	/**
	 * 
	 * @return Agv no assigned for this ticket
	 */
	public int getAgvno() {
		return Agvno;
	}
	
	/**
	 * 
	 * @param agvno to be assigned for this ticket
	 */
	public void setAgvno(int agvno) {
		Agvno = agvno;
	}

	/**
	 * 
	 * @return type of ticket eg. Pickup, Drop, Parking, Charging
	 */
	public String getType() {
		return Type;
	}
	/**
	 * 
	 * @param type of ticket eg. Pickup, Drop, Parking, Charging
	 */
	public void setType(String type) {
		this.Type = type;
	}
	
	/**
	 * 
	 * @return current status of the ticket
	 */
	public String getStatus() {
		return Status;
	}
	
	/**
	 * 
	 * @param status of this ticket
	 */
	public void setStatus(String status) {
		this.Status = status;
	}

	/**
	 * 
	 * @return Unique id of the ticket
	 */
	public long getUid() {
		return Uid;
	}

	/**
	 * 
	 * @param uid of the ticket
	 */
	public void setUid(long uid) {
		Uid = uid;
	}

	@Override
	public int compare(Ticket ticket1, Ticket ticket2) {
		return ticket1.getTid() - ticket2.getTid();
	}

	@Override
	public String toString() {
		return "Ticket [Tid=" + Tid + ", Uid=" + Uid + ", Pdestination=" + Pdestination + "]";
	}
}
