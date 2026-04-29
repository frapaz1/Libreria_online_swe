package orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    // Usiamo il pattern Singleton: avremo una sola connessione in tutto il programma
    private static ConnectionManager instance = null;
    private Connection connection = null;

    private final String URL = "jdbc:postgresql://localhost:5432/libreria";
    private final String USER = "postgres"; 
    private final String PASSWORD = "ComicProject"; // Password di pgAdmin

    // Costruttore privato (nessun altro può creare un'istanza)
    private ConnectionManager() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connessione al database stabilita con successo!");
        } catch (SQLException e) {
            System.err.println("Errore di connessione al DB: " + e.getMessage());
        }
    }

    // Metodo per ottenere l'unica istanza di questa classe
    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    // Metodo per farsi dare la connessione (lo useranno i DAO)
    public Connection getConnection() {
        return connection;
    }
}