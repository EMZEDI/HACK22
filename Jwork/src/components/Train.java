package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import utilities.Drawable;

public class Train implements Drawable {
	
	public enum Type {
		L4, L8
	}
	
	public enum Station {
		NONE, A, B, C, UNION, TRAVELLING
	}

	private int number;
	private Station currentStation = Station.NONE;
	private Type type;
	private int capacity;
	private int numberOfPassengers;
	private double proportionTravelled;
	private int AArrivalTime, BArrivalTime, CArrivalTime, UnionArrivalTime;
	private double x, y, width, height;
	
	private final Color COLOR = new Color(251, 229, 214);
	private final Font FONT = new Font("Tahoma", Font.PLAIN, 16);
	private final int DWELL_TIME = 3;
	private final double B_PROPORTION, C_PROPORTION, UNION_PROPORTION;
	private final double A_TO_B_TIME, B_TO_C_TIME, C_TO_UNION_TIME;
	
	public Train(int number, Type type, int AArrivalTime, int BArrivalTime, int CArrivalTime, int UnionArrivalTime) {
		this.number = number;
		this.type = type;
		switch (type) {
			case L4:
				capacity = 200;
				break;
			case L8:
				capacity = 400;
				break;
		}
		this.numberOfPassengers = 0;
		this.AArrivalTime = AArrivalTime;
		this.BArrivalTime = BArrivalTime;
		this.CArrivalTime = CArrivalTime;
		this.UnionArrivalTime = UnionArrivalTime;
		this.proportionTravelled = -1;
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		
		A_TO_B_TIME = BArrivalTime - AArrivalTime - DWELL_TIME;
		B_TO_C_TIME = CArrivalTime - BArrivalTime - DWELL_TIME;
		C_TO_UNION_TIME = UnionArrivalTime - CArrivalTime - DWELL_TIME;
		B_PROPORTION = A_TO_B_TIME / (A_TO_B_TIME + B_TO_C_TIME + C_TO_UNION_TIME);
		C_PROPORTION = B_TO_C_TIME / (A_TO_B_TIME + B_TO_C_TIME + C_TO_UNION_TIME);
		UNION_PROPORTION = C_TO_UNION_TIME / (A_TO_B_TIME + B_TO_C_TIME + C_TO_UNION_TIME);
	}

	@Override
	public void draw(Graphics2D g2d) {
		Graphics2D g2dPrivate = (Graphics2D) g2d.create();
		
		Rectangle2D.Double rectangle = new Rectangle2D.Double(x, y, width, height);
		g2dPrivate.setColor(COLOR);
		g2dPrivate.fill(rectangle);
		
		g2dPrivate.setColor(Color.black);
		g2dPrivate.setFont(FONT);
		String passengersInfo = numberOfPassengers + "/" + capacity;
		g2dPrivate.drawString(number + "", (float) (x + width/2 - g2dPrivate.getFontMetrics().stringWidth(number + "")/2), (float) (y + height/2));
		g2dPrivate.drawString(passengersInfo, (float) (x + width/2 - g2dPrivate.getFontMetrics().stringWidth(passengersInfo)/2), (float) (y + height/2 + FONT.getSize()));
	}
	
	public void addPassengers(int numberOfPassengersToAdd) {
		numberOfPassengers += numberOfPassengersToAdd;
	}
	
	public void reset() {
		numberOfPassengers = 0;
		currentStation = Station.NONE;
		proportionTravelled = -1;
	}
	
	public void updatePosition(double currentTime) {
		if (currentTime < AArrivalTime) {
			currentStation = Station.NONE;
			proportionTravelled =  -1;
		} else if (currentTime <= AArrivalTime + DWELL_TIME) {
			if (currentTime == AArrivalTime) {
				currentStation = Station.A;
			} else {
				currentStation = Station.TRAVELLING;
			}
			proportionTravelled =  0;
		}
		else if (currentTime < BArrivalTime) {
			currentStation = Station.TRAVELLING;
			proportionTravelled =  B_PROPORTION * (currentTime - AArrivalTime - DWELL_TIME) / A_TO_B_TIME;
		} else if (currentTime <= BArrivalTime + DWELL_TIME) {
			if (currentTime == BArrivalTime) {
				currentStation = Station.B;
			} else {
				currentStation = Station.TRAVELLING;
			}
			proportionTravelled =  B_PROPORTION;
		} else if (currentTime < CArrivalTime) {
			currentStation = Station.TRAVELLING;
			proportionTravelled =  B_PROPORTION + C_PROPORTION * (currentTime - BArrivalTime - DWELL_TIME) / B_TO_C_TIME;
		} else if (currentTime <= CArrivalTime + DWELL_TIME) {
			if (currentTime == CArrivalTime) {
				currentStation = Station.C;
			} else {
				currentStation = Station.TRAVELLING;
			}
			proportionTravelled =  B_PROPORTION + C_PROPORTION;
		} else if (currentTime < UnionArrivalTime) {
			currentStation = Station.TRAVELLING;
			proportionTravelled =  B_PROPORTION + C_PROPORTION + UNION_PROPORTION * (currentTime - CArrivalTime - DWELL_TIME) / C_TO_UNION_TIME;
		} else {
			currentStation = Station.UNION;
			proportionTravelled = 1;
		}
	}
	
	public double getProportionTravelled() {
		return proportionTravelled;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	
	public Station getCurrentStation() {
		return currentStation;
	}
	
	public int getAvailableSpace() {
		return capacity - numberOfPassengers;
	}

	public String getType() {
		switch (type) {
		case L4:
			return "L4";
		case L8:
			return "L8";
		default:
			return "";
		}
	}
	
	public String getAArrivalTime() {
		int heures = 7 + AArrivalTime/60;
		int minutes = this.AArrivalTime%60;
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

	public int getNumber() {
		return number;
	}

}
