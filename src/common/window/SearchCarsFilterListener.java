/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.window;

import common.CarsTableModel;
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
public class SearchCarsFilterListener implements DocumentListener {

    private MainWindow app;
    private TableRowSorter<CarsTableModel> carsSorter;
    private RowFilter<CarsTableModel, Integer> carsFilter;

    public SearchCarsFilterListener(MainWindow app) {
        this.app = app;
        carsSorter = app.getCarsSorter();
        carsFilter = app.getCarsFilter();
    }

    protected void changeFilter(DocumentEvent event) {
        Document document = event.getDocument();
        try {
            String filterStr = document.getText(0, document.getLength());
             app.setFilterCars(filterStr); 
            carsSorter.setRowFilter(carsFilter);
            //  firePropertyChange("filterString", oldFilterString, filterStr);
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