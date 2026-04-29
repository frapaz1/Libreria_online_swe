package orm;

import domain.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    
    private Connection connection;

    // Costruttore a cui passiamo la connessione aperta
    public LibroDAO(Connection connection) {
        this.connection = connection;
    }

    // Metodo per recuperare un singolo libro dal DB tramite ID
    public Libro getLibroById(int id) {
        Libro libro = null;
        String query = "SELECT * FROM Libro WHERE id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    libro = new Libro(
                        resultSet.getInt("id"),
                        resultSet.getString("isbn"),
                        resultSet.getString("titolo"),
                        resultSet.getString("autore"),
                        resultSet.getString("editore"),
                        resultSet.getDouble("prezzo"),
                        resultSet.getInt("quantita_magazzino")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero del libro: " + e.getMessage());
        }
        return libro;
    }

    // Metodo per recuperare TUTTI i libri 
    public List<Libro> getAllLibri() {
        List<Libro> libri = new ArrayList<>();
        String query = "SELECT * FROM Libro ORDER BY titolo ASC";
        
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                libri.add(new Libro(
                    resultSet.getInt("id"),
                    resultSet.getString("isbn"),
                    resultSet.getString("titolo"),
                    resultSet.getString("autore"),
                    resultSet.getString("editore"),
                    resultSet.getDouble("prezzo"),
                    resultSet.getInt("quantita_magazzino")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero della lista libri: " + e.getMessage());
        }
        return libri;
    }

    // Metodo per aggiornare la quantità in magazzino dopo un acquisto
    public boolean updateQuantita(int idLibro, int nuovaQuantita) {
        String query = "UPDATE Libro SET quantita_magazzino = ? WHERE id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, nuovaQuantita);
            statement.setInt(2, idLibro);
            
            // executeUpdate() 
            int righeModificate = statement.executeUpdate();
            return righeModificate > 0;
            
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento della quantità: " + e.getMessage());
            return false;
        }
    }
 // Metodo per la barra di ricerca
    public List<Libro> cercaLibri(String keyword) {
        List<Libro> libri = new ArrayList<>();
        // LOWER per ignorare le maiuscole/minuscole e LIKE per cercare parole parziali
        String query = "SELECT * FROM Libro WHERE LOWER(titolo) LIKE ? OR LOWER(autore) LIKE ? ORDER BY titolo ASC";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // C'è % prima e dopo la parola per cercare ovunque nel testo
            String searchPattern = "%" + keyword.toLowerCase() + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    libri.add(new Libro(
                        resultSet.getInt("id"),
                        resultSet.getString("isbn"),
                        resultSet.getString("titolo"),
                        resultSet.getString("autore"),
                        resultSet.getString("editore"),
                        resultSet.getDouble("prezzo"),
                        resultSet.getInt("quantita_magazzino")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la ricerca: " + e.getMessage());
        }
        return libri;
    }
}