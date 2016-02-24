package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2016.Robot;

/**
 * Controls the intake using the left manipulator joystick.
 */
public class IntakeManualControl extends Command {

    public IntakeManualControl() {

        requires(Robot.intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double speed = Robot.oi.manipulator.getY();
    	if(Math.abs(speed)<.2) {
    		Robot.intake.pivot(0);
    	} else {
    		Robot.intake.pivot((Math.signum(speed)*.8+speed*.2)*.6);
    	}
    	Robot.intake.setRoller(Robot.oi.manipulator.getThrottle());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.intake.pivot(0);
    	Robot.intake.setRoller(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
