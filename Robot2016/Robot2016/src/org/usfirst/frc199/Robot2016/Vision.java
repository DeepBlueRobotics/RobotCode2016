package org.usfirst.frc199.Robot2016;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class Vision {

	String urlOfCamera = "http://10.1.0.11";

	VideoCapture camera;
	Mat frame = new Mat();

	public Vision() {
		try {
			System.out.println("It started the camera constructor.");
			camera = new VideoCapture(urlOfCamera);
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
	}

	public void initCamera() {
		camera.read(frame);
		System.out.println("Frame Obtained");
	}

	/**
	 * did the target enter the camera's vision?
	 * 
	 * @return answer to last question
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
