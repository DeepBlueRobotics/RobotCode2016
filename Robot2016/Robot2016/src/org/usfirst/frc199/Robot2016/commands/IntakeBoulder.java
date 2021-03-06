package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc199.Robot2016.Robot;

/**
 * Runs the rollers and belts inwards until a ball is detected.
 */
public class IntakeBoulder extends Command {

	public IntakeBoulder() {
//		requires(Robot.intake);
	}

	// Called just before this Command runs the first time
	protected void initialize() {

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (!Robot.intake.getBallSensor()) {
			if(!Robot.intake.getLowerLimit()) {
//				Robot.intake.pivot(-.6);
			}
			Robot.intake.setRoller(Robot.getPref("IntakeSpeed", 1.0));
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
