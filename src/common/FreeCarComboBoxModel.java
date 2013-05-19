/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import carrental.Car;
import carrental.CarsManager;
import carrental.RentalsManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author honzap
 */
public class FreeCarComboBoxModel implements ComboBoxModel {

    private List<Car> cars = new ArrayList<>();
    private Object selectedObject;    

    public FreeCarComboBoxModel(RentalsManager manager, CarsTableModel model) {

        List<Car> temp = model.getCars(); 
        Iterator<Car> iter = temp.iterator();
        while (iter.hasNext()) {   
            Car tmp = iter.next();
                if (manager.isCarFree(tmp)) {
                    cars.add(tmp);                
                }
            }
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
        return this.cars.size();
    }

    @Override
    public Object getElementAt(int index) {
        return this.cars.get(index);
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
