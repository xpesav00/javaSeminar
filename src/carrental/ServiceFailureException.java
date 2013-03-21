/**
 * Projekt do predmetu PV168 - Autopujcovna
 * @description Trida reprezentuje chybu sluzby
 * @package carrental
 * @file ServiceFailureException.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 20. 3. 2013
 */

package carrental;

public class ServiceFailureException extends RuntimeException {

    public ServiceFailureException(String msg) {
        super(msg);
    }

    public ServiceFailureException(Throwable cause) {
        super(cause);
    }

    public ServiceFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
