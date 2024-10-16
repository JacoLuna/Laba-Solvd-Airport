package labaSolvd.JacoLuna;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.Services.AirportService;
import labaSolvd.JacoLuna.Services.SaxParserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            SaxParserService handler = new SaxParserService();
            saxParser.parse(new File("D:\\Jaco\\solv course\\xmlFiles\\passengers.xml"), handler);
            List<People> empList = handler.getEmpList();
            for(People emp : empList)
                System.out.println(emp);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        AirportService airportService = new AirportService();
        airportService.startProgram();
    }
}