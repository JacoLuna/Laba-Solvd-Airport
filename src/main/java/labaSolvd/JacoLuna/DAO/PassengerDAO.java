package labaSolvd.JacoLuna.DAO;

import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Connection.ConnectionPool;
import labaSolvd.JacoLuna.Connection.ReusableConnection;
import labaSolvd.JacoLuna.Interfaces.IDAO;
import labaSolvd.JacoLuna.Utils;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassengerDAO implements IDAO<Passenger> {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String ADD_PASSENGER_QUERY = "INSERT INTO passenger (`VIP`, `flightPoints`, `hasSpecialNeeds`, `idPeople`) VALUES (?, ?, ?, ?)";
    private static final String GET_PASSENGER_QUERY = "SELECT * FROM passenger WHERE idPassenger = ?";
    private static final String GET_ALL_PASSENGERS_QUERY = "SELECT * FROM passenger";
    private static final String GET_PEOPLE_QUERY = "SELECT * FROM people WHERE idPeople = ?";
    private static final String DEL_PASSENGER_QUERY = "DELETE FROM passenger WHERE idPassenger = ?";
    private static final String UPDATE_PASSENGER_QUERY = "UPDATE passenger SET VIP = ?, flightPoints = ?, hasSpecialNeeds = ? WHERE idPassenger = ?";

    @Override
    public List<Passenger> getList() {
        List<Passenger> passengers = new ArrayList<>();
        try(ReusableConnection conn = POOL.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_ALL_PASSENGERS_QUERY, Statement.RETURN_GENERATED_KEYS)){

            try(ResultSet generatedKeys = ps.executeQuery()) {
                while (generatedKeys.next()){
                    Passenger passenger = null;
                    long idPassenger = generatedKeys.getLong(1);
                    Boolean VIP = generatedKeys.getBoolean(2);
                    int flightPoints = generatedKeys.getInt(3);
                    Boolean hasSpecialNeeds = generatedKeys.getBoolean(4);
                    long idPerson = generatedKeys.getLong(5);

                    try (PreparedStatement subPs = conn.prepareStatement(GET_PEOPLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                        subPs.setLong(1, idPerson);
                        try(ResultSet subGeneratedKeys = subPs.executeQuery()) {
                            if (subGeneratedKeys.next()){
                                String name = subGeneratedKeys.getString(2);
                                String surname = subGeneratedKeys.getString(3);
                                String email = subGeneratedKeys.getString(4);
                                int age = subGeneratedKeys.getInt(5);
                                passenger = new Passenger(idPerson, name, surname, email, age, idPassenger, VIP, flightPoints, hasSpecialNeeds);
                            }
                        }
                    passengers.add(passenger);
                    }catch (SQLException e) {
                        throw new RuntimeException("Couldn't get Person", e);
                    }
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException("Couldn't find Passengers", e);
        }catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return passengers;
    }

    @Override
    public Passenger getById(long id) {
        Passenger passenger = null;
        try(ReusableConnection conn = POOL.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_PASSENGER_QUERY, Statement.RETURN_GENERATED_KEYS)){
            ps.setLong(1, id);

            try(ResultSet generatedKeys = ps.executeQuery()) {
                if (generatedKeys.next()) {

                    id = generatedKeys.getLong(1);
                    Boolean VIP = generatedKeys.getBoolean(2);
                    int flightPoints = generatedKeys.getInt(3);
                    Boolean hasSpecialNeeds = generatedKeys.getBoolean(4);
                    long idPerson = generatedKeys.getLong(5);

                    try (PreparedStatement subPs = conn.prepareStatement(GET_PEOPLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                        subPs.setLong(1, idPerson);
                        try(ResultSet subGeneratedKeys = subPs.executeQuery()) {
                            if (subGeneratedKeys.next()){
                                String name = subGeneratedKeys.getString(2);
                                String surname = subGeneratedKeys.getString(3);
                                String email = subGeneratedKeys.getString(4);
                                int age = subGeneratedKeys.getInt(5);
                                passenger = new Passenger(idPerson, name, surname, email, age, id, VIP, flightPoints, hasSpecialNeeds);
                            }
                        }

                    }catch (SQLException e) {
                        throw new RuntimeException("Couldn't get Person", e);
                    }
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException("Couldn't get Passenger", e);
        }catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return passenger;
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
        boolean result = false;
        PeopleDAO peopleDAO = new PeopleDAO();
        try(ReusableConnection conn = POOL.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_PASSENGER_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            Map<String, Object> att = new HashMap<>();
            att.put("name", passenger.getName());
            att.put("surname", passenger.getSurname());
            att.put("email", passenger.getEmail());
            att.put("age", passenger.getAge());
            att.put("idPeople", passenger.getIdPeople());

            ps.setBoolean(1, passenger.isVIP());
            ps.setInt(2, passenger.getFlightPoints());
            ps.setBoolean(3, passenger.hasSpecialNeeds());
            ps.setLong(4, passenger.getIdPassenger());

            if(ps.executeUpdate() == 0) throw new SQLException();
            peopleDAO.update(att);
        }catch (SQLException e) {
            throw new RuntimeException("Couldn't update Passenger", e);
        }catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return result;
    }

    @Override
    public boolean delete(long id) {
        boolean result = false;
        Passenger passenger = getById(id);
        long idPeople = passenger.getIdPeople();
        PeopleDAO peopleDAO = new PeopleDAO();
        try(ReusableConnection conn = POOL.getConnection();
            PreparedStatement ps = conn.prepareStatement(DEL_PASSENGER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, id);
            if(ps.executeUpdate() == 0) throw new SQLException();
            peopleDAO.delete(idPeople);
        }catch (SQLException e) {
            throw new RuntimeException("Couldn't delete Passenger", e);
        }catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return result;
    }
    public List<Passenger> search(String column, String value) {
        String GET_CONDITIONS_STRING_PASSENGER_QUERY = "SELECT * FROM passenger as pas " +
                "INNER JOIN people as peo ON pas.idPeople = peo.idPeople WHERE " + column + " LIKE ?";
        return executeSearch("%" + value + "%", String.class, GET_CONDITIONS_STRING_PASSENGER_QUERY);
    }
    public <T> List<Passenger> search(String column, T value) {
        String GET_CONDITIONS_NUMBER_PASSENGER_QUERY = "SELECT * FROM passenger as pas " +
                "INNER JOIN people as peo ON pas.idPeople = peo.idPeople WHERE " + column + " = ?";
        return executeSearch(value, (Class)value.getClass(), GET_CONDITIONS_NUMBER_PASSENGER_QUERY);
    }
    private <T> List<Passenger> executeSearch(T value, Class<T> type, String query) {
        List<Passenger> passengers = new ArrayList<>();
        try(ReusableConnection conn = POOL.getConnection();
            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            switch (type.getSimpleName()){
                case "Integer" -> ps.setInt(1, Integer.parseInt(value.toString()));
                case "Float" -> ps.setFloat(1, Float.parseFloat(value.toString()));
                case "Long" -> ps.setLong(1, Long.parseLong(value.toString()));
                case "Double" -> ps.setDouble(1, Double.parseDouble(value.toString()));
                case "Boolean" -> ps.setBoolean(1, Boolean.parseBoolean(value.toString()));
                case "String" -> ps.setString(1, value.toString());
            }
            try(ResultSet generatedKeys = ps.executeQuery()) {
                while (generatedKeys.next()) {
                    long idPerson = generatedKeys.getLong(5);
                    passengers.add(getById(idPerson));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't find Passengers", e);
        } catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return passengers;
    }
}
