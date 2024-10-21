package labaSolvd.JacoLuna.Services.entityServices;

import jakarta.xml.bind.JAXBException;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.Classes.xmlLists.Persons;
import labaSolvd.JacoLuna.DAO.PassengerDAO;
import labaSolvd.JacoLuna.Enums.JsonPaths;
import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Enums.XmlPaths;
import labaSolvd.JacoLuna.Interfaces.IService;
import labaSolvd.JacoLuna.Parsers.JAX.Marshaller;
import labaSolvd.JacoLuna.Parsers.JSON.JsonParser;
import labaSolvd.JacoLuna.Parsers.SAX.PeopleSaxParser;
import labaSolvd.JacoLuna.Services.InputService;
import labaSolvd.JacoLuna.Utils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

public class PassengerService implements IService<Passenger> {

    private final PassengerDAO passengerDAO;
    private final PeopleService peopleService;
    private final SourceOptions source;
    private Persons persons;
    private final List<Passenger> passengersList;

    public PassengerService(SourceOptions source) {
        this.passengerDAO = new PassengerDAO();
        this.peopleService = new PeopleService(source);
        this.source = source;
        if (this.source == SourceOptions.XML) {
            persons = new Persons();
        }
        passengersList = getAll();
    }

    public void add() {
        String name = InputService.stringAns("Please enter your name: ");
        String surname = InputService.stringAns("Please enter your surname: ");
        String email = InputService.stringAns("Please enter your email: ");
        int age = InputService.setInput("Please enter your age: ", Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.class);
        boolean VIP = InputService.booleanAns("Is " + name + " VIP?");
        boolean hasSpecialNeeds = InputService.booleanAns("Does " + name + " have special needs?");

        long idPeople = peopleService.add(name, surname, email, age);
        Passenger passenger = new Passenger(idPeople, name, surname, email, age, VIP, 0, hasSpecialNeeds);
        addPassenger(passenger);
        switch (source) {
            case XML -> addPassengerWithXML(passenger);
            case JSON -> JsonParser.parse(passengersList, JsonPaths.PEOPLE);
            case DATA_BASE -> {
                long id = passengerDAO.add(passenger);
                if (id != -1) {
                    Utils.CONSOLE.info("Passenger {} created id:{}", name, id);
                }
            }
        }
    }

    private void addPassengerWithXML(Passenger passenger) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        List<Passenger> passengers = new ArrayList<>();
        long id = 1;
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            PeopleSaxParser handler = new PeopleSaxParser();
            saxParser.parse(new File(XmlPaths.PEOPLE.path), handler);
            List<People> empList = handler.getEmpList();
            if (empList != null){
                if (!empList.isEmpty()){
                    empList.forEach(p -> {
                        if (p.getClass().equals(Passenger.class)) {
                            passengers.add((Passenger) p);
                        }
                    });

                    id = Collections.max(passengers, Comparator.comparing(Passenger::getIdPassenger)).getIdPassenger() + 1;
                }
            }
            passenger.setIdPassenger(id);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        addPassenger(passenger);
        try {
            Marshaller.MarshallList(persons, Persons.class, XmlPaths.PEOPLE);
        } catch (JAXBException e) {
            Utils.CONSOLE_ERROR.error(e);
        }
    }

    private void addPassenger(Passenger passenger) {
        persons.getPersons().add(passenger);
        passengersList.add(passenger);
    }

    private void delPassenger(Passenger passenger) {
        persons.getPersons().remove(passenger);
        passengersList.remove(passenger);
    }

    private void updatePassenger(Passenger passenger) {
        int index = persons.getPersons().indexOf(passenger);
        persons.getPersons().set(index, passenger);
        index = passengersList.indexOf(passenger);
        passengersList.set(index, passenger);
    }

    public Passenger getById() {
        return passengerDAO.getById(selectPassengerId());
    }

    public boolean delete() {
        boolean result = false;
        try {
            passengerDAO.delete(selectPassengerId());
        } catch (Exception e) {
            Utils.CONSOLE.error("Error while deleting passenger");
        }
        return result;
    }

    public List<Passenger> getAll() {
        return passengerDAO.getList();
    }

    public void update() {
        Passenger passenger = getById();
        updatePassengerAttributes(passenger);
        try {
            passengerDAO.update(passenger);
        } catch (Exception e) {
            Utils.CONSOLE.error("Error while updating passenger: {}", e.getMessage());
        }
    }

    public List<Passenger> search() {
        List<Field> attributes = getPassengerAttributes();
        List<Passenger> passengers = new ArrayList<>();
        try {
            Object value;
            int attIndex = selectAtt(attributes);

            if (attributes.get(attIndex).getType().equals(String.class)) {
                value = InputService.stringAns("Please enter the search value");
                passengers = passengerDAO.search(attributes.get(attIndex).getName(), (String) value);
            } else {

                Class<?> fieldType = attributes.get(attIndex).getType();
                Utils.CONSOLE.info(fieldType.getName());
                value = switch (fieldType.getName()) {
                    case "int" -> InputService.setInput("Please enter the search value", Integer.class);
                    case "double" -> InputService.setInput("Please enter the search value", Double.class);
                    case "float" -> InputService.setInput("Please enter the search value", Float.class);
                    case "boolean" -> InputService.booleanAns("is the value true?");
                    default -> null;
                };
                if (value != null)
                    passengers = passengerDAO.search(attributes.get(attIndex).getName(), value);
            }
        } catch (Exception e) {
            Utils.CONSOLE.error("Error while searching passengers: {}", e.getMessage());
        }
        return passengers;
    }

    private int selectAtt(List<Field> attributes) {
        int ans;
        StringBuilder sb = new StringBuilder("Select an attribute\n");
        for (int i = 1; i < attributes.size(); i++) {
            sb.append("\n").append(i).append(" ").append(attributes.get(i).getName());
        }
        sb.append("\n").append(attributes.size()).append(" none\n");
        ans = InputService.setInput(sb.toString(), attributes.size(), Integer.class);
        return ans;
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
            ans = selectAtt(attributes);
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
            case "VIP" ->
                    passenger.setVIP(InputService.setInput("Is " + passenger.getName() + " VIP?\n0 - No\n1 - Yes", Arrays.asList(0, 1), Integer.class) == 1);
            case "flightPoints" -> passenger.setFlightPoints(InputService.setInput(
                    "How many flight points does the passenger have?", 0, Integer.MAX_VALUE, Integer.class));
            case "hasSpecialNeeds" -> passenger.setHasSpecialNeeds(InputService.setInput(
                    "Does " + passenger.getName() + " have special needs?\n0 - No\n1 - Yes", Arrays.asList(0, 1), Integer.class) == 1);
        }
    }
}
