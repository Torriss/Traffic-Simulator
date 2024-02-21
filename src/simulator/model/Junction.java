package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
//import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
	
	private List<Road> enterRoads; //lista roads cuyo dest es this
	private Map<Junction, Road> exitRoads; //si existe par j,r es que this esta conectado a j mediante r
	private List<List<Vehicle>> queueList; //indice igual que enterRoads
	private Map<Road,List<Vehicle>> queueMap;
	private int indiceSemaforoVerde;
	private int ultPasoCambioSemaforo;
	private LightSwitchingStrategy cambioSemaforo;
	private DequeuingStrategy eliminarVehicles;
	private int x;
	private int y;
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);
		enterRoads = new ArrayList<Road>();
		exitRoads = new HashMap<Junction, Road>();
		queueList = new ArrayList<List<Vehicle>>();
		queueMap = new HashMap<Road, List<Vehicle>>();
		if(lsStrategy == null || dqStrategy == null) throw new IllegalArgumentException("Strategies must not be null");
		else {cambioSemaforo = lsStrategy; eliminarVehicles = dqStrategy;}
		if(xCoor < 0 || yCoor < 0) throw new IllegalArgumentException("xCoor and yCoor must be positive");
		else {x = xCoor; y = yCoor;}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getCurrentGreen() {
		return indiceSemaforoVerde;
	}
	
	public List<Road> getEnterRoads() {
		return enterRoads;
	}

	@Override
	void advance(int time) {
		if(indiceSemaforoVerde != -1 && queueList.size() > 0 && queueList.get(indiceSemaforoVerde).size() > 0) {
			List<Vehicle> l = eliminarVehicles.dequeue(queueList.get(indiceSemaforoVerde));
			for(Vehicle v: l) {
				v.moveToNextRoad();
				queueList.get(indiceSemaforoVerde).remove(v);
				Road r = enterRoads.get(indiceSemaforoVerde);
				queueMap.get(r).remove(v);
			}
		}
		int nuevoInd = cambioSemaforo.chooseNextGreen(enterRoads, queueList, indiceSemaforoVerde, ultPasoCambioSemaforo, time);
		if(nuevoInd != indiceSemaforoVerde) {
			ultPasoCambioSemaforo = time;
			indiceSemaforoVerde = nuevoInd;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject ob = new JSONObject();
		ob.put("id", this.getId());
		if(indiceSemaforoVerde != -1) ob.put("green", enterRoads.get(indiceSemaforoVerde).getId());
		else ob.put("green", "none");
		JSONArray ar = new JSONArray();
		for(int i = 0; i < queueList.size(); i++) {
			JSONObject obj = new JSONObject();
			obj.put("road", enterRoads.get(i).getId());
			JSONArray arr = new JSONArray();
			for(int j = 0; j < queueList.get(i).size(); j++) {
				arr.put(queueList.get(i).get(j).getId());
			}
			obj.put("vehicles", arr);
			ar.put(obj);
		}
		ob.put("queues", ar);

		return ob;
	}
	
	
	void addIncommingRoad(Road r) {
		if(r.getDest() != this) throw new IllegalArgumentException("DestJunction of r must be this junction");
		else {
			enterRoads.add(r);
			List<Vehicle> colaVehicles = new LinkedList<Vehicle>();
			queueList.add(colaVehicles);
			queueMap.put(r, colaVehicles);
		}
	}
	
	void addOutGoingRoad(Road r) {
		if(r.getSrc() != this) throw new IllegalArgumentException("SrcJunction of r must be this");
		else if(exitRoads.get(r.getDest()) != null) throw new IllegalArgumentException("There is already a road with that junction as a destination");
		exitRoads.put(r.getDest(),r);
	}
	
	void enter(Vehicle v) {
		Road r = v.getRoad();
		queueMap.get(r).add(v);
		int i = enterRoads.indexOf(r);
		queueList.get(i).add(v);
	}
	
	Road roadTo(Junction j) {
		return exitRoads.get(j);
	}
}
