package org.usfirst.frc199.Robot2016;

import java.io.IOException;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Vision {

	// Reference https://github.com/WPIRoboticsProjects/GRIP/wiki/Tutorial:-Run-GRIP-from-a-CPP-or-Java-FRC-program
	// To learn about how to run GRIP in java programs.
	
	private final static String GRIP_CMD = "/usr/local/frc/JRE/bin/java -jar /home/lvuser/grip.jar /home/lvuser/FindingTargets.grip";
	NetworkTable table = NetworkTable.getTable("GRIP/myContoursReport");

	final String UrlOfCamera = "http://10.1.0.11";
	final int CannyDefaultThreshold = 50;

	public Vision() {
		try {
            new ProcessBuilder(GRIP_CMD).inheritIO().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void initCamera() {
		
	}
	
	/**	
	 * 
	 * Given the arrays centerX and centerY from the network tables.
	 * Will return the x, y coordinates that has the max width 
	 * in which we want to shoot.
	 * 
	 * @return
	 */
	public double[] findMidXYCoords(){
		double[] x = new double[0];
		table.getNumberArray("centerX", x);
		double[] y = new double[0];
		table.getNumberArray("centerY", y);
		double[] width = new double[0];
		table.getNumberArray("centerY", width);
		int index = maxIndex(width);
		return new double[]{x[index], y[index]};
	}
	
	/**
	 * returns index at which it has max double value in array input
	 * @param input
	 * @return
	 */
	private int maxIndex(double[] input){
		double max = 0;
		int index = 0;
		for(int i = 0; i < input.length; i++){
			if(input[i] > max){
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
