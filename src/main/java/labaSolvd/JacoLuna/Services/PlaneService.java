package labaSolvd.JacoLuna.Services;

import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.DAO.PassengerDAO;
import labaSolvd.JacoLuna.DAO.PlaneDAO;

import java.util.List;

public class PlaneService {
    private final PlaneDAO planeDAO;

    public PlaneService() {
        this.planeDAO = new PlaneDAO();
    }
    public List<Plane> getAll(){
        return planeDAO.getList();
    }
}
