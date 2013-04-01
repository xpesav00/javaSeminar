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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource; 

public class CarRental {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws NamingException{
		try {
			//connect to db
			//Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/javaSeminar", "developer", "developer");
			
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName("org.apache.derby.jdbc.Driver169");
			dataSource.setUrl("jdbc:derby://localhost:1527/javaSeminar");
			//dataSource.setUrl("jdbc:derby:memory:javaSeminar;create=true");
			dataSource.setUsername("developer");
			dataSource.setPassword("developer");
			Connection connection = dataSource.getConnection();
			
                        
			//here is possible to create  queries
			/*PreparedStatement ps = connection.prepareStatement("SELECT * from car");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				System.out.println(rs.getString("name"));
			}*/
			
			
		} catch (SQLException ex) {
			Logger.getLogger(CarRental.class.getName()).log(Level.SEVERE, null, ex);
		} 
		
	}
}
