package labaSolvd.JacoLuna.myBatysDAO;

import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.Classes.Plane;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PeopleMapper {
    @Select("SELECT * FROM People WHERE idPeople = #{idPeople}")
    People getPeople(long idPeople);

    @Insert("INSERT INTO People (name,surname,email,age)" +
            "VALUES (#{name},#{surname},#{email},#{age})")
    @Options(useGeneratedKeys = true, keyColumn = "idPeople", keyProperty = "idPeople")
    int insertPeople(People people);
    
    @Update("UPDATE People SET name = #{name}, surname = #{surname}, email = #{email}, age = #{age} " +
            "WHERE idPeople = #{idPeople}")
    int updatePeople(People People);

    @Delete("DELETE FROM People WHERE idPeople = #{idPeople}")
    int deletePeople(long idPeople);

    @Select("SELECT * FROM People")
    List<People> getAllPeoples();

    @Select("SELECT * FROM People WHERE ${column} LIKE #{value}")
    List<People> searchByString(@Param("column") String column,@Param("value") String value);

    @Select("SELECT * FROM People WHERE ${column} = #{value}")
    <T extends Number>List<People> searchByNumber(@Param("column") String column,@Param("value") T value);

    @Select("SELECT * FROM People WHERE ${column} = #{value}")
    List<Plane> searchByBoolean(@Param("column") String column, boolean value);
}
