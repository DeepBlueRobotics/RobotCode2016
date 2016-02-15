package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2016.Robot;
import org.usfirst.frc199.Robot2016.RobotMap;

/**
 * Extends the climber using a piston.
 */
public class ExtendClimber extends Command {
	double speed;
	double finalValue;
	public ExtendClimber(double speed, double finalValue) {
        requires(Robot.climber);
        this.speed = speed;
        this.finalValue = finalValue;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.climber.setExtendMotor(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {	
    	if (speed > 0 && Robot.climber.getEncoder() >= finalValue) {
    		return true;
    	} else if (speed < 0 && Robot.climber.getEncoder() <= finalValue) {
    		return true;
    	} else {
    		return false;
    	}
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
