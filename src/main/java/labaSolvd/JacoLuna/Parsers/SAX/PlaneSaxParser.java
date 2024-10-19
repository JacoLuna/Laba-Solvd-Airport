package labaSolvd.JacoLuna.Parsers.SAX;

import labaSolvd.JacoLuna.Classes.Plane;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class PlaneSaxParser extends DefaultHandler {
    private List<Plane> empList = null;
    private Plane plane = null;
    private StringBuilder data = null;

    public List<Plane> getEmpList() {
        return empList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("Plane")) {
            String idPlane = attributes.getValue("idPlane");
            plane = new Plane();
            plane.setIdPlane(idPlane);
            if (empList == null) {
                empList = new ArrayList<Plane>();
            }
        }
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (plane != null) {
            switch (qName.toLowerCase()) {
                case "fuelcapacity" -> plane.setFuelCapacity(Integer.parseInt(data.toString()));
                case "tripulationsize" -> plane.setTripulationSize(Integer.parseInt(data.toString()));
                case "economysize" -> plane.setEconomySize(Integer.parseInt(data.toString()));
                case "premiumsize" -> plane.setPremiumSize(Integer.parseInt(data.toString()));
                case "businesssize" -> plane.setBusinessSize(Integer.parseInt(data.toString()));
                case "firstclasssize" -> plane.setFirstClassSize(Integer.parseInt(data.toString()));
                case "country" -> plane.setCountry(data.toString());
            }
        }
        if (qName.equalsIgnoreCase("plane")) {
            empList.add(plane);
            plane = null;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }

}
