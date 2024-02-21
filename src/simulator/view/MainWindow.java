package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	
	private Controller _ctrl;
	
	public MainWindow(Controller ctrl) {
	super("Traffic Simulator");
	_ctrl = ctrl;
	initGUI();
	}
	
	private void initGUI() {
	JPanel mainPanel = new JPanel(new BorderLayout());
	this.setContentPane(mainPanel);
	
	mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
	mainPanel.add(new StatusBar(_ctrl),BorderLayout.PAGE_END);
	
	JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
	mainPanel.add(viewsPanel, BorderLayout.CENTER);
	
	JPanel tablesPanel = new JPanel();
	tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
	viewsPanel.add(tablesPanel);
	
	JPanel mapsPanel = new JPanel();
	mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
	viewsPanel.add(mapsPanel);
	
	//Tablas
	//las dos sentencias de abajo centran los datos en las columnas de las tablas
	DefaultTableCellRenderer alinear = new DefaultTableCellRenderer();
	alinear.setHorizontalAlignment(SwingConstants.CENTER);
	
	//Tabla eventos
	JTable eventsTable = new JTable(new EventsTableModel(_ctrl));
	eventsTable.setGridColor(Color.WHITE);
	for(int i = 0; i < eventsTable.getColumnModel().getColumnCount(); i++) {
		eventsTable.getColumnModel().getColumn(i).setCellRenderer(alinear);
	}
	JPanel eventsView = createViewPanel(eventsTable, "Events"); 
	eventsView.setPreferredSize(new Dimension(500, 200));
	eventsView.setBorder(new TitledBorder("Eventos"));
	tablesPanel.add(eventsView);
	
	//Tabla vehiculos
	JTable vehiclesTable = new JTable(new VehiclesTableModel(_ctrl));
	vehiclesTable.setGridColor(Color.WHITE);
	for(int i = 0; i < vehiclesTable.getColumnModel().getColumnCount(); i++) {
		vehiclesTable.getColumnModel().getColumn(i).setCellRenderer(alinear);
	}
	JPanel vehicleView = createViewPanel(vehiclesTable,"Vehicles");
	vehicleView.setPreferredSize(new Dimension(500, 200));
	vehicleView.setBorder(new TitledBorder("Vehiculos"));
	tablesPanel.add(vehicleView);
	
	//Tabla carreteras
	JTable roadsTable = new JTable(new RoadsTableModel(_ctrl));
	roadsTable.setGridColor(Color.WHITE);
	for(int i = 0; i < roadsTable.getColumnModel().getColumnCount(); i++) {
		roadsTable.getColumnModel().getColumn(i).setCellRenderer(alinear);
	}
	JPanel roadsView = createViewPanel(roadsTable,"Roads");
	roadsView.setPreferredSize(new Dimension(500, 200));
	roadsView.setBorder(new TitledBorder("Carreteras"));
	tablesPanel.add(roadsView);
	
	//Tabla cruces
	JTable junctionsTable = new JTable(new JunctionsTableModel(_ctrl));
	junctionsTable.setGridColor(Color.WHITE);
	for(int i = 0; i < junctionsTable.getColumnModel().getColumnCount(); i++) {
		junctionsTable.getColumnModel().getColumn(i).setCellRenderer(alinear);
	}
	JPanel junctionView = createViewPanel(junctionsTable, "Junctions");
	junctionView.setPreferredSize(new Dimension(500, 200));
	junctionView.setBorder(new TitledBorder("Cruces"));
	tablesPanel.add(junctionView);
	
	// maps
	JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
	mapView.setPreferredSize(new Dimension(500, 400));
	mapView.setBorder(new TitledBorder("Map"));
	mapsPanel.add(mapView);
	
	// TODO add a map for MapByRoadComponent
	JPanel mapRoadView = createViewPanel(new MapByRoadComponent(_ctrl),"MapByRoad");
	mapRoadView.setPreferredSize(new Dimension(500, 400));
	mapRoadView.setBorder(new TitledBorder("Map By Road"));
	mapsPanel.add(mapRoadView);
	
	this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	this.pack();
	this.setVisible(true);
	}
	
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout() );
		// TODO add a framed border to p with a title
		p.add(new JScrollPane(c));
		return p;
		} 
}
