package labaSolvd.JacoLuna.Enums;

public enum EntityOptions {
    SEE_ALL,
    CREATE,
    GET_ONE,
    UPDATE,
    DELETE,
    EXIT;

    public static String printMenu(){
        StringBuilder sb = new StringBuilder("What do you want to do?\n");
        for (EntityOptions menu : EntityOptions.values()) {
            sb.append(menu.ordinal()).append(" - ").append(menu).append("\n");
        }
        return sb.toString();
    }
}