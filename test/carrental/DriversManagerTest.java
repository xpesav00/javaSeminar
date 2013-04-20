/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Trida testujici DriversManager
 * @package carrental
 * @file DriversManagerTest.java
 * @author Filip Krepinsky
 * @email suomiy@gmail.com
 * @date 15. 3. 2013
 */

package carrental;

import common.DBUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DriversManagerTest {

    private DriversManager manager;
    private Driver driver1, driver2, result;
    private BasicDataSource dataSource;

    public DriversManagerTest() {
    }

    @Before
    public void setUp() throws SQLException, NamingException {
        //connect to db
	dataSource = new BasicDataSource();
	dataSource.setUrl("jdbc:derby:memory;create=true");
	
        try {
            DBUtils.executeSqlScript(dataSource, CarRental.class.getResource("../common/createTables.sql"));
        } catch  (Exception ex){
            DBUtils.executeSqlScript(dataSource, CarRental.class.getResource("../common/dropTables.sql"));
            DBUtils.executeSqlScript(dataSource, CarRental.class.getResource("../common/createTables.sql"));            
        }

        manager = new DriversManager(dataSource);
        driver1 = new Driver();
        driver2 = new Driver();

        driver1.setName("test subject");
        driver1.setSurname("no. 1");
        driver1.setLicenseId("AEG154683");

        driver2.setName("test subject as well");
        driver2.setSurname("no. 2");
        driver2.setLicenseId("EAO2547842");


    }

    @After
    public void tearDown() throws SQLException {
        manager = null;
        driver1 = null;
        driver2 = null;
        result = null;
        DBUtils.executeSqlScript(dataSource, CarRental.class.getResource("../common/dropTables.sql"));
        
    }

    /**
     * Test of createDriver method, of class DriversManager.
     */
    @Test
    public void testCreateDriver() {

        try {
            manager.createDriver(null);
            fail("driver is null");
        } catch (IllegalArgumentException ex) {
        }

        driver1.setId(new Long(20));
        try {
            manager.createDriver(driver1);
            fail("driver with not null id");
        } catch (IllegalArgumentException ex) {
        }

        driver1.setId(null);
        driver1.setName(null);
        try {
            manager.createDriver(driver1);
            fail("driver with null name");
        } catch (IllegalArgumentException ex) {
        }

        driver1.setSurname(null);
        driver1.setName("test subject");
        try {
            manager.createDriver(driver1);
            fail("driver with null surname");
        } catch (IllegalArgumentException ex) {
        }

        driver1.setLicenseId(null);
        driver1.setSurname("no. 1");
        try {
            manager.createDriver(driver1);
            fail("driver with null licenseId");
        } catch (IllegalArgumentException ex) {
        }

        driver1.setLicenseId("AEG154683");


        result = manager.createDriver(driver1);
        assertEquals("doesn't return same object", result, driver1);
        assertEquals("doesn't return same object", manager.findDriverById(driver1.getId()), driver1);

        manager.createDriver(driver2);

        result = manager.findDriverById(driver1.getId());

        assertDriverEquals(result, driver1);

    }

    /**
     * Test of deleteDriver method, of class DriversManager.
     */
    @Test
    public void testDeleteDriver() {
        List<Driver> drivers;

        Driver driverTmp1 = manager.createDriver(driver1);
        Driver driverTmp2 = manager.createDriver(driver2);

        assertNotNull(manager.findDriverById(driverTmp1.getId()));
        assertNotNull(manager.findDriverById(driverTmp2.getId()));

        manager.deleteDriver(driverTmp1);

        assertNull(manager.findDriverById(driverTmp1.getId()));
        assertNotNull(manager.findDriverById(driverTmp2.getId()));
        drivers = manager.findAllDrivers();
        assertEquals("number of drivers doesn't match", drivers.size(), 1);

        try {
            manager.deleteDriver(null);
            fail("cant delete: null");
        } catch (IllegalArgumentException ex) {
        }

        try {

            manager.deleteDriver(driverTmp1);
            fail("cant delete non existing driver");
        } catch (IllegalArgumentException ex) {
        }
        driverTmp1.setId(null);
        try {

            manager.deleteDriver(driverTmp1);
            fail("cant delete non existing driver");
        } catch (IllegalArgumentException ex) {
        }
	
        manager.deleteDriver(driverTmp2);
        drivers = manager.findAllDrivers();
        assertTrue("manager isnt empty", drivers.isEmpty());
    }

    /**
     * Test of updateDriver method, of class DriversManager.
     */
    @Test
    public void testUpdateDriver() {

        driver1 = manager.createDriver(driver1);
        driver2 = manager.createDriver(driver2);
        Long driverId = driver1.getId();

        driver1.setLicenseId("265165AS");
        manager.updateDriver(driver1);
        result = manager.findDriverById(driverId);
        assertDriverEquals(driver1, result);

        driver1.setSurname("Jakob");
        manager.updateDriver(driver1);
        result = manager.findDriverById(driverId);
        assertDriverEquals(driver1, result);

        driver1.setName("Hay");
        manager.updateDriver(driver1);
        result = manager.findDriverById(driverId);
        assertDriverEquals(driver1, result);


        assertDriverEquals(manager.findDriverById(driver2.getId()), driver2);

        assertEquals("number of drivers doesn't match", manager.findAllDrivers().size(), 2);

        driverId = driver2.getId();

        try {
            manager.updateDriver(null);
            fail();
        } catch (IllegalArgumentException ex) {
        }

        try {
            driver1 = manager.findDriverById(driverId);
            driver1.setId(null);
            manager.updateDriver(driver1);
            fail();
        } catch (IllegalArgumentException ex) {
        }
	
         try { 
            driver1 = manager.findDriverById(driverId);
            driver1.setId(Long.MAX_VALUE);
            manager.updateDriver(driver1);
            fail();
        } catch (IllegalArgumentException ex) {
        }

        try {
            driver1 = manager.findDriverById(driverId);
            driver1.setLicenseId(null);
            manager.updateDriver(driver1);
            fail();
        } catch (IllegalArgumentException ex) {
        }

        try {
            driver1 = manager.findDriverById(driverId);
            driver1.setSurname(null);
            manager.updateDriver(driver1);
            fail();
        } catch (IllegalArgumentException ex) {
        }

        try {
            driver1 = manager.findDriverById(driverId);
            driver1.setName(null);
            manager.updateDriver(driver1);
            fail();
        } catch (IllegalArgumentException ex) {
        }

    }

    /**
     * Test of findAllDrivers method, of class DriversManager.
     */
    @Test
    public void testFindAllDrivers() {
        List<Driver> source = new ArrayList<>();

        assertEquals(source, manager.findAllDrivers());

        manager.createDriver(driver1);
        source.add(driver1);
        assertEquals(source, manager.findAllDrivers());

        manager.createDriver(driver2);
        source.add(driver2);
        assertEquals(source, manager.findAllDrivers());

        source.remove(driver1);
        manager.deleteDriver(driver1);
        assertEquals(source, manager.findAllDrivers());

        source.remove(driver2);
        manager.deleteDriver(driver2);
        assertEquals(source, manager.findAllDrivers());

    }

    /**
     * Test of findDriverById method, of class DriversManager.
     */
    @Test
    public void testFindDriverById() {
        assertNull(manager.findDriverById(Long.MAX_VALUE));
	
        try {
            manager.findDriverById(null);
            fail();
        } catch (IllegalArgumentException ex) {
        }

        manager.createDriver(driver1);
        assertEquals(driver1, manager.findDriverById(driver1.getId()));

        manager.createDriver(driver2);
        assertEquals(driver1, manager.findDriverById(driver1.getId()));
        assertEquals(driver2, manager.findDriverById(driver2.getId()));

        driver2.setId(Long.MAX_VALUE);
        assertNull("not created driver must not be found",
                manager.findDriverById(driver2.getId()));
    }

    private static void assertDriverEquals(Driver expected, Driver actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getLicenceId(), actual.getLicenceId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
    }
}