package org.usfirst.frc199.Robot2016.subsystems;

import org.usfirst.frc199.Robot2016.DashboardSubsystem;
import org.usfirst.frc199.Robot2016.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Lifts the Robot at the end of the match.
 */
public class Climber extends Subsystem implements DashboardSubsystem {
	
	private final SpeedController extendMotor = RobotMap.climberExtendMotor;
    private final SpeedController winchMotor = RobotMap.climberWinchMotor;

    public void initDefaultCommand() {
    	
    }

	public double getEncoder() {
		return 0;
	}
	
	public void setWinchMotor(double speed) {
		winchMotor.set(speed);
	}
	public void setExtendMotor(double speed) {
		extendMotor.set(speed);
	}
}