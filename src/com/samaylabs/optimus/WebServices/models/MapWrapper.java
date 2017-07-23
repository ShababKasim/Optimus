package com.samaylabs.optimus.WebServices.models;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a Wrapper class to export Traffic manager map through web services.
 * @author Tulve Shabab Kasim
 *
 */
public class MapWrapper {

	    
    Map<Long, Integer> signals;

    /**
     * 
     */
    public MapWrapper() {
    	signals = new HashMap<Long,Integer>();
	}
    
    /**
     * Initilizes the map variable with param
     * @param map
     */
    public MapWrapper(Map<Long, Integer> map) {
		super();
		this.signals = map;
    }

	public Map<Long, Integer> getSignals() {
		return signals;
	}

	public void setSignals(Map<Long, Integer> signals) {
		this.signals = signals;
	}

}
