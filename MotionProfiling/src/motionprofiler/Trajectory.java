package motionprofiler;

/**
 * Generates velocity values for a set of points along a path.
 */
public class Trajectory {
	
	// The path used to generate the trajectory
	private final Path path;
	// The constraints on the trajectory
	private final double vmax, amax, wmax, alphamax;
	// The set of calculated target velocity values
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
		// point on the spline function
		double s = 1.0*i/velocities.length;
		// dtheta/dL (not with respect to t)
		double w = Math.abs(path.getW(s));
		// d^2theta/dL^2 (not with respect to t)
		double alpha = Math.abs(path.getAlpha(s));
		// velocity limit due to tradeoff between linear and angular velocity
		double v1 = vmax/(1.0+w*vmax/wmax);
		// velocity limit due to maximum angular acceleration
		double v2 = Math.sqrt(alphamax/alpha);
		// return the smallest velocity limit
		return Math.min(v1, v2);
	}
	
	/**
	 * Gets the maximum acceleration allowed at a given point and velocity
	 * @param i - the point being evaluated
	 * @param v - the current velocity
	 * @return the maximum allowed acceleration (accurate to 3 decimal places)
	 */
	private double getAmax(int i, double v) {
		// old point on the spline function
		double s0 = 1.0*(i-1)/velocities.length;
		// new point on the spline function
		double s1 = 1.0*i/velocities.length;
		// change in arc length between the two points
		double l = (s1-s0)*v;
		// initial dw/dL
		double w0 = Math.abs(path.getW(s0));
		// final dw/dL
		double w1 = Math.abs(path.getW(s1));
		// acceleration limit due to tradeoff between linear and angular acceleration
		for(double a = amax; a>0; a-=.001) {
			double sqrt = Math.sqrt(v*v+2*a*l);
			double alpha = (w1*a*sqrt-w0*a*v)/(-v+sqrt);
			if(a<amax-amax*alpha/alphamax) {
				return a;
			}
		}
		return 0.0;
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
		double s = 1.0*i/velocities.length;
		return path.getW(s)*velocities[i];
	}
	
	/**
	 * Determines the index of a point on the path
	 * @param distance - the distance traveled along the path
	 * @return the index of the current point
	 */
	public int getCurrentIndex(double distance) {
		return (int)(path.getS(distance)/velocities.length);
	}
}