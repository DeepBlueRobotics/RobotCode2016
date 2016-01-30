package org.usfirst.frc199.Robot2016.subsystems;

import org.usfirst.frc199.Robot2016.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Brings boulders into the robot.
 */
public class Intake extends Subsystem implements DashboardSubsystem {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	private final SpeedController rollerMotor = RobotMap.intakeRollerMotor;
	private final SpeedController pivotMotor = RobotMap.intakePivotMotor;
	private final DigitalInput upperLimit = RobotMap.intakeUpperLimit;
	private final DigitalInput lowerLimit = RobotMap.intakeLowerLimit;
	private final SpeedController beltMotor = RobotMap.intakeBeltMotor;
	private final DigitalInput ballSensor = RobotMap.intakeBallSensor;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public void initDefaultCommand() {
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	public void setRoller(double speed) {
		rollerMotor.set(speed);
	}

	public void raise(double speed) {
		if (upperLimit.get()) {
			speed = 0;
		}
		pivotMotor.set(speed);
	}

	public void lower(double speed) {
		if (lowerLimit.get()) {
			speed = 0;
		}
		pivotMotor.set(speed);
	}

	public void runBelt(double speed) {
		beltMotor.set(speed);
	}
	
	public boolean getBallSensor() {
		return ballSensor.get();
	}
}
