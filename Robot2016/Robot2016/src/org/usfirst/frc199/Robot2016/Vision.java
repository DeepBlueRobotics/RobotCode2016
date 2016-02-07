package org.usfirst.frc199.Robot2016;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Vision {

	CameraServer server;
	String camera = "cam0";

	int session;
	Image frame;
	Image frame2;
	Image binaryFrame;

	NIVision.Range hue = new NIVision.Range(0, 105);
	NIVision.Range sat = new NIVision.Range(32, 255);
	NIVision.Range val = new NIVision.Range(0, 255);

	NIVision.Rect rect;

	boolean isEnabled = true;

	public Vision() {

		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		frame2 = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);

		int square = 50;
		rect = new NIVision.Rect(480 / 2 - square / 2, 640 / 2 - square / 2, square, square);

		session = NIVision.IMAQdxOpenCamera(camera, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		NIVision.IMAQdxConfigureGrab(session);

		NIVision.IMAQdxStartAcquisition(session);
		runCameraViewer();
		init();
	}

	public void runCameraViewer() {
		int counter = 0;
		while (isEnabled) {
			NIVision.IMAQdxGrab(session, frame, 1);

			// Manipulate frame
			NIVision.imaqDrawShapeOnImage(frame, frame, rect, DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 0);

			// Send frame2 to a widget
			CameraServer.getInstance().setImage(frame);
			counter++;
			if (counter > 10000)
				isEnabled = !isEnabled;
		}
		NIVision.IMAQdxCloseCamera(session);
	}

	private final static String[] GRIP_ARGS = new String[] { "/usr/local/frc/JRE/bin/java", "-jar",
			"/home/lvuser/grip.jar", "/home/lvuser/project.grip" };
	NetworkTable table = NetworkTable.getTable("GRIP/myContoursReport");

	public void init() {
		startGripCommand();
	}

	/**
	 * Starting the grip command and assign to a Process to allow it to be
	 * destroyed.
	 */
	public void startGripCommand() {
		try {
			Runtime.getRuntime().exec(GRIP_ARGS);
		} catch (Exception e) {
		}
	}

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
