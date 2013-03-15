/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package carrental;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Trida testujici DriverManager
 * @package carrental
 * @file DriverManagerTest.java
 * @author Filip Krepinsky
 * @email suomiy@gmail.com
 * @date 15. 3. 2013
 */
public class DriverManagerTest {

    private DriverManager manager;
    private Driver driver1, driver2, result;

    public DriverManagerTest() {
    }

    @Before
    public void setUp() {
        manager = new DriverManager();
        driver1 = new Driver();
        driver2 = new Driver();

        driver1.setName("test subject");
        driver1.setSurname("no. 1");
        driver1.setLicenceId("AEG154683");
        driver1.setId(manager.getNextDriverId());

        driver2.setName("test subject as well");
        driver2.setSurname("no. 2");
        driver2.setLicenceId("EAO2547842");        


    }

    @After
    public void tearDown() {
        manager = null;
        driver1 = null;
        driver2 = null;
        result = null;
    }

    /**
     * Test of createDriver method, of class DriverManager.
     */
    @Test
    public void testCreateDriver() {

        try {
            manager.createDriver(null);
            fail("driver is null");
        } catch (IllegalArgumentException ex) {
        }
        
        driver1.setId(null);
        try {
            manager.createDriver(driver1);
            fail("driver with null id");
        } catch (IllegalArgumentException ex) {
        }


        driver1.setId(new Long(-20));
        try {
            manager.createDriver(driver1);
            fail("driver with negative id");
        } catch (IllegalArgumentException ex) {
        }

        driver1.setId(manager.getNextDriverId());
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

        driver1.setLicenceId(null);
        driver1.setSurname("no. 1");
        try {
            manager.createDriver(driver1);
            fail("driver with null licenseId");
        } catch (IllegalArgumentException ex) {
        }

        driver1.setLicenceId("AEG154683");


        result = manager.createDriver(driver1);
        assertEquals("doesn't return same object", result, driver1);
        assertEquals("doesn't return same object", manager.findDriverById(driver1.getId()), driver1);



        driver2.setId(driver1.getId());
        try {
            manager.createDriver(driver2);
            fail("creates driver with same id");
        } catch (IllegalArgumentException ex) {
        }

        driver2.setId(manager.getNextDriverId());
        manager.createDriver(driver2);

        result = manager.findDriverById(driver1.getId());

        assertDriverEquals(result, driver1);

    }

    /**
     * Test of deleteDriver method, of class DriverManager.
     */
    @Test
    public void testDeleteDriver() {
       List<Driver> drivers;
       
        manager.createDriver(driver1);
        driver2.setId(manager.getNextDriverId());
        manager.createDriver(driver2);

        assertNotNull(manager.findDriverById(driver1.getId()));
        assertNotNull(manager.findDriverById(driver2.getId()));

        manager.deleteDriver(driver1);

        assertNull(manager.findDriverById(driver1.getId()));
        assertNotNull(manager.findDriverById(driver2.getId()));
        drivers = manager.findAllDrivers();
        assertEquals("number of drivers doesn't match", drivers.size(), 1);

        try {
            manager.deleteDriver(null);
            fail("cant delete: null");
        } catch (IllegalArgumentException ex) {
        }

        try {

            manager.deleteDriver(driver1);
            fail("cant delete non existing car");
        } catch (IllegalArgumentException ex) {
        }

        manager.deleteDriver(driver2);
        drivers = manager.findAllDrivers();
        assertTrue("manager isnt empty", drivers.isEmpty());
    }

    /**
     * Test of updateDriver method, of class DriverManager.
     */
    @Test
    public void testUpdateDriver() {
        
        manager.createDriver(driver1);
        driver2.setId(manager.getNextDriverId());
        manager.createDriver(driver2);
        Long driverId = driver1.getId();

        driver1 = manager.findDriverById(driverId);
        driver1.setLicenceId("265165AS");
        manager.updateDriver(driver1);
        result = manager.findDriverById(driverId);
        assertDriverEquals(driver1, result);

        driver1 = manager.findDriverById(driverId);
        driver1.setSurname("Jakob");
        manager.updateDriver(driver1);
        result = manager.findDriverById(driverId);
        assertDriverEquals(driver1, result);

        driver1 = manager.findDriverById(driverId);
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
            driver1.setId(new Long(-1));
            manager.updateDriver(driver1);
            fail();
        } catch (IllegalArgumentException ex) {
        }

        

        try {
            driver1 = manager.findDriverById(driverId);
            driver1.setLicenceId(null);
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
     * Test of findAllDrivers method, of class DriverManager.
     */
    @Test
    public void testFindAllDrivers() {
        List<Driver> source = new ArrayList<>();

        assertEquals(source, manager.findAllDrivers());

        manager.createDriver(driver1);
        source.add(driver1);
        assertEquals(source, manager.findAllDrivers());

        driver2.setId(manager.getNextDriverId());
        manager.createDriver(driver2);
        source.add(driver2);
        assertEquals(source, manager.findAllDrivers());

        manager.deleteDriver(driver1);
        source.remove(driver1);
        assertEquals(source, manager.findAllDrivers());

        manager.deleteDriver(driver2);
        source.remove(driver2);
        assertEquals(source, manager.findAllDrivers());
        
    }

    /**
     * Test of findDriverById method, of class DriverManager.
     */
    @Test
    public void testFindDriverById() {
        assertNull(manager.findDriverById(manager.getNextDriverId()));

        try {
            manager.findDriverById(new Long(-1));
            fail();
        } catch (IllegalArgumentException ex) {
        }
        
        try {
            manager.findDriverById(null);
            fail();
        } catch (IllegalArgumentException ex) {
        }


        manager.createDriver(driver1);
        assertEquals(driver1, manager.findDriverById(driver1.getId()));

        driver2.setId(manager.getNextDriverId());
        manager.createDriver(driver2);
        assertEquals(driver1, manager.findDriverById(driver1.getId()));
        assertEquals(driver2, manager.findDriverById(driver2.getId()));
      
    }

    /**
     * Test of getNextDriverId method, of class DriverManager.
     */
    @Test
    public void testGetNextDriverId() {
        Long store = manager.getNextDriverId();

        assertNotNull("return null", manager.getNextDriverId());
       
        manager.createDriver(driver1);
        assertNotSame("returns same id",store, manager.getNextDriverId());
        assertTrue("negative id", manager.getNextDriverId().longValue() >= 0);
    }

    private static void assertDriverEquals(Driver expected, Driver actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getLicenceId(), actual.getLicenceId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
    }
}