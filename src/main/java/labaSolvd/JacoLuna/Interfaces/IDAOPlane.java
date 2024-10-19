package labaSolvd.JacoLuna.Interfaces;

import labaSolvd.JacoLuna.Classes.Plane;

import java.util.List;

public interface IDAOPlane {
    List<Plane> getList();
    Plane getById(String id);
    long add(Plane plane);
    boolean update(Plane plane);
    boolean delete(String id);
}
