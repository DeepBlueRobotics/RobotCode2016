package org.usfirst.frc199.Robot2016;

import java.io.*;
import java.util.ArrayList;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 * Stores all changeable or arbitrary values in a file on the cRIO, as well as a
 * temporary copy in NetworkTables.
 */
public class Preferences {

    private static ArrayList<String> keys = new ArrayList<String>(); // The preference identifiers
    private static NetworkTable table = NetworkTable.getTable("Preferences");
    private static final String DIVIDER = " ";

	/**
	 * Sets a preference value, creating the preference if it does not exist
	 * 
	 * @param key - The name of the preference
	 * @param value - The desired value of the preference
	 */
	public static void set(String key, Object value) {
		table.putString(key, value+"");
		if (!keys.contains(key)) keys.add(key);
	}

	/**
	 * Casts double to an object for regular set method
	 * 
	 * @param key - The name of the preference
	 * @param value - The desired value of the preference
	 */
	public static void set(String key, double value) {
		set(key, Double.valueOf(value));
	}

	/**
	 * Casts boolean to an object for regular set method
	 * 
	 * @param key - The name of the preference
	 * @param value - The desired value of the preference
	 */
	public static void set(String key, boolean value) {
		set(key, Boolean.valueOf(value));
	}

	/**
	 * Returns value of a preference as a double
	 * 
	 * @param key - The name of the preference
	 * @return The value of the preference
	 */
	public static double getDouble(String key) {
		String value = getString(key);
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			System.err.println("Preference is not a double: " + key + DIVIDER + value);
			return 0;
		}
	}

	/**
	 * Returns value of a preference as a boolean
	 * 
	 * @param key - The name of the preference
	 * @return The value of the preference
	 */
	public static boolean getBoolean(String key) {
		String value = getString(key);
		if (!(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))) {
			System.err.println("Preference is not a boolean: " + key + DIVIDER + value);
		}
		return value.equalsIgnoreCase("true");
	}

	/**
	 * Returns value of a preference as a String
	 * 
	 * @param key - The name of the preference
	 * @return The value of the preference
	 */
	public static String getString(String key) {
		if (table.containsKey(key)) {
			return table.getString(key);
		} else {
			System.err.println("Preference not found: " + key);
			set(key, "0");
			return "0";
		}
	}

	/**
	 * Returns whether a preference exists
	 * 
	 * @param key - The name of the preference
	 * @return The existence of the given preference
	 */
	public static boolean contains(String key) {
		return table.containsKey(key);
	}

	/**
	 * Pulls preferences from the file, overwrites any existing preferences
	 */
	public static void read() {
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("/home/lvuser/Preferences.txt"));
			keys.clear();
			for (int i = 0; i < 10000; i++) {
				String line = in.readLine();
				if (line == null) { // End of file
					break;
				} else if(line.length() == 0) { // Newline
					keys.add("");
				} else if (line.contains(DIVIDER)) { // Key and value
					int dividerIndex = line.indexOf(DIVIDER);
					String key = line.substring(0, dividerIndex);
					String value = line.substring(dividerIndex + DIVIDER.length());
					set(key, value);
				} else { // Key without value
					set(line, "0");
				}
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Overwrites the file with current preferences
	 */
	public static void write() {
		BufferedWriter out;
		if (keys.isEmpty()) { // So we don't accidentally delete the preferences file
			return;
		}
		try {
			out = new BufferedWriter(new FileWriter("/home/lvuser/Preferences.txt"));
			for (int i = 0; i < keys.size(); i++) {
				if(!keys.get(i).equals("")){ // Empty String indicates a blank line
					String line = keys.get(i) + DIVIDER + table.getString(keys.get(i));
					out.write(line);
				}
				out.newLine();
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads the preferences file and starts listening to the network table
	 */
	public static void init(){
		read();
		table.addTableListener(new ITableListener() {
			@Override
			public void valueChanged(ITable source, String key, Object value, boolean isNew) {
				// Make sure the code knows all the keys
				if(!keys.contains(key)) keys.add(key);
			}
		}, true);
	}
}