package org.usfirst.frc199.Robot2016;

import org.usfirst.frc199.Robot2016.commands.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    
    public JoystickButton intakeButton2;
    public JoystickButton alignAngleButton;
    public Joystick leftJoystick;
    public JoystickButton shootLowButton2;
    public JoystickButton gradualDriveButton;
    public JoystickButton alignDistanceButton;
    public Joystick rightJoystick;
    public JoystickButton runShooterButton;
    public JoystickButton feedShooterButton;
    public JoystickButton extendClimberButton;
    public JoystickButton retractClimberButton;
    public JoystickButton manualIntakeButton;
    public JoystickButton manualShooterButton;
    public JoystickButton intakeUpButton;
    public JoystickButton intakeDownButton;
    public JoystickButton shootLowButton;
    public JoystickButton intakeButton;
    public JoystickButton reverseCameraButton;
    public JoystickButton forwardCameraButton;
    public Joystick manipulator;
    public InternalButton upButton;
    public InternalButton downButton;
    public InternalButton leftButton;
    public InternalButton rightButton;
    
    public OI() {

        leftJoystick = new Joystick(0);
        intakeButton2 = new JoystickButton(leftJoystick, 1);
        intakeButton2.whileHeld(new IntakeBoulder());
        alignAngleButton = new JoystickButton(leftJoystick, 2);
        alignAngleButton.whileHeld(new AutoAlignAngle());
        forwardCameraButton = new JoystickButton(leftJoystick, 3);
        forwardCameraButton.whenPressed(new ForwardCamera());
        
        rightJoystick = new Joystick(1);
        gradualDriveButton = new JoystickButton(rightJoystick, 4);
        gradualDriveButton.whileHeld(new GradualDrive());
        alignDistanceButton = new JoystickButton(rightJoystick, 2);
        alignDistanceButton.whileHeld(new AutoAlignDistance());
        reverseCameraButton = new JoystickButton(rightJoystick, 3);
        reverseCameraButton.whenPressed(new ReverseCamera());
        shootLowButton2 = new JoystickButton(rightJoystick, 1);
        shootLowButton2.whileHeld(new ShootLow());
        
        manipulator = new Joystick(2);
        retractClimberButton = new JoystickButton(manipulator, 1);
        retractClimberButton.whileHeld(new Winch());
        intakeDownButton = new JoystickButton(manipulator, 2);
        intakeDownButton.whileHeld(new LowerIntake());
        extendClimberButton = new JoystickButton(manipulator, 3);
        extendClimberButton.whenPressed(new ExtendClimber());
        intakeUpButton = new JoystickButton(manipulator, 4);
        intakeUpButton.whileHeld(new RaiseIntake());
        intakeButton = new JoystickButton(manipulator, 5);
        intakeButton.whileHeld(new IntakeBoulder());
        feedShooterButton = new JoystickButton(manipulator, 6);
        feedShooterButton.whileHeld(new FeedShooter());
        shootLowButton = new JoystickButton(manipulator, 7);
        shootLowButton.whileHeld(new ShootLow());
        runShooterButton = new JoystickButton(manipulator, 8);
        runShooterButton.whileHeld(new RunShooter());
        manualIntakeButton = new JoystickButton(manipulator, 9);
        manualIntakeButton.toggleWhenPressed(new IntakeManualControl());
        manualShooterButton = new JoystickButton(manipulator, 10);
        manualShooterButton.toggleWhenPressed(new ShooterManualControl());
        
        upButton = new InternalButton();
        rightButton = new InternalButton();
        downButton = new InternalButton();
        leftButton = new InternalButton();
        upButton.whileHeld(new RunShooter(1));
        rightButton.whileHeld(new RunShooter(2));
        downButton.whileHeld(new RunShooter(3));
        leftButton.whileHeld(new RunShooter(4));
        
        // Test PID Commands
        SmartDashboard.putData("PID/DriveDistance/TestDriveDistancePID", new TestPID(TestPID.System.DRIVEDISTANCE));
		SmartDashboard.putData("PID/DriveAngle/TestDriveAnglePID", new TestPID(TestPID.System.DRIVEANGLE));
		SmartDashboard.putData("PID/DriveVelocity/TestDriveVelocityPID", new TestPID(TestPID.System.DRIVEVELOCITY));
		SmartDashboard.putData("PID/DriveAngularVelocity/TestDriveAngularVelocityPID", new TestPID(TestPID.System.DRIVEANGULARVELOCITY));
		SmartDashboard.putData("PID/Shooter/TestShooterPID", new TestPID(TestPID.System.SHOOTER));
		SmartDashboard.putData("PID/Intake/TestIntakePID", new TestPID(TestPID.System.INTAKE));
		SmartDashboard.putData("MotionProfile/TestMotionProfiling", new FollowTrajectory());
		SmartDashboard.putData("PID/DriveAngle/AutoTurn", new AutoTurn());
		// Gyro calibration button
		SmartDashboard.putData("CalibrateGyro", new CalibrateGyro());
    }
    
    private int getDPad(Joystick stick) {
        if(stick.getPOV() < 0) {
            return -1;
        } else {
            return stick.getPOV() / 45;
        }
    }
    
    public void updateDPad() {
        int value = getDPad(manipulator);
        upButton.setPressed(value == 0);
        rightButton.setPressed(value == 2);
        downButton.setPressed(value == 4);
        leftButton.setPressed(value == 6);
    }
    
    public Joystick getLeftJoystick() {
        return leftJoystick;
    }

    public Joystick getRightJoystick() {
        return rightJoystick;
    }

    public Joystick getManipulator() {
        return manipulator;
    }

    /**
     * Scales joystick input to increase control at smaller values
     * @param input - raw joystick input
     * @return exponentiated joystick input
     */
    public double exponentiate(double input) {
    	return Math.signum(input)*Math.pow(Math.abs(input), Robot.getPref("Exponentiation", 1.0));
    }
}
