package labaSolvd.JacoLuna.Services;

import labaSolvd.JacoLuna.Enums.EntityOptions;
import labaSolvd.JacoLuna.Enums.SourceOptions;

public class MenuService {

    public enum peopleOptions{
        PASSENGER,
        CREW_MEMBER,
        EXIT;
        static public String printMenu(){
            StringBuilder sb = new StringBuilder();
            for (peopleOptions option : peopleOptions.values()) {
                sb.append(option.ordinal()).append(" - ").append(option.toString()).append("\n");
            }
            return sb.toString();
        }
    }
    public String sourceMenu(){
        return "Data source for the session\n" + SourceOptions.printMenu();
    }
    public String peopleMenu(){
        return "Type of person\n" + peopleOptions.printMenu();
    }
    public String EntityMenu(){
        return EntityOptions.printMenu();
    }
}
