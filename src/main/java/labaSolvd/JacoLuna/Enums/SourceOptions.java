package labaSolvd.JacoLuna.Enums;

public enum SourceOptions {
    DATA_BASE,
    XML,
    EXIT;
    static public String printMenu(){
        StringBuilder sb = new StringBuilder();
        for (SourceOptions option : SourceOptions.values()) {
            sb.append(option.ordinal()).append(" - ").append(option.toString()).append("\n");
        }
        return sb.toString();
    }
}
