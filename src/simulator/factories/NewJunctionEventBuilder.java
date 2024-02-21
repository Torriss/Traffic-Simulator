package simulator.factories;

import org.json.JSONObject;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
	Factory<LightSwitchingStrategy> lssFactory;
	Factory<DequeuingStrategy> dqsFactory;
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy>lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		LightSwitchingStrategy lightSwitch = lssFactory.createInstance(data.getJSONObject("ls_strategy"));
		DequeuingStrategy dequeStrategy = dqsFactory.createInstance(data.getJSONObject("dq_strategy"));
		return new NewJunctionEvent(data.getInt("time"), data.getString("id"), lightSwitch, dequeStrategy, data.getJSONArray("coor").getInt(0), data.getJSONArray("coor").getInt(1));
	}
}
