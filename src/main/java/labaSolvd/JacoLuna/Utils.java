package labaSolvd.JacoLuna;
import labaSolvd.JacoLuna.Connection.DataBaseConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {
    static public final Logger CONSOLE_ERROR = LogManager.getLogger("ConsoleErrorLogger");
    static public final Logger CONSOLE = LogManager.getLogger("ConsoleLogger");
    static public final DataBaseConnection db = new DataBaseConnection();
}
