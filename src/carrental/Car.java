/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Trida zajistuje spravu dat nad objektem Car
 * @package carrental
 * @file Car.java
 * @author Jan Pesava - xpesav00
 * @email xpesav00@mail.muni.cz
 * @date 5. 3. 2013
 */
package carrental;

public class Car {

    private Long id;
    private String vin;
    private String spz;
    private String name;
    private Double mileage;

    public Car() {
    }
    
    public Car(Long id) {
	    this.id = id;
    }

    public Car(Long id, String vin, String spz, String name, Double mileage) {
        this.id = id;
        this.vin = vin;
        this.spz = spz;
        this.name = name;
        this.mileage = mileage;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVin() {
        return this.vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getSpz() {
        return this.spz;
    }

    public void setSpz(String spz) {
        this.spz = spz;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMileage() {
        return this.mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof Car && this.id != null)) {
            Car car = (Car) obj;
            if (this.id.equals(car.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (id == null)? 0 : id.hashCode();
    }
    
    @Override
    public String toString() {
        return id.toString() + " " + name;
    }
}
