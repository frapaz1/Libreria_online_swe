package main;

import javax.swing.SwingUtilities;
import domain.Utente;

public class MainTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Creiamo un utente di test 
            Utente utenteTest = new Utente(1, "utente.test", "pass", "Cliente");
            
            // Passiamo l'utente alla HomeView
            new view.HomeView(utenteTest).setVisible(true);
        });
    }
}