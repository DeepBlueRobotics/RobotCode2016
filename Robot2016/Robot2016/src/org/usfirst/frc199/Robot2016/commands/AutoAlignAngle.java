package org.usfirst.frc199.Robot2016.commands;

import org.usfirst.frc199.Robot2016.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Turns to face the high goal.
 */
public class AutoAlignAngle extends Command {

	double targetAngle;

	public AutoAlignAngle() {
		requires(Robot.drivetrain);
	}

	public AutoAlignAngle(int position) {
		requires(Robot.drivetrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
    	Robot.drivetrain.setAutoAlignAngleTarget();
    }

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.drivetrain.updateAutoAlignPID(Robot.vision.degreeToTarget());
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.vision.isRobotDirectlyFacingTarget();
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drivetrain.arcadeInput(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
