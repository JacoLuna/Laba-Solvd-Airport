package labaSolvd.JacoLuna.DAO;

import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.Classes.People;
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
import java.util.Map;

public class PeopleDAO implements IDAO<People> {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String ADD_PEOPLE_QUERY = "INSERT INTO People (`name`,`surname`,`email`,`age`) VALUES (?,?,?,?)";

    @Override
    public List<People> getList() {
        return List.of();
    }

    @Override
    public People getById(int id) {
        return null;
    }

    @Override
    public long add(People People) {
        long id = -1;
        try(ReusableConnection conn = POOL.getConnection();
            PreparedStatement ps = conn.prepareStatement(ADD_PEOPLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, People.getName());
            ps.setString(2, People.getSurname());
            ps.setString(3, People.getEmail());
            ps.setInt(4, People.getAge());

            if(ps.executeUpdate() == 0) throw new SQLException();

            try(ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException("Couldn't add People", e);
        }catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return id;
    }

    public long add(Map<String, Object> att) {
        long id = -1;
        try(ReusableConnection conn = POOL.getConnection();
            PreparedStatement ps = conn.prepareStatement(ADD_PEOPLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, (String) att.get("name"));
            ps.setString(2, (String) att.get("surname"));
            ps.setString(3, (String) att.get("email"));
            ps.setInt(4, (Integer) att.get("age"));

            if(ps.executeUpdate() == 0) throw new SQLException();

            try(ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException("Couldn't add People", e);
        }catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return id;
    }
    @Override
    public boolean update(People People) {
        return false;
    }

    @Override
    public boolean delete(People People) {
        return false;
    }
}
