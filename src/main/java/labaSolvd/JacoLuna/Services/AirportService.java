package labaSolvd.JacoLuna.Services;

import labaSolvd.JacoLuna.Classes.CrewMember;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Classes.Review;
import labaSolvd.JacoLuna.Enums.EntityOptions;
import labaSolvd.JacoLuna.Enums.MenuOptions;
import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Interfaces.IService;
import labaSolvd.JacoLuna.Services.entityServices.PassengerService;
import labaSolvd.JacoLuna.Services.entityServices.PlaneService;
import labaSolvd.JacoLuna.Services.entityServices.ReviewsService;
import labaSolvd.JacoLuna.Utils;

import java.util.HashMap;

public class AirportService {
    MenuService menuSrv;
    IService<Passenger> passengerSrv;
    IService<CrewMember> crewMemberSrv;
    IService<Plane> planeSrv;
    IService<Review> reviewsSrv;
    HashMap<MenuOptions, IService<?>> services = new HashMap<MenuOptions, IService<?>>();

    public AirportService() {
        menuSrv = new MenuService();
    }

    public void startProgram(SourceOptions source) {
        passengerSrv = new PassengerService(source);
        planeSrv = new PlaneService(source);
        reviewsSrv = new ReviewsService(source);

        services.put(MenuOptions.PASSENGER, passengerSrv);
        services.put(MenuOptions.PLANE, planeSrv);
        services.put(MenuOptions.REVIEW, reviewsSrv);

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
