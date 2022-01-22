package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import components.Train;

public abstract class CsvReader {
	
	private static final String FILE_NAME = "result.csv";
	private static final int NUMBER_OF_LINES = 16;
	private static final int MINIMUM_ARRIVAL_HOUR = 7;
	
	public static ArrayList<Train> readCsvFile() throws FileReadingException {
		ArrayList<Train> trains = new ArrayList<Train>();
		File file = new File("../" + FILE_NAME);
		Scanner scan = null;
		try {
			scan = new Scanner(file);
			scan.nextLine();
	        int counter = 0;
	        while (scan.hasNextLine()) {
	            counter++;
	            String line = scan.nextLine();
	            LineReader lineReader = new LineReader(line);
	            
	            int number = 0;
	            try {
	            	number = Integer.parseInt(lineReader.nextField());
	            } catch (NumberFormatException e) {
	            	throwFormatError();
	            }
	            
	            Train.Type type = null;
	            switch (lineReader.nextField()) {
	            	case "L4":
	            		type = Train.Type.L4;
	            		break;
	            	case "L8":
	            		type = Train.Type.L8;
	            		break;
	            	default:
	            		throwFormatError();	
	            }
	            
	            int AArrivalTime = convertToMinutes(lineReader.nextField());
	            
	            lineReader.nextField();
	            lineReader.nextField();
	            
	            int BArrivalTime = convertToMinutes(lineReader.nextField());
	            
	            lineReader.nextField();
	            lineReader.nextField();
	            
	            int CArrivalTime = convertToMinutes(lineReader.nextField());
	            
	            lineReader.nextField();
	            lineReader.nextField();
	            
	            int UnionArrivalTime = convertToMinutes(lineReader.nextField());
	            
	            trains.add(new Train(number, type, AArrivalTime, BArrivalTime, CArrivalTime, UnionArrivalTime));
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
		return trains;
	}
	
	private static void throwFormatError() throws FileReadingException {
		throw new FileReadingException("Some fields are in incorrect format in the file " + FILE_NAME + ".");
	}
	
	private static int convertToMinutes(String time) throws FileReadingException {
		int splitIndex = time.indexOf(":");
		if (splitIndex == -1) {
			throwFormatError();
		}
		int hours = 0, minutes = 0;
		try {
			hours = Integer.parseInt(time.substring(0, splitIndex));
			minutes = Integer.parseInt(time.substring(splitIndex + 1));
		} catch (NumberFormatException e) {
			throwFormatError();
		}
		return (hours - MINIMUM_ARRIVAL_HOUR) * 60 + minutes;
	}
	
	private static class LineReader {
		
		String line;
		
		public LineReader(String line) {
			this.line = line;
		}
		
		public String nextField() throws FileReadingException {
			if (line.isEmpty()) {
				throw new FileReadingException("Some fields are missing in the file " + FILE_NAME + ".");
			}
			int commaIndex = line.indexOf(',');
			if (commaIndex == -1) {
				String field = line;
				line = "";
				return field;
			} else {
				String field = line.substring(0, commaIndex);
				line = line.substring(commaIndex + 1);
				return field;
			}
		}
		
	}

}
