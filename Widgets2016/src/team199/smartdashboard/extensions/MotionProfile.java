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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A widget to facilitate tuning of motion profiling code
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
    private JPanel pathPanel, graphPanel, numberPanel, graph;
    // Number boxes for displaying data
    private Box dx, dy, dtheta, vmax, amax, kA, wmax, alphamax, kAlpha;
    // Buttons for drawing a path, starting a test command, and saving prefs
    private JButton trace, start, save;
    // Combo box for selecting which graph to view
    private JComboBox chooser;
    // Graphs for displaying data
    private final LinePlot[] graphs = new LinePlot[6];
    // Names of the graphs
    private final String[] graphNames = {"Distance", "Velocity", "Acceleration",
        "Angle", "Angular Velocity", "Angular Acceleration"};
    
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
        pathPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                // TODO: Not yet implemented
            }
            @Override
            public Dimension getPreferredSize() {
                // Panel is 300x300 pixels
                return new Dimension(300, 300);
            }
        };
        pathPanel.setBackground(deepblue);
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
        numberPanel = new JPanel();
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
        // The container panel ensures that the size is correct
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(numberPanel, BorderLayout.CENTER);
        add(container);
    }
    
    /**
     * Draws a trajectory on the path panel
     */
    public void trace() {
        // TODO: Not yet implemented
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
        table.putNumber("MaxV", prefs.getNumber("DriveMaxV", 0.0));
        table.putNumber("MaxA", prefs.getNumber("DriveMaxA", 0.0));
        table.putNumber("MaxW", prefs.getNumber("DriveMaxW", 0.0));
        table.putNumber("MaxAlpha", prefs.getNumber("DriveMaxAlpha", 0.0));
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
}