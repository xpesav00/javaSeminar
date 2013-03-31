/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package carrental;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Trida testujici CarsManager
 * @package carrental
 * @file CarsManagerTest.java
 * @author Filip Krepinsky
 * @email suomiy@gmail.com
 * @date 15. 3. 2013
 */
public class CarsManagerTest {

    private CarsManager manager;
    private Car car1, car2, result;
    private Connection connection = null;
    private Statement st;

    public CarsManagerTest() {
    }

    @Before
    public void setUp() throws SQLException, NamingException {
        //connect to db
        BasicDataSource dataSource = new BasicDataSource();
	dataSource.setDriverClassName("org.apache.derby.jdbc.Driver169");
	dataSource.setUrl("jdbc:derby://localhost:1527/javaSeminar");
	dataSource.setUsername("developer");
	dataSource.setPassword("developer");
	this.connection = dataSource.getConnection();
	    
	    
        st = connection.createStatement();
        st.execute("DELETE FROM CAR"); 
        st.execute("ALTER TABLE CAR ALTER COLUMN id RESTART WITH 1");//reseting id counter
        // st.execute("TRUNCATE TABLE CAR");
        
        manager = new CarsManager(dataSource);
        car1 = new Car();
        car2 = new Car();

        car1.setName("test car no. 1");
        car1.setSpz("ASD15KU");
        car1.setVin("BASDGADG1536ASD");
        car1.setMileage(0.6);

        car2.setName("test car no. 2");
        car2.setSpz("ASDAFDKU");
        car2.setVin("ATEAFF1536ASD");
        car2.setMileage(1241535.6);


    }

    @After
    public void tearDown() throws SQLException {
        manager = null;
        car1 = null;
        car2 = null;
        result = null;

        st.execute("DELETE FROM CAR"); 
        st.execute("ALTER TABLE CAR ALTER COLUMN id RESTART WITH 1");//reseting id counter
        st.close();
        connection.close();

    }

    /**
     * Test of createCar method, of class CarsManager.
     */
    @Test
    public void testCreateCar() {
        try {
            manager.createCar(null);
            fail("car is null");
        } catch (IllegalArgumentException ex) {
        }

        car1.setMileage(-1.0);

        try {
            manager.createCar(car1);
            fail("creates negative milleage");
        } catch (IllegalArgumentException ex) {
        }

        car1.setMileage(1232.9);

        car1.setId(new Long(25));
        try {
            manager.createCar(car1);
            fail("creates car with not null id");
        } catch (IllegalArgumentException ex) {
        }

        car1.setId(null);
        car1.setName(null);
        try {
            manager.createCar(car1);
            fail("creates car with null name");
        } catch (IllegalArgumentException ex) {
        }

        car1.setSpz(null);
        car1.setName("test car no. 1");
        try {
            manager.createCar(car1);
            fail("creates car with null spz");
        } catch (IllegalArgumentException ex) {
        }

        car1.setSpz("ASD15KU");
        car1.setVin(null);
        try {
            manager.createCar(car1);
            fail("creates car with null vin");
        } catch (IllegalArgumentException ex) {
        }

        car1.setVin("BASDGADG1536ASD");


        result = manager.createCar(car1);
        assertEquals("doesn't return same object", result, car1);
        assertEquals("doesn't return same object", manager.findCarById(car1.getId()), car1);

        manager.createCar(car2);

        result = manager.findCarById(car1.getId());

        assertCarEquals(result, car1);

    }

    /**
     * Test of deleteCar method, of class CarsManager.
     */
    @Test
    public void testDeleteCar() {
        List<Car> cars;

        manager.createCar(car1);
        manager.createCar(car2);

        assertNotNull(manager.findCarById(car1.getId()));
        assertNotNull(manager.findCarById(car2.getId()));

        manager.deleteCar(car1);

        car1.setId(Long.MAX_VALUE);
        assertNull(manager.findCarById(car1.getId()));
        assertNotNull(manager.findCarById(car2.getId()));
        cars = manager.findAllCars();
        assertEquals("number of cars doesn't match", cars.size(), 1);

        try {
            manager.deleteCar(null);
            fail("cant delete null");
        } catch (IllegalArgumentException ex) {
        }

        try {

            manager.deleteCar(car1);
            fail("cant delete non existing car");
        } catch (IllegalArgumentException ex) {
        }
        car1.setId(null);
        try {

            manager.deleteCar(car1);
            fail("cant delete non existing car");
        } catch (IllegalArgumentException ex) {
        }

        manager.deleteCar(car2);
        cars = manager.findAllCars();
        assertTrue("manager isnt empty", cars.isEmpty());       
    }

