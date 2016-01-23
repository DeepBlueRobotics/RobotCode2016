// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


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
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());


    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
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
    public Joystick manipulator;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public OI() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

        manipulator = new Joystick(2);
        
        manualShooterButton = new JoystickButton(manipulator, 10);
        manualShooterButton.whileHeld(new ShooterManualControl());
        manualIntakeButton = new JoystickButton(manipulator, 9);
        manualIntakeButton.whileHeld(new IntakeManualControl());
        retractClimberButton = new JoystickButton(manipulator, 5);
        retractClimberButton.whenPressed(new RetractClimber());
        extendClimberButton = new JoystickButton(manipulator, 7);
        extendClimberButton.whenPressed(new ExtendClimber());
        feedShooterButton = new JoystickButton(manipulator, 6);
        feedShooterButton.whileHeld(new FeedShooter());
        runShooterButton = new JoystickButton(manipulator, 8);
        runShooterButton.whileHeld(new RunShooter());
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


        // SmartDashboard Buttons
        SmartDashboard.putData("AutoMode", new AutoMode());
        SmartDashboard.putData("AutoDelay", new AutoDelay());
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
        SmartDashboard.putData("ExtendClimber", new ExtendClimber());
        SmartDashboard.putData("RetractClimber", new RetractClimber());
        SmartDashboard.putData("UpdateDashboard", new UpdateDashboard());
        SmartDashboard.putData("StartCompressor", new StartCompressor());
        SmartDashboard.putData("ShiftLow", new ShiftLow());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    public Joystick getLeftJoystick() {
        return leftJoystick;
    }

    public Joystick getRightJoystick() {
        return rightJoystick;
    }

    public Joystick getManipulator() {
        return manipulator;
    }


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
}

