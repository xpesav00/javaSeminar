/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common.window;


import common.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

/**
 *
 * @author ansy
 */
public class SetStatusBarSwingWorker extends SwingWorker<Integer, Void> {

    String message;
    JTextField statusBar;

    public SetStatusBarSwingWorker(JTextField statusBar, String message) {

        this.message = message;
        this.statusBar = statusBar;

    }

    @Override
    protected Integer doInBackground() throws Exception {

        synchronized (MainWindow.getLock()) {

            statusBar.setText(message);
            
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            statusBar.setText("");
        }
        return 0;
    }
}
