package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2016.Robot;

/**
 * Runs the intake in reverse to shoot into the low goal.
 */
public class ShootLow extends Command {

	public ShootLow() {
	}

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.intake.setRoller(-Robot.getPref("IntakeSpeed", 1.0));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.intake.setRoller(0);	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
