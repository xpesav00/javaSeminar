/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import carrental.Driver;
import carrental.DriversManager;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ansy
 */
public class DriversTableModel extends AbstractTableModel implements TableModelListener {

    private DriversManager manager;
    private List<Driver> drivers;
     private String[] columnNames = new String[4];

    public DriversTableModel(DriversManager manager) {
        this.manager = manager;
        drivers = manager.findAllDrivers();
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
        Long id = Long.parseLong(getValueAt(rowIndex, 0).toString());
        Driver driver = drivers.get(rowIndex);
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
        drivers.set(rowIndex, driver);
       UpdateDriverSwingWorker update = new UpdateDriverSwingWorker(driver);
        update.execute();

    }

    @Override
    public void tableChanged(TableModelEvent e) {
        this.loadData();
    }

    public Driver removeRow(javax.swing.JTable table) {
        Driver driver = null;
        int row = table.getSelectedRow();
        if (row != -1) {
            int view = row;
            row = table.getRowSorter().convertRowIndexToModel(row);
            driver = getDriver(row);            
                   
            
            manager.deleteDriver(driver);
            drivers = manager.findAllDrivers();            
            fireTableRowsDeleted(row, row);

            if (table.getRowSorter().getViewRowCount() > view) {
                table.setRowSelectionInterval(view, view);
            } else {
                table.setRowSelectionInterval(view - 1, view - 1);
            }
        }
        return driver;
    }
          
    public Driver getDriver(int index) {
        return drivers.get(index);

    }

    private void loadData() {
        this.drivers = manager.findAllDrivers();
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

    private class UpdateDriverSwingWorker extends SwingWorker<Void, Void> {
        Driver driver;
        public UpdateDriverSwingWorker(Driver driver){
            this.driver = driver;
        }

        @Override
        protected Void doInBackground() throws Exception {
            manager.updateDriver(driver);
            return null;          
        }
    }
}