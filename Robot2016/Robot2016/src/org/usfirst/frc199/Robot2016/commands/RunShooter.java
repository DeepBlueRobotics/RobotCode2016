package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2016.Robot;

/**
 * Runs the shooter fly wheel at the speed for a high goal.
 */
public class RunShooter extends Command {
	private double speed;
	private boolean flag;
    public RunShooter() {
    	requires(Robot.shooter);
    	flag = false;
    	this.speed = 0;
    }
    public RunShooter(double speed1) {
    	requires(Robot.shooter);
    	flag = true;
    	this.speed = speed1;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	// Target speed to be set in inches per second
    	Robot.shooter.setTargetSpeed(Robot.getPref("ShooterSpeed", 1.0));
    	if (flag) {
    		Robot.shooter.setTargetSpeed(this.speed);
    	} else {
    		Robot.shooter.setTargetSpeed(Preferences.getInstance().getDouble("ShooterSpeed", 1.0));
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.shooter.runShooter(Robot.shooter.updateSpeed());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shooter.runShooter(0);
    
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
