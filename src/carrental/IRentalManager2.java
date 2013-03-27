/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package carrental;

import java.util.List;

/**
 *
 * @author Filip Krepinsky
 */
public interface IRentalManager2 {

    /**
     * saves rental in db
     * @param rental
     * @return this Rental with set id
     */
    public Rental createRental(Rental rental);

    /**
     * sets endTime in rental
     */
    public void endRental(Rental rental);

    /**
     * deletes rental from database(even history won't find this rental)
     */
    public void deleteRental(Rental rental);

    /**
     * updates rental
     */
    public void updateRental(Rental rental);

    /**
     * @param id
     * @return Rental identified by this id
     */
    public Rental findRentalById(Long id);

    /**
     * @return all active Rentals (of unreturned cars)
     */
    public List<Rental> findAllRentals();

    /**
     * @return all active Rentals of this driver
     */
    public List<Rental> findRentalsByDriver(Driver driver);

    /**
     * @param car
     * @return all Rentals in which this car was
     */
    public List<Rental> findCarHistory(Car car);

    /**
     * @param driver
     * @return all Rentals in which this driver was
     */
    public List<Rental> findDriverHistory(Driver driver);

    /**
     * @return all Rentals ever created
     */
    public List<Rental> findRentalsHistory();

    /**
     * @return list of rented cars
     */
    public List<Car> findAllRentedCars();

    /**
     * @return list of free cars
     */
    public List<Car> findAllCarsOnStock();

    /**
     * @return true when car is free, false otherwise
     */
    public boolean isCarFree(Car car);
}
