/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Trida zajistuje rozhrani pro manazera ridicu
 * @package carrental
 * @file IDriverManager.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */

package carrental;
import java.util.List;

public interface IDriverManager {
	public Driver createDriver(Driver driver);
	public void deleteDriver(Driver driver);
	public void updateDriver(Driver driver);
	public List<Driver> findAllDrivers();
	public Driver findDriverById(int id);
}
