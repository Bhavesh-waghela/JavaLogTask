import com.google.gson.Gson;
import models.Event;
import models.ServerLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class LogParser {
    public void parseLogs(BufferedReader reader, DbConnection dbConnection) throws IOException, SQLException {
        HashMap<String, ServerLog> eventMap = new HashMap<>();
        Gson gson = new Gson();
        String line;
        while ((line = reader.readLine()) != null) {
            ServerLog log = gson.fromJson(line, ServerLog.class);
            String eventId = log.getId();
            if (!eventMap.containsKey(eventId)) {
                eventMap.put(eventId, log);
                continue;
            }

            ServerLog previousLog = eventMap.remove(eventId);
            long duration = Math.abs(log.getTimeStamp() - previousLog.getTimeStamp());
            boolean alert = false;
            if (duration > 4) {
                alert = true;
            }

            Event event = new Event.Builder(eventId, duration, alert)
                    .withHost(log.getHost())
                    .withType(log.getType())
                    .build();
            System.out.println(gson.toJson(event).toString());
            dbConnection.writeEvent(event);
        }
    }
}
