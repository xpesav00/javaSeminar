/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Startovaci trida
 * @package carrental
 * @file CarRental.java
 * @author Jan Pesava - xpesav00, Filip Krepinsky
 * @email xpesav00@mail.muni.cz, 410022@mail.muni.cz
 * @date 5. 3. 2013
 */
package carrental;

import common.MainWindow;
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
import javax.swing.JFrame;
import org.apache.commons.dbcp.BasicDataSource; 

public class CarRental {

	private static void createAndShowGUI() {
		JFrame mainWindow = new MainWindow();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
	}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws NamingException{		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    createAndShowGUI();
			}
		});
	}
}
