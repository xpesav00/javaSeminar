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
	public List<Driver> findDriversByCar(Car car);
	public List<Car> findCarsByDriver(Driver driver);
	public void rentCarToDriver(Car car, Driver driver);
	public void returnCar(Car car, Driver driver);
	public List<Car> findAllRentCars();
	public List<Car> findAllCarsOnStock();
	public boolean isCarFree(Car car);
}
