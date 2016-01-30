package team199.smartdashboard.extensions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.Property;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 *
 * @author adamstafford
 */
public class TestAutoModeWidget extends StaticWidget {

    public static final String NAME = "Test Auto Mode";
    private final Defenses[] defenses = Defenses.values();
    private final JComboBox Defense[] = new JComboBox[4];
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
                for (int i = 0;i < 4;i++) {
                    defenseImages[i] = toolkit.getImage("Image" + Defense[i].getSelectedItem() + ".png");
                    g.drawImage(defenseImages[i], i*100, 200, field);
                }
            }
        };
    }

    @Override
    public void init() {
        for (JComboBox DEFENSE : Defense) {
            DEFENSE = new JComboBox(defenses);
        }
        setPreferredSize(new Dimension(215, 300));
        Defense[0].setLocation(0, 200);
        Defense[1].setLocation(50, 200);
        Defense[2].setLocation(100, 200);
        Defense[3].setLocation(150, 200);
        
        }

    @Override
    public void propertyChanged(Property prprt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
