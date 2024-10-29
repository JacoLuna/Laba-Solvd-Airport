package labaSolvd.JacoLuna.DAO;

import labaSolvd.JacoLuna.Classes.CrewMember;
import labaSolvd.JacoLuna.Connection.SessionFactoryBuilder;
import labaSolvd.JacoLuna.Interfaces.IDAO;
import labaSolvd.JacoLuna.Utils;
import labaSolvd.JacoLuna.myBatysDAO.CrewMemberMapper;
import labaSolvd.JacoLuna.myBatysDAO.ReviewMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class CrewMemberDAO implements IDAO<CrewMember> {

    private final PeopleDAO peopleDAO;
    public CrewMemberDAO(){
        this.peopleDAO = new PeopleDAO();
    }

    @Override
    public List<CrewMember> getList() {
        List<CrewMember> list = null;
        try (SqlSession session = SessionFactoryBuilder.getSqlSessionFactory().openSession()) {
            list = session.getMapper(CrewMemberMapper.class).getAllCrewMembers();
        } catch (Exception e) {
            Utils.CONSOLE_ERROR.error(e);
        }
        return list;
    }

    @Override
    public CrewMember getById(long id) {
        return EntityDAO.executeQuery(CrewMemberMapper.class, "getCrewMember", id);
    }

    @Override
    public long add(CrewMember crewMember) {
        return 0;
    }

    @Override
    public boolean update(CrewMember crewMember) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
