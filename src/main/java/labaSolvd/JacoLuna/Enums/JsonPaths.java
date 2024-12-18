package labaSolvd.JacoLuna.Enums;

public enum JsonPaths {
    PLANES("Planes.json"),
    REVIEWS("Reviews.json"),
    PEOPLE("People.json"),
    PASSENGERS("Passengers.json"),
    CREW_MEMBERS("CrewMembers.json");
    public String path = "src\\main\\resources\\json\\";

    JsonPaths(String s) {
        StringBuilder sb = new StringBuilder(path);
        sb.append(s);
        path = sb.toString();
    }
}
