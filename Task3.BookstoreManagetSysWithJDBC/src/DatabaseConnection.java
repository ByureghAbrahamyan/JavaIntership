import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            String address = "jdbc:postgres:/localhost:5432/bookstoredb\"";
            String username = "postgres";
            String password = "postgres";
            this.connection = DriverManager.getConnection(address, username, password);
            // DatabaseConnection.connection = DriverManager.getConnection(address, username, password);
            System.out.println("Connected succesfully");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Database Connection Creation Failed : " + e.getMessage());
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public static DatabaseConnection getInstance() {
        if(instance == null) {
            instance = new DatabaseConnection();
        } else {
            try {
                if(instance.getConnection().isClosed()) {
                    instance = new DatabaseConnection();
                }
            } catch (SQLException e){
                throw new RuntimeException();
            }
        }
        return instance;
    }
}
