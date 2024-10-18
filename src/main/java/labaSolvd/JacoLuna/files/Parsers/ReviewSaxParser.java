package labaSolvd.JacoLuna.files.Parsers;

import labaSolvd.JacoLuna.Classes.CrewMember;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.Review;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class ReviewSaxParser extends DefaultHandler {
    private List<Review> empList = null;
    private Review review = null;
    private StringBuilder data = null;

    public List<Review> getEmpList() {
        return empList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("Review")) {
            String idReview = attributes.getValue("idReview");
            String idFlight = attributes.getValue("idFlight");
            String idPassenger = attributes.getValue("idPassenger");
            review = new Review();
            review.setIdReview(Long.parseLong(idReview));
            review.setIdFlight(Long.parseLong(idFlight));
            review.setIdPassenger(Long.parseLong(idPassenger));
            if (empList == null) {
                empList = new ArrayList<Review>();
            }
        }
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (review != null) {
            switch (qName.toLowerCase()) {
                case "rating" -> review.setRating(Integer.parseInt(data.toString()));
                case "comment" -> review.setComment(data.toString());
            }
        }
        if (qName.equalsIgnoreCase("Review")) {
            if (review != null) {
                empList.add(review);
                review = null;
            }
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }
}
