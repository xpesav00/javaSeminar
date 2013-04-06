/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Trida zajistuje testovani objektu RentalsManager
 * @package carrental
 * @file RentalsManagerTest.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */
package carrental;

import common.DBUtils;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.dbcp.BasicDataSource;

public class RentalsManagerTest {

    private RentalsManager rentalManager;
    private CarsManager carManager;
    private DriversManager driverManager;
    private Car car1, car2, car3;
    private Driver driver1, driver2, driver3;
    private Rental rental1, rental2, rental3;
    private Rental rentalBad1, rentalBad2, rentalBad3, rentalBad4, rentalBad5, rentalBad6, validRental;
    private BasicDataSource dataSource;

    public RentalsManagerTest() {
    }

    @Before
    public void setUp() throws NamingException, SQLException {

        //connect to db
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.apache.derby.jdbc.Driver169");
        dataSource.setUrl("jdbc:derby:memory;create=true"); //  jdbc:derby://localhost:1527/javaSeminar
        dataSource.setUsername("developer");
        dataSource.setPassword("developer");

        try {
            DBUtils.executeSqlScript(dataSource, CarRental.class.getResource("../common/createTables.sql"));
        } catch (Exception ex) {
            DBUtils.executeSqlScript(dataSource, CarRental.class.getResource("../common/dropTables.sql"));
            DBUtils.executeSqlScript(dataSource, CarRental.class.getResource("../common/createTables.sql"));
        }

        //set rentalManager
        this.rentalManager = new RentalsManager(dataSource);
        //set carManager
        this.carManager = new CarsManager(dataSource);
        //set driverManager
        this.driverManager = new DriversManager(dataSource);
        //create cars
        car1 = new Car(null, "CZ334433", "1E03344", "Ford Tranzit", new Double(230000));
        car2 = new Car(null, "435twe444d", "1E23442", "Ford Fiesta", new Double(343444));
        car3 = new Car(null, "werwert454", "1E25662", "Ford Mondeo", new Double(33334));
        carManager.createCar(car1);
        carManager.createCar(car2);
        carManager.createCar(car3);
        //create drivers
        driver1 = new Driver(null, "Karel", "Vomacka", "CZ223235233");
        driver2 = new Driver(null, "Jaroslav", "Velky", "XY34534777");
        driver3 = new Driver(null, "Pavel", "Uzky", "XYee3332777");
        driverManager.createDriver(driver1);
        driverManager.createDriver(driver2);
        driverManager.createDriver(driver3);
    }

    @After
    public void tearDown() throws SQLException {
        rentalManager = null;
        carManager = null;
        driverManager = null;
        car1 = null;
        car2 = null;
        car3 = null;
        driver1 = null;
        driver2 = null;
        driver3 = null;
        rental1 = null;
        rental2 = null;
        rental3 = null;
        rentalBad1 = null;
        rentalBad2 = null;
        rentalBad3 = null;
        rentalBad4 = null;
        rentalBad5 = null;
        rentalBad6 = null;
        validRental = null;        
        DBUtils.executeSqlScript(dataSource, CarRental.class.getResource("../common/dropTables.sql"));
    }

