package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.command.Command;

import java.util.ArrayList;

import org.usfirst.frc199.Robot2016.Robot;
import org.usfirst.frc199.Robot2016.subsystems.DashboardSubsystem;

/**
 * Updates SmartDashboard values from each subsystem.
 */
public class UpdateDashboard extends Command {

	ArrayList <DashboardSubsystem> subsystems = new ArrayList<>();
	
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
 
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
    public UpdateDashboard() {

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	subsystems.add(Robot.climber);
    	subsystems.add(Robot.shooter);
    	subsystems.add(Robot.intake);
    	subsystems.add(Robot.drivetrain);
    	for(DashboardSubsystem s: subsystems) {
    		s.display(key, value);
    		Elevator/~TYPE~", "SubSystem");
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	for(DashboardSubsystem s: subsystems) {
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
