package org.usfirst.frc199.Robot2016.commands;

import org.usfirst.frc199.Robot2016.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Causes the PID code for a specific subsystem to attempt to reach a value
 * specified on the SmartDashboard.
 */
public class TestPID extends Command {

	private final System system;
	private double target = 0;

	// The various PID loops of the robot
	public enum System {
		DRIVEDISTANCE, DRIVEANGLE, SHOOTER;
	}

	/**
	 * @param system - The PID system to be tested
	 */
	public TestPID(System system) {
		this.system = system;
		if (system == System.SHOOTER) {
			requires(Robot.shooter);
		} else {
			requires(Robot.drivetrain);
		}
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		if (system == System.DRIVEDISTANCE) {
			target = SmartDashboard.getNumber("PID/DriveDistance TestTarget");
			Robot.drivetrain.setAutoTarget(target, 0);
		} else if (system == System.DRIVEANGLE) {
			target = SmartDashboard.getNumber("PID/DriveAngle TestTarget");
			Robot.drivetrain.setAutoTarget(0, target);
		} else if (system == System.SHOOTER) {
			target = SmartDashboard.getNumber("PID/Shooter TestTarget");
			Robot.shooter.setTargetSpeed(target);
		}
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (system == System.SHOOTER) {
			Robot.shooter.updateSpeed();
		} else {
			Robot.drivetrain.updateAuto();
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (system == System.SHOOTER) {
			return false;
		} else {
			return Robot.drivetrain.autoReachedTarget();
		}
	}

	// Called once after isFinished returns true
	protected void end() {
		if (system == System.SHOOTER) {
			Robot.shooter.runShooter(0.0);
		} else {
			Robot.drivetrain.tankInput(0, 0);
		}
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
