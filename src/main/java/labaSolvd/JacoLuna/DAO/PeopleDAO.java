package labaSolvd.JacoLuna.DAO;

import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.Connection.ConnectionPool;
import labaSolvd.JacoLuna.Connection.ReusableConnection;
import labaSolvd.JacoLuna.Connection.SessionFactoryBuilder;
import labaSolvd.JacoLuna.Interfaces.IDAO;
import labaSolvd.JacoLuna.Utils;
import labaSolvd.JacoLuna.myBatysDAO.CrewMemberMapper;
import labaSolvd.JacoLuna.myBatysDAO.PeopleMapper;
import org.apache.ibatis.session.SqlSession;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class PeopleDAO implements IDAO<People> {

    private static final ConnectionPool POOL = ConnectionPool.getInstance();
    private static final String ADD_PEOPLE_QUERY = "INSERT INTO People (`name`,`surname`,`email`,`age`) VALUES (?,?,?,?)";
    private static final String GET_PEOPLE_QUERY = "SELECT * FROM people WHERE idPeople = ?";
    private static final String DEL_PEOPLE_QUERY = "DELETE FROM people WHERE idPeople = ?";
    private static final String UPDATE_PEOPLE_QUERY = "UPDATE people SET name = ?, surname = ?, email = ?, age = ? WHERE idPeople = ?";

    @Override
    public List<People> getList() {
        return List.of();
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
        }catch (Exception e ) {
            throw new RuntimeException("Couldn't add People", e);
        }
        return id;
    }

    @Override
    public People getById(long id) {
        return null;
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
    public boolean update(People people) {
        boolean result = false;
        try(ReusableConnection conn = POOL.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_PEOPLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, people.getName());
            if(ps.executeUpdate() == 0) throw new SQLException();
        }catch (SQLException e) {
            throw new RuntimeException("Couldn't delete Passenger", e);
        }catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return result;
    }
    public boolean update(Map<String, Object> att) {
        boolean result = false;
        try(ReusableConnection conn = POOL.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_PEOPLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, (String) att.get("name"));
            ps.setString(2, (String) att.get("surname"));
            ps.setString(3, (String) att.get("email"));
            ps.setInt(4, (Integer) att.get("age"));
            ps.setLong(5, (Long) att.get("idPeople"));
            if(ps.executeUpdate() == 0) throw new SQLException();
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
        try(ReusableConnection conn = POOL.getConnection();
            PreparedStatement ps = conn.prepareStatement(DEL_PEOPLE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, id);
            if(ps.executeUpdate() == 0) throw new SQLException();
        }catch (SQLException e) {
            throw new RuntimeException("Couldn't delete Passenger", e);
        }catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return result;
    }

    //Mybatis implementation

    public List<People> getListMybatis() {
        return List.of();
    }
    public long addMybatis(People people) {
        long id = -1;
        try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
            if (session.getMapper(PeopleMapper.class).insertPeople(people) > 0) {
                session.commit();
                id = people.getIdPeople();
            }
        } catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return id;
    }
    public People getByIdMybatis(long id) {
        return null;
    }

    public long addMybatis(Map<String, Object> att) {
        long id = -1;
        return id;
    }
    public boolean updateMybatis(People people) {
        boolean result = false;
        return result;
    }
    public boolean updateMybatis(Map<String, Object> att) {
        boolean result = false;
        return result;
    }
    public boolean deleteMybatis(long id) {
        boolean result = false;
        return result;
    }
}
