package labaSolvd.JacoLuna.Services;

import labaSolvd.JacoLuna.Classes.CrewMember;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.People;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SaxParserService extends DefaultHandler {
    private List<People> empList = null;
    private Passenger passenger = null;
    private CrewMember crewMember = null;
    private StringBuilder data = null;

    public List<People> getEmpList() {
        return empList;
    }

    boolean bName = false;
    boolean bSurname = false;
    boolean bEmail = false;
    boolean bAge = false;
    boolean bVIP = false;
    boolean bFlightPoints = false;
    boolean bHasSpecialNeeds = false;
    boolean bRole = false;
    boolean bFlightHours = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("Passenger") || qName.equalsIgnoreCase("CrewMember")) {
            String idPeople = attributes.getValue("idPeople");
            if (qName.equalsIgnoreCase("Passenger")){
                String id = attributes.getValue("idPassenger");
                passenger = new Passenger();
                passenger.setIdPassenger(Long.parseLong(id));
                passenger.setIdPeople(Long.parseLong(idPeople));
                System.out.println("passenger");
            }else{
                String id = attributes.getValue("idCrewMember");
                crewMember = new CrewMember();
                crewMember.setIdCrewMember(Long.parseLong(id));
                crewMember.setIdPeople(Long.parseLong(idPeople));
                System.out.println("crewMember");
            }

            if (empList == null) {
                empList = new ArrayList<People>();
            }
        }else if (qName.equalsIgnoreCase("Name")) {
            bName = true;
        }else if (qName.equalsIgnoreCase("Surname")) {
            bSurname = true;
        }else if (qName.equalsIgnoreCase("Email")) {
            bEmail = true;
        }else if (qName.equalsIgnoreCase("Age")) {
            bAge = true;
        }else if (qName.equalsIgnoreCase("VIP")) {
            bVIP = true;
        }else if (qName.equalsIgnoreCase("FlightPoints")) {
            bFlightPoints = true;
        }else if (qName.equalsIgnoreCase("HasSpecialNeeds")) {
            bHasSpecialNeeds = true;
        }else if (qName.equalsIgnoreCase("role")) {
            bRole = true;
        }else if (qName.equalsIgnoreCase("FlightHours")) {
            bFlightHours = true;
        }
        data = new StringBuilder();
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("Name")) {
            if (passenger != null) {
                passenger.setName(data.toString());
            }else {
                crewMember.setName(data.toString());
            }
            bName = false;
        }else if (qName.equalsIgnoreCase("Surname")) {
            if (passenger != null) {
                passenger.setSurname(data.toString());
            }else {
                crewMember.setSurname(data.toString());
            }
            bSurname = false;
        }else if (qName.equalsIgnoreCase("Email")) {
            if (passenger != null) {
                passenger.setEmail(data.toString());
            }else {
                crewMember.setEmail(data.toString());
            }
            bEmail = false;
        }else if (qName.equalsIgnoreCase("Age")) {
            if (passenger != null) {
                passenger.setAge(Integer.parseInt(data.toString()));
            }else {
                crewMember.setAge(Integer.parseInt(data.toString()));
            }
            bAge = false;
        }else if (qName.equalsIgnoreCase("VIP")) {
            passenger.setVIP(Boolean.parseBoolean(data.toString()));
            bVIP = false;
        }else if (qName.equalsIgnoreCase("FlightPoints")) {
            passenger.setFlightPoints(Integer.parseInt(data.toString()));
            bFlightPoints = false;
        }else if (qName.equalsIgnoreCase("HasSpecialNeeds")) {
            passenger.setHasSpecialNeeds(Boolean.parseBoolean(data.toString()));
            bHasSpecialNeeds = false;
        }else if (qName.equalsIgnoreCase("role")) {
            crewMember.setRole(data.toString());
            bFlightPoints = false;
        }else if (qName.equalsIgnoreCase("FlightHours")) {
            crewMember.setFlightHours(Integer.parseInt(data.toString()));
            bHasSpecialNeeds = false;
        }
        if (qName.equalsIgnoreCase("passenger")) {
            empList.add(passenger);
        }
        if (qName.equalsIgnoreCase("CrewMember")) {
            empList.add(crewMember);
        }
    }
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }
}
