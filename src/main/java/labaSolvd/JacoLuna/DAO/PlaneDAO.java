package labaSolvd.JacoLuna.DAO;

import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Classes.Plane;
import labaSolvd.JacoLuna.Connection.ConnectionPool;
import labaSolvd.JacoLuna.Connection.ReusableConnection;
import labaSolvd.JacoLuna.Interfaces.IDAO;
import labaSolvd.JacoLuna.Interfaces.IDAOPlane;
import labaSolvd.JacoLuna.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaneDAO implements IDAOPlane {
    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String ADD_PLANE_QUERY =
            "INSERT INTO plane (`idPlane`,`fuelCapacity`, `tripulationSize`, `economySize`, `premiumSize`, `buinessSize`, `firstClassSize`, `country`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_PLANE_QUERY = "SELECT * FROM plane WHERE idPlane = ?";
    private static final String GET_ALL_PLANES_QUERY = "SELECT * FROM plane";
    private static final String UPDATE_PLANE_QUERY =
            "UPDATE plane SET fuelCapacity = ?, tripulationSize = ?, economySize = ?, premiumSize = ?, buinessSize = ?, firstClassSize = ?, country = ? WHERE idPlane = ?";
    private static final String DEL_PLANE_QUERY = "DELETE FROM plane WHERE idPlane = ?";

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
    public Plane getById(String id) {
        Plane plane = null;
        try (ReusableConnection conn = POOL.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_PLANE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, id);
            try (ResultSet generatedKeys = ps.executeQuery()) {
                id = generatedKeys.getString(1);
                int fuelCapacity = generatedKeys.getInt(2),
                        tripulationSize = generatedKeys.getInt(3),
                        economySize = generatedKeys.getInt(4),
                        premiumSize = generatedKeys.getInt(5),
                        businessSize = generatedKeys.getInt(6),
                        firstClassSize = generatedKeys.getInt(7);
                String country = generatedKeys.getString(8);
                plane = new Plane(id, fuelCapacity, tripulationSize, economySize, premiumSize, businessSize, firstClassSize, country);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't get Plane", e);
        } catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return plane;
    }

    @Override
    public long add(Plane plane) {
        long response = -1;
        try (ReusableConnection conn = POOL.getConnection();
             PreparedStatement ps = conn.prepareStatement(ADD_PLANE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, plane.getIdPlane());
            ps.setInt(2, plane.getFuelCapacity());
            ps.setInt(3, plane.getTripulationSize());
            ps.setInt(4, plane.getEconomySize());
            ps.setInt(5, plane.getPremiumSize());
            ps.setInt(6, plane.getBusinessSize());
            ps.setInt(7, plane.getFirstClassSize());
            ps.setString(8, plane.getCountry());

            if (ps.executeUpdate() == 0) throw new SQLException();
            response = 1;
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't add Plane", e);
        } catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return response;
    }

    @Override
    public boolean update(Plane plane) {
        boolean result = false;
        try(ReusableConnection conn = POOL.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_PLANE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,plane.getFuelCapacity());
            ps.setInt(2,plane.getTripulationSize());
            ps.setInt(3,plane.getEconomySize());
            ps.setInt(4,plane.getPremiumSize());
            ps.setInt(5,plane.getBusinessSize());
            ps.setInt(6,plane.getFirstClassSize());
            ps.setString(7,plane.getCountry());
            if(ps.executeUpdate() == 0) throw new SQLException();
        }catch (SQLException e) {
            throw new RuntimeException("Couldn't update Plane", e);
        }catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return result;
    }

    @Override
    public boolean delete(String id) {
        boolean result = false;
        Plane plane = getById(id);
        String idPlane = plane.getIdPlane();
        try(ReusableConnection conn = POOL.getConnection();
            PreparedStatement ps = conn.prepareStatement(DEL_PLANE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, idPlane);
            if(ps.executeUpdate() == 0) throw new SQLException();
        }catch (SQLException e) {
            throw new RuntimeException("Couldn't delete Passenger", e);
        }catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return result;
    }
}
