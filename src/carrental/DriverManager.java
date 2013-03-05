/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Trida zajistuje spravu dat nad objektem Driver
 * @package carrental
 * @file DriverManager.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */

package carrental;

import java.util.ArrayList;
import java.util.List;

public class DriverManager implements IDriverManager{

	public DriverManager() {
	}
	
	@Override
	public Driver createDriver(Driver driver){
		return new Driver();
	}
	
	@Override
	public void deleteDriver(Driver driver){
	}
	
	@Override
	public void updateDriver(Driver driver){
	}
	
	@Override
	public List<Driver> findAllDrivers(){
		return new ArrayList<>();
	}
	
	@Override
	public Driver findDriverById(int id){
		return new Driver();
	}
}
