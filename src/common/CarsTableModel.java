/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import carrental.Car;
import carrental.CarsManager;
import common.window.FailWindow;
import common.window.SetStatusBarSwingWorker;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author ansy
 */
public class CarsTableModel extends AbstractTableModel /* implements TableModelListener*/ {

    private CarsManager manager;
    private List<Car> cars = new ArrayList<>();
    private String[] columnNames = new String[5];
    private JFrame location;

    public CarsTableModel(CarsManager manager, JFrame location) {
        this.manager = manager;
        cars = manager.findAllCars();
        this.location = location;
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
        Car car = new Car(cars.get(rowIndex));
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
                try {
                    if (car.getMileage() == Double.valueOf(s)) {
                        return;
                    }
                    car.setMileage(Double.parseDouble(s));
                } catch (NumberFormatException ex) {
                    return;
                }
        }
        UpdateCarSwingWorker update = new UpdateCarSwingWorker(car);
        update.execute();
    }

   /* @Override
    public void tableChanged(TableModelEvent e) {
        //  this.loadData();
    }*/

    public Car getSelectedCar(javax.swing.JTable table) { 
        Car car = null;
        int row = table.getSelectedRow();
        if (row != -1) {
            row = table.getRowSorter().convertRowIndexToModel(row);

            car = cars.get(row);
        }
        return car;
    }


    public Car getCar(int index) {
        return cars.get(index);

    }

    public int findCarIndex(Long id) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;

    }

    public void addCar(Car car) {
        cars.add(car);

    }
    
    public void removeCar(Car car) {
        cars.remove(car);

    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public void setColumnNames() {
        ResourceBundle translator = MainWindow.getTranslator();
        columnNames[0] = translator.getString("cars.id");
        columnNames[1] = translator.getString("cars.vin");
        columnNames[2] = translator.getString("cars.licensePlate");
        columnNames[3] = translator.getString("cars.model");
        columnNames[4] = translator.getString("cars.mileage");
    }
    
    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
    

    private class UpdateCarSwingWorker extends SwingWorker<Void, Void> {

        private Car car;
        private boolean test;

        public UpdateCarSwingWorker(Car car) {
            this.car = car;
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                manager.updateCar(car);
                test = true;
            } catch (Exception x) {
                test = false;
            }
            return null;

        }

        @Override
        protected void done() {
            int index = findCarIndex(car.getId());
            if (test && index != -1) {
                Car actualCar =cars.get(index);
                actualCar.setMileage(car.getMileage());
                actualCar.setName(car.getName());
                actualCar.setSpz(car.getSpz());
                actualCar.setVin(car.getVin());
                fireTableRowsUpdated(index, index);
            } else {
                setDialog("fail.updateCar2");

            }
        }

        private void setDialog(String key) {
            FailWindow dialog = new FailWindow(MainWindow.getTranslator().getString(key));
            dialog.setLocationRelativeTo(location);
            dialog.setTitle(car.toString());
            dialog.setVisible(true);
        }
    }
}