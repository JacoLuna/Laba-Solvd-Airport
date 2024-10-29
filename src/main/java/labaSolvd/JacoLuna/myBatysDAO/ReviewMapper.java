package labaSolvd.JacoLuna.myBatysDAO;

import labaSolvd.JacoLuna.Classes.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ReviewMapper {
    @Select("SELECT * FROM review WHERE idReview = #{idReview}")
    Review getReview(long idReview);

    @Insert("INSERT INTO review (idReview,idFlight,idPassenger,rating,comment)" +
            "VALUES (#{idReview},#{idFlight},#{idPassenger},#{rating},#{comment})")
    int insertReview(Review review);
    
    @Update("UPDATE review SET idReview = #{idReview}, idFlight = #{idFlight}, idPassenger = #{idPassenger}, " +
                             "rating = #{rating}, comment = #{comment}" +
            "WHERE idReview = #{idReview}")
    int updateReview(Review review);

    @Delete("DELETE FROM review WHERE idReview = #{idReview}")
    int deleteReview(long idReview);

    @Select("SELECT * FROM review")
    List<Review> getAllReviews();

    @Select("SELECT * FROM review WHERE ${column} LIKE #{value}")
    List<Review> searchByString(@Param("column") String column,@Param("value") String value);

    @Select("SELECT * FROM review as pas WHERE ${column} = #{value}")
    <T>List<Review> searchByOther(@Param("column") String column,@Param("value") T value);
}
