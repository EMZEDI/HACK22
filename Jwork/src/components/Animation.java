package components;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Animation extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Train> trains;
	private boolean animationRunning = false;
	private int framesPerMinute;
	
	private final long SLEEP_TIME = 50;
	
	public Animation(ArrayList<Train> trains) {
		setBackground(Color.white);
		this.trains = trains;
	}

	@Override
	public void run() {
		while(animationRunning) {
			System.out.println("Animation!!  Speed:" + framesPerMinute);// TODO
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
		// TODO
		repaint();
	}

}
