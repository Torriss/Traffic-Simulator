package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class TrafficSimulator implements Observable<TrafficSimObserver> {
	
	private RoadMap mapaCarreteras;
	private List<Event> listaEventos;
	private int tiempo;
	private List<TrafficSimObserver> observers;
	
	public TrafficSimulator() {
		mapaCarreteras = new RoadMap();
		listaEventos = new ArrayList<Event>();
		tiempo = 0;
		this.observers = new ArrayList<TrafficSimObserver>();
	}
	
	public void addEvent(Event e) {
		listaEventos.add(e);
		for(int i = 0; i < observers.size(); i++) {
			observers.get(i).onEventAdded(mapaCarreteras, listaEventos, e, tiempo);
		}
	}
	
	public void advance() {
		tiempo++; 
		int lim = listaEventos.size();
		for(int i = 0; i < observers.size(); i++) {
			observers.get(i).onAdvanceStart(mapaCarreteras, listaEventos, tiempo);
		}
		for(int i = 0; i < lim; i++) {
			if(listaEventos.get(0)._time == tiempo) { 
				listaEventos.get(0).execute(mapaCarreteras);
				listaEventos.remove(0);
			}	
		}
		for(int i = 0; i < mapaCarreteras.getJunctions().size(); i++) {
			mapaCarreteras.getJunctions().get(i).advance(tiempo);
		}
		for(int i = 0; i < mapaCarreteras.getRoads().size(); i++) {
			mapaCarreteras.getRoads().get(i).advance(tiempo);
		}
		for(int i = 0; i < observers.size(); i++) {
			observers.get(i).onAdvanceEnd(mapaCarreteras, listaEventos, tiempo);
		}
	}
	
	public void reset() {
		tiempo = 0;
		mapaCarreteras.reset();
		listaEventos.clear();
		for(int i = 0; i < observers.size(); i++) {
			observers.get(i).onReset(mapaCarreteras, listaEventos, tiempo);
		}
	}
	
	public JSONObject report() {
		JSONObject ob = new JSONObject();
		
		ob.put("time", tiempo);
		ob.put("state", mapaCarreteras.report());
		
		return ob;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		if(!observers.contains(o)) {
			observers.add(o);
			o.onRegister(mapaCarreteras, listaEventos, tiempo);
		}
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		if(observers.contains(o)) observers.remove(o);
	}
	
}
