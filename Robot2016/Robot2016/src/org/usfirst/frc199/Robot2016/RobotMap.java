package org.usfirst.frc199.Robot2016;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	public static SpeedController drivetrainLeftMotor;
	public static SpeedController drivetrainRightMotor;
	public static RobotDrive drivetrainRobotDrive;
	public static AnalogGyro drivetrainGyro;
	public static Encoder drivetrainLeftEncoder;
	public static Encoder drivetrainRightEncoder;
	public static AnalogInput drivetrainUltrasonic;
	public static SpeedController shooterFlywheelMotor;
	public static Encoder shooterFlywheelEncoder;
	public static SpeedController intakeRollerMotor;
	public static SpeedController intakePivotMotor;
	public static DigitalInput intakeUpperLimit;
	public static DigitalInput intakeLowerLimit;
	public static Encoder intakePivotEncoder;
	public static DigitalInput intakeBallSensor;
//	public static SpeedController climberExtendMotor;
	public static SpeedController climberWinchMotor;
	public static DigitalInput climberExtensionLimit;
	public static Talon cameraAxisServo;
	public static PWM led1, led2;
	public static PWM led3;
	
	public static void init() {
		drivetrainLeftMotor = new Talon(0);
		LiveWindow.addActuator("Drivetrain", "LeftMotor", (Talon) drivetrainLeftMotor);

		drivetrainRightMotor = new Talon(1);
		LiveWindow.addActuator("Drivetrain", "RightMotor", (Talon) drivetrainRightMotor);

		drivetrainRobotDrive = new RobotDrive(drivetrainLeftMotor, drivetrainRightMotor);

		drivetrainRobotDrive.setSafetyEnabled(true);
		drivetrainRobotDrive.setExpiration(0.1);
		drivetrainRobotDrive.setSensitivity(0.5);
		drivetrainRobotDrive.setMaxOutput(1.0);

		drivetrainGyro = new AnalogGyro(0);
		LiveWindow.addSensor("Drivetrain", "Gyro", drivetrainGyro);
		drivetrainGyro.setSensitivity(0.007);
		drivetrainLeftEncoder = new Encoder(0, 1, false, EncodingType.k4X);
		LiveWindow.addSensor("Drivetrain", "LeftEncoder", drivetrainLeftEncoder);
		drivetrainLeftEncoder.setDistancePerPulse(Robot.getPref("DrivetrainLeftEncoderRatio", .074));
		drivetrainLeftEncoder.setPIDSourceType(PIDSourceType.kRate);
		drivetrainRightEncoder = new Encoder(2, 3, false, EncodingType.k4X);
		LiveWindow.addSensor("Drivetrain", "RightEncoder", drivetrainRightEncoder);
		drivetrainRightEncoder.setDistancePerPulse(Robot.getPref("DrivetrainRightEncoderRatio", .074));
		drivetrainRightEncoder.setPIDSourceType(PIDSourceType.kRate);
		drivetrainUltrasonic = new AnalogInput(1);
		LiveWindow.addSensor("Drivetrain", "Ultrasonic", drivetrainUltrasonic);

		shooterFlywheelMotor = new Talon(4);
		LiveWindow.addActuator("Shooter", "FlywheelMotor", (Talon) shooterFlywheelMotor);

		shooterFlywheelEncoder = new Encoder(4, 5, false, EncodingType.k4X);
		LiveWindow.addSensor("Shooter", "FlywheelEncoder", shooterFlywheelEncoder);
		shooterFlywheelEncoder.setDistancePerPulse(Robot.getPref("ShooterEncoderRatio", .001068));
		shooterFlywheelEncoder.setPIDSourceType(PIDSourceType.kRate);
		intakeRollerMotor = new Talon(3);
		LiveWindow.addActuator("Intake", "RollerMotor", (Talon) intakeRollerMotor);

		intakePivotMotor = new Talon(2);
		LiveWindow.addActuator("Intake", "PivotMotor", (Talon) intakePivotMotor);

		intakeUpperLimit = new DigitalInput(8);
		LiveWindow.addSensor("Intake", "UpperLimit", intakeUpperLimit);

		intakeLowerLimit = new DigitalInput(9);
		LiveWindow.addSensor("Intake", "LowerLimit", intakeLowerLimit);

		intakePivotEncoder = new Encoder(6, 7, false, EncodingType.k4X);
		LiveWindow.addSensor("Intake", "PivotEncoder", intakePivotEncoder);
		intakePivotEncoder.setDistancePerPulse(Robot.getPref("IntakeEncoderRatio", 1));

		intakeBallSensor = new DigitalInput(10);
		LiveWindow.addSensor("Intake", "BallSensor", intakeBallSensor);
//
//		climberExtendMotor = new Talon(7);
//		LiveWindow.addActuator("Climber", "ExtendMotor", (Talon) climberExtendMotor);

		climberWinchMotor = new Talon(5);
		LiveWindow.addActuator("Climber", "WinchMotor", (Talon) climberWinchMotor);

		climberExtensionLimit = new DigitalInput(11);
		LiveWindow.addSensor("Climber", "ExtensionLimit", climberExtensionLimit);
		
		cameraAxisServo = new Talon(9);
		LiveWindow.addActuator("Shooter", "CameraAxisServo", (Talon)cameraAxisServo);
		
		led1 = new PWM(6);
		led2 = new PWM(7);
		led3 = new PWM(8);
	}
}
