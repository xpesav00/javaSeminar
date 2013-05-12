/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import carrental.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.SwingWorker;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ansy
 */
public class RentalsTableModel extends AbstractTableModel implements TableModelListener {

    private RentalsManager manager;
    private List<Rental> rentals;

    public RentalsTableModel(RentalsManager manager) {
        this.manager = manager;
        rentals = manager.findAllRentals();

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
        if (columnIndex != 3) {
            return false;
        }
        return true;

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Long id = Long.parseLong(getValueAt(rowIndex, 0).toString());
        Rental rental = rentals.get(rowIndex);
        String s = (String) aValue;

        if (s.equals("")) {
            return;
        }

        if (columnIndex == 3) {
            if (rental.getPrice().equals(new BigDecimal(s))) {
                return;
            }
            rental.setPrice(new BigDecimal(s));
        }
        rentals.set(rowIndex, rental);
        UpdateRentalSwingWorker update = new UpdateRentalSwingWorker(rental);
        update.execute();
        System.out.println(rental.getCar());
        System.out.println(rental.getDriver());
        System.out.println(rental.getId());
        System.out.println(rental.getPrice());
        System.out.println(rental.getStartTime());
        manager.updateRental(rental);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        this.loadData();
        //     int row = e.getFirstRow();
        //     int column = e.getColumn();
        // System.err.println("heeey");
        //    getValueAt(row, column);
    }

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

    private void loadData() {
        this.rentals = manager.findAllRentals();
    }
    
    private class UpdateRentalSwingWorker extends SwingWorker<Void, Void> {
        Rental rental;
        public UpdateRentalSwingWorker(Rental rental){
            this.rental = rental;
        }

        @Override
        protected Void doInBackground() throws Exception {
            manager.updateRental(rental);
            return null;          
        }
    }
}