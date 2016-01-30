package team199.smartdashboard.extensions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.Property;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import team199.smartdashboard.extensions.Defenses;

/**
 *
 * @author adamstafford
 */
public class TestAutoModeWidget extends StaticWidget {

    public static final String NAME = "Test Auto Mode";
    private final Defenses[] defenses = Defenses.values();
    private final JComboBox defense0 = new JComboBox(defenses);
    private final JComboBox defense1 = new JComboBox(defenses);
    private final JComboBox defense2 = new JComboBox(defenses);
    private final JComboBox defense3 = new JComboBox(defenses);
    private final JPanel field;
    private final JComboBox startingPosition = new JComboBox(new Integer[]{1, 2, 3, 4});
    private ArrayList<Object> labels = new ArrayList<>();
    private ArrayList<Widget.EditorTextField> fields = new ArrayList<>();

    public TestAutoModeWidget() {
        this.field = new JPanel() {
            @Override
            public void paint(Graphics g) {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Image field1 = toolkit.getImage("Field.png");
                g.drawImage(field1, 0, 0, field);
                Image[] defenseImages = new Image[4];
                for (Defenses defense : defenses) {
                    
                }
            }
        };
    }

    @Override
    public void init() {
        defense0.setLocation(0, 200);
        defense1.setLocation(50, 200);
        defense2.setLocation(100, 200);
        defense3.setLocation(150, 200);

        class MyListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }

        }
    }

    @Override
    public void propertyChanged(Property prprt) {
        
    }
}
