package com.samaylabs.optimus.WebServices;

import java.util.HashMap;
import java.util.Map;

public class MapWrapper {

//	@XmlJavaTypeAdapter(MapAdapter.class)    
    Map<Long, Integer> signals;

    public MapWrapper() {
    	signals = new HashMap<Long,Integer>();
	}
    
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
