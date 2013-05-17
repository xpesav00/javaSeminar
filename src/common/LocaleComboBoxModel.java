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
import java.util.Locale;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author honzap
 */
public class LocaleComboBoxModel implements ComboBoxModel {

	private List<Locale> locales = new ArrayList<>();
	private Object selectedObject;
	
	public LocaleComboBoxModel() {
		this.loadData();
	}
/*	
Čeština
English
Deutsh*/
	private void loadData() {
		this.locales.add(new Locale("cs","CZ"));
		this.locales.add(new Locale("en","US"));
		this.locales.add(new Locale("de","DE"));
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
		return this.locales.size();
	}

	@Override
	public Object getElementAt(int index) {
		return this.locales.get(index);
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
