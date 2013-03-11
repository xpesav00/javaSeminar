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


public class RentalManagerTest {
	
	private RentalManager rentalManager;
	private CarManager carManager;
	private DriverManager driverManager;
	
	public RentalManagerTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
		//set rentalManager
		this.rentalManager = new RentalManager();
		
		//set carManager
		this.carManager = new CarManager();
		
		//set driverManager
		this.driverManager = new DriverManager();
	}
	
	@After
	public void tearDown() {
		this.rentalManager = null;
		this.driverManager = null;
		this.carManager = null;
	}

	/**
	 * Test of findDriversByCar method, of class IRentalManager.
	 */
	@Test
	public void testFindDriversByCar() {
		System.out.println("findDriversByCar");
		
		//create objects
		Driver driver1 = new Driver(new Long(1), "Karel", "Tomasek", "DC12321148");
		Driver driver2 = new Driver(new Long(2), "Martin", "Maly", "DC34342345");
		Car car = new Car(new Long(1), "VIN123456789", "1E3-2233", "Ford Transit", new Double(120433));
		
		//create cars nad drivers
		this.driverManager.createDriver(driver1);
		this.driverManager.createDriver(driver2);
		this.carManager.createCar(car);		
		
		//no rent no relations
		assertTrue("Auto 1 zatim nebylo pujceno", this.rentalManager.findDriversByCar(car).isEmpty());
		
		//rent car
		this.rentalManager.rentCarToDriver(car, driver1);
		this.rentalManager.returnCar(car, driver1);
		this.rentalManager.rentCarToDriver(car, driver2);
		
		//test a rent
		List<Driver> driversList = this.rentalManager.findDriversByCar(car); 
		assertEquals("Seznam neobsahuje dva prvky.", 2, driversList.size());
		Driver driver1Tmp = driversList.get(1);
		Driver driver2Tmp = driversList.get(2);
		assertNotNull("Vytazeny prvek je null.", driver1Tmp);
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", driver1, driver1Tmp);
		assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci.", driver1, driver1Tmp);
		assertNotNull("Vytazeny prvek je null.", driver2Tmp);
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", driver2, driver2Tmp);
		assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci.", driver2, driver2Tmp);		
		
		//fail("The test case is a prototype.");
	}

	/**
	 * Test of findCarsByDriver method, of class IRentalManager.
	 */
	@Test()
	public void testFindCarsByDriver() {
		System.out.println("findCarsByDriver");
		
		//create objects
		Driver driver = new Driver(new Long(1), "Karel", "Tomasek", "DC12321148");
		Car car1 = new Car(new Long(1), "VIN123456789", "1E3-2233", "Ford Transit", new Double(120433));
		Car car2 = new Car(new Long(2), "VIN134566789", "2A4-5452", "Skoda Superb", new Double(12330));
		Car car3 = new Car(new Long(3), "VIN234234567", "6P3-5049", "Seat Toledo", new Double(90230));
		
		//create cars nad drivers
		this.driverManager.createDriver(driver);
		this.carManager.createCar(car1);
		this.carManager.createCar(car2);
		this.carManager.createCar(car3);
		
		//no rent no relations
		assertTrue("Ridic si prozatim zadne auto nepujcil.", this.rentalManager.findCarsByDriver(driver).isEmpty());
		
		//rent car
		this.rentalManager.rentCarToDriver(car1, driver);
		this.rentalManager.rentCarToDriver(car2, driver);
		this.rentalManager.returnCar(car1, driver);
		this.rentalManager.rentCarToDriver(car3, driver);
		
		//test a rent
		List<Car> carsList = this.rentalManager.findCarsByDriver(driver); 
		assertEquals("Seznam neobsahuje tri prvky.", 3, carsList.size());
		Car car1Tmp = carsList.get(1);
		Car car2Tmp = carsList.get(2);
		Car car3Tmp = carsList.get(3);
		
		assertNotNull("Vytazeny prvek je null.", car1Tmp);
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", car1, car1Tmp);
		assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci.", car1, car1Tmp);
		assertNotNull("Vytazeny prvek je null.", car2Tmp);
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", car2, car2Tmp);
		assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci.", car2, car2Tmp);
		assertNotNull("Vytazeny prvek je null.", car3Tmp);
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", car3, car3Tmp);
		assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci.", car3, car3Tmp);
		
		//fail("The test case is a prototype.");
	}

	/**
	 * Test of rentCarToDriver method, of class IRentalManager.
	 */
	@Test
	public void testRentCarToDriver() {
		System.out.println("rentCarToDriver");
		
		//create objects
		Driver driver = new Driver(new Long(1), "Karel", "Tomasek", "DC12321148");
		Car car1 = new Car(new Long(1), "VIN123456789", "1E3-2233", "Ford Transit", new Double(120433));
		Car car2 = new Car(new Long(2), "VIN134566789", "2A4-5452", "Skoda Superb", new Double(12330));
		Car car3 = new Car(new Long(3), "VIN234234567", "6P3-5049", "Seat Toledo", new Double(90230));
		
		//create cars nad drivers
		this.driverManager.createDriver(driver);
		this.carManager.createCar(car1);
		this.carManager.createCar(car2);
		this.carManager.createCar(car3);
		
		//test before rent
		assertEquals("Pocet dostupnych aut nesedi", 3, this.rentalManager.findAllCarsOnStock());
		assertEquals("Pocet pujcenych aut nesedi", 0, this.rentalManager.findAllRentCars());
		
		//rent cars
		this.rentalManager.rentCarToDriver(car1, driver);
		this.rentalManager.rentCarToDriver(car2, driver);
		this.rentalManager.rentCarToDriver(car3, driver);
		this.rentalManager.returnCar(car3, driver);
		
		//test a rent car
		assertEquals("Pocet dostupnych aut pro pujceni nesedi", 1, this.rentalManager.findAllCarsOnStock());
		assertEquals("Pocet prave pujcenych aut nesedi", 2, this.rentalManager.findAllRentCars());
		
		// try to rent a rented car
		try {
			this.rentalManager.rentCarToDriver(car1, driver);
			fail("Neni mozne pujcit jiz pujcene auto");
		} catch(Exception e) {
			// not implemented
		}
		
		//try to return a returned car
		try {
			this.rentalManager.rentCarToDriver(car3, driver);
			fail("Neni mozne vratit jiz vracene auto");
		} catch(Exception e) {
			// not implemented
		}
		
		//fail("The test case is a prototype.");
	}

	/**
	 * Test of returnCar method, of class IRentalManager.
	 */
	@Test
	public void testReturnCar() {
		System.out.println("returnCar");
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findAllRentCars method, of class IRentalManager.
	 */
	@Test
	public void testFindAllRentCars() {
		System.out.println("findAllRentCars");
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findAllCarsOnStock method, of class IRentalManager.
	 */
	@Test
	public void testFindAllCarsOnStock() {
		System.out.println("findAllCarsOnStock");
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isCarFree method, of class IRentalManager.
	 */
	@Test
	public void testIsCarFree() {
		System.out.println("isCarFree");
		fail("The test case is a prototype.");
	}
}
