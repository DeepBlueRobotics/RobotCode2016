package team199.smartdashboard.extensions;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A widget to facilitate tuning of motion profiling code
 */
public class MotionProfile extends StaticWidget {

    public static final String NAME = "Motion Profile";
    private final String prefix = "MotionProfile";
    private final Color deepblue = new Color(0, 1, 99);
    private JPanel pathPanel, graphPanel, numberPanel;
    private final NetworkTable sd = NetworkTable.getTable("SmartDashboard/"+prefix);
    private ITable prefs;
    
    @Override
    public void init() {
        try {
            prefs = Robot.getPreferences();
        } catch(Exception e) {
            System.out.println("Preferences not found");
        }
        setPreferredSize(new Dimension(420, 270));
        setLayout(new FlowLayout());
        setBackground(deepblue);
        initPathPanel();
        initGraphPanel();
        initNumberPanel();
    }

    @Override
    public void propertyChanged(Property prprt) {
        
    }
    
    private void initPathPanel() {
        pathPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillRect(5, 5, getWidth()-10, getHeight()-10);
            }
        };
        pathPanel.setPreferredSize(new Dimension(getWidth()/2, 200));
        add(pathPanel);
    }
    
    private void initGraphPanel() {
        graphPanel = new JPanel();
        graphPanel.setPreferredSize(new Dimension(getWidth()/2, 200));
        add(graphPanel);
    }
    
    private void initNumberPanel() {
        numberPanel = new JPanel();
        numberPanel.setPreferredSize(new Dimension(getWidth(), 50));
        add(numberPanel);
        numberPanel.setLayout(new GridLayout(2,3));
        numberPanel.add(new Box("L"));
        numberPanel.add(new Box("Theta"));
        numberPanel.add(new Box("V"));
        numberPanel.add(new Box("W"));
    }
    
    private class Box extends JComponent {
        public final String name;
        private final JLabel label;
        private final JTextField field;
        public Box(String name) {
            super();
            this.name = name;
            label = new JLabel(name);
            field = new JTextField();
            setLayout(new GridLayout(1,2));
            get();
            add(label);
            add(field);
            field.addFocusListener(new FocusAdapter(){
                @Override
                public void focusLost(FocusEvent e) {
                    set();
                }
            });
            sd.addTableListener((ITable i, String key, Object v, boolean b) -> {
                if (key.equals(name)) get();
            });
        }
        public String getText() {
            return field.getText();
        }
        public void get() {
            field.setText(sd.getNumber(name, 0)+"");
        }
        public void set() {
            try {
                sd.putNumber(name, Double.parseDouble(getText()));
            } catch (Exception e) {
                // Do nothing
            }
        }
        public void save() {
            try {
                prefs.putNumber(name, Double.parseDouble(getText()));
            } catch (Exception e) {
                // Do nothing
            }
        }
    }
}
