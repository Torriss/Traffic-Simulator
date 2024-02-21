package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	public void reduceTotalContamination() {
		int tc = this.getTotalCont(); int resta = 0;
		
		if(this.getWeather() == Weather.SUNNY) resta = 2;
		else if(getWeather() == Weather.CLOUDY) resta = 3;
		else if(getWeather() == Weather.RAINY) resta = 10;
		else if(getWeather() == Weather.WINDY) resta = 15;
		else if(getWeather() == Weather.STORM) resta = 20;
		
		int result = (100-resta)*tc/100;
		setCont(result);

	}

	@Override
	public void updateSpeedLimit() {
		if(getTotalCont() > getContAlarmLimit()) {
			int result = getMaxSpeed()/2;
			setCurrentSpeedLimit(result);
		}
		else setCurrentSpeedLimit(getMaxSpeed());
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) {
		if(getWeather()==Weather.STORM) return (getCurrentSpeedLimit()*8)/10;
		else return getCurrentSpeedLimit();
	}

}
