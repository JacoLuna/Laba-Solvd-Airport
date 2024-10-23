package labaSolvd.JacoLuna.Services.entityServices;

import jakarta.xml.bind.JAXBException;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.Classes.xmlLists.Persons;
import labaSolvd.JacoLuna.Classes.xmlLists.Planes;
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
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

public class PassengerService implements IService<Passenger> {

    private final PassengerDAO passengerDAO;
    private final PeopleService peopleService;
    private final SourceOptions source;
    private Persons persons;
    private final List<Passenger> passengersList;

    public PassengerService(SourceOptions source) {
        this.source = source;
        this.passengerDAO = new PassengerDAO();
        this.peopleService = new PeopleService(source);
        passengersList = getAll();
        System.out.println(passengersList.size());
        if (this.source == SourceOptions.XML) {
            persons = new Persons();
            persons.getPersons().addAll(passengersList);
        }
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
        if (source == SourceOptions.DATA_BASE) {
            long id = passengerDAO.add(passenger);
            if (id != -1) {
                Utils.CONSOLE.info("Passenger {} created id:{}", name, id);
            }
        } else {
            addPassengerWithManualId(passenger);
        }
    }

    private void addPassengerWithManualId(Passenger passenger) {
        long id = 1;
        if (!passengersList.isEmpty()) {
            id = Collections.max(getAll(), Comparator.comparing(Passenger::getIdPassenger)).getIdPassenger() + 1;
        }
        passenger.setIdPassenger(id);
        addPassenger(passenger);
        overwriteFile();
    }

    private void addPassenger(Passenger passenger) {
        if (source == SourceOptions.XML)
            persons.getPersons().add(passenger);
        passengersList.add(passenger);
    }

    private void delPassenger(Passenger passenger) {
        if (source == SourceOptions.XML)
            persons.getPersons().remove(passenger);
        passengersList.remove(passenger);
    }

    private void updatePassenger(Passenger passenger) {
        int index = 0;
        if (source == SourceOptions.XML) {
            index = persons.getPersons().indexOf(passenger);
            persons.getPersons().set(index, passenger);
        }
        index = passengersList.indexOf(passenger);
        passengersList.set(index, passenger);
    }

    private void overwriteFile() {
        try {
            if (source == SourceOptions.XML)
                Marshaller.MarshallList(persons, Persons.class, XmlPaths.PEOPLE);
            else
                JsonParser.parse(passengersList, JsonPaths.PASSENGERS);
        } catch (JAXBException e) {
            Utils.CONSOLE.error("Failed to add passenger {}", e.getMessage());
        }

    }

    public Passenger getById() {
        long id = selectPassengerId();
        if (source == SourceOptions.DATA_BASE) {
            return passengerDAO.getById(id);
        } else {
            return getAll().stream()
                    .filter(p -> p.getIdPassenger() == id)
                    .findFirst()
                    .orElse(null);
        }
    }

    public boolean delete() {
        boolean result = false;
        if (source == SourceOptions.DATA_BASE) {
            try {
                passengerDAO.delete(selectPassengerId());
            } catch (Exception e) {
                Utils.CONSOLE.error("Error while deleting passenger");
            }
        } else {
            Passenger passenger = getById();
            delPassenger(passenger);
            overwriteFile();
        }
        return result;
    }

    public List<Passenger> getAll() {
        List<Passenger> passengers = new ArrayList<>();
        switch (source) {
            case XML -> {
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                try {
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    PeopleSaxParser handler = new PeopleSaxParser();
                    saxParser.parse(new File(XmlPaths.PEOPLE.path), handler);
                    List<People> empList = handler.getEmpList();
                    if (empList != null) {
                        for (People p : empList) {
                            if (p.getClass().equals(Passenger.class)) {
                                passengers.add((Passenger) p);
                            }
                        }
                    }
                } catch (ParserConfigurationException | SAXException | IOException e) {
                    Utils.CONSOLE.error("Failed to bring passengers {}", e.getMessage());
                }
            }
            case JSON -> passengers = JsonParser.unparseToList(Passenger.class, JsonPaths.PASSENGERS);
            case DATA_BASE -> passengers = passengerDAO.getList();
        }
        if (passengers == null) {
            passengers = new ArrayList<>();
        }
        return passengers;
    }

    public void update() {
        Passenger passenger = getById();
        updatePassengerAttributes(passenger);

        if (source == SourceOptions.DATA_BASE) {
            try {
                passengerDAO.update(passenger);
            } catch (Exception e) {
                Utils.CONSOLE.error("Error while updating passenger: {}", e.getMessage());
            }
        } else {
            updatePassenger(passenger);
            overwriteFile();
        }
    }

    public List<Passenger> search() {
        List<Field> attributes = getPassengerAttributes();
        List<Passenger> passengers = new ArrayList<>();
        Method method;
        try {
            int attIndex = selectAtt(attributes);
            Object value;
            Class<?> fieldType = attributes.get(attIndex).getType();

            if (attributes.get(attIndex).getType().equals(String.class)) {
                value = InputService.stringAns("Please enter the search value");
            } else {
                Utils.CONSOLE.info(fieldType.getName());
                value = switch (fieldType.getName()) {
                    case "int" -> InputService.setInput("Please enter the search value", Integer.class);
                    case "double" -> InputService.setInput("Please enter the search value", Double.class);
                    case "float" -> InputService.setInput("Please enter the search value", Float.class);
                    case "boolean" -> InputService.booleanAns("is the value true?");
                    default -> null;
                };
            }

            if (source == SourceOptions.DATA_BASE) {
                passengers = passengerDAO.search(attributes.get(attIndex).getName(), value);
            } else {
                try {
                    method = People.class.getMethod((fieldType.getName().equals(("boolean")) ? "is" : "get") + StringUtils.capitalize(attributes.get(attIndex).getName()));
                } catch (NoSuchMethodException e) {
                    method = Passenger.class.getMethod((fieldType.getName().equals(("boolean")) ? "is" : "get") + StringUtils.capitalize(attributes.get(attIndex).getName()));
                }
                for (Passenger p : getAll()) {
                    if (fieldType.getName().equals("String")) {
                        if (StringUtils.contains((String) method.invoke(p), value.toString())) {
                            passengers.add(p);
                        }
                    }else {
                            if (method.invoke(p).equals(value)) {
                                passengers.add(p);
                            }
                    }
                }
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
