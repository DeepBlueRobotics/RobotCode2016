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
	
//	private final SpeedController extendMotor = RobotMap.climberExtendMotor;
    private final SpeedController winchMotor = RobotMap.climberWinchMotor;
    private final DigitalInput extensionLimit = RobotMap.climberExtensionLimit;

    /**
     * Initial command - HAS TO BE NONE AS WE DON'T WANT TO INITIALLY CLIMB
     */
    public void initDefaultCommand() {
    }
	
    /**
     * Is the climber extended?
     * 
     * @return If the climber is extended.
     */
    public boolean isExtended() {
    	 return extensionLimit.get();
    }
    
    /**
     * Sets the winch motor speed.
     * 
     * @param speed Speed given. 
     */
	public void setWinchMotor(double speed) {
		winchMotor.set(speed);	
	}
	
	/**
	 * Sets motor to extend the climber at the given speed.
	 * 
	 * @param speed Speed given.
	 */
	public void setExtendMotor(double speed) {
		if(isExtended()) {
//			extendMotor.set(0);
		} else {
//			extendMotor.set(speed);			
		}
	}
	
	@Override
	public void displayData() {
		display("Limit", extensionLimit.get());
	}
}