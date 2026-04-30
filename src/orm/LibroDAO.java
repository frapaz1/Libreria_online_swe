package orm;

import domain.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

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
    // Aggiunge un libro alla tabella dei preferiti.
      public boolean aggiungiPreferito(int idLibro) {
        String query = "INSERT INTO preferiti (id_libro) VALUES (?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idLibro);
            int righeInserite = pstmt.executeUpdate();
            return righeInserite > 0;
        } catch (Exception e) {
            System.out.println("Libro già nei preferiti o errore DB: " + e.getMessage());
            return false;
        }
    }

    //Rimuove un libro dai preferiti.
    public boolean rimuoviPreferito(int idLibro) {
        String query = "DELETE FROM preferiti WHERE id_libro = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idLibro);
            int righeEliminate = pstmt.executeUpdate();
            return righeEliminate > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Controlla se un libro è già nei preferiti.
    public boolean isPreferito(int idLibro) {
        String query = "SELECT 1 FROM preferiti WHERE id_libro = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idLibro);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Ritorna true se trova almeno un record
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Recupera la lista di tutti i libri salvati nei preferiti.
     */
    public List<Libro> getLibriPreferiti() {
        List<Libro> preferiti = new ArrayList<>();
        // Facciamo una JOIN tra la tabella libro e la tabella preferiti
        String query = "SELECT l.* FROM libro l INNER JOIN preferiti p ON l.id = p.id_libro";
        
        try (java.sql.Statement stmt = connection.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
            	Libro libro = new Libro(
                        rs.getInt("id"), 
                        rs.getString("isbn"),      
                        rs.getString("titolo"), 
                        rs.getString("autore"), 
                        rs.getString("editore"),   
                        rs.getDouble("prezzo"), 
                        rs.getInt("quantita_magazzino")       
                    );
                preferiti.add(libro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preferiti;
    }
}