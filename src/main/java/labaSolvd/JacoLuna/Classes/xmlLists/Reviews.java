package labaSolvd.JacoLuna.Classes.xmlLists;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Classes.Review;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Reviews")
@XmlAccessorType(XmlAccessType.FIELD)
public class Reviews {
    @XmlElement(name = "Review")
    private List<Review> reviews = null;
    public Reviews() {
        reviews = new ArrayList<Review>();
    }
    public List<Review> getReviews() {
        return reviews;
    }
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
