/**
 * Projekt do predmetu PV168 - Autopujcovna
 *
 * @description Trida zajistuje spravu dat nad objektem Rental
 * @package carrental
 * @file Rental.java
 * @author Jan Pesava - xpesav00, Filip Krepinsky
 * @email xpesav00@mail.muni.cz, 410022
 * @mail.muni.cz
 * @date 5. 3. 2013
 */
package carrental;

import java.math.BigDecimal;
import java.util.Calendar;

public class Rental {

    private Long id;
    private Driver driver;
    private Car car;
    private BigDecimal price;
    private Calendar startTime;
    private Calendar expectedEndTime;
    private Calendar endTime;

    public Rental() {
    }

    public Rental(Long id) {
        this.id = id;
    }

    public Rental(Long id, Driver driver, Car car, BigDecimal price, Calendar startTime, Calendar expectedEndTime) {
        this.id = id;
        this.driver = driver;
        this.car = car;
        this.price = price;
        this.startTime = startTime;
        this.expectedEndTime = expectedEndTime;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Calendar getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Calendar startDate) {
        this.startTime = startDate;
    }

    public Calendar getExpectedEndTime() {
        return this.expectedEndTime;
    }

    public void setExpectedEndTime(Calendar expectedEndTime) {
        this.expectedEndTime = expectedEndTime;
    }

    public Calendar getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Rental) {
            Rental rental = (Rental) object;

            if (this.id == rental.id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) (this.id * 211);
    }

    @Override
    public String toString() {
        return id.toString() + driver + car;
    }
}
