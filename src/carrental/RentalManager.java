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
	public List<Driver> findDriversByCar(Car car){
		return new ArrayList<>();
	}
	
	@Override
	public List<Car> findCarsByDriver(Driver driver){
		return new ArrayList<>(); 
	}
	
	@Override
	public void rentCarToDriver(Car car, Driver driver){
	}
	
	@Override
	public void returnCar(Car car, Driver driver){
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
