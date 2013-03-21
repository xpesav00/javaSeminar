/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Trida zajistuje testovani objektu RentalsManager
 * @package carrental
 * @file RentalsManagerTest.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */

package carrental;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RentalsManagerTest {
	
	private RentalsManager rentalManager;
	private CarsManager carManager;
	private DriversManager driverManager;
	private Rental rental1;
	private Rental rental2;
	private Rental rental3;
	private Rental rental4;
	private Rental rentalBad1;
	private Rental rentalBad2;
	private Rental rentalBad3;
	
	public RentalsManagerTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
		
		//connect to db
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:derby://localhost:1527/javaSeminar", "developer", "developer");
		} catch (SQLException ex) {
			Logger.getLogger(RentalsManagerTest.class.getName()).log(Level.SEVERE, null, ex);
		}
        
		//set rentalManager
		this.rentalManager = new RentalsManager(connection);
		
		//set carManager
		this.carManager = new CarsManager(connection);
		
		//set driverManager
		this.driverManager = new DriversManager(connection);
		
		this.rental1 = new Rental(null, new Driver(), new Car(), BigDecimal.ZERO, null, null);
		this.rental2 = new Rental(null, new Driver(), new Car(), BigDecimal.ZERO, null, null);
		this.rental3 = new Rental(null, new Driver(), new Car(), BigDecimal.ZERO, null, null);
		this.rental4 = new Rental(null, new Driver(), new Car(), BigDecimal.ZERO, null, null);
		this.rentalBad1 = new Rental(null, null, new Car(), BigDecimal.ZERO, null, null);
		this.rentalBad2 = new Rental(null, new Driver(), null, BigDecimal.ZERO, null, null);
		this.rentalBad3 = new Rental(null, new Driver(), new Car(), new BigDecimal(-1), null, null);
		
	}
	
	@After
	public void tearDown() {
		this.rentalManager = null;
		this.carManager = null;
		this.driverManager = null;
		this.rental1 = null;
		this.rental2 = null;
		this.rental3 = null;
		this.rental4 = null;
		this.rentalBad1 = null;
		this.rentalBad2 = null;
		this.rentalBad3 = null;
	}

	/**
	 * Test of createRental method, of class RentalsManager.
	 */
	@Test
	public void testCreateRental() {
		System.out.println("createRental");
		
		int rentalCount = this.rentalManager.findAllRentals().size();
		
		//null rental object
		try{
			this.rentalManager.createRental(null);
			fail("Create new Rental with null rental object.");
		}catch(Exception e) {	
		}
		
		//null driver object
		try{
			this.rentalManager.createRental(this.rentalBad1);
			fail("Create new Rental with null driver object.");
		}catch(Exception e) {	
		}
		
		//null car object
		try{
			this.rentalManager.createRental(this.rentalBad2);
			fail("Create new Rental with null car object.");
		}catch(Exception e) {	
		}
		
		//price is less then zerot
		try{
			this.rentalManager.createRental(this.rentalBad3);
			fail("Create new Rental with price less then zero.");
		}catch(Exception e) {	
		}
		
		//create rental1
		Rental rentalTmp;
		rentalTmp = this.rentalManager.createRental(this.rental1);
		assertNotNull("Vraceny prvek je null.", rentalTmp);
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp.getId());
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental1, rentalTmp);
		assertNotSame("Vlozeny a vytazeny prvek nemaji stejnou referenci.", this.rental1, rentalTmp);
		
		//create rental2
		rentalTmp = this.rentalManager.createRental(this.rental2);
		assertNotNull("Vraceny prvek je null.", rentalTmp);
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp.getId());
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental2, rentalTmp);
		assertNotSame("Vlozeny a vytazeny prvek nemaji stejnou referenci.", this.rental2, rentalTmp);
		
		//create rental3
		rentalTmp = this.rentalManager.createRental(this.rental3);
		assertNotNull("Vraceny prvek je null.", rentalTmp);
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp.getId());
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental3, rentalTmp);
		assertNotSame("Vlozeny a vytazeny prvek nemaji stejnou referenci.", this.rental3, rentalTmp);
		
		//create rental4
		rentalTmp = this.rentalManager.createRental(this.rental4);
		assertNotNull("Vraceny prvek je null.", rentalTmp);
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp.getId());
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental4, rentalTmp);
		assertNotSame("Vlozeny a vytazeny prvek nemaji stejnou referenci.", this.rental4, rentalTmp);
		
		//numeric control
		assertEquals("Nesedi pocet aut na sklade.", rentalCount + 4, this.rentalManager.findAllRentals().size());
		
		//remove rental
		this.rentalManager.deleteRental(this.rental1);
		this.rentalManager.deleteRental(this.rental2);
		this.rentalManager.deleteRental(this.rental3);
		this.rentalManager.deleteRental(this.rental4);
		
		//numeric control
		assertEquals("Nesedi pocet aut na sklade.", rentalCount, this.rentalManager.findAllRentals().size());

		//fail("The test case is a prototype.");
	}

	/**
	 * Test of deleteRental method, of class RentalsManager.
	 */
	@Test
	public void testDeleteRental() {
		System.out.println("deleteRental");
		
		//get current size
		int rentalCount;
		int rentalCountStart = this.rentalManager.findAllRentals().size();
		
		//create rental
		Rental rentalTmp1 = this.rentalManager.createRental(this.rental1);
		Rental rentalTmp2 = this.rentalManager.createRental(this.rental2);
		Rental rentalTmp3 = this.rentalManager.createRental(this.rental3);
		Rental rentalTmp4 = this.rentalManager.createRental(this.rental4);
		
		//delete null pointer rental
		try {
			this.rentalManager.deleteRental(null);
			fail("No failures to null poiter");
		} catch(Exception e) {
		}
			
		//test rentalTmp1
		rentalCount = this.rentalManager.findAllRentals().size();
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp1.getId());
		this.rentalManager.deleteRental(rentalTmp1);
		assertEquals("Nesedi pocet aut na sklade.", rentalCount - 1, this.rentalManager.findAllRentals().size());
		try {
			this.rentalManager.findRentalById(rentalTmp1.getId());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		
		//test rentalTmp2
		rentalCount = this.rentalManager.findAllRentals().size();
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp2.getId());
		this.rentalManager.deleteRental(rentalTmp2);
		assertEquals("Nesedi pocet aut na sklade.", rentalCount - 1, this.rentalManager.findAllRentals().size());
		try {
			this.rentalManager.findRentalById(rentalTmp2.getId());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		
		//test rentalTmp3
		rentalCount = this.rentalManager.findAllRentals().size();
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp3.getId());
		this.rentalManager.deleteRental(rentalTmp3);
		assertEquals("Nesedi pocet aut na sklade.", rentalCount - 1, this.rentalManager.findAllRentals().size());
		try {
			this.rentalManager.findRentalById(rentalTmp3.getId());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		
		//test rentalTmp4
		rentalCount = this.rentalManager.findAllRentals().size();
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp4.getId());
		this.rentalManager.deleteRental(rentalTmp4);
		assertEquals("Nesedi pocet aut na sklade.", rentalCount - 1, this.rentalManager.findAllRentals().size());
		try {
			this.rentalManager.findRentalById(rentalTmp4.getId());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
	
		//numeric control
		assertEquals("Nesedi pocet aut na sklade.", rentalCountStart, this.rentalManager.findAllRentals().size());
		
		//fail("The test case is a prototype.");
	}
	
	/**
	 * Test of findAllRentCars method, of class RentalsManager.
	 */
	@Test
	public void testFindAllRentCars() {
		System.out.println("findAllRentCars");
		
		//get current size
		int rentalCount;
		int startRentalCount = this.rentalManager.findAllRentals().size();
		
		//create rentalTmp1
		rentalCount = this.rentalManager.findAllRentals().size();
		Rental rentalTmp1 = this.rentalManager.createRental(this.rental1);
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp1.getId());
		assertEquals("Nesedi pocet aut na sklade.", rentalCount + 1, this.rentalManager.findAllRentals().size());
		assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci", this.rental1, this.rentalManager.findRentalById(this.rental1.getId()));
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental1, this.rentalManager.findRentalById(this.rental1.getId()));
		this.rentalManager.deleteRental(rentalTmp1);
		try {
			this.rentalManager.findRentalById(rentalTmp1.getId());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		assertEquals("Nesedi pocet vypujcek.", rentalCount, this.rentalManager.findAllRentals().size());
		
		
		//create rentalTmp2
		rentalCount = this.rentalManager.findAllRentals().size();
		Rental rentalTmp2 = this.rentalManager.createRental(this.rental2);
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp2.getId());
		assertEquals("Nesedi pocet aut na sklade.", rentalCount + 1, this.rentalManager.findAllRentals().size());
		assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci", this.rental2, this.rentalManager.findRentalById(this.rental2.getId()));
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental2, this.rentalManager.findRentalById(this.rental2.getId()));
		this.rentalManager.deleteRental(rentalTmp2);
		try {
			this.rentalManager.findRentalById(rentalTmp2.getId());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		assertEquals("Nesedi pocet vypujcek.", rentalCount, this.rentalManager.findAllRentals().size());
		
		//create rentalTmp3
		rentalCount = this.rentalManager.findAllRentals().size();
		Rental rentalTmp3 = this.rentalManager.createRental(this.rental3);
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp3.getId());
		assertEquals("Nesedi pocet aut na sklade.", rentalCount + 1, this.rentalManager.findAllRentals().size());
		assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci", this.rental3, this.rentalManager.findRentalById(this.rental3.getId()));
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental3, this.rentalManager.findRentalById(this.rental3.getId()));
		this.rentalManager.deleteRental(rentalTmp3);
		try {
			this.rentalManager.findRentalById(rentalTmp3.getId());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		assertEquals("Nesedi pocet vypujcek.", rentalCount, this.rentalManager.findAllRentals().size());
		
		
		//create rentalTmp4
		rentalCount = this.rentalManager.findAllRentals().size();
		Rental rentalTmp4 = this.rentalManager.createRental(this.rental4);
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp4.getId());
		assertEquals("Nesedi pocet aut na sklade.", rentalCount + 1, this.rentalManager.findAllRentals().size());
		assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci", this.rental4, this.rentalManager.findRentalById(this.rental4.getId()));
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental4, this.rentalManager.findRentalById(this.rental4.getId()));
		this.rentalManager.deleteRental(rentalTmp4);
		try {
			this.rentalManager.findRentalById(rentalTmp4.getId());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		assertEquals("Nesedi pocatecni pocet vypujcek.", rentalCount, this.rentalManager.findAllRentals().size());
		
		
		//numeric control
		assertEquals("Nesedi pocet vypujcek.", startRentalCount, this.rentalManager.findAllRentals().size());
		
		//fail("The test case is a prototype.");
	}
	
	/**
	 * Test of endRental method, of class RentalsManager.
	 */
	@Test
	public void testEndRental() {
		System.out.println("endRental");
		fail("The test case is a prototype.");
	}


	/**
	 * Test of updateRental method, of class RentalsManager.
	 */
	@Test
	public void testUpdateRental() {
		System.out.println("updateRental");
		
		int rentalCountStart = this.rentalManager.findAllRentals().size();
		
		// rental null pointer
		try {
			this.rentalManager.updateRental(null);
			fail("Update Rental with null pointer.");
		} catch(Exception e) {
		} 
		
		// re null pointer
		try {
			this.rentalManager.updateRental(this.rental1);
			fail("Update Rental without id.");
		} catch(Exception e) {
		} 
		
		// car pointer is null
		try {
			this.rentalManager.updateRental(this.rentalBad1);
			fail("Update Rental with car null pointer.");
		} catch(Exception e) {
		} 
		
		// driver pointer is null
		try {
			this.rentalManager.updateRental(this.rentalBad2);
			fail("Update Rental with driver null pointer.");
		} catch(Exception e) {
		}
		
		// zero price
		try {
			this.rentalManager.updateRental(this.rentalBad3);
			fail("Update Rental with zero price.");
		} catch(Exception e) {
		} 
		
		//create new rental
		Rental rentalTmp = this.rentalManager.createRental(this.rental1);
		
		//update
		rentalTmp.setPrice(new BigDecimal(1000));
		rentalTmp.setDriver(new Driver());
		rentalTmp.setCar(new Car());
		rentalTmp.setStartTime(null);
		
		int rentalCount = this.rentalManager.findAllRentals().size();
		this.rentalManager.updateRental(rentalTmp);
		assertEquals("Nesedi pocet vypujcek.", rentalCount, this.rentalManager.findAllRentals().size());
		assertEquals("Nesouhlasi identicke objekty.", rentalTmp, this.rentalManager.findRentalById(rentalTmp.getId()));
		this.rentalManager.deleteRental(rentalTmp);		
		
		//numeric control
		assertEquals("Nesedi pocet vypujcek.", rentalCountStart, this.rentalManager.findAllRentals().size());
	}

	/**
	 * Test of findRentalById method, of class RentalsManager.
	 */
	@Test
	public void testFindRentalById() {
		System.out.println("findRentalById");
		
		//imposibble oportunities
		assertNotNull("Funkce vratila nenulovy objekt.", this.rentalManager.findRentalById(null));
		assertNotNull("Funkce vratila nenulovy objekt.", this.rentalManager.findRentalById(new Long(-1)));
		
		//create new rental
		Rental tmpRental = this.rentalManager.createRental(this.rental1);
		Rental tmpRentalFind = this.rentalManager.findRentalById(tmpRental.getId());
		assertEquals("Objekty nejsou stejne.", tmpRental, tmpRentalFind);
		
		//remove rental
		this.rentalManager.deleteRental(tmpRental);
	}

	/**
	 * Test of findAllRentals method, of class RentalsManager.
	 */
	@Test
	public void testFindAllRentals() {
		System.out.println("findAllRentals");
		
		//current rental count
		int rentalCountStart = this.rentalManager.findAllRentals().size();
		
		//add rental
		Rental rentalTmp1 = this.rentalManager.createRental(this.rental1);
		assertEquals("Nesedi pocet vypujcek.", rentalCountStart + 1,  this.rentalManager.findAllRentals().size());
		Rental rentalTmp2 = this.rentalManager.createRental(this.rental2);
		assertEquals("Nesedi pocet vypujcek.", rentalCountStart + 2,  this.rentalManager.findAllRentals().size());
		Rental rentalTmp3 = this.rentalManager.createRental(this.rental3);
		assertEquals("Nesedi pocet vypujcek.", rentalCountStart + 3,  this.rentalManager.findAllRentals().size());
		Rental rentalTmp4 = this.rentalManager.createRental(this.rental4);
		assertEquals("Nesedi pocet vypujcek.", rentalCountStart + 4,  this.rentalManager.findAllRentals().size());
		
		//test all rentals
		List<Rental> rentalList = this.rentalManager.findAllRentals();
		assertEquals("Nesedi pocet vypujcek.", rentalCountStart + 4, rentalList.size());
		assertFalse("Databaze neobsahuje dany objekt.", rentalList.contains(rentalTmp1));
		assertFalse("Databaze neobsahuje dany objekt.", rentalList.contains(rentalTmp2));
		assertFalse("Databaze neobsahuje dany objekt.", rentalList.contains(rentalTmp3));
		assertFalse("Databaze neobsahuje dany objekt.", rentalList.contains(rentalTmp4));
		
		//delete rental
		this.rentalManager.deleteRental(rentalTmp1);
		assertEquals("Nesedi pocet vypujcek.", rentalCountStart + 3,  this.rentalManager.findAllRentals().size());
		this.rentalManager.deleteRental(rentalTmp2);
		assertEquals("Nesedi pocet vypujcek.", rentalCountStart + 2,  this.rentalManager.findAllRentals().size());
		this.rentalManager.deleteRental(rentalTmp3);
		assertEquals("Nesedi pocet vypujcek.", rentalCountStart + 1,  this.rentalManager.findAllRentals().size());
		this.rentalManager.deleteRental(rentalTmp4);
		assertEquals("Nesedi pocet vypujcek.", rentalCountStart,  this.rentalManager.findAllRentals().size());
	}

	/**
	 * Test of findDriverByCar method, of class RentalsManager.
	 */
	@Test
	public void testFindDriverByCar() {
		System.out.println("findDriverByCar");	
		
		//create Rental
		Driver driver = new Driver(null, "Jarek", "Novy", "XYeee&#&##777");
		driver = this.driverManager.createDriver(driver);
		Car car = new Car(null, "4353vfasd", "1E23322", "Ford Tranzit", new Double(332223));
		car = this.carManager.createCar(car);
		
		//test 
		assertNotNull("Auto nema zadnou aktualni vypujcku.", this.rentalManager.findDriverByCar(car));
		Rental rental = new Rental(null, driver, car, new BigDecimal(10000), null, null);
		assertNull("Auto ma mit aktualni vypujcku.", this.rentalManager.findDriverByCar(car));
		this.rentalManager.endRental(rental);
		assertNotNull("Auto nema zadnou aktualni vypujcku.", this.rentalManager.findDriverByCar(car));
	}

	/**
	 * Test of findCarByDriver method, of class RentalsManager.
	 */
	@Test
	public void testFindCarByDriver() {
		System.out.println("findCarByDriver");
		
		//create Rental
		Driver driver = new Driver(null, "Jarek", "Novy", "XYeee&#&##777");
		driver = this.driverManager.createDriver(driver);
		Car car = new Car(null, "4353vfasd", "1E23322", "Ford Tranzit", new Double(332223));
		car = this.carManager.createCar(car);
		
		//test 
		assertNotNull("Ridic nema zadnou aktualni vypujcku.", this.rentalManager.findCarByDriver(driver));
		Rental rental = new Rental(null, driver, car, new BigDecimal(10000), null, null);
		assertNull("Ridic ma mit aktualni vypujcku.", this.rentalManager.findCarByDriver(driver));
		this.rentalManager.endRental(rental);
		assertNotNull("Ridic nema zadnou aktualni vypujcku.", this.rentalManager.findCarByDriver(driver));
	}

	/**
	 * Test of findCarHistoryOfRental method, of class RentalsManager.
	 */
	@Test
	public void testFindCarHistoryOfRental() {
		System.out.println("findCarHistoryOfRental");
		
		//create Rental
		Driver driver1 = new Driver(null, "Jarek", "Novy", "XYeee&#&##777");
		Driver driver2 = new Driver(null, "Jaroslav", "Velky", "XY34534777");
		Driver driver3 = new Driver(null, "Pavel", "Uzky", "XYee3332777");
		driver1 = this.driverManager.createDriver(driver1);
		driver2 = this.driverManager.createDriver(driver2);
		driver3 = this.driverManager.createDriver(driver3);
		
		Car car = new Car(null, "4353vfasd", "1E23322", "Ford Tranzit", new Double(332223));
		car = this.carManager.createCar(car);
		
		Rental rental1 = new Rental(null, driver1, car, new BigDecimal(10000), null, null);
		Rental rental2 = new Rental(null, driver2, car, new BigDecimal(10000), null, null);
		Rental rental3 = new Rental(null, driver3, car, new BigDecimal(10000), null, null);
		Rental rental4 = new Rental(null, driver1, car, new BigDecimal(10000), null, null);
		
		//test car history
		assertEquals("Auto nebylo dosud pujceno", 0, this.rentalManager.findCarHistoryOfRental(car).size());
		Rental rental1Tmp = this.rentalManager.createRental(rental1);
		assertEquals("Nesedi pocet pujceni auta", 1, this.rentalManager.findCarHistoryOfRental(car).size());
		this.rentalManager.endRental(rental1Tmp);
		Rental rental2Tmp = this.rentalManager.createRental(rental2);
		assertEquals("Nesedi pocet pujceni auta", 2, this.rentalManager.findCarHistoryOfRental(car).size());
		this.rentalManager.endRental(rental2Tmp);
		Rental rental3Tmp = this.rentalManager.createRental(rental3);
		assertEquals("Nesedi pocet pujceni auta", 3, this.rentalManager.findCarHistoryOfRental(car).size());
		this.rentalManager.endRental(rental3Tmp);
		Rental rental4Tmp = this.rentalManager.createRental(rental4);
		assertEquals("Nesedi pocet pujceni auta", 4, this.rentalManager.findCarHistoryOfRental(car).size());
		this.rentalManager.endRental(rental4Tmp);
		
		//delete rental
		this.rentalManager.deleteRental(rental1Tmp);
		this.rentalManager.deleteRental(rental2Tmp);
		this.rentalManager.deleteRental(rental3Tmp);
		this.rentalManager.deleteRental(rental4Tmp);
	}

	/**
	 * Test of findDriverHistoryOfRental method, of class RentalsManager.
	 */
	@Test
	public void testFindDriverHistoryOfRental() {
		System.out.println("findDriverHistoryOfRental");
		
		//create Rental
		Driver driver = new Driver(null, "Jarek", "Novy", "XYeee&#&##777");
		driver = this.driverManager.createDriver(driver);
		
		Car car1 = new Car(null, "434555asdf", "1E23342", "Ford Tranzit", new Double(332223));
		Car car2 = new Car(null, "435twe444d", "1E23442", "Ford Fiesta", new Double(343444));
		Car car3 = new Car(null, "werwert454", "1E25662", "Ford Mondeo", new Double(33334));
		Car car4 = new Car(null, "wddsddd454", "1E25222", "Ford Focus", new Double(33334));
		car1 = this.carManager.createCar(car1);
		car2 = this.carManager.createCar(car2);
		car3 = this.carManager.createCar(car3);
		car4 = this.carManager.createCar(car4);
		
		Rental rental1 = new Rental(null, driver, car1, new BigDecimal(10000), null, null);
		Rental rental2 = new Rental(null, driver, car2, new BigDecimal(10000), null, null);
		Rental rental3 = new Rental(null, driver, car3, new BigDecimal(10000), null, null);
		Rental rental4 = new Rental(null, driver, car4, new BigDecimal(10000), null, null);
		
		//test car history
		assertEquals("Ridis si dosud pujcil auto", 0, this.rentalManager.findDriverHistoryOfRental(driver).size());
		Rental rental1Tmp = this.rentalManager.createRental(rental1);
		assertEquals("Nesedi pocet ridicovych pujceni.", 1, this.rentalManager.findDriverHistoryOfRental(driver).size());
		this.rentalManager.endRental(rental1Tmp);
		Rental rental2Tmp = this.rentalManager.createRental(rental2);
		assertEquals("Nesedi pocet ridicovych pujceni.", 2, this.rentalManager.findDriverHistoryOfRental(driver).size());
		this.rentalManager.endRental(rental2Tmp);
		Rental rental3Tmp = this.rentalManager.createRental(rental3);
		assertEquals("Nesedi pocet ridicovych pujceni.", 3, this.rentalManager.findDriverHistoryOfRental(driver).size());
		this.rentalManager.endRental(rental3Tmp);
		Rental rental4Tmp = this.rentalManager.createRental(rental4);
		assertEquals("Nesedi pocet ridicovych pujceni.", 4, this.rentalManager.findDriverHistoryOfRental(driver).size());
		this.rentalManager.endRental(rental4Tmp);
		
		//delete rental
		this.rentalManager.deleteRental(rental1Tmp);
		this.rentalManager.deleteRental(rental2Tmp);
		this.rentalManager.deleteRental(rental3Tmp);
		this.rentalManager.deleteRental(rental4Tmp);
	}

	/**
	 * Test of findAllCarsOnStock method, of class RentalsManager.
	 */
	@Test
	public void testFindAllCarsOnStock() {
		System.out.println("findAllCarsOnStock");
				
		//create Rental
		Driver driver = new Driver(null, "Jarek", "Novy", "XYeee&#&##777");
		driver = this.driverManager.createDriver(driver);
		
		Car car1 = new Car(null, "434555asdf", "1E23342", "Ford Tranzit", new Double(332223));
		car1 = this.carManager.createCar(car1);
		
		Rental rental1 = new Rental(null, driver, car1, new BigDecimal(10000), null, null);
		Rental rental2 = new Rental(null, driver, car1, new BigDecimal(10000), null, null);
		
		//test funciton
		int carOnStock = this.rentalManager.findAllCarsOnStock().size();
		rental1 = this.rentalManager.createRental(rental1);
		assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.", carOnStock - 1, this.rentalManager.findAllCarsOnStock().size());
		this.rentalManager.endRental(rental1);
		assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.", carOnStock, this.rentalManager.findAllCarsOnStock().size());
		rental2 = this.rentalManager.createRental(rental2);
		assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.", carOnStock - 1, this.rentalManager.findAllCarsOnStock().size());
		this.rentalManager.endRental(rental2);
		assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.", carOnStock, this.rentalManager.findAllCarsOnStock().size());
		
		//delete object
		this.carManager.deleteCar(car1);
		this.driverManager.deleteDriver(driver);
		this.rentalManager.deleteRental(rental1);
		this.rentalManager.deleteRental(rental2);
	}

	/**
	 * Test of isCarFree method, of class RentalsManager.
	 */
	@Test
	public void testIsCarFree() {
		System.out.println("isCarFree");
		
		Car car = new Car(null, "434555asdf", "1E23342", "Ford Tranzit", new Double(332223));
		car = this.carManager.createCar(car);
		Driver driver = new Driver(null, "Jarek", "Novy", "XYeee&#&##777");
		driver = this.driverManager.createDriver(driver);
		Rental rental = new Rental(null, driver, car, new BigDecimal(10000), null, null);
		
		assertFalse("Auto nebylo dosud pujceno.", this.rentalManager.isCarFree(car));
		
		rental = this.rentalManager.createRental(rental);
		assertTrue("Auto je pujceno.", this.rentalManager.isCarFree(car));
		this.rentalManager.endRental(rental);
		assertFalse("Auto neni pujceno.", this.rentalManager.isCarFree(car));
	}
}
