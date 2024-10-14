package labaSolvd.JacoLuna.Classes;

public class FlightCrew {
    private long idFlight;
    private long idCrewMember;

    public FlightCrew(int idFlight, int idCrewMember) {
        this.idFlight = idFlight;
        this.idCrewMember = idCrewMember;
    }

    public long getIdFlight() {
        return idFlight;
    }

    public long getIdCrewMember() {
        return idCrewMember;
    }
}
