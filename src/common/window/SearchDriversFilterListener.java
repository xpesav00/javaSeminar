/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.window;

import common.CarsTableModel;
import common.DriversTableModel;
import common.MainWindow;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.text.Document;

/**
 *
 * @author ansy
 */
public class SearchDriversFilterListener implements DocumentListener {

    MainWindow app;
    TableRowSorter<DriversTableModel> driversSorter;
    RowFilter<DriversTableModel, Integer> driversFilter;

    public SearchDriversFilterListener(MainWindow app) {
        this.app = app;
        driversSorter = app.getDriversSorter();
        driversFilter = app.getDriversFilter();
    }

    protected void changeFilter(DocumentEvent event) {
        Document document = event.getDocument();
            try {
                String filterStr = document.getText(0, document.getLength());
                app.setFilterDrivers(filterStr);                 
                driversSorter.setRowFilter(driversFilter);              
            } catch (Exception ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    public void changedUpdate(DocumentEvent e) {
        changeFilter(e);
    }

    public void insertUpdate(DocumentEvent e) {
        changeFilter(e);
    }

    public void removeUpdate(DocumentEvent e) {
        changeFilter(e);
    }
}
