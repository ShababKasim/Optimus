package com.samaylabs.optimus.Track.models;


/**
 * Model to hold NodeResolver info from database, use method is prototype
 * @author Tulve Shabab Kasim
 *
 */
public class NodeResolver {
	
	
	private int NRid;
	private long Aid;
	private String label;
	
	/**
	 * 
	 * @param NRid
	 * @param Aid
	 * @param label
	 */
	public NodeResolver(int NRid, long Aid, String label) {
		this.NRid = NRid;
		this.Aid = Aid;
		this.label = label;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getAid() {
		return Aid;
	}
	/**
	 * 
	 * @param Aid
	 */
	public void setAid(long Aid) {
		this.Aid = Aid;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNRid() {
		return NRid;
	}
	
	/**
	 * 
	 * @param nRid
	 */
	public void setNRid(int nRid) {
		NRid = nRid;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
}
