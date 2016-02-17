package org.usfirst.frc199.Robot2016;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;

import org.usfirst.frc199.Robot2016.commands.*;
import org.usfirst.frc199.Robot2016.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    Command autonomousCommand;

    public static OI oi;
    public static Vision vision;
    public static ArrayList <DashboardSubsystem> subsystems = new ArrayList<>();
    public static Drivetrain drivetrain;
    public static Shooter shooter;
    public static Intake intake;
    public static Climber climber;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	RobotMap.init();
    	vision = new Vision();
    	vision.startCamera();
        drivetrain = new Drivetrain();
        shooter = new Shooter();
        intake = new Intake();
        climber = new Climber();
        subsystems.add(climber);
    	subsystems.add(shooter);
    	subsystems.add(intake);
    	subsystems.add(drivetrain);
        oi = new OI();
        for(DashboardSubsystem s: Robot.subsystems) {
    		if(!s.getKey("").substring(0, 4).equals("PID/")) {
    			s.display("~TYPE~", "SubSystem");
    		}
    	}
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){

    }

    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    public void autonomousInit() {
    	Scheduler.getInstance().removeAll();
    	vision.writingImage();
    	int defense = (int)SmartDashboard.getNumber("Auto/Defense", 0.0);
    	int position = (int)SmartDashboard.getNumber("Auto/Position", 0.0);
    	autonomousCommand = new AutoMode(defense, position);
    	autonomousCommand.start();
    	new UpdateDashboard().start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
        Scheduler.getInstance().removeAll();
        vision.writingImage();
        new StartCompressor().start();
        new UpdateDashboard().start();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    /**
	 * Gets a preference or creates one if it does not yet exist
	 * @param key - The name of the preference
	 * @param defaultValue - Backup value if the preference is not found
	 * @return The value of the preference
	 */
	public static double getPref(String key, double defaultValue) {
		if(!Preferences.getInstance().containsKey(key)) {
			Preferences.getInstance().putDouble(key, defaultValue);
		}
		return Preferences.getInstance().getDouble(key, defaultValue);
	}
}
