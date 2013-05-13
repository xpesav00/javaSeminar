/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import carrental.Car;
import carrental.CarsManager;
import carrental.RentalsManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author honzap
 */
public class FreeCarComboBoxModel implements ComboBoxModel {

	List<Car> cars = new ArrayList<>();
	Object selectedObject;
	RentalsManager manager;
	
	public FreeCarComboBoxModel(RentalsManager manager) {
		this.manager = manager;
		this.loadData();
	}
	
	private void loadData() {
		this.cars = manager.findAllCarsOnStock();
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
	
	public void update()  {
		this.loadData();
	}
}
