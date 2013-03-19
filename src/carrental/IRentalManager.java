/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Trida zajistuje rozhrani pro manazera autopujcovny
 * @package carrental
 * @file IRentalManager.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */

package carrental;
import java.util.List;

public interface IRentalManager {
	public Rental createRental(Rental rental);
	public void endRental(Rental rental);
	public void deleteRental(Rental rental);
	public void updateRental(Rental rental);
	public Rental findRentalById(int id);
	public List<Rental> findAllRentals();
	public Driver findDriverByCar(Car car);
	public Car findCarByDriver(Driver driver);
	public List<Driver> findCarHistoryOfRental(Car car);
	public List<Car> findDriverHistoryOfRental(Driver driver);
	public List<Car> findAllRentCars();
	public List<Car> findAllCarsOnStock();
	public boolean isCarFree(Car car);	
}
