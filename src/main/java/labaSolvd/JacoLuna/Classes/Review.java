package labaSolvd.JacoLuna.Classes;

public class Review {
    private long idReview;
    private long idFlight;
    private long idPassenger;
    private int rating;
    private String comment;

    public Review(int idReview, int idFlight, int idPassenger, int rating, String comment) {
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
}
