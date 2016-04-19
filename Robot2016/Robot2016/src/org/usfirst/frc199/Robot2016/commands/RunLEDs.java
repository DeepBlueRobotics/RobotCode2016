package org.usfirst.frc199.Robot2016.commands;

import org.usfirst.frc199.Robot2016.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Runs the LED strip
 */
public class RunLEDs extends Command {

	private final PWM led1 = RobotMap.led1;
	private final PWM led2 = RobotMap.led2;
	private final PWM led3 = RobotMap.led3;
	
    public RunLEDs() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	SmartDashboard.putNumber("LED1", 255);
    	SmartDashboard.putNumber("LED2", 255);
    	SmartDashboard.putNumber("LED3", 255);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	led1.setRaw((int)SmartDashboard.getNumber("LED1", 255));
    	led2.setRaw((int)SmartDashboard.getNumber("LED2", 255));
    	led3.setRaw((int)SmartDashboard.getNumber("LED3", 255));	
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
