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

public class PassengerService extends EntityService<Passenger> implements IService<Passenger> {

    private final PassengerDAO passengerDAO;
    private final PeopleService peopleService;
    private final SourceOptions source;
    private Persons persons;
    private final List<Passenger> passengersList;

    public PassengerService(SourceOptions source) {
        super(Passenger.class);
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
            if (empList != null) {
                if (!empList.isEmpty()) {
                    empList.forEach(p -> {
                        if (p.getClass().equals(Passenger.class)) {
                            passengers.add((Passenger) p);
                        }
                    });

                    id = Collections.max(passengers, Comparator.comparing(Passenger::getIdPassenger)).getIdPassenger() + 1;
                }
            }
            passenger.setIdPassenger(id);
            addPassenger(passenger);
            Marshaller.MarshallList(persons, Persons.class, XmlPaths.PEOPLE);
        } catch (ParserConfigurationException | SAXException | IOException | JAXBException e) {
            Utils.CONSOLE_ERROR.error(e);
        }
    }

    private void addPassenger(Passenger passenger) {
        if (persons != null)
            persons.getPersons().add(passenger);
        passengersList.add(passenger);
    }

    private void delPassenger(Passenger passenger) {
        if (persons != null)
            persons.getPersons().remove(passenger);
        passengersList.remove(passenger);
    }

    private void updatePassenger(Passenger passenger) {
        int index = passengersList.indexOf(passenger);
        passengersList.set(index, passenger);
        if (persons != null) {
            index = persons.getPersons().indexOf(passenger);
            persons.getPersons().set(index, passenger);
        }
    }

    public Passenger getById() {
        return passengerDAO.getById(super.selectId());
    }

    public boolean delete() {
        boolean result = false;
        try {
            passengerDAO.delete(super.selectId());
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
        super.updateEntityAttributes(passenger);
        try {
            passengerDAO.update(passenger);
        } catch (Exception e) {
            Utils.CONSOLE.error("Error while updating passenger: {}", e.getMessage());
        }
    }

    public List<Passenger> search() {
        List<Field> attributes = super.getEntityAttributes();
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
}