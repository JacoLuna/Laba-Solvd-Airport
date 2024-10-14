package labaSolvd.JacoLuna.Classes;

import java.util.Objects;

public class Luggage {
    private long idLuggage;
    private long idPassenger;
    private long idFlight;
    private float size;
    private Boolean onCabin;

    public Luggage(long idLuggage, long idPassenger, long idFlight, float size, Boolean onCabin) {
        this.idLuggage = idLuggage;
        this.idPassenger = idPassenger;
        this.idFlight = idFlight;
        this.size = size;
        this.onCabin = onCabin;
    }

    public long getIdLuggage() {
        return idLuggage;
    }

    public long getIdPassenger() {
        return idPassenger;
    }

    public long getIdFlight() {
        return idFlight;
    }

    public float getSize() {
        return size;
    }

    public Boolean getOnCabin() {
        return onCabin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Luggage luggage = (Luggage) o;
        return idLuggage == luggage.idLuggage && idPassenger == luggage.idPassenger && idFlight == luggage.idFlight && Float.compare(size, luggage.size) == 0 && Objects.equals(onCabin, luggage.onCabin);
    }

    @Override
    public int hashCode() {
        return 21 * (int)idLuggage + (int)idPassenger + (int)idFlight + (int)size + onCabin.toString().length();
    }
}
