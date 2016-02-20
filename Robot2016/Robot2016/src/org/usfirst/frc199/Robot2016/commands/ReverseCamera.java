package org.usfirst.frc199.Robot2016.commands;

import org.usfirst.frc199.Robot2016.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Allows an operator to toggle whether the camera is aimed forward or backward.
 */
public class ReverseCamera extends Command {
	
	private boolean isForward;
	
    public ReverseCamera() {
    	requires(Robot.shooter);
    	isForward = true;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.shooter.setCameraAngle(isForward ? 0 : 180);
    	isForward = !isForward;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}