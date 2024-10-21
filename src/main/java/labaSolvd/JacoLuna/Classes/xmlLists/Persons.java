package labaSolvd.JacoLuna.Classes.xmlLists;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.People;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Persons")
@XmlAccessorType(XmlAccessType.FIELD)
public class Persons {
    @XmlElement(name = "People")
    private List<People> people = null;
    public Persons() {
        people = new ArrayList<People>();
    }
    public List<People> getPersons() {
        return people;
    }

    public void setPersons(List<People>people) {
        this.people = people;
    }
}
