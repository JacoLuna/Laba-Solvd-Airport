package labaSolvd.JacoLuna.myBatysDAO;

import labaSolvd.JacoLuna.Classes.CrewMember;
import labaSolvd.JacoLuna.Classes.Plane;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CrewMemberMapper {
    @Select("SELECT * FROM crewMember as cm INNER JOIN people as p ON cm.idPeople = p.idPeople " +
            "WHERE idCrewMember = #{idCrewMember}")
    CrewMember getCrewMember(long idCrewMember);

    @Insert("INSERT INTO crewMember (role,flightHours,idPeople) " +
            "VALUES (#{role},#{flightHours},#{idPeople})")
//    @Options(useGeneratedKeys = true, keyColumn = "idCrewMember", keyProperty = "idCrewMember")
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
    <T>List<CrewMember> searchByOther(@Param("column") String column,@Param("value") T value);
}
