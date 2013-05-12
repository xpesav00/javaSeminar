/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import carrental.Car;
import carrental.CarsManager;
import common.window.SetStatusBarSwingWorker;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author ansy
 */
public class CarsTableModel extends AbstractTableModel implements TableModelListener {

    private CarsManager manager;
    private List<Car> cars;

    public CarsTableModel(CarsManager manager) {
        this.manager = manager;
        cars = manager.findAllCars();
    }

    @Override
    public int getRowCount() {
        return cars.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Car car = cars.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return car.getId();
            case 1:
                return car.getVin();
            case 2:
                return car.getSpz();
            case 3:
                return car.getName();
            case 4:
                return car.getMileage();
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex != 0) {
            return true;
        }
        return false;

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Long id = Long.parseLong(getValueAt(rowIndex, 0).toString());
        Car car = cars.get(rowIndex);
        String s = (String) aValue;

        if (s.equals("")) {
            return;
        }

        switch (columnIndex) {
            case 1:
                if (car.getVin().equals(s)) {
                    return;
                }
                car.setVin(s);
                break;
            case 2:
                if (car.getSpz().equals(s)) {
                    return;
                }
                car.setSpz(s);
                break;
            case 3:
                if (car.getName().equals(s)) {
                    return;
                }
                car.setName(s);
                break;
            case 4:
                if (car.getMileage() == Double.parseDouble(s)) {
                    return;
                }
                car.setMileage(Double.parseDouble(s));
        }
        cars.set(rowIndex, car);
        UpdateCarSwingWorker update = new UpdateCarSwingWorker(car);
        update.execute();

    }

    @Override
    public void tableChanged(TableModelEvent e) {
        this.loadData();
        //     int row = e.getFirstRow();
        //     int column = e.getColumn();
        // System.err.println("heeey");
        //    getValueAt(row, column);
    }

    public Car removeRow(javax.swing.JTable table) {
        Car car = null;
        int row = table.getSelectedRow();

        if (row != -1) {
            int view = row;
            row = table.getRowSorter().convertRowIndexToModel(row);
           
            car = getCar(row);
            manager.deleteCar(car);
            cars = manager.findAllCars();
            fireTableRowsDeleted(row, row);

            if (table.getRowSorter().getViewRowCount() > view) {
                table.setRowSelectionInterval(view, view);

            } else {
                table.setRowSelectionInterval(view - 1, view - 1);
            }

        }
        return car;

    }

    public Car getCar(int index) {
        return cars.get(index);

    }

    private void loadData() {
        this.cars = manager.findAllCars();        
    }
    
    private class UpdateCarSwingWorker extends SwingWorker<Void, Void> {
        Car car;
        public UpdateCarSwingWorker(Car car){
            this.car = car;
        }

        @Override
        protected Void doInBackground() throws Exception {
            manager.updateCar(car);
            return null;
           
        }
    }
}