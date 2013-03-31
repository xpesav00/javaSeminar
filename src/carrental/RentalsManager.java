/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Trida zajistuje manazera pro praci s objektem Rental
 * @package carrental
 * @file RentalsManager.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */

package carrental;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class RentalsManager implements IRentalManager{
	
	private DataSource dataSource;
	
	public RentalsManager(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public Rental createRental(Rental rental) throws ServiceFailureException {
		
		 //control rental object
		if (rental == null) {
		    throw new IllegalArgumentException("Rental has null pointer.");
		}
		if (rental.getId() != null) {
		    throw new IllegalArgumentException("Rental doesn have null id pointer.");
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

		//save data to database
		try (Connection connection = this.dataSource.getConnection();
			PreparedStatement insertStatement = connection.prepareStatement(
				"INSERT INTO rental (driver_id, car_id, price, start_time, expected_end_time) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);) {
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

		} catch (SQLException ex) {
		    Logger.getLogger(DriversManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		
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
	
	@Override
	public void endRental(Rental rental) {
		//check param
		if(rental == null) {
			throw new IllegalArgumentException("Internal Error: Rental param is null.");
		}
		if(rental.getId() == null) {
			throw new IllegalArgumentException("Internal Error: Rental id is null.");
		}
		if(rental.getId() < 0) {
			throw new IllegalArgumentException("Internal Error: Rental id is less then zero.");
		}
		if(this.findRentalById(rental.getId()) == null) {
			throw new IllegalArgumentException("Internal Error: Rental is not found.");
		}
		if(rental.getCar() == null) {
			throw new IllegalArgumentException("Internal Error: Car is null.");
		}
		if(rental.getDriver() == null) {
			throw new IllegalArgumentException("Internal Error: Driver is null.");
		}
		if(this.isCarFree(rental.getCar())) {
			throw new IllegalArgumentException("Internal Error: Car is free.");
		}
		if(rental.getPrice() == null) {
			throw new IllegalArgumentException("Internal Error: Price is null.");
		}
		if(rental.getStartTime() == null) {
			throw new IllegalArgumentException("Internal Error: StartTime is null.");
		}
		
		rental.setEndTime(Calendar.getInstance());
		this.updateRental(rental);		
	}
	
	@Override
	public void deleteRental(Rental rental) {
		//check param
		if (rental == null) {
		    throw new IllegalArgumentException("Argument - rental is null.");
		}
		if (rental.getId() == null) {
		    throw new IllegalArgumentException("Argument - rental.id is null.");
		}

		//control data in database
		if (this.findRentalById(rental.getId()) == null) {
		    throw new IllegalArgumentException("Rental isnt in database.");
		}

		try(Connection connection = this.dataSource.getConnection();
			PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM rental where id=?");) {
		   
		    deleteStatement.setLong(1, rental.getId());
		    int deletedRows = deleteStatement.executeUpdate();

		    if (deletedRows != 1) {
			throw new ServiceFailureException("Internal error: In database was deleted more or less rows. Deleted rows: " + deletedRows);
		    }

		} catch (SQLException ex) {
		    throw new ServiceFailureException("Internal error: Problem with deleting driver.", ex);
		}
	}
	
	@Override
	public void updateRental(Rental rental) {
		 //control driver object
		if (rental == null) {
		    throw new IllegalArgumentException("Rental has null pointer.");
		}
		if (rental.getId() == null) {
		    throw new IllegalArgumentException("Rental has null id pointer.");
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
		    throw new IllegalArgumentException("Driver has null price pointer.");
		}
		if (rental.getExpectedEndTime() == null) {
		    throw new IllegalArgumentException("Driver has null expectedEndTime pointer.");
		}
		if (rental.getStartTime() == null) {
		    throw new IllegalArgumentException("Driver has null startTime pointer.");
		}

		try {
		    this.findRentalById(rental.getId());
		} catch (IllegalArgumentException ex) {
		    throw new IllegalArgumentException("This rental isnt in database.", ex);
		}

		//save data to database
		try ( Connection connection = this.dataSource.getConnection();
			PreparedStatement updateStatement = connection.prepareStatement(
				"UPDATE rental set driver_id = ?, car_id = ?, price = ?, start_time = ?, expected_end_time=?, end_time=? WHERE id = ?");) {  
		    updateStatement.setLong(1, rental.getDriver().getId());
		    updateStatement.setLong(2, rental.getCar().getId());
		    updateStatement.setBigDecimal(3, rental.getPrice());
		    updateStatement.setTimestamp(4, new Timestamp(rental.getStartTime().getTimeInMillis()));
		    updateStatement.setTimestamp(5, new Timestamp(rental.getExpectedEndTime().getTimeInMillis()));
		    if(rental.getEndTime() != null) {
			updateStatement.setTimestamp(6, new Timestamp(rental.getEndTime().getTimeInMillis()));
		    } else {
			updateStatement.setTimestamp(6, null);    
		    }
		    updateStatement.setLong(7, rental.getId());
		    int updatedRows = updateStatement.executeUpdate();
		    if (updatedRows != 1) {
			throw new IllegalArgumentException("In DB was updated bad count of rows - " + updatedRows);
		    }
		} catch (SQLException ex) {
		    Logger.getLogger(DriversManager.class.getName()).log(Level.SEVERE, null, ex);
		}
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

			    return rental;
			} else {
			    return null;
			}

		} catch (SQLException ex) {
			throw new ServiceFailureException(
				"Error when retrieving driver with id " + id, ex);
		}
	}
	
	@Override
	public List<Rental> findAllRentals() {
		try(Connection connection = this.dataSource.getConnection();
			PreparedStatement findStatement = connection.prepareStatement("SELECT * FROM rental");) {

		    ResultSet rs = findStatement.executeQuery();

		    List<Rental> result = new ArrayList<>();
		    while (rs.next()) {
			result.add(resultSetToRental(rs));
		    }
		    return result;

		} catch (SQLException ex) {
		    throw new ServiceFailureException(
			    "Error when retrieving all rentals", ex);
		}
	}
	
	private Rental resultSetToRental(ResultSet rs) throws SQLException {
		Rental rental = new Rental();
		rental.setId(rs.getLong("id"));
		rental.setCar(new Car(rs.getLong("car_id")));
		rental.setDriver(new Driver(rs.getLong("driver_id")));
		rental.setPrice(rs.getBigDecimal("price"));
		
		Calendar startTime = Calendar.getInstance();
		startTime.setTimeInMillis(rs.getDate("start_time").getTime());		
		rental.setStartTime(startTime);
	
		Date timestamp = rs.getDate("end_time");
		if(timestamp != null) {
			Calendar endTime = Calendar.getInstance();
			endTime.setTimeInMillis(timestamp.getTime());		
			rental.setEndTime(endTime);
		}
		
		Calendar expectedEndTime = Calendar.getInstance();
		expectedEndTime.setTimeInMillis(rs.getDate("expected_end_time").getTime());		
		rental.setExpectedEndTime(expectedEndTime);

		return rental;
	}
	
	@Override
	public List<Rental> findHistoryOfRental(Car car) {
		return new ArrayList<>();
	}
	
	@Override
	public List<Rental> findHistoryOfRental(Driver driver) {
		return new ArrayList<>();
	}
	
	@Override
	public List<Car> findAllRentedCars(){
		return new ArrayList<>();
	}
	
	@Override
	public List<Car> findAllCarsOnStock(){
		return new ArrayList<>();
	}
	
	@Override
	public boolean isCarFree(Car car){
		return true;
	}	
}
