package components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import utilities.Drawable;

public class Table implements Drawable {

	private double x, y, width, height;
	private ArrayList<Train> trains;
	
	private final Color BACKGROUND_COLOR = Color.white;
	private final Color BORDER_COLOR = Color.black;
	private final Color FONT_COLOR = Color.black;
	private final Color TRAVELLING_COLOR = new Color(251, 228, 213);
	private final Color ARRIVED_COLOR = new Color(200, 200, 200);
	private final Font TITLES_FONT = new Font("Tahoma", Font.BOLD, 14);
	private final Font FONT = new Font("Arial", Font.PLAIN, 12);
	private final double FIRST_COLUMN_WIDTH = 100;
	
	public Table(ArrayList<Train> trains) {
		this.trains = trains;
	}

	@Override
	public void draw(Graphics2D g2d) {
		Graphics2D g2dPrivate = (Graphics2D) g2d.create();

		int nbLines = 3;
		int nbColumns = trains.size();
		double lineSize = height/nbLines;
		double columnSize = (width - FIRST_COLUMN_WIDTH)/nbColumns;
		
		Rectangle2D.Double rectangle = new Rectangle2D.Double(x, y, width, height);
		g2dPrivate.setColor(BACKGROUND_COLOR);
		g2dPrivate.fill(rectangle);
		

		g2dPrivate.setFont(FONT);
		for (int i = 0; i < trains.size(); i++) {
			Train train = trains.get(i);
			switch (train.getCurrentStation()) {
			case NONE:
				g2dPrivate.setColor(BACKGROUND_COLOR);
				break;
			case TRAVELLING:
			case A:
			case B:
			case C:
				g2dPrivate.setColor(TRAVELLING_COLOR);
				break;
			case UNION:
				g2dPrivate.setColor(ARRIVED_COLOR);
				break;
			}
			g2dPrivate.fill(new Rectangle2D.Double(x + FIRST_COLUMN_WIDTH + columnSize * i, y, columnSize, height));
			
			g2dPrivate.setColor(FONT_COLOR);;
			drawString(g2dPrivate, train.getNumber() + "", x + FIRST_COLUMN_WIDTH + columnSize * i + columnSize/2, y + lineSize/2);
			drawString(g2dPrivate, train.getType(), x + FIRST_COLUMN_WIDTH + columnSize * i + columnSize/2, y + lineSize/2 + lineSize);
			drawString(g2dPrivate, train.getAArrivalTime(), x + FIRST_COLUMN_WIDTH + columnSize * i + columnSize/2, y + lineSize/2 + lineSize*2);
		}

		g2dPrivate.setColor(BORDER_COLOR);
		g2dPrivate.draw(new Line2D.Double(x, y, x, y + height));
		for (int i = 0; i <= nbLines; i ++) {
			g2dPrivate.draw(new Line2D.Double(x, y + lineSize * i, x + width, y + lineSize * i));
		}
		for (int i = 0; i <= nbColumns; i ++) {
			g2dPrivate.draw(new Line2D.Double(FIRST_COLUMN_WIDTH + x + columnSize * i, y, FIRST_COLUMN_WIDTH + x + columnSize * i, y + height));
		}
		
		g2dPrivate.setFont(TITLES_FONT);
		drawString(g2dPrivate, "Train Number", x + FIRST_COLUMN_WIDTH/2, y + lineSize/2);
		drawString(g2dPrivate, "Train Type", x + FIRST_COLUMN_WIDTH/2, y + lineSize/2 + lineSize);
		drawString(g2dPrivate, "Departure", x + FIRST_COLUMN_WIDTH/2, y + lineSize/2 + lineSize*2);
	}
	
	private void drawString(Graphics2D g2d, String text, double x, double y) {
		int height = g2d.getFont().getSize();
		int width = g2d.getFontMetrics().stringWidth(text);
		g2d.drawString(text, (float) (x - width / 2), (float) (y + height / 2));
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
