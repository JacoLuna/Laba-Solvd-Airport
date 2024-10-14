package labaSolvd.JacoLuna.Services;

import labaSolvd.JacoLuna.Enums.EntityOptions;
import labaSolvd.JacoLuna.Enums.MenuOptions;
import labaSolvd.JacoLuna.Utils;

import java.util.HashMap;
import java.util.Map;

public class AirportService {

    Map<Integer, Runnable> menuActions = new HashMap<>();
    PassengerService passengerSrv;
    MenuService menuSrv;
    public AirportService() {
        passengerSrv = new PassengerService();
        menuSrv = new MenuService();
    }
    public void startProgram(){
        int SourceType, ans, CRUD;
        do {
            SourceType = InputService.setInput(menuSrv.initializationMenu(), 2, Integer.class);
        }while (SourceType != MenuService.InitializationOptions.DATABASE.ordinal() &&
                SourceType != MenuService.InitializationOptions.XML.ordinal() &&
                SourceType != MenuService.InitializationOptions.EXIT.ordinal());

        do {
            ans = InputService.setInput(MenuOptions.printMenu(), MenuOptions.values().length, Integer.class);
            switch (MenuOptions.values()[ans]){
                case MenuOptions.PEOPLE -> {
                    ans = InputService.setInput(menuSrv.peopleMenu(), MenuOptions.PEOPLE.ordinal(), 2, Integer.class);
                    CRUD = InputService.setInput(EntityOptions.printMenu(), EntityOptions.values().length, Integer.class);
                    switch (EntityOptions.values()[CRUD]){
                        case SEE_ALL ->{
                            if (ans == MenuService.peopleOptions.PASSENGER.ordinal()) {
                                passengerSrv.getAll().forEach(p -> Utils.CONSOLE.info("{}\n", p.toString()));
                            }else if(ans == MenuService.peopleOptions.CREW_MEMBER.ordinal()){

                            }
                        }
                        case CREATE ->{
                            if (ans == MenuService.peopleOptions.PASSENGER.ordinal()) {
                                passengerSrv.add();
                            }else if(ans == MenuService.peopleOptions.CREW_MEMBER.ordinal()){

                            }
                        }
                        case DELETE ->{
                            if (ans == MenuService.peopleOptions.PASSENGER.ordinal()) {
                                passengerSrv.delete();
                            }else if(ans == MenuService.peopleOptions.CREW_MEMBER.ordinal()){

                            }
                        }
                        case UPDATE -> {
                            if (ans == MenuService.peopleOptions.PASSENGER.ordinal()) {
                                passengerSrv.update();
                            }else if(ans == MenuService.peopleOptions.CREW_MEMBER.ordinal()){

                            }
                        }
                    }

                }
                case CLASS -> {}
            }
        }while (ans != MenuOptions.END_SESSION.ordinal());
    }


}
