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
        int ans, CRUD;
        int sourceIndex = InputService.setInput(menuSrv.initializationMenu(), MenuService.InitializationOptions.values().length-1, Integer.class);
        MenuService.InitializationOptions source = MenuService.InitializationOptions.values()[sourceIndex];
        if (source == MenuService.InitializationOptions.DATABASE){
            do {
                ans = InputService.setInput(MenuOptions.printMenu(), MenuOptions.values().length, Integer.class);
                switch (MenuOptions.values()[ans]){
                    case MenuOptions.PEOPLE -> {
                        ans = InputService.setInput(menuSrv.peopleMenu(), MenuOptions.PEOPLE.ordinal(), 2, Integer.class);
                        do {
                            CRUD = InputService.setInput(EntityOptions.printMenu(), EntityOptions.values().length, Integer.class);
                            if (ans == MenuService.peopleOptions.PASSENGER.ordinal()) {
                                switch (EntityOptions.values()[CRUD]) {
                                    case SEE_ALL -> passengerSrv.getAll().forEach(p -> Utils.CONSOLE.info("{}\n", p.toString()));
                                    case CREATE -> passengerSrv.add();
                                    case DELETE -> passengerSrv.delete();
                                    case UPDATE -> passengerSrv.update();
                                    case SEARCH -> passengerSrv.search().forEach(p -> Utils.CONSOLE.info("{}\n", p.toString()));
                                    case GET_ONE -> Utils.CONSOLE.info(passengerSrv.getById().toString());
                                }
                            }else if (ans == MenuService.peopleOptions.CREW_MEMBER.ordinal()) {

                            }
                        }while (EntityOptions.values()[CRUD] != EntityOptions.EXIT);
                    }
                    case CLASS -> {}
                }
            }while (ans != MenuOptions.END_SESSION.ordinal());
        }
    }


}
