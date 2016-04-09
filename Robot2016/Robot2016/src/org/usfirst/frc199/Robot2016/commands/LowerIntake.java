package org.usfirst.frc199.Robot2016.commands;

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
//    	Robot.intake.setTargetAngle(i, -1);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//    	Robot.intake.pivot(Robot.intake.updateAngle());
    	Robot.intake.pivot(-.5);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return this.timeSinceInitialized() > .5 || Robot.intake.getLowerLimit();
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
