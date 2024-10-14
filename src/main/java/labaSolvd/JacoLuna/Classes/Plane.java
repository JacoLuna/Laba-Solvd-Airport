package labaSolvd.JacoLuna.Classes;

public class Plane {
    private String idPlane;
    private String fuelCapacity;
    private int tripulationSize;
    private int economySize;
    private int premiumSize;
    private int businessSize;
    private int firstClassSize;
    private String country;

    public Plane(String idPlane, String fuelCapacity, int tripulationSize, int economySize, int premiumSize, int businessSize, int firstClassSize, String country) {
        this.idPlane = idPlane;
        this.fuelCapacity = fuelCapacity;
        this.tripulationSize = tripulationSize;
        this.economySize = economySize;
        this.premiumSize = premiumSize;
        this.businessSize = businessSize;
        this.firstClassSize = firstClassSize;
        this.country = country;
    }

    public String getIdPlane() {
        return idPlane;
    }

    public String getFuelCapacity() {
        return fuelCapacity;
    }

    public int getTripulationSize() {
        return tripulationSize;
    }

    public int getEconomySize() {
        return economySize;
    }

    public int getPremiumSize() {
        return premiumSize;
    }

    public int getBusinessSize() {
        return businessSize;
    }

    public int getFirstClassSize() {
        return firstClassSize;
    }

    public String getCountry() {
        return country;
    }
}
