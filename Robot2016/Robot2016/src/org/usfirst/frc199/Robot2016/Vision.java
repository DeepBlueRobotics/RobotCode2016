package org.usfirst.frc199.Robot2016;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class Vision {

	final String UrlOfCamera = "http://10.1.0.11";
	final int CannyDefaultThreshold = 50;

	VideoCapture camera;
	Mat frame = new Mat();
	Mat greyscale;

	public Vision() {
		try {
			System.out.println("It started the camera constructor.");
			camera = new VideoCapture(UrlOfCamera);
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
	}

	public void initCamera() {
		camera.retrieve(frame);
		System.out.println("Frame Obtained");
	}
	
	/**
	 * 
	 * Applies canny edge detection to given image. In this case, that image is
	 * a mat (Matrix with rgb values)
	 * 
	 * @param image Image inputed by camera 
	 * @return A matrix that 
	 */
	public Mat doCanny(Mat image){
		Mat grayImage = new Mat();
		Mat detectedEdges = new Mat();
		Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(grayImage, detectedEdges, new Size(3,3));
		Imgproc.Canny(detectedEdges, detectedEdges, CannyDefaultThreshold, CannyDefaultThreshold * 3, 3, false);
		return image;
	}
	
	/**
	 * did the target (retro-reflective tape) enter the camera's vision?
	 * 
	 * @return true if the entire U shape of retro-reflective tape is in the frame
	 */
	public boolean isTargetInVisionOfRobotCamera(Mat image) {
		
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
