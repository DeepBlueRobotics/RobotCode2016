package org.usfirst.frc199.Robot2016;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Displays data on Smart Dashboard.
 */
public class SmartDashboardHandler {
	String label;
	
	public void identifyAs(String name) {
		label = name;
	}
	
	public void putNum(String field, double value) {
		SmartDashboard.putNumber(label+" "+field, value);
	}
	
	public void putBool(String field, boolean value) {
		SmartDashboard.putBoolean(label+" "+field+"?", value);
	}
}