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
	private int intakeInitialPosition;

	// The various PID loops of the robot
	public enum System {
		DRIVEDISTANCE, DRIVEANGLE, SHOOTER, INTAKE, DRIVEVELOCITY, DRIVEANGULARVELOCITY;
	}

	/**
	 * @param system - The PID system to be tested
	 */
	public TestPID(System system) {
		this.system = system;
		switch(system) {
			case SHOOTER: requires(Robot.shooter); break;
			case INTAKE: requires(Robot.intake); break;
			default: requires(Robot.drivetrain);
		}
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		switch(system) {
			case DRIVEDISTANCE:
				target = SmartDashboard.getNumber("PID/DriveDistance/TestTarget");
				Robot.drivetrain.setAutoTarget(target, 0);
				break;
			case DRIVEANGLE:
				target = SmartDashboard.getNumber("PID/DriveAngle/TestTarget");
				Robot.drivetrain.setAngleTarget(target);
				break;
			case SHOOTER:
				target = SmartDashboard.getNumber("PID/Shooter/TestTarget");
				Robot.shooter.setTargetSpeed(target);
				break;
			case INTAKE:
				target = SmartDashboard.getNumber("PID/Intake/TestTarget");
				intakeInitialPosition = Robot.intake.getPosition();
				Robot.intake.setTargetAngle(intakeInitialPosition, (int)target);
				break;
			case DRIVEVELOCITY:
				target = SmartDashboard.getNumber("PID/DriveVelocity/TestTarget");
				Robot.drivetrain.setVelocityTarget(target, 0);
				break;
			case DRIVEANGULARVELOCITY:
				target = SmartDashboard.getNumber("PID/DriveAngularVelocity/TestTarget");
				Robot.drivetrain.setVelocityTarget(0, target);
				break;
		}
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		switch(system) {
			case SHOOTER: Robot.shooter.runShooter(Robot.shooter.updateSpeed()); break;
			case INTAKE: Robot.intake.updateAngle(); break;
			case DRIVEDISTANCE: Robot.drivetrain.updateAuto(); break;
			case DRIVEANGLE: Robot.drivetrain.updateAngle(); break;
			case DRIVEVELOCITY: Robot.drivetrain.updateVelocity(); break;
			case DRIVEANGULARVELOCITY: Robot.drivetrain.updateVelocity(); break;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		switch(system) {
			case SHOOTER: return false;
			case INTAKE: return Robot.intake.shouldStop(intakeInitialPosition);
			case DRIVEDISTANCE: return Robot.drivetrain.autoReachedTarget();
			case DRIVEANGLE: return Robot.drivetrain.angleReachedTarget();
			case DRIVEVELOCITY: return false;
			case DRIVEANGULARVELOCITY: return false;
			default: return false;
		}
	}

	// Called once after isFinished returns true
	protected void end() {
		switch(system) {
			case SHOOTER: Robot.shooter.runShooter(0.0); break;
			case INTAKE: Robot.intake.pivot(0.0); break;
			default: Robot.drivetrain.tankDrive(0, 0);
		}
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
