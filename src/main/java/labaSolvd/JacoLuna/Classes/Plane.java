package labaSolvd.JacoLuna.Classes;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.xml.bind.annotation.*;

import java.time.LocalDate;
import java.util.Objects;

@XmlRootElement(name = "Plane")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "Plane")
@JsonPropertyOrder({"idPlane", "fuelCapacity", "tripulationSize", "economySize", "premiumSize", "businessSize", "firstClassSize", "country"})
public class Plane {
    //    @XmlElementRef(name="contacts", type=Plane.class)
    @XmlAttribute(name = "idPlane")
    private String idPlane;
    @XmlElement(name = "fuelCapacity")
    private int fuelCapacity;
    @XmlElement(name = "tripulationSize")
    private int tripulationSize;
    @XmlElement(name = "economySize")
    private int economySize;
    @XmlElement(name = "premiumSize")
    private int premiumSize;
    @XmlElement(name = "businessSize")
    private int businessSize;
    @XmlElement(name = "firstClassSize")
    private int firstClassSize;
    @XmlElement(name = "country")
    private String country;

    public Plane() {
    }

    public Plane(String idPlane, int fuelCapacity, int tripulationSize, int economySize, int premiumSize, int businessSize, int firstClassSize, String country) {
        this.idPlane = idPlane;
        this.fuelCapacity = fuelCapacity;
        this.tripulationSize = tripulationSize;
        this.economySize = economySize;
        this.premiumSize = premiumSize;
        this.businessSize = businessSize;
        this.firstClassSize = firstClassSize;
        this.country = country;
    }

    @JsonGetter("idPlane")
    public String getIdPlane() {
        return idPlane;
    }

    @JsonGetter("fuelCapacity")
    public int getFuelCapacity() {
        return fuelCapacity;
    }

    @JsonGetter("tripulationSize")
    public int getTripulationSize() {
        return tripulationSize;
    }

    @JsonGetter("economySize")
    public int getEconomySize() {
        return economySize;
    }

    @JsonGetter("premiumSize")
    public int getPremiumSize() {
        return premiumSize;
    }

    @JsonGetter("businessSize")
    public int getBusinessSize() {
        return businessSize;
    }

    @JsonGetter("firstClassSize")
    public int getFirstClassSize() {
        return firstClassSize;
    }

    @JsonGetter("country")
    public String getCountry() {
        return country;
    }

    public void setIdPlane(String idPlane) {
        this.idPlane = idPlane;
    }

    public void setFuelCapacity(int fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public void setTripulationSize(int tripulationSize) {
        this.tripulationSize = tripulationSize;
    }

    public void setEconomySize(int economySize) {
        this.economySize = economySize;
    }

    public void setPremiumSize(int premiumSize) {
        this.premiumSize = premiumSize;
    }

    public void setBusinessSize(int businessSize) {
        this.businessSize = businessSize;
    }

    public void setFirstClassSize(int firstClassSize) {
        this.firstClassSize = firstClassSize;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plane plane = (Plane) o;
        return fuelCapacity == plane.fuelCapacity &&
                tripulationSize == plane.tripulationSize &&
                economySize == plane.economySize &&
                premiumSize == plane.premiumSize &&
                businessSize == plane.businessSize &&
                firstClassSize == plane.firstClassSize &&
                Objects.equals(idPlane, plane.idPlane) &&
                Objects.equals(country, plane.country);
    }

    @Override
    public int hashCode() {
        return 21 * fuelCapacity + tripulationSize + economySize + premiumSize + businessSize + firstClassSize;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "idPlane='" + idPlane + '\'' +
                ", fuelCapacity=" + fuelCapacity +
                ", tripulationSize=" + tripulationSize +
                ", economySize=" + economySize +
                ", premiumSize=" + premiumSize +
                ", businessSize=" + businessSize +
                ", firstClassSize=" + firstClassSize +
                ", country='" + country + '\'' +
                '}';
    }
}
