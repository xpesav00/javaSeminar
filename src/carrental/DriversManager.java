/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Trida zajistuje spravu dat nad objektem Driver
 * @package carrental
 * @file DriversManager.java
 * @author Jan Pesava - xpesav00, Filip Krepinsky
 * @email xpesav00@mail.muni.cz, 410022@mail.muni.cz
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class DriversManager implements IDriverManager {

    public static final Logger logger = Logger.getLogger(DriversManager.class.getName());
    private DataSource dataSource;

    public DriversManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Driver createDriver(Driver driver) throws ServiceFailureException {

        checkArgs(driver);
        if (driver.getId() != null) {
            throw new IllegalArgumentException("Driver dont have null id pointer.");
        }

        //save data to database
        try (Connection connection = this.dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO driver (license_id, name, surname) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                insertStatement.setString(1, driver.getLicenseId());
                insertStatement.setString(2, driver.getName());
                insertStatement.setString(3, driver.getSurname());
                int addedRows = insertStatement.executeUpdate();
                if (addedRows != 1) {
                    throw new ServiceFailureException("To DB was added bad count of rows - " + addedRows);
                }

                ResultSet rs = insertStatement.getGeneratedKeys();
                driver.setId(this.getKey(rs, driver));
                connection.commit();
            } finally {
                connection.rollback();
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "create driver", ex);
            throw new ServiceFailureException("Internal error: Failed creating driver", ex);
        }

        return driver;
    }

    @Override
    public void deleteDriver(Driver driver) throws ServiceFailureException {

        //check param
        if (driver == null) {
            throw new IllegalArgumentException("Argument - driver is null.");
        }
        checkArgs2(driver);

        try (Connection connection = this.dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM driver where id=?")) {
                deleteStatement.setLong(1, driver.getId());
                int deletedRows = deleteStatement.executeUpdate();

                if (deletedRows != 1) {
                    throw new ServiceFailureException("Internal error: In database was deleted more or less rows. Deleted rows: " + deletedRows);
                }
                connection.commit();
            } finally {
                connection.rollback();
            }

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "delete driver", ex);
            throw new ServiceFailureException("Internal error: Problem with deleting driver.", ex);
        }
    }

    @Override
    public void updateDriver(Driver driver) throws ServiceFailureException {  //control driver object  

        checkArgs(driver);
        checkArgs2(driver);

        //save data to database
        try (Connection connection = this.dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE driver set license_id = ?,name = ?,surname = ? WHERE id = ?")) {
                updateStatement.setString(1, driver.getLicenseId());
                updateStatement.setString(2, driver.getName());
                updateStatement.setString(3, driver.getSurname());
                updateStatement.setLong(4, driver.getId());
                int updatedRows = updateStatement.executeUpdate();
                if (updatedRows != 1) {
                    throw new IllegalArgumentException("In DB was updated bad count of rows - " + updatedRows);
                }
                connection.commit();
            } finally {
                connection.rollback();
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "update driver", ex);
            throw new ServiceFailureException("Internal error: Problem with updating driver.", ex);
        }
    }

    @Override
    public List<Driver> findAllDrivers() throws ServiceFailureException {

        try (Connection connection = this.dataSource.getConnection();
                PreparedStatement findStatement = connection.prepareStatement("SELECT * FROM driver");) {
            ResultSet rs = findStatement.executeQuery();
            List<Driver> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToDriver(rs));
            }
            return result;

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "find all drivers", ex);
            throw new ServiceFailureException("Error when retrieving all drivers", ex);
        }
    }

    @Override
    public Driver findDriverById(Long id) throws ServiceFailureException {

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
            logger.log(Level.SEVERE, "find driver with id " + id, ex);
            throw new ServiceFailureException("Error when retrieving driver with id " + id, ex);
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

    private Long getKey(ResultSet keyRS, Driver driver) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert driver " + driver
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert driver " + driver
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert driver " + driver
                    + " - no key found");
        }
    }

    private void checkArgs(Driver driver) throws IllegalArgumentException {
        //control driver object
        if (driver == null) {
            throw new IllegalArgumentException("Driver has null pointer.");
        }

        if (driver.getLicenseId() == null) {
            throw new IllegalArgumentException("Driver have null id pointer.");
        }
        if (driver.getName() == null) {
            throw new IllegalArgumentException("Driver have null name pointer.");
        }
        if (driver.getSurname() == null) {
            throw new IllegalArgumentException("Driver have null surname pointer.");
        }
    }

    private void checkArgs2(Driver driver) throws IllegalArgumentException, ServiceFailureException {
        if (driver.getId() == null) {
            throw new IllegalArgumentException("Argument - driver.id is null.");
        }
        if (this.findDriverById(driver.getId()) == null) {
            throw new IllegalArgumentException("Driver isnt in database.");
        }
    }
}
