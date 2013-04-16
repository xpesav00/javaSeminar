/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Trida zajistuje rozhrani pro manazera ridicu
 * @package carrental
 * @file IDriverManager.java
 * @author Jan Pesava - xpesav00, Filip Krepinsky
 * @email xpesav00@mail.muni.cz, 410022@mail.muni.cz
 * @date 5. 3. 2013
 */

package carrental;
import java.util.List;

public interface IDriverManager {
	/**
	 * Method creates driver in db
	 * @param driver
	 * @return driver with id
	 */
	public Driver createDriver(Driver driver);
	
	/**
	 * Method deletes driver from db
	 * @param driver 
	 */
	public void deleteDriver(Driver driver);
	
	/**
	 * Method updates driver in db
	 * @param driver 
	 */
	public void updateDriver(Driver driver);
	
	/**
	 * Method returns all drivers in db
	 * @return List<Driver>
	 */
	public List<Driver> findAllDrivers();
	
	/**
	 * Method returns driver by id
	 * @param id
	 * @return Driver 
	 */
	public Driver findDriverById(Long id);
}
