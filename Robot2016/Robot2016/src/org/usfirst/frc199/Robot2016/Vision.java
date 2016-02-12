package org.usfirst.frc199.Robot2016;

import java.util.Comparator;
import java.util.Vector;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.RGBValue;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Vision {

	CameraServer server;
	USBCamera camera;

	// Camera Constants
	static final int WIDTH = 320, HEIGHT = 240;
	static final int BRIGHTNESS = 50, EXPOSURE = 50;

	int session;

	Image frame;
	Image hslimage;

	Process process;

	Thread runCamera;
	Thread runGrip;

	RGBValue rgb = new RGBValue();

	NIVision.Rect rect;

	// Image processing that we did in GRIP: HSL - H:71-105, S:32-255, L:70-195
	// Finding contours - filter contours - what is being done in
	// updateParticleAnalysisReport()
	// Min Area: 75; Min Perimeter: 150; Min/Max Width: 0,100; Min/Max Height:
	// 0,100; Solidity: 0-100
	// Published Contours Report

	int minArea = 75, minPerimeter = 150;
	int minWidth = 0, maxWidth = 100;
	int minHeight = 0, maxHeight = 100;
	NIVision.Range solidity = new NIVision.Range(0, 100);

	private final NetworkTable contourReport = NetworkTable.getTable("CONTOURS");

	boolean isEnabled = true;

	public class ParticleReport implements Comparator<ParticleReport>, Comparable<ParticleReport> {
		public double percentAreaToImageArea;
		public double area;
		public double convexHullArea;
		public double convexHullPerimeter;
		public int boundingRectLeft;
		public int boundingRectTop;
		public int boundingRectRight;
		public int boundingRectBottom;
		public int center_mass_x;
		public int center_mass_y;
		public int imageHeight;
		public int imageWidth;
		public int boundingRectWidth;

		public ParticleReport() {
			implementation();
		}

		private void implementation() {
			percentAreaToImageArea = 0;
			area = 0;
			convexHullArea = 0;
			convexHullPerimeter = 0;
			boundingRectBottom = 0;
			boundingRectLeft = 0;
			boundingRectRight = 0;
			boundingRectTop = 0;
			boundingRectWidth = 0;
			center_mass_x = 0;
			center_mass_y = 0;
			imageHeight = 0;
			imageWidth = 0;
		}

		@Override
		public int compare(ParticleReport r1, ParticleReport r2) {
			return (int) (r1.area - r2.area);
		}

		@Override
		public int compareTo(ParticleReport r) {
			return (int) (r.area - this.area);
		}

	};

	ParticleReport[] reports;

	public Vision() {

		frame = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
		hslimage = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);

		// int square = 50;
		reports = null;

		camera = new USBCamera("cam0");
		camera.setSize(WIDTH, HEIGHT);
		// camera.setBrightness(BRIGHTNESS);
		// camera.setExposureManual(EXPOSURE);
		// rect = new NIVision.Rect(480 / 2 - square / 2, 640 / 2 - square / 2,
		// square, square);

		camera.openCamera();

	}

	public void startCamera() {
		runCamera = new Thread() {
			public synchronized void run() {
				try {
					isEnabled = true;
					camera.startCapture();
					while (true) {

						camera.getImage(frame);
						applyHSLFilter();
						CameraServer.getInstance().setImage(hslimage); // For
																		// Testing
																		// Purposes
						// CameraServer.getInstance().setImage(frame);
						// updateParticalAnalysisReports(frame);
						// uploadToContourReport();

						Thread.sleep(50);
					}
				} catch (Exception e) {
					SmartDashboard.putString("Image Thread failure", e.toString());
					camera.closeCamera();
					isEnabled = false;
				}
			}
		};
		runCamera.start();
		// try {
		// SmartDashboard.putNumber("Particles count: ",
		// NIVision.imaqCountParticles(frame, 1));
		// } catch (Exception e) {
		// SmartDashboard.putString("Count Particles", "Doesn't work");
		// }
	}

	NIVision.Range hueRange = new NIVision.Range(0, 255);// 71, 105);
	NIVision.Range satRange = new NIVision.Range(0, 255);// 32, 255);
	NIVision.Range luminescenceForHSL = new NIVision.Range(0, 255);// 70, 195);
	NIVision.Range valueForHSV = new NIVision.Range(0, 255);// (0,255);

	private void applyHSLFilter() {
		NIVision.imaqColorThreshold(hslimage, frame, 255, NIVision.ColorMode.HSL, hueRange, satRange,
				luminescenceForHSL);
	}

	public void updateParticalAnalysisReports(NIVision.Image image) {
		final int numParticles = NIVision.imaqCountParticles(image, 0);
		SmartDashboard.putNumber("Object removal blobs:", numParticles);
		final Vector<ParticleReport> particles = new Vector<ParticleReport>();
		if (numParticles > 0) {
			for (int particleIndex = 0; particleIndex < numParticles; particleIndex++) {
				final ParticleReport par = new ParticleReport();
				par.percentAreaToImageArea = NIVision.imaqMeasureParticle(image, particleIndex, 0,
						NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA);
				par.area = NIVision.imaqMeasureParticle(image, particleIndex, 0, NIVision.MeasurementType.MT_AREA);
				par.convexHullArea = NIVision.imaqMeasureParticle(image, particleIndex, 0,
						NIVision.MeasurementType.MT_CONVEX_HULL_AREA);
				par.convexHullPerimeter = NIVision.imaqMeasureParticle(image, particleIndex, 0,
						NIVision.MeasurementType.MT_CONVEX_HULL_PERIMETER);
				par.center_mass_x = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
						NIVision.MeasurementType.MT_CENTER_OF_MASS_X);
				par.center_mass_y = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
						NIVision.MeasurementType.MT_CENTER_OF_MASS_Y);
				par.imageHeight = NIVision.imaqGetImageSize(image).height;
				par.imageWidth = NIVision.imaqGetImageSize(image).width;
				particles.add(par);
			}
			particles.sort(null);
		}
		this.reports = new ParticleReport[particles.size()];
		particles.copyInto(this.reports);
	}

	// private final static String[] GRIP_ARGS = new String[] {
	// "/usr/local/frc/JRE/bin/java", "-jar",
	// "/home/lvuser/grip.jar", "/home/lvuser/project.grip" };
	//
	// public void init() {
	// try {
	// process = Runtime.getRuntime().exec(GRIP_ARGS);
	// SmartDashboard.putString("Running Grip?",
	// process.isAlive() + "?" + new
	// Scanner(process.getErrorStream()).nextLine());
	// } catch (Exception e) {
	// SmartDashboard.putString("Grip Thread Failure", e.toString());
	// }
	// }
	//
	// public void closeGripProcess() {
	// process.destroyForcibly();
	// }
	//
	// public void writingImage() {
	// try {
	// Image bi = frame;
	// String outputfile = "/home/lvuser/Image.png";
	// SmartDashboard.putString("Writing File", "successful?");
	// NIVision.imaqWriteFile(bi, outputfile, rgb);
	// } catch (Exception e) {
	// SmartDashboard.putString("Writing File", e.toString());
	// }
	// }

	// Analyze reports
	public void uploadToContourReport() {
		contourReport.putString("Test", "test");
		for (int i = 0; i < reports.length; i++) {
			// Min Area: 75; Min Perimeter: 150; Min/Max Width: 0,100; Min/Max
			// Height: 0,100;
			if (reports[i].area > 75 && reports[i].convexHullPerimeter > 150 && reports[i].imageWidth < 100
					&& reports[i].imageHeight < 100) {
				contourReport.putNumber("contour" + i + "/area", reports[i].area);
				contourReport.putNumber("contour" + i + "/percentAreaToImageArea", reports[i].percentAreaToImageArea);
				contourReport.putNumber("contour" + i + "/convexHullArea", reports[i].convexHullArea);
				contourReport.putNumber("contour" + i + "/convexHullPerimeter", reports[i].convexHullPerimeter);
				contourReport.putNumber("contour" + i + "/boundingRectTop", reports[i].boundingRectTop);
				contourReport.putNumber("contour" + i + "/boundingRectLeft", reports[i].boundingRectLeft);
				contourReport.putNumber("contour" + i + "/boundingRectBottom", reports[i].boundingRectBottom);
				contourReport.putNumber("contour" + i + "/boundingRectRight", reports[i].boundingRectRight);
				contourReport.putNumber("contour" + i + "/boundingRectWidth", reports[i].boundingRectWidth);
				contourReport.putNumber("contour" + i + "/center_mass_x", reports[i].center_mass_x);
				contourReport.putNumber("contour" + i + "/center_mass_y", reports[i].center_mass_y);
				contourReport.putNumber("contour" + i + "/imageWidth", reports[i].imageWidth);
				contourReport.putNumber("contour" + i + "/imageHeight", reports[i].imageHeight);
			} else {
				contourReport.putString("contour" + i + "/Does Not Meet Constraints", "error");
				contourReport.putNumber("contour" + i + "/area", reports[i].area);
				contourReport.putNumber("contour" + i + "/convexHullArea", reports[i].convexHullArea);
				contourReport.putNumber("contour" + i + "/convexHullPerimeter", reports[i].convexHullPerimeter);
				contourReport.putNumber("contour" + i + "/center_mass_x", reports[i].center_mass_x);
				contourReport.putNumber("contour" + i + "/center_mass_y", reports[i].center_mass_y);
			}
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
	// private int maxIndex(double[] input) {
	// double max = 0;
	// int index = 0;
	// for (int i = 0; i < input.length; i++) {
	// if (input[i] > max) {
	// max = input[i];
	// index = i;
	// }
	// }
	// return index;
	// }

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
