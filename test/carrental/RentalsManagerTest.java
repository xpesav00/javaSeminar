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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.dbcp.BasicDataSource;

public class RentalsManagerTest {

    private RentalsManager rentalManager;
    private CarsManager carManager;
    private DriversManager driverManager;
    private Rental rental;
    private Rental rentalBad1;
    private Rental rentalBad2;
    private Rental rentalBad3;
    private Rental rentalBad4;
    private Rental rentalBad5;
    private Rental rentalBad6;
    private Rental rentalBad7;
    private Connection connection = null;
    private Statement st;
    private BasicDataSource dataSource;

    public RentalsManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws NamingException, SQLException {

        //connect to db
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.apache.derby.jdbc.Driver169");
        dataSource.setUrl("jdbc:derby://localhost:1527/javaSeminar");
        dataSource.setUsername("developer");
        dataSource.setPassword("developer");
        
        try {
            DBUtils.executeSqlScript(dataSource, CarRental.class.getResource("../common/createTables.sql"));
        } catch  (Exception ex){
            DBUtils.executeSqlScript(dataSource, CarRental.class.getResource("../common/dropTables.sql"));
            DBUtils.executeSqlScript(dataSource, CarRental.class.getResource("../common/createTables.sql"));            
        }
        
        //set rentalManager
        this.rentalManager = new RentalsManager(dataSource);

        //set carManager
        this.carManager = new CarsManager(dataSource);

        //set driverManager
        this.driverManager = new DriversManager(dataSource);

        Car car = new Car(null, "CZ334433", "1E03344", "Ford Tranzit", new Double(230000));
        car = this.carManager.createCar(car);
        Driver driver = new Driver(null, "Karel", "Vomacka", "CZ223235233");
        driver = this.driverManager.createDriver(driver);

        this.rental = new Rental(null, driver, car, BigDecimal.ZERO, Calendar.getInstance(), Calendar.getInstance());
        this.rentalBad1 = new Rental(null, null, car, BigDecimal.ZERO, Calendar.getInstance(), Calendar.getInstance());
        this.rentalBad2 = new Rental(null, new Driver(), null, BigDecimal.ZERO, Calendar.getInstance(), Calendar.getInstance());
        this.rentalBad3 = new Rental(null, new Driver(), new Car(), new BigDecimal(-1), Calendar.getInstance(), Calendar.getInstance());
        this.rentalBad4 = new Rental(null, new Driver(), car, new BigDecimal(130), Calendar.getInstance(), Calendar.getInstance());
        this.rentalBad5 = new Rental(null, driver, new Car(), new BigDecimal(222), Calendar.getInstance(), Calendar.getInstance());
        this.rentalBad6 = new Rental(null, driver, new Car(), new BigDecimal(222), null, Calendar.getInstance());
        this.rentalBad7 = new Rental(null, driver, new Car(), new BigDecimal(222), Calendar.getInstance(), null);


    }

    @After
    public void tearDown() throws SQLException {
        this.rentalManager = null;
        this.carManager = null;
        this.driverManager = null;
        this.rental = null;
        this.rentalBad1 = null;
        this.rentalBad2 = null;
        this.rentalBad3 = null;
        this.rentalBad4 = null;
        this.rentalBad5 = null;
        this.rentalBad6 = null;
        this.rentalBad7 = null;
        DBUtils.executeSqlScript(dataSource, CarRental.class.getResource("../common/dropTables.sql"));
    }

    /**
     * Test of createRental method, of class RentalsManager.
     */
    @Test
    public void testCreateRental() {
        System.out.println("createRental");

        int rentalCount = this.rentalManager.findAllRentals().size();

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
            this.rentalManager.createRental(this.rentalBad4);
            fail("Create new rental with null driver id.");
        } catch (IllegalArgumentException ex) {
        }

        //null car object
        try {
            this.rentalManager.createRental(this.rentalBad2);
            fail("Create new rental with null car object.");
        } catch (IllegalArgumentException ex) {
        }

        //null car id
        try {
            this.rentalManager.createRental(this.rentalBad5);
            fail("Create new rental with null car id.");
        } catch (IllegalArgumentException ex) {
        }

