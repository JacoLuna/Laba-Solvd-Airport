package labaSolvd.JacoLuna.DAO;

import labaSolvd.JacoLuna.Connection.SessionFactoryBuilder;
import labaSolvd.JacoLuna.Utils;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Method;

public class EntityDAO {

    public static <T, K> K executeQuery(Class<T> mapperClass, String methodName, Object... params) {
        try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
            T mapper = session.getMapper(mapperClass);
            Method method = findMethod(mapperClass, methodName, params);
            K result = (K) method.invoke(mapper, params);
            session.commit();
            return result;
        } catch (Exception e) {
            Utils.CONSOLE_ERROR.error("Error executing query: {}", e);
            return null;
        }
    }

    private static <T> Method findMethod(Class<T> mapperClass, String methodName, Object[] params) throws NoSuchMethodException {
        for (Method method : mapperClass.getMethods()) {
            if (method.getName().equals(methodName) && method.getParameterCount() == params.length) {
                return method;
            }
        }
        throw new NoSuchMethodException("Method " + methodName + " not found in " + mapperClass.getSimpleName());
    }

}
