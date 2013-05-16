/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Trida zajistuje manazera pro praci s objektem Car
 * @package carrental
 * @file CarsManager.java
 * @author Jan Pesava - xpesav00, Filip Krepinsky
 * @email xpesav00@mail.muni.cz, 410022
 * @mail.muni.cz
 * @date 5. 3. 2013
 */
package carrental;

import common.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 *
 * @author honzap
 */
public class CarsManager implements ICarManager {

    public static final Logger logger = LoggerFactory.getLogger(CarRental.class.getName());
    private DataSource dataSource;

    public CarsManager(DataSource dataSource) {
        this.dataSource = dataSource;
        PropertyConfigurator.configure("log4j.properties");
    }

    @Override
    public Car createCar(Car car) {

        checkArgs(car);
        if (car.getId() != null) {
            throw new IllegalArgumentException("car id is already set");
        }

        try (Connection connection = this.dataSource.getConnection()) {

            connection.setAutoCommit(false);
            try (PreparedStatement st = connection.prepareStatement("INSERT INTO CAR(vin,spz,name,mileage) "
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
                connection.commit();
            } finally {
                connection.rollback();
            }
        } catch (SQLException ex) {
            logger.error("create car", ex);
            throw new ServiceFailureException("Internal error: Failed creating car.", ex);

        } finally {
        }
        logger.error("createCar " + car);
        return car;
    }

    @Override
    public void deleteCar(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("car is null");
        }
        checkArgs2(car);

        try (Connection connection = this.dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement st = connection.prepareStatement(
                    "DELETE FROM CAR WHERE ID = ?")) {
                st.setLong(1, car.getId());
                int alteredRows = st.executeUpdate();
                if (alteredRows != 1) {
                    throw new ServiceFailureException(
                            "Bad amount of rows were deleted : " + alteredRows);
                }
                connection.commit();
            } finally {
                connection.rollback();
            }
        } catch (SQLException ex) {
            logger.error("delete car", ex);
            throw new ServiceFailureException("Internal error: Failed deleting car.", ex);
        }
        logger.info("deleteCar " + car);
        // car.setId(null); 
    }

    @Override
    public List<Car> findAllCars() {
        List<Car> result = null;

        try (Connection connection = this.dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                "SELECT * FROM CAR")) {
            try (ResultSet rs = st.executeQuery()) {

                result = new ArrayList<>();
                while (rs.next()) {
                    result.add(resultSetToCar(rs));
                }
            }
        } catch (SQLException ex) {
            logger.error("findAllCars", ex);
            throw new ServiceFailureException("Internal error: Failed while searching for all cars.", ex);
        }
        logger.info("findAllCars ");
        return result;
    }

    @Override
    public void updateCar(Car car) {
        checkArgs(car);
        checkArgs2(car);

        try (Connection connection = this.dataSource.getConnection()) {
            connection.setAutoCommit(false);
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
                connection.commit();
            } finally {
                connection.rollback();
            }
        } catch (SQLException ex) {
            logger.error("update car", ex);
            throw new ServiceFailureException("Internal error: Failed updating car.", ex);
        }
        logger.info("updateCar " + car);
    }

    @Override
    public Car findCarById(Long id) {
        Car car = null;
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        try (Connection connection = this.dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                "SELECT * FROM CAR WHERE id = ?")) {
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
            logger.error("findCarById with id " + id, ex);
            throw new ServiceFailureException("Error when retrieving car with id " + id, ex);
        }
        logger.info("findCarById " + id);
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

    private void checkArgs(Car car) throws IllegalArgumentException {
        if (car == null) {
            throw new IllegalArgumentException("car is null");
        }

        if (car.getMileage() < 0) {
            throw new IllegalArgumentException("car has negative mileage");
        }
        if (car.getName() == null || car.getName() == "") {
            throw new IllegalArgumentException("car name is null");
        }
        if (car.getSpz() == null || car.getSpz() == "") {
            throw new IllegalArgumentException("car spz is null");
        }
        if (car.getVin() == null || car.getVin() == "") {
            throw new IllegalArgumentException("car vin is null");
        }
    }

    private void checkArgs2(Car car) throws IllegalArgumentException {
        if (car.getId() == null) {
            throw new IllegalArgumentException("car id is null");
        }
        if (findCarById(car.getId()) == null) {
            throw new IllegalArgumentException("car isn't there");
        }
    }
}
