package simulator.factories;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		List<Pair<String, Integer>> cs = new ArrayList<Pair<String, Integer>>();
		for(int i = 0; i < data.getJSONArray("info").length(); i++) {
			cs.add(new Pair<String, Integer>(data.getJSONArray("info").getJSONObject(i).getString("vehicle"), data.getJSONArray("info").getJSONObject(i).getInt("class")));
		}
		return new NewSetContClassEvent(data.getInt("time"), cs);
	}

}
