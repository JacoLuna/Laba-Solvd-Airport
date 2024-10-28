package labaSolvd.JacoLuna.Services;

import labaSolvd.JacoLuna.Classes.CrewMember;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Classes.Review;
import labaSolvd.JacoLuna.Enums.EntityOptions;
import labaSolvd.JacoLuna.Enums.MenuOptions;
import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Interfaces.IService;
import labaSolvd.JacoLuna.Services.entityServices.CrewMemberService;
import labaSolvd.JacoLuna.Services.entityServices.PassengerService;
import labaSolvd.JacoLuna.Services.entityServices.PlaneService;
import labaSolvd.JacoLuna.Services.entityServices.ReviewsService;
import labaSolvd.JacoLuna.Utils;

import java.util.HashMap;

public class AirportService {
    MenuService menuSrv;
    HashMap<MenuOptions, IService<?>> services = new HashMap<>();
    public AirportService() {
        menuSrv = new MenuService();
    }

    public void startProgram(SourceOptions source) {
        services.put(MenuOptions.PASSENGER, new PassengerService(source));
        services.put(MenuOptions.CREW_MEMBER, new CrewMemberService(source));
        services.put(MenuOptions.PLANE, new PlaneService(source));
        services.put(MenuOptions.REVIEW, new ReviewsService(source));

        int ans;
        do {
            ans = InputService.setInput(MenuOptions.printMenu(), MenuOptions.values().length, Integer.class);
            int menuIndex = ans;
            services.forEach((menuOptions, service) -> {
                if (menuOptions == MenuOptions.values()[menuIndex]) {
                    int CRUD;
                    do {
                        CRUD = InputService.setInput(menuSrv.EntityMenu(), EntityOptions.values().length, Integer.class);
                        switch (EntityOptions.values()[CRUD]) {
                            case SEE_ALL -> service.getAll().forEach(p -> Utils.CONSOLE.info("{}", p.toString()));
                            case CREATE -> service.add();
                            case DELETE -> service.delete();
                            case UPDATE -> service.update();
                            case SEARCH -> service.search().forEach(p -> Utils.CONSOLE.info("{}", p.toString()));
                            case GET_ONE -> Utils.CONSOLE.info(service.getById().toString());
                        }
                    } while (EntityOptions.values()[CRUD] != EntityOptions.EXIT);
                }
            });
        } while (ans != MenuOptions.END_SESSION.ordinal());
    }

}
