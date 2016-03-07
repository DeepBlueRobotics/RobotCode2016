package org.usfirst.frc199.Robot2016;

import java.util.Comparator;
import java.util.Vector;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.ColorMode;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.FlipAxis;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.MeasurementType;
import com.ni.vision.NIVision.RGBValue;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 * 
 * Using Microsoft Lifecam HD-3000 Camera: for more information see -
 * http://www.andymark.com/product-p/am-3025.html
 * 
 * This vision class uses NIVision to filter the image and then publish the
 * target's location onto a contour report found on the outline viewer. (See
 * uploadToContourReport method) The class uses its own thread to releases its
 * reliance on the main robot thread and also to create a safety net for an
 * event of vision failure.
 * 
 * @author Alex Zuckut Deep Blue Robotics 2016
 */
public class Vision {

	// Camera implementation
	USBCamera camera;

	// Camera Constants
	static final int WIDTH = 320, HEIGHT = 240;
	static final int BRIGHTNESS = 50, EXPOSURE = 50;

	// The port in which the camera runs
	int session;

	// Images that the camera uses
	Image frame;
	Image hslimage;

	// Thread for running the camera
	Thread runCamera;

	// Objects used for filtering the image
	RGBValue rgb = new RGBValue();
	final NIVision.Range GET_BRIGHTNESS_GREEN = new NIVision.Range(225, 255);
	final NIVision.Range GET_BRIGHTNESS_GENERAL = new NIVision.Range(235, 255);

	// Lines that are drawn on the middle of the target area.
	NIVision.Line crosshairsl;
	NIVision.Line crosshairsr;

	// Network Table to which all the data gets committed to - reports is the
	// data.
	private final NetworkTable contourReport = NetworkTable.getTable("CONTOURS");
	ParticleReport[] reports;

	// Constants for easy use.
	String cx = "center_mass_x";
	String cy = "center_mass_y";
	private static int AREA_DEFAULT = 35;
	boolean isEnabled = true;

	/**
	 * 
	 * This class is merely an aid for data on all the particles that the robot
	 * finds.
	 * 
	 * @author Alex Zuckut 2016 Deep Blue Robotics
	 */
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
		public int first_x;

		/**
		 * Simple Constructor - initialized all values.
		 */
		public ParticleReport() {
			implementation();
		}

		/**
		 * Initialization
		 */
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
			first_x = Vision.WIDTH / 2;
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

