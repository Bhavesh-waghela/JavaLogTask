import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EventReader {

    public static void main(String[] args) {
        String file = "logfile.txt";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));

            LogParser logParser = new LogParser();
            DbConnection dbConnection = new DbConnection();
            logParser.parseLogs(reader, dbConnection);

            dbConnection.selectAll();
            dbConnection.deleteAll();
            dbConnection.closeDatabase();
        } catch (IOException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
