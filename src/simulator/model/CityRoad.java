package simulator.model;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	public void reduceTotalContamination() {
		int resta;
		
		if(this.getWeather() == Weather.WINDY || this.getWeather() == Weather.STORM) resta = 10;
		else resta = 2;
		
		int result = getTotalCont() - resta;
		if(result < 0) result = 0;
		setCont(result);
	}

	@Override
	public void updateSpeedLimit() {
		// TODO Auto-generated method stub
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) {
		int result;
		
		int f = v.getContClass();
		int s = getCurrentSpeedLimit();
		result = (int)(((11.0-f )/11.0)*s);
		
		return result;
	}

}
