/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Trida tvori rozhrani pro obhospodarovani auta
 * @package carrental
 * @file ICarManager.java
 * @author Jan Pesava - xpesav00, Filip Krepinsky
 * @email xpesav00@mail.muni.cz, 410022@mail.muni.cz
 * @date 5. 3. 2013
 */

package carrental;
import java.util.List;

public interface ICarManager {
	/**
	 * Method creates car in db
	 * @param car
	 * @return car with id
	 */
	public Car createCar(Car car);
	
	/**
	 * Metohod deletes car from db
	 * @param car 
	 */
	public void deleteCar(Car car);
	
	/**
	 * Method returns all cars from db
	 * @return List<Car>
	 */
	public List<Car> findAllCars();
	
	/**
	 * Method update car in db
	 * @param car 
	 */
	public void updateCar(Car car);
	
	/**
	 * Method returns car by id
	 * @param id
	 * @return car
	 */
	public Car findCarById(Long id);	        
}
