package labaSolvd.JacoLuna.Services.entityServices;

import jakarta.xml.bind.JAXBException;
import labaSolvd.JacoLuna.Classes.CrewMember;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.Classes.Review;
import labaSolvd.JacoLuna.Classes.xmlLists.Persons;
import labaSolvd.JacoLuna.Classes.xmlLists.Reviews;
import labaSolvd.JacoLuna.Connection.SessionFactoryBuilder;
import labaSolvd.JacoLuna.DAO.CrewMemberDAO;
import labaSolvd.JacoLuna.DAO.EntityDAO;
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
import labaSolvd.JacoLuna.myBatysDAO.CrewMemberMapper;
import labaSolvd.JacoLuna.myBatysDAO.PeopleMapper;
import labaSolvd.JacoLuna.myBatysDAO.ReviewMapper;
import org.apache.ibatis.session.SqlSession;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

public class CrewMemberService implements IService<CrewMember> {

    private final PeopleService peopleService;
    private final SourceOptions source;
    private Persons persons;
    private final List<CrewMember> crewMembersList;

    public CrewMemberService(SourceOptions source) {
        this.peopleService = new PeopleService(source);
        this.source = source;
        if (this.source == SourceOptions.XML) {
            persons = new Persons();
        }
        crewMembersList = getAll();
    }

    @Override
    public void add() {
        String name = InputService.stringAns("Please input name: ");
        String surname = InputService.stringAns("Please input surname: ");
        String email = InputService.stringAns("Please input email: ");
        int age = InputService.setInput("Please input age: ", Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.class);
        String role = InputService.stringAns("Please input role: ");
        long idPeople = peopleService.add(name, surname, email, age);
        CrewMember crewMember = new CrewMember(idPeople, name, surname, email, age, role, 0);
        addCrewMember(crewMember);
        switch (source) {
            case XML -> addCrewMemberWithXML(crewMember);
            case JSON -> JsonParser.parse(crewMembersList, JsonPaths.PEOPLE);
            case DATA_BASE -> {
//                EntityDAO.executeQuery(PeopleMapper.class, "insertPeople", crewMember);
                EntityDAO.executeQuery(CrewMemberMapper.class, "insertCrewMember", crewMember);
            }
        }
    }

