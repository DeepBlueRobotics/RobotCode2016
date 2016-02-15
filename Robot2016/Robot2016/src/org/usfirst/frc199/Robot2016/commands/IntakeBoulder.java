package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2016.Robot;

/**
 * Runs the rollers and belts inwards until a ball is detected.
 */
public class IntakeBoulder extends Command {

	public IntakeBoulder() {
		requires(Robot.intake);
		requires(Robot.shooter);
	}

	// Called just before this Command runs the first time
	protected void initialize() {

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.intake.setRoller(Preferences.getInstance().getDouble("IntakeRollerSpeed", 0.7));
		if (!Robot.intake.getBallSensor()) {
			Robot.intake.setRoller(Preferences.getInstance().getDouble("IntakeRollerSpeed", 0.7));
		} else {
			Robot.intake.setRoller(0);
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return Robot.intake.getBallSensor();
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.intake.setRoller(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
