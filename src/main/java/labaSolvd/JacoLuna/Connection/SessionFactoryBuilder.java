package labaSolvd.JacoLuna.Connection;

import labaSolvd.JacoLuna.Classes.People;
import labaSolvd.JacoLuna.myBatysDAO.CrewMemberMapper;
import labaSolvd.JacoLuna.myBatysDAO.PeopleMapper;
import labaSolvd.JacoLuna.myBatysDAO.PlaneMapper;
import labaSolvd.JacoLuna.myBatysDAO.ReviewMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionFactoryBuilder {

    private static SqlSessionFactory sqlSessionFactory;

    static {
        String resource = "myBatis\\mybatis-config.xml";
        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error building SqlSessionFactory from " + resource, e);
        }
    }
    public static SqlSessionFactory getSqlSessionFactory() {
//        if (sqlSessionFactory != null) {
//            return sqlSessionFactory;
//        }
        DataSource dataSource = new PooledDataSource(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/Airportdb",
                "root",
                "JacoMarce2317"
        );

        Environment environment = new Environment("Development", new JdbcTransactionFactory(), dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(PlaneMapper.class);
        configuration.addMapper(ReviewMapper.class);
        configuration.addMapper(PeopleMapper.class);
        configuration.addMapper(CrewMemberMapper.class);

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        return sqlSessionFactory;
    }
}
