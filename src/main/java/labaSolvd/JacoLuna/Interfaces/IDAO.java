package labaSolvd.JacoLuna.Interfaces;

import java.util.List;
import java.util.Map;

public interface IDAO<T> {
    List<T> getList();
    T getById(int id);
    long add(T t);
    boolean update(T t);
    boolean delete(T t);
}
