/*
 * PLAN
 * 
 * For the objects, time is an int in minutes, starting at 0
 * For the animatoin, tine is a double in minutes, starting at 0
 * 
 * Classes: MainFrame(JFrame), Animation, CsvReader, Drawable, Passenger, Train(Drawable), Station(Drawable), Table(JTable)
 * 
 * Train (drawable):
 * - int number
 * - Enum type
 * - int A arrival time
 * - int B arrival time
 * - int C arrival time
 * - int Union station arrival time
 * - int available capacity
 * - int number of passengers
 * - double x, y, width, height -> all trains should start at station A, even when they are not visible
 * 
 * Station (drawable):
 * - String name
 * - int[] passenger arrival times and numbers -> arrivals = new int[19] where, for example, arrivals[5] = 150 for station A
 * - ArrayList<Passenger> passengers waiting => QUEUE?
 * - double x, y, width, height
 * 
 * Passenger:
 * - int waiting time
 * + waitOneMinute()
 * 
 * ALGORITHM FOR THE ANIMATION:
 * Increment current time (in double, so change not necessarily visible on the rounded display)
 * Update the position of each train using the current time and the different arrival times
 * If a minute has passed:
 *   Each waiting passenger waits one more minute
 *   Update the list of trains to be drawn
 *   Update the table colors
 *   Update the passengers at each station (based on arrivals), including adding them to the lists
 *   Get some passengers in the train and remove them from waiting lists
 *   Update average waiting time
 */

package application;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
