package labaSolvd.JacoLuna.Classes;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.xml.bind.annotation.*;

import java.util.Objects;

@XmlRootElement(name = "CrewMember")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "CrewMember")
@JsonPropertyOrder({"idPassenger", "role", "flightHours"})
public class CrewMember extends People{
    @XmlAttribute(name = "idCrewMember")
    private long idCrewMember;
    @XmlElement(name = "role")
    private String role;
    @XmlElement(name = "flightHours")
    private int flightHours;

    private void initialize(int flightHours, String role) {
        this.flightHours = flightHours;
        this.role = role;
    }

    public CrewMember() {}
    public CrewMember(String name, String surname, String email, int age, String role, int flightHours) {
        super(name, surname, email, age);
        initialize(flightHours, role);
    }
    public CrewMember(long idPeople, String name, String surname, String email, int age, String role, int flightHours) {
        super(idPeople, name, surname, email, age);
        initialize(flightHours, role);
    }
    public CrewMember(long idPeople, String name, String surname, String email, int age, long idCrewMember, String role, int flightHours) {
        super(idPeople, name, surname, email, age);
        this.idCrewMember = idCrewMember;
        initialize(flightHours, role);
    }

    public long getIdCrewMember() {
        return idCrewMember;
    }

    public String getRole() {
        return role;
    }

    public int getFlightHours() {
        return flightHours;
    }

    public void setIdCrewMember(long idCrewMember) {
        this.idCrewMember = idCrewMember;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFlightHours(int flightHours) {
        this.flightHours = flightHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CrewMember that = (CrewMember) o;
        return idCrewMember == that.idCrewMember && flightHours == that.flightHours && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + (int)idCrewMember + role.length() + flightHours;
    }

    @Override
    public String toString() {
        return "CrewMember{" +
                "idCrewMember=" + idCrewMember +
                ", role='" + role + '\'' +
                ", flightHours=" + flightHours +
                ", idPeople=" + idPeople +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", Email='" + Email + '\'' +
                ", age=" + age +
                '}';
    }
}
