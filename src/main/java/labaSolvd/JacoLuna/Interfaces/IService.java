package labaSolvd.JacoLuna.Interfaces;

import labaSolvd.JacoLuna.Enums.SourceOptions;
import labaSolvd.JacoLuna.Services.MenuService;

import java.lang.constant.Constable;
import java.util.List;

public interface IService<T> {
    
    void add();

    T getById();

    boolean delete();

    List<T> getAll();

    List<T> search();

    void update();
}
