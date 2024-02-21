package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private List<Road> listaRoads;
	private Controller controller;
	private String _cols[]= {"Id", "Longitud", "Weather", "Max Speed", "Act. Speed", "Total CO2", "CO2 Limit"};
	
	public RoadsTableModel(Controller _ctrl) {
		controller = _ctrl;
		listaRoads = new ArrayList<Road>();
		controller.addObserver(this);
	}
		@Override
		public int getColumnCount() {
			return _cols.length;
		}

		@Override
		public int getRowCount() {
			return listaRoads == null ? 0 : listaRoads.size();
		}
		
		public String getColumnName(int col) {
			return _cols[col];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Object ob = null;
			Road r = listaRoads.get(rowIndex);
			
			switch (columnIndex){
			case 0:
				ob = r.getId();
				break;
			case 1: 
				ob = r.getLength();
				break;
			case 2:
				ob = r.getWeather();
				break;
			case 3: 
				ob = r.getMaxSpeed();
				break;
			case 4:
				ob = r.getCurrentSpeedLimit();
				break;
			case 5:
				ob = r.getTotalCont();
				break;
			case 6:
				ob = r.getContAlarmLimit();
				break;
			}
			return ob;
		}

		@Override
		public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
			listaRoads = map.getRoads();
			fireTableDataChanged();
			for(int i = 0; i < listaRoads.size(); i++) {
				listaRoads.get(i).getWeather();
				
			}
		}

		@Override
		public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
			listaRoads = map.getRoads();
			fireTableDataChanged();
		}

		@Override
		public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
			listaRoads = map.getRoads();
			fireTableDataChanged();
		}

		@Override
		public void onReset(RoadMap map, List<Event> events, int time) {
			listaRoads = map.getRoads();
			fireTableDataChanged();
		}

		@Override
		public void onRegister(RoadMap map, List<Event> events, int time) {
			listaRoads = map.getRoads();
			fireTableDataChanged();
		}

		@Override
		public void onError(String msg) {
			// TODO Auto-generated method stub	
		}
}
