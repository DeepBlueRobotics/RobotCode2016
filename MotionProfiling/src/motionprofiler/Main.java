package motionprofiler;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main {
	
	static Path p;
	
	public static void main(String[] args) {
//		double amax = 13;
//		double alphamax = 27;
//		double w1 = 16;
//		double w0 = 37;
//		double l = 2;
//		double v = 34;
//		double a = (amax
//				* v
//				* (w1 - w0)
//				* Math.sqrt(8 * amax * amax * alphamax * w1 * l + amax * amax
//						* w1 * w1 * v * v + 2 * amax * amax * w1 * w0 * v * v
//						+ amax * amax * w0 * w0 * v * v + 8 * amax * alphamax
//						* alphamax * l + 4 * amax * alphamax * w1 * v * v + 4
//						* amax * alphamax * w0 * v * v + 4 * alphamax
//						* alphamax * v * v) - amax
//				* (-4 * amax * alphamax * w1 * l + amax * w1 * w1 * v * v
//						- amax * w0 * w0 * v * v - 4 * alphamax * alphamax * l
//						+ 2 * alphamax * w1 * v * v - 2 * alphamax * w0 * v * v))
//				/ (2 * l * (2 * amax * amax * w1 * w1 + 4 * amax * alphamax
//						* w1 + 2 * alphamax * alphamax));
//		System.out.println(a);
//		startPathDemo();
	}
	
	public static void startPathDemo() {
		p = new Path(25,25,25,25,0,0,2);
		JFrame frame = new JFrame("Pathfinder");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 600);
		JTextField f1 = new JTextField("0");
		JTextField f2 = new JTextField("0");
		JTextField f3 = new JTextField("2");
		@SuppressWarnings("serial")
		JPanel panel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				for(double s = 0; s<=1; s+=.002) {
					double x = p.getX(s)*10;
					double y = p.getY(s)*10;
					y = getHeight()-y;
					g.fillRect((int)x-1, (int)y-1, 3, 3);
				}
			}
		};
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				double x = me.getX()/10;
				double y = (panel.getHeight()-me.getY())/10;
				double angle0 = Double.parseDouble(f1.getText());
				double angle1 = Double.parseDouble(f2.getText());
				double k = Double.parseDouble(f3.getText());
				p = new Path(25,25,x,y,angle0,angle1,k);
				panel.repaint();
			}
		});
		panel.setPreferredSize(new Dimension(500, 500));
		f1.setPreferredSize(new Dimension(100, 25));
		f2.setPreferredSize(new Dimension(100, 25));
		f3.setPreferredSize(new Dimension(100, 25));
		frame.setLayout(new FlowLayout());
		frame.add(panel);
		frame.add(f1);
		frame.add(f2);
		frame.add(f3);
		frame.setVisible(true);
	}
	
}
/*
		First attempt:
		
		return (amax
		* v
		* (w1 - w0)
		* Math.sqrt(8 * amax * amax * alphamax * w1 * l + amax * amax
				* w1 * w1 * v * v + 2 * amax * amax * w1 * w0 * v * v
				+ amax * amax * w0 * w0 * v * v + 8 * amax * alphamax
				* alphamax * l + 4 * amax * alphamax * w1 * v * v + 4
				* amax * alphamax * w0 * v * v + 4 * alphamax
				* alphamax * v * v) - amax
		* (-4 * amax * alphamax * w1 * l + amax * w1 * w1 * v * v
				- amax * w0 * w0 * v * v - 4 * alphamax * alphamax * l
				+ 2 * alphamax * w1 * v * v - 2 * alphamax * w0 * v * v))
		/ (2 * l * (2 * amax * amax * w1 * w1 + 4 * amax * alphamax
				* w1 + 2 * alphamax * alphamax));
*/
