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
 * @description Trida testujici CarManager
 * @package carrental
 * @file CarManagerTest.java
 * @author Filip Krepinsky
 * @email suomiy@gmail.com
 * @date 15. 3. 2013
 */
public class CarManagerTest {

    private CarManager manager;
    private Car car1, car2, result;

    public CarManagerTest() {
    }

    @Before
    public void setUp() {
        manager = new CarManager();
        car1 = new Car();
        car2 = new Car();

        car1.setName("test car no. 1");
        car1.setSpz("ASD15KU");
        car1.setVin("BASDGADG1536ASD");
        car1.setMileage(0.6);
        car1.setId(manager.findNextCarId());

        car2.setName("test car no. 2");
        car2.setSpz("ASDAFDKU");
        car2.setVin("ATEAFF1536ASD");
        car2.setMileage(1241535.6);


    }

    @After
    public void tearDown() {
        manager = null;
        car1 = null;
        car2 = null;
        result = null;

    }

    /**
     * Test of createCar method, of class CarManager.
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
        car1.setId(null);
        try {
            manager.createCar(car1);
            fail("car with null id");
        } catch (IllegalArgumentException ex) {
        }


        car1.setId(new Long(-20));
        
        try {
            manager.createCar(car1);
            fail("creates car with negative id");
        } catch (IllegalArgumentException ex) {
        }

        car1.setId(manager.findNextCarId());
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


        car2.setMileage(0.0);
        car2.setId(car1.getId());
        try {
            manager.createCar(car2);
            fail("creates car with same id");
        } catch (IllegalArgumentException ex) {
        }

        car2.setId(manager.findNextCarId());
        manager.createCar(car2);

        result = manager.findCarById(car1.getId());

        assertCarEquals(result, car1);

    }

    /**
     * Test of deleteCar method, of class CarManager.
     */
    @Test
    public void testDeleteCar() {
        List<Car> cars;

        manager.createCar(car1);
        car2.setId(manager.findNextCarId());
        manager.createCar(car2);

        assertNotNull(manager.findCarById(car1.getId()));
        assertNotNull(manager.findCarById(car2.getId()));

        manager.deleteCar(car1);

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

        manager.createCar(car2);
        cars = manager.findAllCars();
        assertTrue("manager isnt empty", cars.isEmpty());
    }

    /**
     * Test of findAllCars method, of class CarManager.
     */
    @Test
    public void testFindAllCars() {
        List<Car> source = new ArrayList<>();

        assertEquals(source, manager.findAllCars());

        manager.createCar(car1);
        source.add(car1);
        assertEquals(source, manager.findAllCars());

        car2.setId(manager.findNextCarId());
        manager.createCar(car2);
        source.add(car2);
        assertEquals(source, manager.findAllCars());

        manager.deleteCar(car1);
        source.remove(car1);
        assertEquals(source, manager.findAllCars());

        manager.deleteCar(car2);
        source.remove(car2);
        assertEquals(source, manager.findAllCars());
    }

    /**
     * Test of updateCar method, of class CarManager.
     */
    @Test
    public void testUpdateCar() {
        manager.createCar(car1);
        car2.setId(manager.findNextCarId());
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
            car1.setId(new Long(-1));
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
     * Test of findCarById method, of class CarManager.
     */
    @Test
    public void testFindCarById() {
        assertNull(manager.findCarById(manager.findNextCarId()));

        try {

            manager.findCarById(new Long(-1));
            fail();
        } catch (IllegalArgumentException ex) {
        }
        
        try {

            manager.findCarById(null);
            fail();
        } catch (IllegalArgumentException ex) {
        }


        manager.createCar(car1);
        assertEquals(car1, manager.findCarById(car1.getId()));

        car2.setId(manager.findNextCarId());
        manager.createCar(car2);
        assertEquals(car1, manager.findCarById(car1.getId()));
        assertEquals(car2, manager.findCarById(car2.getId()));

    }

    @Test
    public void testFindNextId() {
        Long store = manager.findNextCarId();

        assertNotNull("return null", manager.findNextCarId());
       
        manager.createCar(car1);
         
        assertFalse("returns same id",store.equals(manager.findNextCarId()));
        assertTrue("negative id", manager.findNextCarId().longValue() >= 0);


    }

    private static void assertCarEquals(Car expected, Car actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getMileage(), actual.getMileage());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSpz(), actual.getSpz());
        assertEquals(expected.getVin(), actual.getVin());
    }
}
