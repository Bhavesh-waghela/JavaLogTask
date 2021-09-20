import models.Event;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.*;

public class DbConnectionTest {

    @InjectMocks
    private DbConnection dbConnection;
    @Mock
    private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createEventsTable() throws SQLException {
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockConnection.createStatement().executeUpdate(Mockito.anyString())).thenReturn(1);
        dbConnection.createEventsTable();
        Mockito.verify(mockConnection.createStatement(), Mockito.calls(1));
    }

    @Test
    public void writeEvent() throws SQLException {
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
        Mockito.when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Event event = new Event.Builder("test", 2, false)
                .withHost(null)
                .withType(null)
                .build();
        dbConnection.writeEvent(event);
        Mockito.verify(mockConnection.prepareStatement(Mockito.anyString()), Mockito.calls(1));
    }

    @Test
    public void selectAll() throws SQLException {
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeQuery(Mockito.anyString())).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getBoolean(Mockito.anyString())).thenReturn(true);
        dbConnection.selectAll();
        Mockito.verify(mockConnection.createStatement(), Mockito.times(1));
    }

    @Test
    public void deleteAll() throws SQLException {
        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeUpdate(Mockito.anyString())).thenReturn(1);
        dbConnection.deleteAll();
        Mockito.verify(mockConnection.createStatement(), Mockito.times(1));
    }

    @Test
    public void closeDatabase() throws SQLException {
        dbConnection.closeDatabase();
        Mockito.verify(mockConnection, Mockito.calls(1));
    }
}