package labaSolvd.JacoLuna.Classes;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.xml.bind.annotation.*;

import java.util.Objects;

@XmlRootElement(name = "Passenger")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "Passenger")
@JsonPropertyOrder({"idPassenger", "VIP", "flightPoints", "hasSpecialNeeds"})
public class Passenger extends People{
    @XmlAttribute(name = "idPassenger")
    private long idPassenger;
    @XmlElement(name = "VIP")
    private boolean VIP;
    @XmlElement(name = "flightPoints")
    private int flightPoints;
    @XmlElement(name = "hasSpecialNeeds")
    private boolean hasSpecialNeeds;

    private void initialize(Boolean VIP, int flightPoints, Boolean hasSpecialNeeds) {
        this.VIP = VIP;
        this.flightPoints = flightPoints;
        this.hasSpecialNeeds = hasSpecialNeeds;
    }
    public Passenger() {
        super();
    }
    public Passenger(String name, String surname, String email, int age, Boolean VIP, int flightPoints, Boolean hasSpecialNeeds) {
        super(name, surname, email, age);
        initialize(VIP, flightPoints, hasSpecialNeeds);
    }
    public Passenger(long idPeople, String name, String surname, String email, int age, Boolean VIP, int flightPoints, Boolean hasSpecialNeeds) {
        super(idPeople, name, surname, email, age);
        initialize(VIP, flightPoints, hasSpecialNeeds);
    }
    public Passenger(long idPeople, String name, String surname, String email, int age, long idPassenger, Boolean VIP, int flightPoints, Boolean hasSpecialNeeds) {
        super(idPeople, name, surname, email, age);
        this.idPassenger = idPassenger;
        initialize(VIP, flightPoints, hasSpecialNeeds);
    }

    @JsonGetter("idPassenger")
    public long getIdPassenger() {
        return idPassenger;
    }

    @JsonGetter("VIP")
    public Boolean isVIP() {
        return VIP;
    }

    @JsonGetter("flightPoints")
    public int getFlightPoints() {
        return flightPoints;
    }

    @JsonGetter("hasSpecialNeeds")
    public Boolean hasSpecialNeeds() {
        return hasSpecialNeeds;
    }

    public void setVIP(Boolean VIP) {
        this.VIP = VIP;
    }

    public void setFlightPoints(int flightPoints) {
        this.flightPoints = flightPoints;
    }

    public void setHasSpecialNeeds(Boolean hasSpecialNeeds) {
        this.hasSpecialNeeds = hasSpecialNeeds;
    }

    public void setIdPassenger(long idPassenger) {
        this.idPassenger = idPassenger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Passenger passenger = (Passenger) o;
        return idPassenger == passenger.idPassenger && Objects.equals(VIP, passenger.VIP) && Objects.equals(hasSpecialNeeds, passenger.hasSpecialNeeds);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + (int)idPassenger;
    }

    @Override
    public String toString() {
        return  "'id Passenger=" + idPassenger +
                ", VIP=" + VIP +
                ", flightPoints=" + flightPoints +
                ", hasSpecialNeeds=" + hasSpecialNeeds +
                ", id People =" + getIdPeople() +
                ", name =" + getName() +
                ", surname =" + getSurname() +
                ", email =" + getEmail() +
                ", age =" + getAge() + "}'";
    }
}
