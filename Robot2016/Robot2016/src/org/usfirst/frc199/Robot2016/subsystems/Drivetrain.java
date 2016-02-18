package org.usfirst.frc199.Robot2016.subsystems;

import org.usfirst.frc199.Robot2016.DashboardSubsystem;
import org.usfirst.frc199.Robot2016.Robot;
import org.usfirst.frc199.Robot2016.RobotMap;
import org.usfirst.frc199.Robot2016.commands.TeleopDriveMode;
import org.usfirst.frc199.Robot2016.motioncontrol.PID;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Moves the robot around the field.
 */
public class Drivetrain extends Subsystem implements DashboardSubsystem {
	
	public static final int AngleOfRangeFinders = 10; // in degrees

    private final SpeedController leftMotor = RobotMap.drivetrainLeftMotor;
    private final SpeedController rightMotor = RobotMap.drivetrainRightMotor;
    private final RobotDrive robotDrive = RobotMap.drivetrainRobotDrive;
    private final AnalogGyro gyro = RobotMap.drivetrainGyro;
    private final Encoder leftEncoder = RobotMap.drivetrainLeftEncoder;
    private final Encoder rightEncoder = RobotMap.drivetrainRightEncoder;
    private final AnalogInput ultrasonic = RobotMap.drivetrainUltrasonic;
    
    private PID distancePID = new PID("DriveDistance");
	private PID anglePID = new PID("DriveAngle");
	private PID velocityPID = new PID("DriveVelocity");
	private PID angularVelocityPID = new PID("DriveAngularVelocity");

    public void initDefaultCommand() {
        setDefaultCommand(new TeleopDriveMode());
    }
    
    /**
     * Standard arcade drive
     * @param speed - linear velocity output
     * @param turn - angular velocity output
     */
    public void arcadeDrive(double speed, double turn) {
    	robotDrive.arcadeDrive(speed, turn);
    }
    
    /**
     * Standard tank drive
     * @param left - left side of drivetrain output
     * @param right - right side of drivetrain output
     */
    public void tankDrive(double left, double right) {
    	robotDrive.tankDrive(left, right);
    }
    
    /**
     * Runs drive code based on driver inputs
     */
    public void teleopDrive() {
    	// Arcade drive = 0, tank drive = 1
    	boolean arcadedrive = Robot.getPref("DriveMode", 0) == 0;
    	if(arcadedrive){
			double x = -Robot.oi.getRightJoystick().getX();
			double y = -Robot.oi.getLeftJoystick().getY();
			robotDrive.arcadeDrive(y, x);
		} else {
			double left = Robot.oi.getLeftJoystick().getY();
			double right = Robot.oi.getRightJoystick().getY();
			robotDrive.tankDrive(left, right);
		}
    }
    
    /**
     * Gets distance recorded by encoders
     * @return - distance in inches
     */
    public double getEncoderDistance() {
    	return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2;
    }
    
    /**
     * Gets angle recorded by gyro
     * @return - angle clockwise of straight in degrees
     */
    public double getGyroAngle() {
    	return gyro.getAngle();
    }
    
    /**
     * Gets linear velocity recorded by encoders
     * @return - velocity in inches/second
     */
    public double getEncoderRate() {
    	return (leftEncoder.getRate() + rightEncoder.getRate()) / 2;
    }
    
    /**
     * Gets angular velocity recorded by gyro
     * @return - angular velocity in degrees/second
     */
    public double getGyroRate() {
    	return gyro.getRate();
    }
    
    /**
     * Gets distance recorded by rangefinder
     * @return distance in inches
     */
    public double getRangefinderDistance() {
    	return ultrasonic.getAverageVoltage();		//put in formula for conversion
    }

	/**
	 * Sets the target for the auto PID
	 * @param targetDistance - The target distance in inches
	 * @param targetAngle - The target angle in degrees clockwise
	 */
	public void setAutoTarget(double targetDistance, double targetAngle) {
		setDistanceTarget(targetDistance);
		setAngleTarget(targetAngle);
	}

	/**
	 * Sets the distance PID target
	 * @param targetDistance - The target distance in inches
	 */
	public void setDistanceTarget(double targetDistance) {
		distancePID.update(getEncoderDistance());
		distancePID.setRelativeLocation(0);
		distancePID.setTarget(targetDistance);
	}

	/**
	 * Sets the angle PID target
	 * @param targetAngle - The target angle in degrees clockwise
	 */
	public void setAngleTarget(double targetAngle) {
		anglePID.update(getGyroAngle());
		anglePID.setRelativeLocation(0);
		anglePID.setTarget(targetAngle);
	}
    
	/**
	 * Updates the drive PID
	 * @param newDistance - new distance value in inches
	 * @param newAngle - new angle value in degrees clockwise
	 */
	public void updateAuto(double newDistance, double newAngle) {
		distancePID.update(newDistance);
		anglePID.update(newAngle);
		arcadeDrive(distancePID.getOutput(), anglePID.getOutput());
	}

	/**
	 * Updates the drive PID using the encoders and gyro
	 */
	public void updateAuto() {
		updateAuto(getEncoderDistance(), getGyroAngle());
	}

	/**
	 * Updates only the distance PID (may not drive straight)
	 * @return the distance PID output
	 */
	public void updateDistance() {
		distancePID.update(getEncoderDistance());
		arcadeDrive(distancePID.getOutput(), 0);
	}

	/**
	 * Updates only the angle PID
	 * @return the angle PID output
	 */
	public void updateAngle() {
		anglePID.update(getGyroAngle());
		arcadeDrive(0, anglePID.getOutput());
	}
	
	/**
	 * Determines if robot has reached the target position
	 * 
	 * @return Whether both PID loops have reached their target
	 */
	public boolean autoReachedTarget() {
		return distanceReachedTarget() && angleReachedTarget();
	}

	/**
	 * Determines if the distance PID has reached the target
	 * 
	 * @return Whether distance target has been reached
	 */
	public boolean distanceReachedTarget() {
		return distancePID.reachedTarget();
	}
    
	/**
	 * Determines if the angle PID has reached the target
	 * 
	 * @return Whether angle target has been reached
	 */
	public boolean angleReachedTarget() {
		return anglePID.reachedTarget();
	}
	
	/**
	 * Sets the target distance for the rangefinder PID
	 * @param targetDistance - distance from goal in inches
	 */
	public void setRangefinderTarget(double targetDistance) {
		distancePID.update(0);
		distancePID.setRelativeLocation(0);
		distancePID.setTarget(targetDistance);
		setAngleTarget(0);
	}
	
    /**
     * Updates the PID using rangefinder as input
     */
    public void updateRangefinder() {
		updateAuto(getRangefinderDistance(), getGyroAngle());
	}
    
    /**
     * Sets angle target using the camera
     */
    public void setAutoAlignAngleTarget() {
    	setAngleTarget(Robot.vision.degreeToTarget());
    }
    
    /**
     * Uses PID to reach target velocity
     * @param v - linear velocity in inches/second
     * @param w - angular velocity in degrees/second
     */
	public void followTrajectory(double v, double w) {
		velocityPID.setTarget(v);
		angularVelocityPID.setTarget(w);
		velocityPID.update(getEncoderRate());
		angularVelocityPID.update(getGyroRate());
		arcadeDrive(velocityPID.getOutput(), angularVelocityPID.getOutput());
	}
}
