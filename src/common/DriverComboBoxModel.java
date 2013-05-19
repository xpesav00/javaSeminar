/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import carrental.Driver;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author honzap
 */
public class DriverComboBoxModel implements ComboBoxModel {

    private List<Driver> drivers = new ArrayList<>();
    private Object selectedObject;

    public DriverComboBoxModel(DriversTableModel model) {
		
            drivers = model.getDrivers();
    }

    @Override
    public void setSelectedItem(Object anItem) {
        this.selectedObject = anItem;
    }

    @Override
    public Object getSelectedItem() {
        return this.selectedObject;
    }

    @Override
    public int getSize() {
        return this.drivers.size();
    }

    @Override
    public Object getElementAt(int index) {
        return this.drivers.get(index);
    }
    
    @Override
    public void addListDataListener(ListDataListener l) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

  
}
