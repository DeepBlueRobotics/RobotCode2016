package team199.smartdashboard.extensions;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * A widget that provides an easy way to view and modify preferences.
 */
public class NewClass extends StaticWidget {

    public static final String NAME = "Test Auto Mode 2";
    private final JComboBox keyBox = new JComboBox();
    private final JTextField valueField = new JTextField();
    private ITable prefs = NetworkTable.getTable("Preferences");
    private ArrayList<Object> labels = new ArrayList<>();
    private final JButton saveButton = new JButton("Save");
    private final JButton removeButton = new JButton("Remove");
    private final Defenses[] defenses = Defenses.values();
    private final JComboBox Defense[] = new JComboBox[4];
    private final JComboBox startingPosition = new JComboBox(new Integer[]{1, 2, 3, 4});
    private ArrayList<Widget.EditorTextField> fields = new ArrayList<>();

    @Override
    public void init() {
        try {
            prefs = Robot.getPreferences();
        } catch(Exception e) {
            prefs = NetworkTable.getTable("Preferences");
            System.out.println("Preferences not found");
        }
        setPreferredSize(new Dimension(215, 300));
        keyBox.setPreferredSize(new Dimension(200, 25));
        valueField.setPreferredSize(new Dimension(200, 25));
        saveButton.setPreferredSize(new Dimension(100, 25));
        removeButton.setPreferredSize(new Dimension(100, 25));
        Defense[0].setPreferredSize(new Dimension(200, 25));
        Defense[1].setPreferredSize(new Dimension(200, 25));
        Defense[2].setPreferredSize(new Dimension(100, 25));
        Defense[3].setPreferredSize(new Dimension(100, 25));
        keyBox.addItem("New Preference");
        add(keyBox);
        add(valueField);
        add(saveButton);
        add(removeButton);
        /*add(Defense[0]);
        add(Defense[1]);
        add(Defense[2]);
        add(Defense[3]);*/
        update();
        keyBox.addActionListener((ActionEvent e) -> {
            readValueOfCurrentKey();
        });
        keyBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                update();
            }
        });
        valueField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                writeValueOfCurrentKey();
            }
        });
        prefs.addTableListener((ITable itable, String key, Object value, boolean isNew) -> {
            readValueOfCurrentKey();
        }, true);
        saveButton.addActionListener((ActionEvent e) -> {
            Robot.getPreferences().putBoolean(Robot.PREF_SAVE_FIELD, true);
        });
        removeButton.addActionListener((ActionEvent e) -> {
            String key = keyBox.getSelectedItem() + "";
            prefs.delete(key);
            valueField.setText("");
            update();
        });
    }

    @Override
    public void propertyChanged(Property prprt) {
    }
    
    // Organizes the JComboBox to display alphabetically
    private void update() {
        String key = keyBox.getSelectedItem() + "";
        Object[] temp = prefs.getKeys().toArray();
        Arrays.sort(temp);
        keyBox.removeAllItems();
        keyBox.addItem("New Preference");
        for (Object o: temp) {
            keyBox.addItem(o);
        }
        keyBox.setSelectedItem(key);
        repaint();
    }

    // Sets the value field to the correct value
    private void readValueOfCurrentKey() {
        String key = keyBox.getSelectedItem() + "";
        if (prefs.containsKey(key)) {
            valueField.setText(prefs.getValue(key, "") + "");
        } else {
            valueField.setText("");
        }    
    }
    
    // Changes the value of the selected key in the preferences table
    private void writeValueOfCurrentKey() {
        String key = keyBox.getSelectedItem() + "";
        String value = valueField.getText();
        // Add key
        if (key.equals("New Preference") && !value.equals("") && !prefs.containsKey(value)) {
            prefs.putString(value, "");
            update();
        }
        // Change value
        if(!key.equals("New Preference")) {
            prefs.putString(key, value);
        }
    }
}