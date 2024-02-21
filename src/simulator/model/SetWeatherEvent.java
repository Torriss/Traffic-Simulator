package simulator.model;

import java.util.ArrayList;
import java.util.List;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
	
	private List<Pair<String, Weather>> ws;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		if(ws == null) throw new IllegalArgumentException("ws must not be null");
		else this.ws = new ArrayList<Pair<String, Weather>>(ws);
	}

	@Override
	void execute(RoadMap map) {
		for(int i = 0; i < ws.size(); i++) {
			if(map.getRoad(ws.get(i).getFirst()) == null) throw new IllegalArgumentException ("road must not be null");
			else map.getRoad(ws.get(i).getFirst()).setWeather(ws.get(i).getSecond());
		}
	}
}
