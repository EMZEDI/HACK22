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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import components.Train;
import utilities.CsvReader;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import components.Animation;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JPanel pnlTop;
	private JLabel lblTitle;
	private JPanel pnlButtons;
	private JPanel pnlSpeed;
	private JButton btnStart;
	private JButton btnReset;
	private JLabel lblSpeed;
	private JSlider sldrSpeed;
	private Animation animation;
	
	private static final int WINDOW_WIDTH = 1100;
	private static final int WINDOW_HEIGHT = 600;
	private static final int MARGIN = 10;
	private static final int TOP_HEIGHT = 120;
	private static final int BUTTON_WIDTH = 150;
	private static final int BUTTON_HEIGHT = 30;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			MainFrame frame = new MainFrame(CsvReader.readScheduleFile());
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public MainFrame(ArrayList<Train> trains) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(screenSize.width/2 - WINDOW_WIDTH/2, screenSize.height/2 - WINDOW_HEIGHT/2, WINDOW_WIDTH, WINDOW_HEIGHT);
		contentPane = new JPanel();
		contentPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				pnlTop.setSize(contentPane.getWidth() - MARGIN * 2, TOP_HEIGHT);
				lblTitle.setLocation(pnlTop.getWidth()/2 - lblTitle.getWidth()/2, 0);
				pnlButtons.setBounds(0, lblTitle.getY() + lblTitle.getHeight(), pnlTop.getWidth()/2, pnlTop.getHeight() - lblTitle.getHeight());
				pnlSpeed.setBounds(pnlTop.getWidth()/2, pnlButtons.getY(), pnlTop.getWidth()/2, pnlButtons.getHeight());
				int horizontanSpace = (pnlButtons.getWidth() - 2 * BUTTON_WIDTH) / 3;
				int verticalSpace = (pnlButtons.getHeight() - 2 * BUTTON_HEIGHT) / 2;
				pnlButtons.setLayout(new MigLayout("", "[" + horizontanSpace + "px:" + horizontanSpace + "px:" + horizontanSpace + "px][grow,fill][" + horizontanSpace + "px:" + horizontanSpace + "px:" + horizontanSpace + "px][grow,fill][" + horizontanSpace + "px:" + horizontanSpace + "px:" + horizontanSpace + "px]",
						"[" + verticalSpace + "px:" + verticalSpace + "px:" + verticalSpace + "px][grow,fill][" + verticalSpace + "px:" + verticalSpace + "px:" + verticalSpace + "px]"));
				pnlButtons.add(btnReset, "cell 3 1,grow");
				pnlButtons.add(btnStart, "cell 1 1,grow");
				lblSpeed.setLocation(pnlSpeed.getWidth()/2 - lblSpeed.getWidth()/2, pnlSpeed.getHeight()/2 - (sldrSpeed.getHeight() + lblSpeed.getHeight())/2);
				sldrSpeed.setLocation(pnlSpeed.getWidth()/2 - sldrSpeed.getWidth()/2, pnlSpeed.getHeight()/2 - (sldrSpeed.getHeight() - lblSpeed.getHeight())/2);
				animation.setSize(getWidth() - animation.getX() - MARGIN - 15, getHeight() - animation.getY() - MARGIN - 35);
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(240, 240, 240));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		animation = new Animation(trains);
		animation.setLocation(MARGIN, MARGIN + TOP_HEIGHT);
		contentPane.add(animation);
		
		pnlTop = new JPanel();
		pnlTop.setBackground(contentPane.getBackground());
		pnlTop.setLocation(MARGIN, MARGIN);
		pnlTop.setLayout(null);
		contentPane.add(pnlTop);
		
		lblTitle = new JLabel("Vizualisation of Train Schedule");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setSize(350, 40);
		pnlTop.add(lblTitle);
		
		pnlButtons = new JPanel();
		pnlButtons.setBackground(contentPane.getBackground());
		pnlButtons.setLayout(new MigLayout("", "[][][][][]","[][][]"));
		pnlTop.add(pnlButtons);
		
		pnlSpeed = new JPanel();
		pnlSpeed.setBackground(contentPane.getBackground());
		pnlSpeed.setLayout(null);
		pnlTop.add(pnlSpeed);
		
		btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				animation.start();
			}
		});
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnStart.setBackground(new Color(152, 251, 152));
		pnlButtons.add(btnStart, "cell 1 1,grow");
		
		btnReset = new JButton("RESET");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				animation.reset();
			}
		});
		btnReset.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnReset.setBackground(new Color(255, 182, 193));
		pnlButtons.add(btnReset, "cell 3 1,grow");
		
		lblSpeed = new JLabel("Animation Speed:");
		lblSpeed.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpeed.setSize(300, 30);
		pnlSpeed.add(lblSpeed);
		
		sldrSpeed = new JSlider();
		sldrSpeed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!sldrSpeed.getValueIsAdjusting()) {
					animation.setFramesPerMinute(sldrSpeed.getValue());
				}
			}
		});
		sldrSpeed.setMaximum(12);
		sldrSpeed.setSnapToTicks(true);
		sldrSpeed.setPaintTicks(true);
		sldrSpeed.setMajorTickSpacing(3);
		sldrSpeed.setSize(300, 40);
		pnlSpeed.add(sldrSpeed);
	}
}
