package org.usfirst.frc199.Robot2016.subsystems;

import org.usfirst.frc199.Robot2016.DashboardSubsystem;
import org.usfirst.frc199.Robot2016.Robot;
import org.usfirst.frc199.Robot2016.RobotMap;
import org.usfirst.frc199.Robot2016.motioncontrol.PID;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Launches boulders into the high goal.
 */
public class Shooter extends Subsystem implements DashboardSubsystem {

    private final SpeedController flywheelMotor = RobotMap.shooterFlywheelMotor;
    private final Encoder flywheelEncoder = RobotMap.shooterFlywheelEncoder;
//    private final Servo cameraAxisServo = RobotMap.cameraAxisServo;
    private PID shooterPID = new PID("Shooter");
    private Timer timer = new Timer();
    private double target = 0;

    public void initDefaultCommand() {

    }
    
    /**
     * Sets shooter speed manually
     * @param speed - the desired motor output
     */
    public void runShooter(double speed) {
    	double cap = Robot.getPref("ShooterCap", .85);
    	if(speed>cap) speed = cap;
    	if(speed<-cap) speed = -cap;
    	flywheelMotor.set(-speed);
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
    	shooterPID.setTarget(0);
    	shooterPID.update(currentSpeed());
    	target = speed;
    	timer.reset();
    	timer.start();
    }
    
    /**
     * Adjusts the aim of the angle of the servo controlling the camera.
     * @param theta - angle in degrees
     */
    public void setCameraAngle(double theta) {
//    	cameraAxisServo.setAngle(theta);
    }
    
    /**
     * Updates the shooter PID
     * @return the speed output
     */
    public double updateSpeed() {
    	double t = timer.get();
    	double spinupTime = Robot.getPref("ShooterSpinupTime", 0);
    	double setpoint = target;
    	if(t<spinupTime) {
    		setpoint*=t/spinupTime;
    	}
    	shooterPID.setTarget(setpoint);
    	shooterPID.update(currentSpeed());
    	return shooterPID.getOutput()+setpoint/Robot.getPref("ShooterMaxV", 75);
    }
    
    @Override
    public void displayData() {
    	display("Encoder", flywheelEncoder.getDistance());
    	display("Speed", currentSpeed());
//    	display("CameraTiltServo", cameraAxisServo.get());
    	
    	// Regular SmartDashboard value for graphing
    	SmartDashboard.putNumber("ShooterSpeed", currentSpeed());
    	SmartDashboard.putNumber("ShooterCurrent", new PowerDistributionPanel().getCurrent(4));
    	
    	// Indicator that shooter has reached max speed
    	SmartDashboard.putBoolean("ReadyToShoot", currentSpeed()>=Math.min(target, Robot.getPref("ShooterMaxV", 75)));
    }
}