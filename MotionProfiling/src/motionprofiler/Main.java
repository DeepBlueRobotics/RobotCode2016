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
		p = new Path(25,25,25,25,0,0,2);
		JFrame frame = new JFrame("Pathfinder");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 600);
		JTextField f1 = new JTextField("0");
		JTextField f2 = new JTextField("0");
		JTextField f3 = new JTextField("2");
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
