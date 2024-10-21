package labaSolvd.JacoLuna.Classes;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.xml.bind.annotation.*;

import java.util.Objects;

@XmlRootElement(name = "Person")
@XmlSeeAlso({Passenger.class, CrewMember.class})
@XmlAccessorType(XmlAccessType.FIELD)
@JsonRootName(value = "Person")
@JsonPropertyOrder({"idPeople", "name", "surname", "economySize", "Email", "age"})
public abstract class People {
    @XmlAttribute(name = "idPeople")
    protected long idPeople;
    @XmlElement(name = "name")
    protected String name;
    @XmlElement(name = "surname")
    protected String surname;
    @XmlElement(name = "Email")
    protected String Email;
    @XmlElement(name = "age")
    protected int age;

    public People() {}
    public People(long idPeople, String name, String surname, String email, int age) {
        this(name, surname, email, age);
        this.idPeople = idPeople;
    }
    public People(String name, String surname, String email, int age) {
        this.name = name;
        this.surname = surname;
        this.Email = email;
        this.age = age;
    }

    @JsonGetter("idPeople")
    public long getIdPeople() {
        return idPeople;
    }
    @JsonGetter("name")
    public String getName() {
        return name;
    }
    @JsonGetter("surname")
    public String getSurname() {
        return surname;
    }
    @JsonGetter("Email")
    public String getEmail() {
        return Email;
    }
    @JsonGetter("age")
    public int getAge() {
        return age;
    }

    public void setIdPeople(long idPeople) {
        this.idPeople = idPeople;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        People people = (People) o;
        return idPeople == people.idPeople && age == people.age && Objects.equals(name, people.name) && Objects.equals(surname, people.surname) && Objects.equals(Email, people.Email);
    }

    @Override
    public int hashCode() {
        return 21 * (int)idPeople +  name.hashCode() + surname.hashCode() + age;
    }

    @Override
    public String toString() {
        return "People{" +
                "idPeople=" + idPeople +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", Email='" + Email + '\'' +
                ", age=" + age +
                '}';
    }
}
