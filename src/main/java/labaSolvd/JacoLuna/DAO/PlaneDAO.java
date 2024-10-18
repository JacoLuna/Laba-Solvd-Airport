package labaSolvd.JacoLuna.DAO;

import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Connection.ConnectionPool;
import labaSolvd.JacoLuna.Connection.ReusableConnection;
import labaSolvd.JacoLuna.Interfaces.IDAO;
import labaSolvd.JacoLuna.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlaneDAO implements IDAO<Plane> {
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String GET_ALL_PLANES_QUERY = "SELECT * FROM plane";

    @Override
    public List<Plane> getList() {
        List<Plane> Planes = new ArrayList<>();
        try (ReusableConnection conn = POOL.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ALL_PLANES_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            try (ResultSet generatedKeys = ps.executeQuery()) {
                while (generatedKeys.next()) {
                    Plane Plane = null;
                    String idPlane = generatedKeys.getString(1);
                    int fuelCapacity = generatedKeys.getInt(2);
                    int tripulationSize = generatedKeys.getInt(3);
                    int economySize = generatedKeys.getInt(4);
                    int premiumSize = generatedKeys.getInt(5);
                    int businessSize = generatedKeys.getInt(6);
                    int firstClassSize = generatedKeys.getInt(7);
                    String country = generatedKeys.getString(8);

                    Plane = new Plane(idPlane, fuelCapacity, tripulationSize, economySize, premiumSize, businessSize, firstClassSize, country);
                    Planes.add(Plane);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't find Planes", e);
        } catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return Planes;
    }

    @Override
    public Plane getById(long id) {
        return null;
    }

    @Override
    public long add(Plane o) {
        return 0;
    }

    @Override
    public boolean update(Plane o) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
