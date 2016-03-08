package org.usfirst.frc199.Robot2016;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * An interface for subsystems capable of being displayed on the dashboard.
 */
public interface DashboardSubsystem {

	/**
	 * Puts all desired data on SmartDashboard (subsystems should override this
	 * method!!!)
	 */
	public default void displayData() {

	}

	/**
	 * Displays a value on SmartDashboard
	 * 
	 * @param key - The name for use on SmartDashboard
	 * @param value - The value to display
	 */
	default void display(String key, Object value) {
		key = getKey(key);
		if (value instanceof String) {
			SmartDashboard.putString(key, (String) value);
		} else if (value instanceof Double) {
			SmartDashboard.putNumber(key, (double) value);
		} else if (value instanceof Integer) {
			SmartDashboard.putNumber(key, ((Integer) value).doubleValue());
		} else if (value instanceof Boolean) {
			SmartDashboard.putBoolean(key, (boolean) value);
		} else if (value instanceof Sendable) {
			SmartDashboard.putData(key, (Sendable) value);
		} else {
			System.out.println(key + " = " + value);
		}
	}

	/**
	 * Gets number at key.
	 * 
	 * @param key Key where searched. 
	 * @return Double at given key.
	 */
	default double getNumber(String key) {
		return SmartDashboard.getNumber(getKey(key));
	}

	/**
	 * Gets number at key with default value.
	 * 
	 * @param key Key where searched
	 * @param defaultValue Default value if no default at key.
	 * @return Double at key.
	 */
	default double getNumber(String key, double defaultValue) {
		return SmartDashboard.getNumber(getKey(key), defaultValue);
	}

	/**
	 * Gets string at key.
	 * 
	 * @param key Key where searched.
	 * @return String at given key. 
	 */
	default String getString(String key) {
		return SmartDashboard.getString(getKey(key));
	}

	/**
	 * Returns a string at given key
	 * 
	 * @param key Key searched
	 * @param defaultValue Default result if no key.
	 * @return The result given at the key.
	 */
	default String getString(String key, String defaultValue) {
		return SmartDashboard.getString(getKey(key), defaultValue);
	}

	/**
	 * Returns the boolean result at key.
	 * 
	 * @param key Key where boolean is returned from.
	 * @return Boolean result
	 */
	default boolean getBoolean(String key) {
		return SmartDashboard.getBoolean(getKey(key));
	}

	/**
	 * Modifies Smartdashboard keys for compatibility with the subsystem widget.
	 * 
	 * @param key Key for future use
	 * @param defaultValue Automatic value used
	 * @return Current boolean value.
	 */
	default boolean getBoolean(String key, boolean defaultValue) {
		return SmartDashboard.getBoolean(getKey(key), defaultValue);
	}

	/**
	 * Modifies SmartDashboard keys for compatibility with the subsystem widget
	 * 
	 * @param originalKey - The name of the key
	 * @return A modified version of the key
	 */
	default String getKey(String originalKey) {
		return getClass().getSimpleName() + "/" + originalKey;
	}
}
