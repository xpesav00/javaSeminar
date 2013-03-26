/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Trida zajistuje manazera pro praci s objektem Car
 * @package carrental
 * @file CarsManager.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */
package carrental;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author honzap
 */
public class CarsManager implements ICarManager {

    public static final Logger logger = Logger.getLogger(CarsManager.class.getName());
    private Connection connection;

    public CarsManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Car createCar(Car car) {
                
        if (car == null) {
            throw new IllegalArgumentException("car is null");
        }
        if (car.getId() != null) {
            throw new IllegalArgumentException("car id is already set");
        }
        if (car.getMileage() < 0) {
            throw new IllegalArgumentException("car has negative mileage");
        }
        if (car.getName() == null) {
            throw new IllegalArgumentException("car name is null");
        }
        if (car.getSpz() == null) {
            throw new IllegalArgumentException("car spz is null");
        }
        if (car.getVin() == null) {
            throw new IllegalArgumentException("car vin is null");
        }

        try (PreparedStatement st = connection.prepareStatement(
                "INSERT INTO CAR(vin,spz,name,mileage) "
                + "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, car.getVin());
            st.setString(2, car.getSpz());
            st.setString(3, car.getName());
            st.setDouble(4, car.getMileage());

            int alteredRows = st.executeUpdate();
            if (alteredRows != 1) {
                throw new ServiceFailureException(
                        "Bad amount of rows were added : " + alteredRows);
            }

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    car.setId(rs.getLong(1));
                } /*else {
                 throw new SQLException("Creating car failed, no generated key obtained.");
                 }*/
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return car;
    }

    @Override
    public void deleteCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("car is null");
        }
        if (car.getId() == null) {
            throw new IllegalArgumentException("car id is null");
        }
        if (findCarById(car.getId()) == null) {
            throw new IllegalArgumentException("car isn't there");
        }

        try (PreparedStatement st = connection.prepareStatement(
                "DELETE FROM CAR WHERE ID = ?", Statement.RETURN_GENERATED_KEYS)) {
            st.setLong(1, car.getId());
            int alteredRows = st.executeUpdate();
            if (alteredRows != 1) {
                throw new ServiceFailureException(
                        "Bad amount of rows were deleted : " + alteredRows);
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        car.setId(null); //tricky -- we need to delete data in storage before deleting database
    }

    @Override
    public List<Car> findAllCars() {
        List<Car> result = null;

        try (PreparedStatement st = connection.prepareStatement(
                "SELECT * FROM CAR", Statement.RETURN_GENERATED_KEYS)) {
            try (ResultSet rs = st.executeQuery()) {

                result = new ArrayList<>();
                while (rs.next()) {
                    result.add(resultSetToCar(rs));
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public void updateCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("car is null");
        }
        if (car.getId() == null) {
            throw new IllegalArgumentException("car id is unset");
        }
        if (car.getMileage() < 0) {
            throw new IllegalArgumentException("car has negative mileage");
        }
        if (car.getName() == null) {
            throw new IllegalArgumentException("car name is null");
        }
        if (car.getSpz() == null) {
            throw new IllegalArgumentException("car spz is null");
        }
        if (car.getVin() == null) {
            throw new IllegalArgumentException("car vin is null");
        }
        if (findCarById(car.getId()) == null) {
            throw new IllegalArgumentException("car isn't there");
        }

        try (PreparedStatement st = connection.prepareStatement(
                "UPDATE car set name = ?,spz = ?,vin = ?, mileage = ? WHERE id = ?")) {
            st.setString(1, car.getName());
            st.setString(2, car.getSpz());
            st.setString(3, car.getVin());
            st.setDouble(4, car.getMileage());
            st.setLong(5, car.getId());

            int alteredRows = st.executeUpdate();
            if (alteredRows != 1) {
                throw new ServiceFailureException(
                        "Bad amount of rows were updated : " + alteredRows);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Car findCarById(Long id) {
        Car car = null;
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        try (PreparedStatement st = connection.prepareStatement(
                "SELECT * FROM CAR WHERE id = ?", Statement.RETURN_GENERATED_KEYS)) {
            st.setLong(1, id.longValue());
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    car = resultSetToCar(rs);
                    if (rs.next()) {
                        throw new ServiceFailureException(
                                "Internal error: More entities with the same id found "
                                + "(source id: " + id + ", found " + car + " and " + resultSetToCar(rs));
                    }

                }
            }


        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return car;
    }

    private Car resultSetToCar(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getLong("ID"));
        car.setMileage(rs.getDouble("MILEAGE"));
        car.setName(rs.getString("NAME"));
        car.setSpz(rs.getString("SPZ"));
        car.setVin(rs.getString("VIN"));

        return car;
    }
}
