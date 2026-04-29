package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import domain.Libro;
import orm.LibroDAO;
import orm.ConnectionManager;
import businesslogic.AcquistoController;
import java.sql.Connection;

public class HomeView extends JFrame {
    
    private JPanel gridPanel;
    private LibroDAO libroDAO;
    private Connection conn;
    private List<Libro> carrelloSessione = new ArrayList<>();
    private JButton btnCarrello;

    public HomeView() {
        conn = ConnectionManager.getInstance().getConnection();
        libroDAO = new LibroDAO(conn);

        setTitle("Libreria Online - Home Page");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // HEADER E RICERCA 
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(45, 52, 54));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("LA MIA LIBRERIA ONLINE");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JTextField searchBar = new JTextField("Cerca titolo o autore...", 20);
        searchBar.setForeground(Color.GRAY);
        
        // Pulizia del testo al click
        searchBar.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchBar.getText().equals("Cerca titolo o autore...")) {
                    searchBar.setText("");
                    searchBar.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (searchBar.getText().trim().isEmpty()) {
                    searchBar.setForeground(Color.GRAY);
                    searchBar.setText("Cerca titolo o autore...");
                }
            }
        });

        // Tasto INVIO per cercare
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String testo = searchBar.getText().trim();
                    if (testo.isEmpty() || testo.equals("Cerca titolo o autore...")) {
                        refreshCatalogo(""); // Stringa vuota = ricarica tutto
                    } else {
                        refreshCatalogo(testo); // Cerca la parola chiave
                    }
                }
            }
        });

        btnCarrello = new JButton("🛒 Carrello (0)");
        btnCarrello.setBackground(new Color(241, 196, 15));
        btnCarrello.setFont(new Font("Arial", Font.BOLD, 14));
        btnCarrello.addActionListener(e -> mostraRiepilogoCarrello());

        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        eastPanel.setOpaque(false); 
        eastPanel.add(searchBar);
        eastPanel.add(btnCarrello);
        
        headerPanel.add(eastPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // GRIGLIA DEI LIBRI 
        gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        refreshCatalogo(""); // Inizialmente carica tutti i libri

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    //  refreshCatalogo ora accetta un termine di ricerca 
    private void refreshCatalogo(String ricerca) {
        gridPanel.removeAll();
        
        List<Libro> libri;
        if (ricerca.isEmpty()) {
            libri = libroDAO.getAllLibri(); // Tutti
        } else {
            libri = libroDAO.cercaLibri(ricerca); // Solo quelli filtrati
        }

        if (libri.isEmpty()) {
            gridPanel.add(new JLabel("Nessun libro trovato per: '" + ricerca + "'"));
        } else {
            for (Libro l : libri) {
                gridPanel.add(creaSchedaLibro(l));
            }
        }
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel creaSchedaLibro(Libro libro) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);

        JLabel titolo = new JLabel(libro.getTitolo());
        titolo.setFont(new Font("Arial", Font.BOLD, 14));
        titolo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel autore = new JLabel(libro.getAutore());
        autore.setFont(new Font("Arial", Font.ITALIC, 12));
        autore.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel prezzo = new JLabel("€" + String.format("%.2f", libro.getPrezzo()));
        prezzo.setForeground(new Color(46, 204, 113));
        prezzo.setFont(new Font("Arial", Font.BOLD, 16));
        prezzo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Due bottoni, uno per i dettagli, uno per il carrello
        JPanel panelBottoni = new JPanel(new FlowLayout());
        panelBottoni.setBackground(Color.WHITE);

        JButton btnDettagli = new JButton("📖 Info");
        btnDettagli.addActionListener(e -> mostraDettaglioLibro(libro));

        JButton btnAcquista = new JButton("🛒 +");
        btnAcquista.addActionListener(e -> {
            AcquistoController ctrl = new AcquistoController(conn);
            if (ctrl.aggiungiAlCarrello(libro.getId(), 1)) {
                carrelloSessione.add(libro); 
                btnCarrello.setText("🛒 Carrello (" + carrelloSessione.size() + ")");
                refreshCatalogo(""); // Ricarica le giacenze a video
            } else {
                JOptionPane.showMessageDialog(this, "Scorte esaurite!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelBottoni.add(btnDettagli);
        panelBottoni.add(btnAcquista);

        card.add(titolo);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(autore);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(prezzo);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(panelBottoni);

        return card;
    }

    // Finestra popup con i dettagli completi del libro
    private void mostraDettaglioLibro(Libro libro) {
        JDialog dialog = new JDialog(this, "Dettagli: " + libro.getTitolo(), true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelInfo.add(new JLabel("Titolo: " + libro.getTitolo()));
        panelInfo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelInfo.add(new JLabel("Autore: " + libro.getAutore()));
        panelInfo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelInfo.add(new JLabel("Casa Editrice: " + libro.getEditore()));
        panelInfo.add(Box.createRigidArea(new Dimension(0, 10)));
        panelInfo.add(new JLabel("Codice ISBN: " + libro.getIsbn()));
        panelInfo.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel tramaLabel = new JLabel("<html><i>Un fantastico capolavoro della letteratura che ti terrà incollato fino all'ultima pagina.</i></html>");
        panelInfo.add(tramaLabel);
        
        panelInfo.add(Box.createRigidArea(new Dimension(0, 20)));
        panelInfo.add(new JLabel("Disponibilità in magazzino: " + libro.getQuantitaMagazzino() + " copie"));

        JButton btnChiudi = new JButton("Chiudi");
        btnChiudi.addActionListener(e -> dialog.dispose());

        JPanel pnlSud = new JPanel();
        pnlSud.add(btnChiudi);

        dialog.add(panelInfo, BorderLayout.CENTER);
        dialog.add(pnlSud, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void mostraRiepilogoCarrello() {
        if (carrelloSessione.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il carrello è vuoto!");
            return;
        }

        // Apriamo la nuova vista professionale
        AcquistoController ctrl = new AcquistoController(conn);
        CarrelloView vistaCarrello = new CarrelloView(this, carrelloSessione, ctrl, () -> refreshCatalogo(""));
        
        vistaCarrello.setVisible(true);
        
        // Aggiorniamo il numeretto sul bottone della Home dopo la chiusura
        btnCarrello.setText("🛒 Carrello (" + carrelloSessione.size() + ")");
    }
}