    private void addCrewMemberWithXML(CrewMember crewMember) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        List<CrewMember> crewMembers = new ArrayList<>();
        long id = 1;
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            PeopleSaxParser handler = new PeopleSaxParser();
            saxParser.parse(new File(XmlPaths.PEOPLE.path), handler);
            List<People> empList = handler.getEmpList();
            if (empList != null) {
                if (!empList.isEmpty()) {
                    empList.forEach(c -> {
                        if (c.getClass().equals(CrewMember.class)) {
                            crewMembers.add((CrewMember) c);
                        }
                    });

                    id = Collections.max(crewMembers, Comparator.comparing(CrewMember::getIdCrewMember)).getIdCrewMember() + 1;
                }
            }
            crewMember.setIdCrewMember(id);
            addCrewMember(crewMember);
            Marshaller.MarshallList(persons, Persons.class, XmlPaths.PEOPLE);
        } catch (ParserConfigurationException | SAXException | IOException | JAXBException e) {
            Utils.CONSOLE_ERROR.error(e);
        }
    }

    private void addCrewMember(CrewMember crewMember) {
        if (persons != null)
            persons.getPersons().add(crewMember);
        crewMembersList.add(crewMember);
    }

    private void delCrewMember(CrewMember crewMember) {
        if (persons != null)
            persons.getPersons().remove(crewMember);
        crewMembersList.remove(crewMember);
    }

    private void updateCrewMember(CrewMember crewMember) {
        int index = crewMembersList.indexOf(crewMember);
        crewMembersList.set(index, crewMember);
        if (persons != null) {
            index = persons.getPersons().indexOf(crewMember);
            persons.getPersons().set(index, crewMember);
        }
    }

    @Override
    public CrewMember getById() {
        long id = selectCrewMemberId();
        return switch (source) {
            case XML -> null;
            case JSON -> null;
            case DATA_BASE -> EntityDAO.executeQuery(CrewMemberMapper.class, "getCrewMember", id);
            default -> null;
        };
    }

    @Override
    public boolean delete() {
        boolean result = false;
        long id = selectCrewMemberId();
        switch (source) {
            case XML -> result = false;
            case JSON -> result = false;
            case DATA_BASE -> EntityDAO.executeQuery(CrewMemberMapper.class, "deleteCrewMember", id);
        }
        return result;
    }

    @Override
    public List<CrewMember> getAll() {
        return switch (source) {
            case XML -> {
                try {
                    yield Marshaller.UnMarshallList(Reviews.class, XmlPaths.PEOPLE);
                } catch (JAXBException e) {
                    Utils.CONSOLE_ERROR.error(e);
                    yield null;
                }
            }
            case JSON -> JsonParser.unparseToList(CrewMember.class, JsonPaths.PEOPLE);
            case DATA_BASE -> EntityDAO.executeQuery(CrewMemberMapper.class, "getAllCrewMembers");
            default -> null;
        };
    }

    @Override
    public void update() {
        CrewMember crewMember = getById();
        updateCrewMemberAttributes(crewMember);
        System.out.println(crewMember.toString());
        switch (source) {
            case DATA_BASE -> {
                EntityDAO.executeQuery(PeopleMapper.class, "updatePeople", crewMember);
                EntityDAO.executeQuery(CrewMemberMapper.class, "updateCrewMember", crewMember);
            }
        }
//            case XML -> ;
//            case JSON -> ;

//        try {
//            passengerDAO.update(passenger);
//        } catch (Exception e) {
//            Utils.CONSOLE.error("Error while updating passenger: {}", e.getMessage());
//        }
    }

    @Override
    public List<CrewMember> search() {
        List<Field> attributes = getCrewMemberAttributes();
        List<CrewMember> crewMembers = new ArrayList<>();
        Object value;
        int attIndex = selectAtt(attributes);
        Field att = attributes.get(attIndex);
        String prompt = "Search value: ";
        if (att.getType().equals(String.class)) {
            value = InputService.stringAns("Please enter the search value");
        } else {
            Class<?> fieldType = att.getType();
            value = switch (fieldType.getName()) {
                case "int" -> InputService.setInput(prompt, Integer.class);
                case "double" -> InputService.setInput(prompt, Double.class);
                case "float" -> InputService.setInput(prompt, Float.class);
                case "boolean" -> InputService.booleanAns("is the value true?");
                default -> null;
            };
        }

        if (value != null) {
            switch (source) {
                case XML -> crewMembers = crewMembersList;
                case JSON -> crewMembers = crewMembersList;
                case DATA_BASE ->
                        crewMembers = EntityDAO.executeQuery(CrewMemberMapper.class, (att.getType().equals(String.class)) ? "searchByString" : "searchByOther", att.getName(), value);
            }
        }
        return crewMembers;
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

    private long selectCrewMemberId() {
        List<CrewMember> crewMembers = getAll();
        StringBuilder sb = new StringBuilder("Select the id of the crew member");
        List<Long> ids = new ArrayList<>();
        crewMembers.forEach(c -> {
            ids.add(c.getIdCrewMember());
            sb.append("\n").append(c.getIdCrewMember()).append(" ").append(c.getName());
        });
        sb.append("\n");
        return InputService.setInput(sb.toString(), ids, Long.class);
    }

    private void updateCrewMemberAttributes(CrewMember crewMember) {
        List<Field> attributes = getCrewMemberAttributes();
        int ans;
        do {
            ans = selectAtt(attributes);
            if (ans < attributes.size()) {
                updateAttribute(crewMember, attributes.get(ans));
            }
        } while (ans != attributes.size());
    }

    private List<Field> getCrewMemberAttributes() {
        List<Field> superAttributes = new LinkedList<>(Arrays.asList(CrewMember.class.getSuperclass().getDeclaredFields()));
        List<Field> childAttributes = new LinkedList<>(Arrays.asList(CrewMember.class.getDeclaredFields()));
        childAttributes.removeFirst();
        return Stream.concat(superAttributes.stream(), childAttributes.stream()).toList();
    }

    private void updateAttribute(CrewMember crewMember, Field field) {
        field.setAccessible(true);
        switch (field.getName().toLowerCase()) {
            case "name" -> crewMember.setName(InputService.stringAns("Name: "));
            case "surname" -> crewMember.setSurname(InputService.stringAns("Surname: "));
            case "email" -> crewMember.setEmail(InputService.stringAns("Email: "));
            case "age" -> crewMember.setAge(InputService.setInput(
                    "How old is " + crewMember.getName() + "?", 0, Integer.MAX_VALUE, Integer.class));
            case "role" -> crewMember.setRole(InputService.stringAns("Role: "));
            case "flighthours" ->
                    crewMember.setFlightHours(InputService.setInput("Flight hours: ", 0, Integer.MAX_VALUE, Integer.class));
        }
    }
}
