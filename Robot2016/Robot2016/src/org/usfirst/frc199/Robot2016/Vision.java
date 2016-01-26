package org.usfirst.frc199.Robot2016;

public class Vision {

	
	public Vision(){
		
	}
	
	/**
	 * did the target enter the camera's vision?
	 * 
	 * @return answer to last question
	 */
	public boolean isTargetInVisionOfRobotCamera(){
		return false;
	}

	/**
	 * is the target ready to be shot at
	 * 
	 * @return ready to shoot?
	 */
	public boolean isRobotDirectlyFacingTarget(){
		return false;
	}
	
	/**
	 * Just has to a rough estimate: RIGHT: negative LEFT: positive in relation to our current position
	 * 
	 * @return degrees
	 */
	public double degreeToTarget(){
		return 0;
	}
	
	
}
