package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc199.Robot2016.DashboardSubsystem;
import org.usfirst.frc199.Robot2016.Robot;

/**
 * Updates SmartDashboard values from each subsystem.
 */
public class UpdateDashboard extends Command {
	
    public UpdateDashboard() {

    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	for(DashboardSubsystem s: Robot.subsystems) {
    		if(!s.getKey("").substring(0, 4).equals("PID/")) {
    			s.display("~TYPE~", "SubSystem");
    		}
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	for(DashboardSubsystem s: Robot.subsystems) {
    		s.displayData();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
