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
	public AutoMode(int defense, int position, boolean autoalignangle, boolean autoaligndist, boolean shoot) {

		// If we are positioned on the field, we would need to drive a set
		// amount of distance
		int length_of_robot = 41;
		addSequential(new CalibrateGyro());
		addSequential(new AutoDrive(74 - length_of_robot+12 + 24 + 12 + length_of_robot), 2.5);
//		switch (defense) {
//		case 2: // portcullis
//			return;
//		case 3: // cheval de frise
//			addSequential(new AutoDrive(74 - length_of_robot+12));
//			addSequential(new LowerIntake());
//			addSequential(new AutoDrive(24 + 12 + length_of_robot));
//			break;
//		case 4: // moat
//			addSequential(new AutoDrive(74 - length_of_robot+12 + 24 + 12 + length_of_robot), 3);
//			break;
//		case 5: // ramparts
//			addSequential(new AutoDrive(74 - length_of_robot+12 + 24 + 12 + length_of_robot), 3);
//			break;
//		case 6: // drawbridge
//			return;
//		case 7: // sally port
//			return;
//		case 8: // rock wall
//			addSequential(new AutoDrive(74 - length_of_robot+12 + 24 + 12 + length_of_robot), 3);
//			break;
//		case 9: // rough terrain
//			addSequential(new AutoDrive(74 - length_of_robot+12 + 24 + 12 + length_of_robot), 3);
//			break;
//		default:
//			System.out.println("Wrong Defense");
//			return;
//		}
		if(!shoot) return;
		addSequential(new LowerIntake());
		switch (position) {
		case 2:
			addSequential(new AutoTurn(27.38), 3);
			addSequential(new AutoDrive(18), 3);
			if(autoalignangle) addSequential(new AutoAlignAngle(), 3);
			break;
		case 3:
			addSequential(new AutoTurn(9), 3);
			if(autoalignangle) addSequential(new AutoAlignAngle(), 3);
			break;
		case 4:
			addSequential(new AutoTurn(-3), 3);
			if(autoalignangle) addSequential(new AutoAlignAngle(), 3);
			break;
		case 5:
			addSequential(new AutoTurn(-25.88), 3);
			addSequential(new AutoDrive(18), 3);
			if(autoalignangle) addSequential(new AutoAlignAngle(), 3);
			break;
		case 6: // spy box
			break;
		default:
			System.out.println("Wrong position");
			return;
		}
		if(autoaligndist) addSequential(new AutoAlignDistance(), 3);
		addParallel(new RunShooter());
		addSequential(new RaiseIntake());
		addSequential(new AutoDelay(3.0));
		addSequential(new FeedShooter());

	}
}
