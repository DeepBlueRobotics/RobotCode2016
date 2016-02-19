package org.usfirst.frc199.Robot2016;

import org.usfirst.frc199.Robot2016.commands.*;

import edu.wpi.first.wpilibj.Joystick;
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
    public Joystick manipulator;

    public OI() {

        manipulator = new Joystick(2);
        
        manualShooterButton = new JoystickButton(manipulator, 10);
        manualShooterButton.toggleWhenPressed(new ShooterManualControl());
        manualIntakeButton = new JoystickButton(manipulator, 9);
        manualIntakeButton.toggleWhenPressed(new IntakeManualControl());
        
//        retractClimberButton = new JoystickButton(manipulator, 5);
//        retractClimberButton.whileHeld(new Winch());
//        extendClimberButton = new JoystickButton(manipulator, 7);
//        extendClimberButton.whenPressed(new ExtendClimber(0,0));
        
        intakeButton = new JoystickButton(manipulator, 5);
        intakeButton.whenPressed(new IntakeBoulder());
        shootLowButton = new JoystickButton(manipulator, 7);
        shootLowButton.whenPressed(new ShootLow());
        feedShooterButton = new JoystickButton(manipulator, 6);
        feedShooterButton.whileHeld(new FeedShooter());
        runShooterButton = new JoystickButton(manipulator, 8);
        runShooterButton.whileHeld(new RunShooter());
        intakeUpButton = new JoystickButton(manipulator, 4);
        intakeUpButton.whileHeld(new RaiseIntake());
        intakeDownButton = new JoystickButton(manipulator, 2);
        intakeDownButton.whileHeld(new LowerIntake());
        
        rightJoystick = new Joystick(1);

        alignDistanceButton = new JoystickButton(rightJoystick, 2);
        alignDistanceButton.whileHeld(new AutoAlignDistance());
        shootLowButton2 = new JoystickButton(rightJoystick, 1);
        shootLowButton2.whileHeld(new ShootLow());
        
        leftJoystick = new Joystick(0);
        
        alignAngleButton = new JoystickButton(leftJoystick, 2);
        alignAngleButton.whileHeld(new AutoAlignAngle());
        intakeButton2 = new JoystickButton(leftJoystick, 1);
        intakeButton2.whileHeld(new IntakeBoulder());        

        // Test PID Commands
        SmartDashboard.putData("PID/DriveDistance/TestDriveDistancePID", new TestPID(TestPID.System.DRIVEDISTANCE));
		SmartDashboard.putData("PID/DriveAngle/TestDriveAnglePID", new TestPID(TestPID.System.DRIVEANGLE));
		SmartDashboard.putData("PID/DriveVelocity/TestDriveVelocityPID", new TestPID(TestPID.System.DRIVEVELOCITY));
		SmartDashboard.putData("PID/DriveAngularVelocity/TestDriveAngularVelocityPID", new TestPID(TestPID.System.DRIVEANGULARVELOCITY));
		SmartDashboard.putData("PID/Shooter/TestShooterPID", new TestPID(TestPID.System.SHOOTER));
		SmartDashboard.putData("PID/Intake/TestIntakePID", new TestPID(TestPID.System.INTAKE));
        
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
        SmartDashboard.putData("ExtendClimber", new ExtendClimber());
        SmartDashboard.putData("RetractClimber", new Winch());
        SmartDashboard.putData("UpdateDashboard", new UpdateDashboard());
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
