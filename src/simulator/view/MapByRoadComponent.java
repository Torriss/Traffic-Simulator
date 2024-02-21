package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
//import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class MapByRoadComponent extends JPanel implements TrafficSimObserver{
	
	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;
	private RoadMap mapa;
	private Image coche;
	private Image cont[];
	private Image weather;
	private int xCoche;
	private int y;
	private int x1;
	private int x2;
	
	public MapByRoadComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	
	public void initGUI() {
		cont=new Image[6];
		
		for (int i = 0; i < 6; i++) {
			cont[i] = loadImage("cont_" + i + ".png");
		}
		coche = loadImage("car.png");
		setPreferredSize(new Dimension (300, 200));
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (mapa == null || mapa.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			drawMap(g);
		}
	}
	public void drawMap(Graphics2D g) {
		int i = 1;
		
		for(Road r: mapa.getRoads()) {
			y = i*50;
			x1 = 50;
			x2 = getWidth() - 100;
			g.setColor(Color.BLACK);
			g.drawString(r.getId(), x1, y);
			g.drawLine(x1, y, x2, y);
			
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 - _JRADIUS/2, y - _JRADIUS/2, _JRADIUS, _JRADIUS);
			
			int idx = r.getDest().getCurrentGreen();
			
			if(r.getDest().getCurrentGreen()!= -1 && r.equals(r.getDest().getEnterRoads().get(idx))) {
				g.setColor(_GREEN_LIGHT_COLOR);
			}else g.setColor(_RED_LIGHT_COLOR);
			
			g.fillOval(x2 - _JRADIUS/2, y - _JRADIUS/2, _JRADIUS, _JRADIUS);
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getSrc().toString(), x1, y-_JRADIUS);
			g.drawString(r.getDest().toString(), x2, y-_JRADIUS);
			//Dibuja la imagen del clima
			switch(r.getWeather()){
				case RAINY:
					weather = loadImage("rain.png");
					break;
				case SUNNY:
					weather = loadImage("sun.png");
					break;
				case WINDY:
					weather = loadImage("wind.png");
					break;
				case STORM:
					weather = loadImage("storm.png");
					break;
				case CLOUDY:
					weather = loadImage("cloud.png");
					break;
			}
			g.drawImage(weather, x2 + 15, y - _JRADIUS * 2, 32, 32, this);
			int c = (int) Math.floor(Math.min((double) r.getTotalCont()/(1.0 + (double) r.getContAlarmLimit()),1.0) / 0.19);
			g.drawImage(cont[c], x2 + 55, y - _JRADIUS*2, 32, 32, this);
			if (!r.getVehicles().isEmpty()){
				for (Vehicle v : r.getVehicles()){
					xCoche = x1 + (int)((x2-x1)*((double)v.getLocation()/(double)r.getLength()));
					g.setColor(_GREEN_LIGHT_COLOR);
					g.drawString(v.getId(), xCoche, y - _JRADIUS-5);
					g.drawImage(coche, xCoche, y - _JRADIUS - 3, 16, 16, this);
				}
			}
			i++;
		}
		
	}

	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return i;
	}
	public void update(RoadMap map) {
		mapa = map;
		repaint();
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		update(map);
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		update(map);
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}

}
