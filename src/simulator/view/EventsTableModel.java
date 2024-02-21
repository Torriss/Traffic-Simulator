package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {
	
	private Controller controlador;
	private List<Event> listaEventos;
	private String _cols[]= {"Time","Desc"};
	
	public EventsTableModel(Controller _ctrl) {
		controlador = _ctrl;
		listaEventos = new ArrayList<Event>();
		controlador.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return listaEventos == null ? 0 : listaEventos.size();
	}

	@Override
	public int getColumnCount() {
		return _cols.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return _cols[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String s = "";
		Event e = listaEventos.get(rowIndex);
		switch ( columnIndex ) {
			case 0:
				s = "" + e.getTime();
				break;
			case 1:
				s = e.toString();
				break;
			default:
				assert(false);
			}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		listaEventos=events;
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		listaEventos=events;
		this.fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		listaEventos=events;
		this.fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		listaEventos=events;
		this.fireTableDataChanged();
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub

	}

}
