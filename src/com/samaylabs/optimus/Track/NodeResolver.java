package com.samaylabs.optimus.Track;

public class NodeResolver {
	
	
	private int NRid;
	private long Aid;
	private String label;
	
	NodeResolver(int NRid, long Aid, String label) {
		this.NRid = NRid;
		this.Aid = Aid;
		this.label = label;
	}
	
	public long getAid() {
		return Aid;
	}
	public void setAid(long Aid) {
		this.Aid = Aid;
	}
	public int getNRid() {
		return NRid;
	}
	public void setNRid(int nRid) {
		NRid = nRid;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
}
