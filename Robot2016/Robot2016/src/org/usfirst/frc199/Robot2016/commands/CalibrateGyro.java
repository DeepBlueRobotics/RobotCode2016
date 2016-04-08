package org.usfirst.frc199.Robot2016.commands;

import org.usfirst.frc199.Robot2016.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Counteracts gyro drift.
 */
public class CalibrateGyro extends Command {

    public CalibrateGyro() {
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drivetrain.startGyroCalibration();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timeSinceInitialized()>0.5;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.finishGyroCalibration();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
