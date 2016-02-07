package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2016.Robot;

/**
 * Pulls in the climber using a winch.
 */
public class RetractClimber extends Command {

    public RetractClimber() {
    	requires(Robot.climber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.climber.setMotor(1.0); 
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
	    /* No encoder right now, so act as if there was one. 
	      Also 8 is a random value, change the value after testing.
	     */
    	return Robot.climber.getEncoder() > 8;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.climber.setMotor(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
