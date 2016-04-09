package org.usfirst.frc199.Robot2016.commands;

import org.usfirst.frc199.Robot2016.RobotMap;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Runs the LED strip
 */
public class RunLEDs extends Command {

//	private final AnalogInput led1 = RobotMap.led1;
//	private final AnalogInput led2 = RobotMap.led2;
//	private final AnalogInput led3 = RobotMap.led3;
	
    public RunLEDs() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	SmartDashboard.putNumber("LED1", 0);
    	SmartDashboard.putNumber("LED2", 0);
    	SmartDashboard.putNumber("LED3", 0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//    	led1.setAverageBits((int)SmartDashboard.getNumber("LED1", 0));
//    	led2.setAverageBits((int)SmartDashboard.getNumber("LED2", 0));
//    	led3.setAverageBits((int)SmartDashboard.getNumber("LED3", 0));
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
