/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import carrental.Driver;
import carrental.DriversManager;
import java.util.List;
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
        manager.updateDriver(driver);

    }

    @Override
    public void tableChanged(TableModelEvent e) {
        this.loadData();
        //     int row = e.getFirstRow();
        //     int column = e.getColumn();
        // System.err.println("heeey");
        //    getValueAt(row, column);
    }

    public Driver removeRow(javax.swing.JTable table) {
        Driver driver = null;
        int row = table.getSelectedRow();
        if (row != -1) {
            row = table.convertRowIndexToModel(row);
            Long id = Long.parseLong(getValueAt(row, 0).toString());
            driver = manager.findDriverById(id);
            manager.deleteDriver(driver);
            drivers = manager.findAllDrivers();
            row = table.convertRowIndexToView(row);
            fireTableRowsDeleted(row, row);

            if (getRowCount() > row) {
                table.setRowSelectionInterval(row, row);
            } else {
                table.setRowSelectionInterval(row - 1, row - 1);
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
}