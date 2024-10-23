package labaSolvd.JacoLuna.Services.entityServices;

import labaSolvd.JacoLuna.Classes.CrewMember;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.DAO.PeopleDAO;
import labaSolvd.JacoLuna.Enums.JsonPaths;
import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Enums.XmlPaths;
import labaSolvd.JacoLuna.Parsers.JSON.JsonParser;
import labaSolvd.JacoLuna.Parsers.SAX.PeopleSaxParser;
import labaSolvd.JacoLuna.Utils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PeopleService {
    private final PeopleDAO peopleDAO;
    private final SourceOptions source;

    public PeopleService(SourceOptions source) {
        this.peopleDAO = new PeopleDAO();
        this.source = source;
    }
    public Long add(String name, String surname, String email, int age){
        long id = -1;
        switch (source){
            case DATA_BASE -> {
                Map<String, Object> att = new HashMap<>();
                att.put("name", name);
                att.put("surname", surname);
                att.put("email", email);
                att.put("age", age);
                id = peopleDAO.add(att);
            }
            case XML -> {
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                try {
                    SAXParser saxParser = saxParserFactory.newSAXParser();
                    PeopleSaxParser handler = new PeopleSaxParser();
                    saxParser.parse(new File(XmlPaths.PEOPLE.path), handler);
                    List<People> empList = handler.getEmpList();
                    if (empList != null){
                        id = Collections.max(empList, Comparator.comparing(People::getIdPeople)).getIdPeople() + 1;
                    }else {
                        id = 1;
                    }
                } catch (ParserConfigurationException | SAXException | IOException e) {
                    Utils.CONSOLE.error("Failed to add passenger {}", e.getMessage());
                }
            }
            case JSON ->{
                long idPeople;
                List<Passenger> passengers = JsonParser.unparseToList(Passenger.class, JsonPaths.PASSENGERS);
                List<CrewMember> crewMembers = JsonParser.unparseToList(CrewMember.class, JsonPaths.CREW_MEMBERS);
                if (passengers != null && crewMembers != null){
                    id = 1;
                }else {
                    long maxCrewId = (crewMembers != null && !crewMembers.isEmpty())
                            ? Collections.max(crewMembers, Comparator.comparing(People::getIdPeople)).getIdPeople()
                            : Long.MIN_VALUE;

                    long maxPassengerId = (passengers != null && !passengers.isEmpty())
                            ? Collections.max(passengers, Comparator.comparing(People::getIdPeople)).getIdPeople()
                            : Long.MIN_VALUE;

                    id = Math.max(maxCrewId, maxPassengerId)+1;
                }
            }
        }
        return id;
    }
}
