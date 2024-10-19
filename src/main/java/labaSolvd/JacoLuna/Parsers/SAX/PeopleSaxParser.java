package labaSolvd.JacoLuna.Parsers.SAX;

import labaSolvd.JacoLuna.Classes.CrewMember;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.People;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class PeopleSaxParser extends DefaultHandler {
    private List<People> empList = null;
    private Passenger passenger = null;
    private CrewMember crewMember = null;
    private StringBuilder data = null;

    public List<People> getEmpList() {
        return empList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName.toLowerCase()) {
            case "people" -> {
                String idPeople = attributes.getValue("idPeople");
                String Type = attributes.getValue("xsi:type");
                if (Type != null && Type.equalsIgnoreCase("Passenger")) {
                    String id = attributes.getValue("idPassenger");
                    passenger = new Passenger();
                    passenger.setIdPassenger(Long.parseLong(id));
                    passenger.setIdPeople(Long.parseLong(idPeople));
                } else if (Type != null && Type.equalsIgnoreCase("CrewMember")) {
                    String id = attributes.getValue("idCrewMember");
                    crewMember = new CrewMember();
                    crewMember.setIdCrewMember(Long.parseLong(id));
                    crewMember.setIdPeople(Long.parseLong(idPeople));
                }
                if (empList == null) {
                    empList = new ArrayList<People>();
                }
            }
        }
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (passenger != null) {
            switch (qName.toLowerCase()) {
                case "name" -> passenger.setName(data.toString());
                case "surname" -> passenger.setSurname(data.toString());
                case "email" -> passenger.setEmail(data.toString());
                case "age" -> passenger.setAge(Integer.parseInt(data.toString()));
                case "vip" -> passenger.setVIP(Boolean.parseBoolean(data.toString()));
                case "flightpoints" -> passenger.setFlightPoints(Integer.parseInt(data.toString()));
                case "hasspecialneeds" -> passenger.setHasSpecialNeeds(Boolean.parseBoolean(data.toString()));
            }
        } else {
            switch (qName.toLowerCase()) {
                case "name" -> crewMember.setName(data.toString());
                case "surname" -> crewMember.setSurname(data.toString());
                case "email" -> crewMember.setEmail(data.toString());
                case "age" -> crewMember.setAge(Integer.parseInt(data.toString()));
                case "role" -> crewMember.setRole(data.toString());
                case "flighthours" -> crewMember.setFlightHours(Integer.parseInt(data.toString()));
            }
        }
        if (qName.equalsIgnoreCase("People")) {
            if (passenger != null) {
                empList.add(passenger);
                passenger = null;
            } else if (crewMember != null) {
                empList.add(crewMember);
                crewMember = null;
            }
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }
}
