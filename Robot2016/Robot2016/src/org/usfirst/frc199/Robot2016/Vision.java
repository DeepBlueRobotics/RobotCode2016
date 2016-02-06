package org.usfirst.frc199.Robot2016;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;

import edu.wpi.first.wpilibj.CameraServer;

public class Vision {

	int session;
	Image frame;
	Image binaryFrame;

	boolean isEnabled = true;

	public Vision() {
		frame = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
		binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);

		session = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);

		NIVision.IMAQdxConfigureGrab(session);

		startLookingForTarget();
	}

	public void startLookingForTarget() {
		NIVision.IMAQdxStartAcquisition(session);

		CameraServer.getInstance().setImage(frame);

		while (isEnabled) {

			NIVision.IMAQdxGrab(session, frame, 1);
			

		}

	}

	// private final static String GRIP_CMD = "/usr/local/frc/JRE/bin/java -jar
	// /home/lvuser/grip.jar /home/lvuser/FindingTargets.grip";
	// NetworkTable table = NetworkTable.getTable("GRIP/myContoursReport");

	// ProcessBuilder builder = new ProcessBuilder(GRIP_CMD).inheritIO();
	// //.inheritIO().start()
	// Process gettingTargets;

	// public void init() {
	// startGripCommand();
	// }
	//
	// /**
	// * Starting the grip command and assign to a Process to allow it to be
	// destroyed.
	// */
	// public void startGripCommand(){
	// try {
	// gettingTargets = builder.start();
	// } catch (Exception e) {
	// }
	// }
	//
	// /**
	// * You need the total destruction
	// */
	// public void destoryGripCommand(){
	// gettingTargets.destroyForcibly();
	// }

	/**
	 * 
	 * Given the arrays centerX and centerY from the network tables. Will return
	 * the x, y coordinates that has the max width in which we want to shoot.
	 * 
	 * @return
	 */
	// public double[] findMidXYCoords(){
	// double[] x = new double[0];
	// table.getNumberArray("centerX", x);
	// double[] y = new double[0];
	// table.getNumberArray("centerY", y);
	// double[] width = new double[0];
	// table.getNumberArray("centerY", width);
	// int index = maxIndex(width);
	// return new double[]{x[index], y[index]};
	// }

	/**
	 * Returns index at which it has max double value in array input
	 * 
	 * @param input
	 * @return
	 */
	private int maxIndex(double[] input) {
		double max = 0;
		int index = 0;
		for (int i = 0; i < input.length; i++) {
			if (input[i] > max) {
				max = input[i];
				index = i;
			}
		}
		return index;
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
