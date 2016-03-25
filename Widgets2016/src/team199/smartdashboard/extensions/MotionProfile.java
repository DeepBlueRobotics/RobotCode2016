package team199.smartdashboard.extensions;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.gui.elements.LinePlot;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 * A widget to facilitate tuning of motion profiling code.
 * 
 * This widget depends on certain network table values being used by the code. 
 * All angles are in degrees clockwise, all distances are in inches, and all 
 * velocities are in inches/second.
 * 
 * Values that code should read from network tables:
 * SmartDashboard/MotionProfile/dX - Target change in x for test command
 * SmartDashboard/MotionProfile/dY - Target change in y for test command
 * SmartDashboard/MotionProfile/dTheta - Target change in theta for test command
 * SmartDashboard/MotionProfile/kA - Linear acceleration constant
 * SmartDashboard/MotionProfile/kAlpha - Angular acceleration constant
 * SmartDashboard/MotionProfile/MaxV - Maximum linear velocity
 * SmartDashboard/MotionProfile/MaxA - Maximum linear acceleration
 * SmartDashboard/MotionProfile/MaxW - Maximum angular velocity
 * SmartDashboard/MotionProfile/MaxAlpha - Maximum angular acceleration
 * 
 * Values that code should write to network tables:
 * SmartDashboard/MotionProfile/L - current average encoder distance
 * SmartDashboard/MotionProfile/V - current linear velocity
 * SmartDashboard/MotionProfile/A - current linear acceleration
 * SmartDashboard/MotionProfile/Theta - current gyro angle
 * SmartDashboard/MotionProfile/W - current angular velocity
 * SmartDashboard/MotionProfile/Alpha - current angular acceleration
 * SmartDashboard/MotionProfile/Start - set to true when starting a motion profile command
 * SmartDashboard/Drivetrain/LeftEncoder - current left encoder distance
 * SmartDashboard/Drivetrain/RightEncoder - current right encoder distance
 * SmartDashboard/Drivetrain/GyroAngle - current gyro angle
 * 
 * Commands code should put in network tables:
 * SmartDashboard/MotionProfile/TestMotionProfiling - A command to follow the given trajectory
 * 
 * Preferences that code should read:
 * DrivekA - Linear acceleration constant
 * DrivekAlpha - Angular acceleration constant
 * DriveMaxV - Maximum linear velocity
 * DriveMaxA - Maximum linear acceleration
 * DriveMaxW - Maximum angular velocity
 * DriveMaxAlpha - Maximum angular acceleration
 */
public class MotionProfile extends StaticWidget {

    // Name of widget, used by SmartDashboard
    public static final String NAME = "Motion Profile";
    // Background color of widget
    private final Color deepblue = new Color(0, 1, 99);
    // Networktable used for storing values
    private final NetworkTable table = NetworkTable.getTable("SmartDashboard/MotionProfile");
    // Table used for saving preferences
    private ITable prefs;
    // Panels used for controlling widget layout
    private JPanel graphPanel, numberPanel, graph;
    private PathPanel pathPanel;
    // Number boxes for displaying data
    private Box dx, dy, dtheta, vmax, amax, kA, wmax, alphamax, kAlpha;
    // Buttons for drawing a path, starting a test command, and saving prefs
    private JButton trace, start, save;
    // Combo box for selecting which graph to view
    private JComboBox chooser;
    // Current specified path to follow
    private Path path = new Path(0,0,0,0,0,0,0);
    // Graphs for displaying data
    private final LinePlot[] graphs = new LinePlot[6];
    // Names of the graphs
    private final String[] graphNames = {"Distance", "Velocity", "Acceleration",
        "Angle", "Angular Velocity", "Angular Acceleration"};
    private final double TIMESTEP = 0.01, SCALE = 2; // Pixels per inch
    
    @Override
    public void init() {
        // Get the preferences table
        try {
            prefs = Robot.getPreferences();
        } catch(Exception e) {
            System.out.println("Preferences not found");
        }
        setPreferredSize(new Dimension(705, 380));
        // Set blue background (widgets are transparent by default)
        setOpaque(true);
        setBackground(deepblue);
        // Set the layout
        FlowLayout f = new FlowLayout();
        f.setHgap(10);
        f.setVgap(10);
        setLayout(f);
        // Initialize each panel
        initPathPanel();
        initGraphPanel();
        initNumberPanel();
        readPrefs();
    }

