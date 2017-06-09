package com.samaylabs.optimus.Transport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.samaylabs.optimus.Dao.DbLogger;
import com.samaylabs.optimus.Track.models.Node;

/**
 * 
 * @author Shabab
 *
 */
public class TrafficManager extends Thread {


	private volatile Map<Long,Integer> signals = null;
	private boolean stop;
	private List<Agv> agvs;
	private Map<Node,Boolean> parkingStations;
	Logger log;

	public TrafficManager(List<Long> anchors) {
		signals = new HashMap<Long,Integer>();
		for(int i=0 ; i < anchors.size() ; i++){
			signals.put(anchors.get(i), -1);
		}
	}

	public TrafficManager(List<Long> anchors,List<Agv> agvs, Map<Node,Boolean> parkingStations) {
		super("Traffic Manager");
		signals = new Hashtable<Long,Integer>();
		this.agvs = agvs;
		this.parkingStations = parkingStations;
		for(int i=0 ; i < anchors.size() ; i++){
			signals.put(anchors.get(i), -1);
		}
	}

	public void stoptManager() {
		this.stop = true;
	}

	public int getSignal(Long id) {
		return signals.get(id);
	}

	public Map<Long, Integer> getSignals() {
		return signals;
	}

	public boolean isBlocked(Long id,int agvid) {
		return (signals.get(id) == -1) ? false : (signals.get(id) == agvid) ? false : true;
	}


	public void replaceSignal(Long id , int value) {
		signals.replace(id,value);
	}


	public void blockSignal(Long signal,int agvid) {
		signals.replace(signal,agvid);
	}


	public void releaseSignal(Long signal) {
		signals.replace(signal,-1);
	}


	public int getSignalSize() {
		return signals.size();
	}

	public List<Long> getReservedSignals(int agvId) {
		List<Long> list = new ArrayList<Long>();
		for(Map.Entry<Long, Integer> entry : signals.entrySet()){
			if(entry.getValue() == agvId) 
				list.add(entry.getKey());
		}
		return list;
	}

	public String printReservedSignals(int agvId) {
		StringBuffer sigs  = new StringBuffer();
		for(Map.Entry<Long, Integer> entry : signals.entrySet()){
			if(entry.getValue() == agvId) 
				sigs.append(agvId + "-->" + entry.getKey() + ","); 
		}
		return sigs.toString();
	}

	public int reserveMine(List<Long> anchors,int agvId) {
		if(anchors.size() > 0){
			releaseVisitedMine(anchors,agvId);
			for(Long l : anchors){
				if(signals.get(l) == -1 || signals.get(l) == agvId)
					signals.replace(l, agvId);
				else
					return signals.get(l);
			}
			/*for(Long l : signals.keySet()){
				if(anchors.contains(l))
					if(signals.get(l) == -1 || signals.get(l) == agvId)
						signals.replace(l, agvId);
					else{
						return signals.get(l);
					
					}
			}*/
		} else{
			releaseMine(agvId);
		}
		return -1;
	}

	public void releaseVisitedMine(List<Long> reserveList, int agvId) {
		for(Map.Entry<Long, Integer> entry : signals.entrySet()){
			if(entry.getValue() == agvId){
				if(!reserveList.contains(entry.getKey()))
					signals.replace(entry.getKey(), -1);
			}

		}
	}

	public void releaseMine(int agvId) {
		for(Map.Entry<Long, Integer> entry : signals.entrySet()){
			if(entry.getValue() == agvId)
				signals.replace(entry.getKey(), -1);
		}
	}

	private Agv getAgvById(int Id){
		for(Agv agv : agvs){
			if(agv.getAgvId() == Id)
				return agv;
		}
		return null;
	}

	@Override
	public void run(){
		log = new DbLogger().getLogger("Traffic Manager");
		log.info("Traffic Manager Started");
		while(!stop){
			try{
				for(Agv agv : agvs){
//					log.info(printReservedSignals(agv.getAgvId()));
					int res = reserveMine(agv.getStateMachine().getReserveList(),agv.getAgvId());
					if(res != -1){
						if(getAgvById(res).getStateMachine().isAvailable())
							getAgvById(res).getStateMachine().setShouldPark(true);
					}

					for(Node node : parkingStations.keySet()) {
						if(agv.getStateMachine().getPosition() != null)
							if(agv.getStateMachine().getPosition().equals(node))
								parkingStations.replace(node, true);
					}
				}
			}catch (Exception e){
				continue;
			}
		}
		log.info("Traffic Manager Stopped");
	}

	public boolean isStop() {
		return stop;
	}	
}
