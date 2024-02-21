package simulator.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {
	
	private Controller controller;
	private JLabel tiempo;
	private JLabel tiempoAct;
	private JLabel mensajeAct;

	public StatusBar(Controller _ctrl) {
		controller = _ctrl;
		controller.addObserver(this);
		initGUI();
	}
	
	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		tiempo = new  JLabel("Time: ", JLabel.LEFT);
		tiempoAct = new JLabel("");
		mensajeAct = new JLabel("");
		this.add(tiempo);
		this.add(tiempoAct);
		this.add(mensajeAct);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tiempoAct.setText(" "+time);
				mensajeAct.setText("");
			}
		});
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tiempoAct.setText(" " + time);
				mensajeAct.setText(e.toString());
			}
		});
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tiempoAct.setText(" " + time);
				mensajeAct.setText("");
			}
		});
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tiempoAct.setText(" " + time);
				mensajeAct.setText("");
			}
		});
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub

	}

}
