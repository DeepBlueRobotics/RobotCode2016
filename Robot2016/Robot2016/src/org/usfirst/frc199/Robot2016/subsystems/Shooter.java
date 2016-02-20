package org.usfirst.frc199.Robot2016.subsystems;

import org.usfirst.frc199.Robot2016.DashboardSubsystem;
import org.usfirst.frc199.Robot2016.RobotMap;
import org.usfirst.frc199.Robot2016.motioncontrol.PID;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Launches boulders into the high goal.
 */
public class Shooter extends Subsystem implements DashboardSubsystem {

    private final SpeedController flywheelMotor = RobotMap.shooterFlywheelMotor;
    private final Encoder flywheelEncoder = RobotMap.shooterFlywheelEncoder;
    private final Servo cameraAxisServo = RobotMap.cameraAxisServo;
    private PID shooterPID = new PID("ShooterSpeed");

    public void initDefaultCommand() {

    }
    
    /**
     * Sets shooter speed manually
     * @param speed - the desired motor output
     */
    public void runShooter(double speed) {
    	flywheelMotor.set(speed);
    }
    
    /**
     * Determines current speed of the shooter wheel
     * @return The wheel speed in inches per second
     */
    private double currentSpeed() {
    	return flywheelEncoder.getRate();
    }
    
    /**
     * Sets flywheel motor speed using PID
     * @param speed - target speed in inches per second
     */
    public void setTargetSpeed(double speed) {
    	flywheelEncoder.reset();
    	shooterPID.update(currentSpeed());
    	shooterPID.setTarget(speed);
    }
    
    /**
     * Adjusts the aim of the angle of the servo controlling the camera.
     * @param theta - angle in degrees
     */
    public void setCameraAngle(double theta) {
    	cameraAxisServo.setAngle(theta);
    }
    
    /**
     * Updates the shooter PID
     * @return the speed output
     */
    public double updateSpeed() {
    	shooterPID.update(currentSpeed());
    	return shooterPID.getOutput();
    }
    
    @Override
    public void displayData() {
    	display("Encoder", flywheelEncoder.getDistance());
    	display("Speed", currentSpeed());
    	display("CameraTiltServo", cameraAxisServo.get());
    }
}