package labaSolvd.JacoLuna;

import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Services.AirportService;
import labaSolvd.JacoLuna.Services.InputService;
import labaSolvd.JacoLuna.Services.MenuService;

import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        int ans;
        boolean isValid;
        String xml, xmlSchema;
        do {
            ans = InputService.setInput("Menu\n1 - Start program\n2 - Check XML Schema\n3 - Exit", Arrays.asList(1,2,3), Integer.class);
            if(ans == 1){
                MenuService menuSrv = new MenuService();
                AirportService airportService = new AirportService();

                int sourceIndex = InputService.setInput(menuSrv.sourceMenu(), SourceOptions.values().length - 1, Integer.class);
                SourceOptions source = SourceOptions.values()[sourceIndex];
                airportService.startProgram(source);
                break;
            }else{
                xml = InputService.stringAns("Input the xml file name");
                xmlSchema = InputService.stringAns("Input the xml schema file name");
                isValid = XMLValidator.validateXML("src\\main\\resources\\xml\\" + xml + ".xml",
                        "src\\main\\resources\\xml\\xsd\\"+ xmlSchema + ".xml");
                if (isValid)
                    Utils.CONSOLE.info("is valid");
                else
                    Utils.CONSOLE.info("is not valid");
            }
        } while (ans != 3);
    }
}