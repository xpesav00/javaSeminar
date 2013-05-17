/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import carrental.Car;
import carrental.Driver;
import carrental.DriversManager;
import common.window.FailWindow;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ansy
 */
public class DriversTableModel extends AbstractTableModel {

    private DriversManager manager;
    private List<Driver> drivers;
    private String[] columnNames = new String[4];
    private JFrame location;

    public DriversTableModel(DriversManager manager, JFrame location) {
        this.manager = manager;
        drivers = manager.findAllDrivers();
        this.location = location;
    }

    @Override
    public int getRowCount() {
        return drivers.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Driver driver = drivers.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return driver.getId();
            case 1:
                return driver.getSurname();
            case 2:
                return driver.getName();
            case 3:
                return driver.getLicenseId();
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
        Driver driver = new Driver(drivers.get(rowIndex));
        String s = (String) aValue;


        if (s.equals("")) {
            return;
        }

        switch (columnIndex) {
            case 1:
                if (driver.getSurname().equals(s)) {
                    return;
                }
                driver.setSurname(s);
                break;
            case 2:
                if (driver.getName().equals(s)) {
                    return;
                }
                driver.setName(s);
                break;
            case 3:
                if (driver.getLicenseId().equals(s)) {
                    return;
                }
                driver.setLicenseId(s);
                break;

        }
        UpdateDriverSwingWorker update = new UpdateDriverSwingWorker(driver);
        update.execute();


    }

  

    public Driver getDriver(int index) {
        return drivers.get(index);

    }

 
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public void setColumnNames(ResourceBundle translator) {
        columnNames[0] = translator.getString("cars.id");
        columnNames[1] = translator.getString("drivers.surname");
        columnNames[2] = translator.getString("drivers.name");
        columnNames[3] = translator.getString("drivers.licenseId");
    }

    public int findDriverIndex(Long id) {
        for (int i = 0; i < drivers.size(); i++) {
            if (drivers.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;

    }

    public void addDriver(Driver car) {
        drivers.add(car);

    }

    public void removeDriver(Driver car) {
        drivers.remove(car);

    }

    public Driver getSelectedDriver(javax.swing.JTable table) {
        Driver driver = null;
        int row = table.getSelectedRow();
        if (row != -1) {
            row = table.getRowSorter().convertRowIndexToModel(row);

            driver = drivers.get(row);
        }
        return driver;
    }

    private class UpdateDriverSwingWorker extends SwingWorker<Void, Void> {

        private Driver driver;
        private boolean test;

        public UpdateDriverSwingWorker(Driver driver) {
            this.driver = driver;
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                manager.updateDriver(driver);
                test = true;
            } catch (Exception x) {
                test = false;
            }
            return null;
        }

        @Override
        protected void done() {
            int index = findDriverIndex(driver.getId());
            if (test && index != -1) {
                drivers.set(index, driver);
                fireTableRowsUpdated(index, index);
            } else {
                setDialog("fail.updateDriver");

            }
        }

        private void setDialog(String key) {
            FailWindow dialog = new FailWindow(MainWindow.getTranslator().getString(key));
            dialog.setLocationRelativeTo(location);
            dialog.setTitle(driver.toString());
            dialog.setVisible(true);
        }
    }
}