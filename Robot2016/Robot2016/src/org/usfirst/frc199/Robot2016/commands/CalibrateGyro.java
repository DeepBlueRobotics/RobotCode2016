package org.usfirst.frc199.Robot2016.commands;

import org.usfirst.frc199.Robot2016.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Cancels out gyro drift.
 */
public class CalibrateGyro extends Command {

	double initialValue = 0;
	
    public CalibrateGyro() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	initialValue = Robot.drivetrain.getGyroAngle();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timeSinceInitialized()>1.0;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.calibrateGyro(initialValue, timeSinceInitialized());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
