package motionprofiler;

/**
 * Generates velocity values for a set of points along a path.
 */
public class Trajectory {
	
	private final Path path;
	private final double vmax, amax, wmax, alphamax;
	private final double[] velocities;
	
	/**
	 * Generates a trajectory that follows a given path under the given constraints
	 * @param path - the path to follow
	 * @param v0 - the initial velocity
	 * @param v1 - the final velocity
	 * @param vmax - the absolute maximum allowed velocity
	 * @param amax - the absolute maximum allowed acceleration
	 * @param wmax - the absolute maximum allowed angular velocity
	 * @param alphamax - the absolute maximum allowed angular acceleration
	 * @param points - the number of points to sample along the trajectory
	 */
	public Trajectory(Path path, double v0, double v1, double vmax, double amax, double wmax, double alphamax, int points) {
		this.path = path;
		this.vmax = vmax;
		this.amax = amax;
		this.wmax = wmax;
		this.alphamax = alphamax;
		velocities = new double[points];
		velocities[0] = v0;
		velocities[points-1] = v1;
		computeForwardTrajectory();
		computeReverseTrajectory();
	}
	
	/**
	 * Numerically computes the optimal velocity value at each point on the trajectory
	 */
	private void computeForwardTrajectory() {
		for(int i=1; i<velocities.length-1; i++) {
			double vprev = velocities[i-1];
			double vmax = getVmax(i);
			double amax = getAmax(i, vprev);
			if(vprev + amax < vmax) {
				velocities[i] = vprev + amax;
			} else {
				velocities[i] = vmax;
			}
		}
	}
	
	/**
	 * Computes the trajectory in reverse (call this AFTER computeForwardTrajectory)
	 * Overwrites any infeasible velocity values that were computed by the forward trajectory
	 */
	private void computeReverseTrajectory() {
		for(int i=velocities.length-2; i>0; i--) {
			double vprev = velocities[i+1];
			double vmax = getVmax(i);
			double amax = getAmax(i, vprev);
			if(vprev + amax < vmax) {
				velocities[i] = Math.min(velocities[i], vprev + amax);
			} else {
				velocities[i] = Math.min(velocities[i], vmax);
			}
		}
	}

	/**
	 * Gets the maximum velocity allowed at a given point
	 * @param i - the point being evaluated
	 * @return the maximum allowed velocity
	 */
	private double getVmax(int i) {
		// TODO: Not yet implemented
		return 0;
	}
	
	/**
	 * Gets the maximum acceleration allowed at a given point and velocity
	 * @param i - the point being evaluated
	 * @param v - the current velocity
	 * @return the maximum allowed acceleration
	 */
	private double getAmax(int i, double v) {
		// TODO: Not yet implemented
		return 0;
	}
	
	/**
	 * Gets the target velocity for a given point
	 * @param i - the index of the point
	 * @return the target velocity
	 */
	public double getV(int i) {
		return velocities[i];
	}
	
	/**
	 * Gets the target angular velocity for a given point
	 * @param i - the index of the point
	 * @return the target angular velocity
	 */
	public double getW(int i) {
		// TODO: Not yet implemented
		return 0.0;
	}
}