    /**
     * Test of updateCar method, of class CarsManager.
     */
    @Test
    public void testUpdateCar() {
        manager.createCar(car1);
        manager.createCar(car2);
        Long carId = car1.getId();

        car1 = manager.findCarById(carId);
        car1.setVin("ABCD");
        manager.updateCar(car1);
        result = manager.findCarById(carId);
        assertCarEquals(car1, result);

        car1 = manager.findCarById(carId);
        car1.setSpz("BHOP12");
        manager.updateCar(car1);
        result = manager.findCarById(carId);
        assertCarEquals(car1, result);

        car1 = manager.findCarById(carId);
        car1.setName("test car no. 20");
        manager.updateCar(car1);
        result = manager.findCarById(carId);
        assertCarEquals(car1, result);

        car1 = manager.findCarById(carId);
        car1.setMileage(2563.30);
        manager.updateCar(car1);
        result = manager.findCarById(carId);
        assertCarEquals(car1, result);


        assertCarEquals(manager.findCarById(car2.getId()), car2);

        assertEquals("number of cars doesn't match", manager.findAllCars().size(), 2);

        carId = car2.getId();

        try {
            manager.updateCar(null);
            fail();
        } catch (IllegalArgumentException ex) {
        }

        try {
            car1 = manager.findCarById(carId);
            car1.setId(null);
            manager.updateCar(car1);
            fail();
        } catch (IllegalArgumentException ex) {
        }

        try {
            car1 = manager.findCarById(carId);
            car1.setId(new Long(Long.MAX_VALUE));
            manager.updateCar(car1);
            fail();
        } catch (IllegalArgumentException ex) {
        }

        try {
            car1 = manager.findCarById(carId);
            car1.setMileage(-0.01);
            manager.updateCar(car1);
            fail();
        } catch (IllegalArgumentException ex) {
        }

        try {
            car1 = manager.findCarById(carId);
            car1.setVin(null);
            manager.updateCar(car1);
            fail();
        } catch (IllegalArgumentException ex) {
        }

        try {
            car1 = manager.findCarById(carId);
            car1.setSpz(null);
            manager.updateCar(car1);
            fail();
        } catch (IllegalArgumentException ex) {
        }

        try {
            car1 = manager.findCarById(carId);
            car1.setName(null);
            manager.updateCar(car1);
            fail();
        } catch (IllegalArgumentException ex) {
        }

    }
    
    /**
     * Test of findAllCars method, of class CarsManager.
     */
    @Test
    public void testFindAllCars() {
        List<Car> source = new ArrayList<>();

        assertEquals(source, manager.findAllCars());

        manager.createCar(car1);
        source.add(car1);
        assertEquals(source, manager.findAllCars());

        manager.createCar(car2);
        source.add(car2);
        assertEquals(source, manager.findAllCars());

        source.remove(car1);
        manager.deleteCar(car1);
        assertEquals(source, manager.findAllCars());

        source.remove(car2);
        manager.deleteCar(car2);
        assertEquals(source, manager.findAllCars());
    }
    
    /**
     * Test of findCarById method, of class CarsManager.
     */
    @Test
    public void testFindCarById() {
        assertNull(manager.findCarById(Long.MAX_VALUE));

        /*        try {  //only if negative id's are forbiden
         manager.findCarById(new Long(-1));
         fail();
         } catch (IllegalArgumentException ex) {
         }*/

        try {

            manager.findCarById(null);
            fail();
        } catch (IllegalArgumentException ex) {
        }


        manager.createCar(car1);
        assertEquals(car1, manager.findCarById(car1.getId()));

        manager.createCar(car2);
        assertEquals(car1, manager.findCarById(car1.getId()));
        assertEquals(car2, manager.findCarById(car2.getId()));

        car2.setId(Long.MAX_VALUE);
        assertNull("not created car must not be found",
                manager.findCarById(car2.getId()));
    }

    private static void assertCarEquals(Car expected, Car actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getMileage(), actual.getMileage());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSpz(), actual.getSpz());
        assertEquals(expected.getVin(), actual.getVin());
    }
}
