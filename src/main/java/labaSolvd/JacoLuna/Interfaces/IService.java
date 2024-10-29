package labaSolvd.JacoLuna.Interfaces;

import java.util.List;

public interface IService<T> {

    void add();

    T getById();

    boolean delete();

    List<T> getAll();

    List<T> search();

    void update();
}
