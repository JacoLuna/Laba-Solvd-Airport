package labaSolvd.JacoLuna.Services;

import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.DAO.PeopleDAO;

import java.util.HashMap;
import java.util.Map;

public class PeopleService {
    private final PeopleDAO peopleDAO;

    public PeopleService() {
        this.peopleDAO = new PeopleDAO();
    }

    public Long add(String name, String surname, String email, int age){
        Map<String, Object> att = new HashMap<>();
        att.put("name", name);
        att.put("surname", surname);
        att.put("email", email);
        att.put("age", age);
        return peopleDAO.add(att);
    }
}
