/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import carrental.*;
import java.text.DateFormat;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ansy
 */
public class RentalsTableModel extends AbstractTableModel/* implements TableModelListener*/ {

    private RentalsManager manager;
    private List<Rental> rentals;
    private String[] columnNames = new String[7];    

    public RentalsTableModel(RentalsManager manager,CarsTableModel carsModel,DriversTableModel driversModel) {
        this.manager = manager;
        rentals = manager.findAllRentals();  
        for(int i=0;i<rentals.size();i++){
            int carIndex= carsModel.findCarIndex(rentals.get(i).getCar().getId());
            int driverIndex =driversModel.findDriverIndex(rentals.get(i).getDriver().getId());
            rentals.get(i).setCar(carsModel.getCar(carIndex));
            rentals.get(i).setDriver(driversModel.getDriver(driverIndex));            
        }

    }

    @Override
    public int getRowCount() {
        return rentals.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Rental rental = rentals.get(rowIndex);
        DateFormat formating = DateFormat.getDateInstance(); //new SimpleDateFormat("dd/MM/yyyy");

        switch (columnIndex) {
            case 0:
                return rental.getId();
            case 1:
                return rental.getDriver();
            case 2:
                return rental.getCar();
            case 3:
                return rental.getPrice();
            case 4:

                return formating.format(rental.getStartTime().getTime());
            case 5:
                return formating.format(rental.getExpectedEndTime().getTime());
            case 6:
                return (rental.getEndTime() != null) ? formating.format(rental.getEndTime().getTime()) : null;
            default:
                throw new IllegalArgumentException("columnIndex");
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {     
            return false;       
    }

    
/*
    @Override
    public void tableChanged(TableModelEvent e) {
        this.loadData();        
    }*/

    public Rental removeRow(javax.swing.JTable table) {
        Rental rental = null;
        int row = table.getSelectedRow();
        if (row != -1) {
            int view = row;
            row = table.getRowSorter().convertRowIndexToModel(row);

            rental = getRental(row);
            manager.deleteRental(rental);
            rentals = manager.findAllRentals();
            fireTableRowsDeleted(row, row);

            if (table.getRowSorter().getViewRowCount() > view) {
                table.setRowSelectionInterval(view, view);
            } else {
                table.setRowSelectionInterval(view - 1, view - 1);
            }
        }
        return rental;
    }

    public Rental getRental(int index) {
        return rentals.get(index);

    }


    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public void setColumnNames(ResourceBundle translator) {
        columnNames[0] = translator.getString("rentals.id");
        columnNames[1] = translator.getString("rentals.driver");
        columnNames[2] = translator.getString("rentals.car");
        columnNames[3] = translator.getString("rentals.price");
        columnNames[4] = translator.getString("rentals.startTime");
        columnNames[5] = translator.getString("rentals.expectedEndTime");
        columnNames[6] = translator.getString("rentals.endTime");
    }    

    public void addRental(Rental rental) {
        rentals.add(rental);

    }

    public void removeRental(Rental rental) {
        rentals.remove(rental);

    }
    public int findRentalIndex(Long id) {
        for (int i = 0; i < rentals.size(); i++) {
            if (rentals.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;

    }

    public Rental getSelectedRental(javax.swing.JTable table) {
        Rental rental = null;
        int row = table.getSelectedRow();
        if (row != -1) {
            row = table.getRowSorter().convertRowIndexToModel(row);

            rental = rentals.get(row);
        }
        return rental;
    }

}