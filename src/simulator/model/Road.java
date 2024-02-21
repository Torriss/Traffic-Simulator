package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject {
	
	private Junction srcJunction;
	private Junction destJunction;//circular es ir desde source hasta dest
	private int length;
	private int maxSpeed;
	private int currentSpeedLimit; //inicialmente igual a maxSpeed
	private int contAlarmLimit;
	private Weather weather;
	private int totalCont;
	private List<Vehicle> vehicles;

	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);
		totalCont = 0;
		vehicles = new ArrayList<Vehicle>();
		if(maxSpeed > 0) {this.maxSpeed = maxSpeed; currentSpeedLimit = maxSpeed;}
		else throw new IllegalArgumentException("the maxSpeed must be positive");
		if(contLimit >= 0) this.contAlarmLimit = contLimit;
		else throw new IllegalArgumentException("the contLimit must be positive");
		if(length > 0) this.length = length;
		else throw new IllegalArgumentException("the length must be positive");
		if(srcJunc == null || destJunc == null || weather == null) throw new IllegalArgumentException("srcJunc, destJunc and weather must not be null");
		else {srcJunction = srcJunc; destJunction = destJunc; this.weather = weather;}
	}

	@Override
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		for(int i = 0; i < vehicles.size(); i++) {
			vehicles.get(i).setSpeed(calculateVehicleSpeed(vehicles.get(i)));
			vehicles.get(i).advance(time);
		}
		VehicleComparator comparador = new VehicleComparator();
		vehicles.sort(comparador);
	} 

	@Override
	public JSONObject report() {
		JSONObject ob = new JSONObject();
		ob.put("id", this.getId());
		ob.put("speedlimit", currentSpeedLimit);
		ob.put("weather", weather.toString());
		ob.put("co2", totalCont);
		ob.put("vehicles", this.vehicles.toString());
		
		return ob;
	}

	public int getLength() {
		return length;
	}
	
	public Junction getDest() {
		return destJunction;
	}
	
	public Junction getSrc( ) {
		return srcJunction;
	}
	
	public int getTotalCont() {
		return totalCont;
	}
	
	public Weather getWeather() {
		return weather;
	}
	
	public int getContAlarmLimit() {
		return contAlarmLimit;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public int getCurrentSpeedLimit() {
		return currentSpeedLimit;
	}
	
	public List<Vehicle> getVehicles() {
		List<Vehicle> v = Collections.unmodifiableList(new ArrayList<>(vehicles));
		return v;
	}
	
	public void setCont(int totalCont) {
		this.totalCont = totalCont;
	}
	
	public void setCurrentSpeedLimit(int l) {
		currentSpeedLimit += l;
	}
	
	void enter(Vehicle v) {
		if(v.getCurrentSpeed() == 0 && v.getLocation() == 0) vehicles.add(v);
		else throw new IllegalArgumentException("CurrentSpeed and Location must be 0 to enter road");
	}
	
	void exit(Vehicle v) {
		vehicles.remove(v);
	}
	
	void setWeather(Weather w) {
		if(w == null) throw new IllegalArgumentException("W must not be null");
		else weather = w;
	}
	
	void addContamination(int c) {
		if(c < 0) throw new IllegalArgumentException("C must be positive");
		else totalCont += c;
	}
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle v);

}
