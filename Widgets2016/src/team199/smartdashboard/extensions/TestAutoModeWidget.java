package team199.smartdashboard.extensions;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.lang.Enum;

/**
 * A widget that provides an easy way to view and modify preferences.
 */
public class TestAutoModeWidget extends StaticWidget {

    public static final String NAME = "Test Auto Mode";
    private final JComboBox keyBox = new JComboBox();
    private final JTextField valueField = new JTextField();
    private ITable prefs = NetworkTable.getTable("Preferences");
    private ArrayList<Object> labels = new ArrayList<>();
    private final JButton saveButton = new JButton("Save");
    private final JButton removeButton = new JButton("Remove");
    private final Defenses[] defenses = Defenses.values();
    private final JComboBox Defense[] = new JComboBox[4];
    private final JComboBox startingPosition = new JComboBox(new Integer[]{2, 3, 4, 5});
    private ArrayList<Widget.EditorTextField> fields = new ArrayList<>();
    private JPanel field;
    private JPanel menus;
    ITable ntable = NetworkTable.getTable("SmartDashboard/Auto");
           
    @Override
    public void init() {
        /*try {
            prefs = Robot.getPreferences();
        } catch(Exception e) {
            prefs = NetworkTable.getTable("Preferences");
            System.out.println("Preferences not found");
        }*/
        this.setLayout(null);
        Defense[0] = new JComboBox(defenses);
        Defense[1] = new JComboBox(defenses);
        Defense[2] = new JComboBox(defenses);
        Defense[3] = new JComboBox(defenses);
        this.field = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.clearRect(0,0,274,465);
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Image field1 = null;
                try {
                    field1 = ImageIO.read(TestAutoModeWidget.class.getResource("Field.png"));
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    System.exit(1);
                }
                try {
                    g.drawImage(field1, 0, 0, null);
                } catch (Exception ex) {
                    System.out.println(ex.getCause());
                    System.exit(1);
                }
                Image[] defenseImages = new Image[4];
                System.out.println(Defense[0].getSelectedItem());
                Image robotImage = null;
                try {
                    robotImage = ImageIO.read(TestAutoModeWidget.class.getResource("RobotImage.png"));
                } catch (IOException ex) {
                    Logger.getLogger(TestAutoModeWidget.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (int i = 0;i < 4;i++) {
                    try {
                        defenseImages[i] = ImageIO.read(TestAutoModeWidget.class.getResource("Image" + (((Defenses)Defense[i].getSelectedItem()).ordinal()+1) + ".png"));
                    } catch (Exception ex) {
                        System.out.println(ex.getCause());
                        System.exit(1);
                    }
                    System.out.println(defenseImages[i]);
                    g.drawImage(defenseImages[i], 87, -i*70+105+3*70, 50, 50, null);
                    if (startingPosition.getSelectedIndex() == i)
                    {
                        g.drawImage(robotImage, 160, -i*70+105+3*70, 50, 50, null);
                        ntable.putNumber("Defense", ((Defenses)Defense[i].getSelectedItem()).ordinal());
                        ntable.putNumber("Position", i+2);
                    }
                }
                TestAutoModeWidget.this.repaint();
            }
        };
        final BufferedImage image = (new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB));
        setPreferredSize(new Dimension(800, 600));
        field.setSize(new Dimension(500,500));
        field.setLocation(50,25);
        Defense[0].setSize(new Dimension(175, 25));
        Defense[1].setSize(new Dimension(175, 25));
        Defense[2].setSize(new Dimension(175, 25));
        Defense[3].setSize(new Dimension(175, 25));
        for (int i = 0;i < 4;i++) {
            Defense[i].setLocation(350,3*72+135-72*i);
        }
        startingPosition.setSize(175, 25);
        startingPosition.setLocation(350,135-72);
        this.add(field);
        add(Defense[0]);
        add(Defense[1]);
        add(Defense[2]);
        add(Defense[3]);
        add(startingPosition);
        this.setVisible(true);
        update();
    }

    @Override
    public void propertyChanged(Property prprt) {
    }
    
    // Organizes the JComboBox to display alphabetically
    private void update() {
        repaint();
    }
}