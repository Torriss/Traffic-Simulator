package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {

	private List<Vehicle> listaVehicles;
	private Controller controlador;
	private String _cols[]= {"id", "Estado", "Itinerario", "CO2 Class", "Max Speed", "Act. Speed", "Total CO2", "Distancia"};
	
	public VehiclesTableModel(Controller _ctrl) {
		controlador = _ctrl;
		listaVehicles = new ArrayList<Vehicle>();
		controlador.addObserver(this);
	}
	
	@Override
	public int getColumnCount() {
		return _cols.length;
	}
	
	public String getColumnName(int col) {
		return _cols[col];
	}
	
	@Override
	public int getRowCount() {
		return  listaVehicles == null ? 0 : listaVehicles.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Vehicle v = listaVehicles.get(rowIndex);
		Object ob = null;
		
		switch (columnIndex){
		case 0:
			ob = v.getId();
				break;
		case 1: 
			switch(v.getStatus()) {
			case PENDING:
				ob = "Pending";
				break;
			case TRAVELING:
				ob = v.getRoad().getId() + ":" +v.getLocation();
				break;
			case WAITING:
				ob = "Waiting:" + v.getRoad().getDest().getId();
				break;
			case ARRIVED:
				ob = "Arrived";
				break;
			}
			break;
		case 2:
			ob = v.getItinerary();
			break;
		case 3: 
			ob = v.getContClass();
			break;
		case 4:
			ob = v.getMaxSpeed();
			break;
		case 5:
			ob = v.getCurrentSpeed();
			break;
		case 6:
			ob = v.getTotalCont();
			break;
		case 7: 
			ob = v.getTotalTravelDist();
			break;
		}
		return ob;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		listaVehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		listaVehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		listaVehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		listaVehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		listaVehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
	}

}
