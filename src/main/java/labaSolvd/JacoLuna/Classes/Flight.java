package labaSolvd.JacoLuna.Classes;

public class Flight {
    private long idFlight;
    private float duration;
    private String startingPoint;
    private String destination;
    private long idPlane;

    public Flight(long idFlight, float duration, String startingPoint, String destination, int idPlane) {
        this.idFlight = idFlight;
        this.duration = duration;
        this.startingPoint = startingPoint;
        this.destination = destination;
        this.idPlane = idPlane;
    }

    public long getIdFlight() {
        return idFlight;
    }

    public float getDuration() {
        return duration;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public String getDestination() {
        return destination;
    }

    public long getIdPlane() {
        return idPlane;
    }
}
