package org.usfirst.frc199.Robot2016.commands;

import org.usfirst.frc199.Robot2016.Robot;
import org.usfirst.frc199.Robot2016.motioncontrol.Path;
import org.usfirst.frc199.Robot2016.motioncontrol.Trajectory;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Follows a motion profile.
 */
public class FollowTrajectory extends Command {
	
	private final Trajectory t;
	private boolean finished;

	/**
	 * Travels to the given coordinates assuming zero initial/final velocity
	 * @param dx - Target horizontal displacement
	 * @param dy - Target vertical displacement
	 * @param dtheta - Target angle displacement
	 */
    public FollowTrajectory(double dx, double dy, double dtheta) {
        requires(Robot.drivetrain);
        Path p = new Path(0, 0, dx, dy, 0, dtheta, 2);
        t = new Trajectory(p, 0, 0, (int)(1000*Math.sqrt(dx*dx+dy*dy)));
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
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	int i = t.getCurrentIndex(Robot.drivetrain.getEncoderDistance());
    	double kA = Robot.getPref("kA", 0);
    	double kAlpha = Robot.getPref("kAlpha", 0);
    	double v = t.getV(i)*(1-kA+kA*t.getV(i+1));
    	double w = t.getW(i)*(1-kAlpha+kAlpha*t.getW(i+1));
    	Robot.drivetrain.followTrajectory(v, w);
    	finished = i>=1;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finished;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drivetrain.arcadeDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
