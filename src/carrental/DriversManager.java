/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Trida zajistuje spravu dat nad objektem Driver
 * @package carrental
 * @file DriversManager.java
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

public class DriversManager implements IDriverManager {

    private Connection connection;

    public DriversManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Driver createDriver(Driver driver) throws ServiceFailureException {

        //control driver object
        if (driver == null) {
            throw new IllegalArgumentException("Driver has null pointer.");
        }
        if (driver.getId() != null) {
            throw new IllegalArgumentException("Driver dont have null id pointer.");
        }
        if (driver.getLicenceId() == null) {
            throw new IllegalArgumentException("Driver have null id pointer.");
        }
        if (driver.getName() == null) {
            throw new IllegalArgumentException("Driver have null name pointer.");
        }
        if (driver.getSurname() == null) {
            throw new IllegalArgumentException("Driver have null surname pointer.");
        }

        //save data to database
        PreparedStatement insertStatement = null;
        try {
            insertStatement = connection.prepareStatement("INSERT INTO driver (license_id, name, surname) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, driver.getLicenceId());
            insertStatement.setString(2, driver.getName());
            insertStatement.setString(3, driver.getSurname());
            int addedRows = insertStatement.executeUpdate();
            if (addedRows != 1) {
                throw new ServiceFailureException("To DB was added bad count of rows - " + addedRows);
            }

            ResultSet rs = insertStatement.getGeneratedKeys();
            driver.setId(this.getKey(rs, driver));
            //System.out.println("created new driver with this id:"+driver.getId());

        } catch (SQLException ex) {
            Logger.getLogger(DriversManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (insertStatement != null) {
                try {
                    insertStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DriversManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

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

    @Override
    public void deleteDriver(Driver driver) throws ServiceFailureException {

        //check param
        if (driver == null) {
            throw new IllegalArgumentException("Argument - driver is null.");
        }
        if (driver.getId() == null) {
            throw new IllegalArgumentException("Argument - driver.id is null.");
        }

        //control data in database

        if (this.findDriverById(driver.getId()) == null) {
            throw new IllegalArgumentException("Driver isnt in database.");
        }

        PreparedStatement deleteStatement = null;
        try {
            deleteStatement = this.connection.prepareStatement("DELETE FROM driver where id=?");
            deleteStatement.setLong(1, driver.getId());
            int deletedRows = deleteStatement.executeUpdate();

            if (deletedRows != 1) {
                throw new ServiceFailureException("Internal error: In database was deleted more or less rows. Deleted rows: " + deletedRows);
            }

        } catch (SQLException ex) {
            throw new ServiceFailureException("Internal error: Problem with deleting driver.", ex);
        } finally {
            if (deleteStatement != null) {
                try {
                    deleteStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DriversManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void updateDriver(Driver driver) throws ServiceFailureException {
        //control driver object
        if (driver == null) {
            throw new IllegalArgumentException("Driver has null pointer.");
        }
        if (driver.getId() == null) {
            throw new IllegalArgumentException("Driver have null id pointer.");
        }
        if (driver.getLicenceId() == null) {
            throw new IllegalArgumentException("Driver have null id pointer.");
        }
        if (driver.getName() == null) {
            throw new IllegalArgumentException("Driver have null name pointer.");
        }
        if (driver.getSurname() == null) {
            throw new IllegalArgumentException("Driver have null surname pointer.");
        }

        try {
            this.findDriverById(driver.getId());
        } catch (ServiceFailureException ex) {
            throw new IllegalArgumentException("This driver isnt in database.", ex);
        }

        //save data to database
        PreparedStatement updateStatement = null;
        try {
            updateStatement = connection.prepareStatement("UPDATE driver set license_id = ?,name = ?,surname = ? WHERE id = ?");
            updateStatement.setString(1, driver.getLicenceId());
            updateStatement.setString(2, driver.getName());
            updateStatement.setString(3, driver.getSurname());
            updateStatement.setLong(4, driver.getId());
            int updatedRows = updateStatement.executeUpdate();
            if (updatedRows != 1) {
                throw new IllegalArgumentException("In DB was updated bad count of rows - " + updatedRows);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DriversManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (updateStatement != null) {
                try {
                    updateStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DriversManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public List<Driver> findAllDrivers() throws ServiceFailureException {

        PreparedStatement findStatement = null;
        try {
            findStatement = connection.prepareStatement("SELECT * FROM driver");
            ResultSet rs = findStatement.executeQuery();

            List<Driver> result = new ArrayList<>();
            while (rs.next()) {
                result.add(resultSetToDriver(rs));
            }
            return result;

        } catch (SQLException ex) {
            throw new ServiceFailureException(
                    "Error when retrieving all drivers", ex);
        } finally {
            if (findStatement != null) {
                try {
                    findStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DriversManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public Driver findDriverById(Long id) throws ServiceFailureException {

        if (id == null) {
            throw new IllegalArgumentException("Bad param: id is null.");
        }

        PreparedStatement findStatement = null;
        try {
            findStatement = connection.prepareStatement("SELECT * FROM driver WHERE id = ?");
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
            throw new ServiceFailureException(
                    "Error when retrieving driver with id " + id, ex);
        } finally {
            if (findStatement != null) {
                try {
                    findStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DriversManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private Driver resultSetToDriver(ResultSet rs) throws SQLException {
        Driver driver = new Driver();
        driver.setId(rs.getLong("id"));
        driver.setLicenceId(rs.getString("license_id"));
        driver.setName(rs.getString("name"));
        driver.setSurname(rs.getString("surname"));
        return driver;
    }
}
