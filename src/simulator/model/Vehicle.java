package simulator.model;

import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Vehicle extends SimulatedObject {
	
	private List<Junction> itinerary;//cruces
	private int maxSpeed;
	private int currentSpeed;
	private VehicleStatus status;
	private Road road;
	private int location; //dist recorrida en carretera actual
	private int contClass; //0 al 10
	private int totalCont; //contaminacion total producida
	private int totalTravelDist; //distancia total recorrida
	
	Vehicle(String id, int maxSpeed, int contClass, List <Junction> itinerary) {
		super(id);
		totalTravelDist = 0;
		this.itinerary = new ArrayList<Junction>();
		if(maxSpeed > 0) this.maxSpeed = maxSpeed;
		else throw new IllegalArgumentException("the maxSpeed must be positive");
		if(contClass >= 0 && contClass <= 10) this.contClass = contClass;
		else throw new IllegalArgumentException("the contClass must be a number between 0 and 10, both inclusive");
		if(itinerary.size() < 2) throw new IllegalArgumentException("length of itinerary must be over 2");
		else this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		status = VehicleStatus.PENDING;
	}
	
	public int getTotalTravelDist() {
		return totalTravelDist;
	}
	
	public int getLocation() {
		return location;
	}
	
	public int getCurrentSpeed() {
		return currentSpeed;
	}
	
	public int getContClass() {
		return contClass;
	}
	
	public int getTotalCont() {
		return totalCont;
	}
	
	public Road getRoad() {
		return road;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public VehicleStatus getStatus() {
		return status;
	}
	
	public List<Junction> getItinerary() {
		return itinerary;
	}
	
	@Override
	void advance(int time) {
		if(status == VehicleStatus.TRAVELING) {
			int locPrevia = location;
			if((location + currentSpeed) < road.getLength()) {
				location += currentSpeed;
				totalTravelDist += currentSpeed;
			}
			else {
				location = road.getLength();
				totalTravelDist += (road.getLength() - locPrevia);
			}
			int contNueva = contClass * (location - locPrevia);
			totalCont += contNueva; road.addContamination(contNueva);
			if(location == road.getLength()) {
				road.getDest().enter(this);
				status = VehicleStatus.WAITING;
				currentSpeed = 0;
			}
		}
		else currentSpeed = 0;
	}

	@Override
	public JSONObject report() {
		JSONObject ob = new JSONObject();
		ob.put("id", this.getId());
		ob.put("speed", currentSpeed);
		ob.put("distance", totalTravelDist);
		ob.put("co2", totalCont);
		ob.put("class", contClass);
		ob.put("status", status.toString());
		if(status.equals(VehicleStatus.TRAVELING) || status.equals(VehicleStatus.WAITING)) {
			ob.put("road", road.getId());
			ob.put("location", location);
		}
	
		return ob;
	}
	
	void setSpeed(int s) {
		if(s >= 0) {
			if(s > maxSpeed) currentSpeed = maxSpeed;
			else currentSpeed = s;
		}
		else throw new IllegalArgumentException("s must be positive");
	}

	void setContamination(int c) {
		if(c >= 0 && c <= 10) contClass = c;
		else throw new IllegalArgumentException("c must be a number between 0 and 10");
	}
	
	void moveToNextRoad() {
		location=0;
		if(status != VehicleStatus.PENDING && status != VehicleStatus.WAITING) throw new IllegalArgumentException("Status must be WAITING OR PENDING");
		else {
			if(status == VehicleStatus.PENDING) {
				status = VehicleStatus.TRAVELING;
				road = itinerary.get(0).roadTo(itinerary.get(1));
				road.enter(this);
			}
			else {
				status=VehicleStatus.TRAVELING;
				road.exit(this);
				if(itinerary.indexOf(road.getDest()) == itinerary.size()-1) status = VehicleStatus.ARRIVED;
				else {
					currentSpeed = 0;
					road = itinerary.get(itinerary.indexOf(road.getDest())).roadTo(itinerary.get(itinerary.indexOf(road.getDest())+1));
					road.enter(this);
				}
			}
		}
	}
}
