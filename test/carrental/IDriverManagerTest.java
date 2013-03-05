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
public class IDriverManagerTest {
	
	public IDriverManagerTest() {
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
	 * Test of createDriver method, of class IDriverManager.
	 */
	@Test
	public void testCreateDriver() {
		System.out.println("createDriver");
		Driver driver = null;
		IDriverManager instance = new IDriverManagerImpl();
		Driver expResult = null;
		Driver result = instance.createDriver(driver);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of deleteDriver method, of class IDriverManager.
	 */
	@Test
	public void testDeleteDriver() {
		System.out.println("deleteDriver");
		Driver driver = null;
		IDriverManager instance = new IDriverManagerImpl();
		instance.deleteDriver(driver);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateDriver method, of class IDriverManager.
	 */
	@Test
	public void testUpdateDriver() {
		System.out.println("updateDriver");
		Driver driver = null;
		IDriverManager instance = new IDriverManagerImpl();
		instance.updateDriver(driver);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findAllDrivers method, of class IDriverManager.
	 */
	@Test
	public void testFindAllDrivers() {
		System.out.println("findAllDrivers");
		IDriverManager instance = new IDriverManagerImpl();
		List expResult = null;
		List result = instance.findAllDrivers();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findDriverById method, of class IDriverManager.
	 */
	@Test
	public void testFindDriverById() {
		System.out.println("findDriverById");
		int id = 0;
		IDriverManager instance = new IDriverManagerImpl();
		Driver expResult = null;
		Driver result = instance.findDriverById(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	public class IDriverManagerImpl implements IDriverManager {

		public Driver createDriver(Driver driver) {
			return null;
		}

		public void deleteDriver(Driver driver) {
		}

		public void updateDriver(Driver driver) {
		}

		public List<Driver> findAllDrivers() {
			return null;
		}

		public Driver findDriverById(int id) {
			return null;
		}
	}
}