	/**
	 * This constuctor implements the vision class which implements the camera
	 * and then sets the size to 320x240 and finally opens the camera for use.
	 */
	public Vision() {
		try {
			frame = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
			hslimage = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);

			reports = null;
			SmartDashboard.putNumber("Is in hsl", 0);
			SmartDashboard.putBoolean("Needs flip?", true);

			camera = new USBCamera("cam0");
			camera.setSize(WIDTH, HEIGHT);

			camera.openCamera();
		} catch (Exception e) {
			System.out.println("error");
		}
	}

	/**
	 * This implements the startCamera thread and then starts that thread.
	 * 
	 * In the thread, an image is taken from the camera and filters the camera
	 * using a RGB filter and analyzes that filtered image and finally uploads
	 * that data to the contour report.
	 * 
	 * From that data, it draws the cross hairs onto the image that is returned
	 * to the live feed which gets uploaded back to the camera server - and then
	 * to the feed.
	 */
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
						double x = SmartDashboard.getNumber("Is in hsl", 0);
						boolean flip = SmartDashboard.getBoolean("Needs flip?");
						if (x == 0)
							CameraServer.getInstance().setImage(flip ? flipImage(frame) : frame);
						else
							CameraServer.getInstance().setImage(flip ? flipImage(hslimage) : frame);
						Thread.sleep(100);
					}
				} catch (Exception e) {
					// In case of failure - camera and all vision subsets are
					// cut.
					SmartDashboard.putString("Image Thread failure", e.toString());
					camera.closeCamera();
					isEnabled = false;
				}
			}

		};
		try {
			runCamera.start();
		} catch (Exception e) {
			System.out.println("error");
		}
	}

	/**
	 * This generates the cross hairs at the contour provided. The cross hairs
	 * are just the combination of two lines.
	 * 
	 * @param i
	 *            Is the blob that the cross hair is being generated around.
	 */
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

		NIVision.imaqDrawLineOnImage(frame, frame, DrawMode.DRAW_VALUE, topleft, bottomright, 0);
		NIVision.imaqDrawLineOnImage(frame, frame, DrawMode.DRAW_VALUE, topright, bottomleft, 0);
	}

	/**
	 * This method applies a RGB filter to the feed of the camera and returns
	 * that feed as an image in the variable hslimage.
	 */
	public void applyFilters() {
		NIVision.imaqColorThreshold(hslimage, frame, 255, ColorMode.RGB, GET_BRIGHTNESS_GENERAL, GET_BRIGHTNESS_GREEN,
				GET_BRIGHTNESS_GENERAL);
	}

	/**
	 * Gets the data of each respective particle of each image and returns the
	 * data of the binary image through a particle report.
	 * 
	 * @param image
	 *            The image that the method uses to count the particles and
	 *            returns the data. It has to be a binary image or only
	 *            containing black and white pixels.
	 */
	public void updateParticalAnalysisReports(NIVision.Image image) {
		int numParticles = NIVision.imaqCountParticles(image, 0);
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
				par.first_x = (int) NIVision.imaqMeasureParticle(image, particleIndex, 0,
						NIVision.MeasurementType.MT_FIRST_PIXEL_X);
				par.boundingRectWidth = (int) (NIVision.imaqMeasureParticle(image, particleIndex, 0,
						MeasurementType.MT_BOUNDING_RECT_WIDTH));
				par.boundingRectTop = (int) (NIVision.imaqMeasureParticle(image, particleIndex, 0,
						MeasurementType.MT_BOUNDING_RECT_TOP));
				par.boundingRectBottom = (int) (NIVision.imaqMeasureParticle(image, particleIndex, 0,
						MeasurementType.MT_BOUNDING_RECT_BOTTOM));
				par.boundingRectLeft = (int) (NIVision.imaqMeasureParticle(image, particleIndex, 0,
						MeasurementType.MT_BOUNDING_RECT_LEFT));
				par.boundingRectRight = (int) (NIVision.imaqMeasureParticle(image, particleIndex, 0,
						MeasurementType.MT_BOUNDING_RECT_RIGHT));
				par.imageHeight = NIVision.imaqGetImageSize(image).height;
				par.imageWidth = NIVision.imaqGetImageSize(image).width;
				particles.add(par);
			}
			particles.sort(null);
		}
		this.reports = new ParticleReport[particles.size()];
		particles.copyInto(this.reports);
	}

	/**
	 * Simply writes the rgb (frame) image to the path: /home/lvuser/Image.png
	 * 
	 */
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

	/**
	 * Uploads the data that satisfies the constraints to the Network Table
	 * previously specified. The data that is returns goes into the network
	 * table into subclasses named: "contourX/DATA" where "X" is the contour
	 * number and "DATA" is the data that is being published.
	 * 
	 */
	public void uploadToContourReport() {
		int num = 0;
		for (int i = 0; i < reports.length; i++) {
			if (reports[i].area > AREA_DEFAULT && reports[i].convexHullPerimeter > 2 * AREA_DEFAULT
					&& reports[i].imageWidth > 0 && reports[i].imageHeight > 0
					&& reports[i].center_mass_y > 2 * AREA_DEFAULT) {
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
				contourReport.putNumber("contour" + num + "/first_x", reports[i].first_x);
				contourReport.putNumber("contour" + num + "/imageWidth", reports[i].imageWidth);
				contourReport.putNumber("contour" + num + "/imageHeight", reports[i].imageHeight);
				num++;
			}
		}
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
		double pixelsOff = Math
				.abs(Vision.WIDTH / 2 - ((int) (contourReport.getNumber("contour0/first_x", Vision.WIDTH / 2))
						+ ((int) (contourReport.getNumber("contour0/boundingRectWidth", 0)) / 2)));
		double d = (Vision.WIDTH / 2) / Math.tan(Math.toRadians(34));
		return Math.toDegrees(Math.atan(pixelsOff / d));
	}

	/**
	 * Functionality for flipping the image. Requires a image and returns that
	 * image flipped.
	 * 
	 * @param image
	 *            An NIVision.Image
	 * @return The same NIVision image being flipped over the horizontal axis to
	 *         flip the camera which was mounted in the opposite direction.
	 */
	public NIVision.Image flipImage(NIVision.Image image) {
		NIVision.imaqFlip(image, image, FlipAxis.HORIZONTAL_AXIS);
		return image;
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
	// process.destroyForcibly(); // Best method ever.
	// }
	//

}
