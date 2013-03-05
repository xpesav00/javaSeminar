/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package carrental;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author honzap
 */
public class ICarManagerTest {
	
	public ICarManagerTest() {
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
	 * Test of createCar method, of class ICarManager.
	 */
	@Test
	public void testCreateCar() {
		System.out.println("createCar");
		Car car = null;
		ICarManager instance = new ICarManagerImpl();
		Car expResult = null;
		Car result = instance.createCar(car);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of deleteCar method, of class ICarManager.
	 */
	@Test
	public void testDeleteCar() {
		System.out.println("deleteCar");
		Car car = null;
		ICarManager instance = new ICarManagerImpl();
		instance.deleteCar(car);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findAllCars method, of class ICarManager.
	 */
	@Test
	public void testFindAllCars() {
		System.out.println("findAllCars");
		ICarManager instance = new ICarManagerImpl();
		List expResult = null;
		List result = instance.findAllCars();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateCar method, of class ICarManager.
	 */
	@Test
	public void testUpdateCar() {
		System.out.println("updateCar");
		Car car = null;
		ICarManager instance = new ICarManagerImpl();
		instance.updateCar(car);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findCarById method, of class ICarManager.
	 */
	@Test
	public void testFindCarById() {
		System.out.println("findCarById");
		int id = 0;
		ICarManager instance = new ICarManagerImpl();
		Car expResult = null;
		Car result = instance.findCarById(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	public class ICarManagerImpl implements ICarManager {

		public Car createCar(Car car) {
			return null;
		}

		public void deleteCar(Car car) {
		}

		public List<Car> findAllCars() {
			return null;
		}

		public void updateCar(Car car) {
		}

		public Car findCarById(int id) {
			return null;
		}
	}
}
