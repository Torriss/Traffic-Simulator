package simulator.model;

public class NewInterCityRoadEvent extends NewRoadEvent {

	public NewInterCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}

	@Override
	void execute(RoadMap map) {
		InterCityRoad r = new InterCityRoad(getId(), map.getJunction(getSrcJunc()), map.getJunction(getDestJunc()), getMaxSpeed(), getCo2Limit(), getLength(), getWeather());
		map.addRoad(r);
	}
	
	public String toString() {
		return "New InterCityRoad '" + super.getId() + "'";
	}
}
