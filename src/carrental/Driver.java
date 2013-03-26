/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Trida zajistuje spravu dat nad objektem Driver
 * @package carrental
 * @file Driver.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */
package carrental;

public class Driver {

    private Long id;
    private String name;
    private String surname;
    private String licenseId;

    public Driver() {
    }

    public Driver(Long id, String name, String surname, String licenseId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.licenseId = licenseId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLicenceId() {
        return this.licenseId;
    }

    public void setLicenceId(String licenseId) {
        this.licenseId = licenseId;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof Driver && this.id != null)) {
            Driver driver = (Driver) obj;
            if (this.id.equals(driver.getId())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
    }
    
    @Override
    public String toString() {
        return id.toString() + " " + surname;
    }
}
