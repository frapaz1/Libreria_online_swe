package orm;

import domain.Ordine;
import domain.Libro;
import domain.Spedizione;
import java.sql.*;
import java.util.List;

public class OrdineDAO {
    private Connection connection;

    public OrdineDAO(Connection connection) {
        this.connection = connection;
    }

    //Salva l'intero ordine, le sue righe e la spedizione nel database.
    public boolean salvaOrdineCompleto(Ordine ordine, List<Libro> carrello) {
        String sqlOrdine = "INSERT INTO ordine (utente_id, totale, stato) VALUES (?, ?, ?) RETURNING id";
        String sqlLinea = "INSERT INTO linea_ordine (ordine_id, libro_id, quantita) VALUES (?, ?, ?)";
        String sqlSpedizione = "INSERT INTO spedizione (ordine_id, corriere, tracking, indirizzo_consegna) VALUES (?, ?, ?, ?)";

        try {
            // Disabilitiamo l'autocommit per gestire la transazione manualmente
            connection.setAutoCommit(false);

            //  Inserimento dell'Ordine e recupero dell'ID generato
            int ordineId = -1;
            try (PreparedStatement psOrdine = connection.prepareStatement(sqlOrdine)) {
                psOrdine.setInt(1, ordine.getUtenteId());
                psOrdine.setDouble(2, ordine.getTotale());
                psOrdine.setString(3, ordine.getStato());
                
                ResultSet rs = psOrdine.executeQuery();
                if (rs.next()) {
                    ordineId = rs.getInt(1);
                    ordine.setId(ordineId);
                }
            }

            // Inserimento di ogni libro del carrello in linea_ordine
            try (PreparedStatement psLinea = connection.prepareStatement(sqlLinea)) {
                for (Libro libro : carrello) {
                    psLinea.setInt(1, ordineId);
                    psLinea.setInt(2, libro.getId());
                    psLinea.setInt(3, 1); 
                    psLinea.addBatch(); 
                }
                psLinea.executeBatch();
            }

            // Inserimento della Spedizione
            Spedizione s = ordine.getSpedizione();
            if (s != null) {
                try (PreparedStatement psSped = connection.prepareStatement(sqlSpedizione)) {
                    psSped.setInt(1, ordineId);
                    psSped.setString(2, s.getCorriere());
                    psSped.setString(3, s.getTrackingNumber());
                    psSped.setString(4, s.getIndirizzoConsegna());
                    psSped.executeUpdate();
                }
            }

            // Se tutto è andato bene, confermiamo le modifiche
            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback(); // In caso di errore, annulliamo tutto
                System.err.println("Errore transazione: Rollback eseguito. " + e.getMessage());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public List<Ordine> getOrdiniByUtente(int utenteId) {
        List<Ordine> ordini = new java.util.ArrayList<>();
        String sql = "SELECT o.*, s.corriere, s.tracking, s.indirizzo_consegna " +
                     "FROM ordine o LEFT JOIN spedizione s ON o.id = s.ordine_id " +
                     "WHERE o.utente_id = ? ORDER BY o.data_ordine DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, utenteId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ordine o = new Ordine();
                o.setId(rs.getInt("id"));
                o.setTotale(rs.getDouble("totale"));
                o.setStato(rs.getString("stato"));
                o.setDataOrdine(rs.getTimestamp("data_ordine"));

                // Se esiste una spedizione associata, la carichiamo
                if (rs.getString("tracking") != null) {
                    domain.Spedizione s = new domain.Spedizione();
                    s.setCorriere(rs.getString("corriere"));
                    s.setTrackingNumber(rs.getString("tracking"));
                    s.setIndirizzoConsegna(rs.getString("indirizzo_consegna"));
                    o.setSpedizione(s);
                }
                ordini.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordini;
    }
}