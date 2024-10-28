package labaSolvd.JacoLuna.myBatysDAO;

import labaSolvd.JacoLuna.Classes.CrewMember;
import labaSolvd.JacoLuna.Classes.Plane;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CrewMemberMapper {
    @Select("SELECT * FROM crewMember as cm INNER JOIN people as p ON cm.idPeople = p.idPeople " +
            "WHERE idCrewMember = #{idCrewMember}")
    CrewMember getCrewMember(long idCrewMember);

    @Insert("INSERT INTO crewMember (idCrewMember,role,flightHours,idPeople)" +
            "VALUES (#{idCrewMember},#{role},#{flightHours},#{idPeople})")
    int insertCrewMember(CrewMember crewMember);
    
    @Update("UPDATE CrewMember SET role = #{role}, flightHours = #{flightHours} " +
            "WHERE idCrewMember = #{idCrewMember}")
    int updateCrewMember(CrewMember crewMember);

    @Delete("DELETE FROM crewMember WHERE idCrewMember = #{idCrewMember}")
    int deleteCrewMember(long idCrewMember);

    @Select("SELECT * FROM crewMember as cm INNER JOIN people as p ON cm.idPeople = p.idPeople ")
    List<CrewMember> getAllCrewMembers();

    @Select("SELECT * FROM crewMember as cm INNER JOIN people as p ON cm.idPeople = p.idPeople " +
            "WHERE ${column} LIKE #{value}")
    List<CrewMember> searchByString(@Param("column") String column,@Param("value") String value);

    @Select("SELECT * FROM crewMember as cm INNER JOIN people as p ON cm.idPeople = p.idPeople " +
            "WHERE ${column} = #{value}")
    <T extends Number>List<CrewMember> searchByNumber(@Param("column") String column,@Param("value") T value);

    @Select("SELECT * FROM crewMember as cm INNER JOIN people as p ON cm.idPeople = p.idPeople " +
            "WHERE ${column} = #{value}")
    List<Plane> searchByBoolean(@Param("column") String column, boolean value);
}