    @Override
    public void propertyChanged(Property prprt) {
        // Widget does not have any properties
    }
    
    /**
     * The path panel displays the shape of the calculated path, and draws the 
     * trajectory along the path in real time
     */
    private void initPathPanel() {
        pathPanel = new PathPanel();
        pathPanel.setBackground(deepblue);
        pathPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                int x = me.getX();
                int y = me.getY();
                dx.setText((x-pathPanel.getWidth()/2)/SCALE+"");
                dy.setText((pathPanel.getHeight()/2-y)/SCALE+"");
            }
        });
        add(pathPanel);
    }
    
    /**
     * The graph panel displays graphs of variables that can be toggled between 
     * using a combo box
     */
    private void initGraphPanel() {
        graphPanel = new JPanel();
        graphPanel.setBackground(deepblue);
        graphPanel.setLayout(new BorderLayout());
        // Graph holds the actual graphs
        graph = new JPanel();
        // Card layout stores graphs on top of each other
        graph.setLayout(new CardLayout());
        graphPanel.add(graph, BorderLayout.CENTER);
        chooser = new JComboBox();
        // Add graph names to the combo box
        for(String name:graphNames) {
            chooser.addItem(name);
        }
        graphPanel.add(chooser, BorderLayout.SOUTH);
        add(graphPanel);
        // Initialize the graph panel
        resetGraphs();
        // Change the current graph when an option is selected on the combobox
        chooser.addActionListener((ActionEvent e) -> {
            String value = chooser.getSelectedItem()+"";
            for(int i=0; i<graphNames.length; i++) {
                if(graphNames[i].equals(value)) {
                    graphs[i].setVisible(true);
                } else {
                    graphs[i].setVisible(false);
                }
            }
        });
        // Update graphs with SmartDashboard data
        table.addTableListener((ITable t, String key, Object value, boolean b) -> {
            if(key.equals("L")) graphs[0].setValue(value);
            if(key.equals("V")) graphs[1].setValue(value);
            if(key.equals("A")) graphs[2].setValue(value);
            if(key.equals("Theta")) graphs[3].setValue(value);
            if(key.equals("W")) graphs[4].setValue(value);
            if(key.equals("Alpha")) graphs[5].setValue(value);
        });
    }
    
    /**
     * The number panel contains text boxes for setting values and buttons 
     * for drawing a path, starting a test command, and saving preferences
     */
    private void initNumberPanel() {
        numberPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 50);
            }
        };
        numberPanel.setBackground(deepblue);
        numberPanel.setLayout(new GridLayout(2,6));
        // Initialize components
        dx = new Box("dX");
        dy = new Box("dY");
        dtheta = new Box("dTheta");
        vmax = new Box("MaxV");
        amax = new Box("MaxA");
        kA = new Box("kA");
        trace = new JButton("Trace");
        start = new JButton("Start");
        save = new JButton("Save");
        wmax = new Box("MaxW");
        alphamax = new Box("MaxAlpha");
        kAlpha = new Box("kAlpha");
        // Add components to the panel
        numberPanel.add(dx);
        numberPanel.add(dy);
        numberPanel.add(dtheta);
        numberPanel.add(vmax);
        numberPanel.add(amax);
        numberPanel.add(kA);
        numberPanel.add(trace);
        numberPanel.add(start);
        numberPanel.add(save);
        numberPanel.add(wmax);
        numberPanel.add(alphamax);
        numberPanel.add(kAlpha);
        // Add listeners to buttons
        trace.addActionListener((ActionEvent e) -> {trace();});
        start.addActionListener((ActionEvent e) -> {start();});
        save.addActionListener((ActionEvent e) -> {save();});
        table.addTableListener((ITable t, String key, Object v, boolean b) -> {
            if (key.equals("Start")&&v.equals("true")) {
                start();
                table.putBoolean("Start", false);
            }
        });
        add(numberPanel);
    }
    
    /**
     * Draws a trajectory on the path panel
     */
    public void trace() {
        path = getPath();
        pathPanel.simulate();
    }
    
    /**
     * Starts or cancels a test command
     */
    public void start() {
        if(table.containsSubTable("TestMotionProfiling")) {
            ITable command = table.getSubTable("TestMotionProfiling");
            boolean wasRunning = command.getBoolean("running", false);
            command.putBoolean("running", !wasRunning);
            if(!wasRunning){
                // Start command
                start.setText("Cancel");
                resetGraphs();
                pathPanel.startTracking();
            } else {
                // Cancel command
                start.setText("Start");
            }
        }
    }
    
    /**
     * Saves current values to preferences
     */
    public void save() {
        // Write values to preferences
        vmax.save();
        amax.save();
        kA.save();
        wmax.save();
        alphamax.save();
        kAlpha.save();
        // Tell RoboRIO to save preferences
        Robot.getPreferences().putBoolean(Robot.PREF_SAVE_FIELD, true);
    }
    
    /**
     * Reinitializes all of the graphs
     */
    public void resetGraphs() {
        graph.removeAll();
        for(int i=0; i<graphs.length; i++) {
            graphs[i] = new LinePlot();
            // Entire panel is 300x300 pixels
            // Actual height may be incorrect, so use preferred height instead
            graphs[i].setPreferredSize(new Dimension(300, 300-chooser.getPreferredSize().height));
            graphs[i].setFieldName(graphNames[i]);
            graphs[i].init();
            graphs[i].setVisible(false);
            graph.add(graphs[i]);
        }
        graphs[chooser.getSelectedIndex()].setVisible(true);
    }
    
    /**
     * Pulls initial preference values to SmartDashboard
     */
    public void readPrefs() {
        table.putNumber("kA", prefs.getNumber("DrivekA", 0.0));
        table.putNumber("kAlpha", prefs.getNumber("DrivekAlpha", 0.0));
        table.putNumber("MaxV", prefs.getNumber("DriveMaxV", 144));
        table.putNumber("MaxA", prefs.getNumber("DriveMaxA", 144));
        table.putNumber("MaxW", prefs.getNumber("DriveMaxW", 360));
        table.putNumber("MaxAlpha", prefs.getNumber("DriveMaxAlpha", 360));
    }
    
    /**
     * Determines the specified path based on user inputs
     * @return The specified path
     */
    public Path getPath() {
        double deltaX = 0, deltaY = 0, deltaTheta = 0;
        try {
            deltaX = Double.parseDouble(dx.getText());
            deltaY = Double.parseDouble(dy.getText());
            deltaTheta = Double.parseDouble(dtheta.getText());
        } catch(Exception e) {
            // Bad input defaults to zero
        }
        return new Path(0, 0, deltaX, deltaY, 0, deltaTheta, 2);
    }
    
    /**
     * A labeled text box for storing numbers that is synchronized with 
     * SmartDashboard and can write to preferences.
     */
    private class Box extends JComponent {
        
        public final String name;
        private final JLabel label;
        private final JTextField field;
        
        /**
         * Creates a new Box
         * @param name - the label text and SmartDashboard key
         */
        public Box(String name) {
            super();
            this.name = name;
            // Make the displayed name look nicer
            label = new JLabel(name.replace("Alpha", "α").replace("Theta", "θ"));
            field = new JTextField();
            setLayout(new GridLayout(1,2));
            get();
            // White shows up better against blue
            label.setForeground(Color.WHITE);
            add(label);
            add(field);
            // Update SmartDashboard if user enters a value
            field.addFocusListener(new FocusAdapter(){
                @Override
                public void focusLost(FocusEvent e) {
                    set();
                }
            });
            // Update textbox if SmartDashboard value changes
            table.addTableListener((ITable t, String key, Object v, boolean b) -> {
                if (key.equals(name)) get();
            });
        }
        
        public String getText() {
            return field.getText();
        }
        
        public void setText(String text) {
            field.setText(text);
        }
        
        /**
         * Gets value from SmartDashboard
         */
        public void get() {
            field.setText(table.getNumber(name, 0)+"");
        }
        
        /**
         * Writes value to SmartDashboard
         */
        public void set() {
            try {
                table.putNumber(name, Double.parseDouble(getText()));
            } catch (Exception e) {
                // Ignore bad input
            }
        }
        
        /**
         * Writes value to preferences
         */
        public void save() {
            try {
                // Preferences are all prefixed with "Drive" (e.g. DriveMaxA)
                prefs.putNumber("Drive"+name, Double.parseDouble(getText()));
            } catch (Exception e) {
                // Ignore bad input
            }
        }
    }

    /**
     * A panel displaying progress along a trajectory
     */
    private class PathPanel extends JPanel {
        
        Trajectory trajectory;
        Timer tim = null;
        ArrayList<Double> xarray = new ArrayList<>();
        ArrayList<Double> yarray = new ArrayList<>();
        double l, theta, x, y;
        
        @Override
        public void paint(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            // Draw path
            g.setColor(deepblue);
            for(double s = 0; s<=1; s+=.002) {
                double x1 = path.getX(s)*SCALE+getWidth()/2;
                double y1 = getHeight()/2-path.getY(s)*SCALE;
                g.fillRect((int)x1-1, (int)y1-1, 3, 3);				
            }
            // Draw trajectory
            g.setColor(Color.RED);
            for(int i=0; i<xarray.size(); i++) {
                double x1 = xarray.get(i);
                double y1 = yarray.get(i);
                double x2 = x1*SCALE+getWidth()/2;
                double y2 = getHeight()/2-y1*SCALE;
                g.fillRect((int)x2-1, (int)y2-1, 3, 3);
            }
        }
        
        /**
         * Starts a simulation of the specified trajectory
         */
        public void simulate() {
            double vm = Double.parseDouble(vmax.getText());
            double am = Double.parseDouble(amax.getText());
            double wm = Double.parseDouble(wmax.getText());
            double alpham = Double.parseDouble(alphamax.getText());
            // Initial and final velocity of 1 in/s because simulation has no acceleration term
            trajectory = new Trajectory(path, 1, 1, vm, am, wm, alpham, 1000);
            xarray.clear();
            yarray.clear();
            l = 0;
            theta = path.getTheta(0);
            x = path.getX(0);
            y = path.getY(0);
            if(tim!=null) tim.stop();
            tim = new Timer((int)(TIMESTEP*1000), (ActionEvent e) -> {
                if (path.getS(l) < 1.0) {
                    updateTrajectory();
                } else {
                    tim.stop();
                }
            });
            tim.start();
        }
        
        /**
         * Starts tracking the robot's actual trajectory
         */
        public void startTracking() {
            xarray.clear();
            yarray.clear();
            // l is the distance traveled by the robot in inches
            l = getDistance();
            // theta is the initial gyro value in degrees
            theta = getAngle();
            x = path.getX(0);
            y = path.getY(0);
            if(tim!=null) tim.stop();
            tim = new Timer((int)(TIMESTEP*1000), (ActionEvent e) -> {
                if (path.getS(l) < 1.0) {
                    trackTrajectory();
                } else {
                    tim.stop();
                }
            });
            tim.start();
        }
        
        /**
         * Finds the next point along the trajectory
         */
        public void updateTrajectory() {
            int i = trajectory.getCurrentIndex(l);
            double v = trajectory.getV(i);
            l += v * TIMESTEP;
            double w = trajectory.getW(i);
            theta += w * TIMESTEP;
            x += (v * Math.sin((Math.toRadians(theta)))) * TIMESTEP;
            y += (v * Math.cos((Math.toRadians(theta)))) * TIMESTEP;
            xarray.add(x);
            yarray.add(y);
            repaint();
        }
        
        /**
         * Plot the robot's current location
         */
        public void trackTrajectory() {
            // Change in arc length since last time this method was called
            double dl = getDistance()-l;
            if(dl==0) return;
            l +=dl;
            // Angle in degrees relative to initial angle
            double relativeTheta = getAngle()-theta;
            x += dl * Math.sin((Math.toRadians(relativeTheta)));
            y += dl * Math.cos((Math.toRadians(relativeTheta)));
            xarray.add(x);
            yarray.add(y);
            repaint();
        }
        
        @Override
        public Dimension getPreferredSize() {
            // Panel is 300x300 pixels
            return new Dimension(300, 300);
        }
        
        private double getDistance() {
            NetworkTable driveTable = NetworkTable.getTable("SmartDashboard/Drivetrain");
            return (driveTable.getNumber("LeftEncoder", 0)+driveTable.getNumber("RightEncoder", 0))/2;
        }
        
        private double getAngle() {
            NetworkTable driveTable = NetworkTable.getTable("SmartDashboard/Drivetrain");
            return driveTable.getNumber("GyroAngle", 0);
        }
    }
}