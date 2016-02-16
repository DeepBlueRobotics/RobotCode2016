package org.usfirst.frc199.Robot2016;

import org.usfirst.frc199.Robot2016.commands.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    
    public JoystickButton intakeButton;
    public JoystickButton alignAngleButton;
    public Joystick leftJoystick;
    public JoystickButton shootLowButton;
    public JoystickButton alignDistanceButton;
    public JoystickButton shiftLowButton;
    public Joystick rightJoystick;
    public JoystickButton runShooterButton;
    public JoystickButton feedShooterButton;
    public JoystickButton extendClimberButton;
    public JoystickButton retractClimberButton;
    public JoystickButton manualIntakeButton;
    public JoystickButton manualShooterButton;
    public JoystickButton intakeUpButton;
    public JoystickButton intakeDownButton;
    public Joystick manipulator;

    public OI() {

        manipulator = new Joystick(2);
        
        manualShooterButton = new JoystickButton(manipulator, 10);
        manualShooterButton.whileHeld(new ShooterManualControl());
        manualIntakeButton = new JoystickButton(manipulator, 9);
        manualIntakeButton.whileHeld(new IntakeManualControl());
        retractClimberButton = new JoystickButton(manipulator, 5);
        retractClimberButton.whenPressed(new Winch());
        extendClimberButton = new JoystickButton(manipulator, 7);
        extendClimberButton.whenPressed(new ExtendClimber(0,0));
        feedShooterButton = new JoystickButton(manipulator, 6);
        feedShooterButton.whileHeld(new FeedShooter());
        runShooterButton = new JoystickButton(manipulator, 8);
        runShooterButton.whileHeld(new RunShooter());
        intakeUpButton = new JoystickButton(manipulator, 4);
        intakeUpButton.whileHeld(new RaiseIntake());
        intakeDownButton = new JoystickButton(manipulator, 2);
        intakeDownButton.whileHeld(new LowerIntake());
        
        rightJoystick = new Joystick(1);
        
        shiftLowButton = new JoystickButton(rightJoystick, 3);
        shiftLowButton.whileHeld(new ShiftLow());
        alignDistanceButton = new JoystickButton(rightJoystick, 2);
        alignDistanceButton.whileHeld(new AutoAlignDistance());
        shootLowButton = new JoystickButton(rightJoystick, 1);
        shootLowButton.whileHeld(new ShootLow());
        
        leftJoystick = new Joystick(0);
        
        alignAngleButton = new JoystickButton(leftJoystick, 2);
        alignAngleButton.whileHeld(new AutoAlignAngle());
        intakeButton = new JoystickButton(leftJoystick, 1);
        intakeButton.whileHeld(new IntakeBoulder());
        
        

        SmartDashboard.putData("PID/Test DriveDistance PID", new TestPID(TestPID.System.DRIVEDISTANCE));
		SmartDashboard.putData("PID/Test DriveSlide PID", new TestPID(TestPID.System.DRIVEANGLE));
		SmartDashboard.putData("PID/Test DriveAngle PID", new TestPID(TestPID.System.SHOOTER));
        // SmartDashboard Buttons
        SmartDashboard.putData("AutoMode", new AutoMode(4, 2));
        SmartDashboard.putData("AutoDelay", new AutoDelay(0));
        SmartDashboard.putData("AutoDrive", new AutoDrive(0));
        SmartDashboard.putData("AutoTurn", new AutoTurn(0));
        SmartDashboard.putData("AutoAlignDistance", new AutoAlignDistance());
        SmartDashboard.putData("AutoAlignAngle", new AutoAlignAngle());
        SmartDashboard.putData("TeleopDriveMode", new TeleopDriveMode());
        SmartDashboard.putData("IntakeBoulder", new IntakeBoulder());
        SmartDashboard.putData("ShootLow", new ShootLow());
        SmartDashboard.putData("RunShooter", new RunShooter());
        SmartDashboard.putData("FeedShooter", new FeedShooter());
        SmartDashboard.putData("LowerIntake", new LowerIntake());
        SmartDashboard.putData("RaiseIntake", new RaiseIntake());
        SmartDashboard.putData("IntakeManualControl", new IntakeManualControl());
        SmartDashboard.putData("ShooterManualControl", new ShooterManualControl());
        SmartDashboard.putData("ExtendClimber", new ExtendClimber(0,0));
        SmartDashboard.putData("RetractClimber", new Winch());
        SmartDashboard.putData("UpdateDashboard", new UpdateDashboard());
        SmartDashboard.putData("StartCompressor", new StartCompressor());
        SmartDashboard.putData("ShiftLow", new ShiftLow());
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
}

