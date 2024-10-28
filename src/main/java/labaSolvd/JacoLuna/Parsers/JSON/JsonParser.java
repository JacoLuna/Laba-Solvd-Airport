package labaSolvd.JacoLuna.Parsers.JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import labaSolvd.JacoLuna.Enums.JsonPaths;
import labaSolvd.JacoLuna.Services.FileService;
import labaSolvd.JacoLuna.Utils;
import org.apache.logging.log4j.core.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonParser {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static <T> void parse(T obj, JsonPaths path){

        File file = new File(path.path);
        try {
            file.createNewFile();
            mapper.writeValue(file, obj);
        } catch (IOException e) {
            Utils.CONSOLE_ERROR.error(e.getMessage());
        }
    }
    public static <T> List<T> unparseToList(Class<T> type, JsonPaths path){
        File file = new File(path.path);
        try {
            return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, type));
        } catch (IOException e) {
            Utils.CONSOLE_ERROR.error(e.getMessage());
        }
        return null;
    }
}
