/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Trida zajistuje testovani objektu s rozhranim IRentalManager
 * @package carrental
 * @file IRentalManagerTest.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */

package carrental;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class IRentalManagerTest {
	
	public IRentalManagerTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of findDriversByCar method, of class IRentalManager.
	 */
	@Test
	public void testFindDriversByCar() {
		System.out.println("findDriversByCar");
		Car car = null;
		IRentalManager instance = new IRentalManagerImpl();
		List expResult = null;
		List result = instance.findDriversByCar(car);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findCarsByDriver method, of class IRentalManager.
	 */
	@Test
	public void testFindCarsByDriver() {
		System.out.println("findCarsByDriver");
		Driver driver = null;
		IRentalManager instance = new IRentalManagerImpl();
		List expResult = null;
		List result = instance.findCarsByDriver(driver);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of rentCarToDriver method, of class IRentalManager.
	 */
	@Test
	public void testRentCarToDriver() {
		System.out.println("rentCarToDriver");
		Car car = null;
		Driver driver = null;
		IRentalManager instance = new IRentalManagerImpl();
		instance.rentCarToDriver(car, driver);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of returnCar method, of class IRentalManager.
	 */
	@Test
	public void testReturnCar() {
		System.out.println("returnCar");
		Car car = null;
		Driver driver = null;
		IRentalManager instance = new IRentalManagerImpl();
		instance.returnCar(car, driver);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findAllRentCars method, of class IRentalManager.
	 */
	@Test
	public void testFindAllRentCars() {
		System.out.println("findAllRentCars");
		IRentalManager instance = new IRentalManagerImpl();
		List expResult = null;
		List result = instance.findAllRentCars();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findAllCarsOnStock method, of class IRentalManager.
	 */
	@Test
	public void testFindAllCarsOnStock() {
		System.out.println("findAllCarsOnStock");
		IRentalManager instance = new IRentalManagerImpl();
		List expResult = null;
		List result = instance.findAllCarsOnStock();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isCarFree method, of class IRentalManager.
	 */
	@Test
	public void testIsCarFree() {
		System.out.println("isCarFree");
		Car car = null;
		IRentalManager instance = new IRentalManagerImpl();
		boolean expResult = false;
		boolean result = instance.isCarFree(car);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	public class IRentalManagerImpl implements IRentalManager {

		public List<Driver> findDriversByCar(Car car) {
			return null;
		}

		public List<Car> findCarsByDriver(Driver driver) {
			return null;
		}

		public void rentCarToDriver(Car car, Driver driver) {
		}

		public void returnCar(Car car, Driver driver) {
		}

		public List<Car> findAllRentCars() {
			return null;
		}

		public List<Car> findAllCarsOnStock() {
			return null;
		}

		public boolean isCarFree(Car car) {
			return false;
		}
	}
}
