package labaSolvd.JacoLuna.Enums;

public enum MenuOptions{
    PASSENGER,
    CREW_MEMBER,
    PLANE,
    CLASS,
    REVIEW,
    LUGGAGE,
    BOARDING_PASS,
    FLIGHT,
    END_SESSION;

    public static String printMenu(){
        StringBuilder sb = new StringBuilder("Main Menu\n");
        for (MenuOptions menu : MenuOptions.values()) {
            sb.append(menu.ordinal()).append(" - ").append(menu).append("\n");
        }
        return sb.toString();
    }
}
