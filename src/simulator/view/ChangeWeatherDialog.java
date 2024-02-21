package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {
	
	private int estado;
	private JComboBox<Road> carretera;
	private JSpinner ticks;
	private JComboBox<Weather> clima;
	private JPanel principal;
	private JPanel panelCombos;
	private JPanel botones;
	private DefaultComboBoxModel<Road> roadModel;
	private DefaultComboBoxModel<Weather> weatherModel;
	private JLabel textoVentana;
	private JLabel textoRoad;
	private JLabel textoClima;
	private JLabel textoTicks;
	private JButton cancel;
	private JButton ok;
	
	public ChangeWeatherDialog(Frame parent) {
		super(parent, true);
		initGUi();
	}

	private void initGUi() {
		principal = new JPanel();
		principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
		setContentPane(principal);
		textoVentana = new JLabel("Cambio Clima");
		principal.add(textoVentana);
		
		this.setTitle("Change Weather Dialog");
		
		panelCombos = new JPanel();
		textoRoad = new JLabel("road");
		roadModel = new DefaultComboBoxModel<Road>();
		carretera = new JComboBox<Road>(roadModel);
		panelCombos.add(textoRoad);
		panelCombos.add(carretera);
		textoClima = new JLabel("clima");
		weatherModel = new DefaultComboBoxModel<Weather>();
		clima = new JComboBox<Weather>(weatherModel);
		panelCombos.add(textoClima);
		panelCombos.add(clima);
		textoTicks = new JLabel("ticks");
		ticks = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
		panelCombos.add(textoTicks);
		panelCombos.add(ticks);
		
		botones = new JPanel();
		cancel = new JButton("Cancelar");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				estado = 0;
				setVisible(false);
			}
		});
		botones.add(cancel);
		ok = new JButton("Aceptar");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(carretera.getSelectedItem() != null) {
					estado = 1;
					setVisible(false);
				}
			}
		});
		botones.add(ok);
		
		principal.add(panelCombos);
		principal.add(botones);
		
		setPreferredSize(new Dimension(500,200));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public int open(RoadMap mapa) {
		for(Road r:mapa.getRoads()) {
			roadModel.addElement(r);
		}
		for (Weather w : Weather.values())
		{
			weatherModel.addElement(w);
		}
		setVisible(true);
		return estado;
	}
	public int getTicks() {
		return (int)ticks.getValue();
	}
	public Road getRoad() {
		return (Road)roadModel.getSelectedItem();
	}
	public Weather getWeather() {
		return (Weather)weatherModel.getSelectedItem();
	}

}
