package labaSolvd.JacoLuna.myBatysDAO;

import labaSolvd.JacoLuna.Classes.Passenger;
import labaSolvd.JacoLuna.Classes.Plane;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PlaneMapper {
    @Select("SELECT * FROM plane WHERE idPlane = #{idPlane}")
    Plane getPlane(String idPlane);

    @Insert("INSERT INTO plane (idPlane,fuelCapacity,tripulationSize,economySize,premiumSize,businessSize,firstClassSize,country)" +
            "VALUES (#{idPlane},#{fuelCapacity},#{tripulationSize},#{economySize},#{premiumSize},#{businessSize},#{firstClassSize},#{country})")
    int insertPlane(Plane plane);
    
    @Update("UPDATE plane SET fuelCapacity = #{fuelCapacity}, tripulationSize = #{tripulationSize}, economySize = #{economySize}, " +
                             "premiumSize = #{premiumSize}, businessSize = #{businessSize}, firstClassSize = #{firstClassSize}, " +
                             "country = #{country} " +
            "WHERE idPlane = #{idPlane}")
    int updatePlane(Plane plane);

    @Delete("DELETE FROM plane WHERE idPlane = #{idPlane}")
    int deletePlane(String idPlane);

    @Select("SELECT * FROM plane")
    List<Plane> getAllPlanes();

    @Select("SELECT * FROM plane as pas WHERE ${column} LIKE #{value}")
    List<Plane> searchByString(@Param("column") String column,@Param("value") String value);

    @Select("SELECT * FROM plane as pas WHERE ${column} = #{value}")
    <T>List<Plane> searchByOther(@Param("column") String column,@Param("value") T value);
}
