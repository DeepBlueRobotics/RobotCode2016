package org.usfirst.frc199.Robot2016.subsystems;

import org.usfirst.frc199.Robot2016.DashboardSubsystem;
import org.usfirst.frc199.Robot2016.Robot;
import org.usfirst.frc199.Robot2016.RobotMap;
import org.usfirst.frc199.Robot2016.motioncontrol.PID;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Brings boulders into the robot.
 */
public class Intake extends Subsystem implements DashboardSubsystem {

	private final SpeedController rollerMotor = RobotMap.intakeRollerMotor;
	private final SpeedController pivotMotor = RobotMap.intakePivotMotor;
	private final DigitalInput upperLimit = RobotMap.intakeUpperLimit;
	private final DigitalInput lowerLimit = RobotMap.intakeLowerLimit;
	private final DigitalInput ballSensor = RobotMap.intakeBallSensor;
	private final Encoder pivotEncoder = RobotMap.intakePivotEncoder;
	
	private PID pivotPID = new PID("Intake");
	private double midLowAngle = Robot.getPref("MidLowAngle", 0); // Measure angle between mid and low positions
	private double midHighAngle = Robot.getPref("MidHighAngle", 0); // Measure angle between mid and high positions
	private double totalAngle = midLowAngle + midHighAngle;

	public void initDefaultCommand() {
	}

	public void setRoller(double speed) {
		rollerMotor.set(-speed);
	}
	
	public boolean getUpperLimit() {
		return !upperLimit.get();
	}
	
	public boolean getLowerLimit() {
		return !lowerLimit.get();
	}

	/**
	 * Runs pivot motor
	 * 
	 * @param speed - The speed at which the motor should run
	 */
	public void pivot(double speed) {
		if(speed>0&&!getUpperLimit()||speed<0&&!getLowerLimit()) {
			pivotMotor.set(-speed);
		} else {
			pivotMotor.set(0);
		}
	}
	
	/**
	 * Sets pivotPID target angle
	 * 
	 * @param initPos - Number representing the intake's initial position
	 * @param up - boolean indicating whether the intake is raised or lowered
	 */
	public void setTargetAngle(int initPos, int dir) {
		double target = 0;
		
		switch (initPos) {
		case 1: 
			target = midHighAngle;
			break;
		case 2:
			target = totalAngle;
			break;
		case 3:
			target = midLowAngle;
			break;
		}
		
		pivotPID.setTarget(target * dir);
	}
	
	/**
	 * Updates pivotPID
	 */
	public double updateAngle() {
		pivotPID.update(pivotEncoder.getDistance());
		return pivotPID.getOutput();
	}
	
	
	/**
	 * Returns true when the intake has reached one spot past its initial position  
	 * 
	 * @param initPos - The starting position of the intake
	 */
	public boolean shouldStop(int initPos) {
		switch (initPos) {
		case 1: 
			return pivotPID.reachedTarget() || getLowerLimit();
		case 3:
			return pivotPID.reachedTarget() || getUpperLimit();
		case 2:
			return pivotPID.reachedTarget() || getLowerLimit() || getUpperLimit();
		case 0:
			return true;
		}
		return false;
	}
	
	
	public boolean getBallSensor() {
		return !ballSensor.get();
	}
	
	public int getPosition() {
		int pos = 0;
		if(getUpperLimit() && !getLowerLimit()) {
			pos = 1;
		} else if(!getUpperLimit() && !getLowerLimit()) {
			pos = 2;
		} else if(lowerLimit.get() && !getUpperLimit()) {
			pos = 3;
		} else {
			System.out.println("Multiple limit switches activated on intake.");
		}
		return pos;
	}
	
	@Override
	public void displayData() {
		display("Encoder", pivotEncoder.getDistance());
		display("BallSensor", getBallSensor());
		display("UpperLimit", getUpperLimit());
		display("LowerLimit", getLowerLimit());
	}
}
