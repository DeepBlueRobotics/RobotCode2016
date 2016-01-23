package team199.smartdashboard.extensions;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * A widget that provides an easy way to view and modify preferences.
 * @author Paul
 */
public class PreferenceWidget extends StaticWidget {

    public static final String NAME = "Preference Viewer";
    private JComboBox keyBox = new JComboBox();
    private final JTextField valueField = new JTextField();
    private final ITable prefs = NetworkTable.getTable("Preferences");
    private ArrayList<Object> labels = new ArrayList<>();
    private ArrayList<Widget.EditorTextField> fields = new ArrayList<>();

    @Override
    public void init() {
        setPreferredSize(new Dimension(415, 40));
        keyBox.setPreferredSize(new Dimension(200, 25));
        valueField.setPreferredSize(new Dimension(200, 25));
        
        keyBox.addItem("Generate Preference");

        // If the user changes the JComboBox, update the value field
        keyBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(prefs.containsKey(keyBox.getSelectedItem()+"")){
                    valueField.setText(prefs.getValue(keyBox.getSelectedItem()+"")+"");
                }
            }
        });

        // If the user changes the JTextField, update the subtable
        valueField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(keyBox.getSelectedItem().equals("Generate Preference") && !valueField.getText().equals("") 
                        && !prefs.containsKey(valueField.getText())){
                    prefs.putString(valueField.getText(), "");
                } else {
                    prefs.putString(keyBox.getSelectedItem() + "", valueField.getText());
                }
            }
        });

        // If a key is added to the subtable, it is added to the JComboBox
        // If the value of the selected preference is changed, the JTextArea will be updated
        prefs.addTableListener(new ITableListener() {
            @Override
            public void valueChanged(ITable itable, String key, Object value, boolean isNew) {
                if (isNew && !key.equals("Generate Preference")) {
                    System.out.println("Added pref");
                    keyBox.addItem(key);
                    sort();
                }
                if (key.equals(keyBox.getSelectedItem())) {
                    valueField.setText(value + "");
                }
            }
        }, true);

        add(keyBox);
        add(valueField);
    }

    @Override
    public void propertyChanged(Property prprt) {
    }
    
    //Organizes the JComboBox display alphabetically
    public void sort() {
        System.out.println("sort");
        int size = keyBox.getItemCount() - 1;
        Object[] temp = new String[size];
        for(int i = 0; i < size; i++) {
            temp[i] = keyBox.getItemAt(i+1);
        }      
        Arrays.sort(temp);
        
        keyBox.removeAllItems();
        keyBox.addItem("Generate Preference");
        for(int i = 0; i < size; i++){
            keyBox.addItem(temp[i]);
        }
        
    }
}