package org.usfirst.frc199.Robot2016.subsystems;

import org.usfirst.frc199.Robot2016.DashboardSubsystem;
import org.usfirst.frc199.Robot2016.Robot;
import org.usfirst.frc199.Robot2016.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Lifts the Robot at the end of the match.
 */
public class Climber extends Subsystem implements DashboardSubsystem {
	
	private final SpeedController extendMotor = RobotMap.climberExtendMotor;
    private final SpeedController winchMotor = RobotMap.climberWinchMotor;
    private final DigitalInput extensionLimit = RobotMap.climberExtensionLimit;

    public void initDefaultCommand() {
    }
	
    public boolean isExtended() {
    	 return extensionLimit.get();
    }
    
	public void setWinchMotor(double speed) {
		winchMotor.set(speed);	
	}
	
	public void setExtendMotor(double speed) {
		if(isExtended()) {
			extendMotor.set(0);
		} else {
			extendMotor.set(speed);			
		}
	}
	
	@Override
	public void displayData() {
		display("Limit", extensionLimit.get());
		if (RobotMap.climberExtensionLimit.get() == true) {
			extendMotor.set(0);
		} else {
			extendMotor.set(speed);
		}
	}
}