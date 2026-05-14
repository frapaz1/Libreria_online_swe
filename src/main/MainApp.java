package main;

import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.sql.Connection;

import orm.ConnectionManager;
import domain.Utente;
import view.HomeView;

public class MainApp {
    public static void main(String[] args) {
        
        // Inizializziamo la connessione a PostgreSQL
        Connection connection = ConnectionManager.getInstance().getConnection();
        
        if (connection == null) {
            JOptionPane.showMessageDialog(null, 
                "Impossibile connettersi al database. Verifica che PostgreSQL sia avviato.", 
                "Errore di Connessione", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Creiamo un Utente fittizio
        Utente utenteLoggato = new Utente(1, "mario.rossi", "password123", "Cliente");

        // Avviamo la vera interfaccia principale (HomeView)
        SwingUtilities.invokeLater(() -> {
            HomeView frame = new HomeView(utenteLoggato);
            frame.setVisible(true);
        });
    }
}