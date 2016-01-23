// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc199.Robot2016.subsystems;

import org.usfirst.frc199.Robot2016.Robot;
import org.usfirst.frc199.Robot2016.RobotMap;
import org.usfirst.frc199.Robot2016.commands.TeleopDriveMode;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class Drivetrain extends Subsystem {
	
	public static final int AngleOfRangeFinders = 10;// in degrees

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final SpeedController leftMotor = RobotMap.drivetrainLeftMotor;
    private final SpeedController rightMotor = RobotMap.drivetrainRightMotor;
    private final RobotDrive robotDrive = RobotMap.drivetrainRobotDrive;
    private final AnalogGyro gyro = RobotMap.drivetrainGyro;
    private final Encoder leftEncoder = RobotMap.drivetrainLeftEncoder;
    private final Encoder rightEncoder = RobotMap.drivetrainRightEncoder;
    private final Ultrasonic leftUltrasonic = RobotMap.drivetrainLeftUltrasonic;
    private final Ultrasonic rightUltrasonic = RobotMap.drivetrainRightUltrasonic;
    private final Compressor compressor = RobotMap.drivetrainCompressor;
    private final DoubleSolenoid shifter = RobotMap.drivetrainShifter;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS


    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new TeleopDriveMode());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
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
			double x = Robot.oi.getRightJoystick().getX();
			double y = Robot.oi.getLeftJoystick().getY();
			robotDrive.arcadeDrive(y, x);
		} else {
			double left = Robot.oi.getLeftJoystick().getY();
			double right = Robot.oi.getRightJoystick().getY();
			robotDrive.tankDrive(left, right);
		}
    }
    
    public double getRangeFinderInchesLeft(){
    	return leftUltrasonic.getRangeInches();
    }
    
    public double getRangeFinderInchesRight(){
    	return rightUltrasonic.getRangeInches();
    }
    
    public double getRangefinderDistance(){
    	return (getRangeFinderInchesLeft() + getRangeFinderInchesRight())/2;
    }
    
    //Requires PID
    public void updateRangefinder() {
//		distancePID.update(getRangefinderDistance());
//		arcadeInput(-distancePID.getOutput(),0);
	} 
    
    public void setRangefinderTarget(double target) { 
//		distancePID.setTarget(target);
	}
    
}