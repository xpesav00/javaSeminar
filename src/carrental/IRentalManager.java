/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Trida zajistuje rozhrani pro manazera autopujcovny
 * @package carrental
 * @file IRentalManager.java
 * @author Jan Pesava - xpesav00, Filip Krepinsky
 * @email xpesav00@mail.muni.cz, 410022@mail.muni.cz
 * @date 5. 3. 2013
 */
package carrental;

import java.util.List;

public interface IRentalManager {

    /**
     * Method creates rental object in database
     *
     * @param Rental rental is new created rental
     * @return object with new id
     */
    public Rental createRental(Rental rental);

    /**
     * Method ends current active rental
     *
     * @param Rental rental is still active rental
     */
    public void endRental(Rental rental);

    /**
     * Method delete rental from database
     *
     * @param Rental rental
     */
    public void deleteRental(Rental rental);

    /**
     * Method updates object in database
     *
     * @param Rental rental
     */
    public void updateRental(Rental rental);

    /**
     * Method find rental in database by id
     *
     * @param Long id
     * @return rental if success or null
     */
    public Rental findRentalById(Long id);

    /**
     * Method returns all rentals in database
     *
     * @return List<Rental>
     */
    public List<Rental> findAllRentals();

    /**
     * Method find all rental with this car
     *
     * @param Car car
     * @return List with rentals
     */
    public List<Rental> findHistoryOfRental(Car car);

    /**
     * Method find all rental with this driver
     *
     * @param Driver driver
     * @return List with rentals
     */
    public List<Rental> findHistoryOfRental(Driver driver);

    /**
     * Method returns all rented car at the moment
     *
     * @return List<Car>
     */
    public List<Car> findAllRentedCars();

    /**
     * Method return all cars to rent at the monent
     *
     * @return List<car>
     */
    public List<Car> findAllCarsOnStock();

    /**
     * Method control status of car
     *
     * @param Car car is control car
     * @return true if is free to rent
     */
    public boolean isCarFree(Car car);

    /**
     * @param List<Rental>
     * @return all active rentals
     */
    public  List<Rental> activeRentals(List<Rental> list);
}
