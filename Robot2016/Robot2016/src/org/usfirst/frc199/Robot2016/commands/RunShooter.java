package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2016.Robot;

/**
 * Runs the shooter fly wheel at the speed for a high goal.
 */
public class RunShooter extends Command {
	
	private final double speed;
	private final int setting;
	
    public RunShooter() {
    	this(0, 1.0);
    }
    
    /**
     * Runs the shooter at a given speed
     * @param speed - The speed to run the shooter
     */
    public RunShooter(double speed) {
    	requires(Robot.shooter);
    	this.speed = speed;
    	this.setting = -1;
    }
    
    /**
     * Runs the shooter at a specific speed from preferences
     * @param setting - the shooter speed setting to use
     * @param backup - speed to use if preference not found
     */
    public RunShooter(int setting, double backup) {
    	requires(Robot.shooter);
    	this.setting = setting;
    	this.speed = backup;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if (setting<0) {
    		Robot.shooter.setTargetSpeed(speed);
    	} else if(setting==0) {
    		Robot.shooter.setTargetSpeed(Robot.getPref("ShooterSpeed", speed));
    	} else {
    		Robot.shooter.setTargetSpeed(Robot.getPref("ShooterSpeed"+setting, speed));
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
