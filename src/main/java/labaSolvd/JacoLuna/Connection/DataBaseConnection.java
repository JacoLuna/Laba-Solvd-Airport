package labaSolvd.JacoLuna.Connection;

import labaSolvd.JacoLuna.Services.FileService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DataBaseConnection {
    public static Connection getConnection() throws SQLException{
        Properties props = new Properties();
        try(InputStream input = DataBaseConnection.class.getClassLoader().getResourceAsStream("db.properties")){
            if (input == null)
                throw new IOException("Unable to find db.properties.");
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(
                props.getProperty("URL"),
                props.getProperty("USERNAME"),
                props.getProperty("PASSWORD")
        );
    }
}

