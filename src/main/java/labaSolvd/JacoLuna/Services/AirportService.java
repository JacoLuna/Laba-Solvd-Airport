package labaSolvd.JacoLuna.Services;

import labaSolvd.JacoLuna.Enums.EntityOptions;
import labaSolvd.JacoLuna.Enums.MenuOptions;
import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Utils;

import java.util.HashMap;
import java.util.Map;

public class AirportService {

    Map<Integer, Runnable> menuActions = new HashMap<>();
    PassengerService passengerSrv;
    PlaneService planeSrv;
    MenuService menuSrv;

    public AirportService() {
        menuSrv = new MenuService();
    }

    public void startProgram() {
        int ans, CRUD;
        int sourceIndex = InputService.setInput(menuSrv.sourceMenu(), SourceOptions.values().length - 1, Integer.class);
        SourceOptions source = SourceOptions.values()[sourceIndex];

        passengerSrv = new PassengerService();
        planeSrv = new PlaneService(SourceOptions.XML);

        do {
            ans = InputService.setInput(MenuOptions.printMenu(), MenuOptions.values().length, Integer.class);
            switch (MenuOptions.values()[ans]) {
                case MenuOptions.PEOPLE -> {
                    ans = InputService.setInput(menuSrv.peopleMenu(), MenuOptions.PEOPLE.ordinal(), 2, Integer.class);
                    do {
                        CRUD = InputService.setInput(menuSrv.EntityMenu(), EntityOptions.values().length, Integer.class);
                        if (ans == MenuService.peopleOptions.PASSENGER.ordinal()) {
                            switch (EntityOptions.values()[CRUD]) {
                                case SEE_ALL ->
                                        passengerSrv.getAll().forEach(p -> Utils.CONSOLE.info("{}\n", p.toString()));
                                case CREATE -> passengerSrv.add();
                                case DELETE -> passengerSrv.delete();
                                case UPDATE -> passengerSrv.update();
                                case SEARCH ->
                                        passengerSrv.search().forEach(p -> Utils.CONSOLE.info("{}\n", p.toString()));
                                case GET_ONE -> Utils.CONSOLE.info(passengerSrv.getById().toString());
                            }
                        } else if (ans == MenuService.peopleOptions.CREW_MEMBER.ordinal()) {

                        }
                    } while (EntityOptions.values()[CRUD] != EntityOptions.EXIT);
                }
                case CLASS -> {
                }
                case PLANE -> {
                    do {
                        CRUD = InputService.setInput(menuSrv.EntityMenu(), EntityOptions.values().length, Integer.class);
                        switch (EntityOptions.values()[CRUD]) {
                            case SEE_ALL -> planeSrv.getAll().forEach(p -> Utils.CONSOLE.info("{}\n", p.toString()));
                        }
                    } while (EntityOptions.values()[CRUD] != EntityOptions.EXIT);
                }
            }
        } while (ans != MenuOptions.END_SESSION.ordinal());

    }


}
