/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team199.smartdashboard.extensions;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.Property;
import java.awt.Dimension;

/**
 *
 * @author Joshua
 */
public class AutoAssist extends  Widget {
     public static final String NAME = "AutoAssist";
    
    
    @Override
    public void init() {
        setPreferredSize(new Dimension(415,40));
    }

    @Override
    public void setValue(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void propertyChanged(Property prprt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}