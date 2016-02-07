package motionprofiler;

public class Path {
	
	private final double Ax, Bx, Cx, Dx, Ay, By, Cy, Dy; 
	
	/**
	 * Creates a path from point 0 to point 1
	 * @param x0 - initial x
	 * @param y0 - initial y
	 * @param x1 - final x
	 * @param y1 - final y
	 * @param angle0 - initial angle (degrees clockwise from <0, 1>)
	 * @param angle1 - final angle
	 * @param k - Determines how sharply the curve turns (2 usually works well)
	 */
	public Path(double x0, double y0, double x1, double y1, double angle0, double angle1, double k) {
		double dx = x1-x0;
		double dy = y1-y0;
		double vx0 = Math.sin(Math.toRadians(angle0))*k*dx;
		double vy0 = Math.cos(Math.toRadians(angle0))*k*dy;
		double vx1 = Math.sin(Math.toRadians(angle1))*k*dx;
		double vy1 = Math.cos(Math.toRadians(angle1))*k*dy;
		Ax = vx1+vx0-2*dx;
		Bx = -vx1-2*vx0+3*dx;
		Cx = vx0;
		Dx = x0;
		Ay = vy1+vy0-2*dy;
		By = -vy1-2*vy0+3*dy;
		Cy = vy0;
		Dy = y0;
	}

	public double getX(double s) {
		return Ax*s*s*s+Bx*s*s+Cx*s+Dx;
	}

	public double getVx(double s) {
		return 3*Ax*s*s+2*Bx*s+Cx;
	}
	
	public double getY(double s) {
		return Ay*s*s*s+By*s*s+Cy*s+Dy;
	}

	public double getVy(double s) {
		return 3*Ay*s*s+2*By*s+Cy;
	}
}
