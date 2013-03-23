/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Trida tvori rozhrani pro obhospodarovani auta
 * @package carrental
 * @file ICarManager.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */

package carrental;
import java.util.List;

public interface ICarManager {
	public Car createCar(Car car);
	public void deleteCar(Car car);
	public List<Car> findAllCars();
	public void updateCar(Car car);
	public Car findCarById(Long id);	        
}
