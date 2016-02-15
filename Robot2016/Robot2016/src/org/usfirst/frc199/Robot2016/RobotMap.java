package org.usfirst.frc199.Robot2016;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;

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
    public static Compressor drivetrainCompressor;
    public static DoubleSolenoid drivetrainShifter;
    public static SpeedController shooterFlywheelMotor;
    public static Encoder shooterFlywheelEncoder;
    public static SpeedController intakeRollerMotor;
    public static SpeedController intakePivotMotor;
    public static DigitalInput intakeUpperLimit;
    public static DigitalInput intakeLowerLimit;
    public static Encoder intakePivotEncoder;
//    public static SpeedController intakeBeltMotor;
    public static DigitalInput intakeBallSensor;
    public static SpeedController climberExtendMotor;
    public static SpeedController climberWinchMotor;
    public static DigitalInput climberReachedRung;

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
        drivetrainLeftEncoder.setDistancePerPulse(1.0);
        drivetrainLeftEncoder.setPIDSourceType(PIDSourceType.kRate);
        drivetrainRightEncoder = new Encoder(2, 3, false, EncodingType.k4X);
        LiveWindow.addSensor("Drivetrain", "RightEncoder", drivetrainRightEncoder);
        drivetrainRightEncoder.setDistancePerPulse(1.0);
        drivetrainRightEncoder.setPIDSourceType(PIDSourceType.kRate);
        drivetrainUltrasonic = new AnalogInput(1);
        LiveWindow.addSensor("Drivetrain", "Ultrasonic", drivetrainUltrasonic);
        //Any extra code needed for the ultrasonic?
        
        drivetrainCompressor = new Compressor(0);
        
        
        drivetrainShifter = new DoubleSolenoid(0, 0, 1);
        LiveWindow.addActuator("Drivetrain", "Shifter", drivetrainShifter);
        
        shooterFlywheelMotor = new Talon(4);
        LiveWindow.addActuator("Shooter", "FlywheelMotor", (Talon) shooterFlywheelMotor);
        
        shooterFlywheelEncoder = new Encoder(4, 5, false, EncodingType.k4X);
        LiveWindow.addSensor("Shooter", "FlywheelEncoder", shooterFlywheelEncoder);
        shooterFlywheelEncoder.setDistancePerPulse(1.0);
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
        intakePivotEncoder.setDistancePerPulse(Preferences.getInstance().getDouble("IntakeEncoderRatio", 1));
        
//        intakeBeltMotor = new Talon(3);
//        LiveWindow.addActuator("Intake", "BeltMotor", (Talon) intakeBeltMotor);
        
        intakeBallSensor = new DigitalInput(10);
        LiveWindow.addSensor("Intake", "BallSensor", intakeBallSensor);
        
        climberExtendMotor = new Talon(7);
        LiveWindow.addActuator("Climber", "ExtendMotor", (Talon) climberExtendMotor);
        
        climberWinchMotor = new Talon(6);
        LiveWindow.addActuator("Climber", "WinchMotor", (Talon) climberWinchMotor);
        
        climberReachedRung = new DigitalInput(11);
        LiveWindow.addSensor("Climber", "Stop", climberReachedRung);
        
    }
}
