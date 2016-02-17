package org.usfirst.frc199.Robot2016.subsystems;

import org.usfirst.frc199.Robot2016.DashboardSubsystem;
import org.usfirst.frc199.Robot2016.Robot;
import org.usfirst.frc199.Robot2016.RobotMap;
import org.usfirst.frc199.Robot2016.commands.TeleopDriveMode;
import org.usfirst.frc199.Robot2016.motioncontrol.PID;
import org.usfirst.frc199.Robot2016.motioncontrol.Trajectory;

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
	private PID angularvelocityPID = new PID("DriveAngularVelocity");

    public void initDefaultCommand() {
        setDefaultCommand(new TeleopDriveMode());
    }
    
    public void arcadeInput(double speed, double turn) {
    	robotDrive.arcadeDrive(speed, turn);
    }
    
    public void tankInput(double left, double right) {
    	robotDrive.tankDrive(left, right);
    }
    
    public void drive() {
    	boolean arcadedrive = true;
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
    
    public double getRangefinderDistance(){
    	return ultrasonic.getAverageVoltage();		//put in formula for conversion
    }
    
    //Requires PID
    public void updateRangefinder() {
		distancePID.update(getRangefinderDistance());
		anglePID.update(gyro.getAngle());
		arcadeInput(distancePID.getOutput(),0);
	}
    
    public void setAutoAlignAngleTarget(){
    	anglePID.setTarget(Robot.vision.degreeToTarget());
    }
    
    public void updateAutoAlignPID(double angle){
    	anglePID.update(angle);
    	arcadeInput(0, anglePID.getOutput());
    }
    

	/**
	 * Sets the target for the auto PID
	 * 
	 * @param targetDistance - Forward/back
	 * @param targetAngle
	 *            - Clockwise/counterclockwise
	 */
	public void setAutoTarget(double targetDistance, double targetAngle) {
		distancePID.update((leftEncoder.getDistance() + rightEncoder.getDistance()) / 2);
		anglePID.update(gyro.getAngle());
		distancePID.setRelativeLocation(0);
		anglePID.setRelativeLocation(0);
		distancePID.setTarget(targetDistance);
		anglePID.setTarget(targetAngle);
	}

	/**
	 * Updates the drive PID
	 */
	public void updateAuto() {
		distancePID.update((leftEncoder.getDistance() + rightEncoder.getDistance()) / 2);
		anglePID.update(gyro.getAngle());
	}
	
	public void updateAuto(double newDistance, double newAngle) {
		distancePID.update(newDistance);
		anglePID.update(newAngle);
		arcadeInput(distancePID.getOutput(), anglePID.getOutput());
	}

	/**
	 * Determines if robot has reached the target position
	 * 
	 * @return Whether both PID loops have reached their target
	 */
	public boolean autoReachedTarget() {
		return distancePID.reachedTarget() && anglePID.reachedTarget();
	}
	public void setAngleTarget(double targetAngle) {
		anglePID.update(gyro.getAngle());
		anglePID.setRelativeLocation(0);
		anglePID.setTarget(targetAngle);
	}

	/**
	 * Updates only the angle PID
	 * 
	 * @return The angle PID output
	 */
	public double updateAngle() {
		anglePID.update(gyro.getAngle());
		return anglePID.getOutput();
	}

	/**
	 * Determines if the angle PID has reached the target
	 * 
	 * @return Whether angle target has been reached
	 */
	public boolean reachedAngle() {
		return anglePID.reachedTarget();
	}

	/**
	 * Sets the distance PID target
	 * 
	 * @param targetDistance - The target distance in inches
	 */
	public void setDistanceTarget(double targetDistance) {
		distancePID.update((leftEncoder.getDistance() + rightEncoder.getDistance()) / 2);
		distancePID.setRelativeLocation(0);
		distancePID.setTarget(targetDistance);
	}

	/**
	 * Updates only the distance PID
	 * 
	 * @return The distance PID output
	 */
	public double updateDistance() {
		distancePID.update((leftEncoder.getDistance() + rightEncoder
				.getDistance()) / 2);
		return distancePID.getOutput();
	}

	/**
	 * Determines if the distance PID has reached the target
	 * 
	 * @return Whether distance target has been reached
	 */
	public boolean reachedDistance() {
		return distancePID.reachedTarget();
	}

	public void startTrajectory(Trajectory t) {
		
	}
	
	public void followTrajectory(Trajectory t) {
		
	}

	public boolean completedTrajectory(Trajectory t) {
		return false;
	}
}
