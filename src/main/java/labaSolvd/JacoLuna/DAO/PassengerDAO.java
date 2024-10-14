package labaSolvd.JacoLuna.DAO;

import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Connection.ConnectionPool;
import labaSolvd.JacoLuna.Connection.ReusableConnection;
import labaSolvd.JacoLuna.Interfaces.IDAO;
import labaSolvd.JacoLuna.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PassengerDAO implements IDAO<Passenger> {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String ADD_PASSENGER_QUERY = "INSERT INTO passenger (`VIP`, `flightPoints`, `hasSpecialNeeds`, `idPeople`) VALUES (?, ?, ?, ?)";

    @Override
    public List<Passenger> getList() {
        return List.of();
    }

    @Override
    public Passenger getById(int id) {
        return null;
    }

    @Override
    public long add(Passenger passenger) {
        long id = -1;
        try(ReusableConnection conn = POOL.getConnection();
            PreparedStatement ps = conn.prepareStatement(ADD_PASSENGER_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            ps.setBoolean(1, passenger.isVIP());
            ps.setInt(2, passenger.getFlightPoints());
            ps.setBoolean(3, passenger.hasSpecialNeeds());
            ps.setLong(4, passenger.getIdPeople());

            if(ps.executeUpdate() == 0) throw new SQLException();

            try(ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                    passenger.setIdPassenger(id);
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException("Couldn't add Passenger", e);
        }catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return id;
    }

    @Override
    public boolean update(Passenger passenger) {
        return false;
    }

    @Override
    public boolean delete(Passenger passenger) {
        return false;
    }
}
