package common;

import carrental.CarRental;
import static carrental.CarsManager.logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.log4j.*;
import org.slf4j.LoggerFactory;

/**
 * Some DB tools.
 *
 * @author Petr Adamek
 */
public class Utils {

    private static final Logger logger = Logger.getLogger(
            Utils.class.getName());

    /**
     * sets format and file for logging catches IOException
     *
     * @param logger logger for initialization
     */
 /*   public static void initLogger(org.apache.log4j.Logger logger2) {
        try {
            FileAppender logfile = new FileAppender(new PatternLayout("%d %r [%t] %-5p %c %x - %m%n"), "appLog.log", true);
            logger2.addAppender(logfile);
        } catch (IOException x) {
        }
    }*/

    /**
     * Makes sure there is only one instance of program
     * http://www.java2s.com/Code/Java/File-Input-Output/DemonstratesfilelockingandsimplefilereadandwriteoperationsusingjavaniochannelsFileChannel.htm
     *
     */
    public static void initProgram() {
        RandomAccessFile file;
        FileChannel f;
        FileLock lock;

        try {
            String tmpdir = System.getProperty("java.io.tmpdir");
            String filename = Lock.class.getName() + ".lock";
            File lockfile = new File(tmpdir, filename);

            file = new RandomAccessFile(lockfile, "rw");
            f = file.getChannel();

            lock = f.tryLock();

            if (lock != null) {                
                lockfile.deleteOnExit();           
                
            } else {
                 final org.slf4j.Logger logger3 = LoggerFactory.getLogger(CarRental.class.getName());
                PropertyConfigurator.configure("log4j.properties");
                logger3.warn("Can't run two instances at same time");                
                System.exit(1);            
            }

        } catch (IOException x) {
            System.exit(1);           
        }        
    }

    /**
     * Closes connection and logs possible error. W
     *
     * @param conn connection to close
     * @param statements statements to close
     */
    public static void closeQuietly(Connection conn, Statement... statements) {
        for (Statement st : statements) {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Error when closing statement", ex);
                }
            }
        }
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error when switching autocommit mode back to true", ex);
            }
            try {
                conn.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error when closing connection", ex);
            }
        }
    }

    /**
     * Rolls back transaction and logs possible error.
     *
     * @param conn connection
     */
    public static void doRollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                if (conn.getAutoCommit()) {
                    throw new IllegalStateException("Connection is in the autocommit mode!");
                }
                conn.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error when doing rollback", ex);
            }
        }
    }

    /**
     * Extract key from given ResultSet.
     *
     * @param key resultSet with key
     * @return key from given result set
     * @throws SQLException when operation fails
     */
    public static Long getId(ResultSet key) throws SQLException {
        if (key.getMetaData().getColumnCount() != 1) {
            throw new IllegalArgumentException("Given ResultSet contains more columns");
        }
        if (key.next()) {
            Long result = key.getLong(1);
            if (key.next()) {
                throw new IllegalArgumentException("Given ResultSet contains more rows");
            }
            return result;
        } else {
            throw new IllegalArgumentException("Given ResultSet contain no rows");
        }
    }

    /**
     * Reads SQL statements from file. SQL commands in file must be separated by
     * a semicolon.
     *
     * @param url url of the file
     * @return array of command strings
     */
    private static String[] readSqlStatements(URL url) {
        try {
            char buffer[] = new char[256];
            StringBuilder result = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
            while (true) {
                int count = reader.read(buffer);
                if (count < 0) {
                    break;
                }
                result.append(buffer, 0, count);
            }
            return result.toString().split(";");
        } catch (IOException ex) {
            throw new RuntimeException("Cannot read " + url, ex);
        }
    }

    /**
     * Try to execute script for creating tables. If tables already exist,
     * appropriate exception is catched and ignored.
     *
     * @param ds dataSource
     * @param scriptUrl url of script for creating tables
     * @throws SQLException when operation fails
     */
    public static void tryCreateTables(DataSource ds, URL scriptUrl) throws SQLException {
        try {
            executeSqlScript(ds, scriptUrl);
            logger.warning("Tables created");
        } catch (SQLException ex) {
            if ("X0Y32".equals(ex.getSQLState())) {
                // This code represents "Table/View/... already exists"
                // This code is Derby specific!
                return;
            } else {
                throw ex;
            }
        }
    }

    /**
     * Executes SQL script.
     *
     * @param ds datasource
     * @param scriptUrl url of sql script to be executed
     * @throws SQLException when operation fails
     */
    public static void executeSqlScript(DataSource ds, URL scriptUrl) throws SQLException {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            for (String sqlStatement : readSqlStatements(scriptUrl)) {
                if (!sqlStatement.trim().isEmpty()) {
                    conn.prepareStatement(sqlStatement).executeUpdate();
                }
            }
        } finally {
            closeQuietly(conn);
        }
    }
    /**
     * Check if updates count is one. Otherwise appropriate exception is thrown.
     *
     * @param count updates count.
     * @param entity updated entity (for includig to error message)
     * @param insert flag if performed operation was insert
     * @throws IllegalEntityException when updates count is zero, so updated
     * entity does not exist
     * @throws ServiceFailureException when updates count is unexpected number
     */
    /*  public static void checkUpdatesCount(int count, Object entity, 
     boolean insert) throws IllegalEntityException, ServiceFailureException {
        
     if (!insert && count == 0) {
     throw new IllegalEntityException("Entity " + entity + " does not exist in the db");
     }
     if (count != 1) {
     throw new ServiceFailureException("Internal integrity error: Unexpected rows count in database affected: " + count);
     }
     }*/
}
