package labaSolvd.JacoLuna.Services;

import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.DAO.PassengerDAO;

public class PassengerService {

    private final PassengerDAO passengerDAO;
    private PeopleService peopleService;

    public PassengerService(PassengerDAO passengerDAO, PeopleService peopleService) {
        this.passengerDAO = passengerDAO;
        this.peopleService = peopleService;
    }

    public void add(String name, String surname, String email, int age, Boolean VIP, int flightPoints, Boolean hasSpecialNeeds){
        long idPeople = peopleService.add(name,surname,email,age);
        passengerDAO.add(new Passenger(idPeople, name, surname, email, age, VIP, flightPoints, hasSpecialNeeds));
    }
}
