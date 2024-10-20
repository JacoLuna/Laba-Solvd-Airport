package labaSolvd.JacoLuna.Connection;

import labaSolvd.JacoLuna.Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {

    private static final int INITIAL_CAPACITY = 10;
    private final BlockingQueue<Connection> connections;

    private ConnectionPool() {
        this.connections = new ArrayBlockingQueue<>(INITIAL_CAPACITY);
        initializePool();
    }

    private void initializePool() {
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            try {
                Connection connection = DataBaseConnection.getConnection();
                connections.offer(connection);
            }catch (SQLException e) {
                Utils.CONSOLE_ERROR.error("Error in initializing pool");
            }
        }
    }
    public void releaseConnection(Connection connection) {
        connections.offer(connection);
    }

    public ReusableConnection getConnection() {
        try {
            Connection connection = connections.take();
            return new ReusableConnection(connection, this);
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error getting connection", e);
        }
    }

    private static final class InstanceHolder {
        private static final ConnectionPool INSTANCE = new ConnectionPool();
    }

    public static ConnectionPool getInstance() { return InstanceHolder.INSTANCE; }
}
