/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Startovaci trida
 * @package carrental
 * @file CarRental.java
 * @author Jan Pesava - xpesav00, Filip Krepinsky
 * @email xpesav00@mail.muni.cz, 410022
 * @mail.muni.cz
 * @date 5. 3. 2013
 */
package carrental;

import static carrental.DriversManager.logger;
import common.MainWindow;
import common.ServiceFailureException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.swing.JFrame;
import org.apache.commons.dbcp.BasicDataSource;

public class CarRental {

    private static Properties prop = new Properties();
    

    private static void createAndShowGUI() {
        JFrame mainWindow = new MainWindow();
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NamingException {
        
        BasicDataSource dataSource;
        try {
            prop.load(new FileInputStream("db.properties"));
           
            dataSource = new BasicDataSource();
            dataSource.setUrl(prop.getProperty("url"));
            dataSource.setUsername(prop.getProperty("username"));
            dataSource.setPassword(prop.getProperty("password"));

        } catch (IOException ex) {            
            throw new ServiceFailureException("Internal error: couldn't load properties", ex);
        }
	
	
        
        MainWindow.main(args);
        /*javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
		    MainWindow.main(args);
            }
        });*/
    }
}
