package labaSolvd.JacoLuna.Services;
import jdk.jshell.execution.Util;
import labaSolvd.JacoLuna.Utils;
import org.apache.logging.log4j.core.util.FileUtils;

import java.io.*;
import java.nio.charset.Charset;

public class FileService {

    public static final String folder = "src\\main\\java\\labaSolvd\\JacoLuna\\files";
    public static String readFile(String path, boolean localFile){
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(localFile?folder + "\\" + path : path))){
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (FileNotFoundException ex) {
            System.out.println("An error occurred." + ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return sb.toString();
    }
    public static void writeFile(String path, String content, boolean localFile){
        try (FileWriter fw = new FileWriter(new File(localFile?folder + "\\" + path : path), Charset.defaultCharset());){
            fw.write(content);
        } catch (IOException e) {
            Utils.CONSOLE_ERROR.error("An error occurred.", e);
        }
    }
}
