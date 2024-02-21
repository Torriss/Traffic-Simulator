package simulator.model;

import java.util.ArrayList;
import java.util.List;
import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {
	
	private List<Pair<String, Integer>> cs;
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) {
		super(time);
		if(cs == null) throw new IllegalArgumentException("cs must not be null");
		else this.cs = new ArrayList<Pair<String, Integer>>(cs);
	}

	@Override
	void execute(RoadMap map) {
		for(int i = 0; i < cs.size(); i++) {
			if(map.getVehicle(cs.get(i).getFirst()) == null) throw new IllegalArgumentException ("vehicle must not be null");
			else map.getVehicle(cs.get(i).getFirst()).setContamination(cs.get(i).getSecond());
		}
	}
}
