/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Trida zajistuje testovani objektu RentalManager
 * @package carrental
 * @file RentalManagerTest.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */

package carrental;

import java.math.BigDecimal;
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
	private Rental rental1;
	private Rental rental2;
	private Rental rental3;
	private Rental rental4;
	private Rental rentalBad1;
	private Rental rentalBad2;
	private Rental rentalBad3;
	
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
	 * Test of createRental method, of class RentalManager.
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
	 * Test of deleteRental method, of class RentalManager.
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
			this.rentalManager.findRentalById(rentalTmp1.getId().intValue());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		
		//test rentalTmp2
		rentalCount = this.rentalManager.findAllRentals().size();
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp2.getId());
		this.rentalManager.deleteRental(rentalTmp2);
		assertEquals("Nesedi pocet aut na sklade.", rentalCount - 1, this.rentalManager.findAllRentals().size());
		try {
			this.rentalManager.findRentalById(rentalTmp2.getId().intValue());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		
		//test rentalTmp3
		rentalCount = this.rentalManager.findAllRentals().size();
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp3.getId());
		this.rentalManager.deleteRental(rentalTmp3);
		assertEquals("Nesedi pocet aut na sklade.", rentalCount - 1, this.rentalManager.findAllRentals().size());
		try {
			this.rentalManager.findRentalById(rentalTmp3.getId().intValue());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		
		//test rentalTmp4
		rentalCount = this.rentalManager.findAllRentals().size();
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp4.getId());
		this.rentalManager.deleteRental(rentalTmp4);
		assertEquals("Nesedi pocet aut na sklade.", rentalCount - 1, this.rentalManager.findAllRentals().size());
		try {
			this.rentalManager.findRentalById(rentalTmp4.getId().intValue());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
	
		//numeric control
		assertEquals("Nesedi pocet aut na sklade.", rentalCountStart, this.rentalManager.findAllRentals().size());
		
		//fail("The test case is a prototype.");
	}
	
	/**
	 * Test of findAllRentCars method, of class RentalManager.
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
		assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci", this.rental1, this.rentalManager.findRentalById(this.rental1.getId().intValue()));
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental1, this.rentalManager.findRentalById(this.rental1.getId().intValue()));
		this.rentalManager.deleteRental(rentalTmp1);
		try {
			this.rentalManager.findRentalById(rentalTmp1.getId().intValue());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		assertEquals("Nesedi pocet vypujcek.", rentalCount, this.rentalManager.findAllRentals().size());
		
		
		//create rentalTmp2
		rentalCount = this.rentalManager.findAllRentals().size();
		Rental rentalTmp2 = this.rentalManager.createRental(this.rental2);
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp2.getId());
		assertEquals("Nesedi pocet aut na sklade.", rentalCount + 1, this.rentalManager.findAllRentals().size());
		assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci", this.rental2, this.rentalManager.findRentalById(this.rental2.getId().intValue()));
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental2, this.rentalManager.findRentalById(this.rental2.getId().intValue()));
		this.rentalManager.deleteRental(rentalTmp2);
		try {
			this.rentalManager.findRentalById(rentalTmp2.getId().intValue());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		assertEquals("Nesedi pocet vypujcek.", rentalCount, this.rentalManager.findAllRentals().size());
		
		//create rentalTmp3
		rentalCount = this.rentalManager.findAllRentals().size();
		Rental rentalTmp3 = this.rentalManager.createRental(this.rental3);
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp3.getId());
		assertEquals("Nesedi pocet aut na sklade.", rentalCount + 1, this.rentalManager.findAllRentals().size());
		assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci", this.rental3, this.rentalManager.findRentalById(this.rental3.getId().intValue()));
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental3, this.rentalManager.findRentalById(this.rental3.getId().intValue()));
		this.rentalManager.deleteRental(rentalTmp3);
		try {
			this.rentalManager.findRentalById(rentalTmp3.getId().intValue());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		assertEquals("Nesedi pocet vypujcek.", rentalCount, this.rentalManager.findAllRentals().size());
		
		
		//create rentalTmp4
		rentalCount = this.rentalManager.findAllRentals().size();
		Rental rentalTmp4 = this.rentalManager.createRental(this.rental4);
		assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp4.getId());
		assertEquals("Nesedi pocet aut na sklade.", rentalCount + 1, this.rentalManager.findAllRentals().size());
		assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci", this.rental4, this.rentalManager.findRentalById(this.rental4.getId().intValue()));
		assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental4, this.rentalManager.findRentalById(this.rental4.getId().intValue()));
		this.rentalManager.deleteRental(rentalTmp4);
		try {
			this.rentalManager.findRentalById(rentalTmp4.getId().intValue());
			fail("Podarilo se nalezt odstanenou vypujcku");
		} catch(Exception e) {
		}
		assertEquals("Nesedi pocet vypujcek.", rentalCount, this.rentalManager.findAllRentals().size());
		
		
		//numeric control
		assertEquals("Nesedi pocet aut na sklade.", startRentalCount, this.rentalManager.findAllRentals().size());
		
		//fail("The test case is a prototype.");
	}
	
	/**
	 * Test of endRental method, of class RentalManager.
	 */
	@Test
	public void testEndRental() {
		System.out.println("endRental");
		fail("The test case is a prototype.");
	}


	/**
	 * Test of updateRental method, of class RentalManager.
	 */
	@Test
	public void testUpdateRental() {
		System.out.println("updateRental");
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findRentalById method, of class RentalManager.
	 */
	@Test
	public void testFindRentalById() {
		System.out.println("findRentalById");
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findAllRentals method, of class RentalManager.
	 */
	@Test
	public void testFindAllRentals() {
		System.out.println("findAllRentals");
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findDriverByCar method, of class RentalManager.
	 */
	@Test
	public void testFindDriverByCar() {
		System.out.println("findDriverByCar");
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findCarByDriver method, of class RentalManager.
	 */
	@Test
	public void testFindCarByDriver() {
		System.out.println("findCarByDriver");
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findCarHistoryOfRental method, of class RentalManager.
	 */
	@Test
	public void testFindCarHistoryOfRental() {
		System.out.println("findCarHistoryOfRental");
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findDriverHistoryOfRental method, of class RentalManager.
	 */
	@Test
	public void testFindDriverHistoryOfRental() {
		System.out.println("findDriverHistoryOfRental");
		fail("The test case is a prototype.");
	}

	/**
	 * Test of findAllCarsOnStock method, of class RentalManager.
	 */
	@Test
	public void testFindAllCarsOnStock() {
		System.out.println("findAllCarsOnStock");
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isCarFree method, of class RentalManager.
	 */
	@Test
	public void testIsCarFree() {
		System.out.println("isCarFree");
		fail("The test case is a prototype.");
	}
}
