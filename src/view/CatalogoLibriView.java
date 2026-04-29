package view;

import javax.swing.*;
import java.awt.*;
import domain.Libro;
import orm.LibroDAO;
import orm.ConnectionManager;
import businesslogic.AcquistoController;
import java.sql.Connection;

public class CatalogoLibriView extends JFrame {
    
    private JLabel giacenzaLabel;
    
    public CatalogoLibriView() {
        setTitle("Libreria Online - Progetto SWE");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());

        Connection conn = ConnectionManager.getInstance().getConnection();
        LibroDAO libroDAO = new LibroDAO(conn);
        Libro libroEsempio = libroDAO.getLibroById(1); 

        JPanel panelCentrale = new JPanel();
        panelCentrale.setLayout(new BoxLayout(panelCentrale, BoxLayout.Y_AXIS));
        panelCentrale.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        if (libroEsempio != null) {
            panelCentrale.add(new JLabel("Titolo: " + libroEsempio.getTitolo()));
            panelCentrale.add(Box.createRigidArea(new Dimension(0, 10)));
            panelCentrale.add(new JLabel("Autore: " + libroEsempio.getAutore()));
            panelCentrale.add(Box.createRigidArea(new Dimension(0, 10)));
            panelCentrale.add(new JLabel("Prezzo: €" + String.format("%.2f", libroEsempio.getPrezzo())));
            panelCentrale.add(Box.createRigidArea(new Dimension(0, 10)));
            
            // Inizializziamo l'etichetta che cambierà
            giacenzaLabel = new JLabel("Disponibili in magazzino: " + libroEsempio.getQuantitaMagazzino() + " copie");
            giacenzaLabel.setFont(new Font("Arial", Font.BOLD, 12));
            giacenzaLabel.setForeground(Color.BLUE);
            panelCentrale.add(giacenzaLabel);
        }

        JButton btnAcquista = new JButton("Aggiungi al Carrello (Compri 1 copia)");
        
        btnAcquista.addActionListener(e -> {
            if (libroEsempio != null) {
                AcquistoController ctrl = new AcquistoController(conn);
                boolean successo = ctrl.aggiungiAlCarrello(libroEsempio.getId(), 1);
                
                if (successo) {
                    // Ricarichiamo il libro dal DB per avere la giacenza aggiornata!
                    Libro libroAggiornato = libroDAO.getLibroById(1);
                    giacenzaLabel.setText("Disponibili in magazzino: " + libroAggiornato.getQuantitaMagazzino() + " copie");
                    JOptionPane.showMessageDialog(this, "Acquisto confermato! Copie scalate dal magazzino.");
                } else {
                    JOptionPane.showMessageDialog(this, "Errore: Scorte esaurite!", "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        add(new JLabel(" Benvenuto nella nostra Libreria! ", SwingConstants.CENTER), BorderLayout.NORTH);
        add(panelCentrale, BorderLayout.CENTER);
        add(btnAcquista, BorderLayout.SOUTH);
    }
}