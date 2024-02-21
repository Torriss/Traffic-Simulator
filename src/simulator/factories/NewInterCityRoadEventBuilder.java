package simulator.factories;

import org.json.JSONObject;
import simulator.model.Event;
import simulator.model.NewCityRoadEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends Builder<Event> {

	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		Weather w = Weather.valueOf(data.getString("weather"));
		return new NewCityRoadEvent(data.getInt("time"), data.getString("id"), data.getString("src"), data.getString("dest"), data.getInt("length"), data.getInt("co2limit"), data.getInt("maxspeed"), w);
	}

}
