package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import domain.Libro;
import domain.Utente;
import domain.Ordine;
import businesslogic.AcquistoController;
import businesslogic.ScontoStrategy;
import businesslogic.ScontoFedelta;
import businesslogic.ScontoNullo;

public class CarrelloView extends JDialog {
    private DefaultListModel<String> listModel;
    private JList<String> displayList;
    private List<Libro> carrello;
    private AcquistoController controller;
    private Utente utenteLoggato; // AGGIUNTO: L'utente che sta facendo l'acquisto
    private Runnable onUpdate; 
    private JCheckBox checkStudente; 

    public CarrelloView(JFrame parent, List<Libro> carrello, AcquistoController controller, Utente utenteLoggato, Runnable onUpdate) {
        super(parent, "Il Tuo Carrello", true); 
        this.carrello = carrello;
        this.controller = controller;
        this.utenteLoggato = utenteLoggato;
        this.onUpdate = onUpdate;

        setSize(500, 550); 
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Lista grafica
        listModel = new DefaultListModel<>();
        displayList = new JList<>(listModel);
        add(new JScrollPane(displayList), BorderLayout.CENTER);

        // Selezione dello sconto
        JPanel panelSconto = new JPanel();
        checkStudente = new JCheckBox("Applica Sconto Studente (10%)");
        checkStudente.addActionListener(e -> aggiornaListaUI()); 
        panelSconto.add(checkStudente);
        add(panelSconto, BorderLayout.NORTH);

        // Pannello Azioni 
        JPanel panelAzioni = new JPanel(new GridLayout(3, 1, 5, 5));
        panelAzioni.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Bottone Pagamento 
        JButton btnPaga = new JButton("💳 Procedi al Pagamento");
        btnPaga.setBackground(new Color(46, 204, 113)); 
        btnPaga.setForeground(Color.WHITE);
        btnPaga.setFont(new Font("Arial", Font.BOLD, 14));
        btnPaga.addActionListener(e -> effettuaPagamento());

        // Bottone Rimuovi
        JButton btnRimuovi = new JButton("🗑 Rimuovi Elemento Selezionato");
        btnRimuovi.addActionListener(e -> rimuoviElemento());
        
        // Bottone Chiudi
        JButton btnChiudi = new JButton("Torna allo Shopping");
        btnChiudi.addActionListener(e -> dispose());

        panelAzioni.add(btnPaga);
        panelAzioni.add(btnRimuovi);
        panelAzioni.add(btnChiudi);
        add(panelAzioni, BorderLayout.SOUTH);

        aggiornaListaUI();
    }

    private void aggiornaListaUI() {
        listModel.clear();
        double totaleLordo = 0;
        for (Libro l : carrello) {
            listModel.addElement(l.getTitolo() + " - €" + String.format("%.2f", l.getPrezzo()));
            totaleLordo += l.getPrezzo();
        }

        ScontoStrategy strategia;
        if (checkStudente.isSelected()) {
            strategia = new ScontoFedelta();
        } else {
            strategia = new ScontoNullo();
        }

        double totaleScontato = controller.calcolaTotaleOrdine(totaleLordo, strategia);
        setTitle("Carrello - Totale: €" + String.format("%.2f", totaleScontato));
    }

    private void rimuoviElemento() {
        int index = displayList.getSelectedIndex();
        if (index != -1) {
            Libro libroDaRimuovere = carrello.get(index);
            
            if (controller.rimuoviDalCarrello(libroDaRimuovere)) {
                carrello.remove(index);
                aggiornaListaUI();
                onUpdate.run(); 
                JOptionPane.showMessageDialog(this, "Libro rimosso e tornato disponibile in magazzino.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un libro da rimuovere.");
        }
    }

    // --- METODO PER IL PAGAMENTO CON SALVATAGGIO NEL DB ---
    private void effettuaPagamento() {
        if (carrello.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Il carrello è vuoto! Aggiungi dei libri prima di pagare.");
            return;
        }

        // Chiediamo l'indirizzo di spedizione tramite pop-up
        String indirizzo = JOptionPane.showInputDialog(this, 
            "Inserisci l'indirizzo di spedizione per procedere:", 
            "Dati di Spedizione", 
            JOptionPane.QUESTION_MESSAGE);

        // Se l'utente ha inserito qualcosa e non ha premuto "Annulla"
        if (indirizzo != null && !indirizzo.trim().isEmpty()) {
            
            // Definiamo la strategia
            ScontoStrategy strategia = checkStudente.isSelected() ? new ScontoFedelta() : new ScontoNullo();
            
            //  CHIAMIAMO IL CONTROLLER PER SALVARE FISICAMENTE NEL DATABASE
            Ordine ordineSalvato = controller.effettuaCheckout(carrello, utenteLoggato, strategia, indirizzo);

            // Verifichiamo il risultato
            if (ordineSalvato != null) {
                JOptionPane.showMessageDialog(this, 
                    "Pagamento completato con successo!\n" +
                    "ID Ordine: " + ordineSalvato.getId() + "\n" +
                    "Totale pagato: €" + String.format("%.2f", ordineSalvato.getTotale()) + "\n" +
                    "Spedizione verso: " + indirizzo, 
                    "Checkout Completato", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                carrello.clear();
                onUpdate.run(); // Aggiorna la home
                dispose();      // Chiude il carrello
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Errore critico durante il salvataggio dell'ordine nel database.", 
                    "Errore di Sistema", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Indirizzo non valido. Transazione annullata.", "Checkout Annullato", JOptionPane.WARNING_MESSAGE);
        }
    }
}