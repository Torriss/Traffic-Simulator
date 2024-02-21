package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver {
	
	private Controller controller;
	private RoadMap mapa;
	private int tiempo;
	private JToolBar barraHerramientas;
	private JButton botonCarga;
	private JButton botonCambioCO2;
	private JButton botonChangeWeather;
	private JButton run;
	private JButton stop;
	private JButton exit;
	private JFileChooser seleccion;
	//private ChangeCO2ClassDialog co2Dialog;
	//private ChangeWeatherDialog weatherDialog;
	private boolean _stopped;
	private JLabel textoTicks;
	private JSpinner ticks;

	public ControlPanel(Controller _ctrl) {
		controller = _ctrl;
		controller.addObserver(this);
		initGUI();
	}
	
	public void initGUI() {
	setLayout(new BorderLayout());
	barraHerramientas = new JToolBar();
	barraHerramientas.setBackground(Color.LIGHT_GRAY);
	this.add(barraHerramientas, BorderLayout.PAGE_START);
	
	seleccion=new JFileChooser();
	barraHerramientas.addSeparator();
	botonCargaFichero();
	barraHerramientas.addSeparator();
	botonCambioContClass();
	barraHerramientas.addSeparator();
	botonChangeWeather();
	barraHerramientas.addSeparator();
	botonRun();
	barraHerramientas.addSeparator();
	botonStop();
	barraHerramientas.addSeparator();
	spinnerTicks();
	barraHerramientas.add(Box.createGlue());
	botonExit();
	}
	
	void botonExit() {
		exit = new JButton();
		exit.setToolTipText("Cierra applicacion");
		exit.setIcon(loadImage("./resources/icons/exit.png"));
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "Hasta Pronto", "exit", JOptionPane.YES_NO_OPTION);
				if(n == JOptionPane.YES_NO_OPTION) System.exit(0);
			}
		});
		barraHerramientas.add(exit);
	}
	
	private void spinnerTicks() {
		textoTicks = new JLabel();
		textoTicks.setText("Ticks: ");
		ticks = new JSpinner(new SpinnerNumberModel(10, 1, 99999, 1));
		ticks.setMinimumSize(new Dimension(80, 30));
		ticks.setMaximumSize(new Dimension(200, 30));
		ticks.setPreferredSize(new Dimension(80, 30));
		barraHerramientas.add(textoTicks);
		barraHerramientas.add(ticks);
	}
	
	void botonStop() {
		stop = new JButton();
		stop.setToolTipText("Detener ejecucion");
		stop.setIcon(loadImage("./resources/icons/stop.png"));
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop_sim();
			}
			
		});
		barraHerramientas.add(stop);
	}
	
	private void stop_sim() {
		_stopped = true;
		run_sim(Integer.parseInt(ticks.getValue().toString()));
	}
	
	void botonRun() {
		run = new JButton();
		run.setToolTipText("Ejecutar");
		run.setIcon(loadImage("./resources/icons/run.png"));
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		barraHerramientas.add(run);
	}
	
	private void start() {
		_stopped = false;
		enableToolBar(false);
		run_sim((Integer)ticks.getValue());
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				controller.run(1);
			} catch (Exception e) {
				// TODO show error message and _stopped = true
				JOptionPane.showMessageDialog(null, "ERROR-ejecutar ");
				_stopped = true;
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			enableToolBar(true);
			_stopped = true;
		}
	}
	
	void botonCargaFichero() {
		botonCarga = new JButton();
		botonCarga.setToolTipText("Carga de Fichero de Eventos");
		botonCarga.setIcon(loadImage("./resources/icons/open.png"));
		botonCarga.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(seleccion.showOpenDialog(seleccion) == JFileChooser.APPROVE_OPTION) {
					InputStream in;
					try {
						in = new FileInputStream(seleccion.getSelectedFile());
						controller.reset();
						controller.loadEvents(in);
					}catch(FileNotFoundException a) {
						JOptionPane.showMessageDialog(null, "Error al cargar el archivo");
					}
				}		
			}
		});
		barraHerramientas.add(botonCarga);
	}
	
	void botonCambioContClass() {
		botonCambioCO2 = new JButton();
		botonCambioCO2.setToolTipText("Cambio clase contaminacion Vehiculo");
		botonCambioCO2.setIcon(loadImage("./resources/icons/co2class.png"));
		botonCambioCO2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeCO2class();	
			}
		});
		barraHerramientas.add(botonCambioCO2);
	}
	
	private void changeCO2class() {
		 ChangeCO2Dialog co2Dialog = new ChangeCO2Dialog((Frame) SwingUtilities.getWindowAncestor(this));
		 int estado = co2Dialog.open(mapa);
			if(estado == 1) {
				List<Pair<String,Integer>> listaParejas = new ArrayList<Pair<String,Integer>>();
				listaParejas.add(new Pair<String,Integer>(co2Dialog.getVehicle().getId(), co2Dialog.getCO2Class()));
				try {
					int s = tiempo + co2Dialog.getTicks();
					controller.addEvent(new NewSetContClassEvent(s,listaParejas));
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "ERROR-Cambio de Contaminacion ");
				}
			}
	}
		
	
	
	void botonChangeWeather() {
		botonChangeWeather = new JButton();
		botonChangeWeather.setToolTipText("Cambio clima de una carretera");
		botonChangeWeather.setIcon(loadImage("./resources/icons/weather.png"));
		botonChangeWeather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeWeather();
			}
		});
		barraHerramientas.add(botonChangeWeather);
	}
	
	private void changeWeather() {
		ChangeWeatherDialog weatherDialog = new ChangeWeatherDialog((Frame) SwingUtilities.getWindowAncestor(this));
		if(weatherDialog.open(mapa) == 1) {
			List<Pair<String,Weather>> listaParejas= new ArrayList<Pair<String,Weather>>();
			listaParejas.add(new Pair<String,Weather>(weatherDialog.getRoad().getId(),weatherDialog.getWeather()));
			try {
				int s = tiempo + weatherDialog.getTicks();
				controller.addEvent(new SetWeatherEvent(s,listaParejas));
			}catch(Exception e1) {
				JOptionPane.showMessageDialog(null, "ERROR-Cambio de tiempo");
			}
		}
	}
	
	protected ImageIcon loadImage(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(path)); 
	}
	
	private void enableToolBar(boolean enable) {	
		ticks.setEnabled(enable);
		botonCarga.setEnabled(enable);
		run.setEnabled(enable);
		botonChangeWeather.setEnabled(enable);
		exit.setEnabled(enable);
		botonCambioCO2.setEnabled(enable);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.mapa = map;
		tiempo = time;
	}

	@Override 
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.mapa = map;
		tiempo = time;
	}
	
	@Override 
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.mapa = map;
		tiempo = time;
	}
	
	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.mapa = map;
		tiempo = time;
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
	}

}
