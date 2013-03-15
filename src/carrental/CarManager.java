/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Trida zajistuje manazera pro praci s objektem Car
 * @package carrental
 * @file CarManager.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */

package carrental;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author honzap
 */
public class CarManager implements ICarManager{
	
	public CarManager() {	
	}
	
	@Override
	public Car createCar(Car car) {
		return new Car();
	}
	
	@Override
	public void deleteCar(Car car){
	}
	
	@Override
	public List<Car> findAllCars(){
		return new ArrayList<Car>();
	}
	
	@Override
	public void updateCar(Car car){
	}
	
	@Override
	public Car findCarById(Long id){
		return new Car();
	}
        
        @Override
        public Long getNextCarId()
        {
            return new Long(System.currentTimeMillis());
        }
}
