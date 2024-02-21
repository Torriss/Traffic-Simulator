package simulator.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;
import simulator.model.Event;
import simulator.model.Road;
import simulator.control.Controller;
import simulator.misc.Pair;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class WeatherHistoryDialog extends JDialog implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	private static final String _HELPMSG = "<html><p>Information about roads weather.</p></html>";
	List<Pair<Integer, List<Pair<String, Weather>>>> _history;
	private JComboBox<Weather> _weather;
	private HistoryTableModel _historyTableModel;

	class HistoryTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		String[] _header = { "Tick", "Roads" };

		@Override
		public int getRowCount() {
			return _history.size();
		}

		@Override
		public int getColumnCount() {
			return _header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Pair<Integer, List<Pair<String, Weather>>> e = _history.get(rowIndex);
			String v = "";

			switch (columnIndex) {
			case 0:
				v = e.getFirst() + "";
				break;
			case 1:
				v = filter(e.getSecond());
				break;
			}
			return v;
		}

		private String filter(List<Pair<String, Weather>> l) {
			List<String> roads = new ArrayList<>();
			Weather w = (Weather) _weather.getSelectedItem();

			for (Pair<String, Weather> e : l) {
				if (e.getSecond().equals(w)) {
					roads.add(e.getFirst());
				}
			}
			return roads.toString();
		}

		private void update() {
			fireTableDataChanged();
		}

	}

	public WeatherHistoryDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {

		_history = new ArrayList<>();
		_historyTableModel = new HistoryTableModel();

		setTitle("Roads Weather History");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		JLabel help = new JLabel(_HELPMSG);
		help.setAlignmentX(CENTER_ALIGNMENT);

		mainPanel.add(help);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel viewsPanel = new JPanel();
		viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(viewsPanel);

		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(buttonsPanel);

		_weather = new JComboBox<Weather>(Weather.values());
		_weather.setPreferredSize(new Dimension(100, 25));

		viewsPanel.add(new JLabel("Weather:"));
		viewsPanel.add(_weather);

		JButton cancelButton = new JButton("Close");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				WeatherHistoryDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		JButton okButton = new JButton("Update");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_historyTableModel.update();
			}
		});
		buttonsPanel.add(okButton);

		mainPanel.add(new JScrollPane(new JTable(_historyTableModel)));

		setPreferredSize(new Dimension(500, 500));
		pack();
		setResizable(false);
		setVisible(false);
	}

	public void open() {
		_historyTableModel.update();
		setVisible(true);
	}

	private void updateHistory(RoadMap map, int time) {
		List<Pair<String, Weather>> l = new ArrayList<>();
		for (Road r : map.getRoads()) {
			l.add(new Pair<>(r.getId(), r.getWeather()));
		}
		_history.add(new Pair<>(time, l));
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(() -> updateHistory(map, time));
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(() -> _history.clear());
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
	}

}
