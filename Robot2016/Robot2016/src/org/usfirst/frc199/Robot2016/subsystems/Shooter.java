package org.usfirst.frc199.Robot2016.subsystems;

import org.usfirst.frc199.Robot2016.DashboardSubsystem;
import org.usfirst.frc199.Robot2016.PID;
import org.usfirst.frc199.Robot2016.Robot;
import org.usfirst.frc199.Robot2016.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Launches boulders into the high goal.
 */
public class Shooter extends Subsystem implements DashboardSubsystem {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS
	

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final SpeedController flywheelMotor = RobotMap.shooterFlywheelMotor;
    private final Encoder flywheelEncoder = RobotMap.shooterFlywheelEncoder;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private PID shooterPID = new PID("ShooterSpeed");

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
    
    public void runShooter(double speed) {
    	flywheelMotor.set(speed);
    }
    
    //put in expression for conversion from actual rate to physical speed
    private double currentSpeed() {
    	return flywheelEncoder.getRate();
    }
    
    /**
     * Sets flywheel motor speed
     * 
     * @param target speed in inches per second
     */
    public void setTargetSpeed(double speed) {
    	flywheelEncoder.reset();
    	shooterPID.update(currentSpeed());
    	shooterPID.setTarget(speed);
    }
    
    /**
     * Updates the shooter PID
     * @return the speed output
     */
    public double updateSpeed() {
    	shooterPID.update(currentSpeed());
    	return shooterPID.getOutput();
    }
    
}