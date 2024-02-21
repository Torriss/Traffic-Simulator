package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	
	private List<Vehicle> vehicleList;
	private List<Road> roadList;
	private List<Junction> junctionList;
	private Map<String, Vehicle> vehicleMap;
	private Map<String, Road> roadMap;
	private Map<String, Junction> junctionMap;
	
	RoadMap() {
		junctionList = new ArrayList<Junction>();
		roadList = new ArrayList<Road>();
		vehicleList = new ArrayList<Vehicle>();
		junctionMap = new HashMap<String,Junction>();
		roadMap = new HashMap<String,Road>();
		vehicleMap = new HashMap<String,Vehicle>();
	}
	
	void addJunction(Junction j) {
		if((junctionMap.get(j.getId()) != null)) throw new IllegalArgumentException("j already exists");
		else {
			junctionList.add(j);
			junctionMap.put(j.getId(), j);
		}
	}
	
	void addRoad(Road r) {
		if(roadMap.get(r.getId()) != null) throw new IllegalArgumentException("r already exists");
		else {
			if(junctionMap.get(r.getDest().getId()) == null || junctionMap.get(r.getSrc().getId()) == null) throw new IllegalArgumentException("src or dest of r doesnt exists");
			else {
				roadList.add(r);
				roadMap.put(r.getId(), r);
				r.getDest().addIncommingRoad(r);
				r.getSrc().addOutGoingRoad(r);
			}
		}
	}
	
	void addVehicle(Vehicle v) {
		if(vehicleMap.get(v.getId()) != null) throw new IllegalArgumentException("v already exists");
		else {
			 List<Junction> itinerary = v.getItinerary();
			 for(int i = 0; i < itinerary.size()-1; i++) {
				 if(itinerary.get(i).roadTo(itinerary.get(i+1)) == null) {
					 throw new IllegalArgumentException("Itinerario invalido");
				 }
			 }
			 vehicleList.add(v);
			 vehicleMap.put(v.getId(), v);
		}
	}
	
	public Junction getJunction(String j) {
		return junctionMap.get(j);
	}
	
	public Road getRoad(String r) {
		return roadMap.get(r);
	}
	
	public Vehicle getVehicle(String v) {
		return vehicleMap.get(v);
	}
	
	public List<Junction>getJunctions(){
		return Collections.unmodifiableList(new ArrayList<Junction>(junctionList));
	}

	public List<Road>getRoads(){
		return Collections.unmodifiableList(new ArrayList<Road>(roadList));
	}
	
	public List<Vehicle>getVehicles(){
		return Collections.unmodifiableList(new ArrayList<Vehicle>(vehicleList));
	}
	 
	 void reset() {
		 vehicleList.clear();
		 roadList.clear();
		 junctionList.clear();
		 vehicleMap.clear();
		 roadMap.clear();
		 junctionMap.clear();
	 }
	 
	 public JSONObject report() {
		 JSONArray crucesjson = new JSONArray();
		 JSONArray carreterasjson = new JSONArray();
		 JSONArray vehiculosjson = new JSONArray();
		 JSONObject list = new JSONObject();
		 for(int i = 0; i < junctionList.size(); i++) crucesjson.put(junctionList.get(i).report());
		 for(int i = 0; i < roadList.size(); i++) carreterasjson.put(roadList.get(i).report()); 
		 for(int i = 0; i < vehicleList.size(); i++) vehiculosjson.put(vehicleList.get(i).report());
		 
		 list.put("junctions", crucesjson);
		 list.put("roads", carreterasjson);
		 list.put("vehicles", vehiculosjson);
		 
		 return list;
	 }
}
