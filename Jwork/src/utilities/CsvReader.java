package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class CsvReader {
	
	private static final String FILE_NAME = "result.csv";
	private static final int NUMBER_OF_LINES = 16;
	
	public static void readCsvFile() throws FileReadingException {
		File file = new File("../" + FILE_NAME);
		Scanner scan = null;
		try {
			scan = new Scanner(file);
			scan.nextLine();
	        int counter = 0;
	        while (scan.hasNextLine()) {
	            counter++;
	            String line = scan.nextLine();
	        }
	        if (counter != NUMBER_OF_LINES) {
	        	throw new FileReadingException(	"There are " + counter + " train schedules in the file " + FILE_NAME + ".\n" +
	        									"Expected: " + NUMBER_OF_LINES + " train schedules.");
	        }
		} catch (FileNotFoundException e) {
			throw new FileReadingException("The file " + FILE_NAME + " could not be found.");
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
	}

}
