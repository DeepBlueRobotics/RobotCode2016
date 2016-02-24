package org.usfirst.frc199.Robot2016.commands;

import org.usfirst.frc199.Robot2016.Robot;
import org.usfirst.frc199.Robot2016.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class ForwardCamera extends Command {

	Timer timer = new Timer();
	
    // Called just before this Command runs the first time
    protected void initialize() {
    	RobotMap.cameraAxisServo.set(-1);
    	timer.reset();
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(timer.get() < Robot.getPref("CameraFliptime", 1)){
    		RobotMap.cameraAxisServo.set(-1);
    	}else{
    		RobotMap.cameraAxisServo.set(0);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !(timer.get() < Robot.getPref("CameraFliptime", 1));
    }

    // Called once after isFinished returns true
    protected void end() {
    	RobotMap.cameraAxisServo.set(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
	
}
