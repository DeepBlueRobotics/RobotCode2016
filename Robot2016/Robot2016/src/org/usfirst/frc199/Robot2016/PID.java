package org.usfirst.frc199.Robot2016;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Creates a PID loop, has methods to set the target value and get the output.
 * Automatically generates new SmartDashboard values and preferences.
 */
public class PID {

	private final String name; // The loop's unique identifier
	private double kP = 0.0, kI = 0.0, kD = 0.0, kF = 0.0; // PID constants
	private double input = 0.0; // Scaled value from sensor
	private double sensorValue = 0.0; // Raw value from sensor
	private double target = 0.0; // Target for the PID loop
	private double offset = 0.0; // Offset from input value to the zero value
	private double error = 0.0; // Distance from target value
	private double lastError = 0.0, totalError = 0.0; // Stores previous error
	private double output = 0.0; // Loop output value
	private double interval = 0.0; // Time between loops
	private double rate = 0.0; // Change in error per second
	private Timer timer = new Timer(); // Keeps track of loop frequency

	/**
	 * Instantiates a PID loop
	 * 
	 * @param name - The unique identifier of the system for preferences and
	 *     dashboard values
	 */
	public PID(String name) {
		kP = Preferences.getDouble(name + "_kP");
		kI = Preferences.getDouble(name + "_kI");
		kD = Preferences.getDouble(name + "_kD");
		kF = Preferences.getDouble(name + "_kF");
		SmartDashboard.putNumber("PID/"+name + " kP", kP);
		SmartDashboard.putNumber("PID/"+name + " kI", kI);
		SmartDashboard.putNumber("PID/"+name + " kD", kD);
		SmartDashboard.putNumber("PID/"+name + " kF", kF);
		SmartDashboard.putNumber("PID/"+name + " TestTarget", 0);

		this.name = name;
		timer.start();
		displayData();
	}

	/**
	 * Updates the PID loop using new input data, call before using other
	 * methods
	 * 
	 * @param newValue - The new input value from the sensor
	 */
	public void update(double newValue) {
		kP = Preferences.getDouble(name + "_kP");
		kI = 0;//Preferences.getDouble(name + "_kI");
		kD = Preferences.getDouble(name + "_kD");
		kF = Preferences.getDouble(name + "_kF");
		sensorValue = newValue;
		interval = timer.get();
		input = sensorValue; //* Preferences.getDouble(name + "SensorRatio") - offset;
		lastError = error;
		error = target - input;
		if (Math.abs(output) < Preferences.getDouble(name + "IntegralLimit")
				&& interval < 1.0)
			totalError += error * interval;
		if (interval != 0)
			rate = (error - lastError) / interval;
		output = kP * error + kI * totalError + kD * rate;
		timer.reset();
		displayData();
		if(!this.name.equals("DriveDistance")) return;
		SmartDashboard.putNumber("kP value", kP);
		SmartDashboard.putNumber("input value", input);
		SmartDashboard.putNumber("target value", target);
		SmartDashboard.putNumber("output value", output);
		SmartDashboard.putNumber("error value", error);
	}

	/**
	 * Getter method for error (call update first)
	 * 
	 * @return The displacement from the target value
	 */
	public double getError() {
		return error;
	}

	/**
	 * Getter method for input (call update first)
	 * 
	 * @return The raw sensor value
	 */
	public double getInputValue() {
		return input;
	}

	/**
	 * Getter method for output (call update first)
	 * 
	 * @return The PID output value
	 */
	public double getOutput() {
		return output;
	}

	/**
	 * Setter method for target
	 * 
	 * @param newTarget - The target value the PID will attempt to reach
	 */
	public void setTarget(double newTarget) {
		target = newTarget;
		error = target - input;
		totalError = error * kF;
		displayData();
	}

	/**
	 * Getter method for target
	 * 
	 * @return The current target for the PID loop
	 */
	public double getTarget() {
		return target;
	}

	/**
	 * Sets the relative value of the current location
	 * 
	 * @param value - The desired value of the current location
	 */
	public void setRelativeLocation(double value) {
		offset += input - value;
		input = value;
		setTarget(target);
	}

	/**
	 * Determines if error and rate are within acceptable tolerances
	 * 
	 * @return Whether the PID loop has reached the target
	 */
	public boolean reachedTarget() {
		return Math.abs(error) < Preferences.getDouble(name + "ErrorTolerance")
				&& Math.abs(rate) < Preferences.getDouble(name
						+ "RateTolerance");
	}

	/**
	 * Displays data on the SmartDashboard
	 */
	private void displayData() {
		SmartDashboard.putNumber("PID/"+name + " Error", error);
		SmartDashboard.putNumber("PID/"+name + " Target", target);
		SmartDashboard.putNumber("PID/"+name + " Input", input);
		SmartDashboard.putNumber("PID/"+name + " Output", output);
		SmartDashboard.putNumber("PID/"+name + " Interval", interval);
		SmartDashboard.putNumber("PID/"+name + " Rate", rate);
		SmartDashboard.putNumber("PID/"+name + " SensorValue", sensorValue);
		SmartDashboard.putNumber("PID/"+name + " TotalError", totalError);
		SmartDashboard.putBoolean("PID/"+name + " Reached Target", reachedTarget());
	}
}