        //price is less then zero
        try {
            this.rentalManager.createRental(this.rentalBad3);
            fail("Create new Rental with price less then zero.");
        } catch (IllegalArgumentException ex) {
        }

        //startDate is null
        try {
            this.rentalManager.createRental(this.rentalBad6);
            fail("Create new Rental with null startDate.");
        } catch (IllegalArgumentException ex) {
        }

        //expected end date is null
        try {
            this.rentalManager.createRental(this.rentalBad7);
            fail("Create new Rental with null expectedEndDate.");
        } catch (IllegalArgumentException ex) {
        }

        //create rental
        Rental rentalTmp;
        rentalTmp = this.rentalManager.createRental(this.rental);
        assertNotNull("Vraceny prvek je null.", rentalTmp);
        assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp.getId());
        assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental, rentalTmp);
        assertSame("Vlozeny a vytazeny prvek nemaji stejnou referenci.", this.rental, rentalTmp);


        //numeric control
        assertEquals("Nesedi pocet aut na sklade.", rentalCount + 1, this.rentalManager.findAllRentals().size());

        //remove rental
        this.rentalManager.deleteRental(this.rental);

        //numeric control
        assertEquals("Nesedi pocet aut na sklade.", rentalCount, this.rentalManager.findAllRentals().size());
    }

    /**
     * Test of deleteRental method, of class RentalsManager.
     */
    @Test
    public void testDeleteRental() {
        System.out.println("deleteRental");

        //get current size
        int rentalCountStart = this.rentalManager.findAllRentals().size();

        //delete null pointer rental
        try {
            this.rentalManager.deleteRental(null);
            fail("No failures to null pointer");
        } catch (IllegalArgumentException ex) {
        }

        //delete with null id
        try {
            this.rentalManager.deleteRental(new Rental());
            fail("No failures to null id.");
        } catch (IllegalArgumentException ex) {
        }

        //create rental
        Rental rentalTmp = this.rentalManager.createRental(this.rental);

        //test rentalTmp
        assertNotNull("Vraceny prvek je null.", rentalTmp);
        assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp.getId());
        this.rentalManager.deleteRental(rentalTmp);
        assertEquals("Nesedi pocet aut na sklade.", rentalCountStart, this.rentalManager.findAllRentals().size());

        if (this.rentalManager.findRentalById(rentalTmp.getId()) != null) {
            fail("Podarilo se nalezt odstanenou vypujcku");
        }
    }

    /**
     * Test of findAllRentCars method, of class RentalsManager.
     */
    @Test
    public void testFindAllRentCars() {
        System.out.println("findAllRentCars");

        //get current size
        int startRentalCount = this.rentalManager.findAllRentals().size();

        //create rentalTmp
        Rental rentalTmp = this.rentalManager.createRental(this.rental);
        assertNotNull("Vraceny prvek nema prirazene id.", rentalTmp.getId());
        assertEquals("Nesedi pocet aut na sklade.", startRentalCount + 1, this.rentalManager.findAllRentals().size());
        assertNotSame("Vlozeny a vytayeny prvek nemaji stejnou referenci", this.rental, this.rentalManager.findRentalById(this.rental.getId()));
        assertEquals("Vlozeny a vytazeny prvek neni stejny.", this.rental, this.rentalManager.findRentalById(this.rental.getId()));
        this.rentalManager.deleteRental(rentalTmp);

        assertNull("Podarilo se nalezt odstanenou vypujcku", this.rentalManager.findRentalById(rentalTmp.getId()));

        //numeric control
        assertEquals("Nesedi pocet vypujcek.", startRentalCount, this.rentalManager.findAllRentals().size());
    }

    /**
     * Test of endRental method, of class RentalsManager.
     */
    @Test
    public void testEndRental() {
        System.out.println("endRental");

        int rentalCountStart = this.rentalManager.findAllRentals().size();

        // rental null pointer
        try {
            this.rentalManager.updateRental(null);
            fail("Update Rental with null pointer.");
        } catch (Exception ex) {
        }

        // rental null pointer
        try {
            this.rentalManager.updateRental(this.rental);
            fail("Update Rental without id.");
        } catch (Exception ex) {
        }

        // car pointer is null
        try {
            this.rentalManager.updateRental(this.rentalBad1);
            fail("Update Rental with car null pointer.");
        } catch (Exception ex) {
        }

        // driver pointer is null
        try {
            this.rentalManager.updateRental(this.rentalBad2);
            fail("Update Rental with driver null pointer.");
        } catch (Exception ex) {
        }

        // zero price
        try {
            this.rentalManager.updateRental(this.rentalBad3);
            fail("Update Rental with zero price.");
        } catch (Exception ex) {
        }


        //create new rental
        Rental rentalTmp = this.rentalManager.createRental(this.rental);

        //update
        rentalTmp.setEndTime(Calendar.getInstance());

        int rentalCount = this.rentalManager.findAllRentals().size();

        assertEquals(0, rentalManager.findAllCarsOnStock().size());
        assertEquals(1, rentalManager.findAllRentedCars().size());
        this.rentalManager.updateRental(rentalTmp);
        assertEquals("Nesedi pocet vypujcek.", rentalCount, this.rentalManager.findAllRentals().size());
        assertEquals("Nesouhlasi identicke objekty.", rentalTmp, this.rentalManager.findRentalById(rentalTmp.getId()));

        assertEquals(1, rentalManager.findAllCarsOnStock().size());
        assertEquals(0, rentalManager.findAllRentedCars().size());
        assertEquals(1, carManager.findAllCars().size());

        this.rentalManager.deleteRental(rentalTmp);

        //numeric control
        assertEquals("Nesedi pocet vypujcek.", rentalCountStart, this.rentalManager.findAllRentals().size());
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
        } catch (Exception ex) {
        }

        // rental.id null pointer
        try {
            this.rentalManager.updateRental(this.rental);
            fail("Update Rental without id.");
        } catch (Exception ex) {
        }

        // car pointer is null
        try {
            this.rentalManager.updateRental(this.rentalBad1);
            fail("Update Rental with car null pointer.");
        } catch (Exception ex) {
        }

        // car.id pointer is null
        try {
            this.rentalManager.updateRental(this.rentalBad4);
            fail("Update Rental with car.id null pointer.");
        } catch (Exception ex) {
        }

        // driver pointer is null
        try {
            this.rentalManager.updateRental(this.rentalBad2);
            fail("Update Rental with driver null pointer.");
        } catch (Exception ex) {
        }

        // driver.id pointer is null
        try {
            this.rentalManager.updateRental(this.rentalBad5);
            fail("Update Rental with driver.id null pointer.");
        } catch (Exception ex) {
        }

        // zero price
        try {
            this.rentalManager.updateRental(this.rentalBad3);
            fail("Update Rental with zero price.");
        } catch (Exception ex) {
        }

        // startDate
        try {
            this.rentalManager.updateRental(this.rentalBad6);
            fail("Update Rental with null start date.");
        } catch (Exception ex) {
        }

        // expectedEndDate
        try {
            this.rentalManager.updateRental(this.rentalBad7);
            fail("Update Rental with null expected end date.");
        } catch (Exception ex) {
        }

        //create new rental
        Rental rentalTmp = this.rentalManager.createRental(this.rental);

        //update
        rentalTmp.setPrice(new BigDecimal(1000));
        rentalTmp.setDriver(new Driver(new Long(1)));
        rentalTmp.setCar(new Car(new Long(1)));
        rentalTmp.setStartTime(Calendar.getInstance());
        rentalTmp.setExpectedEndTime(Calendar.getInstance());

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
        Rental tmpRental = this.rentalManager.createRental(this.rental);
        Rental tmpRentalFind = this.rentalManager.findRentalById(tmpRental.getId());
        assertEquals("Objekty nejsou stejne.", tmpRental, tmpRentalFind);
        assertNotSame("Objekty maji stejnou referenci.", tmpRental, tmpRentalFind);

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
        Rental rentalTmp = this.rentalManager.createRental(this.rental);
        assertEquals("Nesedi pocet vypujcek.", rentalCountStart + 1, this.rentalManager.findAllRentals().size());

        //test all rentals
        List<Rental> rentalList = this.rentalManager.findAllRentals();
        assertEquals("Nesedi pocet vypujcek.", rentalCountStart + 1, rentalList.size());
        assertTrue("Databaze neobsahuje vytvoreny objekt.", rentalList.contains(rentalTmp));

        //delete rental
        this.rentalManager.deleteRental(rentalTmp);
        rentalList = this.rentalManager.findAllRentals();
        assertFalse("Databaze obsahuje vymazany objekt.", rentalList.contains(rentalTmp));
        assertEquals("Nesedi pocet vypujcek.", rentalCountStart, this.rentalManager.findAllRentals().size());
    }

    /**
     * Test of findCarHistoryOfRental method, of class RentalsManager.
     */
    @Test
    public void testFindHistoryOfRentalByCar() {
        System.out.println("findHistoryOfRentalByCar");

        //create Rental
        Driver driver1 = new Driver(null, "Jarek", "Novy", "XYeee&#&##777");
        Driver driver2 = new Driver(null, "Jaroslav", "Velky", "XY34534777");
        Driver driver3 = new Driver(null, "Pavel", "Uzky", "XYee3332777");
        driver1 = this.driverManager.createDriver(driver1);
        driver2 = this.driverManager.createDriver(driver2);
        driver3 = this.driverManager.createDriver(driver3);

        Car car = new Car(null, "4353vfasd", "1E23322", "Ford Tranzit", new Double(332223));
        car = this.carManager.createCar(car);

        Rental rental1 = new Rental(null, driver1, car, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        Rental rental2 = new Rental(null, driver2, car, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        Rental rental3 = new Rental(null, driver3, car, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        Rental rental4 = new Rental(null, driver1, car, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());

        //test car history
        assertEquals("Auto nebylo dosud pujceno", 0, this.rentalManager.findHistoryOfRental(car).size());
        Rental rental1Tmp = this.rentalManager.createRental(rental1);
        assertEquals("Nesedi pocet pujceni auta", 1, this.rentalManager.findHistoryOfRental(car).size());
        this.rentalManager.endRental(rental1Tmp);
        Rental rental2Tmp = this.rentalManager.createRental(rental2);
        assertEquals("Nesedi pocet pujceni auta", 2, this.rentalManager.findHistoryOfRental(car).size());
        this.rentalManager.endRental(rental2Tmp);
        Rental rental3Tmp = this.rentalManager.createRental(rental3);
        assertEquals("Nesedi pocet pujceni auta", 3, this.rentalManager.findHistoryOfRental(car).size());
        this.rentalManager.endRental(rental3Tmp);
        Rental rental4Tmp = this.rentalManager.createRental(rental4);
        assertEquals("Nesedi pocet pujceni auta", 4, this.rentalManager.findHistoryOfRental(car).size());
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
    public void testFindHistoryOfRentalByDriver() {
        System.out.println("findDriverHistoryOfRentalByDriver");

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

        Rental rental1 = new Rental(null, driver, car1, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        Rental rental2 = new Rental(null, driver, car2, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        Rental rental3 = new Rental(null, driver, car3, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        Rental rental4 = new Rental(null, driver, car4, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());

        //test car history
        assertEquals("Ridis si dosud pujcil auto", 0, this.rentalManager.findHistoryOfRental(driver).size());
        Rental rental1Tmp = this.rentalManager.createRental(rental1);
        assertEquals("Nesedi pocet ridicovych pujceni.", 1, this.rentalManager.findHistoryOfRental(driver).size());
        this.rentalManager.endRental(rental1Tmp);
        Rental rental2Tmp = this.rentalManager.createRental(rental2);
        assertEquals("Nesedi pocet ridicovych pujceni.", 2, this.rentalManager.findHistoryOfRental(driver).size());
        this.rentalManager.endRental(rental2Tmp);
        Rental rental3Tmp = this.rentalManager.createRental(rental3);
        assertEquals("Nesedi pocet ridicovych pujceni.", 3, this.rentalManager.findHistoryOfRental(driver).size());
        this.rentalManager.endRental(rental3Tmp);
        Rental rental4Tmp = this.rentalManager.createRental(rental4);
        assertEquals("Nesedi pocet ridicovych pujceni.", 4, this.rentalManager.findHistoryOfRental(driver).size());
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
    public void testFindAllRentedCars() {
        Car car1 = new Car(null, "434555asdf", "1E23342", "Ford Tranzit", new Double(332223));
        Car car2 = new Car(null, "435twe444d", "1E23442", "Ford Fiesta", new Double(343444));
        Car car3 = new Car(null, "werwert454", "1E25662", "Ford Mondeo", new Double(33334));
        Car car4 = new Car(null, "wddsddd454", "1E25222", "Ford Focus", new Double(33334));
        car1 = this.carManager.createCar(car1);
        car2 = this.carManager.createCar(car2);
        car3 = this.carManager.createCar(car3);
        car4 = this.carManager.createCar(car4);
        Driver driver = new Driver(null, "Jarek", "Novy", "XYeee&#&##777");
        driver = this.driverManager.createDriver(driver);
        Rental rental1 = new Rental(null, driver, car1, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        Rental rental2 = new Rental(null, driver, car2, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        Rental rental3 = new Rental(null, driver, car3, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        Rental rental4 = new Rental(null, driver, car4, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());

        rentalManager.createRental(rental1);
        assertEquals("Nesouhlasi pocet zapujcenych aut", 1, this.rentalManager.findAllRentedCars().size());
        rentalManager.createRental(rental2);
        rentalManager.createRental(rental3);
        rentalManager.createRental(rental4);
        rentalManager.deleteRental(rental1);
        assertEquals("Nesouhlasi pocet zapujcenych aut", 3, this.rentalManager.findAllRentedCars().size());
        rentalManager.endRental(rental2);
        rentalManager.endRental(rental3);
        assertTrue("auto je vraceno", rentalManager.isCarFree(car3));
        assertTrue("auto je vraceno", rentalManager.isCarFree(car1));
        assertFalse("auto stale pujceno", rentalManager.isCarFree(car4));
        assertEquals("Nesouhlasi pocet zapujcenych aut", 1, this.rentalManager.findAllRentedCars().size());
        assertEquals("Nesouhlasi pocet zapujcenych aut",
                carManager.findAllCars().size() - rentalManager.findAllRentedCars().size(), this.rentalManager.findAllCarsOnStock().size());
        
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

        Rental rental1 = new Rental(null, driver, car1, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());
        Rental rental2 = new Rental(null, driver, car1, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());

        //test funciton
        int carOnStock = this.rentalManager.findAllCarsOnStock().size();

        assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.",
                this.carManager.findAllCars().size(), this.rentalManager.findAllCarsOnStock().size());
        rental1 = this.rentalManager.createRental(rental1);
        assertEquals("Nesouhlasi pocet zapujcenych aut.",
                this.rentalManager.findAllRentals().size(), this.rentalManager.findAllRentedCars().size());

        assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.", carOnStock - 1, this.rentalManager.findAllCarsOnStock().size());
        this.rentalManager.endRental(rental1);
        assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.", carOnStock, this.rentalManager.findAllCarsOnStock().size());
        rental2 = this.rentalManager.createRental(rental2);
        assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.", carOnStock - 1, this.rentalManager.findAllCarsOnStock().size());

        Car car4 = new Car(null, "wddsddd454", "1E25222", "Ford Focus", new Double(33334));
        this.carManager.createCar(car4);
        assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.", carOnStock, this.rentalManager.findAllCarsOnStock().size());

        this.rentalManager.endRental(rental2);
        assertEquals("Nesouhlasi pocet aut na sklade k zapujceni.", carOnStock + 1, this.rentalManager.findAllCarsOnStock().size());

        //delete object        
        this.rentalManager.deleteRental(rental1);
        this.rentalManager.deleteRental(rental2);
        this.carManager.deleteCar(car1);
        this.driverManager.deleteDriver(driver);
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
        Rental rental = new Rental(null, driver, car, new BigDecimal(10000), Calendar.getInstance(), Calendar.getInstance());

        assertTrue("Auto nebylo dosud pujceno.", this.rentalManager.isCarFree(car));

        rental = this.rentalManager.createRental(rental);
        assertFalse("Auto je pujceno.", this.rentalManager.isCarFree(car));
        this.rentalManager.endRental(rental);
        assertTrue("Auto neni pujceno.", this.rentalManager.isCarFree(car));
    } 
}
