package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import components.Train;

public abstract class CsvReader {
	
	private static final String SCHEDULE_FILE_NAME = "output.csv";
	private static final String SCHEDULE_FILE_PATH = "../static/optimized/" + SCHEDULE_FILE_NAME;
	private static final String PASSENGERS_FILE_NAME = "passengerArrivals.csv";
	private static final String PASSENGERS_FILE_PATH = PASSENGERS_FILE_NAME;
	private static final int NUMBER_OF_TRAINS = 16;
	private static final int MINIMUM_ARRIVAL_HOUR = 7;
	private static final int NUMBER_OF_TIME_SLOTS = 19;
	
	public static ArrayList<Train> readScheduleFile() throws FileReadingException {
		ArrayList<Train> trains = new ArrayList<Train>();
		File file = new File(SCHEDULE_FILE_PATH);
		Scanner scan = null;
		try {
			scan = new Scanner(file);
			scan.nextLine();
	        int counter = 0;
	        while (scan.hasNextLine()) {
	            String line = scan.nextLine();
	        	if (!line.isBlank()) {
	        		counter++;
		            LineReader lineReader = new LineReader(line);
		            
		            int number = 0;
		            try {
		            	number = Integer.parseInt(lineReader.nextField());
		            } catch (NumberFormatException e) {
		            	throwFormatError(SCHEDULE_FILE_NAME);
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
		            		throwFormatError(SCHEDULE_FILE_NAME);	
		            }
		            
		            int AArrivalTime = convertToMinutes(lineReader.nextField(), SCHEDULE_FILE_NAME);
		            
		            lineReader.nextField();
		            lineReader.nextField();
		            
		            int BArrivalTime = convertToMinutes(lineReader.nextField(), SCHEDULE_FILE_NAME);
		            
		            lineReader.nextField();
		            lineReader.nextField();
		            
		            int CArrivalTime = convertToMinutes(lineReader.nextField(), SCHEDULE_FILE_NAME);
		            
		            lineReader.nextField();
		            lineReader.nextField();
		            
		            int UnionArrivalTime = convertToMinutes(lineReader.nextField(), SCHEDULE_FILE_NAME);
		            
		            trains.add(new Train(number, type, AArrivalTime, BArrivalTime, CArrivalTime, UnionArrivalTime));
	        	}
	        }
	        if (counter != NUMBER_OF_TRAINS) {
	        	throw new FileReadingException(	"There are " + counter + " train schedules in the file " + SCHEDULE_FILE_NAME + ".\n" +
	        									"Expected: " + NUMBER_OF_TRAINS + " train schedules.");
	        }
		} catch (FileNotFoundException e) {
			throw new FileReadingException("The file " + SCHEDULE_FILE_NAME + " could not be found.");
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
		return trains;
	}
	
	public static ArrayList<int[]> readPassengersFile() throws FileReadingException {
		int[] stationAPassengers = new int[NUMBER_OF_TIME_SLOTS];
		int[] stationBPassengers = new int[NUMBER_OF_TIME_SLOTS];
		int[] stationCPassengers = new int[NUMBER_OF_TIME_SLOTS];
		File file = new File(PASSENGERS_FILE_PATH);
		Scanner scan = null;
		try {
			scan = new Scanner(file);
			scan.nextLine();
	        while (scan.hasNextLine()) {
	            String line = scan.nextLine();
	            int commaIndex = line.indexOf(',');
	            if (commaIndex == -1) {
	            	throwFormatError(PASSENGERS_FILE_NAME);
	            }
	            String stationLetter = line.substring(0, commaIndex);
	            line = line.substring(commaIndex + 1);
	            int[] station;
	            switch (stationLetter) {
		            case "A":
		            	station = stationAPassengers;
		            	break;
		            case "B":
		            	station = stationBPassengers;
		            	break;
		            case "C":
		            	station = stationCPassengers;
		            	break;
		            default:
		            	station = null;
		            	throwFormatError(PASSENGERS_FILE_NAME);
		            	break;	
	            }
	            commaIndex = line.indexOf(',');
	            if (commaIndex == -1) {
	            	throwFormatError(PASSENGERS_FILE_NAME);
	            }
	            String arrivalTime = line.substring(0, commaIndex);
	            line = line.substring(commaIndex + 1);
	            int numberOfPassengers = 0;
	            try {
	            	numberOfPassengers = Integer.parseInt(line);
	            } catch (NumberFormatException e) {
	            	throwFormatError(PASSENGERS_FILE_NAME);
	            }
	            int timeIndex = convertToMinutes(arrivalTime, PASSENGERS_FILE_NAME)/10;
	            if (timeIndex < 0 || timeIndex >= NUMBER_OF_TIME_SLOTS) {
	            	throw new FileReadingException(	"Some arrival times are outside of the schedule time period in the file " + PASSENGERS_FILE_NAME + ".\n" +
	            									"Expected time period: between 7:00 and 10:00.");
	            }
	            station[timeIndex] = numberOfPassengers; 
	        }
		} catch (FileNotFoundException e) {
			throw new FileReadingException("The file " + PASSENGERS_FILE_NAME + " could not be found.");
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
		ArrayList<int[]> list = new ArrayList<int[]>();
		list.add(stationAPassengers);
		list.add(stationBPassengers);
		list.add(stationCPassengers);
		return list;
	}
	
	private static void throwFormatError(String fileName) throws FileReadingException {
		throw new FileReadingException("Some fields are in an incorrect format in the file " + fileName + ".");
	}
	
	private static int convertToMinutes(String time, String fileName) throws FileReadingException {
		int splitIndex = time.indexOf(":");
		if (splitIndex == -1) {
			throwFormatError(fileName);
		}
		int hours = 0, minutes = 0;
		try {
			hours = Integer.parseInt(time.substring(0, splitIndex));
			minutes = Integer.parseInt(time.substring(splitIndex + 1));
		} catch (NumberFormatException e) {
			throwFormatError(fileName);
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
				throw new FileReadingException("Some fields are missing in the file " + SCHEDULE_FILE_NAME + ".");
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
