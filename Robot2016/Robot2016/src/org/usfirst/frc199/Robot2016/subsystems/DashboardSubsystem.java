package org.usfirst.frc199.Robot2016.subsystems;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public interface DashboardSubsystem {
	
	/**
	 * Puts all desired data on SmartDashboard
	 */
	public default void displayData() {
		
	}
	
	/**
	 * Displays a value on SmartDashboard
	 * @param key - The name for use on SmartDashboard
	 * @param value - The value to display
	 */
	public default void display(String key, Object value) {
		key = getKey(key);
		if(value instanceof String) {
			SmartDashboard.putString(key, (String)value);
		} else if(value instanceof Double || value instanceof Integer) {
			SmartDashboard.putNumber(key, (double)value);
		} else if(value instanceof Boolean) {
			SmartDashboard.putBoolean(key, (boolean)value);
		} else if(value instanceof Sendable) {
			SmartDashboard.putData(key, (Sendable) value);
		} else {
			System.out.println(key + " = " + value);
		}
	}
	
	public default String getKey(String originalKey) {
		return getName() + "/" + originalKey;
	}
	
	public default String getName() {
		return getClass().getName();
	}
}
