package org.usfirst.frc199.Robot2016;

public class Vision {

	
	public Vision(){
		
	}
	
	public boolean isTargetInVisionOfRobotCamera(){
		return false;
	}
	
	public boolean isRobotDirectlyFacingTarget(){
		return false;
	}
	
	
	// Just has to a rough estimate: RIGHT: negative LEFT: positive
	public int degreeToTarget(){
		return 0;
	}
	
	
}
