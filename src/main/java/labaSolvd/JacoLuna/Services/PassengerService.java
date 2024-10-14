package labaSolvd.JacoLuna.Services;

import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.DAO.PassengerDAO;
import labaSolvd.JacoLuna.Utils;

import java.awt.*;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class PassengerService {

    private final PassengerDAO passengerDAO;
    private PeopleService peopleService;

    public PassengerService() {
        this.passengerDAO = new PassengerDAO();
        this.peopleService = new PeopleService();
    }
    public void add(){
        String name = InputService.stringAns("Please enter your name: ");
        String surname = InputService.stringAns("Please enter your surname: ");
        String email = InputService.stringAns("Please enter your email: ");
        int age = InputService.setInput("Please enter your age: ", Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.class);
        boolean VIP = InputService.setInput("Is " + name + " VIP?\n0 - No\n1 - Yes\n", Arrays.asList(0, 1), Integer.class) == 1;
        boolean hasSpecialNeeds = InputService.setInput("Does " + name + " have special needs?\n0 - No\n1 - Yes\n", Arrays.asList(0, 1), Integer.class) == 1;
        add(name, surname, email, age, VIP, 0, hasSpecialNeeds);
    }
    public void add(String name, String surname, String email, int age, boolean VIP, int flightPoints, boolean hasSpecialNeeds){
        long idPeople = peopleService.add(name,surname,email,age);
        Passenger passenger = new Passenger(idPeople, name, surname, email, age, VIP, flightPoints, hasSpecialNeeds);
        long id = passengerDAO.add(passenger);
        if (id != -1){
            Utils.CONSOLE.info("Passenger {} created id:{}", name, id);
        }
    }

    public Passenger getById(long id){
        return passengerDAO.getById(id);
    }

    public boolean delete(){
        boolean result = false;
        try{
            passengerDAO.delete( selectPassengerId() );
        } catch (Exception e) {
            Utils.CONSOLE.error("Error while deleting passenger");
        }
        return result;
    }

    public List<Passenger> getAll(){
        return passengerDAO.getList();
    }

    public void update() {
        Passenger passenger = getById( selectPassengerId() );
        updatePassengerAttributes(passenger);
        try {
            passengerDAO.update(passenger);
        } catch (Exception e) {
            Utils.CONSOLE.error("Error while updating passenger: {}", e.getMessage());
        }
    }

    private long selectPassengerId() {
        List<Passenger> passengers = getAll();
        StringBuilder sb = new StringBuilder("Select the id of the passenger");
        List<Long> ids = new ArrayList<>();
        passengers.forEach(p -> {
            ids.add(p.getIdPassenger());
            sb.append("\n").append(p.getIdPassenger()).append(" ").append(p.getName());
        });
        sb.append("\n");
        return InputService.setInput(sb.toString(), ids, Long.class);
    }

    private void updatePassengerAttributes(Passenger passenger) {
        List<Field> attributes = getPassengerAttributes();
        int ans;
        do {
            StringBuilder sb = new StringBuilder("Select attribute to change\n");
            for (int i = 1; i < attributes.size(); i++) {
                sb.append("\n").append(i).append(" ").append(attributes.get(i).getName());
            }
            sb.append("\n").append(attributes.size()).append(" none\n");
            ans = InputService.setInput(sb.toString(), attributes.size(), Integer.class);
            if (ans < attributes.size()) {
                updateAttribute(passenger, attributes.get(ans));
            }
        } while (ans != attributes.size());
    }

    private List<Field> getPassengerAttributes() {
        List<Field> superAttributes = new LinkedList<>(Arrays.asList(Passenger.class.getSuperclass().getDeclaredFields()));
        List<Field> childAttributes = new LinkedList<>(Arrays.asList(Passenger.class.getDeclaredFields()));
        childAttributes.removeFirst();
        return Stream.concat(superAttributes.stream(), childAttributes.stream()).toList();
    }

    private void updateAttribute(Passenger passenger, Field field) {
        field.setAccessible(true);
        switch (field.getName()) {
            case "name" -> passenger.setName(InputService.stringAns("Name: "));
            case "surname" -> passenger.setSurname(InputService.stringAns("Surname: "));
            case "email" -> passenger.setEmail(InputService.stringAns("Email: "));
            case "age" -> passenger.setAge(InputService.setInput(
                    "How old is " + passenger.getName() + "?", 0, Integer.MAX_VALUE, Integer.class));
            case "VIP" -> passenger.setVIP(InputService.setInput("Is " + passenger.getName() + " VIP?\n0 - No\n1 - Yes", Arrays.asList(0, 1), Integer.class) == 1);
            case "flightPoints" -> passenger.setFlightPoints(InputService.setInput(
                    "How many flight points does the passenger have?", 0, Integer.MAX_VALUE, Integer.class));
            case "hasSpecialNeeds" -> passenger.setHasSpecialNeeds(InputService.setInput(
                    "Does " + passenger.getName() + " have special needs?\n0 - No\n1 - Yes", Arrays.asList(0, 1), Integer.class) == 1);
        }
    }
}
