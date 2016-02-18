package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2016.Robot;

/**
 * Extends the climber using a piston.
 */
public class ExtendClimber extends Command {

	public ExtendClimber() {
        requires(Robot.climber);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.climber.setExtendMotor(Robot.getPref("ClimberExtendSpeed", 1.0));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {	
    	return Robot.climber.isExtended();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.climber.setExtendMotor(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }

	
}
