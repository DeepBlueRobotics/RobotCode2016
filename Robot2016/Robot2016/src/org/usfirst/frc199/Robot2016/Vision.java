package org.usfirst.frc199.Robot2016;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Vision {

	NetworkTable table;

	private final static String GRIP_CMD = "/usr/local/frc/JRE/bin/java -jar /home/lvuser/grip.jar /home/lvuser/FindingTargets.grip";

	final String UrlOfCamera = "http://10.1.0.11";
	final int CannyDefaultThreshold = 50;

	public Vision() {
		table = NetworkTable.getTable("GRIP/myContourReport");
	}

	public void initCamera() {
	}

	/**
	 * did the target (retro-reflective tape) enter the camera's vision?
	 * 
	 * @return true if the entire U shape of retro-reflective tape is in the
	 *         frame
	 */
	public boolean isTargetInVisionOfRobotCamera() {

		return false;
	}

	/**
	 * is the target ready to be shot at
	 * 
	 * @return ready to shoot?
	 */
	public boolean isRobotDirectlyFacingTarget() {
		return false;
	}

	/**
	 * Just has to a rough estimate: RIGHT: negative LEFT: positive in relation
	 * to our current position
	 * 
	 * @return degrees
	 */
	public double degreeToTarget() {
		return 0;
	}

}
