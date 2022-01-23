package components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Animation extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Train> trains;
	private Station stationA, stationB, stationC, stationUnion;
	private ArrayList<Passenger> allPassengers;
	
	private boolean animationRunning = false;
	private int currentTime = 0;
	private int frame = 0;
	
	private final double MARGIN = 10;
	private final double TRAIN_STATION_SPACING = 10;
	private final double STATION_WIDTH = 100;
	private final double STATION_HEIGHT = 60;
	private final double TRAIN_WIDTH = 100;
	private final double TRAIN_HEIGHT = 60;
	private final int TABLE_HEIGHT = 50;
	private final long SLEEP_TIME = 50;
	
	private static final int MINIMUM_FRAMES_PER_MINUTE = 30;
	private static final int MAXIMUM_FRAMES_PER_MINUTE = 200;
	private int framesPerMinute = (MINIMUM_FRAMES_PER_MINUTE + MAXIMUM_FRAMES_PER_MINUTE)/2;
	
	public Animation(ArrayList<Train> trains, ArrayList<int[]> passengerArrivals) {
		setBackground(Color.white);
		stationA = new Station("STATION A", passengerArrivals.get(0), STATION_WIDTH, STATION_HEIGHT);
		stationC = new Station("STATION B", passengerArrivals.get(1), STATION_WIDTH, STATION_HEIGHT);
		stationB = new Station("STATION C", passengerArrivals.get(2), STATION_WIDTH, STATION_HEIGHT);
		stationUnion = new Station("STATION\nUNION", new int[0], STATION_WIDTH, STATION_HEIGHT);
		this.trains = trains;
		for (Train train : trains) {
			train.setWidth(TRAIN_WIDTH);
			train.setHeight(TRAIN_HEIGHT);
		}
		allPassengers = new ArrayList<Passenger>();
	}
	
	@Override
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;	
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		drawStations(g2d);
		drawTrack(g2d);
		drawTrains(g2d);
	}

	@Override
	public void run() {
		while(animationRunning) {
			
			repaint();
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Animation stopped");
	}
	
	public void setFramesPerMinute(int framesPerMinute) {
		this.framesPerMinute = framesPerMinute;
	}
	public void start() {
		if (!animationRunning) { 
			Thread animation = new Thread(this);
			animation.start();
			animationRunning = true;
		}
	}
	
	public void stop() {
		animationRunning = false;
	}

	public void reset() {
		stop();
		currentTime = 0;
		frame = 0;
		stationA.reset();
		stationB.reset();
		stationC.reset();
		stationUnion.reset();
		for (Train train : trains) {
			train.reset();
		}
		allPassengers.clear();
		repaint();
	}

	private void drawStations(Graphics2D g2d) {
		double space = (getWidth() - MARGIN*2 - STATION_WIDTH*4) / 3; 
		double y = getHeight() - STATION_HEIGHT - MARGIN;
		stationA.setX(MARGIN);
		stationA.setY(y);
		stationB.setX(MARGIN + STATION_WIDTH + space);
		stationB.setY(y);
		stationC.setX(MARGIN + (STATION_WIDTH + space) * 2);
		stationC.setY(y);
		stationUnion.setX(MARGIN + (STATION_WIDTH + space) * 3);
		stationUnion.setY(y);
		stationA.draw(g2d);
		stationB.draw(g2d);
		stationC.draw(g2d);
		stationUnion.draw(g2d);
	}

	private void drawTrack(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
		g2d.draw(new Line2D.Double(MARGIN, getHeight() - MARGIN - STATION_HEIGHT - TRAIN_STATION_SPACING - TRAIN_HEIGHT/2, getWidth() - MARGIN, getHeight() - MARGIN - STATION_HEIGHT - TRAIN_STATION_SPACING - TRAIN_HEIGHT/2));
	}

	private void drawTrains(Graphics2D g2d) {
		
	}

	public static int getMinimumFramesPerMinute() {
		return MINIMUM_FRAMES_PER_MINUTE;
	}

	public static int getMaximumFramesPerMinute() {
		return MAXIMUM_FRAMES_PER_MINUTE;
	}

}
