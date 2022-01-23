package components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Animation extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Train> trains;
	private Station stationA, stationB, stationC, stationUnion;
	private ArrayList<Passenger> allPassengers;
	private Table table;
	
	private boolean animationRunning = false;
	private int currentTime = 0;
	private double averageWaitingTime = 0;
	private int framesPerMinute;
	private int frame = 0;
	private double spacing;
	
	private final double MARGIN = 10;
	private final double TRAIN_STATION_SPACING = 10;
	private final double STATION_WIDTH = 110;
	private final double STATION_HEIGHT = 60;
	private final double TRAIN_WIDTH = 60;
	private final double TRAIN_HEIGHT = 50;
	private final int TABLE_HEIGHT = 60;
	private final int TEXT_HEIGHT = 25;
	private final Font FONT = new Font("Tahoma", Font.PLAIN, 18);
	private final Font SMALL_FONT = new Font("Tahoma", Font.PLAIN, 16);
	
	private final long SLEEP_TIME = 20;
	private final int MINIMUM_FRAMES_PER_MINUTE = 3;
	private final int FRAME_MULTIPLICATION_FACTOR = 2;
	private final int MAX_TIME = 217;
	
	private final double A_TO_B = 8, B_TO_C = 9, C_TO_UNION = 11;
	private final double B_PROPORTION = A_TO_B / (A_TO_B + B_TO_C + C_TO_UNION);
	private final double C_PROPORTION = (A_TO_B + B_TO_C) / (A_TO_B + B_TO_C + C_TO_UNION);
	
	public Animation(ArrayList<Train> trains, ArrayList<int[]> passengerArrivals) {
		setBackground(Color.white);
		stationA = new Station("STATION A", passengerArrivals.get(0), STATION_WIDTH, STATION_HEIGHT);
		stationB = new Station("STATION B", passengerArrivals.get(1), STATION_WIDTH, STATION_HEIGHT);
		stationC = new Station("STATION C", passengerArrivals.get(2), STATION_WIDTH, STATION_HEIGHT);
		stationUnion = new Station("UNION\nSTATION", new int[0], STATION_WIDTH, STATION_HEIGHT);
		this.trains = trains;
		for (Train train : trains) {
			train.setWidth(TRAIN_WIDTH);
			train.setHeight(TRAIN_HEIGHT);
		}
		allPassengers = new ArrayList<Passenger>();
		table = new Table(trains);
		table.setHeight(TABLE_HEIGHT);
	}
	
	@Override
	public void paintComponent(Graphics g) {		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;	
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		spacing = (getHeight() - TEXT_HEIGHT - TABLE_HEIGHT - TRAIN_HEIGHT - TRAIN_STATION_SPACING - STATION_HEIGHT) / 4.0;

		drawTable(g2d);
		drawStations(g2d);
		drawTrack(g2d);
		drawTrains(g2d);
		writeTimes(g2d);
	}

	@Override
	public void run() {
		updateStations();
		updateTrainPositions();
		fillTrains();
		while(animationRunning && currentTime < MAX_TIME) {
			frame++;
			updateTrainPositions();
			if (frame == framesPerMinute) {
				frame = 0;
				currentTime++;
				waitOneMinute();
				updateStations();
				fillTrains();
				calculateAverageWaitingTime();
			}
			repaint();
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setSpeed(int speed) {
		double ratio = (double) frame/framesPerMinute;
		framesPerMinute = (int) (MINIMUM_FRAMES_PER_MINUTE * Math.pow(FRAME_MULTIPLICATION_FACTOR,speed));
		frame = (int) (framesPerMinute * ratio);
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
		averageWaitingTime = 0;
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
		double y = getHeight() - STATION_HEIGHT - spacing;
		stationA.setX(MARGIN);
		stationA.setY(y);
		stationB.setX(MARGIN + (getWidth() - MARGIN * 2 - STATION_WIDTH) * B_PROPORTION);
		stationB.setY(y);
		stationC.setX(MARGIN + (getWidth() - MARGIN * 2 - STATION_WIDTH) * C_PROPORTION);
		stationC.setY(y);
		stationUnion.setX(MARGIN + getWidth() - MARGIN * 2 - STATION_WIDTH);
		stationUnion.setY(y);
		stationA.draw(g2d);
		stationB.draw(g2d);
		stationC.draw(g2d);
		stationUnion.draw(g2d);
	}

	private void drawTrack(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
		double y = getHeight() - STATION_HEIGHT - TRAIN_STATION_SPACING - TRAIN_HEIGHT/2 - spacing;
		g2d.draw(new Line2D.Double(MARGIN, y, getWidth() - MARGIN, y));
	}

	private void drawTrains(Graphics2D g2d) {
		for (Train train : trains) {
			double proportionTravelled = train.getProportionTravelled();
			if (proportionTravelled > -1 && proportionTravelled < 1) {
				train.setX(stationA.getX() + STATION_WIDTH/2 - TRAIN_WIDTH/2 + (stationUnion.getX() - stationA.getX()) * proportionTravelled);
				train.setY(getHeight() - STATION_HEIGHT - TRAIN_STATION_SPACING - TRAIN_HEIGHT - spacing);
				train.draw(g2d);
			}
		}
	}

	private void writeTimes(Graphics2D g2d) {
		String currentTime = "Current Time: " + timeToString(this.currentTime);
		String averageWainting = "Average Waiting Time: " + Math.round(averageWaitingTime * 100) / 100.0;
		g2d.setFont(FONT);
		g2d.drawString(currentTime,  (float) (getWidth()/4 - g2d.getFontMetrics().stringWidth(currentTime)/2), (float) (spacing + FONT.getSize()*1.5));
		g2d.drawString(averageWainting,  (float) (getWidth()*3/4 - g2d.getFontMetrics().stringWidth(averageWainting)/2), (float) (spacing + FONT.getSize()*1.5));
	}

	private void drawTable(Graphics2D g2d) {
		String train = "Train";
		String schedule = "Schedule:";
		g2d.setFont(SMALL_FONT);
		int trainWidth = g2d.getFontMetrics().stringWidth(train);
		int textWidth = g2d.getFontMetrics().stringWidth(schedule);
		
		table.setWidth(getWidth() - MARGIN * 3 - textWidth);
		table.setX(MARGIN * 2 + textWidth);
		table.setY(spacing * 2 + TEXT_HEIGHT);
		table.draw(g2d);
		
		g2d.drawString(train,  (float) (MARGIN + (textWidth)/2 - trainWidth/2), (float) (table.getY() + table.getHeight()/2));
		g2d.drawString(schedule,  (float) MARGIN, (float) (table.getY() + table.getHeight()/2 + FONT.getSize()));
	}
	
	private void calculateAverageWaitingTime() {
		averageWaitingTime = 0;
		if (!allPassengers.isEmpty()) {
			for (Passenger passenger : allPassengers) {
				averageWaitingTime += passenger.getWaitingTime();
			}
			averageWaitingTime /= allPassengers.size();
		}
	}
	
	private void waitOneMinute() {
		stationA.waitOneMinute();
		stationB.waitOneMinute();
		stationC.waitOneMinute();
	}
	
	private void updateStations() {
		if (currentTime <= 3 * 60) {
			stationA.addPassengers(currentTime, allPassengers);
			stationB.addPassengers(currentTime, allPassengers);
			stationC.addPassengers(currentTime, allPassengers);
		}
	}

	private void updateTrainPositions() {
		for (Train train : trains) {
			train.updatePosition(currentTime + (double) frame/framesPerMinute);
		}
	}
	
	private void fillTrains() {
		for (Train train : trains) {
			Station station = null;
			boolean fillTrain;
			switch (train.getCurrentStation()) {
				case A:
					station = stationA;
					fillTrain = true;
					break;
				case B:
					station = stationB;
					fillTrain = true;
					break;
				case C:
					station = stationC;
					fillTrain = true;
					break;
				default:
					fillTrain = false;
					break;
			}
			if (fillTrain) {
				int waitingPassengers = station.getNumberOfWaitingPassengers();
				int availableSpace = train.getAvailableSpace();
				if (waitingPassengers != 0 && availableSpace != 0) {
					if (waitingPassengers < availableSpace) {
						station.removePassengers(waitingPassengers);
						train.addPassengers(waitingPassengers);
					} else {
						station.removePassengers(availableSpace);
						train.addPassengers(availableSpace);
					}
				}
			}
		}
	}
	
	private String timeToString(int currentTime) {
		int heures = 7 + this.currentTime/60;
		int minutes = this.currentTime%60;
		String strHeures, strMinutes;
		if (heures >= 10) {
			strHeures = "" + heures;
		} else {
			strHeures = "0" + heures;
		}
		if (minutes >= 10) {
			strMinutes = "" + minutes;
		} else {
			strMinutes = "0" + minutes;
		}
		return strHeures + ":" + strMinutes;
	}

}
