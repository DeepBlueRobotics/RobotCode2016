package org.usfirst.frc199.Robot2016;

import edu.wpi.first.wpilibj.Timer;

/**
 * Creates a PID loop, that automatically generates new
 * SmartDashboard values and preferences.
 */
public class PID {
	// parameters (provided by user)
	private double target; // setpoint; the value PID will attempt to reach
	private double P; // Proportional factor
	private double I; // Integral factor
	private double D; // Derivative factor
	private double errorTolerance;
	private double rateTolerance;
	
	// state
	private boolean alreadyReceivedInput; // true if lastError has a value (to handle initialization properly)
	private Timer intervalTimer; // keeps track of loop frequency
	private double lastError; // previous displacement from target value
	private double totalError; // weighted sum of previous
	private double rate;
	
	private SmartDashboardHandler ui;
	
	public PID(String subsystem) {
		ui.identifyAs(subsystem+"/PID");
		reset();
	}
	
	public void setTarget(double value) {
		target = value;
		reset();
		
		ui.putNum("Target", target);
	}
	
	/**
	 * @return time between consecutive loops
	 */
	private double interval() {
		double timeDifference = intervalTimer.get();
		intervalTimer.reset();
		return timeDifference;
	}
	
	/**
	 * Get rid of built-up state.
	 */
	private void reset() {
		alreadyReceivedInput = false;
		totalError = 0;
		rate = 0;
	}
	
	/**
	 * Connects source input, caches results.
	 * Call this method first in order for the input to register.
	 * @param input new raw data from sensor
	 */
	public void update(double input) {
		final double thisError = target-input;
		
		if (alreadyReceivedInput) {
			final double dError = thisError-lastError;
			final double dt = interval();
			totalError += thisError*dt;
			rate = dError/dt;
			
			ui.putNum("Interval", dt);
			ui.putNum("Output", output());
			ui.putNum("Rate", rate);
		} else {
			alreadyReceivedInput = true;
			intervalTimer.start();
		}
		
		lastError = thisError;
		
		ui.putNum("Input", input);
		ui.putNum("Error", lastError);
		ui.putNum("Total Error", totalError);
		ui.putBool("Reached Target", reachedTarget());
	}
	
	public double output() {
		assert alreadyReceivedInput;
	   	return P*lastError + I*totalError + D*rate;
	}
	
	/**
	 * @return whether error and rate are within acceptable tolerances
	 */
	public boolean reachedTarget() {
		return Math.abs(lastError) < errorTolerance
		    && Math.abs(rate) < rateTolerance;
	}
}