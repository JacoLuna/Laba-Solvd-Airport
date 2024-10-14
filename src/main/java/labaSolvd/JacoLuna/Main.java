package labaSolvd.JacoLuna;
import labaSolvd.JacoLuna.Services.AirportService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        AirportService airportService = new AirportService();
        airportService.startProgram();
    }
}