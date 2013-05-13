/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.window;

import common.*;
import common.DriversTableModel;
import common.MainWindow;
import common.RentalsTableModel;
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
public class SearchRentalsFilterListener implements DocumentListener {

    MainWindow app;
    TableRowSorter<RentalsTableModel> rentalsSorter;
    RowFilter<RentalsTableModel, Integer> rentalsFilter;

    public SearchRentalsFilterListener(MainWindow app) {
        this.app = app;
        rentalsSorter = app.getRentalsSorter();
       rentalsFilter = app.getRentalsFilter();
    }

    protected void changeFilter(DocumentEvent event) {
        Document document = event.getDocument();
            try {
                String filterStr = document.getText(0, document.getLength());
                app.setFilterRentals(filterStr);                 
                rentalsSorter.setRowFilter(rentalsFilter);              
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
