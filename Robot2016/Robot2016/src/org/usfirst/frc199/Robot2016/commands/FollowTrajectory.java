package org.usfirst.frc199.Robot2016.commands;

import org.usfirst.frc199.Robot2016.Robot;
import org.usfirst.frc199.Robot2016.motioncontrol.Path;
import org.usfirst.frc199.Robot2016.motioncontrol.Trajectory;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Follows a motion profile.
 */
public class FollowTrajectory extends Command {
	
	private Trajectory t = null;
	private boolean finished = false, readFromSD = false;

	/**
	 * Follows a trajectory specified through SmartDashboard
	 */
	public FollowTrajectory() {
		readFromSD = true;
	}
	
	/**
	 * Travels to the given coordinates assuming zero initial/final velocity
	 * @param dx - Target horizontal displacement
	 * @param dy - Target vertical displacement
	 * @param dtheta - Target angle displacement
	 */
    public FollowTrajectory(double dx, double dy, double dtheta) {
        this(new Path(0, 0, dx, dy, 0, dtheta, 2));
    }
    
    /**
     * Follows the given path assuming zero initial/final velocity
     * @param p - The path to follow
     */
    public FollowTrajectory(Path p) {
        requires(Robot.drivetrain);
        double x2 = p.getX(1);
        double y2 = p.getY(1);
        t = new Trajectory(p, 0, 0, (int)(1000*Math.sqrt(x2*x2+y2*y2)));
    }
    
    /**
     * Follows the given trajectory
     * @param t - The trajectory to follow
     */
    public FollowTrajectory(Trajectory t) {
        requires(Robot.drivetrain);
        this.t = t;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	finished = false;
    	SmartDashboard.putBoolean("MotionProfile/Start", true);
    	if(readFromSD) {
    		double dx = SmartDashboard.getNumber("MotionProfile/dX");
    		double dy = SmartDashboard.getNumber("MotionProfile/dY");
    		double dTheta = SmartDashboard.getNumber("MotionProfile/dTheta");
    		double maxV = SmartDashboard.getNumber("MotionProfile/MaxV");
    		double maxA = SmartDashboard.getNumber("MotionProfile/MaxA");
    		double maxW = SmartDashboard.getNumber("MotionProfile/MaxW");
    		double maxAlpha = SmartDashboard.getNumber("MotionProfile/MaxAlpha");
    		Path p = new Path(0, 0, dx, dy, 0, dTheta, 2);
    		double x2 = p.getX(1);
            double y2 = p.getY(1);
    		t = new Trajectory(p, 0, 0, maxV, maxA, maxW, maxAlpha, (int)(1000*Math.sqrt(x2*x2+y2*y2)));
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	int i = t.getCurrentIndex(Robot.drivetrain.getEncoderDistance());
    	double v = t.getV(i);
    	double v1 = t.getV(i+1);
    	double w = t.getW(i);
    	double w1 = t.getW(i+1);
    	double a = (v1-v)*(v+v1)/2/t.getDisplacement(i);
    	double alpha = (w1-w)*(w+w1)/2/t.getDisplacement(i);
    	Robot.drivetrain.followTrajectory(v, w, a, alpha);
    	finished = i>=1;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finished;
    }

    // Called once after isFinished returns true
    protected void end() {
    	// Continue moving if final velocity isn't zero
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.drivetrain.arcadeDrive(0, 0);
    }
}
