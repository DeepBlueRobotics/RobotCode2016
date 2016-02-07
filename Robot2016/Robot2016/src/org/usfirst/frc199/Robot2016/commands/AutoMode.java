package org.usfirst.frc199.Robot2016.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Determines starting location and defense to cross from SmartDashboard.
 */
public class AutoMode extends CommandGroup {

	/**
	 * @param defense - 
	 *            the defense which we are trying to pass 1 - Low bar (X) 2 -
	 *            Portcullis (A) 3 - Cheval de frise (A) 4 - Moat (B) 5 -
	 *            Ramparts (B) 6 - Drawbridge (C) 7 - Sally Port (C) 8 - Rock
	 *            Wall (D) 9 - Rough Terrain (D)
	 * @param position - 
	 *            can be 1, 2, 3, 4, 5
	 */
	public AutoMode(int defense, int position) {

		addSequential(new AutoDrive(30));

		switch (defense) {
		case 2: // portcullis
			addSequential(new AutoDrive(20)); // 20 inches
			addSequential(new RaiseIntake());
			addSequential(new AutoDrive(30));
			break;
		case 3: // cheval de frise
			addSequential(new AutoDrive(20)); // 20 inches
			addSequential(new LowerIntake());
			addSequential(new AutoDrive(30));
			break;
		case 4: // moat
			addSequential(new AutoDrive(50)); // 50 inches
			break;
		case 5: // ramparts
			addSequential(new AutoDrive(50)); // 50 inches
			break;
		case 6: // drawbridge
			return;
		case 7: // sally port
			return;
		case 8: // rock wall
			addSequential(new AutoDrive(50)); // 50 inches
			break;
		case 9: // rough terrain
			addSequential(new AutoDrive(50)); // 50 inches
			break;
		default:
			System.out.println("Wrong Defense");
			return;
		}

		switch (position) {
		case 2:
			addSequential(new AutoDrive(70)); // 70 inches
			addSequential(new AutoTurn(-30)); // -40 degrees (towards the right)
			addSequential(new AutoAlignAngle()); // Of Course, assuming that the camera can see the target in its current position
			break;
		case 3:
			addSequential(new AutoDrive(60)); // 60 inches
			addSequential(new AutoAlignAngle()); // Of Course, assuming that the camera can see the target in its current position
			break;
		case 4:
			addSequential(new AutoDrive(60)); // 60 inches
			addSequential(new AutoAlignAngle()); // Of Course, assuming that the camera can see the target in its current position
			break;
		case 5:
			addSequential(new AutoDrive(70)); // 70 inches
			addSequential(new AutoTurn(10)); // 10 degrees (towards the left)
			addSequential(new AutoAlignAngle()); // Of Course, assuming that the camera can see the target in its current position
			break;
		default:
			System.out.println("Wrong position");
			break;
		}
		
		addParallel(new RunShooter());
		addSequential(new AutoDelay(2.0));
		addSequential(new FeedShooter());
	}
}
