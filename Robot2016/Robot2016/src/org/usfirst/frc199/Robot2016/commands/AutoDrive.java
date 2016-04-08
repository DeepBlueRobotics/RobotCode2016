package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2016.Robot;

/**
 * Autonomously travels forward a specified distance using PID.
 */
public class AutoDrive extends Command {

	private final double distance;
	private Timer t = new Timer();
	private double initial;

	/**
	 * @param distance - The distance to travel in inches
	 */
	public AutoDrive(double distance) {
		this.distance = distance;
        requires(Robot.drivetrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
//		Robot.drivetrain.setAutoTarget(distance, 0);
//		initial = Robot.drivetrain.getEncoderDistance();
		t.start();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
//		Robot.drivetrain.updateAuto();
		while(t.get() < 3) {
			Robot.drivetrain.arcadeDrive(1, 0);
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.drivetrain.autoReachedTarget() || 
//				(t.get()>0.5 && Robot.drivetrain.getEncoderDistance() == initial)
				t.get() > 3;
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

