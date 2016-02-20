package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Determines starting location and defense to cross from SmartDashboard.
 */
public class AutoMode extends CommandGroup {

	/**
	 * @param defense
	 *            - the defense which we are trying to pass 1 - Low bar (X) 2 -
	 *            Portcullis (A) 3 - Cheval de frise (A) 4 - Moat (B) 5 -
	 *            Ramparts (B) 6 - Drawbridge (C) 7 - Sally Port (C) 8 - Rock
	 *            Wall (D) 9 - Rough Terrain (D)
	 * @param position
	 *            - can be 1, 2, 3, 4, 5
	 */
	public AutoMode(int defense, int position) {

		// If we are positioned on the field, we would need to drive a set
		// amount of distance
		int length_of_robot = 41;
		addSequential(new AutoDrive(74 - length_of_robot));

		switch (defense) {
		case 2: // portcullis
			return;
		case 3: // cheval de frise
			addSequential(new AutoDrive(12));
			addSequential(new LowerIntake());
			addSequential(new AutoDrive(24 + 12 + length_of_robot));
			break;
		case 4: // moat
			addSequential(new AutoDrive(12 + 24 + 12 + length_of_robot));
			break;
		case 5: // ramparts
			addSequential(new AutoDrive(12 + 24 + 12 + length_of_robot));
			break;
		case 6: // drawbridge
			return;
		case 7: // sally port
			return;
		case 8: // rock wall
			addSequential(new AutoDrive(12 + 24 + 12 + length_of_robot));
			break;
		case 9: // rough terrain
			addSequential(new AutoDrive(12 + 24 + 12 + length_of_robot));
			break;
		default:
			System.out.println("Wrong Defense");
			return;
		}

		switch (position) {
		case 2:
			addSequential(new AutoTurn(-Math.toDegrees(Math.atan(5.5 / (14 + Math.sqrt(3) / 2)))));
			addSequential(new AutoAlignAngle());
			break;
		case 3:
			addSequential(new AutoTurn(-Math.toDegrees(Math.atan(3.5 / (14 + Math.sqrt(3) / 2)))));
			addSequential(new AutoAlignAngle());
			break;
		case 4:
			addSequential(new AutoAlignAngle());
			break;
		case 5:
			addSequential(new AutoTurn(Math.toDegrees(Math.atan(1.5 / (14 + Math.sqrt(3) / 2)))));
			addSequential(new AutoAlignAngle());
			break;
		case 6: // spy box
			return;
		default:
			System.out.println("Wrong position");
			return;
		}

		addParallel(new RunShooter());
		addSequential(new AutoDelay(2.0));
		addSequential(new FeedShooter());

	}
}
