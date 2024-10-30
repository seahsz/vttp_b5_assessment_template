package vttp.batch5.sdf.task01;

// Use this class as the entry point of your program
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import vttp.batch5.sdf.task01.models.BikeEntry;
import vttp.batch5.sdf.task01.models.BikeEntryWithTotal;

import java.util.ArrayList;
import java.util.Comparator;

public class Main {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: java -cp classes task01.vttp.batch5.sdf.task01.Main <csv_file>");
			return;
		}

		String fileName = args[0];

		File csvFile = new File(fileName);

		// check if file exists --> if not exit main
		if (!csvFile.exists()) {
			System.err.println("Please enter a valid file path (root dir is vttp.batch5.sdf.task01)");
			return;
		}

		List<BikeEntry> allEntries = parseCSV(csvFile);

		List<BikeEntryWithTotal> EntriesWithTotal = addTotalToBikeEntry(allEntries);

		List<BikeEntryWithTotal> sortedDescendingByTotal = EntriesWithTotal.stream()
				.sorted(Comparator.comparing(entry -> entry.getTotal(), Comparator.reverseOrder()))
				.collect(Collectors.toList());

		// for (int i = 0; i < 30; i++) {
		// BikeEntryWithTotal temp = sortedByTotal.get(i);
		// System.out.println("Season: " + temp.getBe().getSeason() + ", Month: " +
		// temp.getBe().getMonth()
		// + ", Total: " + temp.getTotal());
		// }

		for (int i = 0; i < 5; i++)
			customPrint(sortedDescendingByTotal.get(i), i);

	}

	public static List<BikeEntry> parseCSV(File file) {

		List<BikeEntry> BikeEntries = new ArrayList<>();
		int count = 0; // check num lines in csv file (- header)

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String[] headers = parseLine(br.readLine()); // throw away the header
			String line;

			while ((line = br.readLine()) != null) {

				String[] info = parseLine(line);

				BikeEntries.add(BikeEntry.toBikeEntry(info));
				count++;
			}

		} catch (FileNotFoundException ex) {
			System.err.println("System error: unable to find file");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.err.println("System error: error reading file");
			ex.printStackTrace();
		}

		System.out.println("Number of lines read in CSV: " + count);
		return BikeEntries;

	}

	public static String[] parseLine(String line) {

		List<String> result = new ArrayList<>();
		StringBuilder currField = new StringBuilder();
		boolean isInQuotes = false;

		for (int i = 0; i < line.length(); i++) {

			char currChar = line.charAt(i);

			// just in case, but doesn't seem like csv file has any quoted fields
			if (currChar == '\"')
				isInQuotes = !isInQuotes;

			else if (currChar == ',' && !isInQuotes) {
				result.add(currField.toString().trim());
				currField.setLength(0);
			} else {
				currField.append(currChar);
			}

		}

		result.add(currField.toString().trim());

		/*
		 * set to String[0] if not there might be null elements in the String[]
		 * if result smaller than our specified array length
		 */
		return result.toArray(new String[0]);

	}

	public static List<BikeEntryWithTotal> addTotalToBikeEntry(List<BikeEntry> beList) {

		List<BikeEntryWithTotal> result = new ArrayList<>();

		for (int i = 0; i < beList.size(); i++) {

			BikeEntry be = beList.get(i);
			int total = be.getCasual() + be.getRegistered();

			result.add(new BikeEntryWithTotal(be, total));
		}

		return result;
	}

	public static void customPrint(BikeEntryWithTotal be, int pos) {

		int total = be.getTotal();
		String position = "";
		String season = Utilities.toSeason(be.getBe().getSeason());
		String day = Utilities.toWeekday(be.getBe().getWeekday());
		String month = Utilities.toMonth(be.getBe().getMonth());
		String weather = "";
		String holiday = "";

		switch (pos) {
			case 0:
				position = "highest";
				break;
			case 1:
				position = "second highest";
				break;
			case 2:
				position = "third highest";
				break;
			case 3:
				position = "fourth highest";
				break;
			case 4:
				position = "fifth highest";
				break;

			default:
				System.out.println("Position > 5 (Not accepted)");
				return;
		}

		if (be.getBe().isHoliday())
			holiday = "a holiday";
		else
			holiday = "not a holiday";

		switch (be.getBe().getWeather()) {
			case 1:
				weather = "Clear, Few clouds, Partly cloudy, Partly cloudy";
				break;
			case 2:
				weather = "Mist + Cloudy, Mist + Broken clouds, Mist + Few clouds, Mist";
				break;
			case 3:
				weather = "Light Snow, Light Rain + Thunderstorm + Scattered clouds, Light Rain + Scattered clouds";
				break;
			case 4:
				weather = "Heavy Rain + Ice Pallets + Thunderstorm + Mist, Snow + Fog";
				break;

			default:
				weather = "wrong weather!!!!";
				break;
		}

		System.out.printf(
				"The %s recorded number of cyclists was in %s, on a %s in the month of %s. There was a total of %d cyclists. The weather was %s. %s was %s.\n",
				position, season, day, month, total, weather, day, holiday);

		System.out.println();
	}

}
