package labaSolvd.JacoLuna.Services;

public class MenuService {

    public enum InitializationOptions{
        DATABASE,
        XML,
        EXIT;
        static public String getMenu(){
            StringBuilder sb = new StringBuilder();
            for (InitializationOptions option : InitializationOptions.values()) {
                sb.append(option.ordinal()).append(" - ").append(option.toString()).append("\n");
            }
            return sb.toString();
        }
    }
    public enum peopleOptions{
        PASSENGER,
        CREW_MEMBER,
        EXIT;
        static public String getMenu(){
            StringBuilder sb = new StringBuilder();
            for (peopleOptions option : peopleOptions.values()) {
                sb.append(option.ordinal()).append(" - ").append(option.toString()).append("\n");
            }
            return sb.toString();
        }
    }
    public String initializationMenu(){
        return "Data source for the session\n" + InitializationOptions.getMenu();
    }
    public String peopleMenu(){
        return "Type of person\n" + peopleOptions.getMenu();
    }
}
