package labaSolvd.JacoLuna.Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReusableConnection implements AutoCloseable {
    private final Connection connection;
    private final ConnectionPool pool;

    public ReusableConnection(Connection connection, ConnectionPool pool) {
        this.connection = connection;
        this.pool = pool;
    }
    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }
    public PreparedStatement prepareStatement(String query, int autoGeneratedKeys) throws SQLException {
        return connection.prepareStatement(query, autoGeneratedKeys);
    }
    @Override
    public void close() throws Exception {
        pool.releaseConnection(connection);
    }
}
