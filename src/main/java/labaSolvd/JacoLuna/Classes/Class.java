package labaSolvd.JacoLuna.Classes;

public class Class {
    private long idClass;
    private String name;
    private boolean hasFood;
    private boolean hasSupplies;
    private boolean hasSpecialServices;

    public Class(int idClass, String name, boolean hasFood, boolean hasSuplies, boolean hasSpecialServices) {
        this.idClass = idClass;
        this.name = name;
        this.hasFood = hasFood;
        this.hasSupplies = hasSuplies;
        this.hasSpecialServices = hasSpecialServices;
    }
    public long getIdClass() {
        return idClass;
    }
    public String getName() {
        return name;
    }
    public boolean isHasFood() {
        return hasFood;
    }
    public boolean isHasSupplies() {
        return hasSupplies;
    }
    public boolean isHasSpecialServices() {
        return hasSpecialServices;
    }
}