    /**
     * Test of createRental method, of class RentalsManager.
     */
    @Test
    public void testCreateRental() {
        Long id = null;        

        prepareBadRentals();
        //null rental object
        try {
            this.rentalManager.createRental(null);
            fail("Create new rental with null rental object.");
        } catch (IllegalArgumentException ex) {
        }

        //null driver object
        try {
            this.rentalManager.createRental(this.rentalBad1);
            fail("Create new rental with null driver object.");
        } catch (IllegalArgumentException ex) {
        }

        //null driver id
        try {
            id = driver3.getId();
            driver3.setId(null);
            this.rentalManager.createRental(this.validRental);
            fail("Create new rental with null driver id.");
        } catch (IllegalArgumentException ex) {
        }
        driver3.setId(id);

        //null car object
        try {
            this.rentalManager.createRental(this.rentalBad2);
            fail("Create new rental with null car object.");
        } catch (IllegalArgumentException ex) {
        }

        //null car id
        try {
            id = car3.getId();
            car3.setId(null);
            this.rentalManager.createRental(this.validRental);
            fail("Create new rental with null car id.");
        } catch (IllegalArgumentException ex) {
        }
        car3.setId(id);

        //price is less then zero
        try {
            this.rentalManager.createRental(this.rentalBad3);
            fail("Create new Rental with price less then zero.");
        } catch (IllegalArgumentException ex) {
        }

        //startDate is null
        try {
            this.rentalManager.createRental(this.rentalBad4);
            fail("Create new Rental with null startDate.");
        } catch (IllegalArgumentException ex) {
        }

        //expected end date is null
        try {
            this.rentalManager.createRental(this.rentalBad5);
            fail("Create new Rental with null expectedEndDate.");
        } catch (IllegalArgumentException ex) {
        }

        try {
            this.rentalManager.createRental(this.rentalBad6);
            fail("Create new Rental with expected end date not greater than start date");
        } catch (IllegalArgumentException ex) {
        }

        //create rental
        Rental rentalTmp;
        rentalTmp = this.rentalManager.createRental(validRental);
        assertNotNull("Vraceny prvek je null.", rentalTmp);
        assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp.getId());
        assertEquals("Vlozeny a vytazeny prvek neni stejny.", validRental, rentalTmp);
        assertSame("Vlozeny a vytazeny prvek nemaji stejnou referenci.", validRental, rentalTmp);
        assertEquals("doesn't return same object", rentalManager.findRentalById(validRental.getId()), validRental);
        assertRentalEquals(validRental, rentalTmp);

        //numeric control
        assertEquals("Nesedi pocet aut na sklade.", 1, this.rentalManager.findAllRentals().size());
        createTestRentals();
        assertEquals("Nesedi pocet aut na sklade.", 3, this.rentalManager.findAllRentals().size());
        //remove rental
        this.rentalManager.deleteRental(validRental);

