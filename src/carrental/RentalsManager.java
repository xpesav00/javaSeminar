/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Trida zajistuje manazera pro praci s objektem Rental
 * @package carrental
 * @file RentalsManager.java
 * @author Jan Pesava - xpesav00, Filip Krepinsky
 * @email xpesav00@mail.muni.cz, 410022
 * @mail.muni.cz
 * @date 5. 3. 2013
 */
package carrental;

import common.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.sql.DataSource;
import org.apache.log4j.*;
import org.slf4j.LoggerFactory;

public class RentalsManager implements IRentalManager {

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(CarRental.class.getName());
    private DataSource dataSource;

    public RentalsManager(DataSource dataSource) {
        this.dataSource = dataSource;
        PropertyConfigurator.configure("log4j.properties");
    }

    @Override
    public Rental createRental(Rental rental) throws ServiceFailureException {

        checkArgs(rental);
        if (rental.getId() != null) {
            throw new IllegalArgumentException("Rental doesn have null id pointer.");
        }

        //save data to database
        try (Connection connection = this.dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO rental (driver_id, car_id, price, start_time, expected_end_time)"
                    + " VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);) {

                insertStatement.setLong(1, rental.getDriver().getId());
                insertStatement.setLong(2, rental.getCar().getId());
                insertStatement.setBigDecimal(3, rental.getPrice());
                insertStatement.setTimestamp(4, new Timestamp(rental.getStartTime().getTimeInMillis()));
                insertStatement.setTimestamp(5, new Timestamp(rental.getExpectedEndTime().getTimeInMillis()));
                int addedRows = insertStatement.executeUpdate();
                if (addedRows != 1) {
                    throw new ServiceFailureException("To DB was added bad count of rows - " + addedRows);
                }

                ResultSet rs = insertStatement.getGeneratedKeys();
                rental.setId(this.getKey(rs, rental));
                //System.out.println("Prirazuji id: " + rental.getId());
                connection.commit();
            } finally {
                connection.rollback();
            }
        } catch (SQLException ex) {
            logger.error("create rental", ex);
            throw new ServiceFailureException("Internal error: Failed creating rental.", ex);
        }
        logger.info("createRental " + rental);
        return rental;
    }

    @Override
    public void endRental(Rental rental) {
        //check param
        checkArgs(rental);
        checkArgs2(rental);
        if (rental.getId() < 0) {
            throw new IllegalArgumentException("Internal Error: Rental id is less then zero.");
        }
        rental.setEndTime(Calendar.getInstance());
        this.updateRental(rental);
        logger.info("endRental" + rental);
    }

    @Override
    public void deleteRental(Rental rental) {
        //check param
        if (rental == null) {
            throw new IllegalArgumentException("Argument - rental is null.");
        }
        checkArgs2(rental);

        try (Connection connection = this.dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM rental where id=?")) {

                deleteStatement.setLong(1, rental.getId());
                int deletedRows = deleteStatement.executeUpdate();

                if (deletedRows != 1) {
                    throw new ServiceFailureException("Internal error: In database was deleted more or less rows. Deleted rows: " + deletedRows);
                }
                connection.commit();
            } finally {
                connection.rollback();
            }

        } catch (SQLException ex) {
            logger.error("delete rental", ex);
            throw new ServiceFailureException("Internal error: Problem with deleting rental.", ex);
        }
        logger.info("deleteRental " + rental);
    }

    @Override
    public void updateRental(Rental rental) {
        //control driver object
        checkArgs(rental);
        checkArgs2(rental);

        //save data to database
        try (Connection connection = this.dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE rental set driver_id = ?, car_id = ?, price = ?, start_time = ?, expected_end_time=?, end_time=? WHERE id = ?");) {
                updateStatement.setLong(1, rental.getDriver().getId());
                updateStatement.setLong(2, rental.getCar().getId());
                updateStatement.setBigDecimal(3, rental.getPrice());
                updateStatement.setTimestamp(4, new Timestamp(rental.getStartTime().getTimeInMillis()));
                updateStatement.setTimestamp(5, new Timestamp(rental.getExpectedEndTime().getTimeInMillis()));

                if (rental.getEndTime() != null) {
                    updateStatement.setTimestamp(6, new Timestamp(rental.getEndTime().getTimeInMillis()));
                } else {
                    updateStatement.setTimestamp(6, null);
                }
                updateStatement.setLong(7, rental.getId());
                int updatedRows = updateStatement.executeUpdate();
                if (updatedRows != 1) {
                    throw new IllegalArgumentException("In DB was updated bad count of rows - " + updatedRows);
                }
                connection.commit();
            } finally {
                connection.rollback();
            }
        } catch (SQLException ex) {
            logger.error("update rental", ex);
            throw new ServiceFailureException("Internal error: Failed updating rental", ex);
        }
        logger.info("updateRental " + rental);
    }

    @Override
    public Rental findRentalById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Bad param: id is null.");
        }

        if (id < 0) {
            throw new IllegalArgumentException("Bad param: id less then zero.");
        }

        try (Connection connection = this.dataSource.getConnection();
                PreparedStatement findStatement = connection.prepareStatement("SELECT * FROM rental WHERE id = ?");) {

            findStatement.setLong(1, id);
            ResultSet rs = findStatement.executeQuery();

            if (rs.next()) {
                Rental rental = resultSetToRental(rs);

                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + rental + " and " + resultSetToRental(rs));
                }
                logger.info("findRentalById " + id);
                return rental;
            } else {
                logger.info("findRentalById " + id);
                return null;
            }

        } catch (SQLException ex) {
            logger.error("find rental with id" + id, ex);
            throw new ServiceFailureException("Error when retrieving rental with id " + id, ex);
        }
    }

    @Override
    public List<Rental> findAllRentals() {
        try (Connection connection = this.dataSource.getConnection();
                PreparedStatement findStatement = connection.prepareStatement("SELECT * FROM rental");) {

            ResultSet rs = findStatement.executeQuery();

            List<Rental> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToRental(rs));
            }
            logger.info("findAllRentals");
            return result;

        } catch (SQLException ex) {
            logger.error("findAllRentals", ex);
            throw new ServiceFailureException("Error when retrieving all rentals", ex);
        }
    }

    @Override
    public List<Rental> findHistoryOfRental(Car car) {
        List<Rental> result = null;
        carArgs(car);

        try (Connection connection = this.dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                "SELECT * FROM rental WHERE car_id = ?")) {
            st.setLong(1, car.getId());
            try (ResultSet rs = st.executeQuery()) {

                result = new ArrayList<>();
                while (rs.next()) {
                    result.add(resultSetToRental(rs));
                }
            }
        } catch (SQLException ex) {
            logger.error("find history of rental", ex);
            throw new ServiceFailureException("Error when finding history of rental(car)", ex);
        }
        logger.info("findHistoryOfRental " + car);
        return result;

    }

    @Override
    public List<Rental> findHistoryOfRental(Driver driver) {
        List<Rental> result = null;

        if (driver == null) {
            throw new IllegalArgumentException("driver is null");
        }
        if (driver.getId() == null) {
            throw new IllegalArgumentException("driver id is null");
        }

        try (Connection connection = this.dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                "SELECT * FROM rental WHERE driver_id = ?")) {
            st.setLong(1, driver.getId());
            try (ResultSet rs = st.executeQuery()) {

                result = new ArrayList<>();
                while (rs.next()) {
                    result.add(resultSetToRental(rs));
                }
            }
        } catch (SQLException ex) {
            logger.error("find history of rental", ex);
            throw new ServiceFailureException("Error when finding history of rental(driver)", ex);
        }
        logger.info("findHistoryOfRental " + driver);
        return result;

    }

    @Override
    public List<Car> findAllRentedCars() {
        List<Car> result = null;
        try (Connection connection = this.dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                "SELECT car.*"
                + "FROM rental ,car "
                + "WHERE  rental.car_id = car.id AND "
                + "rental.end_time IS NULL")) {
            try (ResultSet rs = st.executeQuery()) {

                result = new ArrayList<>();
                while (rs.next()) {
                    result.add(resultSetToCar(rs));
                }

            }
        } catch (SQLException ex) {
            logger.error("find all rented cars", ex);
            throw new ServiceFailureException("Error when finding all rented cars", ex);
        }
        logger.info("findAllRentedCars ");
        return result;
    }

    @Override
    public List<Car> findAllCarsOnStock() {
        List<Car> result;
        CarsManager manager = new CarsManager(dataSource);
        result = manager.findAllCars();

        result.removeAll(findAllRentedCars());
        logger.info("findAllCarsOnStock ");
        return result;
    }

    @Override
    public boolean isCarFree(Car car) {
        carArgs(car);

        try (Connection connection = this.dataSource.getConnection();
                PreparedStatement st = connection.prepareStatement(
                "SELECT * FROM rental WHERE car_id = ? AND end_time IS NULL")) {
            st.setLong(1, car.getId());
            try (ResultSet rs = st.executeQuery()) {

                if (rs.next()) {
                    if (rs.next()) {
                        throw new ServiceFailureException("database: data corruption");
                    }
                    return false;
                }
            }
        } catch (SQLException ex) {
            logger.error("isCarFree", ex);
            throw new ServiceFailureException("Error when assuring car is free", ex);
        }
        return true;
    }

    @Override
    public List<Rental> activeRentals(List<Rental> list) {
        List<Rental> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getEndTime() == null) {
                result.add(list.get(i));
            }
        }
        logger.info("activeRentals ");
        return result;
    }

    private Rental resultSetToRental(ResultSet rs) throws SQLException {
        Rental rental = new Rental();
        rental.setId(rs.getLong("id"));
        rental.setCar(findCarById(rs.getLong("car_id")));
        rental.setDriver(findDriverById(rs.getLong("driver_id")));
        rental.setPrice(rs.getBigDecimal("price"));

        Calendar startTime = Calendar.getInstance();
        startTime.setTimeInMillis(rs.getTimestamp("start_time").getTime());
        rental.setStartTime(startTime);

        Timestamp timestamp = rs.getTimestamp("end_time");
        if (timestamp != null) {
            Calendar endTime = Calendar.getInstance();
            endTime.setTimeInMillis(timestamp.getTime());
            rental.setEndTime(endTime);
        }

        Calendar expectedEndTime = Calendar.getInstance();
        expectedEndTime.setTimeInMillis(rs.getTimestamp("expected_end_time").getTime());
        rental.setExpectedEndTime(expectedEndTime);

        return rental;
    }

    private Long getKey(ResultSet keyRS, Rental rental) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert driver " + rental
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert driver " + rental
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert driver " + rental
                    + " - no key found");
        }
    }

    private void checkArgs(Rental rental) throws IllegalArgumentException {
        //control rental object
        if (rental == null) {
            throw new IllegalArgumentException("Rental has null pointer.");
        }
        if (rental.getCar() == null) {
            throw new IllegalArgumentException("Rental has null car pointer.");
        }
        if (rental.getCar().getId() == null) {
            throw new IllegalArgumentException("Rental has null car.id pointer.");
        }
        if (rental.getDriver() == null) {
            throw new IllegalArgumentException("Rental has null driver pointer.");
        }
        if (rental.getDriver().getId() == null) {
            throw new IllegalArgumentException("Rental has null driver.id pointer.");
        }
        if (rental.getPrice() == null) {
            throw new IllegalArgumentException("Rental has null price pointer.");
        }
        if (rental.getStartTime() == null) {
            throw new IllegalArgumentException("Rental has null start time pointer.");
        }
        if (rental.getExpectedEndTime() == null) {
            throw new IllegalArgumentException("Rental has null expected end time pointer.");
        }
        if (rental.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Rental has negative price");
        }
        if (!rental.getExpectedEndTime().after(rental.getStartTime())) {
            throw new IllegalArgumentException("Expected end time should be greater than start time");
        }

        if (rental.getEndTime() != null) {
            if (rental.getStartTime().after(rental.getEndTime()) && !rental.getStartTime().equals(rental.getEndTime())) {
                throw new IllegalArgumentException("End time should be greater than start time");
            }
        }
    }

    private void checkArgs2(Rental rental) throws IllegalArgumentException {
        if (rental.getId() == null) {
            throw new IllegalArgumentException("Internal Error: Rental id is null.");
        }
        if (this.findRentalById(rental.getId()) == null) {
            throw new IllegalArgumentException("Internal Error: Rental is not found.");
        }
    }

    private void carArgs(Car car) throws IllegalArgumentException {
        if (car == null) {
            throw new IllegalArgumentException("car is null");
        }
        if (car.getId() == null) {
            throw new IllegalArgumentException("car id is null");
        }
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

    private Driver findDriverById(Long id) throws ServiceFailureException {

        if (id == null) {
            throw new IllegalArgumentException("Bad param: id is null.");
        }

        try (Connection connection = this.dataSource.getConnection();
                PreparedStatement findStatement = connection.prepareStatement("SELECT * FROM driver WHERE id = ?");) {

            findStatement.setLong(1, id);
            ResultSet rs = findStatement.executeQuery();

            if (rs.next()) {
                Driver driver = resultSetToDriver(rs);

                if (rs.next()) {
                    throw new ServiceFailureException(
                            "Internal error: More entities with the same id found "
                            + "(source id: " + id + ", found " + driver + " and " + resultSetToDriver(rs));
                }
                return driver;

            } else {
                return null;
            }

        } catch (SQLException ex) {
            logger.error("find driver with id (from rentalsManager)" + id, ex);
            throw new ServiceFailureException("Error when retrieving driver with id(from rentalsManager) " + id, ex);
        }
    }

    private Driver resultSetToDriver(ResultSet rs) throws SQLException {
        Driver driver = new Driver();
        driver.setId(rs.getLong("id"));
        driver.setLicenseId(rs.getString("license_id"));
        driver.setName(rs.getString("name"));
        driver.setSurname(rs.getString("surname"));
        return driver;
    }

    private Car findCarById(Long id) {
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
        return car;
    }
}
