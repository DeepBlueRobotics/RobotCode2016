package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2016.Robot;

/**
 * Lowers the intake until the limit switch is hit.
 */
public class LowerIntake extends Command {
	private int i;
	
    public LowerIntake() {
        requires(Robot.intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	i = Robot.intake.getPosition();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.intake.pivot(-Preferences.getInstance().getDouble("IntakePivotSpeed", .7));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.intake.shouldStop(i);
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.intake.pivot(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
