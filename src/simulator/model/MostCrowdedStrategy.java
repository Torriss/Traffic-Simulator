package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {
	
	private int timeSlot;
	
	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
		//Si devuelve = que currGreen no se cambia, y si es -1 todos en rojo
		if(roads.isEmpty()) return -1;
		else if(currGreen == -1) return currGreen = busquedaRoad(qs);
		else if(currTime - lastSwitchingTime < timeSlot) return currGreen;
		else return busquedaRoad_2(qs, (currGreen +1)%roads.size());
	}
	
	private int busquedaRoad(List<List<Vehicle>> lista) {
		int tam = 0, pos = 0;
		
		for(int i = 0; i < lista.size(); i++) {
			if(lista.get(i).size() > tam) {
				tam = lista.get(i).size();
				pos = i;
			}
		}
		return pos;
	}
	
	//(currGreen +1)%roads.size()
	private int busquedaRoad_2(List<List<Vehicle>> lista, int comienzo) {
		int tam1 = 0, pos1 = 0;
		int tam2 = 0, pos2 = 0;
		
		for(int i = 0; i < comienzo; i++) {
			if(lista.get(i).size() > tam1) {
				tam1 = lista.get(i).size();
				pos1 = i;
			}
		}
		for(int i = comienzo; i < lista.size(); i++) {
			if(lista.get(i).size() > tam2) {
				tam2 = lista.get(i).size();
				pos2 = i;
			}
		}
		if(tam1 == tam2) return pos2;
		else if(tam1 > tam2) return pos1;
		else return pos2;
	}

}
