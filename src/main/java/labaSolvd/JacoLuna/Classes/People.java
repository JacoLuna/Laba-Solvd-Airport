package labaSolvd.JacoLuna.Classes;

import java.util.Objects;

public abstract class People {
    private long idPeople;
    private String name;
    private String surname;
    private String Email;
    private int age;

    public People(long idPeople, String name, String surname, String email, int age) {
        this(name, surname, email, age);
        idPeople = idPeople;
    }
    public People(String name, String surname, String email, int age) {
        idPeople = idPeople;
        this.name = name;
        this.surname = surname;
        Email = email;
        this.age = age;
    }

    public long getIdPeople() {
        return idPeople;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getEmail() {
        return Email;
    }
    public int getAge() {
        return age;
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
