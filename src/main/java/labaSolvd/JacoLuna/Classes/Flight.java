package labaSolvd.JacoLuna.Classes;

import java.time.LocalDate;

public class Flight {
    private long idFlight;
    private float duration;
    private String startingPoint;
    private String destination;
    private long idPlane;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private String departureHour;
    private String arrivalHour;

    public Flight() {}
    public Flight(long idFlight, float duration, String startingPoint, String destination, int idPlane,
                  String country,LocalDate departureDate, LocalDate arrivalDate, String departureHour, String arrivalHour) {
        this.idFlight = idFlight;
        this.duration = duration;
        this.startingPoint = startingPoint;
        this.destination = destination;
        this.idPlane = idPlane;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.departureHour = departureHour;
        this.arrivalHour = arrivalHour;
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

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public String getDepartureHour() {
        return departureHour;
    }

    public String getArrivalHour() {
        return arrivalHour;
    }

    public void setIdFlight(long idFlight) {
        this.idFlight = idFlight;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setIdPlane(long idPlane) {
        this.idPlane = idPlane;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setDepartureHour(String departureHour) {
        this.departureHour = departureHour;
    }

    public void setArrivalHour(String arrivalHour) {
        this.arrivalHour = arrivalHour;
    }
}
