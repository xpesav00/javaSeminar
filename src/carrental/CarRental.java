/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Startovaci trida
 * @package carrental
 * @file CarRental.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */
package carrental;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarRental {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args){
		try {
			//connect to db
			Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/javaSeminar", "developer", "developer");
			
			//here is possible to create  queries
			
		} catch (SQLException ex) {
			Logger.getLogger(CarRental.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
}
