package labaSolvd.JacoLuna;
import labaSolvd.JacoLuna.Services.DataBaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.*;

public class Utils {
    static public final Logger CONSOLE_ERROR = LogManager.getLogger("ConsoleErrorLogger");
    static public final Logger CONSOLE = LogManager.getLogger("ConsoleLogger");
    static public final DataBaseService db = new DataBaseService();
}
