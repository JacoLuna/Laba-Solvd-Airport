package labaSolvd.JacoLuna.Interfaces;

import java.util.List;
import java.util.Map;

public interface IDAO<T> {
    List<T> getList();
    T getById(long id);
    long add(T t);
    boolean update(T t);
    boolean delete(long id);
}
