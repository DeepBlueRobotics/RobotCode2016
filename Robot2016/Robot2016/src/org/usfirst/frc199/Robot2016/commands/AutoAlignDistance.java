package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2016.Robot;

/**
 * Drives to the correct distance for shooting.
 */
public class AutoAlignDistance extends Command {

	int distance;
	boolean targetAcquired;
	
	public AutoAlignDistance() {
    	distance = 60;
        requires(Robot.drivetrain);
    }
    
    public AutoAlignDistance(int distance){
    	this.distance = distance;
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	targetAcquired = false; 
    	Robot.drivetrain.setRangefinderTarget(distance);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.drivetrain.updateRangefinder();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Robot.drivetrain.getRangefinderDistance()-distance) < Preferences.getInstance().getDouble("DriveDistanceErrorTolerance", 0);
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
