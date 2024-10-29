package labaSolvd.JacoLuna.Services.entityServices;

import jakarta.xml.bind.JAXBException;
import labaSolvd.JacoLuna.Classes.CrewMember;
import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.Classes.xmlLists.Persons;
import labaSolvd.JacoLuna.Classes.xmlLists.Reviews;
import labaSolvd.JacoLuna.DAO.EntityDAO;
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
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class CrewMemberService extends EntityService<CrewMember> implements IService<CrewMember> {

    private final PeopleService peopleService;
    private final SourceOptions source;
    private Persons persons;
    private final List<CrewMember> crewMembersList;

    public CrewMemberService(SourceOptions source) {
        super(CrewMember.class);
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
            case DATA_BASE -> EntityDAO.executeQuery(CrewMemberMapper.class, "insertCrewMember", crewMember);
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
//        long id = selectCrewMemberId();
        long id = super.selectId();
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
//        long id = selectCrewMemberId();
        long id = super.selectId();
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
        updateEntityAttributes(crewMember);
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
//        List<Field> attributes = getCrewMemberAttributes();
        List<Field> attributes = super.getEntityAttributes();
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
}