        //numeric control
        assertEquals("Nesedi pocet aut na sklade.", 2, this.rentalManager.findAllRentals().size());
    }

    /**
     * Test of deleteRental method, of class RentalsManager.
     */
    @Test
    public void testDeleteRental() {        
        Long id;
        createTestRentals();
        assertNotNull(rentalManager.findRentalById(rental1.getId()));
        assertNotNull(rentalManager.findRentalById(rental2.getId()));
        id = rental1.getId();
        rental1.setId(null);

        //delete null pointer rental
        try {
            this.rentalManager.deleteRental(null);
            fail("No failures to null pointer");
        } catch (IllegalArgumentException ex) {
        }

        //delete with null id
        try {
            this.rentalManager.deleteRental(rental1);
            fail("No failures to null id.");
        } catch (IllegalArgumentException ex) {
        }
        rental1.setId(id);

        assertEquals("Nesedi pocet vypujcek.", 2, this.rentalManager.findAllRentals().size());
        this.rentalManager.deleteRental(rental1);
        assertEquals("Nesedi pocet vypujcek.", 1, this.rentalManager.findAllRentals().size());
        if (this.rentalManager.findRentalById(id) != null) {
            fail("Podarilo se nalezt odstanenou vypujcku");
        }
        rental2.setId(Long.MAX_VALUE);
        try {

            rentalManager.deleteRental(rental1);
            fail("cant delete non existing car");
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
     * Test of endRental method, of class RentalsManager.
     */
    @Test
    public void testEndRental() {        

        createTestRentals();
        assertEquals("Nesedi pocet vypujcek.", 2, this.rentalManager.findAllRentals().size());
        assertEquals(1, rentalManager.findAllCarsOnStock().size());

        assertNull(rental1.getEndTime());
        rentalManager.endRental(rental1);
        assertNotNull(rental1.getEndTime());
        assertTrue(rental1.getExpectedEndTime().after(rental1.getStartTime()));
        assertTrue(rental1.getEndTime().after(rental1.getStartTime()));
        assertNull(rental2.getEndTime());

        assertEquals("Nesedi pocet vypujcek.", 2, this.rentalManager.findAllRentals().size());
        assertEquals(2, rentalManager.findAllCarsOnStock().size());

    }

    /**
     * Test of updateRental method, of class RentalsManager.
     */
    @Test
    public void testUpdateRental() {      
        Long id = null;
        prepareBadRentals();
        createTestRentals();

        // rental null pointer
        try {
            this.rentalManager.updateRental(null);
            fail("Update Rental with null pointer.");
        } catch (Exception ex) {
        }


        // rental.id null pointer
        try {
            id = rental2.getId();
            rental2.setId(null);
            this.rentalManager.updateRental(rental2);
            fail("Update Rental without id.");
        } catch (Exception ex) {
        }
        rental2.setId(id);

        // car pointer is null
        try {
            this.rentalManager.updateRental(this.rentalBad1);
            fail("Update Rental with car null pointer.");
        } catch (Exception ex) {
        }

        // car.id pointer is null
        try {
            id = car3.getId();
            car3.setId(null);
            this.rentalManager.updateRental(this.validRental);
            fail("Update Rental with car.id null pointer.");
        } catch (Exception ex) {
        }
        car3.setId(id);

        // driver pointer is null
        try {
            this.rentalManager.updateRental(this.rentalBad2);
            fail("Update Rental with driver null pointer.");
        } catch (Exception ex) {
        }

        // driver.id pointer is null
        try {
            id = driver3.getId();
            driver3.setId(null);
            this.rentalManager.updateRental(this.validRental);
            fail("Update Rental with driver.id null pointer.");
        } catch (Exception ex) {
        }
        driver3.setId(id);

        // negative price
        try {
            this.rentalManager.updateRental(this.rentalBad3);
            fail("Update Rental with zero price.");
        } catch (Exception ex) {
        }

        // startDate
        try {
            this.rentalManager.updateRental(this.rentalBad4);
            fail("Update Rental with null start date.");
        } catch (Exception ex) {
        }

        // expectedEndDate
        try {
            this.rentalManager.updateRental(this.rentalBad5);
            fail("Update Rental with null expected end date.");
        } catch (Exception ex) {
        }
        try {
            this.rentalManager.createRental(this.rentalBad6);
            fail("Create new Rental with expected end date not greater than start date");
        } catch (IllegalArgumentException ex) {
        }
        id = rental1.getId();

        //    rental1.setDriver(new Driver(new Long(1)));
        //  rentalManager.updateRental(rental1);
        rental3 = rentalManager.findRentalById(id);
        assertRentalEquals(rental1, rental3);

        rental1.setCar(new Car(new Long(1)));
        rentalManager.updateRental(rental1);
        rental3 = rentalManager.findRentalById(id);
        assertRentalEquals(rental1, rental3);

        rental1.getExpectedEndTime().add(Calendar.DAY_OF_MONTH, 3);
        rentalManager.updateRental(rental1);
        rental3 = rentalManager.findRentalById(id);
        assertRentalEquals(rental1, rental3);

        rental1.setStartTime(Calendar.getInstance());
        rentalManager.updateRental(rental1);
        rental3 = rentalManager.findRentalById(id);
        assertRentalEquals(rental1, rental3);

        assertEquals("Nesedi pocet vypujcek.", 2, this.rentalManager.findAllRentals().size());

        rental1.setEndTime(Calendar.getInstance());
        rental1.getEndTime().add(Calendar.DAY_OF_MONTH, 3);
        rentalManager.updateRental(rental1);
        rental3 = rentalManager.findRentalById(id);
        assertRentalEquals(rental1, rental3);

        assertEquals("Nesedi pocet vypujcek.", 1,
                rentalManager.activeRentals(this.rentalManager.findAllRentals()).size());
        this.rentalManager.deleteRental(rental1);
        assertEquals("Nesedi pocet vypujcek.", 1, this.rentalManager.findAllRentals().size());
    }

    /**
     * Test of findRentalById method, of class RentalsManager.
     */
    @Test
    public void testFindRentalById() {       

        //is is null
        try {
            this.rentalManager.findRentalById(null);
            fail("Internal Error: Funkce vratila hodnotu s nulovy parametrem.");
        } catch (IllegalArgumentException ex) {
        }

        //is is less then zero
        try {
            this.rentalManager.findRentalById(new Long(-1));
            fail("Internal Error: Funkce vratila hodnotu se zapornym parametrem.");
        } catch (IllegalArgumentException ex) {
        }

        //create new rental
        createTestRentals();
        Rental tmpRentalFind = this.rentalManager.findRentalById(rental1.getId());       
        assertRentalEquals(rental1, tmpRentalFind);
        assertNotSame("Objekty maji stejnou referenci.", rental1, tmpRentalFind);

        //remove rental
        Long id = rental1.getId();
        this.rentalManager.deleteRental(rental1);
        assertNotNull(rentalManager.findRentalById(rental2.getId()));
        assertNull(rentalManager.findRentalById(id));
    }

    /**
     * Test of findAllRentals method, of class RentalsManager.
     */
    @Test
    public void testFindAllRentals() {        

        assertEquals("Nesedi pocet vypujcek.", 0, this.rentalManager.findAllRentals().size());
        createTestRentals();
        assertEquals("Nesedi pocet vypujcek.", 2, this.rentalManager.findAllRentals().size());

        //test all rentals
        List<Rental> rentalList = this.rentalManager.findAllRentals();
        assertEquals("Nesedi pocet vypujcek.", 2, rentalList.size());
        assertTrue("Databaze neobsahuje vytvoreny objekt.", rentalList.contains(rental1));

        //delete rental
        this.rentalManager.deleteRental(rental1);
        rentalList = this.rentalManager.findAllRentals();
        assertFalse("Databaze obsahuje vymazany objekt.", rentalList.contains(rental1));
        assertEquals("Nesedi pocet vypujcek.", 1, this.rentalManager.findAllRentals().size());
        this.rentalManager.deleteRental(rental2);
        rentalList = this.rentalManager.findAllRentals();
        assertFalse("Databaze obsahuje vymazany objekt.", rentalList.contains(rental2));
        assertEquals("Nesedi pocet vypujcek.", 0, this.rentalManager.findAllRentals().size());
    }

    /**
     * Test of findCarHistoryOfRental method, of class RentalsManager.
     */
    @Test
    public void testFindHistoryOfRentalByCar() {        

        rental1 = new Rental(null, driver1, car1, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        rental2 = new Rental(null, driver2, car1, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        rental3 = new Rental(null, driver3, car1, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        changeExpectedEndTime();

        //test car history
        assertEquals("Auto nebylo dosud pujceno", 0, this.rentalManager.findHistoryOfRental(car1).size());
        Rental rental1Tmp = this.rentalManager.createRental(rental1);
        assertEquals("Nesedi pocet pujceni auta", 1, this.rentalManager.findHistoryOfRental(car1).size());
        this.rentalManager.endRental(rental1Tmp);
        Rental rental2Tmp = this.rentalManager.createRental(rental2);
        assertEquals("Nesedi pocet pujceni auta", 2, this.rentalManager.findHistoryOfRental(car1).size());
        this.rentalManager.endRental(rental2Tmp);
        Rental rental3Tmp = this.rentalManager.createRental(rental3);
        assertEquals("Nesedi pocet pujceni auta", 3, this.rentalManager.findHistoryOfRental(car1).size());
        this.rentalManager.endRental(rental3Tmp);
        rental1.setId(null);
        this.rentalManager.createRental(rental1);
        assertEquals("Nesedi pocet pujceni auta", 4, this.rentalManager.findHistoryOfRental(car1).size());
    }

    /**
     * Test of findDriverHistoryOfRental method, of class RentalsManager.
     */
    @Test
    public void testFindHistoryOfRentalByDriver() {        

        rental1 = new Rental(null, driver1, car1, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        rental2 = new Rental(null, driver1, car2, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        rental3 = new Rental(null, driver1, car3, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        changeExpectedEndTime();

        //test car history
        assertEquals("Ridis si dosud pujcil auto", 0, this.rentalManager.findHistoryOfRental(driver1).size());
        Rental rental1Tmp = this.rentalManager.createRental(rental1);
        assertEquals("Nesedi pocet ridicovych pujceni.", 1, this.rentalManager.findHistoryOfRental(driver1).size());
        this.rentalManager.endRental(rental1Tmp);
        Rental rental2Tmp = this.rentalManager.createRental(rental2);
        assertEquals("Nesedi pocet ridicovych pujceni.", 2, this.rentalManager.findHistoryOfRental(driver1).size());
        this.rentalManager.endRental(rental2Tmp);
        Rental rental3Tmp = this.rentalManager.createRental(rental3);
        assertEquals("Nesedi pocet ridicovych pujceni.", 3, this.rentalManager.findHistoryOfRental(driver1).size());
        this.rentalManager.endRental(rental3Tmp);
    }

    /**
     * Test of findAllCarsOnStock method, of class RentalsManager.
     */
    @Test
    public void testFindAllRentedCars() {

        rental1 = new Rental(null, driver1, car1, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        rental2 = new Rental(null, driver2, car2, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        rental3 = new Rental(null, driver3, car3, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        changeExpectedEndTime();

        rentalManager.createRental(rental1);
        assertEquals("Nesouhlasi pocet zapujcenych aut", 1, this.rentalManager.findAllRentedCars().size());
        rentalManager.createRental(rental2);
        rentalManager.createRental(rental3);
        rentalManager.deleteRental(rental1);
        assertEquals("Nesouhlasi pocet zapujcenych aut", 2, this.rentalManager.findAllRentedCars().size());
        rentalManager.endRental(rental3);
        assertTrue("auto je vraceno", rentalManager.isCarFree(car3));
        assertTrue("auto je vraceno", rentalManager.isCarFree(car1));
        assertFalse("auto stale pujceno", rentalManager.isCarFree(car2));
        assertEquals("Nesouhlasi pocet zapujcenych aut", 1, this.rentalManager.findAllRentedCars().size());
        assertEquals("Nesouhlasi pocet zapujcenych aut",
                carManager.findAllCars().size() - rentalManager.findAllRentedCars().size(), this.rentalManager.findAllCarsOnStock().size());

    }

    /**
     * Test of findAllCarsOnStock method, of class RentalsManager.
     */
    @Test
    public void testFindAllCarsOnStock() {

        assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.",
                this.carManager.findAllCars().size(), this.rentalManager.findAllCarsOnStock().size());
        createTestRentals();
        assertEquals("Nesouhlasi pocet zapujcenych aut.",
                this.rentalManager.findAllRentals().size(), this.rentalManager.findAllRentedCars().size());

        assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.", 1, this.rentalManager.findAllCarsOnStock().size());
        this.rentalManager.endRental(rental1);
        assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.", 2, this.rentalManager.findAllCarsOnStock().size());

        Car car4 = new Car(null, "wddsddd454", "1E25222", "Ford Focus", new Double(33334));
        this.carManager.createCar(car4);
        assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.", 3, this.rentalManager.findAllCarsOnStock().size());

        this.rentalManager.endRental(rental2);
        assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.", 4, this.rentalManager.findAllCarsOnStock().size());
    }

    /**
     * Test of isCarFree method, of class RentalsManager.
     */
    @Test
    public void testIsCarFree() {

        assertTrue("Auto nebylo dosud pujceno.", this.rentalManager.isCarFree(car1));
        createTestRentals();
        assertFalse("Auto je pujceno.", this.rentalManager.isCarFree(car1));
        this.rentalManager.endRental(rental1);
        assertTrue("Auto neni pujceno.", this.rentalManager.isCarFree(car1));
        assertFalse("Auto je pujceno.", this.rentalManager.isCarFree(car2));
    }

    @Test
    public void testActiveRentals() {
        createTestRentals();
        assertEquals("neshoduje se pocet aktivnich vypucek", 2, rentalManager.activeRentals(rentalManager.findAllRentals()).size());
        rentalManager.endRental(rental1);
        assertEquals("neshoduje se pocet aktivnich vypucek", 1, rentalManager.activeRentals(rentalManager.findAllRentals()).size());
        rentalManager.endRental(rental2);
        assertEquals("neshoduje se pocet aktivnich vypucek", 0, rentalManager.activeRentals(rentalManager.findAllRentals()).size());

    }
    
    //setup methods
    private void createTestRentals() {

        rental1 = new Rental(null, driver1, car1, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        rental2 = new Rental(null, driver2, car2, new BigDecimal(20000), Calendar.getInstance(), Calendar.getInstance());
        rental1.getExpectedEndTime().add(Calendar.DAY_OF_MONTH, 3);
        rental2.getExpectedEndTime().add(Calendar.DAY_OF_MONTH, 3);
        rentalManager.createRental(rental1);
        rentalManager.createRental(rental2);
    }

    private void prepareBadRentals() {
        //null driver
        this.rentalBad1 = new Rental(null, null, car3, BigDecimal.ZERO, Calendar.getInstance(), Calendar.getInstance());
        rentalBad1.getExpectedEndTime().add(Calendar.DAY_OF_MONTH, 3);
        // null car
        this.rentalBad2 = new Rental(null, driver3, null, BigDecimal.ZERO, Calendar.getInstance(), Calendar.getInstance());
        rentalBad2.getExpectedEndTime().add(Calendar.DAY_OF_MONTH, 3);
        //negative price
        this.rentalBad3 = new Rental(null, driver3, car3, new BigDecimal(-1), Calendar.getInstance(), Calendar.getInstance());
        rentalBad3.getExpectedEndTime().add(Calendar.DAY_OF_MONTH, 3);
        // null start date
        this.rentalBad4 = new Rental(null, driver3, car3, new BigDecimal(222), null, Calendar.getInstance());
        rentalBad4.getExpectedEndTime().add(Calendar.DAY_OF_MONTH, 3);
        // null expected end date
        this.rentalBad5 = new Rental(null, driver3, car3, new BigDecimal(222), Calendar.getInstance(), null);
        //expected date must be grater than end date
        Calendar cal = Calendar.getInstance();
        this.rentalBad6 = new Rental(null, driver3, car3, new BigDecimal(222), cal, cal);
        // null driver id    and null car id           
        this.validRental = new Rental(null, driver3, car3, new BigDecimal(130), Calendar.getInstance(), Calendar.getInstance());
        validRental.getExpectedEndTime().add(Calendar.DAY_OF_MONTH, 3);
    }

    private void changeExpectedEndTime() {
        rental1.getExpectedEndTime().add(Calendar.DAY_OF_MONTH, 3);
        rental2.getExpectedEndTime().add(Calendar.DAY_OF_MONTH, 3);
        rental3.getExpectedEndTime().add(Calendar.DAY_OF_MONTH, 3);
    }

    private static void assertRentalEquals(Rental expected, Rental actual) {
        Calendar c1, c2;
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCar(), actual.getCar());
        assertEquals(expected.getDriver(), actual.getDriver());
        assertEquals(expected.getEndTime(), actual.getEndTime());
        assertEquals(expected.getExpectedEndTime(), actual.getExpectedEndTime());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getStartTime(), actual.getStartTime());

    }
}
