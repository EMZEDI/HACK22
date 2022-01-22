package components;

import java.awt.Graphics2D;

import utilities.Drawable;

public class Train implements Drawable {
	
	public enum Type {
		L4, L8
	}

	private int number;
	private Type type;
	int capacity;
	int numberOfPassengers;
	int AArrivalTime, BArrivalTime, CArrivalTime, UnionArrivalTime;
	double x, y, width, height;
	
	public Train(int number, Type type, int AArrivalTime, int BArrivalTime, int CArrivalTime, int UnionArrivalTime) {
		this.number = number;
		this.type = type;
		switch (type) {
			case L4:
				capacity = 200;
			case L8:
				capacity = 400;
		}
		this.numberOfPassengers = 0;
		this.AArrivalTime = AArrivalTime;
		this.BArrivalTime = BArrivalTime;
		this.CArrivalTime = CArrivalTime;
		this.UnionArrivalTime = UnionArrivalTime;
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
	}

	@Override
	public void draw(Graphics2D g2d) {
		Graphics2D g2dPrivate = (Graphics2D) g2d.create();
		
	}
	
	public void addPassengers(int numberOfPassengersToAdd) {
		numberOfPassengers += numberOfPassengersToAdd;
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
