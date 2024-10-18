package labaSolvd.JacoLuna.Services;

import jakarta.xml.bind.JAXBException;
import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Classes.xmlLists.Planes;
import labaSolvd.JacoLuna.DAO.PassengerDAO;
import labaSolvd.JacoLuna.DAO.PlaneDAO;
import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Interfaces.IService;
import labaSolvd.JacoLuna.Utils;

import java.util.List;

public class PlaneService implements IService<Plane> {
    private final PlaneDAO planeDAO;
    private final SourceOptions source;

    public PlaneService(SourceOptions sourceOptions) {
        source = sourceOptions;
        this.planeDAO = new PlaneDAO();
    }

    @Override
    public void add() {
    }

    @Override
    public Plane getById() {
        return null;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public List<Plane> getAll() {
        if (source == SourceOptions.DATA_BASE)
            return planeDAO.getList();
        try {
            Utils.CONSOLE.info("A");
            return MarshallListPlane.UnMarshallListPlane();
        } catch (JAXBException e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return null;
    }

    @Override
    public List<Plane> search() {
        return List.of();
    }

    @Override
    public void update() {

    }
}
