/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Trida zajistuje manazera pro praci s objektem Rental
 * @package carrental
 * @file RentalManager.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */

package carrental;

import java.util.ArrayList;
import java.util.List;

public class RentalManager implements IRentalManager{
	
	public RentalManager() {
	}
	
	@Override
	public Rental createRental(Rental rental) {
		return new Rental();
	}
	
	@Override
	public void endRental(Rental rental) {
	}
	
	@Override
	public void deleteRental(Rental rental) {
	}
	
	@Override
	public void updateRental(Rental rental) {
	}
	
	@Override
	public Rental findRentalById(Long id) {
		return new Rental();
	}
	
	@Override
	public List<Rental> findAllRentals() {
		return new ArrayList();
	}
	
	@Override
	public Driver findDriverByCar(Car car) {
		return new Driver();
	}
	
	@Override
	public Car findCarByDriver(Driver driver) {
		return new Car();
	}
	
	@Override
	public List<Driver> findCarHistoryOfRental(Car car) {
		return new ArrayList();
	}
	
	@Override
	public List<Car> findDriverHistoryOfRental(Driver driver) {
		return new ArrayList();
	}
	
	@Override
	public List<Car> findAllRentCars(){
		return new ArrayList<>();
	}
	
	@Override
	public List<Car> findAllCarsOnStock(){
		return new ArrayList<>();
	}
	
	@Override
	public boolean isCarFree(Car car){
		return true;
	}	
}
