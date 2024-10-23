package labaSolvd.JacoLuna.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Enums.EntityOptions;
import labaSolvd.JacoLuna.Enums.MenuOptions;
import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Parsers.JSON.JsonParser;
import labaSolvd.JacoLuna.Services.entityServices.PassengerService;
import labaSolvd.JacoLuna.Services.entityServices.PlaneService;
import labaSolvd.JacoLuna.Services.entityServices.ReviewsService;
import labaSolvd.JacoLuna.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AirportService {
    PassengerService passengerSrv;
    PlaneService planeSrv;
    ReviewsService reviewsSrv;
    MenuService menuSrv;
    public AirportService() {
        menuSrv = new MenuService();
    }

    public void startProgram(SourceOptions source) {
        int ans, CRUD;
        passengerSrv = new PassengerService(source);
        planeSrv = new PlaneService(source);
        reviewsSrv = new ReviewsService(source);

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
                                        passengerSrv.getAll().forEach(p -> Utils.CONSOLE.info("{}", p.toString()));
                                case CREATE -> passengerSrv.add();
                                case DELETE -> passengerSrv.delete();
                                case UPDATE -> passengerSrv.update();
                                case SEARCH ->
                                        passengerSrv.search().forEach(p -> Utils.CONSOLE.info("{}", p.toString()));
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
                            case SEE_ALL -> planeSrv.getAll().forEach(p -> Utils.CONSOLE.info("{}", p.toString()));
                            case CREATE -> planeSrv.add();
                            case DELETE -> planeSrv.delete();
                            case UPDATE -> planeSrv.update();
                            case SEARCH ->
                                    planeSrv.search().forEach(p -> Utils.CONSOLE.info("{}", p.toString()));
                            case GET_ONE -> Utils.CONSOLE.info(planeSrv.getById().toString());

                        }
                    } while (EntityOptions.values()[CRUD] != EntityOptions.EXIT);
                }
                case REVIEW ->{
                    do {
                        CRUD = InputService.setInput(menuSrv.EntityMenu(), EntityOptions.values().length, Integer.class);
                        switch (EntityOptions.values()[CRUD]) {
                            case CREATE -> reviewsSrv.add();
                            case SEE_ALL -> reviewsSrv.getAll().forEach(p -> Utils.CONSOLE.info("{}", p.toString()));
                            case GET_ONE -> Utils.CONSOLE.info(planeSrv.getById().toString());
                        }
                    } while (EntityOptions.values()[CRUD] != EntityOptions.EXIT);
                }
            }
        } while (ans != MenuOptions.END_SESSION.ordinal());

    }


}
