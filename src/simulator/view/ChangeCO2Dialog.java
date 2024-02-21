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

import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class ChangeCO2Dialog extends JDialog {
	
	private int estado = 0;
	private JComboBox<Vehicle> comboV;
	private JSpinner ticks;
	private JComboBox<Integer> comboCO2;
	private JLabel textoTicks;
	private JPanel principal;
	private JLabel textoVentana;
	private JLabel textoVehicle;
	private JLabel textoCO2;
	private JButton ok;
	private JButton cancel;
	private JPanel botones;
	private JPanel opciones;
	private DefaultComboBoxModel<Vehicle> vehicleModel;
	private DefaultComboBoxModel<Integer> co2Model;

	public ChangeCO2Dialog(Frame frame) {
		super(frame,true);
		initGUI();
	}

	private void initGUI() {
		principal=new JPanel();
		principal.setLayout(new BoxLayout(principal,BoxLayout.Y_AXIS));
		setContentPane(principal);
		
		this.setTitle("Change CO2 Dialog");
		
		botones = new JPanel();
		textoVehicle = new JLabel();
		textoVehicle.setText("Vehicle: ");
		botones.add(textoVehicle);
		
		vehicleModel = new DefaultComboBoxModel<Vehicle>();
		comboV = new JComboBox<Vehicle>(vehicleModel);
		botones.add(comboV);
		
		textoCO2 = new JLabel();
		textoCO2.setText("CO2: ");
		botones.add(textoCO2);
		co2Model = new DefaultComboBoxModel<Integer>();
		comboCO2 = new JComboBox<Integer>(co2Model);
		botones.add(comboCO2);
		ticks = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
		textoTicks = new JLabel();
		textoTicks.setText("Ticks: ");
		botones.add(textoTicks);
		botones.add(ticks);
		
		opciones = new JPanel();
		cancel = new JButton("Cancelar");
		
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				 estado=0;
				 setVisible(false);
			}
		});
		opciones.add(cancel);
		ok=new JButton("Aceptar");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(comboV.getSelectedItem()!=null) {
					estado=1;
					setVisible(false);
				}
			}
		});
		opciones.add(ok);
		principal.add(botones);
		principal.add(opciones);
		
		setPreferredSize(new Dimension(500,200));
		pack();
		setResizable(false);
		setVisible(false);
		
	}
	public int open(RoadMap mapa) {
		for(Vehicle v: mapa.getVehicles()) {
			vehicleModel.addElement(v);
		}
		for (int i = 0; i < 11; i++) {
			co2Model.addElement(i);
		}
		setVisible(true);
		return estado;
	}
	
	public int getTicks() {
		return (int)ticks.getValue();
	}
	
	public Vehicle getVehicle() {
		return (Vehicle)comboV.getSelectedItem();
	}
	
	public int getCO2Class() {
		return (int)comboCO2.getSelectedItem();
	}

}
