/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.window;

import carrental.Car;
import carrental.Driver;
import carrental.Rental;
import common.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author ansy
 */
public class DeleteSwingWorker extends SwingWorker<Void, Void> {

    int deleteChoice;
    CarsTableModel carsModel;
    DriversTableModel driversModel;
    RentalsTableModel rentalsModel;
    JTable jTableCars, jTableDrivers, jTableRentals;
    JDialog DeleteWindow;      
    JTextField statusBar;
    MainWindow app;

    public DeleteSwingWorker(MainWindow app) {

        this.app = app;
        deleteChoice = app.getDeleteChoice();
        carsModel = app.getCarsModel();
        driversModel = app.getDriversModel();
        rentalsModel = app.getRentalsModel();
        jTableCars = app.getjTableCars();
        jTableDrivers = app.getjTableDrivers();
        jTableRentals = app.getjTableRentals();
        DeleteWindow = app.getDeleteWindow();
        statusBar = app.getStatusBar();          

    }

    @Override
    protected Void doInBackground() throws Exception {
        Car car = null;
        Driver driver = null;
        Rental rental;
        String message = null;
        DeleteWindow.dispose();
        switch (deleteChoice) {
            case 0:
                car = carsModel.removeRow(jTableCars);            
                message = "Car : " + car + " was deleted";               
                break;
            case 1:
                driver = driversModel.removeRow(jTableDrivers);                  
                message = "Driver : " + driver + " was removed from db";
                break;
            case 2:
                rental = rentalsModel.removeRow(jTableRentals);
                message = "Rental : " + rental + " was deleted";
                break;
                          
        }
        
        

        SetStatusBarSwingWorker status = new SetStatusBarSwingWorker(statusBar, message);
        status.execute();
        return null;
        
    }
}
