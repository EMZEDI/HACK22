package components;

import java.awt.Graphics2D;
import java.util.ArrayList;
import utilities.Drawable;

public class Station implements Drawable {
	
	private String name;
	private int[] passengerArrivals;
	private ArrayList<Passenger> waitingPassengers;
	double x, y, width, height;
	
	public Station(String name, int[] passengerArrivals, double x, double y, double width, double height) {
		this.name = name;
		this.passengerArrivals = passengerArrivals;
		this.waitingPassengers = new ArrayList<Passenger>();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Graphics2D g2d) {
		Graphics2D g2dPrivate = (Graphics2D) g2d.create();
		
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

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

}
