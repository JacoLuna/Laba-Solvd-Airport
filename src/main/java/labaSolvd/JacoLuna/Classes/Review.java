package labaSolvd.JacoLuna.Classes;

import jakarta.xml.bind.annotation.*;

import java.util.Objects;

@XmlRootElement(name = "Review")
@XmlAccessorType(XmlAccessType.FIELD)
public class Review {
    @XmlAttribute(name = "idReview")
    private long idReview;
    @XmlAttribute(name = "idFlight")
    private long idFlight;
    @XmlAttribute(name = "idPassenger")
    private long idPassenger;
    @XmlElement(name = "rating")
    private int rating;
    @XmlElement(name = "comment")
    private String comment;

    public Review(){
    }
    public Review(long idReview, long idFlight, long idPassenger, int rating, String comment) {
        this.idReview = idReview;
        this.idFlight = idFlight;
        this.idPassenger = idPassenger;
        this.rating = rating;
        this.comment = comment;
    }

    public long getIdReview() {
        return idReview;
    }

    public long getIdFlight() {
        return idFlight;
    }

    public long getIdPassenger() {
        return idPassenger;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public void setIdReview(long idReview) {
        this.idReview = idReview;
    }

    public void setIdFlight(long idFlight) {
        this.idFlight = idFlight;
    }

    public void setIdPassenger(long idPassenger) {
        this.idPassenger = idPassenger;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Review{" +
                "idReview=" + idReview +
                ", idFlight=" + idFlight +
                ", idPassenger=" + idPassenger +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return idReview == review.idReview && idFlight == review.idFlight && idPassenger == review.idPassenger && rating == review.rating && Objects.equals(comment, review.comment);
    }

    @Override
    public int hashCode() {
        return 21 * (int)idReview + (int)idFlight + (int)idPassenger + rating + comment.length();
    }
}
