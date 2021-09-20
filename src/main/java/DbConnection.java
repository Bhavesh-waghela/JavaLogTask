import models.Event;

import java.sql.*;

public class DbConnection {

    private static final String tableName = "Events";

    private static Connection connection;
    public DbConnection() throws SQLException {
        String connectionString = "jdbc:hsqldb:file:hsqldb/logs";
        connection = DriverManager.getConnection(connectionString, "SA", "");
    }

    public void createEventsTable() throws SQLException {
        String createEvents = "CREATE TABLE IF NOT EXISTS Events (id VARCHAR(50) NOT NULL, duration FLOAT NOT NULL, " +
                "type VARCHAR(50), host VARCHAR(50), alert BOOLEAN NOT NULL)";
        connection.createStatement().executeUpdate(createEvents);
    }

    void writeEvent(Event event) throws SQLException {
        String addEvent = "INSERT INTO " + tableName + " (id, duration, type, host, alert)  VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(addEvent);
        preparedStatement.setString(1, event.getId());
        preparedStatement.setFloat(2, event.getDuration());
        preparedStatement.setString(3, event.getType());
        preparedStatement.setString(4, event.getHost());
        preparedStatement.setBoolean(5, event.isAlert());

        preparedStatement.executeUpdate();
    }

    public void closeDatabase() throws SQLException {
        connection.close();
    }

    public void selectAll() throws SQLException {
        String getAll = "SELECT * FROM " + tableName;
        ResultSet resultSet = connection.createStatement().executeQuery(getAll);

        while (resultSet.next()) {
            if (resultSet.getBoolean(5)) {
                System.out.println("Alert for EventID <" + resultSet.getString(1) + ">");
            }
        }
    }

    public void deleteAll() throws SQLException {
        String deleteAll = "DELETE FROM " + tableName;
        connection.createStatement().executeUpdate(deleteAll);
    }
}
