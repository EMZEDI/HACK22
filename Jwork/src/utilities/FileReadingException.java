package utilities;

import javax.swing.JOptionPane;

public class FileReadingException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private static final String STANDARD_MESSAGE = "An error occurred while reading the .csv file containing the train schedule.";
	
	public FileReadingException() {
		super(STANDARD_MESSAGE);
		showDialog(STANDARD_MESSAGE);
	}
	
	public FileReadingException(String message) {
		super(message);
		showDialog(message);
	}
	
	private void showDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
	}

}
