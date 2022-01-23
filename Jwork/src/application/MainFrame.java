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
	private static final int BUTTON_HEIGHT = 40;
	private static final int NUMBER_OF_SPEEDS = 5;
	

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
			ArrayList<Train> trains = CsvReader.readScheduleFile();
			ArrayList<int[]> passengerArrivals = CsvReader.readPassengersFile();
			MainFrame frame = new MainFrame(trains, passengerArrivals);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public MainFrame(ArrayList<Train> trains, ArrayList<int[]> passengerArrivals) {
		setMinimumSize(new Dimension(900, 500));
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
				int horizontalSpace = (pnlButtons.getWidth() - 2 * BUTTON_WIDTH) / 3;
				int verticalSpace = (pnlButtons.getHeight() - BUTTON_HEIGHT) / 2;
				btnStart.setLocation(horizontalSpace, verticalSpace);
				btnReset.setLocation(horizontalSpace * 2 + BUTTON_WIDTH, verticalSpace);
				lblSpeed.setLocation(pnlSpeed.getWidth()/2 - lblSpeed.getWidth()/2, pnlSpeed.getHeight()/2 - (sldrSpeed.getHeight() + lblSpeed.getHeight())/2);
				sldrSpeed.setLocation(pnlSpeed.getWidth()/2 - sldrSpeed.getWidth()/2, pnlSpeed.getHeight()/2 - (sldrSpeed.getHeight() - lblSpeed.getHeight())/2);
				animation.setSize(getWidth() - animation.getX() - MARGIN - 15, getHeight() - animation.getY() - MARGIN - 35);
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(243, 243, 240));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		animation = new Animation(trains, passengerArrivals);
		animation.setLocation(MARGIN, MARGIN + TOP_HEIGHT);
		contentPane.add(animation);
		
		pnlTop = new JPanel();
		pnlTop.setBackground(contentPane.getBackground());
		pnlTop.setLocation(MARGIN, MARGIN);
		pnlTop.setLayout(null);
		contentPane.add(pnlTop);
		
		lblTitle = new JLabel("Visualization of Train Schedule");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setSize(350, 40);
		pnlTop.add(lblTitle);
		
		pnlButtons = new JPanel();
		pnlButtons.setBackground(contentPane.getBackground());
		pnlButtons.setLayout(null);
		pnlTop.add(pnlButtons);
		
		pnlSpeed = new JPanel();
		pnlSpeed.setBackground(contentPane.getBackground());
		pnlSpeed.setLayout(null);
		pnlTop.add(pnlSpeed);
		
		btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnStart.getText().equals("START")) {
					animation.start();
					btnStart.setText("PAUSE");
				} else {
					animation.stop();
					btnStart.setText("START");
				}
			}
		});
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnStart.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		btnStart.setBackground(new Color(152, 251, 152));
		pnlButtons.add(btnStart);
		
		btnReset = new JButton("RESET");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				animation.reset();
			}
		});
		btnReset.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnReset.setBackground(new Color(255, 182, 193));
		btnReset.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		pnlButtons.add(btnReset);
		
		lblSpeed = new JLabel("Animation Speed:");
		lblSpeed.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpeed.setSize(300, 30);
		pnlSpeed.add(lblSpeed);
		
		sldrSpeed = new JSlider();
		sldrSpeed.setMaximum(NUMBER_OF_SPEEDS);
		sldrSpeed.setMinimum(1);
		sldrSpeed.setValue(NUMBER_OF_SPEEDS/2 + 1);
		sldrSpeed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!sldrSpeed.getValueIsAdjusting()) {
					animation.setSpeed(NUMBER_OF_SPEEDS - sldrSpeed.getValue());
				}
			}
		});
		sldrSpeed.setSnapToTicks(true);
		sldrSpeed.setPaintTicks(true);
		sldrSpeed.setMajorTickSpacing(1);
		sldrSpeed.setSize(300, 40);
		pnlSpeed.add(sldrSpeed);
		animation.setSpeed(NUMBER_OF_SPEEDS - sldrSpeed.getValue());
	}
}
