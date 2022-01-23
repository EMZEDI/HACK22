package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import utilities.Drawable;

public class Station implements Drawable {
	
	private String name;
	private int[] passengerArrivals;
	private ArrayList<Passenger> waitingPassengers;
	private double x, y, width, height;
	
	private final Color LIGHT_COLOR = new Color(222, 235, 247);
	private final Color DARK_COLOR = new Color(157, 195, 230);
	private final Font FONT = new Font("Tahoma", Font.PLAIN, 16);
	
	public Station(String name, int[] passengerArrivals, double width, double height) {
		this.name = name;
		this.passengerArrivals = passengerArrivals;
		this.waitingPassengers = new ArrayList<Passenger>();
		this.x = 0;
		this.y = 0;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Graphics2D g2d) {
		Graphics2D g2dPrivate = (Graphics2D) g2d.create();
		
		Rectangle2D.Double rectangle = new Rectangle2D.Double(x, y, width, height);
		if (passengerArrivals.length == 0) {
			g2dPrivate.setColor(DARK_COLOR);
		} else {
			g2dPrivate.setColor(LIGHT_COLOR);
		}
		g2dPrivate.fill(rectangle);
		
		g2dPrivate.setColor(Color.black);
		g2dPrivate.setFont(FONT);
		if (name.contains("\n")) {
			String firstPart = name.substring(0, name.indexOf("\n"));
			String secondPart = name.substring(name.indexOf("\n"));
			g2dPrivate.drawString(firstPart, (float) (x + width/2 - g2dPrivate.getFontMetrics().stringWidth(firstPart)/2), (float) (y + height/2 - FONT.getSize()*1/4));
			g2dPrivate.drawString(secondPart, (float) (x + width/2 - g2dPrivate.getFontMetrics().stringWidth(secondPart)/2), (float) (y + height/2 + FONT.getSize()*5/4));
		} else {
			g2dPrivate.drawString(name, (float) (x + width/2 - g2dPrivate.getFontMetrics().stringWidth(name)/2), (float) (y + height/2 - FONT.getSize()*0.5));
			String firstPassengerString = waitingPassengers.size() + " passengers";
			String secondPassengerString = "waiting";
			g2dPrivate.drawString(firstPassengerString,  (float) (x + width/2 - g2dPrivate.getFontMetrics().stringWidth(firstPassengerString)/2), (float) (y + height/2 + FONT.getSize()*0.5));
			g2dPrivate.drawString(secondPassengerString,  (float) (x + width/2 - g2dPrivate.getFontMetrics().stringWidth(secondPassengerString)/2), (float) (y + height/2 + FONT.getSize()*1.5));
		}
	}
	
	public void waitOneMinute() {
		for (Passenger passenger : waitingPassengers) {
			passenger.waitOneMinute();
		}
	}
	
	public void addPassengers(int currentTime, ArrayList<Passenger> allPassengers) {
		if (currentTime % 10 == 0) {
			int index = currentTime / 10;
			for (int i = 0; i < passengerArrivals[index]; i++) {
				Passenger newPassenger = new Passenger();
				waitingPassengers.add(newPassenger);
				allPassengers.add(newPassenger);
			}
		}
	}
	
	public void removePassengers(int numberToBeRemoved) {
		for (int i = 0; i < numberToBeRemoved; i++) {
			waitingPassengers.remove(0);
		}
	}
	
	public void reset() {
		waitingPassengers.clear();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public int getNumberOfWaitingPassengers() {
		return waitingPassengers.size();
	}

}
