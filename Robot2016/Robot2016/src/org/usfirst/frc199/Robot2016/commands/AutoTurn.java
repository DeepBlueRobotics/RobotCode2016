package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2016.Robot;

/**
 * Autonomously turns a specified angle using PID.
 */
public class AutoTurn extends Command {

    public final double angle;
    
    public AutoTurn(double angle) {
		this.angle = angle;
        requires(Robot.drivetrain);
	}

    // Called just before this Command runs the first time
	protected void initialize() {
		Robot.drivetrain.setAutoTarget(0, angle);
	}

	protected void execute() {
		Robot.drivetrain.updateAuto();
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.drivetrain.reachedAngle();
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
