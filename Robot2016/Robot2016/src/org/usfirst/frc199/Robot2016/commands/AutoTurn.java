package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc199.Robot2016.Robot;

/**
 * Autonomously turns a specified angle using PID.
 */
public class AutoTurn extends Command {

    public final double angle;
	private Timer t = new Timer();
	private double initial;
    
    public AutoTurn(double angle) {
		this.angle = angle;
        requires(Robot.drivetrain);
	}
    public AutoTurn() {
    	this.angle = SmartDashboard.getNumber("Contour/thetaTurn", 0);
    	requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
	protected void initialize() {
		Robot.drivetrain.setAngleTarget(angle);
		t.start();
		initial = Robot.drivetrain.getGyroAngle();
	}

	protected void execute() {
		Robot.drivetrain.updateAngle();
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.drivetrain.angleReachedTarget() || (t.get()>0.5 && Math.abs(Robot.drivetrain.getGyroAngle()-initial) < 1);
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drivetrain.arcadeDrive(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
