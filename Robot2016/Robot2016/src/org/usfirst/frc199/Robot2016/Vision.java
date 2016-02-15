package org.usfirst.frc199.Robot2016;

import java.util.Comparator;
import java.util.Vector;

import javax.annotation.Generated;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.CannyOptions;
import com.ni.vision.NIVision.ColorMode;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.GetBisectingLineResult;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.Line;
import com.ni.vision.NIVision.MorphologyMethod;
import com.ni.vision.NIVision.Point;
import com.ni.vision.NIVision.RGBValue;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Vision {

	CameraServer server;
	USBCamera camera;

	int counter = 0;

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
	NIVision.Line crosshairsl;
	NIVision.Line crosshairsr;

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

						applyFilters();
						updateParticalAnalysisReports(hslimage);
						uploadToContourReport();

						generateCrossHairsAtCenterContour(0);

						// Now that we are correctly applying the threshold for
						// bright objects, we need to make sure
						// that the methods to get the particle data work.

						// OFF-TOPIC NOTE: we need to make sure that the cameras
						// are able to be mounted and learn about what
						// the plans are for the camera/light-ring.

						// So start with testing the particle method
						// updateParticalAnalysisReports USING THE HSLIMAGE
						// IMAGE
						// This is a key because we need to analysis the
						// filtered image with is the hslimage.

						// Then the next step is to upload that data to the
						// contour report, so using
						// the uploadToContourReport method.

						// If all of this works, the next thing to look for is
						// that the data is accurate and to put a x
						// on the center of the ORIGINAL FRAME and set that
						// frame to the camera server.

						// If all that works, the next part would be to get the
						// methods at the bottom to work with angles to
						// return boolean about the images.

//						CameraServer.getInstance().setImage(hslimage);
						CameraServer.getInstance().setImage(frame);
						Thread.sleep(100);
					}
				} catch (Exception e) {
					SmartDashboard.putString("Image Thread failure", e.toString());
					camera.closeCamera();
					isEnabled = false;
				}
			}

		};

		runCamera.start();
	}

	String cx = "center_mass_x";
	String cy = "center_mass_y";

	private void generateCrossHairsAtCenterContour(int i) {

		double centerx = contourReport.getNumber("contour" + i + "/" + cx, NIVision.imaqGetImageSize(frame).height / 2);
		double centery = contourReport.getNumber("contour" + i + "/" + cy, NIVision.imaqGetImageSize(frame).width / 2);

		// So, we need to set the cross hairs around the point.
		// So we need to set the lines around the cross hair to go from the
		// opposite corners to each other. Something like this:
		// \----/
		// -\--/-
		// --\/--
		// --/\--
		// -/--\-
		// /----\
		// The center is point (centerx, centery), and lets say that the length
		// of the lines will be LEN and let x = LEN/2(cos45).
		// So the top-left corner would be (centerx - x, centery - x) because at
		// 45 degrees, the length of dx and dy would be identical.
		// So the corners would be (centerx - x, centery - x), (centerx + x,
		// centery - y), (centerx - x, centery + y), and
		// (centerx + x, centery + y).

		// Set length to what we want x to be.
		int length = 20;
		NIVision.Point topleft = new NIVision.Point((int) (centerx - length), (int) (centery - length));
		NIVision.Point topright = new NIVision.Point((int) (centerx + length), (int) (centery - length));
		NIVision.Point bottomleft = new NIVision.Point((int) (centerx - length), (int) (centery + length));
		NIVision.Point bottomright = new NIVision.Point((int) (centerx + length), (int) (centery + length));
		
		SmartDashboard.putString("Putting crosshairs", "true");

		NIVision.imaqDrawLineOnImage(frame, frame, DrawMode.DRAW_VALUE, topleft, bottomright, 0);
		NIVision.imaqDrawLineOnImage(frame, frame, DrawMode.DRAW_VALUE, topright, bottomleft, 0);
//		NIVision.imaqDrawLineOnImage(hslimage, hslimage, DrawMode.DRAW_VALUE, topleft, bottomright, 0);
//		NIVision.imaqDrawLineOnImage(hslimage, hslimage, DrawMode.DRAW_VALUE, topright, bottomleft, 0);
	}

	final NIVision.Range GET_BRIGHTNESS = new NIVision.Range(240, 255);

	public void applyFilters() {
		NIVision.imaqColorThreshold(hslimage, frame, 255, ColorMode.RGB, GET_BRIGHTNESS, GET_BRIGHTNESS,
				GET_BRIGHTNESS);
	}

	public void updateParticalAnalysisReports(NIVision.Image image) {
		int numParticles = NIVision.imaqCountParticles(image, 0);
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
	public void writingImage() {
		try {
			Image bi = frame;
			String outputfile = "/home/lvuser/Image.png";
			SmartDashboard.putString("Writing File", "successful?");
			NIVision.imaqWriteFile(bi, outputfile, rgb);
		} catch (Exception e) {
			SmartDashboard.putString("Writing File", e.toString());
		}
	}

	// Analyze reports
	public void uploadToContourReport() {
		int num = 0;
		for (int i = 0; i < reports.length; i++) {
			if (reports[i].area > 200 && reports[i].convexHullPerimeter > 150 && reports[i].imageWidth > 0
					&& /*
						 * reports[i].imageWidth < 100 && reports[i].imageHeight
						 * < 100 &&
						 */ reports[i].imageHeight > 0 && reports[i].center_mass_y > 50) {
				counter++;
				SmartDashboard.putNumber("Looked for Data", counter);
				contourReport.putNumber("contour" + num + "/area", reports[i].area);
				contourReport.putNumber("contour" + num + "/percentAreaToImageArea", reports[i].percentAreaToImageArea);
				contourReport.putNumber("contour" + num + "/convexHullArea", reports[i].convexHullArea);
				contourReport.putNumber("contour" + num + "/convexHullPerimeter", reports[i].convexHullPerimeter);
				contourReport.putNumber("contour" + num + "/boundingRectTop", reports[i].boundingRectTop);
				contourReport.putNumber("contour" + num + "/boundingRectLeft", reports[i].boundingRectLeft);
				contourReport.putNumber("contour" + num + "/boundingRectBottom", reports[i].boundingRectBottom);
				contourReport.putNumber("contour" + num + "/boundingRectRight", reports[i].boundingRectRight);
				contourReport.putNumber("contour" + num + "/boundingRectWidth", reports[i].boundingRectWidth);
				contourReport.putNumber("contour" + num + "/" + cx, reports[i].center_mass_x);
				contourReport.putNumber("contour" + num + "/" + cy, reports[i].center_mass_y);
				contourReport.putNumber("contour" + num + "/imageWidth", reports[i].imageWidth);
				contourReport.putNumber("contour" + num + "/imageHeight", reports[i].imageHeight);
				num++;
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
