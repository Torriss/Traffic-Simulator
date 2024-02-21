package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private List<Junction> listaJunction;
	private Controller controller;
	private String _cols[]= { "Id", "Green", "Queues" };
	
	public JunctionsTableModel(Controller _ctrl) {
		controller = _ctrl;
		listaJunction = new ArrayList<Junction>();
		controller.addObserver(this);
	}
	@Override
	public int getColumnCount() {
		return _cols.length;
	}

	@Override
	public int getRowCount() {
		return listaJunction.size();
	}
	
	public String getColumnName(int col) {
		return _cols[col];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object ob = null;
		Junction j = listaJunction.get(rowIndex);
		
		switch (columnIndex){
			case 0:
				ob = j.getId();
				break;
			case 1:
				if (j.getCurrentGreen() == -1) ob = "NONE";
				else ob = j.getEnterRoads().get(j.getCurrentGreen());
				break;
			case 2:
				String g = null;
				for (Road r :j.getEnterRoads()){
					g = r.getId() + ":" + "[";
					for(int i =0; i < r.getVehicles().size(); i++) {
						if(i == r.getVehicles().size()-1) g += r.getVehicles().get(i).getId();
						else g += r.getVehicles().get(i).getId()+",";
					}
					g += "]";
				}
				ob = g;
				break;
		}
		return ob;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		listaJunction = map.getJunctions();
		fireTableDataChanged();	
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		listaJunction = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		listaJunction = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		listaJunction = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		listaJunction = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub	
	}
}
