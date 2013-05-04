/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import carrental.Car;
import carrental.CarsManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
        manager.updateCar(car);

    }

    @Override
    public void tableChanged(TableModelEvent e) {
        //     int row = e.getFirstRow();
        //     int column = e.getColumn();
        System.err.println("heeey");
        //    getValueAt(row, column);
    }

    public void removeRow(int row, javax.swing.JTable table) {
        if (row != -1) {
            Long id = Long.parseLong(getValueAt(row, 0).toString());
            manager.deleteCar(manager.findCarById(id));
            cars = manager.findAllCars();
            fireTableRowsDeleted(row, row);
            if (getRowCount() > row) {
                table.setRowSelectionInterval(row, row);
            } else {
                table.setRowSelectionInterval(row -1, row -1);
            }

        }


    }
}