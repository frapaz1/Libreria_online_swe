package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import domain.Libro;
import domain.Utente;
import orm.LibroDAO;
import orm.ConnectionManager;
import businesslogic.AcquistoController;
import java.sql.Connection;

public class CatalogoLibriView extends JFrame {
    
    private AcquistoController controller;
    private Utente utenteLoggato;
    private List<Libro> carrelloLocale; 
    
    private LibroDAO libroDAO;
    private Libro libroEsempio;
    private JLabel giacenzaLabel;
    private JButton btnVaiAlCarrello;
    
    // Il costruttore accetta il controller e l'utente passati dal MainApp
    public CatalogoLibriView(AcquistoController controller, Utente utenteLoggato) {
        this.controller = controller;
        this.utenteLoggato = utenteLoggato;
        this.carrelloLocale = new ArrayList<>(); // Inizializziamo il carrello vuoto

        setTitle("Libreria Online - Progetto SWE");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());

        // Setup DAO per leggere il libro
        Connection conn = ConnectionManager.getInstance().getConnection();
        this.libroDAO = new LibroDAO(conn);
        this.libroEsempio = libroDAO.getLibroById(1); 

        // --- PANNELLO CENTRALE (Dettagli Libro) ---
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
            
            giacenzaLabel = new JLabel("Disponibili in magazzino: " + libroEsempio.getQuantitaMagazzino() + " copie");
            giacenzaLabel.setFont(new Font("Arial", Font.BOLD, 12));
            giacenzaLabel.setForeground(Color.BLUE);
            panelCentrale.add(giacenzaLabel);
        } else {
            panelCentrale.add(new JLabel("Nessun libro trovato nel database con ID 1."));
        }

        // --- PANNELLO SUD (Bottoni Azione) ---
        JPanel panelSud = new JPanel(new FlowLayout());

        JButton btnAcquista = new JButton("Aggiungi al Carrello");
        btnAcquista.addActionListener(e -> {
            if (libroEsempio != null) {
                boolean successo = controller.aggiungiAlCarrello(libroEsempio.getId(), 1);
                
                if (successo) {
                    carrelloLocale.add(libroEsempio); // Salviamo il libro nel carrello della sessione
                    aggiornaInterfaccia();            // Aggiorniamo le scorte e il bottone
                    JOptionPane.showMessageDialog(this, "Aggiunto al carrello con successo!");
                } else {
                    JOptionPane.showMessageDialog(this, "Errore: Scorte esaurite!", "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnVaiAlCarrello = new JButton("🛒 Vai al Carrello (0)");
        btnVaiAlCarrello.addActionListener(e -> {
            CarrelloView carrelloDialog = new CarrelloView(this, carrelloLocale, controller, utenteLoggato, this::aggiornaInterfaccia);
            carrelloDialog.setVisible(true);
        });

        panelSud.add(btnAcquista);
        panelSud.add(btnVaiAlCarrello);

        add(new JLabel(" Benvenuto: " + utenteLoggato.getUsername() + " ", SwingConstants.CENTER), BorderLayout.NORTH);
        add(panelCentrale, BorderLayout.CENTER);
        add(panelSud, BorderLayout.SOUTH);
    }

    //Metodo che viene richiamato per ricaricare i dati grafici.
    // Viene usato sia quando aggiungi un libro, sia dalla CarrelloView (quando rimuovi o paghi).

    private void aggiornaInterfaccia() {
        if (libroEsempio != null) {
            // Ricarichiamo il libro dal DB per avere la giacenza corretta
            libroEsempio = libroDAO.getLibroById(libroEsempio.getId());
            giacenzaLabel.setText("Disponibili in magazzino: " + libroEsempio.getQuantitaMagazzino() + " copie");
        }
        // Aggiorniamo il contatore del carrello
        btnVaiAlCarrello.setText("🛒 Vai al Carrello (" + carrelloLocale.size() + ")");
    }
}