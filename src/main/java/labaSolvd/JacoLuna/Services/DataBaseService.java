package labaSolvd.JacoLuna.Services;

import labaSolvd.JacoLuna.AppProperties;

import java.sql.*;

public class DataBaseService{
    private static final String URL = AppProperties.URL;
    private static final String USERNAME = AppProperties.USERNAME;
    private static final String PASSWORD = AppProperties.PASSWORD;

    /*
    * query['statement'] -> select, insert, update, delete
    * query['fields'] -> List<String> fields
    * query['table']*/

    /*
    public void query(HashMap<String, String> query){
        String sql;
        try(Connection con = DriverManager.getConnection(url, userName, password)){
            switch (query.get("statement")) {
                case "select":
                    sql = select(query);
                    break;
                case "insert":
                    sql = insert(query);
                break;
                default: sql = "";
                break;
            }

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Utils.CONSOLE.info(rs.getString(2));
            }
        }catch(SQLException e){
            Utils.CONSOLE_ERROR.error(e);
        }
    }
    public String select(HashMap<String, String> query){
        StringBuilder sql = new StringBuilder(query.get("statement"));
        sql.append(" ").append(query.get("fields")).append(" FROM ").append( query.get("table"));
        return sql.toString();
    }

    public String insert(HashMap<String, String> query){

        StringBuilder sql = new StringBuilder(query.get("statement"));
        sql.append(" ").append(query.get("table")).append( query.get("fields") ).append(" VALUES ");
        return sql.toString();

        return "";
    }
    */

    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }catch (SQLException e){
            throw new RuntimeException("Unable to connect to database.", e);
        }
    }

  /*  public ResultSet select(HashMap<String, String> query){
        ResultSet rs = null;
        try(Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD)){

            StringBuilder sql = new StringBuilder(query.get("statement"));
            sql.append(" ").append(query.get("fields")).append(" FROM ").append( query.get("table"));
            PreparedStatement ps = con.prepareStatement(sql.toString());
            rs = ps.executeQuery();

        }catch(SQLException e){
            Utils.CONSOLE_ERROR.error(e);
        }
        return rs;
    }*/

}

