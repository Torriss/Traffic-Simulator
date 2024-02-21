package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONObject;
import org.json.JSONTokener;
import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator trafficSimulator;
	private Factory<Event> eventsFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		if(sim == null || eventsFactory == null) throw new IllegalArgumentException("Arguments must not be null");
		else {trafficSimulator = sim; this.eventsFactory = eventsFactory;}
	}
	
	//Excepcion si la entrada json no encaja con la de arriba
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		
		for(int i = 0; i < jo.getJSONArray("events").length(); i++) {
			trafficSimulator.addEvent(eventsFactory.createInstance(jo.getJSONArray("events").getJSONObject(i)));
		}
	}
	
	public void run(int n, OutputStream out) {
		PrintStream consola = new PrintStream(out);
		
        consola.println("{");
        consola.println(" \"states\": [");
        for(int i = 0; i < n; i++) {
      	  trafficSimulator.advance();
      	  consola.print(trafficSimulator.report());
      	  if(i == n-1) {}
      	  else consola.println(",");
      	}
        consola.println(); 
        consola.println("]");  
        consola.println("}");
        consola.close();
	}
	
	public void run(int n) {
		for(int i = 0; i < n; i++) {
			trafficSimulator.advance();
		}
	}
	
	public void reset() {
		trafficSimulator.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		trafficSimulator.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		trafficSimulator.removeObserver(o);
	}
	
	public void addEvent(Event e){
		trafficSimulator.addEvent(e);
	}
}
