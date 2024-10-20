package labaSolvd.JacoLuna.Parsers.JSON;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import labaSolvd.JacoLuna.Classes.Plane;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonParser {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static <T> void parse(T obj, String path){
        File file = new File(path);
        try {
            mapper.writeValue(file, obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static <T> List<T> unparseToList(String path, Class<T> type){
        File file = new File(path);
        try {
//            return mapper.readValue(file, new TypeReference<List<T>>() {});
            return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, type));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
